package com.kjetland.jackson.jsonSchema

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode

object implicits {
  implicit class JsonNodeExtension(o:JsonNode) {
    def visit(path: String, f: (ObjectNode, String) => Unit): Unit = {
      var p = o

      val split = path.split('/')
      for (name <- split.dropRight(1)) {
        p = Option(p.get(name)).getOrElse(p.asInstanceOf[ObjectNode].putObject(name))
      }
      f(p.asInstanceOf[ObjectNode], split.last)
    }
  }
}
