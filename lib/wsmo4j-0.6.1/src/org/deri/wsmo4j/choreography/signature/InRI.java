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

package org.deri.wsmo4j.choreography.signature;

import java.util.Set;

import org.omwg.ontology.Concept;
import org.omwg.ontology.Relation;
import org.wsmo.service.signature.*;

/**
 * Reference implementation for the In mode.
 * 
 * <pre>
 *    Created on Jul 26, 2005
 *    Committed by $Author: vassil_momtchev $
 *    $Source: /cvsroot/wsmo4j/ext/choreography/src/ri/org/deri/wsmo4j/choreography/signature/InRI.java,v $
 * </pre>
 * 
 * @author Thomas Haselwanter
 * @author James Scicluna
 * 
 * @version $Revision: 1.10 $ $Date: 2006/10/24 14:11:47 $
 */
public class InRI extends GroundedModeRI implements In {

    public InRI(Concept concept)
    {
        super(concept);
    }
    /**
     * @param concept
     *            A Concept object which is associated
     *            with the Mode
     * @param g
     *            A set of Grounding object definitions
     */
    public InRI(Concept concept, Set<Grounding> g) {
        super(concept, g);
    }
    
    public InRI(Relation relation){
        super(relation);
    }
    
    public InRI(Relation relation, Set<Grounding> g){
        super(relation,g);
    }
}