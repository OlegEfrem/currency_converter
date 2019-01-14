package com.oef.converter.currency.service

import java.util.Currency
import com.oef.converter.currency.CurrencyConverter
import com.oef.converter.currency.exception.CurrencyNotFoundException
import com.oef.converter.currency.model.{ConversionRequest, ConversionResponse, RatesResponse}
import com.oef.converter.json.JsonConverter
import scala.concurrent.Future
import scala.io.Source
import scala.concurrent.ExecutionContext.Implicits.global

class ExchangeRatesApi(jsonConverter: JsonConverter) extends CurrencyConverter {

  override def convert(conversionRequest: ConversionRequest): Future[ConversionResponse] = {
    val fromCurrency = Currency.getInstance(conversionRequest.fromCurrency)
    val toCurrency   = Currency.getInstance(conversionRequest.toCurrency)
    val rate         = getRate(fromCurrency, toCurrency)
    for {
      r <- rate
      convertedAmount = r * conversionRequest.amount
    } yield ConversionResponse(r, convertedAmount, conversionRequest.amount)
  }

  private def getRate(fromCurrency: Currency, toCurrency: Currency): Future[BigDecimal] = {
    val url = s"""https://api.exchangeratesapi.io/latest?base=${fromCurrency.getCurrencyCode}&symbols=${toCurrency.getCurrencyCode}"""
    for {
      response <- Future { Source.fromURL(url).mkString }
      ratesResponse = jsonConverter.fromJson[RatesResponse](response)
    } yield extractRate(toCurrency, ratesResponse)
  }

  private def extractRate(toCurrency: Currency, ratesResponse: RatesResponse): BigDecimal = {
    ratesResponse.rates
      .getOrElse(toCurrency.getCurrencyCode, throw CurrencyNotFoundException(s"Not found rate for currency ${toCurrency.getCurrencyCode}"))
  }

}
