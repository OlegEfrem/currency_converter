package com.oef.converter.json.jackson
import java.io.InputStream

import com.oef.converter.UnitSpec

class JsonConverterJacksonTest extends UnitSpec {
  "jsonToMap should" - {
    val jsonConverter = new JsonConverterJackson
    import JsonConverterJacksonTest._

    "return empty for an empty json" in {
      jsonConverter.jsonToMap(emptyJson) shouldBe Map()
    }

    "return an IllegalArgumentException for an empty input" in {
      a[IllegalArgumentException] should be thrownBy jsonConverter.jsonToMap(emptyFile)
    }

  }

}

object JsonConverterJacksonTest {
  val emptyJson: InputStream   = readFile("Empty.json")
  val emptyFile: InputStream   = readFile("EmptyFile")
  val invalidJson: InputStream = readFile("InvalidJson")
  val validJson: InputStream   = readFile("3Level.json")

  private def readFile(fileName: String): InputStream = getClass.getResourceAsStream(fileName)
}
