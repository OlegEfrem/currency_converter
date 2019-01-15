package com.oef.converter.currency.service.external.exception
import com.oef.converter.currency.exception.CurrencyConverterException

//scalastyle:off
case class InvalidCurrencyException(message: String, throwable: Throwable = null) extends CurrencyConverterException(message)
