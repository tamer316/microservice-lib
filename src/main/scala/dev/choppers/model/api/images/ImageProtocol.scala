package dev.choppers.model.api.images

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import dev.choppers.model.api.images.ImageProtocol.{Image, ImageSize}
import spray.json.DefaultJsonProtocol

object ImageProtocol {

  object ImageStatus extends Enumeration {
    val New, Approved, Rejected = Value
  }

  case class ImageSize(size: String, url: String)

  case class Image(id: String, position: Int, sizes: Seq[ImageSize], approved: Boolean)

}

trait ImageJsonProtocol {
  this: SprayJsonSupport with DefaultJsonProtocol =>
  implicit val imageSizeFormat = jsonFormat2(ImageSize.apply)
  implicit val imageFormat = jsonFormat4(Image.apply)
}