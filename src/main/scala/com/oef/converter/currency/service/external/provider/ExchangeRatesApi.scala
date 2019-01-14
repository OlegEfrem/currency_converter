package com.oef.converter.currency.service.external.provider

import java.util.Currency
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCode, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.oef.converter.currency.service.external.exception.{CurrencyApiException, CurrencyNotFoundException}
import com.oef.converter.currency.model.RatesResponse
import com.oef.converter.currency.service.external.RatesApi
import com.oef.converter.currency.util.ActorContext
import com.oef.converter.json.JsonConverter

import scala.concurrent.Future

class ExchangeRatesApi(endpoint: ExchangeRatesApiEndpoint, jsonConverter: JsonConverter = JsonConverter()) extends RatesApi with ActorContext {

  def exchangeRate(fromCurrency: Currency, toCurrency: Currency): Future[BigDecimal] = {
    for {
      response <- endpoint.httpGet(fromCurrency, toCurrency)
      ratesResponse = jsonConverter.fromJson[RatesResponse](response)
    } yield extractRate(toCurrency, ratesResponse)
  }

  private def extractRate(toCurrency: Currency, ratesResponse: RatesResponse): BigDecimal = {
    ratesResponse.rates
      .getOrElse(toCurrency.getCurrencyCode, throw CurrencyNotFoundException(s"Not found rate for currency ${toCurrency.getCurrencyCode}"))
  }

}

class ExchangeRatesApiEndpoint() extends ActorContext {

  def httpGet(fromCurrency: Currency, toCurrency: Currency): Future[String] = {
    val url      = s"""https://api.exchangeratesapi.io/latest?base=${fromCurrency.getCurrencyCode}&symbols=${toCurrency.getCurrencyCode}"""
    val response = Http().singleRequest(HttpRequest(uri = url))
    response flatMap {
      case resp @ HttpResponse(StatusCodes.OK, _, entity, _) => Unmarshal(entity).to[String]
      case resp @ HttpResponse(code, _, entity, _)           => Unmarshal(entity).to[String].flatMap(msg => Future.failed(error(url, code, msg)))
    }
  }

  private def error(url: String, code: StatusCode, message: String): CurrencyApiException = {
    CurrencyApiException(s"error calling url: $url, status code: $code, message: $message")
  }

}
