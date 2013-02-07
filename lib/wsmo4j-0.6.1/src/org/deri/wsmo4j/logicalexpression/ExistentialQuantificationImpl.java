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


import java.util.*;

import org.deri.wsmo4j.logicalexpression.util.*;
import org.omwg.logicalexpression.*;
import org.omwg.ontology.*;


/**
 * @author DERI Innsbruck, reto.krummenacher@deri.org
 * @author DERI Innsbruck, thomas.haselwanter@deri.org
 */
public class ExistentialQuantificationImpl
        extends QuantifiedImpl
        implements ExistentialQuantification {

    public ExistentialQuantificationImpl(Variable variable, LogicalExpression expr)
            throws IllegalArgumentException {
        this(SetUtil.createSet(variable), expr);
    }

    public ExistentialQuantificationImpl(Set <Variable> variables, LogicalExpression expr)
            throws IllegalArgumentException {
        super(variables, expr);
    }

    public void accept(Visitor v) {
        v.visitExistentialQuantification(this);
    }

    /**
     * <p>
     * The <code>equals</code> method implements an equivalence relation
     * on non-null object references. Quantified expressions are equal if their operator,
     * their logical expresssion and their set of variables are equal.
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
        if (o instanceof ExistentialQuantification) {
            ExistentialQuantification q = (ExistentialQuantification)o;
            if (!variables.equals(q.listVariables())) {
                return false;
            }
            return expr.equals(q.getOperand());
        }
        return false;
    }

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
     * @see java.util.Set#hashCode()
     */

}
