package se.yobriefca.triggerhappy.http.controllers

import java.util.UUID
import javax.inject.Inject

import com.twitter.finatra.http.Controller
import com.twitter.inject.annotations.Flag
import io.igl.jwt._
import org.mindrot.jbcrypt.BCrypt
import se.yobriefca.triggerhappy.data.Db
import se.yobriefca.triggerhappy.data.models.{Enrolment, EnrolmentRequest, Token, User}
import se.yobriefca.triggerhappy.http.request.{BeginEnrolmentRequest, ConfirmEnrolmentRequest}
import se.yobriefca.triggerhappy.services.EmailService

class EnrolmentController @Inject()(@Flag("token.secret") secret: String, database: Db, emailer: EmailService) extends Controller {

  prefix("/api/v1") {
    post("/enrol") { request: BeginEnrolmentRequest =>
      val user = database.query[User]
        .whereEqual("emailAddress", request.emailAddress)
        .fetchOne()
        .getOrElse {
          database.save(User(UUID.randomUUID().toString, request.emailAddress))
        }

      database.query[EnrolmentRequest]
        .whereEqual("user", user)
        .whereEqual("deviceId", request.deviceId)
        .fetch()
        .foreach(database.delete(_))

      val token = database.query[Token].fetchOne().map { token =>
        database.delete(token)
        token.token
      }

      if(token.isDefined) {
        val enrolmentRequest = database.save(EnrolmentRequest(user, request.deviceId, request.deviceDescription, token.get))
        emailer.sendEnrolmentEmail(enrolmentRequest)
        response.accepted(Map(
          "success" -> true,
          "publicId" -> user.publicId
        ))
      } else {
        response.status(423).json(Map(
          "success" -> false,
          "errors" -> Seq("Enrolments are currently not open")
        ))
      }
    }

    put("/enrol") { request: ConfirmEnrolmentRequest =>
      database.query[EnrolmentRequest]
        .whereEqual("token", request.token)
        .fetchOne()
        .collect {
          case enrolmentRequest if enrolmentRequest.expired =>
            response.gone.json(Map(
              "success" -> false,
              "errors" -> Seq("Token expired")
            ))
          case enrolmentRequest =>
            val jwt = new DecodedJwt(
              Seq(Alg(Algorithm.HS256), Typ("JWT")),
              Seq(Sub(enrolmentRequest.deviceId), Aud(enrolmentRequest.user.publicId)))
            val accessToken = jwt.encodedAndSigned(secret)
            val encryptedAccessToken = BCrypt.hashpw(accessToken, BCrypt.gensalt())

            database.query[Enrolment]
              .whereEqual("user", enrolmentRequest.user)
              .whereEqual("deviceId", enrolmentRequest.deviceId)
              .fetch()
              .foreach(database.delete(_))

            database.save(Enrolment(enrolmentRequest.user, enrolmentRequest.deviceId, enrolmentRequest.deviceDescription, encryptedAccessToken))

            response.ok.json(Map(
              "success" -> true,
              "accessToken" -> accessToken
            ))
        }.getOrElse {
          response.notFound.json(Map(
            "success" -> false,
            "errors" -> Seq("No enrolment request found")
          ))
        }
    }
  }
}
