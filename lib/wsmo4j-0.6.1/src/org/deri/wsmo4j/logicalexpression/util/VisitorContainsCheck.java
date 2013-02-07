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
package org.deri.wsmo4j.logicalexpression.util;


import java.util.*;

import org.omwg.logicalexpression.*;


/**
 * Class to check if a specified operator of a logical expression is contained in an
 * array of operators
 */
public class VisitorContainsCheck
        implements Visitor {

    private Class[] operators;

    private boolean containsOperator;

    /**
     * @param operators array of classes that will be checked for
     * @see org.deri.wsmo4j.logicalexpression.util.ExpressionContainmentUtil#contains(LogicalExpression, Class)
     */
    protected VisitorContainsCheck(Class[] operators) {
        containsOperator = false;
        this.operators = operators;
    }

    /**
     * Checks if a specified operator is contained in the array of operators
     * @param operator operator that will be checked for
     * @return <code>true</code> if the specified operator is contained
     */
    private boolean contained(Class operator) {
        for (int i = 0; i < operators.length; i++) {
            List classes = Arrays.asList(operator.getInterfaces());
            if (classes.contains(operators[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Empty method, as an Atom can't contain an operator
     * @see org.omwg.logicalexpression.Visitor#visitAtom(org.omwg.logicalexpression.Atom)
     */
    public void visitAtom(Atom expr) {
        //atom can't contain operator
    }

    /**
     * Checks if the operator of the Unary expression is contained in the array of operators
     * @see org.omwg.logicalexpression.Visitor#visitNegationAsFailure(NegationAsFailure)
     */
    public void visitNegationAsFailure(NegationAsFailure expr) {
        if (contained(expr.getClass())) {
            containsOperator = true;
            return;
        }
        expr.getOperand().accept(this);
    }

    /**
     * Checks if the operator of the Unary expression is contained in the array of operators
     * @see org.omwg.logicalexpression.Visitor#visitNegation(Negation)
     */
    public void visitNegation(Negation expr) {
        if (contained(expr.getClass())) {
            containsOperator = true;
            return;
        }
        expr.getOperand().accept(this);
    }

    /**
     * Checks if the operator of the Unary expression is contained in the array of operators
     * @see org.omwg.logicalexpression.Visitor#visitConstraint(Constraint)
     */
    public void visitConstraint(Constraint expr) {
        if (contained(expr.getClass())) {
            containsOperator = true;
            return;
        }
        expr.getOperand().accept(this);
    }

    /**
     * Checks if the operator of the Binary expression is contained in the array of operators
     * @see org.omwg.logicalexpression.Visitor#visitConjunction(Conjunction)
     */
    public void visitConjunction(Conjunction expr) {
        if (contained(expr.getClass())) {
            containsOperator = true;
            return;
        }
        expr.getLeftOperand().accept(this);
        expr.getRightOperand().accept(this);
    }

    /**
     * Checks if the operator of the Binary expression is contained in the array of operators
     * @see org.omwg.logicalexpression.Visitor#visitEquivalence(Equivalence)
     */
    public void visitEquivalence(Equivalence expr) {
        if (contained(expr.getClass())) {
            containsOperator = true;
            return;
        }
        expr.getLeftOperand().accept(this);
        expr.getRightOperand().accept(this);
    }

    /**
     * Checks if the operator of the Binary expression is contained in the array of operators
     * @see org.omwg.logicalexpression.Visitor#visitInverseImplication(InverseImplication)
     */
    public void visitInverseImplication(InverseImplication expr) {
        if (contained(expr.getClass())) {
            containsOperator = true;
            return;
        }
        expr.getLeftOperand().accept(this);
        expr.getRightOperand().accept(this);
    }

    /**
     * Checks if the operator of the Binary expression is contained in the array of operators
     * @see org.omwg.logicalexpression.Visitor#visitImplication(Implication)
     */
    public void visitImplication(Implication expr) {
        if (contained(expr.getClass())) {
            containsOperator = true;
            return;
        }
        expr.getLeftOperand().accept(this);
        expr.getRightOperand().accept(this);
    }

    /**
     * Checks if the operator of the Binary expression is contained in the array of operators
     * @see org.omwg.logicalexpression.Visitor#visitLogicProgrammingRule(LogicProgrammingRule)
     */
    public void visitLogicProgrammingRule(LogicProgrammingRule expr) {
        if (contained(expr.getClass())) {
            containsOperator = true;
            return;
        }
        expr.getLeftOperand().accept(this);
        expr.getRightOperand().accept(this);
    }

    /**
     * Checks if the operator of the Binary expression is contained in the array of operators
     * @see org.omwg.logicalexpression.Visitor#visitDisjunction(Disjunction)
     */
    public void visitDisjunction(Disjunction expr) {
        if (contained(expr.getClass())) {
            containsOperator = true;
            return;
        }
        expr.getLeftOperand().accept(this);
        expr.getRightOperand().accept(this);
    }

    /**
     * Checks if the operator of the Quantified expression is contained in the
     * array of operators
     * @see org.omwg.logicalexpression.Visitor#visitUniversalQuantification(UniversalQuantification)
     */
    public void visitUniversalQuantification(UniversalQuantification expr) {
        if (contained(expr.getClass())) {
            containsOperator = true;
            return;
        }
        expr.getOperand().accept(this);
    }

    /**
     * Checks if the operator of the Quantified expression is contained in the
     * array of operators
     * @see org.omwg.logicalexpression.Visitor#visitExistentialQuantification(ExistentialQuantification)
     */
    public void visitExistentialQuantification(ExistentialQuantification expr) {
        if (contained(expr.getClass())) {
            containsOperator = true;
            return;
        }
        expr.getOperand().accept(this);
    }

    /**
     * Checks if the previous logexp the visitor was applied to,
     * contained the compound expression with the operator passed in constructor.
     * So it checks if this logexp contained a specified operator.
     * @return true if it is contained
     */
    protected boolean isContained() {
        return containsOperator;
    }

    public void visitAttributeContraintMolecule(AttributeConstraintMolecule expr) {
        //atom can't contain operator
    }

    public void visitAttributeInferenceMolecule(AttributeInferenceMolecule expr) {
        //atom can't contain operator
    }

    public void visitAttributeValueMolecule(AttributeValueMolecule expr) {
        //atom can't contain operator
    }

    public void visitCompoundMolecule(CompoundMolecule expr) {
        //atom can't contain operator
    }

    public void visitMemberShipMolecule(MembershipMolecule expr) {
        //atom can't contain operator
    }

    public void visitSubConceptMolecule(SubConceptMolecule expr) {
        //atom can't contain operator
    }
}
