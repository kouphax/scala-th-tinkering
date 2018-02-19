package se.yobriefca.triggerhappy.data

import java.util.UUID

import se.yobriefca.triggerhappy.data.models._
import sorm.{Entity, Instance}

class Db(url: String) extends Instance(
  entities = Set(
    Entity[Enrolment]       (unique = Set() + Seq("deviceId")),
    Entity[User]            (unique = Set() + Seq("publicId") + Seq("emailAddress")),
    Entity[EnrolmentRequest](unique = Set() + Seq("deviceId")),
    Entity[Token]           (unique = Set() + Seq("token")),
    Entity[Entry]           (),
    Entity[Geolocation]     ()
  ),
  url = url
) {
  def seedTokens(total: Int = 100): Unit = {
    for (_ <- 1 to total) yield {
      save(Token(UUID.randomUUID().toString))
    }
  }
}
