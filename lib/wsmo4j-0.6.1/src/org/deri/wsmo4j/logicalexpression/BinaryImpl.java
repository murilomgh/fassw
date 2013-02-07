/*
 wsmo4j - a WSMO API and Reference Implementation
 Copyright (c) 2004, DERI Innsbruck
 This library is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the Free
 Software Foundation; either version 2.1 of the License, or (at your option)
 any later version.
 This library is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 details.
 You should have received a copy of the GNU Lesser General Public License along
 with this library; if not, write to the Free Software Foundation, Inc.,
 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.deri.wsmo4j.logicalexpression;


import java.util.List;
import java.util.Vector;

import org.deri.wsmo4j.logicalexpression.util.ExpressionContainmentUtil;
import org.deri.wsmo4j.logicalexpression.util.SetUtil;
import org.omwg.logicalexpression.*;


/**
 * This class reunites all binary logical expressions
 * (e.g LogExpr1 op LogExpr2)
 * @author DERI Innsbruck, reto.krummenacher@deri.org
 * @author DERI Innsbruck, thomas.haselwanter@deri.org
 * @version $Revision: 1.11 $ $Date: 2007/04/02 12:13:20 $
 * @see org.omwg.logicalexpression.Binary
 */
public abstract class BinaryImpl extends LogicalExpressionImpl
        implements Binary {

    protected LogicalExpression exprLeft;

    protected LogicalExpression exprRight;

    /**
     * @param exprLeft the logical expression left of the operator
     * @param exprRight the logical expression right of the operator
     * @throws IllegalArgumentException
     * <p>in case one of the two logical expressions is null
     * <p>in case the operator is different from AND, EQUIVALENT, IMPLIEDBY, IMPLIES,
     * LP_IMPL or OR</p>
     * <p>in case one of the two logical expressions contains a nested CONSTRAINT</p>
     */
    public BinaryImpl(LogicalExpression exprLeft, LogicalExpression exprRight)
            throws IllegalArgumentException {
        if (exprLeft == null || exprRight == null) {
            throw new IllegalArgumentException("null not allowed for Arguments of Binary operator");
        }
        if (ExpressionContainmentUtil.contains(exprLeft, Constraint.class)
            || ExpressionContainmentUtil.contains(exprRight, Constraint.class)) {
            throw new IllegalArgumentException("Nested Constraint not allowed in Binary Operator");
        }
        this.exprLeft = exprLeft;
        this.exprRight = exprRight;
    }

    public LogicalExpression getLeftOperand() {
        return exprLeft;
    }

    public LogicalExpression getRightOperand() {
        return exprRight;
    }

    /**
     * @see CompoundExpression#listOperands()
     */
    public List <LogicalExpression> listOperands() {
        Vector <LogicalExpression> ret = new Vector <LogicalExpression>();
        ret.add(exprLeft);
        ret.add(exprRight);
        return ret;
    }

    public int hashCode() {
        return exprLeft.hashCode() + exprRight.hashCode();
    }

    /* (non-Javadoc)
     * @see org.omwg.logicalexpression.Binary#setLeftOperand(org.omwg.logicalexpression.LogicalExpression)
     */
    public void setLeftOperand(LogicalExpression le) {
        if (le == null) {
            throw new IllegalArgumentException("null not allowed for Arguments of Binary operator");
        }
        if (ExpressionContainmentUtil.contains(le, Constraint.class)) {
            throw new IllegalArgumentException("Nested Constraint not allowed in Binary Operator");
        }
        exprLeft = le;
        
    }

    /* (non-Javadoc)
     * @see org.omwg.logicalexpression.Binary#setRightOperand(org.omwg.logicalexpression.LogicalExpression)
     */
    public void setRightOperand(LogicalExpression le) {
        if (le == null) {
            throw new IllegalArgumentException("null not allowed for Arguments of Binary operator");
        }
        if (ExpressionContainmentUtil.contains(le, Constraint.class)) {
            throw new IllegalArgumentException("Nested Constraint not allowed in Binary Operator");
        }
        exprRight = le;  
    }

    /* (non-Javadoc)
     * @see org.omwg.logicalexpression.CompoundExpression#setOperands(java.util.List)
     */
    public void setOperands(List operands) {
        if (operands.size()!=2){
            throw new IllegalArgumentException("For a Binary Operator exactly 2 expressions are necessary!");
        }
        if (!SetUtil.allOfType(operands, LogicalExpression.class)){
            throw new IllegalArgumentException("Operands of a Binary must be Logical Expressions!!");
        }
        setLeftOperand((LogicalExpression)operands.get(0));
        setRightOperand((LogicalExpression)operands.get(1));
    }
}