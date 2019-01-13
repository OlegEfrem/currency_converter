package com.oef.converter.json.jackson
import java.io.InputStream

import com.oef.converter.UnitSpec


class JsonConverterJacksonTest extends UnitSpec {
  "jsonToMap should" - {
    "return empty for empty input" in {
      val jsonConverter = new JsonConverterJackson
      jsonConverter.jsonToMap(readFile("JsonSample.json")) shouldBe Map()
    }
  }

  private def readFile(fileName: String): InputStream = getClass.getResourceAsStream(fileName)
}
