package com.oef.converter.currency.service
import java.util.Currency
import com.oef.converter.UnitSpec
import com.oef.converter.currency.model.{ConversionRequest, ConversionResponse}
import com.oef.converter.currency.service.external.RatesApi
import com.oef.converter.currency.service.external.exception.InvalidCurrencyException
import com.oef.converter.currency.service.impl.SingleCurrencyConverter
import scala.concurrent.Future

class SingleCurrencyConverterTest extends UnitSpec {
  private val ratesApi           = stub[RatesApi]
  private val service            = new SingleCurrencyConverter(ratesApi)
  private val pound              = "GBP"
  private val amount: BigDecimal = 100
  private val gbp                = Currency.getInstance(pound)
  private val euro               = "EUR"
  private val eur                = Currency.getInstance(euro)
  private val exchangeRate       = 1.2

  "convert should" - {

    "throw InvalidCurrencyException exception for a non existing base currency" in {
      whenReady(service.convert(ConversionRequest("NonExistingCurrency", euro, 10)).failed) { e =>
        e shouldBe an[InvalidCurrencyException]
      }
    }

    "calculate the total conversion amount" in {
      ratesApi.exchangeRate _ when (gbp, eur) returns Future.successful(exchangeRate)
      service.convert(ConversionRequest(pound, euro, 100)).futureValue shouldBe ConversionResponse(exchangeRate, amount * exchangeRate, amount)

    }

  }
}
