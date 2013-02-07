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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.deri.wsmo4j.io.serializer.wsml.LogExprSerializerWSML;
import org.deri.wsmo4j.logicalexpression.ConstantTransformer;
import org.omwg.logicalexpression.Atom;
import org.omwg.logicalexpression.AtomicExpression;
import org.omwg.logicalexpression.AttributeConstraintMolecule;
import org.omwg.logicalexpression.AttributeInferenceMolecule;
import org.omwg.logicalexpression.AttributeValueMolecule;
import org.omwg.logicalexpression.Binary;
import org.omwg.logicalexpression.CompoundMolecule;
import org.omwg.logicalexpression.Conjunction;
import org.omwg.logicalexpression.Constraint;
import org.omwg.logicalexpression.Disjunction;
import org.omwg.logicalexpression.Equivalence;
import org.omwg.logicalexpression.ExistentialQuantification;
import org.omwg.logicalexpression.Implication;
import org.omwg.logicalexpression.InverseImplication;
import org.omwg.logicalexpression.LogicProgrammingRule;
import org.omwg.logicalexpression.LogicalExpression;
import org.omwg.logicalexpression.MembershipMolecule;
import org.omwg.logicalexpression.Molecule;
import org.omwg.logicalexpression.Negation;
import org.omwg.logicalexpression.NegationAsFailure;
import org.omwg.logicalexpression.SubConceptMolecule;
import org.omwg.logicalexpression.UniversalQuantification;
import org.omwg.logicalexpression.Visitor;
import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.Axiom;
import org.omwg.ontology.Variable;
import org.wsmo.common.WSML;
import org.wsmo.factory.LogicalExpressionFactory;
import org.wsmo.validator.ValidationError;


/**
 * Checks logical expressions for wsml-core validity.
 *
 * <pre>
 *  Created on Aug 19, 2005
 *  Committed by $Author: morcen $
 *  $Source: /cvsroot/wsmo4j/wsmo4j/org/deri/wsmo4j/validator/WsmlCoreExpressionValidator.java,v $,
 * </pre>
 *
 * @author nathalie.steinmetz@deri.org
 * @version $Revision: 1.39 $ $Date: 2007/04/02 12:13:20 $
 */
