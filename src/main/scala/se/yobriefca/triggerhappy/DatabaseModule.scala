package se.yobriefca.triggerhappy

import javax.inject.Singleton

import com.google.inject.Provides
import com.twitter.inject.TwitterModule
import se.yobriefca.triggerhappy.data.Db

object DatabaseModule extends TwitterModule {

  private val url = flag[String]("db.url", "database connection url")

  @Singleton
  @Provides
  def providesDatabase: Db = {
    val database = new Db(url())
    database.seedTokens()
    database
  }
}
