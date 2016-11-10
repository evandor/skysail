package io.skysail.server.http.query

import io.skysail.server.filter.Operation
import io.skysail.server.domain.jvm.FieldFacet

import java.util.Collections;
import io.skysail.server.filter.ExprNode

abstract class LeafNode(op: Operation, attribute: String, value: String) extends AbstractExprNode(op) {
  
  def this(op: Operation, attribute: String) {
    this(op, attribute, null);
  }
  
  def handleFacet(attributeName: String, format: String, facets: Map[String, FieldFacet], gotten: Object): Boolean;
  
  def isLeaf() = true

  def getSelected(x$1: FieldFacet): java.util.Set[String] = {
    Collections.emptySet();
  }

  def getKeys(): java.util.Set[String] = {
    val result = new java.util.HashSet[String]();
    val attributeWithoutFormat = attribute.split(";",2).apply(0);
    result.add(attributeWithoutFormat);
    result;
  }

  def reduce(x$1: String, x$2: FieldFacet, x$3: String): ExprNode = {
    ???
  }

  
}