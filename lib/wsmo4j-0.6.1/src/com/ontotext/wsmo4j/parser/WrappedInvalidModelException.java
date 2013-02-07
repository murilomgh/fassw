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

package com.ontotext.wsmo4j.parser;

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */
import org.wsmo.common.exception.InvalidModelException;

public class WrappedInvalidModelException extends RuntimeException {
    public WrappedInvalidModelException(InvalidModelException cause) {
        super(cause);
    }
}

/*
 * $Log: WrappedInvalidModelException.java,v $
 * Revision 1.1  2005/06/27 08:32:00  alex_simov
 * refactoring: *.io.parser -> *.parser
 *
 * Revision 1.4  2005/06/22 14:49:27  alex_simov
 * Copyright header added/updated
 *
 * Revision 1.3  2005/06/02 14:19:19  alex_simov
 * v0.4.0
 *
 * Revision 1.1  2005/05/26 09:36:04  damian
 * io package
 *
 * Revision 1.2  2005/01/12 15:20:18  alex_simov
 * checkstyle formatting
 *
 */
