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

import org.deri.wsmo4j.io.serializer.wsml.VisitorSerializeWSMLTransitionRules;
import org.deri.wsmo4j.rule.PerformRI;
import org.wsmo.common.IRI;
import org.wsmo.common.TopEntity;
import org.wsmo.service.Goal;
import org.wsmo.service.orchestration.rule.OrchestrationAchieveGoal;
import org.wsmo.service.rule.Visitor;

/**
 * Interface or class description
 *
 * @author James Scicluna
 * @author Thomas Haselwanter
 * @author Vassil Momtchev
 *
 * Created on 02-Feb-2006
 * Committed by $Author: morcen $
 *
 * $Source: /cvsroot/wsmo4j/ext/orchestration/src/ri/org/deri/wsmo4j/orchestration/rule/OrchestrationAchieveGoalRI.java,v $,
 * @version $Revision: 1.2 $ $Date: 2007/04/02 13:05:17 $
 */

public class OrchestrationAchieveGoalRI extends PerformRI implements OrchestrationAchieveGoal {

	private Goal goal;
	
	public OrchestrationAchieveGoalRI(IRI performance, Goal goal) {
		super(performance);
		this.goal = goal;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitOrchestrationAchieveGoal(this);
	}
	
    /* (non-Javadoc)
     * @see org.wsmo.service.choreography.rule.Rule#toString(org.wsmo.common.TopEntity)
     */
	public String toString(TopEntity te) {
        VisitorSerializeWSMLTransitionRules visitor = new VisitorSerializeWSMLTransitionRules(te);
        visitor.visitOrchestrationAchieveGoal(this);
        return visitor.getSerializedObject();
    }

	public Goal getGoal() {
		return goal;
	}
	
}
