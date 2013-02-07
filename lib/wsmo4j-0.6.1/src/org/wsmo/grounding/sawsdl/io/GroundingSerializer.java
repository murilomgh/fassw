/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2004-2007, Ontotext Lab. / SIRMA

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

package org.wsmo.grounding.sawsdl.io;

import java.io.Writer;

import org.wsmo.grounding.sawsdl.Grounding;

public interface GroundingSerializer {


    /**
     * Transforms the speficied grounding object into semantically annotated WSDL (SA-WSDL).
     */
    public void serialize(Grounding grounding, Writer output) throws Exception;
}

/*
 * $Log: GroundingSerializer.java,v $
 * Revision 1.3  2007/04/27 17:46:31  alex_simov
 * javadoc added
 *
 * Revision 1.2  2007/04/24 15:31:26  alex_simov
 * turned into interfaces
 *
 * Revision 1.1  2007/04/24 14:09:45  alex_simov
 * new SA-WSDL api
 *
 */
