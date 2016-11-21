package io.skysail.server.http.query.nodes

import io.skysail.server.filter.Operation
import io.skysail.server.filter.SqlFilterVisitor
import io.skysail.server.filter.PreparedStatement
import io.skysail.server.filter.EntityEvaluationFilterVisitor
import io.skysail.server.domain.jvm.FieldFacet
import io.skysail.server.filter.ExprNode

class AndNode(op: Operation, attribute: String) extends BranchNode(op, attribute) {    
  
  def createPreparedStatement(x$1: SqlFilterVisitor): PreparedStatement = {
    ???
  }

  def evaluateEntity(x$1: EntityEvaluationFilterVisitor): Boolean = {
    ???
  }

  override def reduce(x$1: String, x$2: FieldFacet, x$3: String): ExprNode = {
    ???
  }

  def asLdapString(): String = {
    ???
  }

  
}