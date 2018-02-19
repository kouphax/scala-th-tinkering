package se.yobriefca.triggerhappy.http

import javax.inject.Singleton

import com.google.inject.{Module, Provides}
import com.twitter.finagle.http.{Response, Status}
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.TwitterModule
import com.twitter.inject.server.FeatureTest
import com.twitter.util.Future
import se.yobriefca.triggerhappy.DatabaseModule.url
import se.yobriefca.triggerhappy.data.Db
import se.yobriefca.triggerhappy.data.models.EnrolmentRequest
import se.yobriefca.triggerhappy.http.request.BeginEnrolmentRequest
import se.yobriefca.triggerhappy.services.EmailService

class AuthenticationSpec extends FeatureTest {
  override val server = new EmbeddedHttpServer(new TriggerHappyServer {
      override def overrideModules: Seq[Module] = Seq(new TwitterModule{
        @Singleton
        @Provides
        def providesEmailMock: EmailService = new EmailService {
          override def sendEnrolmentEmail(request: EnrolmentRequest): Future[Unit] = {
            println("aksldjajsdkajlksdjkaljsdkjaskjdkajsdkljalksjdkajskdjaklsjdklajskdjaksljdlkajskdjask")
            super.sendEnrolmentEmail(request)
          }
        }
      })
    },
    flags = Map(
      "token.secret" -> "secret",
      "db.url" -> "jdbc:h2:mem:test"))

  test("TriggerHappyServer#authentication flow") {
    server.httpPost(
      path = "/api/v1/record",
      postBody = "",
      andExpect = Status.Unauthorized)

    val beginEnrolmentResponse = server.httpPost(
      path = "/api/v1/enrol",
      postBody =
        """{
          | "email_address": "james@yobriefca.se",
          | "device_id": "1234567890",
          | "device_description": "integration test"
          |}
        """.stripMargin,
      andExpect = Status.Accepted)

    val extractor = """"publicId" *: *"(.+)"""".r
    val publicId = extractor
      .findFirstMatchIn(beginEnrolmentResponse.contentString)
      .map(_.group(1))
      .getOrElse(fail("No publicId extractable from request"))

    val confirmEnrolmentResponse = server.httpPut(
      path = "/api/v1/enrol",
      putBody =
        s"""{
          | "user_id": "$publicId",
          | "device_id": "1234567890",
          | "token": "___________"
          |}
        """.stripMargin,
      andExpect = Status.Accepted)


  }
}
