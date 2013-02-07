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
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.factory.LogicalExpressionFactory;
import org.wsmo.validator.ValidationError;
import org.wsmo.validator.ValidationWarning;


/**
 * Checks an ontology for wsml-core validity.
 *
 * <pre>
 *   Created on Aug 18, 2005
 *   Committed by $Author: morcen $
 *   $Source: /cvsroot/wsmo4j/wsmo4j/org/deri/wsmo4j/validator/WsmlCoreValidator.java,v $,
 * </pre>
 *
 * @author Holger Lausen (holger.lausen@deri.org)
 * @author nathalie.steinmetz@deri.org
 * @version $Revision: 1.29 $ $Date: 2007/04/02 12:13:19 $
 */
public class WsmlCoreValidator
        extends WsmlDLValidator {
 
    private WsmlFlightValidator flightVal;
    
    public WsmlCoreValidator(LogicalExpressionFactory leFactory) {
        super(leFactory);
        flightVal = new WsmlFlightValidator(leFactory); 
    }
    
    private void setup(){
        flightVal.setErrors(this.errors);
        flightVal.setWarnings(this.warnings);
    }

    /**
     * Checks if an axiom is valid to wsml-core.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullValidator#visitAxiom(org.omwg.ontology.Axiom)
     */
    protected void visitAxiom(Axiom axiom) {
        setup();
        WsmlCoreExpressionValidator coreExprVal = new WsmlCoreExpressionValidator(axiom, leFactory, errors, this);
        WsmlDLExpressionValidator dlExprVal = new WsmlDLExpressionValidator(axiom, leFactory, errors, this);
        WsmlFlightExpressionValidator flightExprVal = new WsmlFlightExpressionValidator(axiom, leFactory, errors, this);
        WsmlRuleExpressionValidator ruleExprVal = new WsmlRuleExpressionValidator(axiom, errors, this);
        WsmlFullExpressionValidator fullExprVal = new WsmlFullExpressionValidator(axiom, errors);
        FunctionSymbolHelper fsHelper = new FunctionSymbolHelper(axiom, errors, WSML.WSML_CORE, this);
        IDCollectHelper idHelper = new IDCollectHelper();
        Iterator axioms = axiom.listDefinitions().iterator();
        while (axioms.hasNext()){
            LogicalExpression le = (LogicalExpression) axioms.next();
            
            le.accept(idHelper);
            // adding the Axiom's Concepts, Instances and Relations to the vectors
            idConcepts.addAll(idHelper.getConceptIds());
            idInstances.addAll(idHelper.getInstanceIds());
            idRelations.addAll(idHelper.getRelationIds());
            idAbstractRelations.addAll(idHelper.getIdAbstractRelations());
            idConcreteRelations.addAll(idHelper.getIdConcreteRelations());
            
            le.accept(fsHelper);
            
            le.accept(fullExprVal);
            ruleExprVal.setup();
            le.accept(ruleExprVal);
            flightExprVal.setup();
            le.accept(flightExprVal);
            dlExprVal.setup();
            le.accept(dlExprVal);
            coreExprVal.setup();
            le.accept(coreExprVal);
        }
    }

    /**
     * Checks if a concept is valid to wsml-core.
     *      
     * @see org.deri.wsmo4j.validator.WsmlFullValidator#visitConcept(org.omwg.ontology.Concept)
     */
    protected void visitConcept(Concept concept) {  
        super.visitConcept(concept);
//        setup();
//        flightVal.visitConcept(concept);
    }

    /**
     * Checks if an instance is valid to wsml-core.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullValidator#visitInstance(org.omwg.ontology.Instance)
     */
    protected void visitInstance(Instance instance) {
        super.visitInstance(instance); 
//        setup();
//        flightVal.visitInstance(instance);
    }

    /**
     * Checks if a relation is valid to wsml-core.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullValidator#visitRelation(org.omwg.ontology.Relation)
     */
    protected void visitRelation(Relation relation) {
        super.visitRelation(relation);
//        setup();
//        flightVal.visitRelation(relation);
    }

    /**
     * Checks if a relation instance is valid to wsml-core.
     * 
     * @throws InvalidModelException
     * @throws SynchronisationException
     * @see org.deri.wsmo4j.validator.WsmlFullValidator#visitRelationInstance(org.omwg.ontology.RelationInstance)
     */
    protected void visitRelationInstance(RelationInstance relationInstance)
            throws SynchronisationException, InvalidModelException {
        super.visitRelationInstance(relationInstance);
//        setup();
//        flightVal.visitRelationInstance(relationInstance);
    }
    
    public boolean isValid(LogicalExpression logExpr, List <ValidationError> errorMessages, List <ValidationWarning> warningMessages) {
    	super.isValid(logExpr, errorMessages, warningMessages);
    	errors = errorMessages;
        warnings = warningMessages;
        setup();
        WsmlCoreExpressionValidator coreExprVal = new WsmlCoreExpressionValidator(null, leFactory, errors, this);
        WsmlDLExpressionValidator dlExprVal = new WsmlDLExpressionValidator(null, leFactory, errors, this);
        WsmlFlightExpressionValidator flightExprVal = new WsmlFlightExpressionValidator(null, leFactory, errors, this);
        WsmlRuleExpressionValidator ruleExprVal = new WsmlRuleExpressionValidator(null, errors, this);
        WsmlFullExpressionValidator fullExprVal = new WsmlFullExpressionValidator(null, errors);
        FunctionSymbolHelper fsHelper = new FunctionSymbolHelper(
        		null, errors, WSML.WSML_CORE, this);
        IDCollectHelper idHelper = new IDCollectHelper();
        
        logExpr.accept(idHelper);
        // adding the Axiom's Concepts, Instances and Relations to the vectors
        idConcepts.addAll(idHelper.getConceptIds());
        idInstances.addAll(idHelper.getInstanceIds());
        idRelations.addAll(idHelper.getRelationIds());
        idAbstractRelations.addAll(idHelper.getIdAbstractRelations());
        idConcreteRelations.addAll(idHelper.getIdConcreteRelations());
        
        logExpr.accept(fsHelper);
        
        logExpr.accept(fullExprVal);
        ruleExprVal.setup();
        logExpr.accept(ruleExprVal);
        flightExprVal.setup();
        logExpr.accept(flightExprVal);
        dlExprVal.setup();
        logExpr.accept(dlExprVal);
        coreExprVal.setup();
        logExpr.accept(coreExprVal);
        
        return (errors.isEmpty());
    }
    
    /*
     * Adds a new ValidationError to the error list
     */
    protected void addError(Entity ent, String msg) {
        ValidationErrorImpl ve = new ValidationErrorImpl(ent, msg, WSML.WSML_CORE);
        if (!errors.contains(ve)) {
            errors.add(ve);
        }
    }
}
