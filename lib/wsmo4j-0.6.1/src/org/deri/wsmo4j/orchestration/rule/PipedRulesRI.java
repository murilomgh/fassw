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

import org.deri.wsmo4j.io.serializer.wsml.*;
import org.wsmo.common.*;
import org.wsmo.service.orchestration.rule.*;
import org.wsmo.service.rule.*;

/**
 * Interface or class description
 *
 * @author James Scicluna
 * @author Thomas Haselwanter
 *
 * Created on 02-Feb-2006
 * Committed by $Author: vassil_momtchev $
 *
 * $Source: /cvsroot/wsmo4j/ext/orchestration/src/ri/org/deri/wsmo4j/orchestration/rule/PipedRulesRI.java,v $,
 * @version $Revision: 1.1 $ $Date: 2006/10/24 15:01:07 $
 */

public class PipedRulesRI implements OrchestrationPipedRules {

    /**
     * @uml.property   name="rules"
     * @uml.associationEnd   multiplicity="(0 -1)" elementType="org.wsmo.service.rule.ChoreographyRule"
     */
    private Set<Rule> rules;
    
    /**
     * 
     */
    public PipedRulesRI() {
        super();
        this.rules = new HashSet<Rule>();
        // TODO Auto-generated constructor stub
    }
    
    public PipedRulesRI(Set<Rule> rules){
        super();
        this.rules = rules;
    }

    /* (non-Javadoc)
     * @see org.wsmo.service.choreography.rule.PipedRules#addPipedRule(org.wsmo.service.choreography.rule.Rule)
     */
    public void addPipedRule(Rule rule) {
        this.rules.add(rule);
    }

    /* (non-Javadoc)
     * @see org.wsmo.service.choreography.rule.PipedRules#removePipedRule(org.wsmo.service.choreography.rule.Rule)
     */
    public void removePipedRule(Rule rule) {
        this.rules.remove(rule);
    }

    /* (non-Javadoc)
     * @see org.wsmo.service.choreography.rule.PipedRules#listPipedRules()
     */
    public Set<Rule> listPipedRules() {
        return this.rules;
    }

    /* (non-Javadoc)
     * @see org.wsmo.service.choreography.rule.Rule#accept(org.wsmo.service.choreography.rule.Visitor)
     */
    public void accept(Visitor visitor) {
        visitor.visitOrchestrationPipedRules(this);
    }
    
    /* (non-Javadoc)
     * @see org.wsmo.service.choreography.rule.Rule#toString(org.wsmo.common.TopEntity)
     */
    public String toString(TopEntity te) {
        VisitorSerializeWSMLTransitionRules visitor = new VisitorSerializeWSMLTransitionRules(te);
        visitor.visitOrchestrationPipedRules(this);
        return visitor.getSerializedObject();
    }
    
    @Override
    public String toString() {
        return toString(null);
    }
}
