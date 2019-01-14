package com.oef.converter.currency.service.external.exception

case class CurrencyApiException(message: String) extends Exception(message)
