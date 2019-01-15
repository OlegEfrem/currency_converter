package com.oef.converter.currency.service.external.provider.era

import java.util.Currency

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCode, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.oef.converter.currency.service.external.exception.RatesApiException
import com.oef.converter.currency.util.ActorContext

import scala.concurrent.Future

class ExchangeRatesApiEndpoint() extends ActorContext {

  def httpGet(fromCurrency: Currency, toCurrency: Currency): Future[String] = {
    val url      = s"""https://api.exchangeratesapi.io/latest?base=${fromCurrency.getCurrencyCode}&symbols=${toCurrency.getCurrencyCode}"""
    val response = Http().singleRequest(HttpRequest(uri = url))
    response flatMap {
      case resp @ HttpResponse(StatusCodes.OK, _, entity, _) => Unmarshal(entity).to[String]
      case resp @ HttpResponse(code, _, entity, _)           => Unmarshal(entity).to[String].flatMap(msg => Future.failed(error(url, code, msg)))
    }
  }

  private def error(url: String, code: StatusCode, message: String): RatesApiException = {
    RatesApiException(s"Upstream server responded with status code: $code, message: $message")
  }

}

object ExchangeRatesApiEndpoint {
  def apply(): ExchangeRatesApiEndpoint = new ExchangeRatesApiEndpoint()
}
