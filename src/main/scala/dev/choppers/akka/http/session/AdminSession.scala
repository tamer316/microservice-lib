package dev.choppers.akka.http.session

import reactivemongo.bson.BSONObjectID

case class AdminSession(adminId: BSONObjectID, username: String)

object AdminSession {
  def apply(adminId: String, username: String): AdminSession = {
    AdminSession(BSONObjectID.parse(adminId).get, username)
  }
}
