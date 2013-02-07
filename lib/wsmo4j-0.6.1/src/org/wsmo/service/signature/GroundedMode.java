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

package org.wsmo.service.signature;

import java.util.Set;

/**
 * Extends Mode and defines the method getGrounding to be implemented by the
 * grounded modes
 * 
 * @author James Scicluna
 * @author Thomas Haselwanter
 * 
 * Created on 26-Sep-2005 Committed by $Author: vassil_momtchev $
 * 
 * $Source:
 * /cvsroot/wsmo4j/ext/choreography/src/api/org/wsmo/service/choreography/signature/GroundedMode.java,v $,
 * @version $Revision: 1.1 $ $Date: 2006/10/24 14:56:41 $
 */

public interface GroundedMode extends Mode {

    /**
     * Returns the grounding object entries of the Grounded mode
     * 
     * @return Set of Grounding objects and throws a NotGroundedException if no
     *         grounding is defined.
     * @throws NotGroundedException
     */
    public Set<Grounding> getGrounding() throws NotGroundedException;

    /**
     * Adds a grounding specification to the Grounded Mode
     * 
     * @param g
     *            A Grounding object to be added
     */
    public void addGrounding(Grounding g);

    /**
     * Removes a grounding specification from the Grounded Mode
     * 
     * @param g
     *            A Grounding object to be removed
     */
    public void removeGrounding(Grounding g);

}
