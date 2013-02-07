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


import java.util.*;

import org.deri.wsmo4j.logicalexpression.util.*;

import org.omwg.logicalexpression.*;
import org.omwg.ontology.*;


/**
 * This abstract class reunites all quantified logical expressions
 * (e.g qfier {Term} LogExpr)
 * @author DERI Innsbruck, reto.krummenacher@deri.org
 * @version $Revision: 1.5 $ $Date: 2007/04/02 12:13:20 $
 * @see org.omwg.logicalexpression.Quantified
 */
public abstract class QuantifiedImpl
        extends UnaryImpl
        implements Quantified {

    protected Set <Variable> variables;

    /**
     * @param variables the set of variables that are quantified for the expression
     * @param expr the logical expression
     * @throws IllegalArgumentException
     * <p>in case the operator is different from EXISTS or FORALL</p>
     * <p>in case the logical expression contains a nested CONSTRAINT</p>
     * <p>in case the Set of variables is null</p>
     * <p>in case the arguments of the list aren't all of Type Variable</p>
     */
    public QuantifiedImpl(Set <Variable> variables, LogicalExpression expr)
            throws IllegalArgumentException {
        super(expr);
        setVariables(variables);

    }

    /**
     * @return the set of variables that are quantified for the expression
     * @see org.omwg.logicalexpression.Quantified#listVariables()
     */
    public Set <Variable> listVariables() {
        return variables;
    }

    /**
     * @see org.omwg.logicalexpression.LogicalExpression#accept(org.omwg.logicalexpression.Visitor)
     */
    public abstract void accept(Visitor v);

    /* (non-Javadoc)
     * @see org.omwg.logicalexpression.Quantified#setVariables(java.util.Set)
     */
    public void setVariables(Set <Variable> variables) {
        if (variables == null) {
            throw new IllegalArgumentException("Quantified Expression must have at least one Variable");
        }
        if (!SetUtil.allOfType(variables, Variable.class)) {
            throw new IllegalArgumentException("Set variables may only contain org.omwg.logicalexpression.variables");
        }
        this.variables = Collections.unmodifiableSet(variables);        
    }

}
