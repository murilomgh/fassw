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

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omwg.ontology.Variable;
import org.wsmo.service.choreography.rule.ChoreographyQuantifiedRule;
import org.wsmo.service.rule.*;

/**
 * Provides common implementation methods for the quantified rules (Forall and
 * Choose).
 * 
 * <pre>
 *     Created on Jul 26, 2005
 *     Committed by $Author: vassil_momtchev $
 *     $Source: /cvsroot/wsmo4j/ext/choreography/src/ri/org/deri/wsmo4j/choreography/rule/AbstractQuantifiedRuleRI.java,v $
 * </pre>
 * 
 * @author Thomas Haselwanter
 * @author James Scicluna
 * 
 * @version $Revision: 1.7 $ $Date: 2006/10/24 14:11:47 $
 */
public abstract class AbstractQuantifiedRuleRI extends AbstractTransitionRuleRI implements
        ChoreographyQuantifiedRule {

    protected static <E> Set<E> makeSet(E element) {
        Set<E> s = new HashSet<E>();
        s.add(element);
        return s;
    }

    protected Set<Variable> variables = new HashSet<Variable>();

    /**
     * @param variables
     *            A Set of Quantified Variable objects
     * @param condition
     *            Logical Expression defining the condition of the rule
     * @param rules
     *            A set of inner Rule objects of the rule
     */
    public AbstractQuantifiedRuleRI(Set<Variable> variables, Condition condition, Set<ChoreographyRule> rules) {
        super();
        this.variables = variables;
        this.condition = condition;
        this.rules = rules;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.rule.QuantifiedRule#listVariables()
     */
    public Set<Variable> listVariables() {
        return Collections.unmodifiableSet(variables);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.rule.QuantifiedRule#addVariable(org.omwg.ontology.Variable)
     */
    public void addVariable(Variable variable) {
        if (variable != null)
            variables.add(variable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.rule.QuantifiedRule#addVariable(java.util.Set)
     */
    public void addVariables(Set<Variable> variable) {
        if (variable != null)
            variables.addAll(variable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.rule.QuantifiedRule#removeVariable(org.omwg.ontology.Variable)
     */
    public void removeVariable(Variable variable) {
        variables.remove(variable);
    }

    protected String stringVariables() {
        String s = "";
        Iterator<Variable> i = this.variables.iterator();
        while (i.hasNext()) {
            s += i.next().toString();
            if (i.hasNext())
                s += ",";
        }
        return s;
    }

}
