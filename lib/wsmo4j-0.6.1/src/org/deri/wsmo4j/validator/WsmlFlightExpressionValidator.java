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

import java.util.*;

import org.deri.wsmo4j.io.serializer.wsml.LogExprSerializerWSML;
import org.omwg.logicalexpression.*;
import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.factory.*;
import org.wsmo.validator.ValidationError;

/**
 * Checks logical expressions for wsml-flight validity.
 * 
 * @author nathalie.steinmetz@deri.org
 */
public class WsmlFlightExpressionValidator implements Visitor {

    private LogExprSerializerWSML leSerializer = null;
    
    private LogicalExpressionFactory leFactory = null;
    
    private WsmlFullValidator validator = null;
    
    private Axiom axiom = null;
    
    private List <ValidationError> errors = null;
    
    private boolean body = false;
    
    private boolean found = false;
    
    private boolean builtIn = false;
    
    private boolean foundBuiltIn = false;
    
    private boolean foundNaf = false;
    
    private boolean unSafe = false;
    
    private boolean foundPos = false;

    private String errorMessage = "";

    /**
     * @param axiom
     *            whose logical expression is checked
     * @param errors
     *            list that will be filled with error messages of found variant
     *            violations
     */
    protected WsmlFlightExpressionValidator(Axiom axiom, LogicalExpressionFactory leFactory, List <ValidationError> errors, WsmlFullValidator val) {
        this.axiom = axiom;
        this.errors = errors;
        this.validator = val;
        leSerializer = validator.leSerializer;
        this.leFactory = leFactory;
    }

    public void setup() {
        body = false;
        found = false;
        foundBuiltIn = false;
        builtIn = false;
        foundNaf = false;
        foundPos = false;
    }
    
    /**
     * Checks if an Atom is valid to wsml-flight.
     * 
     * @see org.deri.wsmo4j.validator.WsmlRuleExpressionValidator#visitAtom(org.omwg.logicalexpression.Atom)
     */
    public void visitAtom(Atom expr) {
        if (!body) {
            containsVariables(expr);
        }   
    }

    /**
     * Checks if an AttributeConstraintMolecule is valid to wsml-flight.
     * 
     * @see org.deri.wsmo4j.validator.WsmlRuleExpressionValidator#visitAttributeContraintMolecule(org.omwg.logicalexpression.AttributeConstraintMolecule)
     */
    public void visitAttributeContraintMolecule(AttributeConstraintMolecule expr) {
        if (!body) {
            containsVariables(expr);
        }  
    }

    /**
     * Checks if an AttributeInferenceMolecule is valid to wsml-flight.
     * 
     * @see org.deri.wsmo4j.validator.WsmlRuleExpressionValidator#visitAttributeInferenceMolecule(org.omwg.logicalexpression.AttributeInferenceMolecule)
     */
    public void visitAttributeInferenceMolecule(AttributeInferenceMolecule expr) {
        if (!body) {
            containsVariables(expr);
        }  
    }

    /**
     * Checks if an AttributeValueMolecule is valid to wsml-flight.
     * 
     * @see org.deri.wsmo4j.validator.WsmlRuleExpressionValidator#visitAttributeValueMolecule(org.omwg.logicalexpression.AttributeValueMolecule)
     */
    public void visitAttributeValueMolecule(AttributeValueMolecule expr) {
        if (!body) {
            containsVariables(expr);
        }  
    }

    /**
     * Checks if an CompoundMolecule is valid to wsml-flight.
     * 
     * @see org.deri.wsmo4j.validator.WsmlRuleExpressionValidator#visitCompoundMolecule(org.omwg.logicalexpression.CompoundMolecule)
     */
    public void visitCompoundMolecule(CompoundMolecule expr) {
        if (!body) {
            containsVariables(expr);
        }  
    }

    /**
     * Checks if an MembershipMolecule is valid to wsml-flight.
     * 
     * @see org.deri.wsmo4j.validator.WsmlRuleExpressionValidator#visitMemberShipMolecule(org.omwg.logicalexpression.MembershipMolecule)
     */
    public void visitMemberShipMolecule(MembershipMolecule expr) {
        if (!body) {
            containsVariables(expr);
        }  
    }

