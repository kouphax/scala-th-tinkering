package se.yobriefca.triggerhappy.http.request

import com.twitter.finagle.http.Request
import se.yobriefca.triggerhappy.data.models.Enrolment

object RequestFields {
  val Enrolment = Request.Schema.newField[Enrolment]()
}
