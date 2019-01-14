package com.oef.converter.currency.service.external
import java.util.Currency

import scala.concurrent.Future

trait RatesApi {
  def exchangeRate(fromCurrency: Currency, toCurrency: Currency): Future[BigDecimal]
}
