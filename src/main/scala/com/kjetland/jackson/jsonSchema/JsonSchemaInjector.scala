package com.kjetland.jackson.jsonSchema

import java.lang.annotation.Annotation

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode

trait JsonSchemaInjector[A <: Annotation] {
  def inject(thisObjectNode:ObjectNode, objectMapper: ObjectMapper, config:JsonSchemaConfig, a: A): Unit
}
