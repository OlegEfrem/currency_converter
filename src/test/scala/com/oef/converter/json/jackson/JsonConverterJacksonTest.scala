package com.oef.converter.json.jackson
import java.io.InputStream

import com.fasterxml.jackson.core.JsonParseException
import com.oef.converter.UnitSpec

class JsonConverterJacksonTest extends UnitSpec {
  "jsonToMap should" - {
    val jsonConverter = new JsonConverterJackson
    import JsonConverterJacksonTest._

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

  }

}

object JsonConverterJacksonTest {
  val emptyJson: InputStream   = readFile("/Empty.json")
  val emptyFile: InputStream   = readFile("/EmptyFile")
  val invalidJson: InputStream = readFile("/InvalidJson")
  val validJson: InputStream   = readFile("/3Levels.json")

  private def readFile(fileName: String): InputStream = getClass.getResourceAsStream(fileName)
}
