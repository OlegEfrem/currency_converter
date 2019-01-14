package com.oef.converter.currency.service
import com.oef.converter.UnitSpec
import com.oef.converter.currency.model.ConversionRequest
import com.oef.converter.currency.service.external.RatesApi
import com.oef.converter.currency.service.impl.SingleCurrencyConverter

class SingleCurrencyConverterTest extends UnitSpec {
  private val ratesApi = stub[RatesApi]
  private val service  = new SingleCurrencyConverter(ratesApi)
  private val pound    = "GBP"
  private val euro     = "EUR"

  "exchangeRate should" - {

    "throw CurrencyNotFound exception for a non existing base currency" in {
      an[IllegalArgumentException] should be thrownBy service.convert(ConversionRequest("NonExistingCurrency", euro, 10))
    }

    "calculate the total conversion amount" in {}

  }
}
