package com.oef.converter.currency.http

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.oef.converter.currency.model.ConversionRequest
import com.oef.converter.currency.service.CurrencyConverter
import com.oef.converter.currency.service.external.exception.{CurrencyNotFoundException, InvalidCurrencyException, RatesApiException}
import com.oef.converter.json.JsonConverter
import scala.util.{Failure, Success}

class CurrencyConverterRoutes(currencyConverter: CurrencyConverter, jsonConverter: JsonConverter) {

  val converter: Route = pathPrefix("api" / "convert") {
    pathEndOrSingleSlash {
      post {
        entity(as[String]) { conversionRequest =>
          val request = jsonConverter.fromJson[ConversionRequest](conversionRequest)
          val result  = currencyConverter.convert(request)
          onComplete(result) {
            case Success(r)         => complete(jsonConverter.toJson(r))
            case Failure(exception) => completeWithError(exception)
          }
        }
      }
    }
  }

  private def completeWithError(exception: Throwable): Route = {
    import StatusCodes._
    exception match {
      case e: InvalidCurrencyException  => complete(BadRequest, e.getMessage)
      case e: RatesApiException         => complete(BadGateway, e.getMessage)
      case e: CurrencyNotFoundException => complete(NotFound, e.getMessage)
      case _                            => complete(InternalServerError, exception.getMessage)
    }
  }

}

object CurrencyConverterRoutes {
  def apply(): CurrencyConverterRoutes = new CurrencyConverterRoutes(CurrencyConverter(), JsonConverter())
}
