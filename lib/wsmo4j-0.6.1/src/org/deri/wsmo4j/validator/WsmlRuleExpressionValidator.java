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
package org.deri.wsmo4j.validator;


import java.util.List;

import org.deri.wsmo4j.io.serializer.wsml.LogExprSerializerWSML;
import org.deri.wsmo4j.logicalexpression.ConstantTransformer;
import org.omwg.logicalexpression.Atom;
import org.omwg.logicalexpression.AttributeConstraintMolecule;
import org.omwg.logicalexpression.AttributeInferenceMolecule;
import org.omwg.logicalexpression.AttributeValueMolecule;
import org.omwg.logicalexpression.Binary;
import org.omwg.logicalexpression.CompoundMolecule;
import org.omwg.logicalexpression.Conjunction;
import org.omwg.logicalexpression.Constants;
import org.omwg.logicalexpression.Constraint;
import org.omwg.logicalexpression.Disjunction;
import org.omwg.logicalexpression.Equivalence;
import org.omwg.logicalexpression.ExistentialQuantification;
import org.omwg.logicalexpression.Implication;
import org.omwg.logicalexpression.InverseImplication;
import org.omwg.logicalexpression.LogicProgrammingRule;
import org.omwg.logicalexpression.LogicalExpression;
import org.omwg.logicalexpression.MembershipMolecule;
import org.omwg.logicalexpression.Negation;
import org.omwg.logicalexpression.NegationAsFailure;
import org.omwg.logicalexpression.SubConceptMolecule;
import org.omwg.logicalexpression.Unary;
import org.omwg.logicalexpression.UniversalQuantification;
import org.omwg.logicalexpression.Visitor;
import org.omwg.ontology.Axiom;
import org.wsmo.common.WSML;
import org.wsmo.validator.ValidationError;


/**
 * Checks logical expressions for wsml-rule validity.
 *
 * @author nathalie.steinmetz@deri.org
 */
