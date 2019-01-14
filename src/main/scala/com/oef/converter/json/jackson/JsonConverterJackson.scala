package com.oef.converter.json.jackson
import java.io.InputStream

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.oef.converter.json.JsonConverter

class JsonConverterJackson extends JsonConverter {
  private val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  def jsonToMap(json: InputStream): Map[String, Any] = {
    if (json.available() == 0) throw new IllegalArgumentException("Empty data is not a valid json.")
    else {
      mapper.readTree(json)
      Map()
    }

  }
}
