/*
 wsmo4j - a WSMO API and Reference Implementation
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
package org.deri.wsmo4j.io.parser.rdf;

/**
 * This class represents a RDF Parser Error.
 *
 * <pre>
 *  Created on Nov 14, 2006
 *  Committed by $Author: nathaliest $
 *  $Source: /cvsroot/wsmo4j/wsmo4j/org/deri/wsmo4j/io/parser/rdf/RDFParserError.java,v $
 * </pre>
 *
 * @author nathalie.steinmetz@deri.org
 * @version $Revision: 1.1 $ $Date: 2006/11/16 09:53:28 $
 */
public class RDFParserError extends RDFParserMessage {
	
	public RDFParserError(String message, int lineNo, int colNo) {
		super(message, lineNo, colNo);
	}

    /**
     * <p>
     * The <code>equals</code> method implements an equivalence relation
     * on non-null object references. RDFParserWarnings are equal if their 
     * warning message and their line and column numbers are equal.
     * </p>
     * <p>
     * It is generally necessary to override the <code>hashCode</code> method whenever this method
     * is overridden.
     * </p>
     * @param o the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     *          argument; <code>false</code> otherwise.
     * @see java.lang.Object#equals(java.lang.Object)
     * @see java.lang.Object#hashCode()
     */
    public boolean equals(Object obj) {
        if (obj instanceof RDFParserError) {
        	RDFParserError w = (RDFParserError) obj;
            return ((w.getMessage().equals(this.getMessage())
                     && (w.getLine() == this.getLine()) && 
                     (w.getColumn() == this.getColumn())));
        }
        return false;
    }
    
    /**
     * <p>
     * If two objects are equal according to the <code>equals(Object)</code> method, then calling
     * the <code>hashCode</code> method on each of the two objects must produce the same integer
     * result. However, it is not required that if two objects are unequal according to
     * the <code>equals(Object)</code> method, then calling the <code>hashCode</code> method on each of the two
     * objects must produce distinct integer results.
     * </p>
     * <p>
     * This method should be overriden, when the <code>equals(Object)</code> method is overriden.
     * </p>
     * @return A hash code value for this Object.
     * @see java.lang.Object#hashCode()
     * @see java.lang.Object#equals(Object)
     */
    public int hashCode() {
        return super.hashCode();
    }
}
/*
 * $Log: RDFParserError.java,v $
 * Revision 1.1  2006/11/16 09:53:28  nathaliest
 * added RDFParserError
 *
 *
 */