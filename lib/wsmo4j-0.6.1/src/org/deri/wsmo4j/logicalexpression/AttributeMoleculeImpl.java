/*
 wsmo4j - a WSMO API and Reference Implementation
 Copyright (c) 2004, DERI Innsbruck
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
import org.omwg.logicalexpression.terms.*;


/**
 * @author DERI Innsbruck, reto.krummenacher@deri.org
 * @author DERI Innsbruck, thomas.haselwanter@deri.org
 * @author Holger Lausen (holger.lausen@deri.org)
 * @see org.omwg.logicalexpression.AttributeSpecification
 */
public abstract class AttributeMoleculeImpl
        extends MoleculeImpl
        implements AttributeMolecule {

    protected Term attributeName;

    public AttributeMoleculeImpl(Term leftTerm, Term attributeName, Term rightTerm) {
        super(leftTerm, rightTerm);
        setAttribute(attributeName);
    }

    /**
     * @return the name of the attribute specified
     * @see org.omwg.logicalexpression.AttributeSpecification#getName()
     */
    public Term getAttribute() {
        return attributeName;
    }

    /* (non-Javadoc)
     * @see org.omwg.logicalexpression.AttributeMolecule#setAttribute(org.omwg.logicalexpression.terms.Term)
     */
    public void setAttribute(Term t) throws IllegalArgumentException{
        if (t == null) {
            throw new IllegalArgumentException("Attribute name may not be null");
        }
        this.attributeName = t;
    }
}
