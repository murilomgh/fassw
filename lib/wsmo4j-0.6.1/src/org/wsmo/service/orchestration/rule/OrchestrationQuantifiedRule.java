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

import java.util.*;

import org.omwg.ontology.*;

/**
 * A quantified rule is a sub-class of Transition Rule such did it defines
 * quantified variables. Such rules take the form of "forall" and "choose"
 * 
 * <pre>
 *     Created on Jul 26, 2005
 *     Committed by $Author: vassil_momtchev $
 *     $Source: /cvsroot/wsmo4j/ext/orchestration/src/api/org/wsmo/service/orchestration/rule/OrchestrationQuantifiedRule.java,v $
 * </pre>
 * 
 * @author James Scicluna
 * @author Thomas Haselwanter
 * @author Holger Lausen
 * 
 * @version $Revision: 1.1 $ $Date: 2006/10/24 14:59:38 $
 */
public interface OrchestrationQuantifiedRule extends OrchestrationTransitionRule {

    /**
     * Returns the set of Variable objects which are quantified by the Rule
     * 
     * @return Set of Variable objects
     */
    public Set<Variable> listVariables();

    /**
     * Adds a quantified variable object
     * 
     * @param var
     *            Variable object
     */
    public void addVariable(Variable var);

    /**
     * Adds a quantified variable object
     * 
     * @param variables
     *            Variable object
     */
    public void addVariables(Set<Variable> variables);

    /**
     * Removes a quantified variable object from the rule
     * 
     * @param var
     *            Variable object to be removed
     */
    public void removeVariable(Variable var);

}
