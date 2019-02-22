package dev.choppers.model.persistence.auth

import reactivemongo.bson.BSONObjectID

object AuthRefreshTokenEntity {

  case class AuthRefreshTokenEntity[T](_id: BSONObjectID = BSONObjectID.generate,
                                    selector: String,
                                    token: String,
                                    expires: Long,
                                    session: T)

}
