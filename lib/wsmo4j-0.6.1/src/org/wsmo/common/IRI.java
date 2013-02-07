/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2004-2005, OntoText Lab. / SIRMA

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

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 */

package org.wsmo.common;


/**
 * An interface representing an Internationalised Resource Identifier.
 *
 * @author not attributable
 * @version $Revision: 1.3 $ $Date: 2005/10/17 15:08:53 $
 * @since 0.4.0
 */
public interface IRI
    extends Identifier {

    /**
     * Returns the local part of the IRI.
     * @return The local part of this IRI.
     */
    String getLocalName();
    
    /**
     * Retrieve the namespace part of IRI
     * @return the namespace or null in case the namespace is not known.
     */
    String getNamespace();
}

/*
 * $Log: IRI.java,v $
 * Revision 1.3  2005/10/17 15:08:53  marin_dimitrov
 * getNameSpace --> getNamespace
 *
 * Revision 1.2  2005/10/13 12:31:49  vassil_momtchev
 * getNameSpace() method added
 *
 * Revision 1.1  2005/06/01 10:30:23  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.4  2005/05/12 14:42:57  marin
 * added @since tags
 *
 * Revision 1.3  2005/05/12 13:29:16  marin
 * javadoc, header, footer, etc
 *
 */

