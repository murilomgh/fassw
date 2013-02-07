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


import java.util.*;

import org.deri.wsmo4j.logicalexpression.util.SetUtil;
import org.omwg.logicalexpression.Atom;
import org.omwg.logicalexpression.Visitor;
import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.DataValue;
import org.wsmo.common.Identifier;


/**
 * This class is an atom expression with a n-ary domain, where n is
 * the arity of the predicate represented
 * @author DERI Innsbruck, reto.krummenacher@deri.org
 * @version $Revision: 1.13 $ $Date: 2007/04/02 12:13:20 $
 */
public class AtomImpl extends LogicalExpressionImpl
        implements Atom {

    protected List <Term> args = new LinkedList <Term>();

    protected Identifier id;

    /**
     * @param id identifier of Atom
     * @param args list of parameters for that atom
     * @throws IllegalArgumentException in case the parameter id is a Value or the arguments of
     * the list args aren't all of Type Term
     * @see org.wsmo.factory.LogicalExpressionFactory#createAtom(Identifier, List)
     */
    public AtomImpl(Identifier id, List <Term> args)
            throws IllegalArgumentException {
        if (id instanceof DataValue) {
            throw new IllegalArgumentException("No Values allowed as Identifier for Atoms");
        }
        this.id = id;
        setParameters(args);
    }

    /**
     * @return the identifier of the atom, i.e. the name of the predicate
     * @see org.omwg.logicalexpression.Atom#getIdentifier()
     */
    public Identifier getIdentifier() {
        return id;
    }

    /**
     * @param i the position of the parameter desired, maximal value getArity-1
     * @return the parameter at the position provided as parameter
     * @throws IllegalArgumentException in case i<0 or i exceeds the arity of the atom
     * @see org.omwg.logicalexpression.Atom#getParameter(int)
     */
    public Term getParameter(int i)
            throws IllegalArgumentException {
        if (i < 0 || i > args.size()) {
            throw new IllegalArgumentException("Parameter " + i + " exceeds arity of atom");
        }
        return args.get(i);
    }

    /**
     * @return the arity of the atom, i.e. the number of parameters
     * @see org.omwg.logicalexpression.Atom#getArity()
     */
    public int getArity() {
        if (args == null) {
            return 0;
        }
        return args.size();
    }

    /**
     * @see org.omwg.logicalexpression.LogicalExpression#accept(org.omwg.logicalexpression.Visitor)
     */
    public void accept(Visitor v) {
        v.visitAtom(this);
    }

    /**
     * <p>
     * The <code>equals</code> method implements an equivalence relation
     * on non-null object references. Atoms are equal if their arity, their
     * identifier and all their parameters are equal.
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
        if (o instanceof Atom) {
            Atom a = (Atom)o;
            if (a.getArity() != this.getArity()
                || !a.getIdentifier().equals(this.getIdentifier())) {
                return false;
            }
            for (int i = 0; i < a.getArity(); i++) {
                if (!this.getParameter(i).equals(a.getParameter(i))) {
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
     */
    public int hashCode() {
        if (args == null) {
            return id.hashCode();
        }
        return id.hashCode() + args.size();
    }

    public List <Term> listParameters() {
        return Collections.unmodifiableList(args);
    }

    /* (non-Javadoc)
     * @see org.omwg.logicalexpression.Atom#setParameters(java.util.List)
     */
    public void setParameters(List <Term> parameter) throws IllegalArgumentException {
        if (!SetUtil.allOfType(parameter, Term.class)) {
            throw new IllegalArgumentException("Only terms allowed as Argument");
        }
        if (parameter != null && parameter.size() != 0) {
            this.args = Collections.unmodifiableList(parameter);
        }        
    }
}

/*
 * $Log: AtomImpl.java,v $
 * Revision 1.13  2007/04/02 12:13:20  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.12  2006/11/17 15:07:59  holgerlausen
 * added an option to the parser to "remember" the original format of logical expressions, added the representation of the original String to LEImpl, and added to the serializer to check for the orginal formating to not not make complex expressions unreadable when serialized.
 *
 */