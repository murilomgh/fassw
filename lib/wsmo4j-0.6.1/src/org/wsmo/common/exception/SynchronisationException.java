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
 *  This is an <b>unchecked</b> exception thrown when the state entity cannot
 *  be synchronised with the underlying persistence mechanism (if any)
 *  Any access to or modification of an entity may rise such exception
 *
 * @author not attributable
 * @version $Revision: 1.8 $ $Date: 2005/06/22 14:40:48 $
 */
public class SynchronisationException
        extends RuntimeException {

    public SynchronisationException() {
        super();
    }

    public SynchronisationException(Throwable cause) {
        super(cause);
    }

    public SynchronisationException(String msg) {
        super(msg);
    }
}
/*
 * $Log: SynchronisationException.java,v $
 * Revision 1.8  2005/06/22 14:40:48  alex_simov
 * Copyright header added/updated
 *
 * Revision 1.7  2005/06/03 14:06:31  marin_dimitrov
 * no message
 *
 * Revision 1.6  2005/06/02 14:58:37  marin_dimitrov
 * more constructors
 *
 * Revision 1.5  2005/06/01 10:31:34  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.3  2005/05/12 13:29:16  marin
 * javadoc, header, footer, etc
 *
 */
