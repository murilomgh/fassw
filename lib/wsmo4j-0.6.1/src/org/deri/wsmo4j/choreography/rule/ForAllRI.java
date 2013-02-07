/*
 wsmo4j extension - a Choreography API and Reference Implementation

 Copyright (c) 2005, University of Innsbruck, Austria

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

package org.deri.wsmo4j.choreography.rule;

import java.util.Set;

import org.deri.wsmo4j.io.serializer.wsml.*;
import org.omwg.ontology.Variable;
import org.wsmo.common.*;
import org.wsmo.service.choreography.rule.ChoreographyForAll;
import org.wsmo.service.rule.*;

/**
 * Reference Implementation for the ForAll rule.
 * 
 * <pre>
 *     Created on Jul 26, 2005
 *     Committed by $Author: vassil_momtchev $
 *     $Source: /cvsroot/wsmo4j/ext/choreography/src/ri/org/deri/wsmo4j/choreography/rule/ForAllRI.java,v $
 * </pre>
 * 
 * @author Thomas Haselwanter
 * @author James Scicluna
 * 
 * @version $Revision: 1.9 $ $Date: 2006/10/24 14:11:47 $
 */
public class ForAllRI extends AbstractQuantifiedRuleRI implements ChoreographyForAll {

    /**
     * Constructor for a forall rule.
     * 
     * @param variable
     *            Quantified variable object.
     * @param condition
     *            A LogicalExpression defining the condition of the rule
     * @param rule
     *            Inner Rule of forall
     */
    public ForAllRI(Variable variable, Condition condition, ChoreographyRule rule) {
        this(makeSet(variable), condition, makeSet(rule));
    }

    /**
     * Constructor for a forall rule
     * 
     * @param variables
     *            A set of quantified Variable objects
     * @param condition
     *            A LogicalExpression defining the condition of the rule
     * @param rule
     *            Inner Rule of forall
     */
    public ForAllRI(Set<Variable> variables, Condition condition, ChoreographyRule rule) {
        this(variables, condition, makeSet(rule));
    }

    /**
     * Constructor for a choose rule.
     * 
     * @param variable
     *            Quantified variable object.
     * @param condition
     *            A LogicalExpression defining the condition of the rule
     * @param rules
     *            A set of inner Rule objects
     */
    public ForAllRI(Variable variable, Condition condition, Set<ChoreographyRule> rules) {
        this(makeSet(variable), condition, rules);
    }

    /**
     * Constructor for a choose rule
     * 
     * @param variables
     *            A set of quantified Variable objects
     * @param condition
     *            A LogicalExpression defining the condition of the rule
     * @param rules
     *            A set of inner Rule objects
     */
    public ForAllRI(Set<Variable> variables, Condition condition, Set<ChoreographyRule> rules) {
        super(variables, condition, rules);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.rule.Rule#accept(org.wsmo.service.choreography.rule.Visitor)
     */
    public void accept(Visitor visitor) {
        visitor.visitChoreographyForAll(this);
    }

    /* (non-Javadoc)
     * @see org.wsmo.service.choreography.rule.Rule#toString(org.wsmo.common.TopEntity)
     */
    public String toString(TopEntity te) {
        VisitorSerializeWSMLTransitionRules visitor = new VisitorSerializeWSMLTransitionRules(te);
        visitor.visitChoreographyForAll(this);
        return visitor.getSerializedObject();
    }
    
    @Override
    public String toString() {
        return toString(null);
    }
}