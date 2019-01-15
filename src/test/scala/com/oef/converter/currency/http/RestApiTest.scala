package com.oef.converter.currency.http
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes, StatusCode}
import com.oef.converter.ApiSpec
import com.oef.converter.currency.model.{ConversionRequest, ConversionResponse}
import com.oef.converter.currency.service.CurrencyConverter
import com.oef.converter.currency.service.external.exception.{CurrencyNotFoundException, InvalidCurrencyException, RatesApiException}
import com.oef.converter.json.JsonConverter

import scala.concurrent.Future

class RestApiTest extends ApiSpec {

  "restApi should" - {

    s"respond with HTTP-$OK when submitting valid currency conversions" in {
      currencyConverter.convert _ when conversionRequest returns Future.successful(conversionResponse)
      Post(generalUrl, requestEntity) ~> restApi.routes ~> check {
        status shouldBe OK
        responseAs[String] shouldBe conversionResponseJson
      }
    }

    s"respond with HTTP-$NotFound for a non existing path" in {
      Post("/non/existing/") ~> restApi.routes ~> check {
        status shouldBe NotFound
        responseAs[String] shouldBe "The path you requested [/non/existing/] does not exist."
      }
    }

    s"respond with HTTP-$MethodNotAllowed for a non supported HTTP method" in {
      Put(generalUrl) ~> restApi.routes ~> check {
        status shouldBe MethodNotAllowed
        responseAs[String] shouldBe "Not supported method! Supported methods are: POST!"
      }
    }

    s"respond with HTTP-$BadRequest in case of an ${classOf[InvalidCurrencyException].getSimpleName}" in {
      verifyExeptionMappedToCode(InvalidCurrencyException("some error"), BadRequest)
    }

    s"respond with HTTP-$BadGateway in case of a ${classOf[RatesApiException].getSimpleName}" in {
      verifyExeptionMappedToCode(RatesApiException("some error"), BadGateway)
    }

    s"respond with HTTP-$NotFound in case of a ${classOf[CurrencyNotFoundException].getSimpleName}" in {
      verifyExeptionMappedToCode(CurrencyNotFoundException("some error"), NotFound)
    }

    s"respond with HTTP-$InternalServerError in case of a generic ${classOf[Exception].getSimpleName}" in {
      verifyExeptionMappedToCode(new Exception("some error"), InternalServerError)
    }

    def verifyExeptionMappedToCode(exception: Exception, code: StatusCode): Unit = {
      val request = ConversionRequest("Eur", "GBP", 2)
      val entity  = HttpEntity(MediaTypes.`application/json`, jsonConverter.toJson(request))
      currencyConverter.convert _ when request returns Future.failed(exception)
      Post(generalUrl, entity) ~> restApi.routes ~> check {
        status shouldBe code
      }
    }

  }

  val conversionRequest: ConversionRequest   = ConversionRequest("GBP", "EUR", 100)
  val conversionRequestJson                  = """{"fromCurrency": "GBP","toCurrency":"EUR","amount":100}"""
  val conversionResponse: ConversionResponse = ConversionResponse(1.2, 120, 100)
  val conversionResponseJson                 = """{"exchange":1.2,"amount":120,"original":100}"""
  val jsonConverter                          = JsonConverter()
  val requestEntity                          = HttpEntity(MediaTypes.`application/json`, jsonConverter.toJson(conversionRequest))
  val currencyConverter: CurrencyConverter   = stub[CurrencyConverter]
  val restApi                                = new RestApi(new CurrencyConverterRoutes(currencyConverter, JsonConverter()))
  val generalUrl                             = "/v1/api/convert/"

}
