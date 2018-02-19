package se.yobriefca.triggerhappy.data.models

import org.joda.time.DateTime

case class Enrolment(user: User, deviceId: String, description: String, accessToken: String, dateCreated: DateTime = DateTime.now)