    /**
     * Checks if an SubConceptMolecule is valid to wsml-flight.
     * 
     * @see org.deri.wsmo4j.validator.WsmlRuleExpressionValidator#visitSubConceptMolecule(org.omwg.logicalexpression.SubConceptMolecule)
     */
    public void visitSubConceptMolecule(SubConceptMolecule expr) {
        if (!body) {
            containsVariables(expr);
        }  
    }

    
    /**
     * Checks if a Negation is valid to wsml-flight.
     * 
     * @see org.deri.wsmo4j.validator.WsmlRuleExpressionValidator#visitNegation(org.omwg.logicalexpression.Negation)
     */
    public void visitNegation(Negation expr) {

    }

    /**
     * Checks if a NegationAsFailure is valid to wsml-flight.
     * 
     * @see org.deri.wsmo4j.validator.WsmlRuleExpressionValidator#visitNegationAsFailure(org.omwg.logicalexpression.NegationAsFailure)
     */
    public void visitNegationAsFailure(NegationAsFailure expr) {
        if (body) {
            expr.getOperand().accept(this);
        }
    }

    /**
     * Checks if a Constraint is valid to wsml-flight.
     * 
     * @see org.deri.wsmo4j.validator.WsmlRuleExpressionValidator#visitConstraint(org.omwg.logicalexpression.Constraint)
     */
    public void visitConstraint(Constraint expr) {

    }

    /**
     * Checks if a Conjunction is valid to wsml-flight.
     * 
     * @see org.deri.wsmo4j.validator.WsmlRuleExpressionValidator#visitConjunction(org.omwg.logicalexpression.Conjunction)
     */
    public void visitConjunction(Conjunction expr) {
        if (body) {
            expr.getLeftOperand().accept(this);
            expr.getRightOperand().accept(this);
        }
    }

    /**
     * Checks if a Disjunction is valid to wsml-flight.
     * 
     * @see org.deri.wsmo4j.validator.WsmlRuleExpressionValidator#visitDisjunction(org.omwg.logicalexpression.Disjunction)
     */
    public void visitDisjunction(Disjunction expr) {
        if (body) {
            expr.getLeftOperand().accept(this);
            expr.getRightOperand().accept(this);
        }
    }

    /**
     * Checks if an InverseImplication is valid to wsml-flight.
     * 
     * @see org.deri.wsmo4j.validator.WsmlRuleExpressionValidator#visitInverseImplication(org.omwg.logicalexpression.InverseImplication)
     */
    public void visitInverseImplication(InverseImplication expr) {
        if (body) {
            addError(expr, ValidationError.AX_BODY_ERR + ": Must not " +
                    "contain an InverseImplication:\n" + leSerializer.serialize(expr));
            body = false;
        }
    }

    /**
     * Checks if an Implication is valid to wsml-flight.
     * 
     * @see org.deri.wsmo4j.validator.WsmlRuleExpressionValidator#visitImplication(org.omwg.logicalexpression.Implication)
     */
    public void visitImplication(Implication expr) {
        if (body) {
            addError(expr, ValidationError.AX_BODY_ERR + ": Must not " +
                    "contain an Implication:\n" + leSerializer.serialize(expr));
            body = false;
        }
    }

    /**
     * Checks if an Equivalence is valid to wsml-flight.
     * 
     * @see org.deri.wsmo4j.validator.WsmlRuleExpressionValidator#visitEquivalence(org.omwg.logicalexpression.Equivalence)
     */
    public void visitEquivalence(Equivalence expr) {
        if (body) {
            addError(expr, ValidationError.AX_BODY_ERR + ": Must not " +
                    "contain an Equivalence:\n" + leSerializer.serialize(expr));
            body = false;
        }
    }

    /**
     * Checks if a LogicProgrammingRule is valid to wsml-flight.
     * 
     * @see org.deri.wsmo4j.validator.WsmlRuleExpressionValidator#visitLogicProgrammingRule(org.omwg.logicalexpression.LogicProgrammingRule)
     */
    public void visitLogicProgrammingRule(LogicProgrammingRule expr) {
        body = true;
        expr.getRightOperand().accept(this);
        holdsSafetyCondition(expr);
    }

