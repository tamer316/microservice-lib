package dev.choppers.model.api.dates

import dev.choppers.model.api.Validators
import dev.choppers.model.api.dates.DateProtocol.Days

import scala.util.Try

object DateProtocol {

  object Days extends Enumeration {
    val Monday = Value(1)
    val Tuesday = Value(2)
    val Wednesday = Value(3)
    val Thursday = Value(4)
    val Friday = Value(5)
    val Saturday = Value(6)
    val Sunday = Value(7)
  }

}

trait DateProtocol {
  this: Validators =>

  def validateDay(day: String) = {
    requireNonEmptyText("Day", day)
    require(Try(Days.withName(day)).isSuccess, s"Invalid Day ${day}, must be one of ${Days.values.mkString(",")}")
  }
}
