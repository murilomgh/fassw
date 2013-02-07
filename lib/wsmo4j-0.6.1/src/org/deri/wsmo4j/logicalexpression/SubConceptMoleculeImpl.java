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
package org.deri.wsmo4j.logicalexpression;

import org.omwg.logicalexpression.*;
import org.omwg.logicalexpression.Visitor;
import org.omwg.logicalexpression.terms.*;

/**
 * Represents a sub concept of molecule
 *
 * <pre>
 * Created on Sep 19, 2005
 * Committed by $Author$
 * $Source$,
 * </pre>
 *
 * @author Holger Lausen (holger.lausen@deri.org)
 *
 * @version $Revision$ $Date$
 */
public class SubConceptMoleculeImpl 
        extends MoleculeImpl 
        implements SubConceptMolecule {

    /**
     * @param leftTerm
     * @param rightTerm
     * @throws IllegalArgumentException
     */
    public SubConceptMoleculeImpl(Term leftTerm, Term rightTerm) throws IllegalArgumentException {
        super(leftTerm, rightTerm);
    }
    
    public boolean equals(Object o){
        if (!(o instanceof SubConceptMolecule)){
            return false;
        }
        return super.equals(o);
    }
    
    public void accept(Visitor v) {
        v.visitSubConceptMolecule(this);
    }

}


/*
 *$Log$
 *Revision 1.1  2005/09/20 13:21:31  holgerlausen
 *refactored logical expression API to have simple molecules and compound molecules (RFE 1290043)
 *
 */