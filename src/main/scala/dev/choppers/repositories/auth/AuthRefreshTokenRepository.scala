package dev.choppers.repositories.auth

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import com.github.limansky.mongoquery.reactive._
import com.softwaremill.session.{RefreshTokenData, RefreshTokenLookupResult, RefreshTokenStorage}
import dev.choppers.model.persistence.auth.AuthRefreshTokenEntity.AuthRefreshTokenEntity
import dev.choppers.mongo.{MongoDB, Repository}
import reactivemongo.bson.{BSONDocumentReader, BSONDocumentWriter, Macros}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.{Duration, FiniteDuration}

trait AuthRefreshTokenRepository[T] extends Repository[AuthRefreshTokenEntity[T]] with MongoDB with RefreshTokenStorage[T] {
  implicit val sessionReader: BSONDocumentReader[T]

  implicit val sessionWriter: BSONDocumentWriter[T]

  implicit val reader: BSONDocumentReader[AuthRefreshTokenEntity[T]] = Macros.reader[AuthRefreshTokenEntity[T]]

  implicit val writer: BSONDocumentWriter[AuthRefreshTokenEntity[T]] = Macros.writer[AuthRefreshTokenEntity[T]]

  def system: ActorSystem

  implicit def toRefreshTokenLookupResult(te: AuthRefreshTokenEntity[T]): RefreshTokenLookupResult[T] = {
    RefreshTokenLookupResult[T](te.token, te.expires, () => te.session)
  }

  override def lookup(selector: String): Future[Option[RefreshTokenLookupResult[T]]] = {
    findOne(mq"{selector: $selector}").map(_.map(rt => rt))
  }

  override def schedule[S](after: Duration)(op: => Future[S]): Unit = {
    system.scheduler.scheduleOnce(FiniteDuration(after.toSeconds, TimeUnit.SECONDS), new Runnable {
      override def run() = op
    })
  }

  override def store(data: RefreshTokenData[T]): Future[Unit] = {
    insert(AuthRefreshTokenEntity[T](selector = data.selector, token = data.tokenHash,
      expires = data.expires, session = data.forSession))
  }

  override def remove(selector: String): Future[Unit] = {
    delete(mq"{selector: $selector}")
  }
}
