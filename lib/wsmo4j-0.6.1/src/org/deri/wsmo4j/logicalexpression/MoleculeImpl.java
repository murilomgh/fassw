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


import org.omwg.logicalexpression.Molecule;
import org.omwg.logicalexpression.terms.Term;


/**
 * This class reunites all molecular simple logical expressions
 *
 * @author DERI Innsbruck, reto.krummenacher@deri.org
 * @version $Revision: 1.13 $ $Date: 2006/11/17 15:07:59 $
 * @see org.omwg.logicalexpression.AtomicExpression
 */
public abstract class MoleculeImpl extends LogicalExpressionImpl
        implements Molecule {

    private Term leftTerm;
    
    private Term rightTerm;
    
    public MoleculeImpl(Term leftTerm, Term rightTerm)
            throws IllegalArgumentException {
        setLeftOperand(leftTerm);
        setRightOperand(rightTerm);
    }
    
   /**
     * <p>
     * The <code>equals</code> method implements an equivalence relation
     * on non-null object references. Molecules are equal if their term is equal and
     * if their set of AttributeSpecifications (if any), set of MemberOfs (if any) or set of
     * subConceptOfs (if any) is equal.
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
    public boolean equals(Object o) {
        if (o instanceof Molecule) {
            Molecule m = (Molecule)o;
            if (m.getLeftParameter().equals(leftTerm) &&
                    m.getRightParameter().equals(rightTerm)){
            return true;
            }
        }
        return false;
    }
    
    public int hashCode() {
        return leftTerm.hashCode() + rightTerm.hashCode();
    }

    public Term getLeftParameter() {
        return leftTerm;
    }

    public Term getRightParameter() {
        return rightTerm;
    }

    /* (non-Javadoc)
     * @see org.omwg.logicalexpression.Molecule#setLeftOperand(org.omwg.logicalexpression.terms.Term)
     */
    public void setLeftOperand(Term t) throws IllegalArgumentException{
        if (t == null) {
            throw new IllegalArgumentException(
                    "No Term in Molecule Spec may be null");
        }
        leftTerm = t;
    }

    /* (non-Javadoc)
     * @see org.omwg.logicalexpression.Molecule#setRightOperand(org.omwg.logicalexpression.terms.Term)
     */
    public void setRightOperand(Term t) throws IllegalArgumentException{
        if (t == null) {
            throw new IllegalArgumentException(
                    "No Term in Molecule Spec may be null");
        }
        rightTerm = t;
    }
}
