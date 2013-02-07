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

package org.wsmo.service.choreography.io;

import org.wsmo.service.choreography.Choreography;

/**
 * Serializer interface for choreography objects
 * 
 * <pre>
 *      Created on Sep 29, 2005
 *      Committed by $Author: jamsci001 $
 *      $Source: /cvsroot/wsmo4j/ext/choreography/src/api/org/wsmo/service/choreography/io/Serializer.java,v $
 * </pre>
 * 
 * @author James Scicluna
 * @author Thomas Haselwanter
 * 
 * @version $Revision: 1.4 $ $Date: 2005/10/18 15:23:41 $
 */
public interface Serializer {

    /**
     * Serializes a choreography object
     * 
     * @param choreography
     *            object to be serialized
     * @return A String object defining the serialized
     * choreography
     */
    public String serialize(Choreography choreography);

}
