package com.oef.converter.json.jackson
import java.io.InputStream
import com.fasterxml.jackson.databind.{DeserializationFeature, JsonNode, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.oef.converter.json.JsonConverter
import scala.annotation.tailrec
import scala.collection.JavaConverters._
import scala.collection.immutable.ListMap

class JsonConverterJackson extends JsonConverter {
  private val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  def jsonToMap(json: InputStream): Map[String, Any] = {
    if (json.available() == 0) throw new IllegalArgumentException("Empty data is not a valid json.")
    val jsonNode = mapper.readTree(json)
    nodeToMap(nodeFieldsToListMap(jsonNode).toList)
  }

  @tailrec
  private def nodeToMap(fields: List[(String, Any)], map: ListMap[String, Any] = ListMap()): Map[String, Any] = {
    fields match {
      case Nil => map
      case (name, value) :: t =>
        value match {
          case v: JsonNode => nodeToMap(nodeFieldsToListMap(v).toList, map + (name -> nodeToAny(v)))
          case v: Any      => nodeToMap(t, map + (name                             -> v))
        }
    }
  }

  private def nodeToAny(jsonNode: JsonNode): Any = {
    import com.fasterxml.jackson.databind.node.JsonNodeType._
    jsonNode.getNodeType match {
      case OBJECT | POJO | ARRAY => nodeFieldsToListMap(jsonNode)
      case BINARY                => jsonNode.binaryValue()
      case BOOLEAN               => jsonNode.booleanValue()
      case NUMBER                => jsonNode.numberValue()
      case STRING                => jsonNode.textValue()
      case MISSING | NULL        => ""
    }
  }

  private def nodeFieldsToListMap(jsonNode: JsonNode): ListMap[String, Any] = {
    new ListMap ++ jsonNode.fields().asScala.map(jMap => jMap.getKey -> nodeToAny(jMap.getValue))
  }

}
