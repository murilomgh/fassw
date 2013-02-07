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

import org.wsmo.common.exception.InvalidModelException;

/**
 * An <code>UpdateRule<code> is an unconditional rule that specificies a certain
 * kind of state update. The indent of this interface is to provide
 * uniform, generic access to all kind of <code>UpdateRules</code>.
 *
 * <pre>
 *     Created on Jul 26, 2005
 *     Committed by $Author: vassil_momtchev $
 *     $Source: /cvsroot/wsmo4j/ext/choreography/src/api/org/wsmo/service/rule/UpdateRule.java,v $
 * </pre>
 * 
 * @author James Scicluna
 * @author Thomas Haselwanter
 * @author Holger Lausen
 *
 * @version $Revision: 1.1 $ $Date: 2006/10/24 14:11:48 $
 */
public interface UpdateRule extends ChoreographyRule {

    /**
     * Returns the compound fact defined by an update rule
     * 
     * @return A CompoundFact object
     */
    public CompoundFact getFact();

    /**
     * Sets the fact of the update rule
     * 
     * @param fact
     *            A CompoundFact object to defining the fact of the update rule
     */
    public void setFact(CompoundFact fact) throws InvalidModelException;

}