public class WsmlCoreExpressionValidator 
        implements Visitor{

    private Set <Term> equivVariables = null;
    
    private Set <Term> lhsVariables = null;
    
    private Set <Term> rhsVariables = null;
    
    private int numberOfMolecules = 0;
    
    private Set <List <Term>> molecules = null;
    
    private WsmlCoreValidator validator = null;
    
    private LogExprSerializerWSML leSerializer = null;
    
    private LogicalExpressionFactory leFactory = null;
    
    private Axiom axiom = null;
    
    private List <ValidationError> errors = null;
    
    private String error;
    
    private boolean formula;
    
    private boolean rhs;
    
    private boolean lhs;
    
    private boolean errorFlag;
    
    
    /**
     * @param axiom whose logical expression is checked
     * @param errors list that will be filled with error messages of found variant violations
     */
    protected WsmlCoreExpressionValidator(Axiom axiom, LogicalExpressionFactory leFactory, List <ValidationError> errors, WsmlCoreValidator val) {
        this.axiom = axiom;
        this.errors = errors;
        this.validator = val;
        leSerializer = validator.leSerializer;
        this.leFactory = leFactory;
    }

    protected void setup() {
        lhsVariables = new HashSet <Term> ();
        numberOfMolecules = 0;
        molecules = new HashSet <List <Term>> ();
        equivVariables = new HashSet <Term> ();
        lhsVariables = new HashSet <Term> ();
        rhsVariables = new HashSet <Term> ();
        formula = false;
        rhs = false;
        lhs = false;
        errorFlag = true;
    }
    
    /**
     * Checks if an atom is valid to wsml-core.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFlightExpressionValidator#visitAtom(org.omwg.logicalexpression.Atom)
     */
    public void visitAtom(Atom expr) {
        Molecule molecule = atomToMolecule(expr);
        if (molecule != null) {
            molecule.accept(this);
        }
    }

    /**
     * Checks if an AttributeConstraintMolecule is valid to wsml-core.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFlightExpressionValidator#visitAttributeContraintMolecule(org.omwg.logicalexpression.AttributeConstraintMolecule)
     */
    public void visitAttributeContraintMolecule(AttributeConstraintMolecule expr) {
        String error = ":\n Cannot contain an AttributeConstraintMolecule:\n" 
            + leSerializer.serialize(expr);
        // An AttributeConstraintMolecule is no valid right-hand side formula
        if (rhs) {      
            if (errorFlag) {
                addError(expr, ValidationError.AX_RHS_ERR + error); 
            }
        }
        else if (lhs) {
            if (errorFlag) {
                addError(expr, ValidationError.AX_LHS_ERR + error);
            }
        }
        else {
            // the identifier must be a Concept
            if (check(expr.getLeftParameter(), false, true, true, true, true)) {
                addError(expr, ValidationError.AX_ATOMIC_ERR + ":"
                        + "\nThe range identifier must be a Concept, " +
                        "not a " + error + "\n" + leSerializer.serialize(expr));
            }
            // the arguments must be datatypes
            if (check(expr.getRightParameter(), true, true, true, false, true)) {
                addError(expr, ValidationError.AX_ATOMIC_ERR + ":"
                        + "\nThe arguments must be " +
                        "Datatypes, not " + error + "\n"  + leSerializer.serialize(expr));
            }
            // the attribute's name must be a Relation with concrete range
            if ((check(expr.getAttribute(), true, true, false, true, true))
                    || (!checkConcreteRelations(expr.getAttribute()))){
                addError(expr, ValidationError.AX_ATOMIC_ERR + ":" 
                        + "\nThe name must be a Relation with concrete range, " +
                        "not a " + error + "\n" + leSerializer.serialize(expr));
            } 
        }
    }

    /**
     * Checks if an AttributeInferenceMolecule is valid to wsml-core.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFlightExpressionValidator#visitAttributeInferenceMolecule(org.omwg.logicalexpression.AttributeInferenceMolecule)
     */
    public void visitAttributeInferenceMolecule(AttributeInferenceMolecule expr) {
        String error = ":\n Cannot contain an AttributeInferenceMolecule:\n" 
            + leSerializer.serialize(expr);
        // An AttributeInferenceMolecule is no valid right-hand side formula
        if (rhs) {      
            if (errorFlag) {
                addError(expr, ValidationError.AX_RHS_ERR + error);
            }
        }
        else if (lhs) {
            if (errorFlag) {
                addError(expr, ValidationError.AX_LHS_ERR + error);
            }
        }
        else {
            // the identifier must be a Concept
            if (check(expr.getLeftParameter(), false, true, true, true, true)) {
                addError(expr, ValidationError.AX_ATOMIC_ERR + ":"
                        + "\nThe range identifier must be a Concept, " +
                        "not a " + error + "\n" + leSerializer.serialize(expr));
            }
            // the arguments of an infering attribute must be datatypes or concepts
            if (check(expr.getRightParameter(), false, true, true, false, true)) {
                addError(expr, ValidationError.AX_ATOMIC_ERR + ": \nThe arguments " +
                        "must be Datatypes or Concepts, " +
                        "not " + error + "\n" + leSerializer.serialize(expr));
            }    
            else { 
                // if the argument is a datatype, the attribute's name must be a relation with concrete range
                if (checkDatatypes(expr.getRightParameter())) {
                    if (!checkConcreteRelations(expr.getAttribute())) {
                        addError(expr, ValidationError.AX_ATOMIC_ERR + ": \nThe name " +
                                "must be a relation with concrete range " +
                                "(because the argument is a datatype)\n" + leSerializer.serialize(expr));   
                    }
                }
                // if the argument is a concept, the attribute's name must be a relation with abstract range
                else if (checkConcepts(expr.getRightParameter())) {
                    if (!checkAbstractRelations(expr.getAttribute())) {
                        addError(expr, ValidationError.AX_ATOMIC_ERR + ": \nThe name " +
                                "must be a relation with abstract range " +
                                "(because the argument is a concept)\n" + leSerializer.serialize(expr));   
                    }
                }
            }
        }
    }

    /**
     * Checks if an AttributeValueMolecule is valid to wsml-core.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFlightExpressionValidator#visitAttributeValueMolecule(org.omwg.logicalexpression.AttributeValueMolecule)
     */
    public void visitAttributeValueMolecule(AttributeValueMolecule expr) {
        // An AttributeValueMolecule is no valid right-hand side formula
        if (rhs) {      
            if (errorFlag) {
                addError(expr, ValidationError.AX_RHS_ERR + ":\n Cannot " +
                        "contain an AttributeValueMolecule:\n" 
                        + leSerializer.serialize(expr));
            }
        }
        // check if an AttributeValueMolecule is a valid b-molecule
        if (lhs || formula) {
            // the identifier of the molecule must be a variable
            if (!(expr.getLeftParameter() instanceof Variable)) {
                if (errorFlag) {
                    addError(expr, ValidationError.AX_FORMULA_ERR + ": A valid b-molecule " +
                            "must contain a variable as identifier\n" + leSerializer.serialize(expr));
                }
                return;
            }
            else if (!lhsVariables.contains(expr.getLeftParameter())) {
                lhsVariables.add(expr.getLeftParameter());
            }
            
            // the name of the attribute must be a relation
            if (!checkAbstractRelations(expr.getAttribute())) {
                if (errorFlag) {
                    addError(expr, ValidationError.AX_FORMULA_ERR + ": A valid b-molecule's " +
                            "attribute name must be a relation with abstract range\n" 
                            + leSerializer.serialize(expr));
                }
                return;
            }
          
            // the arguments of the attribute must be variables
            if (!(expr.getRightParameter() instanceof Variable)) {
                if (errorFlag) {
                    addError(expr, ValidationError.AX_FORMULA_ERR + ": A valid b-molecule's " +
                            "attribute's arguments must be variables\n" + leSerializer.serialize(expr));
                }
                return;
            }
            else if (!lhsVariables.contains(expr.getRightParameter())) {
                lhsVariables.add(expr.getRightParameter());
            }
                  
            List <Term> l = new Vector <Term> ();
            l.add(expr.getLeftParameter());
            if (!expr.getLeftParameter().equals(expr.getRightParameter())) {
                l.add(expr.getRightParameter());
            }
            numberOfMolecules = numberOfMolecules + 1;
            molecules.add(l);
        }
        else {
            // the identifier must be an Instance
            if (check(expr.getLeftParameter(), true, false, true, true, true)) {
                addError(expr, ValidationError.AX_ATOMIC_ERR + ":" 
                        + "\nThe range identifier must be an Instance, " +
                        "not a " + error + "\n" + leSerializer.serialize(expr));
            }
            // the arguments of a value definition attribute must be DataValues or Instances
            if (check(expr.getRightParameter(), true, false, true, false, true)) {
                addError(expr, ValidationError.AX_ATOMIC_ERR + ":\nThe arguments " +
                        "must be DataValues or Instances, " +
                        "not " + error + "\n" + leSerializer.serialize(expr));
            }
            else {
                // if the argument is a DataValue, the attribute's name must be a relation with concrete range
                if (checkDatatypes(expr.getRightParameter())) {
                    if (!checkConcreteRelations(expr.getAttribute())) {
                        addError(expr, ValidationError.AX_ATOMIC_ERR + ": \nThe name " +
                                "must be a relation with concrete range " +
                                "(because the argument is a DataValue)\n" + leSerializer.serialize(expr));   
                    }
                }
                // if the argument is an instance, the attribute's name must be a relation with abstract range
                else if (checkInstances(expr.getRightParameter())) {
                    if (!checkAbstractRelations(expr.getAttribute())) {
                        addError(expr, ValidationError.AX_ATOMIC_ERR + ": \nThe name " +
                                "must be a relation with abstract range " +
                                "(because the argument is an instance)\n" + leSerializer.serialize(expr));   
                    }
                }
            }
        }
    }

    /**
     * Checks if a CompoundMolecule is valid to wsml-core.
     *
     * @see org.deri.wsmo4j.validator.WsmlFlightExpressionValidator#visitCompoundMolecule(org.omwg.logicalexpression.CompoundMolecule)
     */
    public void visitCompoundMolecule(CompoundMolecule expr) {
        Iterator i = expr.listOperands().iterator();
        while(i.hasNext()){
            ((Molecule)i.next()).accept(this);
        }
    }

    /**
     * Checks if a MembershipMolecule is valid to wsml-core.
     *
     * @see org.deri.wsmo4j.validator.WsmlFlightExpressionValidator#visitMemberShipMolecule(org.omwg.logicalexpression.MembershipMolecule)
     */
    public void visitMemberShipMolecule(MembershipMolecule expr) {
        // Checks if a MembershipMolecule is a valid a-molecule
        if (rhs) {
            // the identifier of the molecule must be a variable
            if (!(expr.getLeftParameter() instanceof Variable)) {
                if (errorFlag) {
                    addError(expr, ValidationError.AX_FORMULA_ERR + ":\n A valid a-molecule " +
                            "must contain a variable as identifier\n" + leSerializer.serialize(expr));
                }
            }
            else {
                if (!equivVariables.contains(expr.getLeftParameter())) {
                    equivVariables.add(expr.getLeftParameter());
                }
                if (!rhsVariables.contains(expr.getLeftParameter())) {
                    rhsVariables.add(expr.getLeftParameter());
                }
            }
            // the argument of the molecule must be a Concept
            if (check(expr.getRightParameter(), false, true, true, true, true)) {
                if (errorFlag) {
                    addError(expr, ValidationError.AX_FORMULA_ERR + ":" 
                            + "\nError: The arguments of an a-molecule must be Concepts\n"
                            + leSerializer.serialize(expr));
                }
            }
        }
        else if (lhs) {
            if (errorFlag) {
                addError(expr, ValidationError.AX_LHS_ERR + ":\n Cannot " +
                        "contain a MembershipMolecule:\n" 
                        + leSerializer.serialize(expr));
            }
        }
        else {
            // the identifier must be an Instance
            if (check(expr.getLeftParameter(), true, false, true, true, true)) {
                addError(expr, ValidationError.AX_ATOMIC_ERR + ":" 
                        + "\nThe identifier must be an Instance, " +
                        "not a " + error + "\n" + leSerializer.serialize(expr));
            }
            // the arguments must be Concepts
            if (check(expr.getRightParameter(), false, true, true, true, true)) {
                addError(expr, ValidationError.AX_ATOMIC_ERR + ":"
                        + "\nThe arguments must be Concepts, " +
                        "not " + error + "\n" + leSerializer.serialize(expr));
            }
        }
    }

    /**
     * Checks if a SubConceptMolecule is valid to wsml-core.
     *
     * @see org.deri.wsmo4j.validator.WsmlFlightExpressionValidator#visitSubConceptMolecule(org.omwg.logicalexpression.SubConceptMolecule)
     */
    public void visitSubConceptMolecule(SubConceptMolecule expr) {
        String error = ":\n Cannot contain a SubConceptMolecule:\n" 
            + leSerializer.serialize(expr);
        // A SubConceptMolecule is no valid right-hand side formula
        if (rhs) {      
            if (errorFlag) {
                addError(expr, ValidationError.AX_RHS_ERR + error);
            }
        }
        else if (lhs) {
            if (errorFlag) {
                addError(expr, ValidationError.AX_LHS_ERR + error);
            }
        }
        else {
            error = "";
            // the identifier must be a Concept
            if (check(expr.getLeftParameter(), false, true, true, true, true)) {
                addError(expr, ValidationError.AX_ATOMIC_ERR + ":"
                        + "\nThe identifier must be a Concept, " +
                        "not a " + error + "\n" + leSerializer.serialize(expr));
            }
            // the arguments must be Concepts
            if (check(expr.getRightParameter(), false, true, true, true, true)) {
                addError(expr, ValidationError.AX_ATOMIC_ERR + ":"
                        + "\nThe arguments must be Concepts, " +
                        "not " + error + "\n" + leSerializer.serialize(expr));
            }
        }
    }
  
    /**
     * Checks if a Negation is valid to wsml-core.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFlightExpressionValidator#visitNegation(org.omwg.logicalexpression.Negation)
     */
    public void visitNegation(Negation expr) {
        String error = ":\n Cannot contain a Negation:\n" 
            + leSerializer.serialize(expr);
        // A negation is no valid right-hand side formula
        if (rhs) {
            if (errorFlag) {
                addError(expr, ValidationError.AX_RHS_ERR + error);
            }
        }
        else if (lhs) {
            if (errorFlag) {
                addError(expr, ValidationError.AX_LHS_ERR + error);
            }
        }
        else {
            addError(expr, ValidationError.AX_FORMULA_ERR + ": A Negation is" +
                    " not a valid formula\n"  + leSerializer.serialize(expr));
        }
    }

    /**
     * Checks if a NegationAsFailure is valid to wsml-core.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFlightExpressionValidator#visitNegationAsFailure(org.omwg.logicalexpression.NegationAsFailure)
     */
    public void visitNegationAsFailure(NegationAsFailure expr) {
        String error = ":\n Cannot contain a NegationAsFailure:\n" 
            + leSerializer.serialize(expr);
        // A NegationAsFailure is no valid right-hand side formula
        if (rhs) {
            if (errorFlag) {
                addError(expr, ValidationError.AX_RHS_ERR + error);
            }
        }
        else if (lhs) {
            if (errorFlag) {
                addError(expr, ValidationError.AX_LHS_ERR + error);
            }
        }
        else {
            addError(expr, ValidationError.AX_FORMULA_ERR + ": A NegationAsFailure " +
                    "is not a valid formula\n"  + leSerializer.serialize(expr));
        }
    }

    /**
     * Checks if a Constraint is valid to wsml-core.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFlightExpressionValidator#visitConstraint(org.omwg.logicalexpression.Constraint)
     */
    public void visitConstraint(Constraint expr) {
        String error = ":\n Cannot contain a Constraint:\n" 
            + leSerializer.serialize(expr);
        // A Constraint is no valid right-hand side formula
        if (rhs) {
            if (errorFlag) {
                addError(expr, ValidationError.AX_RHS_ERR + error);
            }
        }
        else if (lhs) {
            if (errorFlag) {
                addError(expr, ValidationError.AX_LHS_ERR + error);
            }
        }
        else {
            addError(expr, ValidationError.AX_FORMULA_ERR + ": A Constraint is not " +
                    "a valid formula\n"  + leSerializer.serialize(expr));
        }
    }

    /**
     * Checks if a Conjunction is valid to wsml-core.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFlightExpressionValidator#visitConjunction(org.omwg.logicalexpression.Conjunction)
     */
    public void visitConjunction(Conjunction expr) {
        // Checks if a Conjunction is a valid right-hand side formula
        if (rhs || lhs) {
            expr.getLeftOperand().accept(this);
            expr.getRightOperand().accept(this);
        }
        else {
            if((expr.getLeftOperand() instanceof Conjunction) 
                    || (expr.getLeftOperand() instanceof AtomicExpression)){
                expr.getLeftOperand().accept(this); 
            }
            else {
                addError(expr, ValidationError.AX_FORMULA_ERR + ": Invalid " +
                        "Conjunction, all operands must be valid atomic " +
                        "formulae\n" + leSerializer.serialize(expr));
            }
            if ((expr.getRightOperand() instanceof Conjunction)
                    || (expr.getRightOperand() instanceof AtomicExpression)){
                expr.getRightOperand().accept(this);
            }
            else {
                addError(expr, ValidationError.AX_FORMULA_ERR + ": Invalid " +
                        "Conjunction, all operands must be valid atomic " +
                        "formulae\n" + leSerializer.serialize(expr));
            }
        }
    }

    /**
     * Checks if a Disjunction is valid to wsml-core.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFlightExpressionValidator#visitDisjunction(org.omwg.logicalexpression.Disjunction)
     */
    public void visitDisjunction(Disjunction expr) {
        // A Disjunction is no valid right-hand side formula
        if (rhs) {
            if (errorFlag) {
                addError(expr, ValidationError.AX_RHS_ERR + ":\n Cannot " +
                        "contain a Disjunction:\n" + leSerializer.serialize(expr));
            }
        }
        // Checks if a specified Disjunction is a valid left-hand side formula
        else if (lhs) {
            expr.getLeftOperand().accept(this);
            expr.getRightOperand().accept(this);
        }
        else {
            addError(expr, ValidationError.AX_FORMULA_ERR + ": A Disjunction is not " +
                    "a valid formula\n"  + leSerializer.serialize(expr));
        }
    }

    /**
     * Checks if an InverseImplication is valid to wsml-core.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFlightExpressionValidator#visitInverseImplication(org.omwg.logicalexpression.InverseImplication)
     */
    public void visitInverseImplication(InverseImplication expr) {
        String error = ":\n Cannot contain an InverseImplication:\n" 
            + leSerializer.serialize(expr);
        // An InverseImplication is no valid right-hand side formula
        if (rhs) {
            if (errorFlag) {
                addError(expr, ValidationError.AX_RHS_ERR + error);
            }
        }
        else if (lhs) {
            if (errorFlag) {
                addError(expr, ValidationError.AX_LHS_ERR + error);
            }
        }
        else {
            rhsVariables.clear();
            lhsVariables.clear();
            molecules.clear();
            LogicalExpression left = expr.getLeftOperand();    
            LogicalExpression right = expr.getRightOperand();

            // check on transitive attribute structure
            if (((left instanceof Atom) 
                   || (left instanceof AttributeValueMolecule)) && 
                   (right instanceof Conjunction)) {
               LogicalExpression leftConjunction = ((Conjunction) right).getLeftOperand();
               LogicalExpression rightConjunction = ((Conjunction) right).getRightOperand();
               if (((leftConjunction instanceof Atom) 
                       || (leftConjunction instanceof AttributeValueMolecule)) 
                       && ((rightConjunction instanceof Atom) 
                       || (rightConjunction instanceof AttributeValueMolecule))) {
                   isValidTransitiveInvImpl(expr);
               }
           }
           // check on symmetric, sub-attribute and inverse attribute structure
           else if (((left instanceof Atom) || (left instanceof AttributeValueMolecule)) && 
                   ((right instanceof Atom) || (right instanceof AttributeValueMolecule))) {
               isValidInverseImplication(expr);
           }
           else {
               // check on right-hand side impliedBy left-hand side structure
               errorFlag = false;
               rhs = true;  
               left.accept(this);
               rhs = false;
               lhs = true;
               right.accept(this);
               lhs = false;
               
               Iterator it = rhsVariables.iterator();
               while (it.hasNext()) {
                   if (!lhsVariables.contains(it.next())) {
                       addError(expr, ValidationError.AX_IMPL_BY_ERR + ": All " +
                               "variables in 'a' from 'a impliedBy b' " +
                               "must also be contained in 'b'\n" + leSerializer.serialize(expr));
                   }
               }
               if (lhsVariables.size() > 0) {
                   Graph graph = new Graph();
                   graph.isValidGraph(expr);
               }
           }
        }
    }

    /**
     * Checks if an Implication is valid to wsml-core.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFlightExpressionValidator#visitImplication(org.omwg.logicalexpression.Implication)
     */
    public void visitImplication(Implication expr) {
        String error = ":\n Cannot contain an Implication:\n" 
            + leSerializer.serialize(expr);
        // An Implication is no valid right-hand side formula
        if (rhs) {
            if (errorFlag) {
                addError(expr, ValidationError.AX_RHS_ERR + error);
            }
        }
        else if (lhs) {
            if (errorFlag) {
                addError(expr, ValidationError.AX_LHS_ERR + error);
            }
        }
        else {
            LogicalExpression left = expr.getLeftOperand();
            LogicalExpression right = expr.getRightOperand();
            InverseImplication invImp = leFactory.createInverseImplication(right, left);
            invImp.accept(this);
        }
    }

    /**
     * Checks if an Equivalence is valid to wsml-core.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFlightExpressionValidator#visitEquivalence(org.omwg.logicalexpression.Equivalence)
     */
    public void visitEquivalence(Equivalence expr) {
        String error = ":\n Cannot contain an Equivalence:\n" 
            + leSerializer.serialize(expr);
        // An Equivalence is no valid right-hand side formula
        if (rhs) {
            if (errorFlag) {
                addError(expr, ValidationError.AX_RHS_ERR + error);
            }
        }
        else if (lhs) {
            if (errorFlag) {
                addError(expr, ValidationError.AX_LHS_ERR + error);
            }
        }
        else {
            equivVariables.clear();
            rhs = true;
            expr.getLeftOperand().accept(this);
            expr.getRightOperand().accept(this);

            if (equivVariables.size() > 1) {
                addError(expr, ValidationError.AX_EQUIV_ERR + ": The Equivalence formula " +
                        "can only contain one variable\n" + leSerializer.serialize(expr));
                equivVariables.clear();
            }
            equivVariables.clear();
        }
    }

    /**
     * Checks if a LogicProgrammingRule is valid to wsml-core.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFlightExpressionValidator#visitLogicProgrammingRule(org.omwg.logicalexpression.LogicProgrammingRule)
     */
    public void visitLogicProgrammingRule(LogicProgrammingRule expr) {
        String error = ":\n Cannot contain a LogicProgrammingRule:\n" 
            + leSerializer.serialize(expr);
        // A LogicProgrammingRule is no valid right-hand side formula
        if (rhs) {
            if (errorFlag) {
                addError(expr, ValidationError.AX_RHS_ERR + error);
            }
        }
        else if (lhs) {
            if (errorFlag) {
                addError(expr, ValidationError.AX_LHS_ERR + error);
            }
        }
        else {
            addError(expr, ValidationError.AX_FORMULA_ERR + ": A LogicProgrammingRule" +
                    " is not a valid formula\n"  + leSerializer.serialize(expr));
        }
    }

    /**
     * Checks if a UniversalQuantification is valid to wsml-core.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFlightExpressionValidator#visitUniversalQuantification(org.omwg.logicalexpression.UniversalQuantification)
     */
    public void visitUniversalQuantification(UniversalQuantification expr) {
        String error = ":\n Cannot contain an UniversalQuantification:\n" 
            + leSerializer.serialize(expr);
        // An UniversalQuantification is no valid right-hand side formula
        if (rhs) {
            if (errorFlag) {
                addError(expr, ValidationError.AX_RHS_ERR + error);
            }
        }
        else if (lhs) {
            if (errorFlag) {
                addError(expr, ValidationError.AX_LHS_ERR + error);
            }
        }
        else {
            addError(expr, ValidationError.AX_FORMULA_ERR + ": A UniversalQuantification" +
                    " is not a valid formula\n"  + leSerializer.serialize(expr));
        }
    }

    /**
     * Checks if an ExistentialQuantification is valid to wsml-core.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFlightExpressionValidator#visitExistentialQuantification(org.omwg.logicalexpression.ExistentialQuantification)
     */
    public void visitExistentialQuantification(ExistentialQuantification expr) {
        String error = ":\n Cannot contain an ExistentialQuantification:\n" 
            + leSerializer.serialize(expr);
        // An ExistentialQuantification is no valid right-hand side formula
        if (rhs) {
            if (errorFlag) {
                addError(expr, ValidationError.AX_RHS_ERR + error);
            }
        }
        else if (lhs) {
            if (errorFlag) {
                addError(expr, ValidationError.AX_LHS_ERR + error);
            }
        }
        else {
            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n A ExistentialQuantification" +
                    " is not a valid formula\n"  + leSerializer.serialize(expr));
        }
    }
    
    /*
     * Gets an atom and transforms it into a value definition molecule
     */
    private Molecule atomToMolecule(Atom expr) {
        Molecule molecule = null;
        // the atom must be a binary atom
        if (expr.getArity() != 2) {
            addError(expr, ValidationError.AX_ATOMIC_ERR + ":\nThe atom must " +
                    "be a binary atom\n" + leSerializer.serialize(expr));
            return molecule;
        }
        else {
            molecule = leFactory.createAttributeValue(
                    expr.getParameter(0), expr.getIdentifier(), expr.getParameter(1));
            
            return molecule;
        }
    }
       
    /*
     * Checks if a specified InverseImplication, containing a molecule and a 
     * conjunction as operands, is a valid transitive construction
     */
    private void isValidTransitiveInvImpl(InverseImplication expr) {
        AttributeValueMolecule left = null;
        AttributeValueMolecule conLeft = null;
        AttributeValueMolecule conRight = null;
        LogicalExpression l = expr.getLeftOperand();
        LogicalExpression cLeft = ((Conjunction) expr.getRightOperand()).getLeftOperand();
        LogicalExpression cRight = ((Conjunction) expr.getRightOperand()).getRightOperand();
        if (l instanceof Atom) {
            left = (AttributeValueMolecule) atomToMolecule((Atom) l);
        }
        else {
            left = (AttributeValueMolecule) l;
        }
        if (cLeft instanceof Atom) {
            conLeft = (AttributeValueMolecule) atomToMolecule((Atom) cLeft);
        }
        else {
            conLeft = (AttributeValueMolecule) cLeft;
        }
        if (cRight instanceof Atom) {
            conRight = (AttributeValueMolecule) atomToMolecule((Atom) cRight);
        }
        else{
            conRight = (AttributeValueMolecule) cRight;
        }
        if (left == null || conLeft == null || conRight == null) {
            addError(expr, ValidationError.AX_IMPL_BY_ERR + ": Invalid atom\n" +
                    leSerializer.serialize(expr));
            return;
        }

        rhs = false;
        lhs = false;
        formula = true;
        left.accept(this);
        conLeft.accept(this);
        conRight.accept(this);
        
        if (left.getLeftParameter() instanceof Variable 
                && conLeft.getLeftParameter() instanceof Variable
                && conRight.getLeftParameter() instanceof Variable
                && left.getRightParameter() instanceof Variable
                && conLeft.getRightParameter() instanceof Variable
                && conRight.getRightParameter() instanceof Variable
                && left.getAttribute() instanceof Term
                && conLeft.getAttribute() instanceof Term
                && conRight.getAttribute() instanceof Term) {
            Variable leftTerm = (Variable) left.getLeftParameter();
            Variable conLeftTerm = (Variable) conLeft.getLeftParameter();
            Variable conRightTerm = (Variable) conRight.getLeftParameter();
            Term leftRel = left.getAttribute();
            Term conLeftRel = conLeft.getAttribute();
            Term conRightRel = conRight.getAttribute();     
            Variable leftArg = (Variable) left.getRightParameter();
            Variable conLeftArg = (Variable) conLeft.getRightParameter();
            Variable conRightArg = (Variable) conRight.getRightParameter();
            
            // check on globally transitive attribute/relation
            if (leftTerm.equals(conLeftTerm) && leftTerm.equals(conRightTerm)) {
                addError(expr, ValidationError.AX_IMPL_BY_ERR + ": The molecule's " +
                        "term must be contained in exactly one of the conjunction's " +
                        "molecule's terms\n" + leSerializer.serialize(expr));
            }
            else if (!(leftRel.equals(conLeftRel)) || !(leftRel.equals(conRightRel))) {
                addError(expr, ValidationError.AX_IMPL_BY_ERR + ": The molecule's " +
                        "attribute's names must be equal\n" + leSerializer.serialize(expr));
            }
            else if (leftTerm.equals(conLeftTerm)) {
                if (!(leftArg.equals(conRightArg)) || !(conLeftArg.equals(conRightTerm))) {
                    addError(expr, ValidationError.AX_IMPL_BY_ERR + ": No valid" +
                            " transitive construction\n" + leSerializer.serialize(expr));
                }
            }
            else if (leftTerm.equals(conRightTerm)) {
                if (!(leftArg.equals(conLeftArg)) || !(conRightArg.equals(conLeftTerm))) {
                    addError(expr, ValidationError.AX_IMPL_BY_ERR + ": No valid" +
                            " transitive construction\n" + leSerializer.serialize(expr));
                }
            }
        }
    }
    
    /*
     * Checks if an Inverse Implication, containing two molecules as Operands, 
     * is a valid construction. Valid constructions are symmetric, sub-attribute 
     * and inverse attribute structures
     */
    private void isValidInverseImplication(InverseImplication expr) {
        AttributeValueMolecule left = null;
        AttributeValueMolecule right = null;
        LogicalExpression l = expr.getLeftOperand();
        LogicalExpression r = expr.getRightOperand();
        if (l instanceof Atom) {
            left = (AttributeValueMolecule) atomToMolecule((Atom) l);
        }
        else {
            left = (AttributeValueMolecule) l;
        }
        if (r instanceof Atom) {
            right = (AttributeValueMolecule) atomToMolecule((Atom) r);
        }
        else {
            right = (AttributeValueMolecule) r;
        }
        if (left == null || right == null) {
            addError(expr, ValidationError.AX_IMPL_BY_ERR + ": Invalid atom" +
                    leSerializer.serialize(expr));
            return;
        }
        errorFlag = true;
        rhs = false;
        lhs = false;
        formula = true;
        left.accept(this);
        right.accept(this);
        
        if (left.getLeftParameter() instanceof Variable 
                && right.getLeftParameter() instanceof Variable
                && left.getRightParameter() instanceof Variable
                && right.getRightParameter() instanceof Variable
                && left.getAttribute() instanceof Term
                && right.getAttribute() instanceof Term) {
         
            Variable leftTerm = (Variable) left.getLeftParameter();
            Variable rightTerm = (Variable) right.getLeftParameter();
            Term leftRel = left.getAttribute();
            Term rightRel = right.getAttribute();       
            Variable leftArg = (Variable) left.getRightParameter();
            Variable rightArg = (Variable) right.getRightParameter();
                
            // check on globally symmetric and inverse attribute/relation
            if (!(leftTerm.equals(rightArg)) || !(leftArg.equals(rightTerm))) {
                
                // check on globally sub-attribute/relation
                if (!(leftTerm.equals(rightTerm)) || !(leftArg.equals(rightArg)) || leftRel.equals(rightRel)) {
                    addError(expr, ValidationError.AX_IMPL_BY_ERR + ": No valid" +
                            " symmetric, sub-attribute or inverse construction\n" + 
                            leSerializer.serialize(expr));
                } 
            }
        }
    }
    
    /*
     * Check if a specified Molecule's parameter or a term is used in the actual 
     * axiom as Instance, Relation or Datatype, or whether it is a Variable
     */
    private boolean check(Object o, boolean concept, boolean instance, 
            boolean relation, boolean datatype, boolean variable) {
        if (o instanceof Molecule) {
            if (concept && checkConcepts(((Molecule) o).getLeftParameter())) {
                error = "Concept";
                return true;
            }
            if (instance && checkInstances(((Molecule) o).getLeftParameter())) {
                error = "Instance";
                return true;
            }
            else if (relation && checkRelations(((Molecule) o).getLeftParameter())) {
                error = "Relation";
                return true;
            }
            else if (datatype && checkDatatypes(((Molecule) o).getLeftParameter())) {
                error = "Datatype";
                return true;
            }
            else if (variable && (((Molecule) o).getLeftParameter() instanceof Variable)) {
                error = "Variable";
                return true;
            }
        }
        else if (o instanceof Term) {
            if (concept && checkConcepts(o)) {
                error = "Concept";
            }
            if (instance && checkInstances(o)) {
                error = "Instance";
                return true;
            }
            else if (relation && checkRelations(o)) {
                error = "Relation";
                return true;
            }
            else if (datatype && checkDatatypes(o)) {
                error = "Datatype";
                return true;
            }
            else if (variable && (o instanceof Variable)) {
                error = "Variable";
                return true;
            }
        }
        return false;
    }
    
    /*
     * Checks if a specified Term is contained in the concepts vector
     */
    private boolean checkConcepts(Object o) {
        return validator.getIdConcepts().contains(o);
    }
    
    /*
     * Checks if a specified Term is contained in the instances vector
     */
    private boolean checkInstances(Object o) {
        return validator.getIdInstances().contains(o);
    }
    
    /*
     * Checks if a specified Term is contained in the relations vector
     */
    private boolean checkRelations(Object o) {
        return validator.getIdRelations().contains(o);
    }
    
    /*
     * Checks if a specified Term is contained in the abstract relations vector
     */
    private boolean checkAbstractRelations(Object o) {
        return validator.getIdAbstractRelations().contains(o);
    }
    
    /*
     * Checks if a specified Term is contained in the concrete relations vector
     */
    private boolean checkConcreteRelations(Object o) {
        return validator.getIdConcreteRelations().contains(o);
    }
    
    /*
     * Checks if a specified Term is contained in the datatypes vector
     */
    private boolean checkDatatypes(Object o) {
        return ConstantTransformer.getInstance().isDataType(o.toString());
    }

    public int getNumberOfMolecules() {
        return numberOfMolecules;
    }

    public Set getLHSVariables() {
        return lhsVariables;
    }
    
    /*
     * Adds a new ValidationError to the error list
     */
    private void addError(LogicalExpression logexp, String msg) {
        LogicalExpressionErrorImpl le = new LogicalExpressionErrorImpl(
                axiom, logexp, msg, WSML.WSML_CORE);
        if (!errors.contains(le)) {
            errors.add(le);
        }
    }
    
