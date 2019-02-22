resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies ++= {
  val specs2V = "3.8.8"
  Seq(
    "ch.qos.logback" % "logback-classic" % "1.1.2",
    "org.clapper" %% "grizzled-slf4j" % "1.3.0",
    "com.softwaremill.akka-http-session" %% "jwt" % "0.4.0",
    "com.softwaremill.akka-http-session" %% "core" % "0.4.0",
    "net.codingwell" %% "scala-guice" % "4.0.1",
    "com.github.tamer316" %% "mongo-lib" % "1.0.0",
    "com.github.tamer316" %% "akka-http-lib" % "1.0.0",
    "com.osinka.i18n" %% "scala-i18n" % "1.0.1",
    "org.specs2" % "specs2-core_2.11" % specs2V,
    "org.specs2" % "specs2-mock_2.11" % specs2V,
    "com.github.tamer316" %% "mongo-lib" % "1.0.0" classifier "tests",
    "com.github.tamer316" %% "akka-http-lib" % "1.0.0" classifier "tests"
  )
}

Revolver.settings