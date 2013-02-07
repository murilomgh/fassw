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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.omwg.ontology.Concept;
import org.omwg.ontology.Relation;
import org.wsmo.service.signature.*;

/**
 * Implements the common getGrounding method for the Grounded Modes
 * 
 * @author James Scicluna
 * @author Thomas Haselwanter
 * 
 * Created on 26-Sep-2005 Committed by $Author: alex_simov $
 * 
 * $Source:
 * /cvsroot/wsmo4j/ext/choreography/src/ri/org/deri/wsmo4j/choreography/signature/GroundedModeRI.java,v $,
 * @version $Revision: 1.8 $ $Date: 2007/02/22 13:37:46 $
 */

public abstract class GroundedModeRI extends ModeRI implements GroundedMode {

    /**
     * @uml.property   name="groundings"
     * @uml.associationEnd   multiplicity="(0 -1)" elementType="org.wsmo.service.signature.Grounding"
     */
    protected Set<Grounding> groundings;

    /**
     * Default constructor
     * 
     * @param concept A Concept object with which the Grounded Mode
     * is initialized
     */
    public GroundedModeRI(Concept concept){
        super(concept);
        this.groundings = new HashSet <Grounding> ();
    }
    
    /**
     * @param iri
     *            An IRI object defining the grounded mode concept/relation
     * @param g
     *            A set of Grounding object definitions
     */
    public GroundedModeRI(Concept concept, Set<Grounding> g) {
        super(concept);
        this.groundings = g;
        if (g == null) {
            this.groundings = new HashSet <Grounding> ();
        }
        // TODO Auto-generated constructor stub
    }
    
    public GroundedModeRI(Relation relation){
        super(relation);
        this.groundings = new HashSet <Grounding> ();
    }
    
    public GroundedModeRI(Relation relation, Set <Grounding> g){
        super(relation);
        this.groundings = g;
        if (g == null) {
            this.groundings = new HashSet <Grounding> ();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.signature.GroundedMode#getGrounding()
     */
    public Set<Grounding> getGrounding() throws NotGroundedException {
        if (this.groundings == null) {
            throw new NotGroundedException("Mode is not Grounded");
        }
        else if (this.groundings.size() == 0) {
            throw new NotGroundedException("Mode is not Grounded");
        }
        else
            return Collections.unmodifiableSet(groundings);
    }
    
    /* (non-Javadoc)
     * @see org.wsmo.service.choreography.signature.GroundedMode#addGrounding(org.wsmo.service.choreography.signature.Grounding)
     */
    public void addGrounding(Grounding g){
        this.groundings.add(g);
    }
    
    /* (non-Javadoc)
     * @see org.wsmo.service.choreography.signature.GroundedMode#removeGrounding(org.wsmo.service.choreography.signature.Grounding)
     */
    public void removeGrounding(Grounding g){
        this.groundings.remove(g);
    }
}