package dev.choppers.model.api

import dev.choppers.model.persistence.AppEntity.AppEntity

object VerifiedAppProtocol {

  final case class VerifiedApp(id: String, name: String)

  implicit def toVerifiedApp(appEntity: AppEntity): VerifiedApp =
    VerifiedApp(id = appEntity._id.stringify, name = appEntity.name)
}
