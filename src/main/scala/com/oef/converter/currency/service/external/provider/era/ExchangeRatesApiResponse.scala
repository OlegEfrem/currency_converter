package com.oef.converter.currency.service.external.provider.era
import java.util.Date

case class ExchangeRatesApiResponse(rates: Map[String, BigDecimal], base: String, date: Date)
