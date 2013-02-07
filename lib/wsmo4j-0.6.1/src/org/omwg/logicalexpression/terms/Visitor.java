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


import org.omwg.ontology.*;
import org.wsmo.common.*;


/**
 * <p>This interface represents a visitor for the terms of a logical expression
 * tree structure.</p>
 * <p>The visitor design pattern is a way of separating an algorithm from an object
 * structure. A practical result of this separation is the ability to add new
 * operations to existing object structures without modifying those structures.</p>
 * <p>The idea is to use a structure of element classes, each of which has an accept
 * method that takes a visitor object as an argument. The visitor is an interface
 * that has a different visit() method for each element class. The accept() method
 * of an element class calls back the visit() method for its class. Separate
 * concrete visitor classes can then be written that perform some particular
 * operations.</p>
 * @author DERI Innsbruck, reto.krummenacher@deri.org
 * @version $Revision: 1.5 $ $Date: 2005/09/21 08:15:39 $
 * @see <a href "http://en.wikipedia.org/wiki/Visitor_pattern">Visitor Pattern</a>
 */
public interface Visitor {

    /**
     * @param t ConstructedTerm
     */
    void visitConstructedTerm(ConstructedTerm t);

    /**
     * @param t Variable
     */
    void visitVariable(Variable t);

    /**
     * @param t DataValue
     */
    void visitSimpleDataValue(SimpleDataValue t);

    /**
     * @param t DataValue
     */
    void visitComplexDataValue(ComplexDataValue t);

    /**
     * @param t UnNbAnonymousID
     */
    void visitUnnumberedID(UnnumberedAnonymousID t);

    /**
     * @param t NbAnonymousID
     */
    void visitNumberedID(NumberedAnonymousID t);

    /**
     * @param t IRI
     */
    void visitIRI(IRI t);

}
