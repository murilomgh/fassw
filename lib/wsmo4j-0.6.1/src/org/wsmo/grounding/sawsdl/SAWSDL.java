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

package org.wsmo.grounding.sawsdl;

public class SAWSDL {

    /**
     * @uml.property  name="SAWSDL_NS_URI" readOnly="true"
     */
    public static final String SAWSDL_NS_URI = "http://www.w3.org/ns/sawsdl";

    public static final String DEFAULT_SAWSDL_NS_PREFIX = "sawsdl";

    /**
     * @uml.property  name="WSDL_NS_URI" readOnly="true"
     */
    public static final String WSDL_NS_URI = "http://www.w3.org/ns/wsdl";

    /**
     * @uml.property  name="REF_ATTRIBUTE" readOnly="true"
     */
    public static final String REF_ATTRIBUTE = "modelReference";

    /**
     * @uml.property  name="LOWERING_ATTRIBUTE"
     */
    public static final String LOWERING_ATTRIBUTE = "loweringSchemaMapping";

    /**
     * @uml.property  name="LIFTING_ATTRIBUTE" readOnly="true"
     */
    public static final String LIFTING_ATTRIBUTE = "liftingSchemaMapping";

}

/*
 * $Log: SAWSDL.java,v $
 * Revision 1.2  2007/04/25 16:35:48  alex_simov
 * no message
 *
 * Revision 1.1  2007/04/24 14:09:44  alex_simov
 * new SA-WSDL api
 *
 */
