package com.oef.converter.currency.service.external.provider

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Currency
import com.oef.converter.UnitSpec
import com.oef.converter.config.Configuration
import com.oef.converter.currency.service.external.exception.CurrencyApiException

class ExchangeRatesApiEndpointTest extends UnitSpec {
  private val endpoints = new ExchangeRatesApiEndpoint()

  "httpGet should " - {

    "return the rate for an existing currency" in {
      endpoints.httpGet(Currency.getInstance("GBP"), Currency.getInstance("EUR")).futureValue should
        include(s""""date":"${LocalDate.now.format(DateTimeFormatter.ofPattern(Configuration.datePattern))}""")
    }

    "return an error for non supported currency" in {
      whenReady(endpoints.httpGet(Currency.getInstance("GBP"), Currency.getInstance("MDL")).failed) { e =>
        e shouldBe a[CurrencyApiException]
      }
    }

  }
}
