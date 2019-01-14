package com.oef.converter.currency.service
import java.util.Currency

import com.oef.converter.currency.CurrencyConverter
import com.oef.converter.currency.model.{ConversionRequest, ConversionResponse}
import com.oef.converter.currency.service.external.RatesApi
import com.oef.converter.currency.util.ActorContext
import scala.concurrent.Future

class SingleCurrencyConverter(ratesApi: RatesApi) extends CurrencyConverter with ActorContext {

  override def convert(conversionRequest: ConversionRequest): Future[ConversionResponse] = {
    val fromCurrency = Currency.getInstance(conversionRequest.fromCurrency)
    val toCurrency   = Currency.getInstance(conversionRequest.toCurrency)
    val rate         = ratesApi.exchangeRate(fromCurrency, toCurrency)
    for {
      r <- rate
      convertedAmount = r * conversionRequest.amount
    } yield ConversionResponse(r, convertedAmount, conversionRequest.amount)
  }

}
