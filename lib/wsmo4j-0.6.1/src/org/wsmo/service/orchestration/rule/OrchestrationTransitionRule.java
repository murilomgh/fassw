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

package org.wsmo.service.orchestration.rule;

import org.wsmo.service.rule.*;

import java.util.Set;

/**
 * Defines the simplest type of transition rule which takes the form of "if
 * Condition then Rule".
 * 
 * <pre>
 *      Created on Jul 26, 2005
 *      Committed by $Author: vassil_momtchev $
 *      $Source: /cvsroot/wsmo4j/ext/orchestration/src/api/org/wsmo/service/orchestration/rule/OrchestrationTransitionRule.java,v $
 * </pre>
 * 
 * @author James Scicluna
 * @author Thomas Haselwanter
 * @author Holger Lausen
 * 
 * @version $Revision: 1.1 $ $Date: 2006/10/24 14:59:38 $
 */
public interface OrchestrationTransitionRule extends Rule {

    /**
     * Returns the Logical Expression which defines the condition of the rule.
     * 
     * @return Logical Expression defining the condition of the rule
     */
    public Condition getCondition();

    /**
     * Sets the Conditon of the rule.
     * 
     * @param condition
     *            A Condition object defining the condition of the rule
     */
    public void setCondition(Condition condition);

    /**
     * Returns the list of Rule objects nested inside the transition rule
     * 
     * @return Set of Rule objects
     */
    public Set<Rule> listNestedRules();
    
    
    /**
     * Sets the inner rules of the rule
     * 
     * @param rules A set of Rule objects to be set
     * as the inner rules
     */
    public void setRules(Set<Rule> rules);

    /**
     * Removes all the nested rules
     * 
     */
    public void clearNestedRules();

    /**
     * Adds a nested Rule object
     * 
     * @param rule
     *            Adds a nested Rule object
     */
    public void addRule(Rule rule);

    /**
     * Adds a set of rules
     * 
     * @param rules
     *            The set of Rule objects to be added
     */
    public void addRules(Set<Rule> rules);

    /**
     * Removes a nested rule
     * 
     * @param rule
     *            Rule object to be removed
     */
    public void removeRule(Rule rule);

}