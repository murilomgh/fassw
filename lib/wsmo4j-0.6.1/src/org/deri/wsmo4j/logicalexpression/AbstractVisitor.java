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


/**
 * This abstract class represents a visitor for the logical expression
 * tree structure.
 * @author DERI Innsbruck, reto.krummenacher@deri.org
 * @version $Revision: 1.5 $ $Date: 2005/09/13 17:45:05 $
 * @see org.omwg.logicalexpression.Visitor
 * @see <a href "http://en.wikipedia.org/wiki/Visitor_pattern">Visitor Pattern</a>
 */
public abstract class AbstractVisitor
        implements Visitor {

    /**
     * @param expr Atom
     * @see org.omwg.logicalexpression.Visitor#visitAtom(org.omwg.logicalexpression.Atom)
     */
    public void visitAtom(Atom expr) {

    }

    /**
     * @param expr Molecule
     * @see org.omwg.logicalexpression.Visitor#visitMolecule(org.omwg.logicalexpression.Molecule)
     */
    public void visitMolecule(Molecule expr) {

    }

    /**
     * @param expr Unary with operator NEG
     * @see org.omwg.logicalexpression.Visitor#visitNegation(Negation)
     */
    public void visitNegation(Negation expr) {
        expr.getOperand().accept(this);
    }

    /**
     * @param expr Unary with operator NAF
     * @see org.omwg.logicalexpression.Visitor#visitNegationAsFailure(NegationAsFailure)
     */
    public void visitNegationAsFailure(NegationAsFailure expr) {
        expr.getOperand().accept(this);
    }

    /**
     * @param expr Unary with operator CONSTRAINT
     * @see org.omwg.logicalexpression.Visitor#visitConstraint(Constraint)
     */
    public void visitConstraint(Constraint expr) {
        expr.getOperand().accept(this);
    }

    /**
     * @param expr Binary with operator AND
     * @see org.omwg.logicalexpression.Visitor#visitConjunction(Conjunction)
     */
    public void visitConjunction(Conjunction expr) {
        expr.getLeftOperand().accept(this);
        expr.getRightOperand().accept(this);
    }

    /**
     * @param expr Binary with operator OR
     * @see org.omwg.logicalexpression.Visitor#visitDisjunction(Disjunction)
     */
    public void visitDisjunction(Disjunction expr) {
        expr.getLeftOperand().accept(this);
        expr.getRightOperand().accept(this);
    }

    /**
     * @param expr Binary with operator IMPLIEDBY
     * @see org.omwg.logicalexpression.Visitor#visitInverseImplication(InverseImplication)
     */
    public void visitInverseImplication(InverseImplication expr) {
        expr.getLeftOperand().accept(this);
        expr.getRightOperand().accept(this);
    }

    /**
     * @param expr Binary with operator IMPLIES
     * @see org.omwg.logicalexpression.Visitor#visitImplication(Implication)
     */
    public void visitImplication(Implication expr) {
        expr.getLeftOperand().accept(this);
        expr.getRightOperand().accept(this);
    }

    /**
     * @param expr Binary with operator EQUIVALENT
     * @see org.omwg.logicalexpression.Visitor#visitEquivalence(Equivalence)
     */
    public void visitEquivalence(Equivalence expr) {
        expr.getLeftOperand().accept(this);
        expr.getRightOperand().accept(this);
    }

    /**
     * @param expr Binary with operator IMPLIESLP
     * @see org.omwg.logicalexpression.Visitor#visitLogicProgrammingRule(LogicProgrammingRule)
     */
    public void visitLogicProgrammingRule(LogicProgrammingRule expr) {
        expr.getLeftOperand().accept(this);
        expr.getRightOperand().accept(this);
    }

    /**
     * @param expr Quantified with operator FORALL
     * @see org.omwg.logicalexpression.Visitor#visitUniversalQuantification(UniversalQuantification)
     */
    public void visitUniversalQuantification(UniversalQuantification expr) {
        expr.getOperand().accept(this);
    }

    /**
     * @param expr Quantified with operator EXISTS
     * @see org.omwg.logicalexpression.Visitor#visitExistentialQuantification(ExistentialQuantification)
     */
    public void visitExistentialQuantification(ExistentialQuantification expr) {
        expr.getOperand().accept(this);
    }
}
