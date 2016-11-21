package io.skysail.server.http.query.nodes

import io.skysail.server.filter.Operation
import io.skysail.server.domain.jvm.FieldFacet

import java.util.Collections;
import io.skysail.server.filter.ExprNode
import io.skysail.server.http.query.AbstractExprNode

abstract class BranchNode(op: Operation, attribute: String, value: String) extends AbstractExprNode(op) {

   def this(op: Operation, attribute: String) {
    this(op, attribute, null);
  }

  def getSelected(x$1: FieldFacet, x$2: java.util.Map[String, String]): java.util.Set[String] = {
     null
   }

  def isLeaf() = false 

  def reduce(x$1: String, x$2: FieldFacet, x$3: String): ExprNode = {
     ???
   }

  def getKeys(): java.util.Set[String] = {
     var result = new java.util.HashSet[String]();
//     for (ExprNode exprNode : childList) {
//         result.addAll(exprNode.getKeys());
//     }
     result
  }

  def evaluateValue(x$1: Any): Boolean = {
     ???
   }
}