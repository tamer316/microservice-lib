package akka.http.directives

import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.testkit.Specs2RouteTest
import dev.choppers.model.persistence.AppEntity._
import dev.choppers.repositories.AppRepository
import org.apache.commons.codec.digest.{DigestUtils, HmacUtils}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

import scala.concurrent.Future

trait VerifiedAppSpecification extends Specification with Specs2RouteTest with Mockito {

  class HttpRequestWithVerifiedAppSupport(httpRequest: HttpRequest) {
    def addVerifiedAppHeaders(implicit appRepository: AppRepository): HttpRequest = {
      addVerifiedAppHeadersWithNameAndKey("Web", "123456")
    }

    def addVerifiedAppHeadersWithName(appName: String)(implicit appRepository: AppRepository): HttpRequest = {
      addVerifiedAppHeadersWithNameAndKey(appName, "1234567")
    }

    private def addVerifiedAppHeadersWithNameAndKey(appName: String, apiKey: String)(implicit appRepository: AppRepository): HttpRequest = {
      appRepository.findByApiKey(apiKey) returns Future.successful(
        Some(AppEntity(name = appName, apiKey = apiKey,
          secret = "bd2b1aaf7ef4f09be9f52ce2d8d599674d81aa9d6a4421696dc4d93dd0619d682ce56b4d64a9ef097761ced99e0f67265b5f76085e5b0ee7ca4696b2ad6fe2b2")))

      httpRequest.addHeader(RawHeader("apiKey", apiKey)).addHeader(RawHeader("signature",
        calculateSignature(httpRequest._1.value, httpRequest._2.path.toString(), "secret")))
    }
  }

  implicit def pimpHttpRequestWithVerifiedAppSupport(httpRequest: HttpRequest): HttpRequestWithVerifiedAppSupport =
    new HttpRequestWithVerifiedAppSupport(httpRequest)

  private def calculateSignature(reqMethod: String, reqPath: String, secret: String) = {
    val encodedSecret = DigestUtils.sha512Hex(secret)
    val message = reqMethod + reqPath
    HmacUtils.hmacSha256Hex(encodedSecret, message)
  }
}
