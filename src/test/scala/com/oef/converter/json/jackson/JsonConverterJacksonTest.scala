package com.oef.converter.json.jackson
import java.io.InputStream

import com.fasterxml.jackson.core.JsonParseException
import com.oef.converter.UnitSpec
import com.oef.converter.currency.model.{ConversionRequest, ConversionResponse}

class JsonConverterJacksonTest extends UnitSpec {
  val jsonConverter = new JsonConverterJackson
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

    "convert a ConversionResponse json to ConversionResponse" in {}

    "throw an exception when trying to convert a ConversionRequest json to a ConversionResponse object" in {}
  }

  "toJson should" - {
    "convert a ConversionRequest to json" in {}

    "convert a ConversionResponse to json" in {}
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
      |"toCurrency" : "EUR","amount" : 102.6
      |}
    """.stripMargin
  val conversionResponse: ConversionResponse = ConversionResponse(1.11, 113.886, 102.6)
  val conversionResponseJson: String =
    """
      |{
      |"exchange" : 1.11,
      |"amount" : 113.886,
      |"original" : 102.6
      |}
    """.stripMargin

  private def readFile(fileName: String): InputStream = getClass.getResourceAsStream(fileName)
}
