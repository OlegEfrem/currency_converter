package com.oef.converter.currency.http
import akka.http.scaladsl.server.Directives.{handleRejections, pathPrefix}
import akka.http.scaladsl.server.Route

class RestApi(currencyConverterRoutes: CurrencyConverterRoutes) extends RejectionHandling {
  val routes: Route =
    handleRejections(customRejectionHandler) {
      pathPrefix("v1") {
        currencyConverterRoutes.converter
      }
    }

}

object RestApi {
  def apply(): RestApi = new RestApi(CurrencyConverterRoutes())
}
