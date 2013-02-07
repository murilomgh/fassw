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


import org.omwg.logicalexpression.terms.*;


/**
 * Base interface for identifiers.
 * There are three types of identifiers defined at present:
 *    - IRI
 *    - anonymous numbered ID
 *    - anonymous unnumbered ID
 *
 * @author not attributable
 * @version $Revision: 1.8 $ $Date: 2005/09/16 14:03:35 $
 */
public interface Identifier
        extends Term {

}
/*
 * $Log: Identifier.java,v $
 * Revision 1.8  2005/09/16 14:03:35  alex_simov
 * Identifier.asString() removed, use Object.toString() instead
 * (Implementations MUST override toString())
 *
 * Revision 1.7  2005/09/09 10:48:51  marin_dimitrov
 * formatting
 *
 * Revision 1.6  2005/09/02 13:32:43  ohamano
 * move logicalexpression packages from ext to core
 * move tests from logicalexpression.test to test module
 *
 * Revision 1.5  2005/09/02 09:43:27  ohamano
 * integrate wsmo-api and le-api on api and reference implementation level; full parser, serializer and validator integration will be next step
 *
 * Revision 1.4  2005/06/01 10:30:48  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.3  2005/05/12 13:29:16  marin
 * javadoc, header, footer, etc
 *
 */
