package com.oef.converter.json.jackson

import java.io.InputStream
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import com.oef.converter.UnitSpec
import com.oef.converter.currency.model.{ConversionRequest, ConversionResponse, RatesResponse}
import com.oef.converter.currency.util.Configuration

class JsonConverterJacksonTest extends UnitSpec {
  val jsonConverter = JsonConverterJackson
  import JsonConverterJacksonTest._

  "jsonToMap should" - {

    "return empty for an empty json" in {
      val is = emptyJson
      jsonConverter.jsonToMap(emptyJson) shouldBe Map()
    }

    "return an IllegalArgumentException for an empty input" in {
      a[IllegalArgumentException] should be thrownBy jsonConverter.jsonToMap(emptyFile)
    }

    "return a JsonParseException for a not valid json" in {
      a[JsonParseException] should be thrownBy jsonConverter.jsonToMap(invalidJson)
    }

    "return a map for a one level json" in {
      jsonConverter.jsonToMap(json1Level) should contain theSameElementsAs Map("id" -> 10001, "name" -> "Alex Smith")
    }

    "return a map for a multi level json" in {
      jsonConverter.jsonToMap(json3Levels) should contain theSameElementsAs json3LevelsMap
    }

  }

  "fromJson should" - {

    "convert a ConversionRequest json to a ConversionRequest object " in {
      jsonConverter.fromJson[ConversionRequest](conversionRequestJson) shouldBe conversionRequest
    }

    "convert a ConversionResponse json to ConversionResponse object" in {
      jsonConverter.fromJson[ConversionResponse](conversionResponseJson) shouldBe conversionResponse
    }

    "convert a RatesResponse json to a RatesResponse object" in {
      jsonConverter.fromJson[RatesResponse](ratesResponseJson) shouldBe ratesResponse
    }

    "throw an exception when trying to convert a ConversionRequest json to a ConversionResponse object" in {
      an[UnrecognizedPropertyException] should be thrownBy jsonConverter.fromJson[ConversionResponse](conversionRequestJson)
    }
  }

  "toJson should" - {
    "convert a ConversionRequest to json" in {
      jsonConverter.toJson(conversionRequest) shouldBe conversionRequestJson
    }

    "convert a ConversionResponse to json" in {
      jsonConverter.toJson(conversionResponse) shouldBe conversionResponseJson
    }
  }

}

object JsonConverterJacksonTest {
  val emptyJson: InputStream   = readFile("/Empty.json")
  val emptyFile: InputStream   = readFile("/EmptyFile")
  val invalidJson: InputStream = readFile("/InvalidJson")
  val json1Level: InputStream  = readFile("/1Level.json")
  val json3Levels: InputStream = readFile("/3Levels.json")
  val json3LevelsMap: Map[String, Any] = Map(
    "id"   -> 10001,
    "name" -> "Alex Smith",
    "payments" ->
      List(Map("timestamp" -> "ISO_DATE", "amount" -> 100.59), Map("timestamp" -> "ISO_DATE", "amount" -> 12.99)),
    "address" ->
      Map("postcode" -> "W1 5AX",
          "coordiantes" ->
            Map("lat" -> 51, "lon" -> 0),
          "country" -> "UK",
          "second"  -> "Kings Cross",
          "first"   -> "12 Watergarden")
  )
  val conversionRequest: ConversionRequest = ConversionRequest("GBP", "EUR", 102.6)
  val conversionRequestJson: String =
    """
      |{
      |"fromCurrency": "GBP",
      |"toCurrency" : "EUR",
      |"amount" : 102.6
      |}
    """.stripMargin.replaceAll("""\n|\p{Blank}""", "")
  val conversionResponse: ConversionResponse = ConversionResponse(1.11, 113.886, 102.6)
  val conversionResponseJson: String =
    """
      |{
      |"exchange" : 1.11,
      |"amount" : 113.886,
      |"original" : 102.6
      |}
    """.stripMargin.replaceAll("""\n|\p{Blank}""", "")

  val ratesResponse = RatesResponse(Map("EUR" -> 1.1202850005), "GBP", Configuration.dateFormat.parse("2019-01-14"))
  val ratesResponseJson: String =
    """
      |{
      |"rates": {
      |"EUR": 1.1202850005
      |},
      |"base": "GBP",
      |"date": "2019-01-14"
      |}
    """.stripMargin.replaceAll("""\n|\p{Blank}""", "")

  private def readFile(fileName: String): InputStream = getClass.getResourceAsStream(fileName)
}
