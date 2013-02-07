/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2004, , University of Innsbruck, Austria

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


import java.util.*;

import org.omwg.ontology.*;
import org.wsmo.common.*;


/**
 * The interface for constructed terms (function symbols).
 * 
 * E.g. in the molecule jon[age(2005) hasValue 12]
 * age(2005) is a function symbol. Function symbols are only
 * allowed in WSML Rule and WSML FOL. Note that some data
 * values (e.g. _date(2005,12,12) look syntactically like function
 * symbols, but are transformed by the parser directly to
 * a {@link DataValue}.
 *
 * @author DERI Innsbruck, reto.krummenacher@deri.org
 * @author Holger Lausen
 * @version $Revision: 1.4 $ $Date: 2005/11/28 15:36:42 $
 * @see org.omwg.logicalexpression.terms.Term
 */
public interface ConstructedTerm
        extends Term {

    /**
     * The arity of the ConstructedTerm indicates how many Terms are contained in the
     * Term's list of arguments.
     *
     * @return the arity of the term, i.e. the number of parameters
     */
    public int getArity();

    /**
     * The returned IRI represents the name of the ConstructedTerm.
     *
     * @return the name of the term, in wsml only possible in form of an IRI
     */
    public IRI getFunctionSymbol();

    /**
     * The index i points to a position in the ConstructedTerm's list of arguments.
     * The desired Term, to which the index points, is returned.
     *
     * @param i the position of the parameter desired, maximal getArity()-1
     * @return the chosen parameter
     */
    public Term getParameter(int i);
    
    /**
     * Return the list of all Parameters 
     * @return list of parameter of the constructed term
     */
    public List listParameters();
}
/*
 * $Log: ConstructedTerm.java,v $
 * Revision 1.4  2005/11/28 15:36:42  holgerlausen
 * added convinience method listParameter (RFE 1363559)
 *
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
 * Revision 1.5  2005/09/02 09:43:27  ohamano
 * integrate wsmo-api and le-api on api and reference implementation level; full parser, serializer and validator integration will be next step
 *
 * Revision 1.4  2005/08/16 16:28:29  nathaliest
 * JavaDoc added
 * Method getArgument(int) at UnaryImpl, QuantifiedImpl and BinaryImpl changed
 * Method equals(Object) at QuantifiedImpl changed
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
