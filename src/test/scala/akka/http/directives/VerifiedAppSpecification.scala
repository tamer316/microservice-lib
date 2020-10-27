package akka.http.directives

import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.testkit.Specs2RouteTest
import dev.choppers.model.api.VerifiedAppProtocol.AppPermission.AppPermission
import dev.choppers.model.persistence.AppEntity._
import dev.choppers.repositories.AppRepository
import org.apache.commons.codec.digest.{DigestUtils, HmacAlgorithms, HmacUtils}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

import scala.concurrent.Future

trait VerifiedAppSpecification extends Specification with Specs2RouteTest with Mockito {

  class HttpRequestWithVerifiedAppSupport(httpRequest: HttpRequest) {
    def addVerifiedAppHeaders(implicit appRepository: AppRepository): HttpRequest = {
      addVerifiedAppHeadersWithPermissionsAndKey("123456", Set.empty)
    }

    def addVerifiedAppHeadersWithPermission(permission: AppPermission)(implicit appRepository: AppRepository): HttpRequest = {
      addVerifiedAppHeadersWithPermissionsAndKey("1234567", Set(permission.toString))
    }

    private def addVerifiedAppHeadersWithPermissionsAndKey(apiKey: String, permissions: Set[String])
                                                          (implicit appRepository: AppRepository): HttpRequest = {
      appRepository.findByApiKey(apiKey) returns Future.successful(
        Some(AppEntity(name = "Web", apiKey = apiKey,
          secret = "bd2b1aaf7ef4f09be9f52ce2d8d599674d81aa9d6a4421696dc4d93dd0619d682ce56b4d64a9ef097761ced99e0f67265b5f76085e5b0ee7ca4696b2ad6fe2b2",
        permissions = permissions)))

      httpRequest.addHeader(RawHeader("apiKey", apiKey)).addHeader(RawHeader("signature",
        calculateSignature(httpRequest._1.value, httpRequest._2.path.toString(), "secret")))
    }
  }

  implicit def pimpHttpRequestWithVerifiedAppSupport(httpRequest: HttpRequest): HttpRequestWithVerifiedAppSupport =
    new HttpRequestWithVerifiedAppSupport(httpRequest)

  private def calculateSignature(reqMethod: String, reqPath: String, secret: String) = {
    val encodedSecret = DigestUtils.sha512Hex(secret)
    val message = reqMethod + reqPath
    new HmacUtils(HmacAlgorithms.HMAC_SHA_256, encodedSecret).hmacHex(message)
  }
}
