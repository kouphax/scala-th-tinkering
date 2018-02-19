package se.yobriefca.triggerhappy.http.request

case class ConfirmEnrolmentRequest(userId: String, deviceId: String, token: String)