protected class Graph {
        
        private Map <Variable, List <Variable>> listMap = new HashMap <Variable, List <Variable>> ();
        private List <Variable> connected = new Vector <Variable> ();
        
        /*
         * Checks if the variables and molecules of a given inverse implication
         * build a valid connected and acyclic graph.
         */
        protected boolean isValidGraph(Binary expr) {      
            listMap.clear();
            connected.clear();
            
            buildLists();
        
            Iterator it = lhsVariables.iterator();
            Variable v1 = (Variable) it.next();
            Variable v = null;
            boolean valid = false;
            
            // check if the given graph is connected
            while(it.hasNext()) {
                v = (Variable) it.next();       
                valid = checkGraphConnected(v1, v);
                if (!valid) {
                    addError(expr, ValidationError.AX_GRAPH_ERR + ":\nGraph is " +
                            "not connected:\n" + leSerializer.serialize(expr));
                    return false;
                }
            }
            
            // check if the given graph is acyclic  
            List <Variable> checkList = new Vector <Variable> ();
            checkList.add(v1);
            valid = checkGraphAcyclic(v1, v1, checkList);
            if (valid) {
                return true;
            }
            else {
                addError(expr, ValidationError.AX_GRAPH_ERR + ":\nGraph contains " +
                        "cycle:\n" + leSerializer.serialize(expr));
                return false;
            }
        }
        
