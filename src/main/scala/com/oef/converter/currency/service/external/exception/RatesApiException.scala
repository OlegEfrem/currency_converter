package com.oef.converter.currency.service.external.exception

case class RatesApiException(message: String) extends Exception(message)
