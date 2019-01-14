package com.oef.converter.json.jackson
import java.io.InputStream

import com.oef.converter.json.JsonConverter

class JsonConverterJackson extends JsonConverter {
  def jsonToMap(stream: InputStream): Map[String, Any] = {
    if (stream.available() == 0) throw new IllegalArgumentException("Empty data is not a valid json.")
    Map()
  }
}
