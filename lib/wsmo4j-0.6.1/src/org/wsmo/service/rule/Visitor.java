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

package org.wsmo.service.rule;

import org.wsmo.service.choreography.rule.*;
import org.wsmo.service.orchestration.rule.*;

/**
 * Visitor for Choreography Rules.
 * 
 * <pre>
 *     Created on Jul 26, 2005
 *     Committed by $Author: haselwanter $
 *     $Source: /cvsroot/wsmo4j/ext/choreography/src/api/org/wsmo/service/rule/Visitor.java,v $
 * </pre>
 * 
 * @author James Scicluna
 * @author Thomas Haselwanter
 * @author Holger Lausen
 * 
 * @version $Revision: 1.2 $ $Date: 2006/11/03 13:43:59 $
 */
public interface Visitor {

    /**
     * Visitor for if-then rules.
     * 
     * @param rule
     *            IfThen rule object to be visited.
     */
    public void visitChoreographyIfThen(ChoreographyIfThen rule);

    /**
     * Visitor for forAll rules.
     * 
     * @param rule
     *            ForAll rule object to be visited.
     */
    public void visitChoreographyForAll(ChoreographyForAll rule);

    /**
     * Visitor for choose rules.
     * 
     * @param rule
     *            Choose rule object to be visited.
     */
    public void visitChoreographyChoose(ChoreographyChoose rule);
    
    /**
     * Visitor for if-then rules.
     * 
     * @param rule
     *            IfThen rule object to be visited.
     */
    public void visitOrchestrationIfThen(OrchestrationIfThen rule);

    /**
     * Visitor for forAll rules.
     * 
     * @param rule
     *            ForAll rule object to be visited.
     */
    public void visitOrchestrationForAll(OrchestrationForAll rule);

    /**
     * Visitor for choose rules.
     * 
     * @param rule
     *            Choose rule object to be visited.
     */
    public void visitOrchestrationChoose(OrchestrationChoose rule);

    /**
     * Visitor for add(newFact) update rules.
     * 
     * @param rule
     *            Add update rule object to be visited.
     */
    public void visitAdd(Add rule);

    /**
     * Visitor for delete(oldFact) update rules.
     * 
     * @param rule
     *            Delete update rule object to be visited.
     */
    public void visitDelete(Delete rule);

    /**
     * Visitor for update(newFact)/update(newFact,oldFact) update rules.
     * 
     * @param rule
     *            Update rule object to be visited.
     */
    public void visitUpdate(Update rule);
    
    /**
     * Visitor for rule_1 | rule_2 |....| rule_n
     * 
     * @param rules PipedRules to be visited
     */
    public void visitChoreographyPipedRules(ChoreographyPipedRules rules);
    
    /**
     * Visitor for rule_1 | rule_2 |....| rule_n
     * 
     * @param rules PipedRules to be visited
     */
    public void visitOrchestrationPipedRules(OrchestrationPipedRules rules);
    
    /**
     * Visitor for perform rule
     * 
     * @param rule Perform rule to be visited
     */
    public void visitOrchestrationAchieveGoal(OrchestrationAchieveGoal rule);

    /**
     * Visitor for perform rule
     * 
     * @param rule Perform rule to be visited
     */
    public void visitOrchestrationInvokeService(OrchestrationInvokeService rule);

    /**
     * Visitor for perform rule
     * 
     * @param rule Perform rule to be visited
     */
    public void visitOrchestrationApplyMediation(OrchestrationApplyMediation rule);

    /**
     * Visitor for receive rule
     * 
     * @param rule receive rule to be visited
     */
    public void visitReceive(Receive rule);

    /**
     * Visitor for send rule
     * 
     * @param rule send rule to be visited
     */
    public void visitSend(Send rule);

}