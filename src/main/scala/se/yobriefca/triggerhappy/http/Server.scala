package se.yobriefca.triggerhappy.http

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{CommonFilters, LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter
import se.yobriefca.triggerhappy.{DatabaseModule}
import se.yobriefca.triggerhappy.http.controllers.{EnrolmentController, RecordController}

object TriggerHappyServerMain extends TriggerHappyServer

class TriggerHappyServer extends HttpServer {

  flag[String]("token.secret", "Secret token used for encoding and decoding JWTs")

  override val modules = Seq(DatabaseModule)

  override def configureHttp(router: HttpRouter) {
    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .add[EnrolmentController]
      .add[RecordController]
  }
}