    /**
     * Checks if a UniversalQuantification is valid to wsml-flight.
     * 
     * @see org.deri.wsmo4j.validator.WsmlRuleExpressionValidator#visitUniversalQuantification(org.omwg.logicalexpression.UniversalQuantification)
     */
    public void visitUniversalQuantification(UniversalQuantification expr) {
        if (body) {
            addError(expr, ValidationError.AX_BODY_ERR + ": Must not " +
                    "contain a UniversalQuantification:\n" + leSerializer.serialize(expr));
            body = false;
        }
    }

    /**
     * Checks if an ExistentialQuantification is valid to wsml-flight.
     * 
     * @see org.deri.wsmo4j.validator.WsmlRuleExpressionValidator#visitExistentialQuantification(org.omwg.logicalexpression.ExistentialQuantification)
     */
    public void visitExistentialQuantification(ExistentialQuantification expr) {
        if (body) {
            addError(expr, ValidationError.AX_BODY_ERR + ": Must not " +
                    "contain an ExistentialQuantification:\n" + leSerializer.serialize(expr));
            body = false;
        }
    } 

    private boolean containsVariables(LogicalExpression expr) {
        boolean flag = false;
        if (expr instanceof CompoundMolecule) {
            if (!((CompoundMolecule) expr).listAttributeConstraintMolecules().isEmpty()) {
                Iterator it = ((CompoundMolecule) expr).listAttributeConstraintMolecules().iterator();
                while (it.hasNext()) {
                    containsVariables((AttributeConstraintMolecule) it.next());
                }
            }
            else if (!((CompoundMolecule) expr).listAttributeInferenceMolecules().isEmpty()) {
                Iterator it = ((CompoundMolecule) expr).listAttributeInferenceMolecules().iterator();
                while (it.hasNext()) {
                    containsVariables((AttributeInferenceMolecule) it.next());
                }
            }
            else if (!((CompoundMolecule) expr).listAttributeValueMolecules().isEmpty()) {
                Iterator it = ((CompoundMolecule) expr).listAttributeValueMolecules().iterator();
                while (it.hasNext()) {
                    containsVariables((AttributeValueMolecule) it.next());
                }
            }
            else if (!((CompoundMolecule) expr).listMemberShipMolecules().isEmpty()) {
                Iterator it = ((CompoundMolecule) expr).listMemberShipMolecules().iterator();
                while (it.hasNext()) {
                    containsVariables((MembershipMolecule) it.next());
                }
            }
            else if (!((CompoundMolecule) expr).listSubConceptMolecules().isEmpty()) {
                Iterator it = ((CompoundMolecule) expr).listSubConceptMolecules().iterator();
                while (it.hasNext()) {
                    containsVariables((SubConceptMolecule) it.next());
                }
            }
        }
        else if (expr instanceof Molecule) {
            if (((Molecule) expr).getLeftParameter() instanceof Variable) {
                flag = true;
            }
            if (((Molecule) expr).getRightParameter() instanceof Variable) {
                flag = true;
            }
        }
        else if (expr instanceof Atom) {
            Iterator it = ((Atom) expr).listParameters().iterator();
            while (it.hasNext()) {
                if (it.next() instanceof Variable) {
                    flag = true;
                }
            }
        }
        if (flag) {
            addError(expr, ValidationError.AX_HEAD_ERR + ":\n Any variable-free " +
                    "admissible head formula is an admissible formula:\n" +
                    leSerializer.serialize(expr));
            return false;
        }
        return true;
    }

    private void holdsSafetyCondition(LogicProgrammingRule l) {
        LogicalExpression leL = l.getLeftOperand();
        LogicalExpression leR = l.getRightOperand();
        if (leL instanceof Conjunction) {
            transformHeadConjunction(l);
        }
        else if (leL instanceof Equivalence) {
            transformHeadEquivalence(l);
        }
        else if (leL instanceof Implication) {
            transformHeadImplication(l);
        }
        else if (leL instanceof InverseImplication) {
            transformHeadInverseImplication(l);
        }
        else if (leR instanceof Disjunction) {
            transformBody(l);
        }
        else {
            isSafe(l);
        }
    }
    
