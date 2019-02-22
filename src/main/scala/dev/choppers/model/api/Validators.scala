package dev.choppers.model.api

import scala.util.matching.Regex

trait Validators {
  private val emailRegex =
    """^[a-zA-Z0-9\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$""".r

  def requireNonEmptyText(fieldName: String, value: String, minLength: Int = 1, maxLength: Option[Int] = None) = {
    require(value.length >= minLength, fieldName + s" must be at least $minLength characters long")
    maxLength.map { ml =>
      require(value.length <= ml, fieldName + s" must be at most $maxLength characters long")
    }
  }

  def requirePattern(fieldName: String, value: String, pattern: Regex) = {
    require(pattern.findFirstMatchIn(value).isDefined, fieldName + s" is invalid")
  }

  def requireEmail(fieldName: String, value: String) = {
    require(emailRegex.findFirstMatchIn(value).isDefined, fieldName + s" must be a valid email")
  }

  def requirePassword(fieldName: String, value: String, minLength: Int = 8, maxLength: Int = 30) = {
    requireNonEmptyText(fieldName, value, minLength, Some(maxLength))
  }
}
