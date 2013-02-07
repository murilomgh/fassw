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
import org.wsmo.common.*;
import org.wsmo.service.choreography.rule.ChoreographyIfThen;
import org.wsmo.service.rule.*;

/**
 * Reference Implementation for the IfThen rule.
 * 
 * <pre>
 *     Created on Jul 26, 2005
 *     Committed by $Author: vassil_momtchev $
 *     $Source: /cvsroot/wsmo4j/ext/choreography/src/ri/org/deri/wsmo4j/choreography/rule/IfThenRI.java,v $
 * </pre>
 * 
 * @author Thomas Haselwanter
 * @author James Scicluna
 * 
 * @version $Revision: 1.10 $ $Date: 2006/10/24 14:11:47 $
 */
public class IfThenRI extends AbstractTransitionRuleRI implements ChoreographyIfThen {

    /**
     * @param condition
     *            LogicalExpression object defining the condition of the rule.
     * @param rule
     *            An inner Rule object of the rule
     */
    public IfThenRI(Condition condition, ChoreographyRule rule) {
        this.setCondition(condition);
        rules.clear();
        rules.add(rule);
    }

    /**
     * @param condition
     *            LogicalExpression object defining the condition of the rule.
     * @param rules
     *            A set of inner Rule objects of the rule
     */
    public IfThenRI(Condition condition, Set<ChoreographyRule> rules) {
        this.setCondition(condition);
        this.setRules(rules);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.rule.Rule#accept(org.wsmo.service.choreography.rule.Visitor)
     */
    public void accept(Visitor visitor) {
        visitor.visitChoreographyIfThen(this);
    }

    /* (non-Javadoc)
     * @see org.wsmo.service.choreography.rule.Rule#toString(org.wsmo.common.TopEntity)
     */
    public String toString(TopEntity te) {
        VisitorSerializeWSMLTransitionRules visitor = new VisitorSerializeWSMLTransitionRules(te);
        visitor.visitChoreographyIfThen(this);
        return visitor.getSerializedObject();
    }
    
    @Override
    public String toString() {
        return toString(null);
    }
}
