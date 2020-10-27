package dev.choppers.model.api

import dev.choppers.model.persistence.AppEntity.AppEntity

object VerifiedAppProtocol {

  object AppPermission extends Enumeration {
    type AppPermission = Value
    val Admin, Service = Value
  }

  final case class VerifiedApp(id: String, name: String, permissions: Set[AppPermission.AppPermission])

  implicit def toVerifiedApp(appEntity: AppEntity): VerifiedApp =
    VerifiedApp(id = appEntity._id.stringify,
      name = appEntity.name,
      permissions = appEntity.permissions.map(AppPermission.withName))
}
