package se.yobriefca.triggerhappy.data.models

import org.joda.time.DateTime

/**
  * Created by jamhughes on 16/07/2017.
  */
case class User(publicId: String, emailAddress: String, dateCreated: DateTime = DateTime.now)
