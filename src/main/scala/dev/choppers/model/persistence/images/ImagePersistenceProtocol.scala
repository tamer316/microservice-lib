package dev.choppers.model.persistence.images

import dev.choppers.model.api.images.ImageProtocol.{Image, ImageSize, ImageStatus}
import dev.choppers.model.persistence.images.ImagePersistenceProtocol.{ImageEntity, ImageSizeEntity}
import reactivemongo.bson.{BSONDocumentReader, BSONDocumentWriter, BSONObjectID, Macros}

object ImagePersistenceProtocol {

  case class ImageSizeEntity(width: Int, height: Int, url: String)

  case class ImageEntity(_id: BSONObjectID = BSONObjectID.generate, name: String, position: Int,
                         sizes: Seq[ImageSizeEntity], status: String = ImageStatus.New.toString)

  implicit def toImageSize(imageSizeEntity: ImageSizeEntity): ImageSize = {
    ImageSize(s"${imageSizeEntity.width}x${imageSizeEntity.height}", imageSizeEntity.url)
  }

  implicit def toImage(imageEntity: ImageEntity): Image = {
    Image(imageEntity._id.stringify, imageEntity.position,
      imageEntity.sizes.map(is => toImageSize(is)),
      imageEntity.status == ImageStatus.Approved.toString)
  }
}

trait ImageMongoProtocol {
  implicit val imageSizeReader: BSONDocumentReader[ImageSizeEntity] = Macros.reader[ImageSizeEntity]

  implicit val imageSizeWriter: BSONDocumentWriter[ImageSizeEntity] = Macros.writer[ImageSizeEntity]

  implicit val imageReader: BSONDocumentReader[ImageEntity] = Macros.reader[ImageEntity]

  implicit val imageWriter: BSONDocumentWriter[ImageEntity] = Macros.writer[ImageEntity]
}

