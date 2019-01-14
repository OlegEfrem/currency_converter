package com.oef.converter.currency.service.external.provider

import java.util.Currency

import com.oef.converter.UnitSpec
import com.oef.converter.currency.service.external.exception.CurrencyNotFoundException
import com.oef.converter.currency.service.external.provider.era.{ExchangeRatesApi, ExchangeRatesApiEndpoint}

import scala.concurrent.Future

class ExchangeRatesApiTest extends UnitSpec {
  private val endpoint = stub[ExchangeRatesApiEndpoint]
  private val ratesApi = new ExchangeRatesApi(endpoint)
  private val pound    = Currency.getInstance("GBP")
  private val euro     = Currency.getInstance("EUR")

  "exchangeRate shoud" - {
    import ExchangeRatesApiTest._

    "return the exchange rate if found" in {
      endpoint.httpGet _ when (pound, euro) returns Future.successful(gbpToEuroOkJson)
      ratesApi.exchangeRate(pound, euro).futureValue shouldBe 1.1202850005
    }

    "throw CurrencyNotFoundException if currency not found" in {
      endpoint.httpGet _ when (euro, pound) returns Future.successful(gbpToEuroOkJson)
      whenReady(ratesApi.exchangeRate(euro, pound).failed) { e =>
        e shouldBe a[CurrencyNotFoundException]
      }
    }

  }
}

object ExchangeRatesApiTest {
  val gbpToEuroOkJson: String   = """{"rates":{"EUR":1.1202850005},"base":"GBP","date":"2019-01-14"}"""
  val invalidSymbolJson: String = """{"error":"Symbols 'MDL' are invalid for date 2019-01-14."}"""
}
