/*
 wsmo4j - a WSMO API and Reference Implementation
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
package org.wsmo.validator;

import org.omwg.ontology.Attribute;

/**
 * Gives Structure to an attribute validation error.
 *
 * <pre>
 *  Created on November 15, 2005
 *  Committed by $Author: nathaliest $
 *  $Source: /cvsroot/wsmo4j/wsmo-api/org/wsmo/validator/AttributeError.java,v $,
 * </pre>
 *
 * @author nathalie.steinmetz@deri.org
 * @version $Revision: 1.1 $ $Date: 2005/11/15 16:50:37 $
 */
public interface AttributeError extends ValidationError {

    /**
     * Returns the attribute in which the violation occured.
     * 
     * @return an Attribute
     */
    public Attribute getAttribute();
    
}
