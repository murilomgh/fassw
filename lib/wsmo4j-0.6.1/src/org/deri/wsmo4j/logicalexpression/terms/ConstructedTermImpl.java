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


import java.util.*;

import org.deri.wsmo4j.logicalexpression.util.*;

import org.omwg.logicalexpression.terms.*;
import org.wsmo.common.*;


/**
 * @author DERI Innsbruck, reto.krummenacher@deri.org, holger.lausen@deri.org
 * @version $Revision: 1.5 $ $Date: 2007/04/02 12:13:22 $
 *
 * @see org.omwg.logicalexpression.terms.ConstructedTerm
 */
public class ConstructedTermImpl
        implements ConstructedTerm {

    private IRI functionSymbol;

    private List <Term> args = new Vector <Term> ();

    /**
     * @param functionSymbol the name of the term, in form of an IRI
     * @param terms List of Terms
     * @throws IllegalArgumentException in case the functionSymbol is null or the arguments of
     * the list aren't all of Type Term
     * @see org.wsmo.factory.LogicalExpressionFactory#createConstructedTerm(IRI, List)
     */
    public ConstructedTermImpl(IRI functionSymbol, List <Term> terms)
            throws IllegalArgumentException {
        if (functionSymbol == null) {
            throw new IllegalArgumentException("functionSymbol may not be null!");
        }
        if (!SetUtil.allOfType(terms, Term.class)) {
            throw new IllegalArgumentException("Only " + Term.class +" are allowed as Arguments!");
        }
        this.functionSymbol = functionSymbol;
        if (terms != null) {
            this.args = Collections.unmodifiableList(terms);
        }

    }

    /**
     * @return the name of the term, in wsml only possible in form of an IRI
     * @see org.omwg.logicalexpression.terms.ConstructedTerm#getFunctionSymbol()
     */
    public IRI getFunctionSymbol() {
        return functionSymbol;
    }

    /**
     * @return the arity of the term, i.e. the number of parameters
     * @see org.omwg.logicalexpression.terms.ConstructedTerm#getArity()
     */
    public int getArity() {
        return args.size();
    }

    /**
     * @param i the position of the parameter desired, maximal getArity()-1
     * @return a Term, the chosen parameter
     * @see org.omwg.logicalexpression.terms.ConstructedTerm#getParameter(int)
     * @throws IndexOutOfBoundsException if the index is out of range (arity)
     */
    public Term getParameter(int i)
            throws IndexOutOfBoundsException {
        return args.get(i);
    }

    /**
     * @see org.omwg.logicalexpression.terms.Term#accept(org.omwg.logicalexpression.terms.Visitor)
     */
    public void accept(Visitor v) {
        v.visitConstructedTerm(this);
    }

    /**
     * @return The String representation of the constructed term
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String ret = functionSymbol.toString() + "(";
        Iterator i = args.iterator();
        while (i.hasNext()) {
            ret += (Term)i.next();
            if (i.hasNext()) {
                ret += ",";
            }
        }
        ret += ")";
        return ret;
    }

    /**
     * <p>
     * The <code>equals</code> method implements an equivalence relation
     * on non-null object references. Two constructed terms are equal if their arity, their
     * functionSymbol, and all of the arguments from the list of terms are equal.
     * </p>
     * <p>
     * It is generally necessary to override the <code>hashCode</code> method whenever this method
     * is overridden.
     * </p>
     * @param o Object of Type ConstructedTerm
     * @return <code>true</code> if this object is the same as the obj
     *          argument; <code>false</code> otherwise.
     * @see java.lang.Object#equals(java.lang.Object)
     * @see java.lang.Object#hashCode()
     */
    public boolean equals(Object o) {
        if (o instanceof ConstructedTerm) {
            ConstructedTerm c = (ConstructedTerm)o;
            //no need to check further if functionsymbols or
            //arity don't match
            if (!c.getFunctionSymbol().equals(functionSymbol)
                || c.getArity() != this.getArity()) {
                return false;
            }
            //check all arguments
            Iterator i = args.iterator();
            int n = 0;
            while (i.hasNext()) {
                Term t1 = (Term)i.next();
                Term t2 = c.getParameter(n++);
                if (!t1.equals(t2)) {
                    return false;
                }
            }
            return true;
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
     * @see com.ontotext.wsmo4j.common.IRIImpl#hashCode()
     */
    public int hashCode() {
        //can probably be more optimized
        return functionSymbol.hashCode();
    }

    /**
     * @see org.omwg.logicalexpression.terms.ConstructedTerm#listParameters()
     */
    public List listParameters() {
        return args;
    }
}
