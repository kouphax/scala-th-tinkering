package se.yobriefca.triggerhappy.http.controllers

import javax.inject.Inject

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.Controller
import se.yobriefca.triggerhappy.data.Db
import se.yobriefca.triggerhappy.http.filters.AuthFilter
import se.yobriefca.triggerhappy.http.request.{RecordRequest, RequestFields}

class RecordController @Inject()(database: Db) extends Controller {
  prefix("/api/v1") {
    filter[AuthFilter].post("/record") { request: RecordRequest =>
      val enrolment = request.original.ctx(RequestFields.Enrolment)
    }
  }
}