        /*
         * Build a Map with a specific variable as key and with a list as value, 
         * that contains all variables to which the key variable is connected.
         */
        private void buildLists() {
            Variable v1 = null;
            Variable v = null;
            
            Iterator it = lhsVariables.iterator();
            while (it.hasNext()) {
                List <Variable> list = new Vector <Variable> ();
                v1 = (Variable) it.next();
                Iterator itMol = molecules.iterator();
                while (itMol.hasNext()) {
                    List l = (List) itMol.next();
                    if (l.get(0).equals(v1)) {
                        v = (Variable) l.get(1);
                        list.add(v);
                    } 
                    else if (l.get(1).equals(v1)) {
                        v = (Variable) l.get(0);
                        list.add(v);
                    }  
                }
                listMap.put(v1, list);
            }
        }
        
        /*
         * Checks if variable v1 is connected to variable v2.
         */
        private boolean checkGraphConnected(Variable v1, Variable v2) {
            boolean found = false;
            boolean used = false;
            
            List <Variable> l = listMap.get(v1);
            
            if (l.contains(v2)) {
                return true;
            }
            else {
                connected.add(v1);
                Iterator it = l.iterator();
                while (it.hasNext()) {
                    Variable v = (Variable) it.next(); 
                    for (int i=0; i<connected.size(); i++) {
                        if (v.equals(connected.get(i))) {
                            used = true;
                        }
                    }
                    if (!used) {
                        found = checkGraphConnected(v, v2);
                    }
                    if (found) {
                        return true;
                    }
                    used = false;
                }
                return false;
            }
        }
           
        /*
         * Checks if a given graph contains a cycle. 
         */
        private boolean checkGraphAcyclic(Variable from, Variable v, List checkList) {
            if ((numberOfMolecules + 1) == lhsVariables.size()) {
                return true;
            }
            else {
                return false;
            }
        }
    }
    
}
