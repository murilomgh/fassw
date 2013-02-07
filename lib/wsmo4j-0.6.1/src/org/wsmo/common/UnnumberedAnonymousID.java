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
 * An interface representing an anonymous unnumbered identifier
 *
 * @author not attributable
 * @version $Revision: 1.1 $ $Date: 2005/09/02 09:43:27 $
 * @since 0.4.0
 */
public interface UnnumberedAnonymousID
    extends Identifier {
}

/*
 * $Log: UnnumberedAnonymousID.java,v $
 * Revision 1.1  2005/09/02 09:43:27  ohamano
 * integrate wsmo-api and le-api on api and reference implementation level; full parser, serializer and validator integration will be next step
 *
 * Revision 1.2  2005/06/01 16:15:15  marin_dimitrov
 * fixed An[n]nonymousID reference
 *
 * Revision 1.1  2005/06/01 10:30:24  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.4  2005/05/12 14:42:57  marin
 * added @since tags
 *
 * Revision 1.3  2005/05/12 13:29:16  marin
 * javadoc, header, footer, etc
 *
 */