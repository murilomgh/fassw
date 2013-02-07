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

import org.deri.wsmo4j.io.serializer.wsml.*;
import org.deri.wsmo4j.logicalexpression.AttributeValueMoleculeImpl;
import org.deri.wsmo4j.logicalexpression.ConstantTransformer;
import org.omwg.logicalexpression.*;
import org.omwg.logicalexpression.Visitor;
import org.omwg.logicalexpression.terms.*;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.factory.*;
import org.wsmo.validator.ValidationError;


/**
 * Checks logical expressions for wsml-dl validity.
 *
 * @author nathalie.steinmetz@deri.org
 */
public class WsmlDLExpressionValidator
        implements Visitor {

    private WsmlDLValidator validator = null;
    
    private LogExprSerializerWSML leSerializer = null;
    
    private LogicalExpressionFactory leFactory = null;
    
    private Axiom axiom = null;
    
    private List <ValidationError> errors = null;
    
    private boolean formula;
    
    private boolean validDescription;
    
    private boolean quantifier;
    
    private boolean universalRight;
    
    private boolean existential;
    
    private boolean bMol;
    
    private String error;
    
    private Set <Term> variables;
    
    private Set <List <Term>> molecules;
    
    private Variable root;
    
    private int numberOfMolecules;
    
    private boolean checkGraph;
    
    private boolean checkForRoot;
    
    private boolean graph;
    
    private boolean noRoot;
    
    
    /**
     * @param axiom whose logical expression is checked
     * @param errors list that will be filled with error messages of found variant violations
     */
    protected WsmlDLExpressionValidator(Axiom axiom, LogicalExpressionFactory leFactory, List <ValidationError> errors, WsmlDLValidator validator) {
        this.validator = validator;
        leSerializer = validator.leSerializer;
        this.leFactory = leFactory;
        this.axiom = axiom;
        this.errors = errors;
        formula = false;
        validDescription = false;
        quantifier = false;
        universalRight = false;
        existential = false;
        bMol = false;
        graph = false;
        checkGraph = false;
        checkForRoot = false;
        noRoot = false;
        error = null;
        variables = new HashSet <Term>();
        molecules = new HashSet <List <Term>>();
        root = null;
        numberOfMolecules = 0;
    }
   
    protected void setup() {
        formula = false;
        quantifier = false;
        universalRight = false;
        existential = false;
        bMol = false;
        variables = new HashSet <Term>();
        root = null;
        noRoot = false;
        
        setGraph(false);
    }
    
    /**
     * Checks if an Atom is valid to wsml-dl.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitAtom(org.omwg.logicalexpression.Atom)
     */
    public void visitAtom(Atom expr) {
//        System.out.println("\nAtom in now: " + expr.toString() + existential);
        if (universalRight) {
        	quantifier = true;
            String error = ValidationError.AX_FORMULA_ERR + ":\n" +
            "The UniversalQuantifier must only contain strong" +
            " equation builtInAtoms on the right side of" +
            " implication:\n" + leSerializer.serialize(expr);
            if (!expr.getIdentifier().toString().equals(Constants.STRONG_EQUAL)) {
                     addError(expr, error);
                     validDescription = false;
            }
            universalRight = false;
        }
        else if (existential) {
        	quantifier = true;
            String error = ValidationError.AX_FORMULA_ERR + ":\n" +
            "The ExistentialQuantifier must only contain strong" +
            " equation builtInAtoms at negations:\n" +
            leSerializer.serialize(expr);
            if (!expr.getIdentifier().toString().equals(Constants.STRONG_EQUAL)) {
                     addError(expr, error);
                     validDescription = false;
            }
            existential = false;
        }
        if ((expr.getIdentifier().toString().equals(Constants.WSML_NAMESPACE + 
        				Constants.TRUE)) ||
        		(expr.getIdentifier().toString().equals(Constants.WSML_NAMESPACE + 
        				Constants.FALSE))) {
        	noRoot = true;
        }
        else if (expr instanceof BuiltInAtom && !quantifier) {
        	String error = ValidationError.AX_ATOMIC_ERR + ":\n" +
            "Built-in atoms are not allowed:\n" +
            leSerializer.serialize(expr);
        	addError(expr, error);
        }
        else if (!(expr.getIdentifier().toString().equals(Constants.NUMERIC_ADD)) && 
        		!(expr.getIdentifier().toString().equals(Constants.NUMERIC_DIV)) && 
        		!(expr.getIdentifier().toString().equals(Constants.NUMERIC_MUL)) && 
        		!(expr.getIdentifier().toString().equals(Constants.NUMERIC_SUB))) {  
            Molecule molecule = atomToMolecule(expr);
            if (molecule != null) {
                molecule.accept(this);
            }
        }
    }
    
    /**
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitAttributeContraintMolecule(org.omwg.logicalexpression.AttributeConstraintMolecule)
     */
    public void visitAttributeContraintMolecule(AttributeConstraintMolecule expr) {
//        System.out.println("\nAttributeConstraintMolecule in now: " + expr.toString());
        error = "";
        // check if the molecule is a valid b-molecule in a description
        if (formula) {
            isValidBMolecule(expr);
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
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitAttributeInferenceMolecule(org.omwg.logicalexpression.AttributeInferenceMolecule)
     */
    public void visitAttributeInferenceMolecule(AttributeInferenceMolecule expr) {
//        System.out.println("\nAttributeInferenceMolecule in now: " + expr.toString());
        error = "";
        // check if the molecule is a valid b-molecule in a description
        if (formula) {
            isValidBMolecule(expr);
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
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitAttributeValueMolecule(org.omwg.logicalexpression.AttributeValueMolecule)
     */
    public void visitAttributeValueMolecule(AttributeValueMolecule expr) {
//        System.out.println("\nAttributeValueMolecule in now: " + expr.toString());
        error = "";
        // check if the molecule is a valid b-molecule in a description
        if (formula) {
            isValidBMolecule(expr);
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
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitCompoundMolecule(org.omwg.logicalexpression.CompoundMolecule)
     */
    public void visitCompoundMolecule(CompoundMolecule expr) {
//        System.out.println("\nCompoundMolecule in now: " + expr.toString());
        Iterator i = expr.listOperands().iterator();
        while(i.hasNext()){
            ((Molecule)i.next()).accept(this);
        }
    }

    /**
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitMemberShipMolecule(org.omwg.logicalexpression.MembershipMolecule)
     */
    public void visitMemberShipMolecule(MembershipMolecule expr) {
//        System.out.println("\nMembershipMolecule in now: " + expr.toString());
        error = "";
        // checks if the molecule is a valid a-molecule in a description
        if (formula) {
            if (universalRight) {
                addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                        "The UniversalQuantifier may not contain a " +
                        "MembershipMolecule on the right side of" +
                        " implication:\n" + leSerializer.serialize(expr));
                validDescription = false;
                universalRight = false;
            }
            else {
                // the identifier must be a Variable
                if (!(expr.getLeftParameter() instanceof Variable)) {    
                    addError(expr, ValidationError.AX_FORMULA_ERR + ":" 
                            + "\nThe identifier of an a-molecule must be a Variable\n"
                            + leSerializer.serialize(expr));
                    validDescription = false;
                }
                // the argument must be a Concept
                if (check(expr.getRightParameter(), true, false, false, false, false)) {
                    addError(expr, ValidationError.AX_FORMULA_ERR + ":" 
                            + "\nThe argument of an a-molecule must be a Concept\n"
                            + leSerializer.serialize(expr));
                    validDescription = false;
                }
                if (graph && (!variables.contains(expr.getLeftParameter()))) {
//                	checkGraph = true;
                    variables.add(expr.getLeftParameter());
                }
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
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitSubConceptMolecule(org.omwg.logicalexpression.SubConceptMolecule)
     */
    public void visitSubConceptMolecule(SubConceptMolecule expr) {
//        System.out.println("\nSubConceptMolecule in now: " + expr.toString());
        if (universalRight) {
            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                    "The UniversalQuantifier may not contain a " +
                    "SubConceptMolecule on the right side of" +
                    " implication:\n" + leSerializer.serialize(expr));
            validDescription = false;
            universalRight = false;
        }
        else if (existential) {
            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                    "The ExistentialQuantifier may not contain a " +
                    "SubConceptMolecule:\n" + leSerializer.serialize(expr));
            validDescription = false;
            existential = false;
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
     * Checks if a Negation is valid to wsml-dl.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitNegation(org.omwg.logicalexpression.Negation)
     */
    public void visitNegation(Negation expr) {
//        System.out.println("\nNegation in now: " + expr.toString());
        // if the method is called from a formula
        if (formula) {
            if (universalRight || existential) {
                if (!(expr.getOperand() instanceof BuiltInAtom)) {
                    addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                            "The negation in a quantifier must contain a Built-In" +
                            " Atom:\n" + leSerializer.serialize(expr));
                    validDescription = false;
                    existential = false;
                    universalRight = false;
                    return;
                }
            }
            expr.getOperand().accept(this);
        }   
        else {
            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                 "Negation is not allowed:\n" + leSerializer.serialize(expr));
        }
    }

    /**
     * Checks if a NegationAsFailure is valid to wsml-dl.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitNegationAsFailure(org.omwg.logicalexpression.NegationAsFailure)
     */
    public void visitNegationAsFailure(NegationAsFailure expr) {
//        System.out.println("\nNegationAsFailure in now: " + expr.toString() + universalRight);
        if (universalRight) {
            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                    "The UniversalQuantifier may not contain a " +
                    "NegationAsFailure on the right side of" +
                    " implication:\n" + leSerializer.serialize(expr));
            validDescription = false;
            universalRight = false;
        }
        else if (existential) {
            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                    "The ExistentialQuantifier may not contain a " +
                    "NegationAsFailure:\n" + leSerializer.serialize(expr));
            validDescription = false;
            existential = false;
        }
        else if (existential) {
            expr.accept(this);
        }
        else {
            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                    "NegationAsFailure is not allowed:\n" + leSerializer.serialize(expr));
        }
    }

    /**
     * Checks if a Constraint is valid to wsml-dl.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitConstraint(org.omwg.logicalexpression.Constraint)
     */
    public void visitConstraint(Constraint expr) {
//        System.out.println("\nConstraint in now: " + expr.toString());
        if (universalRight) {
            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                    "The UniversalQuantifier may not contain a " +
                    "Constraint on the right side of" +
                    " implication:\n" + leSerializer.serialize(expr));
            validDescription = false;
            universalRight = false;
        }
        else if (existential) {
            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                    "The ExistentialQuantifier may not contain a " +
                    "Constraint:\n" + leSerializer.serialize(expr));
            validDescription = false;
            existential = false;
        }
        else {
            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                        "Constraint is not allowed:\n" + leSerializer.serialize(expr));
        }
    }

    /**
     * Checks if a Conjunction is valid to wsml-dl.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitConjunction(org.omwg.logicalexpression.Conjunction)
     */
    public void visitConjunction(Conjunction expr) {
//        System.out.println("existential: " + existential);
//        System.out.println("\nConjunction in now: " + expr.toString());
//        System.out.println("Left: " + expr.getLeftOperand().toString());
//        System.out.println("Right: " + expr.getRightOperand().toString());
        // if the method is called from a formula
        if (formula) {
            if (universalRight) {
                addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                        "The UniversalQuantifier may not contain a " +
                        "Conjunction on the right side of" +
                        " implication:\n" + leSerializer.serialize(expr));
                validDescription = false;
                universalRight = false;
            }
            else {
                if (existential && (((!(expr.getLeftOperand() instanceof Negation)) 
                        && (!(expr.getLeftOperand() instanceof Conjunction))
                        && (!(expr.getLeftOperand() instanceof AttributeMolecule))
                        && (!(expr.getLeftOperand() instanceof MembershipMolecule))) 
                        || ((!(expr.getRightOperand() instanceof Negation)) 
                        && (!(expr.getRightOperand() instanceof Conjunction))
                        && (!(expr.getRightOperand() instanceof AttributeMolecule))
                        && (!(expr.getRightOperand() instanceof MembershipMolecule))))){
                    addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                            "The Conjunction in an ExistentialQuantifier must contain " +
                            "a Negation or another Conjunction:\n" + leSerializer.serialize(expr));
                    validDescription = false;
                    existential = false;
                    return;
                }
                else {
                    expr.getLeftOperand().accept(this);
                    expr.getRightOperand().accept(this);
                }
            }
        }
        else {
            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                    "Conjunction is not allowed:\n" + leSerializer.serialize(expr));
        }  
    }

    /**
     * Checks if a Disjunction is valid to wsml-dl.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitDisjunction(org.omwg.logicalexpression.Disjunction)
     */
    public void visitDisjunction(Disjunction expr) {
//        System.out.println("\n Disjunction in now: " + expr.toString() + universalRight);
        // if the method is called from a formula
        if (formula) {
            if (universalRight) {
                if (((!(expr.getLeftOperand() instanceof Negation)) 
                        && (!(expr.getLeftOperand() instanceof Disjunction))) 
                        || ((!(expr.getRightOperand() instanceof Negation)) 
                        && (!(expr.getRightOperand() instanceof Disjunction)))){
                    addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                            "The Disjunction in a UniversalQuantifier must contain " +
                            "a Negation or another Disjunction:\n" + leSerializer.serialize(expr));
                    validDescription = false;
                    universalRight = false;
                    return;
                }
            }
            expr.getLeftOperand().accept(this);
            expr.getRightOperand().accept(this);
        }
        else {
            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                    "Disjunction is not allowed:\n" + leSerializer.serialize(expr));
        }  
    }

    /**
     * Checks if an InverseImplication is valid to wsml-dl.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitInverseImplication(org.omwg.logicalexpression.InverseImplication)
     */
    public void visitInverseImplication(InverseImplication expr) {
//        System.out.println("\nInverseImplication in now: " + expr.toString());
        if (universalRight) {
            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                    "The UniversalQuantifier may not contain a " +
                    "InverseImplication on the right side of" +
                    " implication:\n" + leSerializer.serialize(expr));
            validDescription = false;
            universalRight = false;
        }
        else if (existential) {
            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                    "The ExistentialQuantifier may not contain a " +
                    "InverseImplication:\n" + leSerializer.serialize(expr));
            validDescription = false;
            existential = false;
        }
        else {
        	if (formula == true) {
        		addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                        "Nested Implications are not allowed:\n" + 
                        leSerializer.serialize(expr));
                validDescription = false;
        	}
        	else {
	            formula = true;
	            validDescription = true;
	            
	            expr.getLeftOperand().accept(this);
	            expr.getRightOperand().accept(this);
        	}
        	
        	LogicalExpression left = expr.getLeftOperand();    
            LogicalExpression right = expr.getRightOperand();      	
            left.accept(this);    
            right.accept(this);
            if (!validDescription) {
                addError(expr, ValidationError.AX_IMPL_BY_ERR + ":\n" 
                        + leSerializer.serialize(expr));
                return;
            }
            
            // check on transitive attribute structure
            if (((left instanceof Atom) || (left instanceof AttributeValueMolecule)) && 
                    (right instanceof Conjunction)) {
                LogicalExpression leftConjunction = ((Conjunction) right).getLeftOperand();
                LogicalExpression rightConjunction = ((Conjunction) right).getRightOperand();
                if (((leftConjunction instanceof Atom) 
                        || (leftConjunction instanceof AttributeValueMolecule)) 
                        && ((rightConjunction instanceof Atom) 
                        || (rightConjunction instanceof AttributeValueMolecule))) {
                    if (isValidTransitiveInvImpl(expr)) {
                    	validDescription = false;
                    }
                    return;
                }
            }
            
            // check on symmetric, sub-attribute and inverse attribute structure
            if (((left instanceof Atom) || (left instanceof AttributeValueMolecule)) && 
                    ((right instanceof Atom) || (right instanceof AttributeValueMolecule))) {
                if (isValidInverseImplication(expr)) {
                	validDescription = false;
                }
                return;
            }
            
            if (validDescription) {
//                existential = false;
            	
            	checkForRoot = true;
            	new Graph(expr, this);
            	checkForRoot = false;
        
            	new Graph(expr.getLeftOperand(), this);
            	new Graph(expr.getRightOperand(), this);

            }
        }
    }

    /**
     * Checks if an Implication is valid to wsml-dl.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitImplication(org.omwg.logicalexpression.Implication)
     */
    public void visitImplication(Implication expr) {
//        System.out.println("\nImplication in now: " + expr.toString());
        if (universalRight) {
            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                    "The UniversalQuantifier may not contain a " +
                    "Implication on the right side of" +
                    " implication:\n" + leSerializer.serialize(expr));
            validDescription = false;
            universalRight = false;
        }
        else if (existential) {
            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                    "The ExistentialQuantifier may not contain a " +
                    "Implication:\n" + leSerializer.serialize(expr));
            validDescription = false;
            existential = false;
        }
        else {
        	if (formula == true) {
        		addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                        "Nested Implications are not allowed:\n" + 
                        leSerializer.serialize(expr));
                validDescription = false;
        	}
        	else {
        		formula = true;
	            validDescription = true;
	
	            expr.getLeftOperand().accept(this);
	            expr.getRightOperand().accept(this);
        	}
        	
            if (!validDescription) {
                addError(expr, ValidationError.AX_IMP_ERR + ":\n" +
                        leSerializer.serialize(expr));
            }
            else {
//                existential = false;
            	
            	checkForRoot = true;
            	new Graph(expr, this);
            	checkForRoot = false;
        
            	new Graph(expr.getLeftOperand(), this);
            	new Graph(expr.getRightOperand(), this);
            }
        }
    }

    /**
     * Checks if an Equivalence is valid to wsml-dl.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitEquivalence(org.omwg.logicalexpression.Equivalence)
     */
    public void visitEquivalence(Equivalence expr) {
//        System.out.println("\nEquivalence in now: " + expr.toString());
        if (universalRight) {
            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                    "The UniversalQuantifier may not contain a " +
                    "Equivalence on the right side of" +
                    " implication:\n" + leSerializer.serialize(expr));
            validDescription = false;
            universalRight = false;
        }
        else if (existential) {
            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                    "The ExistentialQuantifier may not contain a " +
                    "Equivalence:\n" + leSerializer.serialize(expr));
            validDescription = false;
            existential = false;
        }
        else {
        	if (formula == true) {
        		addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                        "Nested Implications are not allowed:\n" + 
                        leSerializer.serialize(expr));
                validDescription = false;
                existential = false;
        	}
        	else {
	            formula = true;
	            validDescription = true;
	            expr.getLeftOperand().accept(this);
	            expr.getRightOperand().accept(this); 
        	}
        	
            if (!validDescription) {
                addError(expr, ValidationError.AX_EQUIV_ERR + ":\n" +
                        "Invalid Equivalence Formula:\n" +
                        leSerializer.serialize(expr));
            }
            else {
//                existential = false;

            	checkForRoot = true;
            	new Graph(expr, this);
            	checkForRoot = false;
            	
            	new Graph(expr.getLeftOperand(), this);
            	new Graph(expr.getRightOperand(), this);
            }
        }
    }

    /**
     * Checks if a LogicProgrammingRule is valid to wsml-dl.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitLogicProgrammingRule(org.omwg.logicalexpression.LogicProgrammingRule)
     */
    public void visitLogicProgrammingRule(LogicProgrammingRule expr) {
//        System.out.println("\nLogicProgrammingRule in now: " + expr.toString());
        if (universalRight) {
            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                    "The UniversalQuantifier may not contain a " +
                    "LogicProgrammingRule on the right side of" +
                    " implication:\n" + leSerializer.serialize(expr));
            validDescription = false;
            universalRight = false;
        }
        else if (existential) {
            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                    "The ExistentialQuantifier may not contain a " +
                    "LogicProgrammingRule:\n" + leSerializer.serialize(expr));
            validDescription = false;
            existential = false;
        }
        else {
            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                        "LogicProgrammingRule is not allowed:\n" + leSerializer.serialize(expr));
        }
    }

    /**
     * Checks if a UniversalQuantification is valid to wsml-dl.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitUniversalQuantification(org.omwg.logicalexpression.UniversalQuantification)
     */
    public void visitUniversalQuantification(UniversalQuantification expr) {
//        System.out.println("\nUniversalQuantification in now: " + expr.toString());
        if (universalRight) {
            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                    "The UniversalQuantifier may not contain a " +
                    "UniversalQuantification on the right side of" +
                    " implication:\n" + leSerializer.serialize(expr));
            validDescription = false;
            universalRight = false;
        }
        else {
            // if the method is called from a formula
            if (formula) {
                Vector operands = (Vector) expr.listOperands();
                if ((operands.size() == 1) && (operands.get(0) instanceof Implication)) {
                    Implication imp = (Implication) operands.get(0);
                    if (expr.listVariables().size() == 1) {
                        isValidBMolecule(imp.getLeftOperand());
                        imp.getRightOperand().accept(this);    
                    }
                    else {
                        if ((imp.getLeftOperand() instanceof Conjunction) 
                                || (imp.getLeftOperand() instanceof Negation)
                                || (imp.getLeftOperand() instanceof AttributeMolecule)
                                || (imp.getLeftOperand() instanceof MembershipMolecule)) {
                            imp.getLeftOperand().accept(this);  
                        }
                        else {
                            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                                    "The UniversalQuantifier must have a Molecule " +
                                    "or a Conjunction on the left side of implication:\n" +
                                    leSerializer.serialize(expr));
                            validDescription = false;
                            return;
                        }
                        if ((imp.getRightOperand() instanceof Disjunction)
                                || (imp.getRightOperand() instanceof Negation)
                                || (imp.getRightOperand() instanceof BuiltInAtom)) {
                            universalRight = true;
                            imp.getRightOperand().accept(this);    
                        }
                        else {
                            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                                    "The UniversalQuantifier must have a Negation, a Strong Equality " +
                                    "BuiltInAtom or a Disjunction on the right side of implication:\n" +
                                    leSerializer.serialize(expr));
                            validDescription = false;
                            return;
                        }
                    }
                }
                else {
                    addError (expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                            "UniversalQuantification is only allowed with an " +
                            "implication as operand:\n" + leSerializer.serialize(expr));
                    validDescription = false;
                }
                if (graph) {
                    checkGraph = true;
                    Set <Variable> vs = expr.listVariables();
                    Iterator it = vs.iterator();
                    while (it.hasNext()) {
                        Variable v = (Variable) it.next();
                        if (!variables.contains(v)) {
                            variables.add(v);
                        }  
                    }
                }
            }
            else {
                addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                        "UniversalQuantification is not allowed:\n" + leSerializer.serialize(expr));
                validDescription = false;
            }
        }
    }
    
    /**
     * Checks if an ExistentialQuantification is valid to wsml-dl.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullExpressionValidator#visitExistentialQuantification(org.omwg.logicalexpression.ExistentialQuantification)
     */
    public void visitExistentialQuantification(ExistentialQuantification expr) {
//        System.out.println("\nExistentialQuantification in now: " + expr.toString());
        if (universalRight) {
            addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                    "The UniversalQuantifier may not contain a " +
                    "ExistentialQuantification on the right side of" +
                    " implication:\n" + leSerializer.serialize(expr));
            validDescription = false;
            universalRight = false;
        }
        else {
            //  if the method is called from a formula
            if (formula) {
                Vector operands = (Vector) expr.listOperands();
                int size = operands.size();
                for (int i=0; i<size; i++) {
                    if (!(operands.get(i) instanceof Conjunction) && 
                    		!(operands.get(i) instanceof AttributeValueMoleculeImpl)) {
                        addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                                "ExistentialQuantifier is only allowed with " +
                                "conjunctions as operands:\n" + leSerializer.serialize(expr));
                        validDescription = false;
                        return;
                    }
                }
                if (expr.listVariables().size() == 1) {
//                    isValidBMolecule(((Conjunction) operands.get(0)).getLeftOperand());
                	bMol = false;
                	if (operands.get(0) instanceof Conjunction) {
                		((Conjunction) operands.get(0)).getLeftOperand().accept(this);
                	}
                	else {
                		((LogicalExpression) operands.get(0)).accept(this);
                	}
                    if (!graph && !bMol) {
                    	addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                                "ExistentialQuantifier is only allowed with " +
                                "an AttributeValueMolecule as left operand of a conjunction:\n" + 
                                leSerializer.serialize(expr));
                    }
                    if (operands.get(0) instanceof Conjunction) {
                    	((Conjunction) operands.get(0)).getRightOperand().accept(this);
                    }
                }  
                else {
                    existential = true;
                    for (int i=0; i<size; i++) {
                        ((Conjunction) operands.get(i)).getLeftOperand().accept(this);
                        ((Conjunction) operands.get(i)).getRightOperand().accept(this);
                    }
                }
                if (graph) {
                    checkGraph = true;
                    Set <Variable> vs = expr.listVariables();
                    Iterator it = vs.iterator();
                    while (it.hasNext()) {
                        Variable v = (Variable) it.next();
                        if (!variables.contains(v)) {
                            variables.add(v);
                        }  
                    }
                }
            }
            else {
                addError(expr, ValidationError.AX_FORMULA_ERR + ":\n" +
                        "ExistentialQuantifier is not allowed:\n" + leSerializer.serialize(expr));
                validDescription = false;
            }  
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
     * Checks if a specified molecule is a valid b-molecule
     */
    private void isValidBMolecule(LogicalExpression le) {
        if (universalRight) {
            addError(le, ValidationError.AX_FORMULA_ERR + ":\n" +
                    "The UniversalQuantifier may not contain a " +
                    "Molecule on the right side of" +
                    " implication:\n" + leSerializer.serialize(le));
            validDescription = false;
            universalRight = false;
        }
        else {
            AttributeValueMolecule expr = null;
            if (le instanceof Atom) {
                le = atomToMolecule((Atom) le);
                if (le == null) {
                    validDescription = false;
                }
            }
            if (le instanceof AttributeValueMolecule) {
                expr = (AttributeValueMolecule) le;
                
                // the argument of the attribute value molecule must be a Variable
                if (!(expr.getLeftParameter() instanceof Variable)) {    
                    addError(expr, ValidationError.AX_FORMULA_ERR + ":" 
                            + "\nThe argument must be a Variable\n"
                            + leSerializer.serialize(expr));
                    validDescription = false;
                } 
                // the name of the attribute must be a relation
                if (check(expr.getAttribute(), true, true, false, true, true)) {
                    addError(expr, ValidationError.AX_FORMULA_ERR + ": The " +
                            "attribute name must be a relation\n" + leSerializer.serialize(expr));
                    validDescription = false;  
                }
                // the arguments of the attribute must be variables
                if (!(expr.getRightParameter() instanceof Variable)) {
                    addError(expr, ValidationError.AX_FORMULA_ERR + ": The " +
                            "attribute's arguments must be variables\n" + leSerializer.serialize(expr));
                    validDescription = false;                
                }
                // put the variables and molecules into Sets to be able to check if the expression is a valid graph
                if (graph) {
                    checkGraph = true;
                    if (!variables.contains(expr.getLeftParameter())) {
                        variables.add(expr.getLeftParameter());
                    }
                    if (!variables.contains(expr.getRightParameter())) {
                        variables.add(expr.getRightParameter());
                    }
                    List <Term> list = new Vector <Term> ();
                    list.add(expr.getLeftParameter());
                    if (!expr.getLeftParameter().equals(expr.getRightParameter())) {
                        list.add(expr.getRightParameter());
                    }
                    Iterator it = molecules.iterator();
                    while (it.hasNext()) {
                        List l = (List) it.next();
                        if ((l.get(0).equals(expr.getLeftParameter())) && 
                                (l.get(1).equals(expr.getRightParameter()))) {
                            return;
                        }
                    }
                    numberOfMolecules = numberOfMolecules + 1;
                    molecules.add(list);
                }
                bMol = true;
            }
            else {
                addError(le, ValidationError.AX_FORMULA_ERR + ": This b-molecule" +
                        " must be an atom or an AttributeValueMolecule" +
                        leSerializer.serialize(le));
                validDescription = false;
            }
        }
    }
    
    /*
     * Checks if a specified InverseImplication, containing a molecule and a 
     * conjunction as operands, is a valid transitive construction
     */
    private boolean isValidTransitiveInvImpl(InverseImplication expr) {
        AttributeValueMolecule left, conjunctionLeft, conjunctionRight;
        
        // Atoms can be used interchangeably with attribute value molecules
        if (expr.getLeftOperand() instanceof Atom) {
            left = (AttributeValueMolecule) atomToMolecule((Atom) expr.getLeftOperand());
        }
        else {
            left = (AttributeValueMolecule) expr.getLeftOperand();
        }
        if (((Conjunction) expr.getRightOperand()).getLeftOperand() instanceof Atom) {
            conjunctionLeft = (AttributeValueMolecule) atomToMolecule((Atom) 
                    ((Conjunction) expr.getRightOperand()).getLeftOperand());
        }
        else {
            conjunctionLeft = (AttributeValueMolecule) (
                    (Conjunction) expr.getRightOperand()).getLeftOperand();
        }
        if (((Conjunction) expr.getRightOperand()).getRightOperand() instanceof Atom) {
            conjunctionRight = (AttributeValueMolecule) atomToMolecule((Atom) 
                    ((Conjunction) expr.getRightOperand()).getRightOperand());
        }
        else {
            conjunctionRight = (AttributeValueMolecule) ((Conjunction) expr.getRightOperand()).getRightOperand();
        } 
              
        Variable leftVariable = (Variable) left.getLeftParameter();
        Variable conjunctionLeftVariable = (Variable) conjunctionLeft.getLeftParameter();
        Variable conjunctionRightVariable = (Variable) conjunctionRight.getLeftParameter();
        Term leftRelation = left.getAttribute();
        Term conjunctionLeftRelation = conjunctionLeft.getAttribute();
        Term conjunctionRightRelation = conjunctionRight.getAttribute();     
        Variable leftArgument = (Variable) left.getRightParameter();
        Variable conjunctionLeftArgument = (Variable) conjunctionLeft.getRightParameter();
        Variable conjunctionRightArgument = (Variable) conjunctionRight.getRightParameter();
        
        // check on globally transitive attribute/relation
        if (leftVariable.equals(conjunctionLeftVariable) 
                && leftVariable.equals(conjunctionRightVariable)) {
            return false;
        }
        else if (!(leftRelation.equals(conjunctionLeftRelation)) 
                || !(leftRelation.equals(conjunctionRightRelation))) {
        	return false;
        }
        else if (leftVariable.equals(conjunctionLeftVariable)) {
            if (!(leftArgument.equals(conjunctionRightArgument)) 
                    || !(conjunctionLeftArgument.equals(conjunctionRightVariable))) {
            	return false;
            }
        }
        else if (leftVariable.equals(conjunctionRightVariable)) {
            if (!(leftArgument.equals(conjunctionLeftArgument)) 
                    || !(conjunctionRightArgument.equals(conjunctionLeftVariable))) {
            	return false;
            }
        }
        return true;
    }
    
    /*
     * Checks if an Inverse Implication, containing two molecules as Operands, 
     * is a valid construction. Valid constructions are symmetric, sub-attribute 
     * and inverse attribute structures
     */
    private boolean isValidInverseImplication(InverseImplication expr) {
        AttributeValueMolecule left = null;
        AttributeValueMolecule right = null;
        
        // Atoms can be used interchangeably with attribute value molecules
        if (expr.getLeftOperand() instanceof Atom) {
            left = (AttributeValueMolecule) atomToMolecule((Atom) expr.getLeftOperand());
        }
        else {
            left = (AttributeValueMolecule) expr.getLeftOperand();
        }
        if (expr.getRightOperand() instanceof Atom) {
            right = (AttributeValueMolecule) atomToMolecule((Atom) expr.getRightOperand());
        }
        else {
            right = (AttributeValueMolecule) expr.getRightOperand();
        }
        if (left == null || right == null) {
            return false;
        }
        else {
            Variable leftVariable = (Variable) left.getLeftParameter();
            Variable rightVariable = (Variable) right.getLeftParameter();
            Term leftRelation = left.getAttribute();
            Term rightRelation = right.getAttribute();       
            Variable leftArgument = (Variable) left.getRightParameter();
            Variable rightArgument = (Variable) right.getRightParameter();
            
            // check on globally symmetric and inverse attribute/relation
            if (!(leftVariable.equals(rightArgument)) || !(leftArgument.equals(rightVariable))) {
                
                // check on globally sub-attribute/relation
                if (!(leftVariable.equals(rightVariable)) || !(leftArgument.equals(rightArgument)) 
                        || leftRelation.equals(rightRelation)) {
                    return false;
                }
            }
            return true;
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
    
    public Set getMolecules() {
        return molecules;
    }

    public int getNumberOfMolecules() {
        return numberOfMolecules;
    }

    public Set getVariables() {
        return variables;
    }
    
    public Variable getRoot() {
    	return root;
    }
    
    protected void setGraph(boolean graph) {
        this.graph = graph;
        checkGraph = false;
        variables = new HashSet <Term> ();
        molecules = new HashSet <List <Term>> ();
        numberOfMolecules = 0;
    }

    /*
     * Adds a new ValidationError to the error list
     */
    private void addError(LogicalExpression logexp, String msg) {
        LogicalExpressionErrorImpl le = new LogicalExpressionErrorImpl(
                axiom, logexp, msg, WSML.WSML_DL);
        if (!errors.contains(le)) {
            errors.add(le);
        }
    }
   
    protected class Graph {
      
        private WsmlDLExpressionValidator exprVal;
        private Map <Variable, List <Term>> listMap = new HashMap <Variable, List <Term>> ();
        private List <Variable> connected = new Vector <Variable> ();
        private LogicalExpression expr;
        private Set <Term> leftVariables = new HashSet <Term> ();
        private Set <Term> rightVariables = new HashSet <Term> ();
        
        public Graph(LogicalExpression expr, WsmlDLExpressionValidator exprVal) {
            this.expr = expr;
            this.exprVal = exprVal;
            if (checkForRoot) {
            	root = null;
            	leftVariables.clear();
            	rightVariables.clear();
            	exprVal.setGraph(true);
            	hasRootVariable();
            }
            else if (!quantifier){ 	
            	exprVal.setGraph(true);
            	isValidGraph();
            }
        }
        
        /*
         * Checks if the left and right parts of a given logical expression 
         * contain a root variable.
         */
        public boolean hasRootVariable() {
        	((Binary) expr).getLeftOperand().accept(exprVal);
        	Iterator <Term> it = variables.iterator();
        	while (it.hasNext()) {
        		leftVariables.add(it.next());
        	}
        	variables.clear();       	
        	((Binary) expr).getRightOperand().accept(exprVal);
        	it = variables.iterator();
        	while (it.hasNext()) {
        		rightVariables.add(it.next());
        	}       	
        	variables.clear();
        	Iterator leftIt = leftVariables.iterator();
        	while (leftIt.hasNext()) {
        		Variable v = (Variable) leftIt.next();
        		if (rightVariables.contains(v)) {
        			root = v;
        		}
        	} 
        	if (root == null && !noRoot) {
        		addError(expr, ValidationError.AX_GRAPH_ERR + ":\nLeft and " +
        				"right part of expression do not have a common root " +
                        "variable:\n" + leSerializer.serialize(expr));
                return false;
        	}
        	return true;
        }
        
        /*
         * Checks if the variables and molecules of a given logical expression
         * build a valid connected and acyclic graph.
         */
        protected boolean isValidGraph() {  
            listMap.clear();
            connected.clear();

            // fill the sets with the variables and the molecules
            expr.accept(exprVal);
            if ((!variables.isEmpty()) && checkGraph) {
                buildLists();
            
                Iterator it = variables.iterator();
                Variable v1 = (Variable) it.next();
                Variable v = null;
                boolean valid = false;
                
                // check if the given graph is connected
                while(it.hasNext()) {
                    v = (Variable) it.next();  
                    connected.clear();
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
            return true;
        }
        
        /*
         * Build a Map with a specific variable as key and with a list as value, 
         * that contains all variables to which the key variable is connected.
         */
        private void buildLists() {
            Variable v1 = null;
            Variable v = null;
            
            Iterator <Term> it = variables.iterator();
            while (it.hasNext()) {
                List <Term> list = new Vector <Term> ();
                v1 = (Variable) it.next();
                Iterator <List <Term>> itMol = molecules.iterator();
                while (itMol.hasNext()) {
                    List <Term> l = itMol.next();
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
            
            List <Term> l = listMap.get(v1);
            
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
            if ((numberOfMolecules + 1) == variables.size()) {
                return true;
            }
            else {
                return false;
            }
        }
    }
}
