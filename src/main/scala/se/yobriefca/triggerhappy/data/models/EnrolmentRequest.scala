package se.yobriefca.triggerhappy.data.models

import org.joda.time.DateTime

/**
  * Created by jamhughes on 16/07/2017.
  */
case class EnrolmentRequest(user: User, deviceId: String, deviceDescription: String, token: String, expires: DateTime = DateTime.now.plusHours(6)) {
  def expired: Boolean = expires.isBeforeNow
}