    private void transformHeadInverseImplication(LogicProgrammingRule l) {
        LogicProgrammingRule lpr;
        Conjunction c;
        c = leFactory.createConjunction(((Binary) l.getLeftOperand()).getRightOperand(), 
                l.getRightOperand());
        lpr = leFactory.createLogicProgrammingRule(((Binary) l.getLeftOperand()).getLeftOperand(),
                c);
        holdsSafetyCondition(lpr);
    }
    
    private void transformHeadImplication(LogicProgrammingRule l) {
        LogicProgrammingRule lpr;
        Conjunction c;
        c = leFactory.createConjunction(((Binary) l.getLeftOperand()).getLeftOperand(), 
                l.getRightOperand());
        lpr = leFactory.createLogicProgrammingRule(((Binary) l.getLeftOperand()).getRightOperand(),
                c);
        holdsSafetyCondition(lpr);
    }
    
    private void transformHeadEquivalence(LogicProgrammingRule l) {
        LogicProgrammingRule lpr1;
        LogicProgrammingRule lpr2;
        Implication i;
        InverseImplication ini;
        i = leFactory.createImplication(((Binary) l.getLeftOperand()).getLeftOperand(), 
                ((Binary) l.getLeftOperand()).getRightOperand());
        ini = leFactory.createInverseImplication(((Binary) l.getLeftOperand()).getLeftOperand(), 
                ((Binary) l.getLeftOperand()).getRightOperand());
        lpr1 = leFactory.createLogicProgrammingRule(i, l.getRightOperand());
        lpr2 = leFactory.createLogicProgrammingRule(ini, l.getRightOperand());
        holdsSafetyCondition(lpr1);
        holdsSafetyCondition(lpr2);
    }
    
    private void transformHeadConjunction(LogicProgrammingRule l) {
        LogicProgrammingRule lpr1;
        LogicProgrammingRule lpr2;
        lpr1 = leFactory.createLogicProgrammingRule(((Binary) l.getLeftOperand()).getLeftOperand(),
                l.getRightOperand());
        lpr2 = leFactory.createLogicProgrammingRule(
                ((Binary) l.getLeftOperand()).getRightOperand(), l.getRightOperand());
        holdsSafetyCondition(lpr1);
        holdsSafetyCondition(lpr2);
    }

    private void transformBody(LogicProgrammingRule l) {
        LogicProgrammingRule lpr1;
        LogicProgrammingRule lpr2;
        lpr1 = leFactory.createLogicProgrammingRule(l.getLeftOperand(), ((Disjunction) l
                .getRightOperand()).getLeftOperand());
        lpr2 = leFactory.createLogicProgrammingRule(l.getLeftOperand(), ((Disjunction) l
                .getRightOperand()).getRightOperand());
        holdsSafetyCondition(lpr1);
        holdsSafetyCondition(lpr2);
    }

    private void isSafe(LogicProgrammingRule l) {
        found = false;
        foundPos = false;
        LogicalExpression ll = l.getLeftOperand();
        LogicalExpression lr = l.getRightOperand();
        HashSet <Term> leftSet = new HashSet <Term> ();
        leftSet = getSetOfHeadVariables(ll, leftSet);
        if (!leftSet.isEmpty()) {
            Iterator it = leftSet.iterator();
            while (it.hasNext()) {
                Variable v = (Variable) it.next();
                checkBodyVariables(lr, v, false);
                if (unSafe) {
                    addError(l, ValidationError.AX_SAFETY_COND + ": " + 
                            "\nError: " + errorMessage + ":\n" +
                            leSerializer.serialize(l));
                }
            }
        }
    }

