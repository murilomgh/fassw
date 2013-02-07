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

import org.omwg.ontology.Concept;
import org.omwg.ontology.Relation;
import org.wsmo.service.signature.*;

/**
 * Reference Implementation for the upper Mode interface.
 * 
 * <pre>
 *     Created on Jul 26, 2005
 *     Committed by $Author: vassil_momtchev $
 *     $Source: /cvsroot/wsmo4j/ext/choreography/src/ri/org/deri/wsmo4j/choreography/signature/ModeRI.java,v $
 * </pre>
 * 
 * @author Thomas Haselwanter
 * @author James Scicluna
 * 
 * @version $Revision: 1.8 $ $Date: 2006/10/24 14:11:47 $
 */
public abstract class ModeRI implements Mode {

    private Concept concept;
    private Relation relation;

    /**
     * @param concept
     *            A Concept object which is associated
     *            with the Mode
     */
    public ModeRI(Concept concept) {
        this.concept = concept;
    }
    
    /**
     * Associates the mode with a relation
     * 
     * @param relation Relation object to be associated with the Mode
     */
    public ModeRI(Relation relation){
        this.relation = relation;
    }

    /* (non-Javadoc)
     * @see org.wsmo.service.choreography.signature.Mode#getConcept()
     */
    public Concept getConcept() {
        return this.concept;
    }
    
    /* (non-Javadoc)
     * @see org.wsmo.service.choreography.signature.Mode#getRelation()
     */
    public Relation getRelation(){
        return this.relation;
    }
    
    /* (non-Javadoc)
     * @see org.wsmo.service.choreography.signature.Mode#isConcept()
     */
    public boolean isConcept(){
        if(this.concept != null)
            return true;
        else return false;
    }
}
