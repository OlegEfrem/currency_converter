package com.oef.converter
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import com.oef.converter.config.AppConfig
import com.oef.converter.currency.http.RestApi
import com.oef.converter.currency.util.ActorContext

object Main extends App with AppConfig with ActorContext {
  val log: LoggingAdapter = Logging(system, getClass)
  val restApi             = RestApi()
  Http().bindAndHandle(restApi.routes, httpInterface, httpPort)
}