    private HashSet <Term> getSetOfHeadVariables(LogicalExpression le, HashSet <Term> set) {
        if (le instanceof Atom) {
            for (int i = 0; i < ((Atom) le).getArity(); i++) {
                if (((Atom) le).getParameter(i) instanceof Variable) {
                    set.add(((Atom) le).getParameter(i));
                }
            }
        }
        else if (le instanceof CompoundMolecule) {
            Iterator i = ((CompoundMolecule) le).listOperands().iterator();
            while (i.hasNext()) {
                getSetOfHeadVariables((Molecule) i.next(), set);
            }
        }
        else if (le instanceof Molecule) {
            if (((Molecule) le).getLeftParameter() instanceof Variable) {
                set.add(((Molecule) le).getLeftParameter());
            }
            if (((Molecule) le).getRightParameter() instanceof Variable) {
                set.add(((Molecule) le).getRightParameter());
            }
            if (le instanceof AttributeMolecule) {
                if (((AttributeMolecule) le).getAttribute() instanceof Variable) {
                    set.add(((AttributeMolecule) le).getAttribute());
                }
            }
        }
        return set;
    }

    private void checkBodyVariables(LogicalExpression le, Variable v, boolean naf) {
        unSafe = false;
        if (le instanceof Conjunction) {
            LogicalExpression ll = ((Conjunction) le).getLeftOperand();
            LogicalExpression lr = ((Conjunction) le).getRightOperand();
            checkBodyVariables(ll, v, false);
            checkBodyVariables(lr, v, false);
        }
        else if (le instanceof NegationAsFailure) {
            LogicalExpression l = ((NegationAsFailure) le).getOperand();
            if (!foundNaf) {
                foundNaf = true;
                checkBodyVariables(l, v, true);
            }
            else {
                foundNaf = false;
                checkBodyVariables(l, v, false);
            }
            
        }
        else {
            if (le instanceof Atom) {
                if (le instanceof BuiltInAtom) {
                    builtIn = true;
                }
                for (int i = 0; i < ((Atom) le).getArity(); i++) {
                    if (((Atom) le).getParameter(i) instanceof Variable) {
                        if (((Atom) le).getParameter(i).equals(v)) {
                            found = true;
                            if (naf) {
                                foundNaf = true;
                            }
                            else {
                                foundPos = true;
                            }
                            if (builtIn) {
                                foundBuiltIn = true;
                            }
                        }
                    }
                }
            }
            else if (le instanceof CompoundMolecule) {
                Iterator i = ((CompoundMolecule) le).listOperands().iterator();
                while (i.hasNext()) {
                    if (findVariable((Molecule) i.next(), v)) {
                        found = true;
                        if (naf) {
                            foundNaf = true;
                        }
                        if (builtIn) {
                            foundBuiltIn = true;
                        }
                    }
                }
            }
            else if (le instanceof Molecule) {
                if (findVariable((Molecule) le, v)) {
                    found = true;
                    if (naf) {
                        foundNaf = true;
                    }
                    else {
                        foundPos = true;
                    }
                    if (builtIn) {
                        foundBuiltIn = true;
                    }
                }
            }
            if (foundNaf && !foundPos) {
                unSafe = true;
                errorMessage = "Variable " + v.toString() + " occurs in a negative body literal";
            }
            if (foundBuiltIn) {
                unSafe = true;
                errorMessage = "Variable " + v.toString() + " occurs in a body literal that "
                        + "corresponds to a built-in predicate";
            }
            if (!found) {
                unSafe = true;
                errorMessage = "Variable " + v.toString() + " does not occur in the body literal";
            }
        }
    }

    private boolean findVariable(Molecule m, Variable v) {
        if (m.getLeftParameter().equals(v) || m.getRightParameter().equals(v)) {
            return true;
        }
        if (!(m instanceof AttributeMolecule)) {
            return false;
        }
        return ((AttributeMolecule) m).getAttribute().equals(v);
    }

    private void addError(LogicalExpression logexp, String msg) {
        LogicalExpressionErrorImpl le = new LogicalExpressionErrorImpl(
                axiom, logexp, msg, WSML.WSML_FLIGHT);
        if (!errors.contains(le)) {
            errors.add(le);
        }
    }
}
