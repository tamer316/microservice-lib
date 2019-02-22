package dev.choppers.akka.http.session

import reactivemongo.bson.BSONObjectID

case class CustomerSession(customerId: BSONObjectID, firstName: String, lastName: String)

object CustomerSession {
  def apply(customerId: String, firstName: String, lastName: String): CustomerSession = {
    CustomerSession(BSONObjectID.parse(customerId).get, firstName, lastName)
  }
}
