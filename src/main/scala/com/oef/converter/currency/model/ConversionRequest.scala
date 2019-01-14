package com.oef.converter.currency.model
import java.util.Date

case class ConversionRequest(fromCurrency: String, toCurrency: String, amount: BigDecimal)

case class ConversionResponse(exchange: BigDecimal, amount: BigDecimal, original: BigDecimal)

case class RatesResponse(rates: Map[String, BigDecimal], base: String, date: Date)
