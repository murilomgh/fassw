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

import org.wsmo.service.rule.*;

/**
 * Implements common functionality for Update Rules.
 * 
 * <pre>
 *   Created on Sep 8, 2005
 *   Committed by $Author: vassil_momtchev $
 *   $Source: /cvsroot/wsmo4j/ext/choreography/src/ri/org/deri/wsmo4j/rule/AbstractUpdateRule.java,v $
 * </pre>
 * 
 * @author James Scicluna
 * @author Thomas Haselwanter
 * @author Holger Lausen
 * 
 * @version $Revision: 1.1 $ $Date: 2006/10/24 14:11:48 $
 */
public abstract class AbstractUpdateRule implements UpdateRule {

    protected CompoundFact fact;

    /**
     * Default constructor for Update Rules
     */
    public AbstractUpdateRule() {
    }

    /**
     * @param fact
     *            A CompoundFact object denoting the fact of the rule.
     */
    public AbstractUpdateRule(CompoundFact fact) {
        this.fact = fact;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.rule.UpdateRule#getFact()
     */
    public CompoundFact getFact() {
        return this.fact;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.rule.UpdateRule#setFact(org.wsmo.service.choreography.rule.CompoundFact)
     */
    public void setFact(CompoundFact fact) {
        this.fact = fact;
    }
}