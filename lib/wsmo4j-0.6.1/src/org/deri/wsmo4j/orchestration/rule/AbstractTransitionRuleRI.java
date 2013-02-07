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

package org.deri.wsmo4j.orchestration.rule;

import java.util.*;

import org.wsmo.service.orchestration.rule.*;
import org.wsmo.service.rule.*;

/**
 * Implements common functionality for a Transition Rule.
 * 
 * <pre>
 *     Created on Jul 26, 2005
 *     Committed by $Author: vassil_momtchev $
 *     $Source: /cvsroot/wsmo4j/ext/orchestration/src/ri/org/deri/wsmo4j/orchestration/rule/AbstractTransitionRuleRI.java,v $
 * </pre>
 * 
 * @author Thomas Haselwanter
 * @author James Scicluna
 * 
 * @version $Revision: 1.1 $ $Date: 2006/10/24 15:01:06 $
 */
public abstract class AbstractTransitionRuleRI implements OrchestrationTransitionRule {

    protected Condition condition;

    /**
     * @uml.property   name="rules"
     * @uml.associationEnd   multiplicity="(0 -1)" elementType="org.wsmo.service.rule.OrchestrationRule"
     */
    protected Set<Rule> rules = new HashSet<Rule>();

    /**
     * Default constructor for Abstract Transition Rules
     */
    public AbstractTransitionRuleRI() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.orchestration.rule.TransitionRule#getCondition()
     */
    public Condition getCondition() {
        return this.condition;
    }

    /* (non-Javadoc)
     * @see org.wsmo.service.orchestration.rule.TransitionRule#setCondition(org.wsmo.service.choreography.rule.Condition)
     */
    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.orchestration.rule.TransitionRule#listNestedRules()
     */
    public Set<Rule> listNestedRules() {
        return Collections.unmodifiableSet(rules);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.orchestration.rule.TransitionRule#clearNestedRules()
     */
    public void clearNestedRules() {
        this.rules.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.orchestration.rule.TransitionRule#add(org.wsmo.service.orchestration.rule.Rule)
     */
    public void addRule(Rule rule) {
        if (rule != null)
            rules.add(rule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.orchestration.rule.TransitionRule#add(java.util.Set)
     */
    public void addRules(Set<Rule> rules) {
        if (rules != null)
            rules.addAll(rules);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.orchestration.rule.TransitionRule#removeRule(org.wsmo.service.orchestration.rule.Rule)
     */
    public void removeRule(Rule rule) {
        rules.remove(rule);
    }

    /* (non-Javadoc)
     * @see org.wsmo.service.choreography.rule.TransitionRule#setRules(java.util.Set)
     */
    public void setRules(Set<Rule> rules) {
        this.rules = rules;
    }

    protected String stringInnerRules() {
        String s = "";
        Rule r;
        Iterator<Rule> i = this.rules.iterator();
        while (i.hasNext()) {
            r = i.next();
            s += r.toString() + "\n";
        }
        return s;
    }
}