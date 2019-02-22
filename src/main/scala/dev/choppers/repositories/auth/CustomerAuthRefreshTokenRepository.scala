package dev.choppers.repositories.auth

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import dev.choppers.akka.http.session.CustomerSession
import reactivemongo.bson.{BSONDocumentReader, BSONDocumentWriter, Macros}

@Singleton
class CustomerAuthRefreshTokenRepository @Inject()(val system: ActorSystem)
  extends AuthRefreshTokenRepository[CustomerSession] {

  val collectionName = "customers_auth_refresh_tokens"

  implicit val sessionReader: BSONDocumentReader[CustomerSession] = Macros.reader[CustomerSession]
  implicit val sessionWriter: BSONDocumentWriter[CustomerSession] = Macros.writer[CustomerSession]
}
