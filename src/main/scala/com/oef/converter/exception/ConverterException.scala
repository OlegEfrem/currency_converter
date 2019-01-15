package com.oef.converter.exception
//scalastyle:off
class ConverterException(message: String, throwable: Throwable = null) extends Exception(message, throwable) {
  def apply(throwable: Throwable): ConverterException = new ConverterException("", throwable)
}
