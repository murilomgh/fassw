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

package org.deri.wsmo4j.rule;

import org.deri.wsmo4j.io.serializer.wsml.*;
import org.wsmo.common.*;
import org.wsmo.service.rule.*;

/**
 * Perform Implementation.
 * 
 * <pre>
 *      Created on Jul 26, 2005
 *      Committed by $Author: haselwanter $
 *      $Source: /cvsroot/wsmo4j/ext/choreography/src/ri/org/deri/wsmo4j/rule/PerformRI.java,v $
 * </pre>
 * 
 * @author Thomas Haselwanter
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/03 13:43:37 $
 */
public abstract class PerformRI implements Perform {

	private IRI performance;
	
    /**
     * @param fact
     *            A CompoundFact object defining the fact of the Add rule
     */
    public PerformRI(IRI performance) {
        super();
        this.performance = performance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.rule.Rule#accept(org.wsmo.service.choreography.rule.Visitor)
     */
    public abstract void accept(Visitor visitor);

    /* (non-Javadoc)
     * @see org.wsmo.service.choreography.rule.Rule#toString(org.wsmo.common.TopEntity)
     */
	public String toString(TopEntity te) {
        VisitorSerializeWSMLTransitionRules visitor = new VisitorSerializeWSMLTransitionRules(te);
        visitor.visitPerform(this);
        return visitor.getSerializedObject();
    }
    
    @Override
    public String toString() {
        return toString(null);
    }

	public IRI getPerformIRI() {
		return performance;
	}

	public void setPerformIRI(IRI iri) {
		performance = iri;
		
	}
}
