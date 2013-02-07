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

package org.wsmo.service.choreography.rule;

import java.util.Set;

import org.wsmo.common.Entity;
import org.wsmo.service.rule.*;

/**
 * Interface or class description
 * 
 * @author James Scicluna
 * @author Thomas Haselwanter
 * 
 * Created on 06-Dec-2005 Committed by $Author: vassil_momtchev $
 * 
 * $Source: /cvsroot/wsmo4j/ext/choreography/src/api/org/wsmo/service/choreography/rule/ChoreographyRules.java,v $,
 * @version $Revision: 1.1 $ $Date: 2006/10/24 14:11:48 $
 */

public interface ChoreographyRules extends Entity {
    /**
     * Returns <code>Rules</code> that make up this choreography.
     * 
     * @return <code>Set</code> of <code>Rules</code>
     */
    public Set<ChoreographyRule> listRules();

    /**
     * Removes the specified rule from the choreography
     * 
     * @param rule
     *            Rule object to be removed
     */
    public void removeRule(ChoreographyRule rule);

    /**
     * Adds a rule to the choreography
     * 
     * @param rule
     *            Rule object to be added to the choreography
     */
    public void addRule(ChoreographyRule rule);
}
