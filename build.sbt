
scalaVersion := "2.11.11"
parallelExecution in ThisBuild := false

lazy val versions = new {
  val finatra = "2.11.0"
  val guice = "4.0"
  val logback = "1.1.7"
}


resolvers ++= Seq(
  Resolver.sonatypeRepo("releases")
)

assemblyMergeStrategy in assembly := {
  case "BUILD" => MergeStrategy.discard
  case "META-INF/io.netty.versions.properties" => MergeStrategy.last
  case other => MergeStrategy.defaultMergeStrategy(other)
}

Revolver.settings

resolvers += "Sonatype release repository" at "https://oss.sonatype.org/content/repositories/releases/"

resolvers += "lightshed-maven" at "http://dl.bintray.com/content/lightshed/maven"

resolvers ++= Seq(
  "Twitter maven" at "http://maven.twttr.com",
  "Finatra Repo" at "http://twitter.github.com/finatra"
)

libraryDependencies += "ch.lightshed" %% "courier" % "0.1.4"


libraryDependencies ++= Seq(
  "com.twitter" %% "finatra-http" % versions.finatra,
  "com.twitter" %% "finatra-httpclient" % versions.finatra,
  "ch.qos.logback" % "logback-classic" % versions.logback,

  "org.mindrot" % "jbcrypt" % "0.4",
  "io.igl" %% "jwt" % "1.2.2",
  "org.sorm-framework" % "sorm" % "0.3.21",
  "com.h2database" % "h2" % "1.4.196",

  "com.twitter" %% "finatra-http" % versions.finatra % "test",
  "com.twitter" %% "finatra-jackson" % versions.finatra % "test",
  "com.twitter" %% "inject-server" % versions.finatra % "test",
  "com.twitter" %% "inject-app" % versions.finatra % "test",
  "com.twitter" %% "inject-core" % versions.finatra % "test",
  "com.twitter" %% "inject-modules" % versions.finatra % "test",
  "com.google.inject.extensions" % "guice-testlib" % versions.guice % "test",

  "com.twitter" %% "finatra-http" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "finatra-jackson" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-server" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-app" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-core" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-modules" % versions.finatra % "test" classifier "tests",

  "org.mockito" % "mockito-core" % "1.9.5" % "test",
  "junit" % "junit" % "4.12" % "test",
  "org.scalacheck" %% "scalacheck" % "1.13.4" % "test",
  "org.scalatest" %% "scalatest" %  "3.0.0" % "test",
  "org.specs2" %% "specs2-mock" % "2.4.17" % "test")