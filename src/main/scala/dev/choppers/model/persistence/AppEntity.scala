package dev.choppers.model.persistence

import reactivemongo.bson.BSONObjectID

object AppEntity {

  final case class AppEntity(_id: BSONObjectID = BSONObjectID.generate,
                             name: String,
                             apiKey: String,
                             secret: String)

}
