/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2005 University of Innsbruck, Austria
               2005 National University of Ireland, Galway

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
package org.deri.wsmo4j.io.parser;


/**
 * Wrapps Exception during parsing (traversing the sablecc AST tree).
 *
 * This exception is only used internally in the implementation of
 * the API and is not thrown to a "standard" user of the API.
 *
 * <pre>
 * Created on Jul 24, 2005
 * Committed by $Author$
 * $Source$,
 * </pre>
 *
 * @author Holger Lausen
 *
 * @version $Revision$ $Date$
 */
public class WrappedParsingException
        extends RuntimeException {
    //change when changing class
    private static final long serialVersionUID = 3832903273446126645L;

    private Exception e;

    /**
     * @param e Exception forwarded to this WrappedParsingException
     */
    public WrappedParsingException(Exception e) {
        this.e = e;
    }

    /**
     * This method returns the orignial exception that was catched with creating the
     * WrappedParsingException
     *
     * @return Exception
     */
    public Exception getWrappedException() {
        return e;
    }
}
