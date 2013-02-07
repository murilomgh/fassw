/*
 wsmo4j extension - a Choreography API and Reference Implementation

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

package org.deri.wsmo4j.rule;

import org.omwg.logicalexpression.*;
import org.omwg.logicalexpression.Visitor;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.service.rule.*;

/**
 * Reference Implementation for Condition
 * 
 * @author James Scicluna
 * @author Thomas Haselwanter
 * @author Holger Lausen
 * 
 * Created on 17-Oct-2005 Committed by $Author: vassil_momtchev $
 * 
 * $Source:
 * /cvsroot/wsmo4j/ext/choreography/src/ri/org/deri/wsmo4j/choreography/rule/ConditionRI.java,v $,
 * @version $Revision: 1.1 $ $Date: 2006/10/24 14:11:48 $
 */

public class ConditionRI implements Condition {

    protected LogicalExpression le;

    /**
     * Factory for creating a condition from an already existing Logical
     * Expression
     * 
     * @param e
     *            LogicalExpression object from which the condition is to be
     *            created
     */
    public ConditionRI(LogicalExpression e) throws InvalidModelException {
        try {
            validateLogicalExpression(e);
        } 
        catch (UnsupportedOperationException ex){
            throw new InvalidModelException(ex.getMessage());
        }
        this.le = e;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.omwg.logicalexpression.LogicalExpression#accept(org.omwg.logicalexpression.Visitor)
     */
    public void accept(Visitor v) {
        le.accept(v);
    }

    @Override
    public String toString() {
        return le.toString();
    }

    public String toString(TopEntity nsHolder) {
        return le.toString(nsHolder);
    }

    public LogicalExpression getRestrictedLogicalExpression() {
        return le;
    }

    public void setRestrictedLogicalExpression(LogicalExpression le) throws InvalidModelException {
        try {
            validateLogicalExpression(le);
        } 
        catch (UnsupportedOperationException ex){
            throw new InvalidModelException(ex.getMessage());
        }
        this.le = le;
    }

    private void validateLogicalExpression(LogicalExpression le) throws InvalidModelException {
        RestrictedLEValidator validator = new RestrictedLEValidator();
        le.accept(validator);
    }
    
    private class RestrictedLEValidator implements Visitor {
        private String error = "{0} is not allowed for a restricted LogicalExpression use in Condition!";
        
        public void visitAtom(Atom expr) {
        };

        public void visitCompoundMolecule(CompoundMolecule expr) {
        };

        public void visitSubConceptMolecule(SubConceptMolecule expr) {
        };

        public void visitMemberShipMolecule(MembershipMolecule expr) {
        };

        public void visitAttributeValueMolecule(AttributeValueMolecule expr) {
        };

        public void visitAttributeContraintMolecule(AttributeConstraintMolecule expr) {
        };

        public void visitAttributeInferenceMolecule(AttributeInferenceMolecule expr) {
        };

        public void visitNegation(Negation expr) {
            expr.getOperand().accept(this);
        };

        public void visitNegationAsFailure(NegationAsFailure expr) {
            throw new UnsupportedOperationException(String.format(error, "NegationAsFailure"));
        };

        public void visitConstraint(Constraint expr) {
            throw new UnsupportedOperationException(String.format(error, "Constraint"));
        };

        public void visitConjunction(Conjunction expr) {
            expr.getLeftOperand().accept(this);
            expr.getRightOperand().accept(this);
        };

        public void visitDisjunction(Disjunction expr) {
            expr.getLeftOperand().accept(this);
            expr.getRightOperand().accept(this);
        };

        public void visitInverseImplication(InverseImplication expr) {
            expr.getLeftOperand().accept(this);
            expr.getRightOperand().accept(this);
        };

        public void visitImplication(Implication expr) {
            expr.getLeftOperand().accept(this);
            expr.getRightOperand().accept(this);
        };

        public void visitEquivalence(Equivalence expr) {
            expr.getLeftOperand().accept(this);
            expr.getRightOperand().accept(this);
        };

        public void visitLogicProgrammingRule(LogicProgrammingRule expr) {
            throw new UnsupportedOperationException(String.format(error, "LogicProgrammingRule"));
        };

        public void visitUniversalQuantification(UniversalQuantification expr) {
            expr.getOperand().accept(this);
        };

        public void visitExistentialQuantification(ExistentialQuantification expr) {
            expr.getOperand().accept(this);
        };
    }
}

/*
 * $Log: ConditionRI.java,v $
 * Revision 1.1  2006/10/24 14:11:48  vassil_momtchev
 * choreography/orchestration rules refactored. different types where appropriate now supported
 *
 * Revision 1.7  2006/04/17 09:49:02  vassil_momtchev
 * validation of the LogicalExpression - only restricted LE (with no naf, :-, !-) allowed
 *
 */
