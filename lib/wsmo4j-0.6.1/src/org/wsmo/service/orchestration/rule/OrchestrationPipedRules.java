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

import java.util.Set;

import org.wsmo.service.rule.*;

/**
 * Interface or class description
 *
 * @author James Scicluna
 * @author Thomas Haselwanter
 * @author Vassil Momtchev
 *
 * Created on 02-Feb-2006
 * Committed by $Author: vassil_momtchev $
 *
 * $Source: /cvsroot/wsmo4j/ext/orchestration/src/api/org/wsmo/service/orchestration/rule/OrchestrationPipedRules.java,v $,
 * @version $Revision: 1.1 $ $Date: 2006/10/24 14:59:38 $
 */

public interface OrchestrationPipedRules extends Rule {

    /**
     * Adds a rule to the list of piped rules
     * 
     * @param rule Rule object to be added
     */
    public void addPipedRule(Rule rule);
    
    /**
     * Removes a rule from the list of piped rules
     * 
     * @param rule Rule object to be removed
     */
    public void removePipedRule(Rule rule);
    
    /**
     * returns the set of Piped Rules
     * 
     * @return A Set of Rule objects
     */
    public Set<Rule> listPipedRules();
}
