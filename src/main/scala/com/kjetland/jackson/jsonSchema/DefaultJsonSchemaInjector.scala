package com.kjetland.jackson.jsonSchema

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.kjetland.jackson.jsonSchema.Utils.merge
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaInject
import com.kjetland.jackson.jsonSchema.implicits._

class DefaultJsonSchemaInjector extends JsonSchemaInjector[JsonSchemaInject] {

  override def inject(thisObjectNode:ObjectNode, objectMapper: ObjectMapper, config:JsonSchemaConfig, a: JsonSchemaInject): Unit = {
    val injectJsonNode = objectMapper.readTree(a.json())
    Option(a.jsonSupplier())
      .flatMap(cls => Option(cls.newInstance().get()))
      .foreach(json => merge(injectJsonNode, json))
    if (a.jsonSupplierViaLookup().nonEmpty) {
      val json = config.jsonSuppliers.getOrElse(a.jsonSupplierViaLookup(), throw new Exception(s"@JsonSchemaInject(jsonSupplierLookup='${a.jsonSupplierViaLookup()}') does not exist in config.jsonSupplierLookup-map")).get()
      merge(injectJsonNode, json)
    }
    a.strings().foreach(v => injectJsonNode.visit(v.path(), (o, n) => o.put(n, v.value())))
    a.ints().foreach(v => injectJsonNode.visit(v.path(), (o, n) => o.put(n, v.value())))
    a.bools().foreach(v => injectJsonNode.visit(v.path(), (o, n) => o.put(n, v.value())))

    if ( !a.merge()) {
      // Since we're not merging, we must remove all content of thisObjectNode before injecting.
      // We cannot just "replace" it with injectJsonNode, since thisObjectNode already have been added to its parent
      thisObjectNode.removeAll()
    }

    merge(thisObjectNode, injectJsonNode)
  }
}

