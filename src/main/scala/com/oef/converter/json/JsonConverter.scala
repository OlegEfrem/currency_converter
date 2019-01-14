package com.oef.converter.json
import java.io.InputStream

import com.oef.converter.json.jackson.JsonConverterJackson

trait JsonConverter {
  def jsonToMap(json: InputStream): Map[String, Any]
  def fromJson[ToType](json: String)(implicit m: Manifest[ToType]): ToType
  def toJson(obj: Any): String
}

object JsonConverter {
  def apply(): JsonConverter = JsonConverterJackson
}
