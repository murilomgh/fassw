/*
 wsmo4j extension - a Orchestration API and Reference Implementation

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

package org.deri.wsmo4j.orchestration.rule;

import java.util.Set;

import org.deri.wsmo4j.io.serializer.wsml.*;
import org.omwg.ontology.Variable;
import org.wsmo.common.*;
import org.wsmo.service.orchestration.rule.*;
import org.wsmo.service.rule.*;

/**
 * Reference Implementation for the Choose rule.
 * 
 * <pre>
 *     Created on Jul 26, 2005
 *     Committed by $Author: vassil_momtchev $
 *     $Source: /cvsroot/wsmo4j/ext/orchestration/src/ri/org/deri/wsmo4j/orchestration/rule/ChooseRI.java,v $
 * </pre>
 * 
 * @author Thomas Haselwanter
 * @author James Scicluna
 * 
 * @version $Revision: 1.1 $ $Date: 2006/10/24 15:01:06 $
 */
public class ChooseRI extends AbstractQuantifiedRuleRI implements OrchestrationChoose {

    /**
     * Constructor for a choose rule.
     * 
     * @param variable
     *            Quantified variable object.
     * @param condition
     *            A LogicalExpression defining the condition of the rule
     * @param rule
     *            Inner Rule of choose
     */
    public ChooseRI(Variable variable, Condition condition, Rule rule) {
        this(makeSet(variable), condition, makeSet(rule));
    }

    /**
     * Constructor for a choose rule
     * 
     * @param variables
     *            A set of quantified Variable objects
     * @param condition
     *            A LogicalExpression defining the condition of the rule
     * @param rule
     *            Inner Rule of choose
     */
    public ChooseRI(Set<Variable> variables, Condition condition, Rule rule) {
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
    public ChooseRI(Variable variable, Condition condition, Set<Rule> rules) {
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
    public ChooseRI(Set<Variable> variables, Condition condition, Set<Rule> rules) {
        super(variables, condition, rules);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.orchestration.rule.Rule#accept(org.wsmo.service.orchestration.rule.Visitor)
     */
    public void accept(Visitor visitor) {
        visitor.visitOrchestrationChoose(this);
    }

    /* (non-Javadoc)
     * @see org.wsmo.service.orchestration.rule.Rule#toString(org.wsmo.common.TopEntity)
     */
    public String toString(TopEntity te) {
        VisitorSerializeWSMLTransitionRules visitor = new VisitorSerializeWSMLTransitionRules(te);
        visitor.visitOrchestrationChoose(this);
        return visitor.getSerializedObject();
    }
    
    @Override
    public String toString() {
        return toString(null);
    }
}