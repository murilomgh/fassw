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


import java.util.Iterator;
import java.util.List;

import org.omwg.logicalexpression.LogicalExpression;
import org.omwg.ontology.Axiom;
import org.omwg.ontology.Concept;
import org.omwg.ontology.Instance;
import org.omwg.ontology.Relation;
import org.omwg.ontology.RelationInstance;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.common.exception.SynchronisationException;
import org.wsmo.factory.LogicalExpressionFactory;
import org.wsmo.validator.ValidationError;
import org.wsmo.validator.ValidationWarning;


/**
 * Checks an ontology for wsml-rule validity.
 *
 * <pre>
 *  Created on Aug 18, 2005
 *  Committed by $Author: morcen $
 *  $Source: /cvsroot/wsmo4j/wsmo4j/org/deri/wsmo4j/validator/WsmlRuleValidator.java,v $,
 * </pre>
 *
 * @author Holger Lausen (holger.lausen@deri.org)
 * @author nathalie.steinmetz@deri.org
 * @version $Revision: 1.10 $ $Date: 2007/04/02 12:13:20 $
 */
public class WsmlRuleValidator
        extends WsmlFullValidator {

    public WsmlRuleValidator(LogicalExpressionFactory leFactory) {
        super(leFactory);
    }
    
    /**
     * Checks if an axiom is valid to wsml-rule.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullValidator#visitAxiom(org.omwg.ontology.Axiom)
     */
    protected void visitAxiom(Axiom axiom) {
        WsmlRuleExpressionValidator ruleExprVal = new WsmlRuleExpressionValidator(axiom, errors, this);
        WsmlFullExpressionValidator fullExprVal = new WsmlFullExpressionValidator(axiom, errors);
        Iterator axioms = axiom.listDefinitions().iterator();
        while (axioms.hasNext()) {
            LogicalExpression le = (LogicalExpression) axioms.next();
            le.accept(fullExprVal);
            ruleExprVal.setup();
            le.accept(ruleExprVal);
        }
    }

    /**
     * Checks if a concept is valid to wsml-rule.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullValidator#visitConcept(org.omwg.ontology.Concept)
     */
    protected void visitConcept(Concept concept) {
        super.visitConcept(concept);
    }

    /**
     * Checks if an instance is valid to wsml-rule.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullValidator#visitInstance(org.omwg.ontology.Instance)
     */
    protected void visitInstance(Instance instance) {
        super.visitInstance(instance);
    }

    /**
     * Checks if a relation is valid to wsml-rule.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullValidator#visitRelation(org.omwg.ontology.Relation)
     */
    protected void visitRelation(Relation relation) {
        super.visitRelation(relation);
    }

    /**
     * Checks if a relation instance is valid to wsml-rule.
     * 
     * @throws InvalidModelException
     * @throws SynchronisationException
     * @see org.deri.wsmo4j.validator.WsmlFullValidator#visitRelationInstance(org.omwg.ontology.RelationInstance)
     */
    protected void visitRelationInstance(RelationInstance relationInstance)
            throws SynchronisationException, InvalidModelException {
        super.visitRelationInstance(relationInstance);
    }
    
    public boolean isValid(LogicalExpression logExpr, List <ValidationError> errorMessages, List <ValidationWarning> warningMessages) {
    	super.isValid(logExpr, errorMessages, warningMessages);
    	errors = errorMessages;
    	warnings = warningMessages;
    	WsmlRuleExpressionValidator ruleExprVal = new WsmlRuleExpressionValidator(null, errors, this);
        WsmlFullExpressionValidator fullExprVal = new WsmlFullExpressionValidator(null, errors);
        logExpr.accept(fullExprVal);
        ruleExprVal.setup();
        logExpr.accept(ruleExprVal);
        return (errors.isEmpty());
    }
    
}