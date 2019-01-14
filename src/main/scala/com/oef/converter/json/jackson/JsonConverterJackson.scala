package com.oef.converter.json.jackson

import java.io.InputStream

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.{DeserializationFeature, JsonNode, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.oef.converter.config.Configuration
import com.oef.converter.json.JsonConverter

import scala.annotation.tailrec
import scala.collection.JavaConverters._
import scala.collection.immutable.ListMap

object JsonConverterJackson extends JsonConverter {
  private val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.setDateFormat(Configuration.dateFormat)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)

  def jsonToMap(json: InputStream): Map[String, Any] = {
    if (json.available() == 0) throw new IllegalArgumentException("Empty data is not a valid json.")
    val jsonNode = mapper.readTree(json)
    nodeFieldsToMap(nodeToMap(jsonNode).toList)
  }

  @tailrec
  private def nodeFieldsToMap(fields: List[(String, Any)], map: ListMap[String, Any] = ListMap()): Map[String, Any] = {
    fields match {
      case Nil => map
      case (name, value) :: t =>
        value match {
          case v: JsonNode => nodeFieldsToMap(nodeToMap(v).toList, map + (name -> nodeToAny(v)))
          case v: Any      => nodeFieldsToMap(t, map + (name                   -> v))
        }
    }
  }

  private def nodeToMap(jsonNode: JsonNode): ListMap[String, Any] = {
    new ListMap ++ jsonNode.fields().asScala.map(jMap => jMap.getKey -> nodeToAny(jMap.getValue))
  }

  private def nodeToAny(jsonNode: JsonNode): Any = {
    import com.fasterxml.jackson.databind.node.JsonNodeType._
    jsonNode.getNodeType match {
      case ARRAY          => arrayNodeToList(jsonNode.asInstanceOf[ArrayNode])
      case OBJECT | POJO  => nodeToMap(jsonNode)
      case BINARY         => jsonNode.binaryValue()
      case BOOLEAN        => jsonNode.booleanValue()
      case NUMBER         => jsonNode.numberValue()
      case STRING         => jsonNode.textValue()
      case MISSING | NULL => ""
    }
  }

  private def arrayNodeToList(jsonNode: ArrayNode): List[Any] = {
    jsonNode.iterator().asScala.map(nodeToAny).toList
  }
  override def fromJson[ToType](json: String)(implicit m: Manifest[ToType]): ToType = {
    mapper.readValue[ToType](json)
  }

  override def toJson(obj: Any): String = {
    mapper.writeValueAsString(obj)
  }
}
