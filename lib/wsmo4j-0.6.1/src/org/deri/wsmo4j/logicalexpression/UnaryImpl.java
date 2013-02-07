/*
 wsmo4j - a WSMO API and Reference Implementation
 Copyright (c) 2004, University of Innsbruck, Institute of Computer Science
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
import org.omwg.logicalexpression.*;


/**
 * This class reunites all unary logical expressions (e.g op LogExpr)
 *
 * @author DERI Innsbruck, reto.krummenacher@deri.org
 * @author DERI Innsbruck, thomas.haselwanter@deri.org
 * @version $Revision: 1.10 $ $Date: 2007/04/02 12:13:20 $
 * @see org.omwg.logicalexpression.Unary
 */
public abstract class UnaryImpl extends LogicalExpressionImpl
        implements Unary {

    protected LogicalExpression expr;

    /**
     * @param expr the logical expression
     * @throws in case the logical expression contains a nested CONSTRAINT
     */
    public UnaryImpl(LogicalExpression expr)
            throws IllegalArgumentException {
        setOperand(expr);
    }

    /**
     * @see CompoundExpression#listOperands()
     */
    public List <LogicalExpression> listOperands() {
        Vector <LogicalExpression> ret = new Vector <LogicalExpression> ();
        ret.add(expr);
        return ret;
    }

    public LogicalExpression getOperand() {
        return expr;
    }

    public abstract void accept(Visitor v);

    /**
     * <p>
     * If two objects are equal according to the <code>equals(Object)</code> method, then calling
     * the <code>hashCode</code> method on each of the two objects must produce the same integer
     * result. However, it is not required that if two objects are unequal according to
     * the <code>equals(Object)</code> method, then calling the <code>hashCode</code> method on each of the two
     * objects must produce distinct integer results.
     * </p>
     * <p>
     * This method should be overriden, when the <code>equals(Object)</code> method is overriden.
     * </p>
     * @return A hash code value for this Object.
     * @see java.lang.Object#hashCode()
     * @see java.lang.Object#equals(Object)
     * @see com.ontotext.wsmo4j.ontology.LogicalExpressionImpl#hashCode()
     */
    public int hashCode() {
        return expr.hashCode();
    }

    /* (non-Javadoc)
     * @see org.omwg.logicalexpression.Unary#setOperand(org.omwg.logicalexpression.LogicalExpression)
     */
    public void setOperand(LogicalExpression le) throws IllegalArgumentException {
        if (ExpressionContainmentUtil.contains(le, Constraint.class)) {
            throw new IllegalArgumentException(
                    "Unary Expression may not contain a nested CONSTRAINT");
        }
        expr = le;
    }

    /* (non-Javadoc)
     * @see org.omwg.logicalexpression.CompoundExpression#setOperands(java.util.List)
     */
    public void setOperands(List operands) {
        if (operands.size()>1){
            throw new IllegalArgumentException("Unaries only take one argument!");
        }
        if (! (operands instanceof LogicalExpression)){
            throw new IllegalArgumentException("Operand of unary must be of type LogicalExpression");
        }
        setOperand((LogicalExpression)operands.get(0));
    }
}