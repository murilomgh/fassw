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

import org.wsmo.common.IRI;

/**
 * WSDL Grounding is a subclass of Grounding and defines methods for getting
 * information about the wsdl grounding for a concept.
 * 
 * <pre>
 *    Created on Jul 26, 2005
 *    Committed by $Author: vassil_momtchev $
 *    $Source: /cvsroot/wsmo4j/ext/choreography/src/api/org/wsmo/service/signature/WSDLGrounding.java,v $
 * </pre>
 * 
 * @author James Scicluna
 * @author Thomas Haselwanter
 * @author Holger Lausen
 * 
 * @version $Revision: 1.1 $ $Date: 2006/10/24 14:56:41 $
 */
public interface WSDLGrounding extends Grounding {

    /**
     * Returns an IRI pointing to the input/ouput parameter of some WSDL
     * operation
     * 
     * @return An IRI object defining the grounding information
     */
    public IRI getIRI();
    
    /**
     * Sets the grounding IRI
     * 
     * @param iri An IRI object which defines the grounding 
     */
    public void setIRI(IRI iri);

}
