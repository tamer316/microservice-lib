package dev.choppers.repositories

import javax.inject.Singleton

import com.github.limansky.mongoquery.reactive._
import dev.choppers.model.persistence.AppEntity.AppEntity
import dev.choppers.mongo.{MongoDB, Repository}
import reactivemongo.bson._

import scala.concurrent.Future

@Singleton
class AppRepository extends Repository[AppEntity] with MongoDB {
  val collectionName = "apps"

  implicit def reader: BSONDocumentReader[AppEntity] = Macros.reader[AppEntity]

  implicit def writer: BSONDocumentWriter[AppEntity] = Macros.writer[AppEntity]

  def findByApiKey(apiKey: String): Future[Option[AppEntity]] = findOne(mqt"""{apiKey: $apiKey}"""[AppEntity])
}