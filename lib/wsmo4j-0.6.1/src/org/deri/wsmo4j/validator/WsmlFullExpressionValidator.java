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

import org.omwg.logicalexpression.*;
import org.omwg.ontology.*;


/**
 * Checks logical expressions for wsml-full validity.
 * 
 * @author nathalie.steinmetz@deri.org
 */
public class WsmlFullExpressionValidator
        implements Visitor {

    protected Axiom axiom = null;

    protected List errors = null;

    /**
     * @param axiom whose logical expression is checked
     * @param errors list that will be filled with error messages of found variant violations
     */
    protected WsmlFullExpressionValidator(Axiom axiom, List errors) {
        this.axiom = axiom;
        this.errors = errors;
    }

    /**
     * Checks if an Atom is valid to wsml-full.
     * 
     * @see org.omwg.logicalexpression.Visitor#visitAtom(org.omwg.logicalexpression.Atom)
     */
    public void visitAtom(Atom expr) {

    }

    /**
     * Checks if an AttributeConstraintMolecule is valid to wsml-full.
     * 
     * @see org.omwg.logicalexpression.Visitor#visitAttributeContraintMolecule(org.omwg.logicalexpression.AttributeConstraintMolecule)
     */
    public void visitAttributeContraintMolecule(AttributeConstraintMolecule expr) {
        
    }

    /**
     * Checks if an AttributeInferenceMolecule is valid to wsml-full.
     * 
     * @see org.omwg.logicalexpression.Visitor#visitAttributeInferenceMolecule(org.omwg.logicalexpression.AttributeInferenceMolecule)
     */
    public void visitAttributeInferenceMolecule(AttributeInferenceMolecule expr) {
        
    }

    /**
     * Checks if an AttributeValueMolecule is valid to wsml-full.
     *     
     * @see org.omwg.logicalexpression.Visitor#visitAttributeValueMolecule(org.omwg.logicalexpression.AttributeValueMolecule)
     */
    public void visitAttributeValueMolecule(AttributeValueMolecule expr) {
        
    }

    /**
     * Checks if a CompoundMolecule is valid to wsml-full.
     * 
     * @see org.omwg.logicalexpression.Visitor#visitCompoundMolecule(org.omwg.logicalexpression.CompoundMolecule)
     */
    public void visitCompoundMolecule(CompoundMolecule expr) {
        
    }

    /**
     * Checks if a MembershipMolecule is valid to wsml-full.
     * 
     * @see org.omwg.logicalexpression.Visitor#visitMemberShipMolecule(org.omwg.logicalexpression.MembershipMolecule)
     */
    public void visitMemberShipMolecule(MembershipMolecule expr) {
        
    }

    /**
     * Checks if a SubConceptMolecule is valid to wsml-full.
     * 
     * @see org.omwg.logicalexpression.Visitor#visitSubConceptMolecule(org.omwg.logicalexpression.SubConceptMolecule)
     */
    public void visitSubConceptMolecule(SubConceptMolecule expr) {
        
    }
    
    
    /**
     * Checks if a Negation is valid to wsml-full.
     * 
     * @see org.omwg.logicalexpression.Visitor#visitNegation(org.omwg.logicalexpression.Negation)
     */
    public void visitNegation(Negation expr) {

    }

    /**
     * Checks if a NegationAsFailure is valid to wsml-full.
     * 
     * @see org.omwg.logicalexpression.Visitor#visitNegationAsFailure(org.omwg.logicalexpression.NegationAsFailure)
     */
    public void visitNegationAsFailure(NegationAsFailure expr) {

    }

    /**
     * Checks if a Constraint is valid to wsml-full.
     * 
     * @see org.omwg.logicalexpression.Visitor#visitConstraint(org.omwg.logicalexpression.Constraint)
     */
    public void visitConstraint(Constraint expr) {

    }

    /**
     * Checks if a Conjunction is valid to wsml-full.
     * 
     * @see org.omwg.logicalexpression.Visitor#visitConjunction(org.omwg.logicalexpression.Conjunction)
     */
    public void visitConjunction(Conjunction expr) {

    }

    /**
     * Checks if a Disjunction is valid to wsml-full.
     * 
     * @see org.omwg.logicalexpression.Visitor#visitDisjunction(org.omwg.logicalexpression.Disjunction)
     */
    public void visitDisjunction(Disjunction expr) {

    }

    /**
     * Checks if an InverseImplication is valid to wsml-full.
     * 
     * @see org.omwg.logicalexpression.Visitor#visitInverseImplication(org.omwg.logicalexpression.InverseImplication)
     */
    public void visitInverseImplication(InverseImplication expr) {

    }

    /**
     * Checks if an Implication is valid to wsml-full.
     * 
     * @see org.omwg.logicalexpression.Visitor#visitImplication(org.omwg.logicalexpression.Implication)
     */
    public void visitImplication(Implication expr) {

    }

    /**
     * Checks if an Equivalence is valid to wsml-full.
     * 
     * @see org.omwg.logicalexpression.Visitor#visitEquivalence(org.omwg.logicalexpression.Equivalence)
     */
    public void visitEquivalence(Equivalence expr) {

    }

    /**
     * Checks if a LogicProgrammingRule is valid to wsml-full.
     * 
     * @see org.omwg.logicalexpression.Visitor#visitLogicProgrammingRule(org.omwg.logicalexpression.LogicProgrammingRule)
     */
    public void visitLogicProgrammingRule(LogicProgrammingRule expr) {

    }

    /**
     * Checks if a UniversalQuantification is valid to wsml-full.
     * 
     * @see org.omwg.logicalexpression.Visitor#visitUniversalQuantification(org.omwg.logicalexpression.UniversalQuantification)
     */
    public void visitUniversalQuantification(UniversalQuantification expr) {

    }

    /**
     * Checks if an ExistentialQuantification is valid to wsml-full.
     * 
     * @see org.omwg.logicalexpression.Visitor#visitExistentialQuantification(org.omwg.logicalexpression.ExistentialQuantification)
     */
    public void visitExistentialQuantification(ExistentialQuantification expr) {

    }

}
