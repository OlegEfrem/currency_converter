package com.oef.converter.config
import java.text.{DateFormat, SimpleDateFormat}

object SerDeConfig {
  val datePattern: String    = "yyyy-MM-dd"
  val dateFormat: DateFormat = new SimpleDateFormat(datePattern)
}
