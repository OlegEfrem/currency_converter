package com.oef.converter

import akka.event.{LoggingAdapter, NoLogging}
import akka.http.scaladsl.testkit.ScalatestRouteTest

trait ApiSpec extends UnitSpec with ScalatestRouteTest {
  protected val log: LoggingAdapter = NoLogging
}
