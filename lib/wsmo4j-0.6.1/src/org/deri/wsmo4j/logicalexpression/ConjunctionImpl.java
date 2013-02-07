/*
 wsmo4j - a WSMO API and Reference Implementation
 Copyright (c) 2005 University of Innsbruck, Austria
               2005 National University of Ireland, Galway
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


import org.omwg.logicalexpression.*;


/**
 * @author DERI Innsbruck, reto.krummenacher@deri.org
 * @author DERI Innsbruck, thomas.haselwanter@deri.org
 */
public class ConjunctionImpl
        extends BinaryImpl
        implements Conjunction {

    public ConjunctionImpl(LogicalExpression exprLeft, LogicalExpression exprRight)
            throws IllegalArgumentException {
        super(exprLeft, exprRight);
    }

    public void accept(Visitor v) {
        v.visitConjunction(this);
    }

    /**
     * <p>
     * The <code>equals</code> method implements an equivalence relation
     * on non-null object references. Binary expressions are equal if their operator, their
     * left logical expression and their right logical expression are equal.
     * In case the operator is either EQUIVALENT, OR or AND, the left logical expression
     * must to be equal to either the left or the right logical expression of the obj
     * argument and analogous for the right logical expression.
     * </p>
     * <p>
     * It is generally necessary to override the <code>hashCode</code> method whenever this method
     * is overridden.
     * </p>
     * @param o the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     *          argument; <code>false</code> otherwise.
     * @see java.lang.Object#equals(java.lang.Object)
     * @see java.lang.Object#hashCode()
     */
    public boolean equals(Object o) {
        if (o instanceof Conjunction) {
            Conjunction b = (Conjunction)o;
            return ((b.getLeftOperand().equals(exprLeft)
                     && b.getRightOperand().equals(exprRight))
                    || (b.getRightOperand().equals(exprLeft)
                        && b.getLeftOperand().equals(exprRight)));
        }
        return false;
    }
}
