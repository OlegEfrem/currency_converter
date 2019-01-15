package com.oef.converter.currency.exception
import com.oef.converter.exception.ConverterException

//scalastyle:off
class CurrencyConverterException(message: String, throwable: Throwable = null) extends ConverterException(message, throwable) {
  override def apply(throwable: Throwable = null): CurrencyConverterException = new CurrencyConverterException(message, throwable)
}
