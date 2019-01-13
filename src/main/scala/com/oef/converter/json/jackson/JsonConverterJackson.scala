package com.oef.converter.json.jackson
import java.io.InputStream

import com.oef.converter.json.JsonConverter

class JsonConverterJackson extends JsonConverter {
  def jsonToMap(stream: InputStream): Map[String,Any] = Map()
}
