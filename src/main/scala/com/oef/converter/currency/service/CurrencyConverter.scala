package com.oef.converter.currency.service
import com.oef.converter.currency.model.{ConversionRequest, ConversionResponse}
import com.oef.converter.currency.service.external.RatesApi
import com.oef.converter.currency.service.impl.SingleCurrencyConverter
import scala.concurrent.Future

trait CurrencyConverter {
  def convert(conversionRequest: ConversionRequest): Future[ConversionResponse]
}

object CurrencyConverter {
  def apply(ratesApi: RatesApi = RatesApi()): CurrencyConverter = new SingleCurrencyConverter(ratesApi)
}
