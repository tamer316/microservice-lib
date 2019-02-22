package akka.http.session

import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.headers.RawHeader
import dev.choppers.akka.http.session.{CustomerSession, CustomerSessionSupport}

trait CustomerSessionSpecification extends CustomerSessionSupport {

  class HttpRequestWithCustomerLoginSupport(httpRequest: HttpRequest) {
    def addCustomerAuthTokenHeader(implicit customerSession: CustomerSession): HttpRequest = {
      val token = sessionManager.clientSessionManager.encode(customerSession)
      httpRequest.addHeader(RawHeader("Customer-Auth-Token", token))
    }
  }

  implicit def HttpRequestWithCustomerLoginSupport(httpRequest: HttpRequest): HttpRequestWithCustomerLoginSupport =
    new HttpRequestWithCustomerLoginSupport(httpRequest)
}
