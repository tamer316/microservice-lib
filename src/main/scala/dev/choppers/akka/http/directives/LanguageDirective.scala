package dev.choppers.akka.http.directives

import akka.http.scaladsl.server.{Directive1, Directives}
import com.osinka.i18n.Lang

trait LanguageDirective {
  this: Directives =>

  def language: Directive1[Lang] =
    optionalHeaderValueByName("Language").flatMap { langOpt =>
      val lang = langOpt match {
        case Some(x) => Lang(x.toLowerCase.trim)
        case None => Lang("en")
      }
      provide(lang)
    }
}
