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
package org.omwg.logicalexpression.terms;


/**
 * This interface represents a term of a logical expression
 *
 * @author DERI Innsbruck, reto.krummenacher@deri.org
 * @version $Revision: 1.3 $ $Date: 2005/09/09 11:58:20 $
 */
public interface Term {

    /**
     * @see org.omwg.logicalexpression.terms.Visitor
     */
    void accept(Visitor v);
}
/*
 * $Log: Term.java,v $
 * Revision 1.3  2005/09/09 11:58:20  holgerlausen
 * fixed header logexp no longer extension
 *
 * Revision 1.2  2005/09/09 11:03:14  marin_dimitrov
 * formatting
 *
 * Revision 1.1  2005/09/02 13:32:44  ohamano
 * move logicalexpression packages from ext to core
 * move tests from logicalexpression.test to test module
 *
 * Revision 1.6  2005/08/19 14:18:03  ohamano
 * rename the interface VisitorTerms to Visitor
 *
 * Revision 1.5  2005/08/16 16:28:29  nathaliest
 * JavaDoc added
 * Method getArgument(int) at UnaryImpl, QuantifiedImpl and BinaryImpl changed
 * Method equals(Object) at QuantifiedImpl changed
 *
 * Revision 1.4  2005/07/18 09:43:08  ohamano
 * visitor pattern for logical expression tree structure
 * move operator to compound expressions
 *
 * Revision 1.3  2005/06/22 13:32:02  ohamano
 * change header
 *
 * Revision 1.2  2005/06/18 14:06:08  holgerlausen
 * added local LEFactory, updated javadoc, refactored LEVariable > Variable etc. parse(String) for LEFactory is running now
 *
 * Revision 1.1  2005/06/16 13:55:23  ohamano
 * first import
 *
 */