public class WsmlRuleExpressionValidator
        implements Visitor {
    
    private LogExprSerializerWSML leSerializer = null;
    
    private WsmlFullValidator validator = null;

    private Axiom axiom = null;
    
    private List <ValidationError> errors = null;
    
    private boolean body = false;
    
    /**
     * @param axiom whose logical expression is checked
     * @param errors list that will be filled with error messages of found variant violations
     */
    public WsmlRuleExpressionValidator(Axiom axiom, List <ValidationError> errors, WsmlFullValidator val) {
        this.axiom = axiom;
        this.errors = errors;
        this.validator = val;
        leSerializer = validator.leSerializer;
    }

    public void setup() {
        body = false;
    }
    
    /**
     * Checks if an Atom is valid to wsml-rule.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitAtom(org.omwg.logicalexpression.Atom)
     */
    public void visitAtom(Atom expr) {
        if (!body) {
            boolean infix = false;
            String iri = expr.getIdentifier().toString();
            infix = ConstantTransformer.getInstance().isInfix(iri);

            if (infix) {
                if (iri.indexOf(Constants.INEQUAL) != -1) {
                    addError(expr, ValidationError.AX_HEAD_ERR + ": Must not contain the " +
                             "inequality symbol \"!=\":\n" + leSerializer.serialize(expr));
                }
                else if (iri.indexOf(Constants.EQUAL) != -1) {
                    addError(expr, ValidationError.AX_HEAD_ERR + ": Must not contain the " +
                             "equality symbol \"=\":\n" + leSerializer.serialize(expr));
                }
                else if (iri.indexOf(Constants.STRONG_EQUAL) != -1) {
                    addError(expr, ValidationError.AX_HEAD_ERR + ": Must not contain the " +
                             "strong equality symbol \":=:\":\n" +
                             leSerializer.serialize(expr));
                }
            }
        }
        else {
            boolean infix = false;
            String iri = expr.getIdentifier().toString();
            infix = ConstantTransformer.getInstance().isInfix(iri);

            if (infix) {
                if (iri.indexOf(Constants.STRONG_EQUAL) != -1) {
                    addError(expr, ValidationError.AX_BODY_ERR + ": Must " +
                             "not contain the strong equality symbol \":=:\":\n" +
                             leSerializer.serialize(expr));
                }
            }
        }
    }

    /**
     * Checks if an AttributeConstraintMolecule is valid to wsml-rule.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitAttributeContraintMolecule(org.omwg.logicalexpression.AttributeConstraintMolecule)
     */
    public void visitAttributeContraintMolecule(AttributeConstraintMolecule expr) {

    }

    /**
     * Checks if an AttributeInferenceMolecule is valid to wsml-rule.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitAttributeInferenceMolecule(org.omwg.logicalexpression.AttributeInferenceMolecule)
     */
    public void visitAttributeInferenceMolecule(AttributeInferenceMolecule expr) {

    }

    /**
     * Checks if an AttributeValueMolecule is valid to wsml-rule.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitAttributeValueMolecule(org.omwg.logicalexpression.AttributeValueMolecule)
     */
    public void visitAttributeValueMolecule(AttributeValueMolecule expr) {

    }

    /**
     * Checks if an CompoundMolecule is valid to wsml-rule.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitCompoundMolecule(org.omwg.logicalexpression.CompoundMolecule)
     */
    public void visitCompoundMolecule(CompoundMolecule expr) {

    }

    /**
     * Checks if an MembershipMolecule is valid to wsml-rule.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitMemberShipMolecule(org.omwg.logicalexpression.MembershipMolecule)
     */
    public void visitMemberShipMolecule(MembershipMolecule expr) {

    }

    /**
     * Checks if an SubConceptMolecule is valid to wsml-rule.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitSubConceptMolecule(org.omwg.logicalexpression.SubConceptMolecule)
     */
    public void visitSubConceptMolecule(SubConceptMolecule expr) {

    }

    
    /**
     * Checks if a NegationAsFailure is valid to wsml-rule.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitNegationAsFailure(org.omwg.logicalexpression.NegationAsFailure)
     */
    public void visitNegationAsFailure(NegationAsFailure expr) {
        if (!body) {
            addError(expr, ValidationError.AX_HEAD_ERR + ": Must not " +
                    "contain a NegationAsFailure:\n" + 
                    leSerializer.serialize(expr));
        }
        else {
            expr.getOperand().accept(this);
        } 
    }

    /**
     * Checks if a Negation is valid to wsml-rule.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitNegation(org.omwg.logicalexpression.Negation)
     */
    public void visitNegation(Negation expr) {
        if (!body) {
            addError(expr, ValidationError.AX_HEAD_ERR + ": Must not " +
                    "contain a Negation:\n" + leSerializer.serialize(expr));
        }
        else {
            addError(expr, ValidationError.AX_BODY_ERR + ": Must not " +
                    "contain a Negation:\n" + leSerializer.serialize(expr));
            body = false;
        }
    }

    /**
     * Checks if a Constraint is valid to wsml-rule.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitConstraint(org.omwg.logicalexpression.Unary)
     */
    public void visitConstraint(Constraint expr) {
        if (!body) {
            body = true;
            expr.getOperand().accept(this);
        }
        else {
            addError(expr, ValidationError.AX_BODY_ERR + ": Must not " +
                    "contain a Negation:\n" + leSerializer.serialize(expr));
            body = false;
        }
    }

    /**
     * Checks if a Conjunction is valid to wsml-rule.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitConjunction(org.omwg.logicalexpression.Conjunction)
     */
    public void visitConjunction(Conjunction expr) {
        expr.getLeftOperand().accept(this);
        expr.getRightOperand().accept(this);
    }

    /**
     * Checks if a Disjunction is valid to wsml-rule.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitDisjunction(org.omwg.logicalexpression.Disjunction)
     */
    public void visitDisjunction(Disjunction expr) {
        if (!body) {
            addError(expr, ValidationError.AX_HEAD_ERR + ": Must not " +
                    "contain a Disjunction:\n" + leSerializer.serialize(expr));
        }
        else {
            expr.getLeftOperand().accept(this);
            expr.getRightOperand().accept(this);
        }
    }

    /**
     * Checks if an Equivalence is valid to wsml-rule.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitEquivalence(org.omwg.logicalexpression.Equivalence)
     */
    public void visitEquivalence(Equivalence expr) {
        if (!body) {
            if (checkHeads(expr)) {
                addError(expr, ValidationError.AX_HEAD_ERR + ": a and b from " +
                        "\"a equivalent b\" must not contain {implies, " +
                        "impliedBy, equivalent):\n" + leSerializer.serialize(expr));
            }
            else {
                expr.getLeftOperand().accept(this);
                expr.getRightOperand().accept(this);
            }
        }
        else {
            expr.getLeftOperand().accept(this);
            expr.getRightOperand().accept(this);
        }
    }

    /**
     * Checks if an InverseImplication is valid to wsml-rule.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitInverseImplication(org.omwg.logicalexpression.InverseImplication)
     */
    public void visitInverseImplication(InverseImplication expr) {
        if (!body) {
            if (checkHeads(expr)) {
                addError(expr, ValidationError.AX_HEAD_ERR + ": a and b from " +
                        "\"a impliedBy b\" must not contain {implies, " +
                        "impliedBy, equivalent):\n" + leSerializer.serialize(expr));
            }
            else {
                expr.getLeftOperand().accept(this);
                expr.getRightOperand().accept(this);
            }
        }
        else {
            expr.getLeftOperand().accept(this);
            expr.getRightOperand().accept(this);
        }
    }

    /**
     * Checks if an Implication is valid to wsml-rule.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitImplication(org.omwg.logicalexpression.Implication)
     */
    public void visitImplication(Implication expr) {
        if (!body) {
            if (checkHeads(expr)) {
                addError(expr, ValidationError.AX_HEAD_ERR + ": a and b " +
                        "from \"a implies b\" must not contain {implies, " +
                        "impliedBy, equivalent):\n" + leSerializer.serialize(expr));
            }
            else {
                expr.getLeftOperand().accept(this);
                expr.getRightOperand().accept(this);
            }
        }
        else {
            expr.getLeftOperand().accept(this);
            expr.getRightOperand().accept(this);
        }
    }

    /**
     * Checks if a LogicProgrammingRule is valid to wsml-rule.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitLogicProgrammingRule(org.omwg.logicalexpression.LogicProgrammingRule)
     */
    public void visitLogicProgrammingRule(LogicProgrammingRule expr) {
        body = false;
        expr.getLeftOperand().accept(this);
        body = true;
        expr.getRightOperand().accept(this);
    }

    /**
     * Checks if a UniversalQuantification is valid to wsml-rule.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitUniversalQuantification(org.omwg.logicalexpression.UniversalQuantification)
     */
    public void visitUniversalQuantification(UniversalQuantification expr) {
        if (!body) {
            addError(expr, ValidationError.AX_HEAD_ERR + ": Must not " +
                    "contain an UniversalQuantification:\n" + 
                    leSerializer.serialize(expr));
        }
        else {
            expr.getOperand().accept(this);
        }
        
    }
    
    /**
     * Checks if an ExistentialQuantification is valid to wsml-rule.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitExistentialQuantification(org.omwg.logicalexpression.ExistentialQuantification)
     */
    public void visitExistentialQuantification(ExistentialQuantification expr) {
        if (!body) {
            addError(expr, ValidationError.AX_HEAD_ERR + ": Must not " +
                    "contain an ExistentialQuantification:\n" + 
                    leSerializer.serialize(expr));
        }
        else {
            expr.getOperand().accept(this);
        }
        
    }

    /*
     * check if Equivalences, Implications and InverseImplications don't contain
     * themselves other Equivalences, Implications or InverseImplications
     */
    private boolean checkHeads(LogicalExpression expr) {
        boolean flag = false;
        if (expr instanceof Binary) {
            LogicalExpression lLeft = ((Binary) expr).getLeftOperand();
            LogicalExpression lRight = ((Binary) expr).getRightOperand();
            if (lLeft instanceof Equivalence || lLeft instanceof Implication 
                    || lLeft instanceof InverseImplication) {
                flag = true;
            }
            else {
                checkHeads(lLeft);
            }
            if (lRight instanceof Equivalence || lRight instanceof Implication 
                    || lRight instanceof InverseImplication) {
                flag = true;
            }
            else {
                checkHeads(lRight);
            }
        }
        else if (expr instanceof Unary) {
            LogicalExpression l = ((Unary) expr).getOperand();
            if (l instanceof Equivalence || l instanceof Implication 
                    || l instanceof InverseImplication) {
                flag = true;
            }
            else {
                checkHeads(l);
            }
        }     
        return flag;
    }
    
    private void addError(LogicalExpression logexp, String msg) {
        LogicalExpressionErrorImpl le = new LogicalExpressionErrorImpl(
                axiom, logexp, msg, WSML.WSML_RULE);
        if (!errors.contains(le)) {
            errors.add(le);
        }
    }
}
