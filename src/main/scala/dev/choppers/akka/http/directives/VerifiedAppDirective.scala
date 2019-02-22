package dev.choppers.akka.http.directives

import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.{Directive1, Directives, MalformedHeaderRejection}
import dev.choppers.model.api.VerifiedAppProtocol.VerifiedApp
import dev.choppers.repositories.AppRepository
import org.apache.commons.codec.digest.HmacUtils

trait VerifiedAppDirective {
  this: Directives =>

  def appRepository: AppRepository

  def verifiedApp: Directive1[VerifiedApp] =
    extractRequest.flatMap { req =>
      headerValueByName("apiKey").flatMap { apiKey =>
        headerValueByName("signature").flatMap { signature =>
          onSuccess(appRepository.findByApiKey(apiKey)).flatMap {
            case Some(app) =>
              if (signature == calculateSignature(req, app.secret)) provide(app)
              else reject(MalformedHeaderRejection("signature", "Invalid Signature"))
            case None => reject(MalformedHeaderRejection("apiKey", "Invalid API Key"))
          }
        }
      }
    }

  def verifiedAppWithName(appNames: String*): Directive1[VerifiedApp] =
    extractRequest.flatMap { req =>
      headerValueByName("apiKey").flatMap { apiKey =>
        headerValueByName("signature").flatMap { signature =>
          onSuccess(appRepository.findByApiKey(apiKey)).flatMap {
            case Some(app) =>
              if (signature == calculateSignature(req, app.secret)) {
                if (appNames.contains(app.name)) {
                  provide(app)
                } else reject(MalformedHeaderRejection("apiKey", "App not allowed to access this resource"))
              } else reject(MalformedHeaderRejection("signature", "Invalid Signature"))
            case None => reject(MalformedHeaderRejection("apiKey", "Invalid API Key"))
          }
        }
      }
    }

  private def calculateSignature(request: HttpRequest, secret: String) = {
    val message = request._1.value + request._2.path.toString()
    HmacUtils.hmacSha256Hex(secret, message)
  }
}
