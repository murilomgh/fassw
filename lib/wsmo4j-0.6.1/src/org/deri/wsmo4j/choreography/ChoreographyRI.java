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

package org.deri.wsmo4j.choreography;

import org.wsmo.common.Identifier;
import org.wsmo.service.choreography.rule.ChoreographyRules;
import org.wsmo.service.signature.*;

import com.ontotext.wsmo4j.common.EntityImpl;

/**
 * Choreography Reference Implementation.
 * 
 * TODO: the accept method *probably* accepts the wrong type of Visitor (it
 * should rather be org.wsmo.service.Visitor)
 * 
 * <pre>
 *     Created on Jul 26, 2005
 *     Committed by $Author: vassil_momtchev $
 *     $Source: /cvsroot/wsmo4j/ext/choreography/src/ri/org/deri/wsmo4j/choreography/ChoreographyRI.java,v $
 * </pre>
 * 
 * @author Thomas Haselwanter
 * @author James Scicluna
 * @author Vassil Momtchev
 * 
 * @version $Revision: 1.12 $ $Date: 2006/10/24 14:11:47 $
 */
public class ChoreographyRI extends EntityImpl implements
        org.wsmo.service.choreography.Choreography {

    StateSignature signature;

    ChoreographyRules rules;

    /**
     * Initializes a default Choreography Implementation given the
     * <code>IRI</code>,<code>StateSignature</code> and <code>Rules</code>
     * objects.
     * 
     * @param id
     *            Identifier <code>IRI</code> of the choreography
     * @param signature
     *            <code>StateSignature</code> of the choreography
     * @param rules
     *            <code>Rules</code> container of the choreography
     */
    public ChoreographyRI(Identifier id, StateSignature signature, ChoreographyRules rules) {
        super(id);
        this.signature = signature;
        this.rules = rules;
    }
    
    public ChoreographyRI(Identifier id) {
        super(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.Choreography#getStateSignature()
     */
    public StateSignature getStateSignature() {
        return signature;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.Choreography#setStateSignature(StateSignature stateSignature)
     */
    public void setStateSignature(StateSignature stateSignature) {
        this.signature = stateSignature;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.Choreography#getRules()
     */
    public ChoreographyRules getRules() {
        return this.rules;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.Choreography#setRules(Rules rules)
     */
    public void setRules(ChoreographyRules rules) {
        this.rules = rules;
    }

}
