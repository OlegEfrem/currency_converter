package com.oef.converter.json
import java.io.InputStream

trait JsonConverter {
  def jsonToMap(json: InputStream): Map[String, Any]
  def fromJson[ToType](json: String): ToType
  def toJson(obj: Any): String
}
