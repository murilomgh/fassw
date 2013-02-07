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

import org.omwg.ontology.Concept;
import org.omwg.ontology.Relation;

/**
 * A Mode entry container. Defines only one method to get the Concept or
 * Relation associated with it. A Mode can be either a concept or a relation
 * but not both.
 * 
 * <pre>
 *    Created on Jul 26, 2005
 *    Committed by $Author: vassil_momtchev $
 *    $Source: /cvsroot/wsmo4j/ext/choreography/src/api/org/wsmo/service/signature/Mode.java,v $
 * </pre>
 * 
 * @author James Scicluna
 * @author Thomas Haselwanter
 * @author Holger Lausen
 * 
 * @version $Revision: 1.1 $ $Date: 2006/10/24 14:56:41 $
 */
public abstract interface Mode {

    /**
     * Returns the Concept which is associated with the
     * mode
     * 
     * @return A Concept object associated with the Mode
     */
    public Concept getConcept();
    
    /**
     * Returns the Relation which is associated with the
     * mode
     * 
     * @return A Relation object associated with the Mode
     */
    public Relation getRelation();
    
    /**
     * Determines whether the mode is associated with a concept (true
     * if yes, false if it's a relation)
     * @return
     */
    public boolean isConcept();

}