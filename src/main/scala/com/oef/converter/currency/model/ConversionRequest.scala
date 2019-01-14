package com.oef.converter.currency.model

case class ConversionRequest(fromCurrency: String, toCurrency: String, ammount: BigDecimal)

case class ConversionResponse(exchange: BigDecimal, amount: BigDecimal, original: BigDecimal)
