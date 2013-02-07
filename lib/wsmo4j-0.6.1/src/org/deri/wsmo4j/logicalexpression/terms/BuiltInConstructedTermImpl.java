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
package org.deri.wsmo4j.logicalexpression.terms;


import java.util.List;

import org.deri.wsmo4j.logicalexpression.ConstantTransformer;
import org.omwg.logicalexpression.terms.BuiltInConstructedTerm;
import org.omwg.logicalexpression.terms.Term;
import org.wsmo.common.IRI;


/**
 *
 *
 * <pre>
 * Created on Sep 7, 2005
 * Committed by $Author$
 * $Source$,
 * </pre>
 *
 * @author Holger Lausen (holger.lausen@deri.org)
 *
 * @version $Revision$ $Date$
 */
public class BuiltInConstructedTermImpl
        extends ConstructedTermImpl
        implements BuiltInConstructedTerm {

    /**
     * @param functionSymbol the name of the term, in form of an IRI
     * @param terms List of Terms
     * @throws IllegalArgumentException in case the functionSymbol is not a built in
     */
    public BuiltInConstructedTermImpl(IRI functionSymbol, List <Term> terms)
            throws IllegalArgumentException {
        super(functionSymbol, terms);
        if (!ConstantTransformer.getInstance().isBuiltInFunctionSymbol(functionSymbol.toString())) {
            throw new IllegalArgumentException(functionSymbol + "is not Built in Function symbol!");
        }
    }
}
