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
 * Reference Implementation for Static Mode.
 * 
 * <pre>
 *    Created on Jul 26, 2005
 *    Committed by $Author: vassil_momtchev $
 *    $Source: /cvsroot/wsmo4j/ext/choreography/src/ri/org/deri/wsmo4j/choreography/signature/StaticRI.java,v $
 * </pre>
 * 
 * @author Thomas Haselwanter
 * @author James Scicluna
 * 
 * @version $Revision: 1.7 $ $Date: 2006/10/24 14:11:47 $
 */
public class StaticRI extends ModeRI implements Static {

    /**
     * Default Constructor for StaticRI
     * 
     * @param concept
     *            A Concept object which is associated
     *            with the Mode
     */
    public StaticRI(Concept concept) {
        super(concept);
    }
    
    public StaticRI(Relation relation){
        super(relation);
    }
}
