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
import java.util.Map.Entry;

import org.omwg.logicalexpression.*;
import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.factory.LogicalExpressionFactory;
import org.wsmo.validator.ValidationError;
import org.wsmo.validator.ValidationWarning;


/**
 * Checks an ontology for wsml-dl validity.
 *
 * <pre>
 *  Created on Aug 18, 2005
 *  Committed by $Author: morcen $
 *  $Source: /cvsroot/wsmo4j/wsmo4j/org/deri/wsmo4j/validator/WsmlDLValidator.java,v $,
 * </pre>
 *
 * @author Holger Lausen (holger.lausen@deri.org)
 * @author nathalie.steinmetz@deri.org
 * @version $Revision: 1.22 $ $Date: 2007/04/02 12:13:19 $
 */
public class WsmlDLValidator
        extends WsmlFullValidator {

    public List <Term> idConcepts = new Vector <Term> ();
    
    public List <Term> explicitConcepts = new Vector <Term> ();

    public List <Term> idInstances = new Vector <Term> ();
    
    public List <Term> explicitInstances = new Vector <Term> ();

    public List <Term> idRelations = new Vector <Term> ();
    
    public List <Term> explicitRelations = new Vector <Term> ();
    
    public List <Term> idAbstractRelations = new Vector <Term> ();
    
    public List <Term> idConcreteRelations = new Vector <Term> ();
    
    private WsmlDLExpressionValidator dlExprVal = null;

    public WsmlDLValidator(LogicalExpressionFactory leFactory) {
        super(leFactory);
    }
    
    /**
     * Checks if an axiom is valid to wsml-dl.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullValidator#visitAxiom(org.omwg.ontology.Axiom)
     */
    protected void visitAxiom(Axiom axiom) {
        WsmlDLExpressionValidator dlExprVal = new WsmlDLExpressionValidator(axiom, leFactory, errors, this);
        WsmlFullExpressionValidator fullExprVal = new WsmlFullExpressionValidator(axiom, errors);
        FunctionSymbolHelper fsHelper = new FunctionSymbolHelper(
                axiom, errors, WSML.WSML_DL, this);
        IDCollectHelper idHelper = new IDCollectHelper();
        Iterator axioms = axiom.listDefinitions().iterator();
        while (axioms.hasNext()){
            LogicalExpression le = (LogicalExpression) axioms.next();
            
            le.accept(idHelper);
            // adding the Axiom's Concepts, Instances and Relations to the vectors
            idConcepts.addAll(idHelper.getConceptIds());
            explicitConcepts.addAll(idHelper.getExplicitConcepts());
            idInstances.addAll(idHelper.getInstanceIds());
            explicitInstances.addAll(idHelper.getExplicitInstances());
            idRelations.addAll(idHelper.getRelationIds());
            explicitRelations.addAll(idHelper.getExplicitRelations());
            idAbstractRelations.addAll(idHelper.getIdAbstractRelations());
            idConcreteRelations.addAll(idHelper.getIdConcreteRelations());
            
            le.accept(fsHelper);
            
            dlExprVal.setup();
            le.accept(fullExprVal);
            le.accept(dlExprVal);
        }
    }

    /**
     * Checks if a concept is valid to wsml-dl.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullValidator#visitConcept(org.omwg.ontology.Concept)
     */
    protected void visitConcept(Concept concept) {
        super.visitConcept(concept);
        Iterator i = concept.listAttributes().iterator();
        while (i.hasNext()) {
            Attribute a = (Attribute)i.next();
            String id1 = a.getIdentifier().toString();
            String id2 = concept.getIdentifier().toString();
            
            if (a.getIdentifier() instanceof IRI) {
                id1 = ((IRI) a.getIdentifier()).getLocalName();
            }
            if (concept.getIdentifier() instanceof IRI) {
                id2 = ((IRI) concept.getIdentifier()).getLocalName();
            }
            
            // check if the attribute has the transitive feature
            if (a.isTransitive()) {    
                addError(concept, a, ValidationError.ATTR_FEAT_ERR + ": Attributes may " +
                         "not contain the attribute feature 'transitive' \n("
                         + id1 + " at Concept " + id2 +  ")");
            }
            // check if the attribute has the reflexive feature
            else if (a.isReflexive()) {
                addError(concept, a, ValidationError.ATTR_FEAT_ERR + ": Attributes may " +
                         "not contain the attribute feature 'reflexive' \n("
                         + id1 + " at Concept " + id2 + ")");
            }
            // check if the attribute has the symmetric feature
            else if (a.isSymmetric()) {
                addError(concept, a, ValidationError.ATTR_FEAT_ERR + ": Attributes may " +
                         "not contain the attribute feature 'symmetric' \n("
                         + id1 + " at Concept " + id2 + ")");
            }
            // check if the attribute has the inverse feature
            else if (a.getInverseOf() != null) {
                addError(concept, a, ValidationError.ATTR_FEAT_ERR + ": Attributes may " +
                         "not contain the attribute feature 'inverseOf' \n("
                         + id1 + " at Concept " + id2 + ")");
            }
            // check if an attribute has a cardinality constraint
            if (a.getMaxCardinality() != Integer.MAX_VALUE || a.getMinCardinality() != 0) {
                addError(concept, a, ValidationError.ATTR_CARD_ERR + ": Attributes may " +
                         "not contain cardinality constraints \n("
                         + id1 + " at Concept " + id2 + ")");
            }
            Iterator it = a.listTypes().iterator();
            while (it.hasNext()) {
            	Type t = (Type)it.next();
                // check that if the attribute is constraining, it has a datatype range
                if (a.isConstraining()) {
                    if (!(t instanceof WsmlDataType)) {
                    	addError(concept, a, ValidationError.ATTR_CONS_ERR
                    			+ ": The attribute type 'ofType' is not allowed, " 
                    			+ "other than for datatype identifiers \n("
                    			+ id1 + " at Concept " + id2 + ")");
                    }
                }
                else {
                	if (t instanceof Concept) {
            			idConcepts.add(((Concept) t).getIdentifier());	
            		}
                }
            }
            // adding all attributes t o a vector
            idRelations.add(a.getIdentifier());
            isRelation(a);
        }
        // adding all concepts to a vector
        Identifier id = concept.getIdentifier();
        idConcepts.add(id);
        explicitConcepts.add(id);
        isConcept(concept);
        
        //checking for SuperConcepts and adding them to a vector
        if (!concept.listSuperConcepts().isEmpty()) {
            Iterator it = concept.listSuperConcepts().iterator();
            while (it.hasNext()) {
                Concept con = (Concept) it.next();
                idConcepts.add(con.getIdentifier());
                isConcept(con);
            }
        }
    }
    
    /*
     * Check for metamodelling error
     */
    private void isConcept(Entity e) {
        Identifier id = e.getIdentifier();
        if (idInstances.contains(id) || idRelations.contains(id) || 
                constants.isDataType(id.toString())){
            String idString = id.toString();
            if (id instanceof IRI) {
                idString = ((IRI) id).getLocalName();
            }
            addError(e, ValidationError.META_MODEL_ERR
                    + ": An ID can only denote an entity of one single type: \n("
                    + "at Concept " + idString + ")");
        }
    }

    /**
     * Checks if an instance is valid to wsml-dl.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullValidator#visitInstance(org.omwg.ontology.Instance)
     */
    protected void visitInstance(Instance instance) {
        super.visitInstance(instance);
        
        // adding all instances to a vector
        Identifier id = instance.getIdentifier();
        idInstances.add(id);
        explicitInstances.add(id);
        Set entries = instance.listAttributeValues().entrySet();
        Iterator it = entries.iterator();
        while (it.hasNext()) {
			Entry entry = (Entry) it.next();			
            Identifier attrId = (Identifier) entry.getKey();
        	idRelations.add(attrId);
        	Set valueSet = (Set) entry.getValue();
        	Iterator it2 = valueSet.iterator();
        	while (it2.hasNext()) {
        		Value value = (Value) it2.next();
            	if (value instanceof Instance) {
            		idInstances.add(((Instance) value).getIdentifier());
            		isInstance((Instance) value);
            	}
        	}	
        }
        isInstance(instance);
    }

    /*
     * Check for metamodelling error
     */
    private void isInstance(Entity e) {
    	Identifier id = e.getIdentifier();
        if (idConcepts.contains(id) || idRelations.contains(id) ||
                constants.isDataType(id.toString())){
            String idString = id.toString();
            if (id instanceof IRI) {
                idString = ((IRI) id).getLocalName();
            }
            addError(e, ValidationError.META_MODEL_ERR
                    + ": An ID can only denote an entity of one single type: \n("
                    + "at Instance " + idString + ")");
        }
    }
    
    /**
     * Checks if a relation is valid to wsml-dl.
     * 
     * @see org.deri.wsmo4j.validator.WsmlFullValidator#visitRelation(org.omwg.ontology.Relation)
     */
    protected void visitRelation(Relation relation) {
        super.visitRelation(relation);
    
        // check if the relation is a binary relation
        if (relation.listParameters().size() != 2) {
            addError(relation, ValidationError.REL_ARITY_ERR + ": The arity of " +
                    "relations is restricted to 2 \n");
        }
        else {
            Parameter p1 = relation.getParameter((byte)0);
            Parameter p2 = relation.getParameter((byte)1);

            // check that the first parameter hasn't got a datatype at its range
            Iterator it = p1.listTypes().iterator();
            while (it.hasNext()) {
            	Type type = (Type) it.next();
                if (type instanceof WsmlDataType) {
                    addError(relation, ValidationError.REL_ERR + ": The range of " +
                            "the first parameter of a binary relation may not contain a " +
                            "datatype\n");
                }
                else {
                	idConcepts.add(((Concept) type).getIdentifier());
                	isConcept((Concept) type);
                }
            }
            
            // check that the first parameter isn't constraining
            if (p1.isConstraining()) {
                addError(relation, ValidationError.REL_CONS_ERR + ": The 'ofType' " +
                        "keyword is not allowed for the first parameter of a binary " +
                        "relation");
            }
            
            /*
             * check that in case the second parameter is constraining, it
             * must have a datatype at its range
             */
            it = p2.listTypes().iterator();
            while (it.hasNext()) {
            	Type type = (Type)it.next();
            	if (p2.isConstraining()) {
            		if (!(type instanceof WsmlDataType)) {
            			addError(relation, ValidationError.REL_CONS_ERR + ": The 'ofType' " +
            					"keyword is not allowed without a datatype at the parameter's " +
            					"range\n");
            		}
            	}
            	else {
            		if (type instanceof Concept) {
            			idConcepts.add(((Concept) type).getIdentifier());
            			isConcept((Concept) type);
            		}
            	}
            }
        }
        // adding all relations to a vector
        idRelations.add(relation.getIdentifier());
        explicitRelations.add(relation.getIdentifier());
        isRelation(relation);
        
        // checking for SuperRelations and adding them to a vector
        if (!relation.listSuperRelations().isEmpty()) {
            Iterator it = relation.listSuperRelations().iterator();
            while (it.hasNext()) {
                Relation rel = (Relation) it.next();
                idRelations.add(rel.getIdentifier());
                isRelation(rel);
            }
        }
    }
    
    /*
     * Check for metamodelling error
     */
    private void isRelation(Entity e) {
        Identifier id = e.getIdentifier();
        if (idConcepts.contains(id) || idInstances.contains(id) ||
                constants.isDataType(id.toString())){
            String idString = id.toString();
            if (id instanceof IRI) {
                idString = ((IRI) id).getLocalName();
            }
            addError(e, ValidationError.META_MODEL_ERR
                    + ": An ID can only denote an entity of one single type: \n("
                    + "at Relation " + idString + ")");
        }
    }
    
    /**
     * Checks if a relation instance is valid to wsml-dl.
     * 
     * @throws InvalidModelException
     * @throws SynchronisationException
     * @see org.deri.wsmo4j.validator.WsmlFullValidator#visitRelationInstance(org.omwg.ontology.RelationInstance)
     */
    protected void visitRelationInstance(RelationInstance relationInstance)
            throws SynchronisationException, InvalidModelException {
        /*
         * It is not possible to check if the values of a relation correspond to
         * their signatures, because instance hierarches are not implemented
         *
         * Exceptions from relationInstance.getParameterValue(byte) shouldn't be
         * thrown
         */
        super.visitRelationInstance(relationInstance);
        
        Value v1 = relationInstance.getParameterValue((byte)0);
        Value v2 = relationInstance.getParameterValue((byte)1);
        String id1 = relationInstance.getIdentifier().toString();
        String id2 = relationInstance.getRelation().getIdentifier().toString();
        if (relationInstance.getIdentifier() instanceof IRI) {
            id1 = ((IRI) relationInstance.getIdentifier()).getLocalName();
        }
        if (relationInstance.getRelation().getIdentifier() instanceof IRI) {
            id2 = ((IRI) relationInstance.getRelation().getIdentifier()).getLocalName();
        }
        // check that both values of the relation are specified
        if (v1 == null || v2 == null) {
             
            addError(relationInstance, ValidationError.REL_INST_ERR + ": Both values of " +
                     "the relation have to be specified \n("
                     + id1 + " at relation "
                     + id2 + ")");
        }
        // check that the first value is not a data value
        if (v1 instanceof DataValue) {
            addError(relationInstance, ValidationError.REL_INST_ERR + ": The first value " +
                     "may not be a data value \n(" + id1 + " at relation " + id2 + ")");
        }
        idRelations.add(relationInstance.getIdentifier());
        isRelation(relationInstance);
    }
    
    /**
     * @return Returns the idConcepts.
     */
    public List getIdConcepts() {
        return idConcepts;
    }

    /**
     * @return Returns the idInstances.
     */
    public List getIdInstances() {
        return idInstances;
    }

    /**
     * @return Returns the idRelations.
     */
    public List getIdRelations() {
        return idRelations;
    }
    
    /**
     * 
     * @return Returns the idAbstractRelations
     */
    public List getIdAbstractRelations() {
        return idAbstractRelations;
    }

    /**
     * @return Returns the idConcreteRelations
     */
    public List getIdConcreteRelations() {
        return idConcreteRelations;
    }

    private void checkForMetaModelling() {
    	Iterator it = idConcepts.iterator();
    	while (it.hasNext()) {
    		Object obj = it.next();
    		if (obj instanceof IRI) {
    			Identifier id = (Identifier) obj;
    			if (idInstances.contains(id) || idRelations.contains(id) 
    					|| constants.isDataType(id.toString())) {
    				String idString = id.toString();
    				if (id instanceof IRI) {
    					idString = ((IRI) id).getLocalName();
    				}
    				addError(null, ValidationError.META_MODEL_ERR
    						+ ": An ID can only denote an entity of one single type: \n("
    						+ "at Concept " + idString + ")");
    			}
    		}
    	}
    	it = idInstances.iterator();
    	while (it.hasNext()) {
    		Object obj = it.next();
    		if (obj instanceof IRI) {
    			Identifier id = (Identifier) obj;
    			if (idConcepts.contains(id) || idRelations.contains(id) 
    					|| constants.isDataType(id.toString())) {
    				String idString = id.toString();
    				if (id instanceof IRI) {
    					idString = ((IRI) id).getLocalName();
    				}
    				addError(null, ValidationError.META_MODEL_ERR
    						+ ": An ID can only denote an entity of one single type: \n("
    						+ "at Instance " + idString + ")");
    			}
    		}
    	}
    	it = idRelations.iterator();
    	while (it.hasNext()) {
    		Object obj = it.next();
    		if (obj instanceof IRI) {
    			Identifier id = (Identifier) obj;
				if (idInstances.contains(id) || idConcepts.contains(id) 
						|| constants.isDataType(id.toString())) {
					String idString = id.toString();
					if (id instanceof IRI) {
						idString = ((IRI) id).getLocalName();
					}
					addError(null, ValidationError.META_MODEL_ERR
							+ ": An ID can only denote an entity of one single type: \n("
							+ "at Relation " + idString + ")");
				}
    		}
    	}
    }
    
    public boolean isValid(TopEntity te, List <ValidationError> errorMessages, List <ValidationWarning> warningMessages) {
        boolean result = super.isValid(te, errorMessages, warningMessages);
        
        checkForMetaModelling();
        
        // check if all concepts have been explicitly declared
        for (int i=0; i<idConcepts.size(); i++) {
            String id = idConcepts.get(i).toString();
        
            if (idConcepts.get(i) instanceof IRI) {
                id = ((IRI) idConcepts.get(i)).getLocalName();
            }
        
            if (!explicitConcepts.contains(idConcepts.get(i))) {
                addWarning(te, "concept " + id + " not explicitly declared!", null);
            }
        }
        
//        // check if all instances have been explicitly declared
//        for (int i=0; i<idInstances.size(); i++) {
//            String id = idInstances.get(i).toString();
//            
//            if (idInstances.get(i) instanceof IRI) {
//                id = ((IRI) idInstances.get(i)).getLocalName();
//            }
//            
//            if (!explicitInstances.contains(idInstances.get(i))) {
//                addWarning(te, "instance " + id + " not explicitly declared!", null);
//            }
//        }
        
//        // check if all relations have been explicitly declared
//        for (int i=0; i<idRelations.size(); i++) {
//            String id = idRelations.get(i).toString();
//            
//            if (idRelations.get(i) instanceof IRI) {
//                id = ((IRI) idRelations.get(i)).getLocalName();
//            }
//            
//            if (!explicitRelations.contains(idRelations.get(i))) {
//                addWarning(te, "relation " + id + " not explicitly declared!", null);
//            }
//        }
        
        return result;
    }
    
    public boolean isValid(LogicalExpression logExpr, List <ValidationError> errorMessages, List <ValidationWarning> warningMessages) {
    	super.isValid(logExpr, errorMessages, warningMessages);
    	errors = errorMessages;
        warnings = warningMessages;
		
        dlExprVal = new WsmlDLExpressionValidator(null, leFactory, errors, this);
        WsmlFullExpressionValidator fullExprVal = new WsmlFullExpressionValidator(null, errors);
        FunctionSymbolHelper fsHelper = new FunctionSymbolHelper(
        		null, errors, WSML.WSML_DL, this);
        IDCollectHelper idHelper = new IDCollectHelper();    
        
        logExpr.accept(idHelper);
        // adding the Axiom's Concepts, Instances and Relations to the vectors
        idConcepts.addAll(idHelper.getConceptIds());
        explicitConcepts.addAll(idHelper.getExplicitConcepts());
        idInstances.addAll(idHelper.getInstanceIds());
        explicitInstances.addAll(idHelper.getExplicitInstances());
        idRelations.addAll(idHelper.getRelationIds());
        explicitRelations.addAll(idHelper.getExplicitRelations());
        idAbstractRelations.addAll(idHelper.getIdAbstractRelations());
        idConcreteRelations.addAll(idHelper.getIdConcreteRelations());
        
        logExpr.accept(fsHelper);
        
        dlExprVal.setup();
        logExpr.accept(fullExprVal);
        logExpr.accept(dlExprVal);
        
        checkForMetaModelling();
        
        return (errors.isEmpty());
    }
    
    public Variable getRootVariable(LogicalExpression logExpr) {
    	return dlExprVal.getRoot();
    }
    
    /*
     * Adds a new ValidationError to the error list
     */
    protected void addError(Entity ent, String msg) {
        ValidationErrorImpl ve = new ValidationErrorImpl(ent, msg, WSML.WSML_DL);
        if (!errors.contains(ve)) {
            errors.add(ve);
        }
    }
    
    /*
     * Adds a new AttributeError to the error list
     */
    protected void addError(Entity ent, Attribute att, String msg) {
        AttributeErrorImpl ae = new AttributeErrorImpl(ent, att, msg, WSML.WSML_DL);
        if (!errors.contains(ae)) {
            errors.add(ae);
        }
    }
    
}