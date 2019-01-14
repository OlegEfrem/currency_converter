package com.oef.converter.currency.service.external

import java.util.Currency

import com.oef.converter.currency.service.external.provider.era.{ExchangeRatesApi, ExchangeRatesApiEndpoint}
import com.oef.converter.json.JsonConverter

import scala.concurrent.Future

trait RatesApi {
  def exchangeRate(fromCurrency: Currency, toCurrency: Currency): Future[BigDecimal]
}

object RatesApi {
  def apply(endpoint: ExchangeRatesApiEndpoint = ExchangeRatesApiEndpoint(), jsonConverter: JsonConverter = JsonConverter()): RatesApi =
    ExchangeRatesApi(endpoint, jsonConverter)
}
