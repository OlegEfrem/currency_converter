package com.oef.converter.currency.service.impl

import java.util.Currency
import com.oef.converter.currency.model.{ConversionRequest, ConversionResponse}
import com.oef.converter.currency.service.CurrencyConverter
import com.oef.converter.currency.service.external.RatesApi
import com.oef.converter.currency.service.external.exception.InvalidCurrencyException
import com.oef.converter.currency.util.ActorContext
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class SingleCurrencyConverter(ratesApi: RatesApi) extends CurrencyConverter with ActorContext {

  override def convert(conversionRequest: ConversionRequest): Future[ConversionResponse] = {
    for {
      fromCurrency <- parseCurrency(conversionRequest.fromCurrency)
      toCurrency   <- parseCurrency(conversionRequest.toCurrency)
      rate         <- ratesApi.exchangeRate(fromCurrency, toCurrency)
      convertedAmount = rate * conversionRequest.amount
    } yield ConversionResponse(rate, convertedAmount, conversionRequest.amount)
  }

  private def parseCurrency(currency: String): Future[Currency] =
    Try {
      Currency.getInstance(currency)
    } match {
      case Success(c)         => Future.successful(c)
      case Failure(exception) => Future.failed(InvalidCurrencyException(s"Not a valid currency: $currency"))
    }

}
