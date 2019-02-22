package dev.choppers.akka.http.session

import com.softwaremill.session._
import dev.choppers.config.HasConfig

trait CustomerSessionSupport extends SessionDirectives with HasConfig {

  val sessionConfig = SessionConfig.fromConfig(config, "customer.session")

  implicit val serializer = JValueSessionSerializer.caseClass[CustomerSession]
  implicit val encoder = new JwtSessionEncoder[CustomerSession]
  implicit val sessionManager = new SessionManager[CustomerSession](sessionConfig)

}
