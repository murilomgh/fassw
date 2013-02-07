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
package org.omwg.logicalexpression;


import java.util.*;

import org.omwg.logicalexpression.terms.*;
import org.wsmo.common.*;


/**
 * This interface represents an atom with a n-ary domain, where n is
 * the arity of the predicate represented.
 * @author DERI Innsbruck, reto.krummenacher@deri.org
 * @version $Revision: 1.7 $ $Date: 2007/04/02 12:13:15 $
 * @see org.omwg.logicalexpression.AtomicExpression
 */
public interface Atom
        extends AtomicExpression {

    /**
     * The arity of the Atom indicates how many Terms are contained in the
     * Atom's list of arguments.
     *
     * @return the arity of the atom, i.e. the number of parameters
     */
    int getArity();

    /**
     * The returned Identifier represents the name of the predicate.
     *
     * @return the identifier of the atom, i.e. the name of the predicate
     */
    Identifier getIdentifier();

    /**
     * The index i points to a position in the Atom's list of arguments. So i mustn't
     * exceed the arity of the atom.
     * The desired Term, to which the index points, is returned.
     *
     * @param i the position of the parameter desired, maximal value getArity-1
     * @return the parameter at the position provided as parameter
     * @throws IllegalArgumentException in case i<0 or i exceeds the arity of the atom
     */
    Term getParameter(int i)
            throws IllegalArgumentException;
    
    /**
     * 
     * @return List containing the Atom's arguments (a list of Terms).
     */
    List <Term> listParameters();
    
    public void setParameters(List <Term> parameter);
}
/*
 * $Log: Atom.java,v $
 * Revision 1.7  2007/04/02 12:13:15  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.6  2005/10/25 12:20:55  holgerlausen
 * listParamters -> listParameters
 *
 * Revision 1.5  2005/09/21 06:31:55  holgerlausen
 * allowing to set arguments rfe  1290049
 *
 * Revision 1.4  2005/09/13 16:07:08  holgerlausen
 * added convinience method listParameters
 *
 * Revision 1.3  2005/09/09 11:58:19  holgerlausen
 * fixed header logexp no longer extension
 *
 * Revision 1.2  2005/09/09 11:12:12  marin_dimitrov
 * formatting
 *
 * Revision 1.1  2005/09/02 13:32:43  ohamano
 * move logicalexpression packages from ext to core
 * move tests from logicalexpression.test to test module
 *
 * Revision 1.7  2005/09/02 09:43:28  ohamano
 * integrate wsmo-api and le-api on api and reference implementation level; full parser, serializer and validator integration will be next step
 *
 * Revision 1.6  2005/08/18 16:15:40  nathaliest
 * JavaDoc added
 *
 * Revision 1.5  2005/08/16 16:28:29  nathaliest
 * JavaDoc added
 * Method getArgument(int) at UnaryImpl, QuantifiedImpl and BinaryImpl changed
 * Method equals(Object) at QuantifiedImpl changed
 *
 * Revision 1.4  2005/06/22 13:32:01  ohamano
 * change header
 *
 * Revision 1.3  2005/06/20 08:30:03  holgerlausen
 * formating
 *
 * Revision 1.2  2005/06/18 14:06:10  holgerlausen
 * added local LEFactory, updated javadoc, refactored LEVariable > Variable etc. parse(String) for LEFactory is running now
 *
 * Revision 1.1  2005/06/16 13:55:23  ohamano
 * first import
 *
 */