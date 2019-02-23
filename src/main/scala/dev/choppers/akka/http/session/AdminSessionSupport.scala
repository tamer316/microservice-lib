package dev.choppers.akka.http.session

import com.softwaremill.session._
import dev.choppers.config.HasConfig

trait AdminSessionSupport extends SessionDirectives with HasConfig {

  val sessionConfig = SessionConfig.fromConfig(config, "admin.session")

  implicit val serializer = JValueSessionSerializer.caseClass[AdminSession]
  implicit val encoder = new JwtSessionEncoder[AdminSession]
  implicit val sessionManager = new SessionManager[AdminSession](sessionConfig)

}
