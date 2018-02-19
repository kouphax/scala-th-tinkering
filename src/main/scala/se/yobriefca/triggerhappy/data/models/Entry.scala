package se.yobriefca.triggerhappy.data.models

import org.joda.time.DateTime

case class Geolocation(lat: BigDecimal, lng: BigDecimal)

case class Entry(user: User, value: Int, triggers: Set[String], emotions: Set[String], location: Geolocation, notes: String, recorded: DateTime)
