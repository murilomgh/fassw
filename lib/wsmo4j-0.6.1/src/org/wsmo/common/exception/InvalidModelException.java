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

package org.wsmo.common.exception;

/**
 *  This is a <b>checked</b> exception thrown when the changes applied to an entity
 *  break the consistency of its underlying logical model (if any)
 *  For example, if an axiom is added to an entity that is in a logical
 *  formalism not understood or incomatible with the current model
 *
 * @author not attributable
 * @version $Revision: 1.6 $ $Date: 2005/06/29 17:16:10 $
 */
public class InvalidModelException
    extends Exception {

    public InvalidModelException() {
        super();
    }

    public InvalidModelException(Throwable cause) {
        super(cause);
    }

    public InvalidModelException(String msg) {
        super(msg);
    }
}
/*
 * $Log: InvalidModelException.java,v $
 * Revision 1.6  2005/06/29 17:16:10  marin_dimitrov
 * added constructors (bug 1225639)
 *
 * Revision 1.5  2005/06/22 14:40:48  alex_simov
 * Copyright header added/updated
 *
 * Revision 1.4  2005/06/01 10:31:34  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.3  2005/05/12 13:29:16  marin
 * javadoc, header, footer, etc
 *
 */
