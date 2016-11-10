package io.skysail.server.http.query

import io.skysail.server.filter.ExprNode
import io.skysail.server.filter.FilterVisitor
import io.skysail.server.filter.Operation

abstract class AbstractExprNode(op: Operation) extends ExprNode {
  
  def accept(visitor: FilterVisitor): Object = {
    visitor.visit(this)
  }
  
  override def toString() = render()
}