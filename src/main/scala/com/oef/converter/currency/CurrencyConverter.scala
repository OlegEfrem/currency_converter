package com.oef.converter.currency
import com.oef.converter.currency.model.{ConversionRequest, ConversionResponse}

import scala.concurrent.Future

trait CurrencyConverter {
  def convert(conversionRequest: ConversionRequest): Future[ConversionResponse]
}
