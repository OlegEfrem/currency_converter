package com.oef.converter.currency.model

case class ConversionRequest(fromCurrency: String, toCurrency: String, amount: BigDecimal)

case class ConversionResponse(exchange: BigDecimal, amount: BigDecimal, original: BigDecimal)
