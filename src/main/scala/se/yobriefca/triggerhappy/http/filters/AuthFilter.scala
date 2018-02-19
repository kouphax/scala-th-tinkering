package se.yobriefca.triggerhappy.http.filters

import javax.inject.Inject

import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.inject.annotations.Flag
import com.twitter.util.Future
import io.igl.jwt._
import org.mindrot.jbcrypt.BCrypt
import se.yobriefca.triggerhappy.data.Db
import se.yobriefca.triggerhappy.data.models.Enrolment
import se.yobriefca.triggerhappy.http.request.RequestFields

import scala.util.{Failure, Success}

class AuthFilter @Inject()(@Flag("token.secret") secret: String, database: Db) extends SimpleFilter[Request, Response] with com.twitter.inject.Logging {

  private val unauthorized = Future.value(Response(Status.Unauthorized))
  private val BearerTokenPattern = "Bearer (.+)$".r

  private def validate(token: String) = {
    DecodedJwt.validateEncodedJwt(token, secret, Algorithm.HS256, Set.empty, Set(Aud, Sub), ignoredHeaders = Set(Typ.name))
  }

  override def apply(request: Request, continue: Service[Request, Response]): Future[Response] = {
    request.headerMap.get("Authorization").fold(unauthorized) {
      case BearerTokenPattern(token) =>
        validate(token) match {
          case Success(jwt) =>
            val success = for(
              aud <- jwt.getClaim[Aud];
              sub <- jwt.getClaim[Sub];
              rec <- database.query[Enrolment]
                .whereEqual("user.publicId", aud.value.left.get)
                .whereEqual("deviceId", sub.value)
                .fetchOne() if BCrypt.checkpw(token, rec.accessToken)
            ) yield {
              request.ctx.update(RequestFields.Enrolment, rec)
              continue(request)
            }

            success getOrElse unauthorized
          case Failure(exc) =>
            error("Invalid token", exc)
            unauthorized
        }
      case _ =>
        error("Invalid header")
        unauthorized
    }
  }
}
