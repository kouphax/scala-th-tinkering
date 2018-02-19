package se.yobriefca.triggerhappy.http.request

import com.twitter.finagle.http.Request
import org.joda.time.DateTime
import se.yobriefca.triggerhappy.data.models.Geolocation

case class RecordRequest(value: Int, triggers: Set[String], emotions: Set[String], location: Geolocation, notes: String, recorded: DateTime, original: Request)
