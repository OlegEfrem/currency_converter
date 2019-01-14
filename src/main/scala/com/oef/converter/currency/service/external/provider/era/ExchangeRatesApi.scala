package com.oef.converter.currency.service.external.provider.era

import java.util.Currency
import com.oef.converter.currency.service.external.RatesApi
import com.oef.converter.currency.service.external.exception.CurrencyNotFoundException
import com.oef.converter.currency.util.ActorContext
import com.oef.converter.json.JsonConverter
import scala.concurrent.Future

class ExchangeRatesApi(endpoint: ExchangeRatesApiEndpoint, jsonConverter: JsonConverter = JsonConverter()) extends RatesApi with ActorContext {

  def exchangeRate(fromCurrency: Currency, toCurrency: Currency): Future[BigDecimal] = {
    for {
      response <- endpoint.httpGet(fromCurrency, toCurrency)
      ratesResponse = jsonConverter.fromJson[ExchangeRatesApiResponse](response)
    } yield extractRate(toCurrency, ratesResponse)
  }

  private def extractRate(toCurrency: Currency, ratesResponse: ExchangeRatesApiResponse): BigDecimal = {
    ratesResponse.rates
      .getOrElse(toCurrency.getCurrencyCode, throw CurrencyNotFoundException(s"Not found rate for currency ${toCurrency.getCurrencyCode}"))
  }

}

object ExchangeRatesApi {
  def apply(endpoint: ExchangeRatesApiEndpoint = ExchangeRatesApiEndpoint(), jsonConverter: JsonConverter = JsonConverter()): ExchangeRatesApi =
    new ExchangeRatesApi(endpoint, jsonConverter)
}
