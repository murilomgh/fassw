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

import org.wsmo.common.IRI;
import org.wsmo.service.signature.*;

/**
 * Reference Implementation for the WSDL grounding
 * 
 * @author James Scicluna
 * @author Thomas Haselwanter
 * 
 * Created on 26-Sep-2005 Committed by $Author: vassil_momtchev $
 * 
 * $Source:
 * /cvsroot/wsmo4j/ext/choreography/src/ri/org/deri/wsmo4j/choreography/signature/WSDLGroundingRI.java,v $,
 * @version $Revision: 1.4 $ $Date: 2006/10/24 14:11:47 $
 */

public class WSDLGroundingRI implements WSDLGrounding {

    private IRI iri;

    /**
     * Initiates the WSDLGrounding object
     */
    public WSDLGroundingRI() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * Initiates the WSDLGrounding object using the specified IRI
     * 
     * @param iri
     *            IRI object defining the grounding reference
     */
    public WSDLGroundingRI(IRI iri) {
        this.iri = iri;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.signature.WSDLGrounding#getIRI()
     */
    public IRI getIRI() {
        return this.iri;
    }

    /* (non-Javadoc)
     * @see org.wsmo.service.choreography.signature.WSDLGrounding#setIRI(org.wsmo.common.IRI)
     */
    public void setIRI(IRI iri) {
        // TODO Auto-generated method stub
        
    }

}
