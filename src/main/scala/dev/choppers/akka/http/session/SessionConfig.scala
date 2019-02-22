package dev.choppers.akka.http.session

import java.util.concurrent.TimeUnit

import com.softwaremill.session.{CookieConfig, HeaderConfig, SessionConfig}
import com.typesafe.config.{Config, ConfigFactory}

object SessionConfig {

  private implicit class PimpedConfig(config: Config) {
    val noneValue = "none"

    def getOptionalString(path: String) = if (config.getAnyRef(path) == noneValue) None
    else
      Some(config.getString(path))

    def getOptionalLong(path: String) = if (config.getAnyRef(path) == noneValue) None
    else
      Some(config.getLong(path))

    def getOptionalDurationSeconds(path: String) = if (config.getAnyRef(path) == noneValue) None
    else
      Some(config.getDuration(path, TimeUnit.SECONDS))
  }

  def fromConfig(config: Config = ConfigFactory.load(), configPath: String): SessionConfig = {
    val scopedConfig = config.getConfig(configPath)
    val csrfConfig = scopedConfig.getConfig("csrf")
    val refreshTokenConfig = scopedConfig.getConfig("refresh-token")

    new SessionConfig(
      serverSecret = scopedConfig.getString("server-secret"),
      sessionCookieConfig = CookieConfig(
        name = scopedConfig.getString("cookie.name"),
        domain = scopedConfig.getOptionalString("cookie.domain"),
        path = scopedConfig.getOptionalString("cookie.path"),
        secure = scopedConfig.getBoolean("cookie.secure"),
        httpOnly = scopedConfig.getBoolean("cookie.http-only")
      ),
      sessionHeaderConfig = HeaderConfig(
        sendToClientHeaderName = scopedConfig.getString("header.send-to-client-name"),
        getFromClientHeaderName = scopedConfig.getString("header.get-from-client-name")
      ),
      sessionMaxAgeSeconds = scopedConfig.getOptionalDurationSeconds("max-age"),
      sessionEncryptData = scopedConfig.getBoolean("encrypt-data"),
      csrfCookieConfig = CookieConfig(
        name = csrfConfig.getString("cookie.name"),
        domain = csrfConfig.getOptionalString("cookie.domain"),
        path = csrfConfig.getOptionalString("cookie.path"),
        secure = csrfConfig.getBoolean("cookie.secure"),
        httpOnly = csrfConfig.getBoolean("cookie.http-only")
      ),
      csrfSubmittedName = csrfConfig.getString("submitted-name"),
      refreshTokenCookieConfig = CookieConfig(
        name = refreshTokenConfig.getString("cookie.name"),
        domain = refreshTokenConfig.getOptionalString("cookie.domain"),
        path = refreshTokenConfig.getOptionalString("cookie.path"),
        secure = refreshTokenConfig.getBoolean("cookie.secure"),
        httpOnly = refreshTokenConfig.getBoolean("cookie.http-only")
      ),
      refreshTokenHeaderConfig = HeaderConfig(
        sendToClientHeaderName = refreshTokenConfig.getString("header.send-to-client-name"),
        getFromClientHeaderName = refreshTokenConfig.getString("header.get-from-client-name")
      ),
      refreshTokenMaxAgeSeconds = refreshTokenConfig.getDuration("max-age", TimeUnit.SECONDS),
      removeUsedRefreshTokenAfter = refreshTokenConfig.getDuration("remove-used-token-after", TimeUnit.SECONDS)
    )
  }
}
