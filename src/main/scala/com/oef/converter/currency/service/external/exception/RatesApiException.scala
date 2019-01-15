package com.oef.converter.currency.service.external.exception
import com.oef.converter.currency.exception.CurrencyConverterException

case class RatesApiException(message: String) extends CurrencyConverterException(message)
