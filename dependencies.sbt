resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies ++= {
  val specs2V = "3.8.8"
  val akkaHttpLibV = "1.1.0"
  val akkaHttpSessionV = "0.5.2"
  val akkaHttpCorsV = "0.4.1"
  val mongoLibV = "2.2.2"
  Seq(
    "ch.qos.logback" % "logback-classic" % "1.1.2",
    "org.clapper" %% "grizzled-slf4j" % "1.3.0",
    "com.softwaremill.akka-http-session" %% "jwt" % akkaHttpSessionV,
    "com.softwaremill.akka-http-session" %% "core" % akkaHttpSessionV,
    "ch.megard" %% "akka-http-cors" % akkaHttpCorsV,
    "net.codingwell" %% "scala-guice" % "4.0.1",
    "com.github.tamer316" % "mongo-lib" % mongoLibV,
    "com.github.tamer316" % "akka-http-lib" % akkaHttpLibV,
    "com.osinka.i18n" %% "scala-i18n" % "1.0.1",
    "org.specs2" % "specs2-core_2.11" % specs2V,
    "org.specs2" % "specs2-mock_2.11" % specs2V,
    "com.github.tamer316" % "mongo-lib" % mongoLibV classifier "tests",
    "com.github.tamer316" % "akka-http-lib" % akkaHttpLibV classifier "tests"
  )
}

Revolver.settings
