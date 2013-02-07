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
import org.deri.wsmo4j.logicalexpression.*;
import org.omwg.logicalexpression.*;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.factory.LogicalExpressionFactory;
import org.wsmo.service.Capability;
import org.wsmo.service.Goal;
import org.wsmo.service.WebService;
import org.wsmo.validator.*;


/**
 * Checks an ontology for wsml-full validity.
 *
 * <pre>
 *    Created on Jul 28, 2005
 *    Committed by $Author: morcen $
 *    $Source: /cvsroot/wsmo4j/wsmo4j/org/deri/wsmo4j/validator/WsmlFullValidator.java,v $,
 * </pre>
 *
 * @author Holger Lausen
 * @author nathalie.steinmetz@deri.org
 * @version $Revision: 1.28 $ $Date: 2007/04/02 12:13:19 $
 */
public class WsmlFullValidator
        implements Validator {
    
    private Ontology ontology = null;
   
    private AnonIdCollectHelper helper = null;
    
    protected ConstantTransformer constants = ConstantTransformer.getInstance();
    
    protected List <ValidationError> errors = null;
    
    protected List <ValidationWarning> warnings = null;
    
    protected LogExprSerializerWSML leSerializer = null;
    
    protected LogicalExpressionFactory leFactory = null;
    
    public WsmlFullValidator(LogicalExpressionFactory leFactory) {
        this.leFactory = leFactory;
    }
    
    /**
     * Checks if an axiom is valid to wsml-full.
     * 
     * @param axiom
     */
    protected void visitAxiom(Axiom axiom) {
        WsmlFullExpressionValidator exprVal = new WsmlFullExpressionValidator(axiom, errors);
        Iterator axioms = axiom.listDefinitions().iterator();
        while (axioms.hasNext()) {
            ((LogicalExpression)axioms.next()).accept(exprVal);
        }
    }

    /**
     * Checks if a concept is valid to wsml-full.
     * 
     * @param concept
     */
    protected void visitConcept(Concept concept) {
        // concept ID should always be OK
        // superConcept should also be OK
        // attributes can be mal formed      
        Iterator i = concept.listAttributes().iterator();
        while (i.hasNext()) {
            Attribute a = (Attribute)i.next();
            if (a.listTypes().size() == 0) {
                if (a.getIdentifier() instanceof IRI) {
                    if (concept.getIdentifier() instanceof IRI) {
                        addError(concept, a, ValidationError.ATTR_ERR + ":\n" +
                                "Attributes must have a range specified ("
                                 + ((IRI) a.getIdentifier()).getLocalName() + " at Concept "
                                 + ((IRI) concept.getIdentifier()).getLocalName() + ")"); 
                    }
                    else {
                        addError(concept, a, ValidationError.ATTR_ERR + ":\n" +
                                "Attributes must have a range specified ("
                                 + ((IRI) a.getIdentifier()).getLocalName() + " at Concept "
                                 + concept.getIdentifier().toString() + ")"); 
                    }
                }
                else {
                    if (concept.getIdentifier() instanceof IRI) {
                        addError(concept, a, ValidationError.ATTR_ERR + ":\n" +
                                "Attributes must have a range specified ("
                                 + a.getIdentifier().toString() + " at Concept "
                                 + ((IRI) concept.getIdentifier()).getLocalName() + ")"); 
                    }
                    else {
                        addError(concept, a, ValidationError.ATTR_ERR + ":\n" +
                                "Attributes must have a range specified ("
                                 + a.getIdentifier().toString() + " at Concept "
                                 + concept.getIdentifier().toString() + ")");   
                    }
                }
            }
            if (a.isReflexive()) {
            	Iterator it = a.listTypes().iterator();
            	while (it.hasNext()) {
            		if (it.next() instanceof WsmlDataType) {
            			addError(concept, a, ValidationError.ATTR_ERR + ":\n" +
                                "Attribute features are not allowed on attributes " +
                                "with a data type in its range ("
                                 + a.getIdentifier().toString() + " at Concept "
                                 + concept.getIdentifier().toString() + ")");  
            		}
            	}
            }
            if (a.isSymmetric()) {
            	Iterator it = a.listTypes().iterator();
            	while (it.hasNext()) {
            		if (it.next() instanceof WsmlDataType) {
            			addError(concept, a, ValidationError.ATTR_ERR + ":\n" +
                                "Attribute features are not allowed on attributes " +
                                "with a data type in its range ("
                                 + a.getIdentifier().toString() + " at Concept "
                                 + concept.getIdentifier().toString() + ")");  
            		}
            	} 
            }
            if (a.isTransitive()) {
            	Iterator it = a.listTypes().iterator();
            	while (it.hasNext()) {
            		if (it.next() instanceof WsmlDataType) {
            			addError(concept, a, ValidationError.ATTR_ERR + ":\n" +
                                "Attribute features are not allowed on attributes " +
                                "with a data type in its range ("
                                 + a.getIdentifier().toString() + " at Concept "
                                 + concept.getIdentifier().toString() + ")");  
            		}
            	} 
            }
            if (a.getInverseOf() != null) {
            	Iterator it = a.listTypes().iterator();
            	while (it.hasNext()) {
            		if (it.next() instanceof WsmlDataType) {
            			addError(concept, a, ValidationError.ATTR_ERR + ":\n" +
                                "Attribute features are not allowed on attributes " +
                                "with a data type in its range ("
                                 + a.getIdentifier().toString() + " at Concept "
                                 + concept.getIdentifier().toString() + ")");  
            		}
            	}
            }
        }
    }

    /**
     * Checks if an instance is valid to wsml-full.
     * 
     * @param instance
     */
    protected void visitInstance(Instance instance) {
        // API does not allow for inconsistent instances
    }

    /**
     * Checks if a relation is valid to wsml-full.
     * 
     * @param relation
     */
    protected void visitRelation(Relation relation) {
        int parameterCount = relation.listParameters().size(); 
        if (parameterCount == 0) {
            if (relation.getIdentifier() instanceof IRI) {
                addError(relation, ValidationError.REL_ARITY_ERR + ":\n" +
                        "Relation must have arity specified ("
                        + ((IRI) relation.getIdentifier()).getLocalName() + ")");
            }
            else {
                addError(relation, ValidationError.REL_ARITY_ERR + ":\n" +
                        "Relation must have arity specified ("
                         + relation.getIdentifier().toString() + ")");
            }
            return;
        }
        Iterator i = relation.listParameters().iterator();
        int specifiedParameters=0;
        while (i.hasNext()) {
            Parameter p = (Parameter)i.next();
            if (p.listTypes().size() == 0) {
                specifiedParameters++;
            }
        } 
        if (parameterCount != specifiedParameters && specifiedParameters != 0) {
            if (relation.getIdentifier() instanceof IRI) {
                addError(relation, ValidationError.REL_ERR + ":\n" +
                        "Either all Parameter or none must be specified\n" +
                        "(in relation " + ((IRI) relation.getIdentifier()).getLocalName() + ")");
            }
            else {
                addError(relation, ValidationError.REL_ERR + ":\n" +
                        "Either all Parameter or none must be specified\n" +
                        "(in relation " + relation.getIdentifier().toString() + ")");
            }
        }

    }

    /*
     * These exceptions shouldn't be thrown at all
     */
    /**
     * Checks if a relation instance is valid to wsml-full.
     * 
     * @param relationInstance
     * @throws SynchronisationException
     * @throws InvalidModelException
     */
    protected void visitRelationInstance(RelationInstance relationInstance)
            throws SynchronisationException, InvalidModelException {
        if (relationInstance.getRelation() == null) {
            if (relationInstance.getIdentifier() instanceof IRI) {
                addError(relationInstance, ValidationError.REL_INST_ERR + ":\n" +
                         "RelationInstances must be associated to an Relation "
                         + "(" + ((IRI) relationInstance.getIdentifier()).getLocalName() + ")");
            }
            else {
                addError(relationInstance, ValidationError.REL_INST_ERR + ":\n" +
                        "RelationInstances must be associated to an Relation "
                        + "(" + relationInstance.getIdentifier().toString() + ")");
            }
        }
    }

    /**
     * 
     * @return error list
     */
    public Collection getErrors() {
        return errors;
    }
    
    /**
     * Given an ontology as TopEntity, the method checks if the different 
     * ontology elements are valid and fills the error- and warningMessages lists 
     * with errors and warnings if not.
     * Given a WebService or a Goal as TopEntity, the method checks the capability 
     * assumptions, effects, postcondition and precondition axioms for validity and 
     * fills the error- and warningMessages if the axioms are not valid.
     * 
     * @param te TopEntity to be checked
     * @param errorMessages list that will be filled with error messages of found variant violations
     * @return true if valid (no errors), false if errors occured
     * @see org.wsmo.validator.Validator#isValid(org.wsmo.common.TopEntity, java.util.List, 
     *      java.util.List)
     */
    public boolean isValid(TopEntity te, List <ValidationError> errorMessages, List <ValidationWarning> warningMessages) {
        leSerializer = new LogExprSerializerWSML(te);
        errors = errorMessages;
        warnings = warningMessages;
        if (te instanceof Ontology) {
            ontology = (Ontology)te;
            // check for an erronous use of unnumbered anonymous identifiers
            helper = new AnonIdCollectHelper(errors);
            helper.checkAnonIds(ontology);
            Iterator i = ontology.listConcepts().iterator();
            while (i.hasNext()) {
                visitConcept((Concept)i.next());
            }
            i = ontology.listInstances().iterator();
            while (i.hasNext()) {
                visitInstance((Instance)i.next());
            }

            i = ontology.listRelations().iterator();
            while (i.hasNext()) {
                visitRelation((Relation)i.next());
            }

            i = ontology.listRelationInstances().iterator();
            while (i.hasNext()) {
                try {
                    visitRelationInstance((RelationInstance)i.next());
                }
                catch (InvalidModelException ime) {
                    new RuntimeException(ime);
                }
            }

            i = ontology.listAxioms().iterator();
            while (i.hasNext()) {
                visitAxiom((Axiom)i.next());
            }
        }
        else if (te instanceof WebService || te instanceof Goal) {
        	Capability capa = null;
        	if (te instanceof WebService) {
        		WebService webServ = (WebService) te;
        		capa = webServ.getCapability();
        	}
        	else if (te instanceof Goal) {
        		Goal goal = (Goal) te;
        		capa = goal.getCapability();
        	}
        	if (capa != null) {
	        	Iterator it = capa.listAssumptions().iterator();
	        	while (it.hasNext()) {
	        		visitAxiom((Axiom) it.next());
	        	}
	        	
	        	it = capa.listEffects().iterator();
	        	while (it.hasNext()) {
	        		visitAxiom((Axiom) it.next());
	        	}
	        	
	        	it = capa.listPreConditions().iterator();
	        	while (it.hasNext()) {
	        		visitAxiom((Axiom) it.next());
	        	}
	        	
	        	it = capa.listPostConditions().iterator();
	        	while (it.hasNext()) {
	        		visitAxiom((Axiom) it.next());
	        	}
        	}
        	addWarning(te, "The validation of Web Services and Goals is not " +
        			"yet complete. Currently the logical expressions in " +
        			"assumptions, effects, pre- and postconditions are " +
        			"validated against the WSML variant specified in the " +
        			"file.", null);
        }
        else {
            throw new UnsupportedOperationException(
                    "Only implemented for ontologies, web services and goals at present");
        }
        return (errors.isEmpty());
    }
    
    public boolean isValid(LogicalExpression logExpr, List <ValidationError> errorMessages, List <ValidationWarning> warningMessages) {
    	leSerializer = new LogExprSerializerWSML(null);
    	errors = errorMessages;
        warnings = warningMessages;
    	WsmlFullExpressionValidator exprVal = new WsmlFullExpressionValidator(null, errors);
        logExpr.accept(exprVal);
    	return (errors.isEmpty());
    }
    
    /*
     * Adds a new ValidationWarning to the error list
     */
    protected void addWarning(Entity ent, String msg, String quickFix) {
        ValidationWarningImpl vw = new ValidationWarningImpl(ent, msg, quickFix);
        if (!warnings.contains(vw)) {
            warnings.add(vw);
        }
    }
    
    /*
     * Adds a new ValidationError to the error list
     */
    protected void addError(Entity ent, String msg) {
        ValidationErrorImpl ve = new ValidationErrorImpl(ent, msg, WSML.WSML_FULL);
        if (!errors.contains(ve)) {
            errors.add(ve);
        }
    }
    
    /*
     * Adds a new AttributeError to the error list
     */
    protected void addError(Entity ent, Attribute att, String msg) {
        AttributeErrorImpl ae = new AttributeErrorImpl(ent, att, msg, WSML.WSML_FULL);
        if (!errors.contains(ae)) {
            errors.add(ae);
        }
    }
    
}