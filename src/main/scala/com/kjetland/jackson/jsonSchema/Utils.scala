package com.kjetland.jackson.jsonSchema

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode

object Utils {
  def merge(mainNode: JsonNode, updateNode: JsonNode): Unit = {
    val fieldNames = updateNode.fieldNames()
    while (fieldNames.hasNext) {

      val fieldName = fieldNames.next()
      val jsonNode = mainNode.get(fieldName)
      // if field exists and is an embedded object
      if (jsonNode != null && jsonNode.isObject) {
        merge(jsonNode, updateNode.get(fieldName))
      }
      else {
        mainNode match {
          case node: ObjectNode =>
            // Overwrite field
            val value = updateNode.get(fieldName)
            node.set(fieldName, value)
          case _ =>
        }
      }
    }
  }
}
