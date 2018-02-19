package se.yobriefca.triggerhappy.services

import com.twitter.inject.Logging
import com.twitter.util.Future
import se.yobriefca.triggerhappy.data.models.EnrolmentRequest

class EmailService extends Logging {

  def sendEnrolmentEmail(request: EnrolmentRequest): Future[Unit] = {
    info(s"Sending email about request: $request")
    Future.Unit
  }
}
