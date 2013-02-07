/*
 wsmo4j - a WSMO API and Reference Implementation
 Copyright (c) 2004, DERI Innsbruck
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

import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.validator.ValidationError;

/**
 * The AnonIdCollectHelper collects the anonym concepts, instances and relation, and 
 * checks if they are used correctly. An object with an unnumbered anonymous identifier 
 * cannot be referenced, and thus, the validator throws an error, if such an object is 
 * referenced.
 *
 * <pre>
 * Created on March 24, 2006
 * Committed by $Author: morcen $
 * $Source: /cvsroot/wsmo4j/wsmo4j/org/deri/wsmo4j/validator/AnonIdCollectHelper.java,v $
 * </pre>
 *
 * @author nathalie.steinmetz@deri.org
 *
 * @version $Revision: 1.5 $ $Date: 2007/04/02 12:13:20 $
 */
public class AnonIdCollectHelper {

    private final static String ERROR_ANON_IDS = "An object with an unnumbered " +
            "anonymous identifier cannot be referenced. To reference this object, name it.";
    
    private List <ValidationError> errors = null;
    
    private HashMap <Concept, Integer> concepts = null;
    
    private HashMap <Relation, Integer> relations = null;
    
    private HashMap <Instance, Integer> instances = null;
    
    public AnonIdCollectHelper(List <ValidationError> errors) {
        this.errors = errors;
        concepts = new HashMap <Concept, Integer> ();
        relations = new HashMap <Relation, Integer> ();
        instances = new HashMap <Instance, Integer> ();
    }
    
    /*
     * This method checks if the given concept has got an unnumbered 
     * anonymous identifier and thus needs to be put into the concerned 
     * map 'concepts'. If not, it is checked if there are any references 
     * to such an anonymous concept, which then needs to be put in the map.
     */
    private void visitConcept(Concept concept){
        boolean checked = false;
        if (concept.getIdentifier() instanceof UnnumberedAnonymousID) { 
            addToConcepts(concept);          
            if (concept.listSuperConcepts().size() > 0) {
                if (checked) {
                    checked = false;
                    addToConcepts(concept);
                }
                else {
                    checked = true;
                }
            }
            if (concept.listAttributes().size() > 0) {
                if (checked) {
                    checked = false;
                    addToConcepts(concept);
                }
                else {
                    checked = true;
                }
            }
            if (concept.listInstances().size() > 0) {
                for (int i=0; i<concept.listInstances().size(); i++) {
                    if (checked) {
                        checked = false;
                        addToConcepts(concept);
                    }
                    else {
                        checked = true;
                    }
                }
            }
            if (concept.listNFPValues().size() > 0) {
                for (int i=0; i<concept.listNFPValues().size(); i++) {
                    if (checked) {
                        checked = false;
                        addToConcepts(concept);
                    }
                    else {
                        checked = true;
                    }
                }
            }   
        }
        if (concept.listSuperConcepts().size() > 0) {
            Iterator it = concept.listSuperConcepts().iterator();
            while (it.hasNext()) {
                Concept tmp = (Concept) it.next();
                if (tmp.getIdentifier() instanceof UnnumberedAnonymousID) {
                    addToConcepts(tmp, concept);
                }
            }
        }
        if (concept.listAttributes().size() > 0) {
            Iterator it = concept.listAttributes().iterator();
            while (it.hasNext()) {
                Attribute a = (Attribute) it.next();
                if (a.listTypes().size() > 0) {
                    Iterator it2 = a.listTypes().iterator();
                    while (it2.hasNext()) {
                        Type t = (Type) it2.next();
                        if (t instanceof Concept && 
                                ((Concept) t).getIdentifier() instanceof UnnumberedAnonymousID) {
                            addToConcepts((Concept) t, a);
                        }
                    }
                }
            }
        }
    }
    
    /*
     * This method checks if the given instance has got an unnumbered 
     * anonymous identifier and thus needs to be put into the appropriate 
     * map 'instances'. If not, it is checked if there are any references 
     * to such an anonymous instance, which then needs to be put in the map.
     */
    private void visitInstance(Instance instance) {
        boolean checked = false;
        if (instance.getIdentifier() instanceof UnnumberedAnonymousID) {
            addToInstances(instance);
            if (instance.listAttributeValues().size() > 0) {
                if (checked) {
                    checked = false;
                    addToInstances(instance);
                }
                else {
                    checked = true;
                }
            }
            if (instance.listConcepts().size() > 0) {
                for (int i=0; i<instance.listConcepts().size(); i++) {
                    if (checked) {
                        checked = false;
                        addToInstances(instance);
                    }
                    else {
                        checked = true;
                    }
                }
            }
            if (instance.listNFPValues().size() > 0) {
                for (int i=0; i<instance.listNFPValues().size(); i++) {
                    if (checked) {
                        checked = false;
                        addToInstances(instance);
                    }
                    else {
                        checked = true;
                    }
                }
            }
        }
        if (instance.listAttributeValues().size() > 0) {
            Iterator it = instance.listAttributeValues().entrySet().iterator();
            while (it.hasNext()) { 
                Set set = (Set) ((Entry) it.next()).getValue();
                Iterator it2 = set.iterator();
                while (it2.hasNext()) {
                    Object o = it2.next();
                    if (o instanceof Instance && 
                            ((Instance) o).getIdentifier() instanceof UnnumberedAnonymousID) {
                        addToInstances((Instance) o, instance);
                    }
                }
            }       
        }
    }
    
    /*
     * This method checks if the given relation has got an unnumbered 
     * anonymous identifier and thus needs to be put into the appropriate 
     * map 'relations'. If not, it is checked if there are any references 
     * to an anonymous relation or concept, which then needs to be put in 
     * the appropriate map.
     */
    private void visitRelation(Relation relation) {   
        if (relation.getIdentifier() instanceof UnnumberedAnonymousID) {
            addToRelations(relation);
            if (relation.listSuperRelations().size() > 0) {
                addToRelations(relation);
            }
            if (relation.listParameters().size() > 0) {
                Iterator it = relation.listParameters().iterator();
                while (it.hasNext()) {
                    Parameter p = (Parameter) it.next();
                    if (p.listTypes().size() > 0) {
                        addToRelations(relation);
                    }
                }
            }
            if (relation.listNFPValues().size() > 0) {
                for (int i=0; i<relation.listNFPValues().size(); i++) {
                    addToRelations(relation);
                }
            }
        }
        if (relation.listSuperRelations().size() > 0) {
            Iterator it = relation.listSuperRelations().iterator();
            while(it.hasNext()) {
                Relation tmp = (Relation) it.next();
                if (tmp.getIdentifier() instanceof UnnumberedAnonymousID) {
                    addToRelations(tmp, relation);
                }
            }
        }
        if (relation.listParameters().size() > 0) {
            Iterator it = relation.listParameters().iterator();
            while (it.hasNext()) {
                Parameter p = (Parameter) it.next();
                if (p.listTypes().size() > 0) {
                    Iterator it2 = p.listTypes().iterator();
                    while (it2.hasNext()) {
                        Type t = (Type) it2.next();
                        if (t instanceof Concept && 
                                ((Concept) t).getIdentifier() instanceof UnnumberedAnonymousID) {
                            addToConcepts((Concept) t, relation);
                            // if an relation contains a reference to an anonymous concept, 
                            // this relation cannot have a sub- or superrelation.
                            if (relation.listSuperRelations().size() > 0 || 
                                    relation.listSubRelations().size() > 0) {
                                addError(relation, ValidationError.ANON_ID_ERR + ":\n" + 
                                        ERROR_ANON_IDS);
                            }
                        }
                    }
                }
            }
        }
    }
    
    /*
     * This method checks if the given relationInstance references 
     * an anonymous relation or instance, which then needs to be put 
     * in the appropriate map.
     */
    private void visitRelationInstance(RelationInstance relationInstance) {
        if (relationInstance.getRelation().getIdentifier() instanceof UnnumberedAnonymousID) {
            addError(relationInstance, ValidationError.ANON_ID_ERR + ":\n" + 
                    "A relationInstance cannot reference an anonymous relation");
        }
        if (relationInstance.listParameterValues().size() > 0) {
            Iterator it = relationInstance.listParameterValues().iterator();
            while (it.hasNext()) {
                Value v = (Value) it.next();
                if (v instanceof Instance && 
                        ((Instance) v).getIdentifier() instanceof UnnumberedAnonymousID) {
                    addToInstances((Instance) v, relationInstance);
                }
            }
        }
    }
    
    /*
     * see addToConcepts(Concept concept, Entity entity)
     */
    private void addToConcepts(Concept concept) {
        addToConcepts(concept, concept);
    }
    
    /*
     * This method adds a given concept to the concepts map. If 
     * the concept is already contained in the map, the number of 
     * references to it is increased and a ValidationError is added.
     * 
     * param concept, concept to be added to the concepts map
     * param entity, entity to be added to the ValidationError
     */
    private void addToConcepts(Concept concept, Entity entity) {
        if (concepts.containsKey(concept)) {
            concepts.put(concept, new Integer(concepts.get(concept).intValue() + 1));
            addError(entity, ValidationError.ANON_ID_ERR + ":\n" + 
                  ERROR_ANON_IDS);
        }
        else {
            concepts.put(concept, new Integer(1));
        }
    }
    
    /*
     * see addToInstances(Instance instance, Entity entity)
     */
    private void addToInstances(Instance instance) {
        addToInstances(instance, instance);
    }
    
    /*
     * This method adds a given instance to the instances map. If 
     * the instance is already contained in the map, the number of 
     * references to it is increased and a ValidationError is added.
     * 
     * param instance, instance to be added to the instances map
     * param entity, entity to be added to the ValidationError
     */
    private void addToInstances(Instance instance, Entity entity) {
        if (instances.containsKey(instance)) {
            instances.put(instance, new Integer(instances.get(instance).intValue() + 1));
            addError(entity, ValidationError.ANON_ID_ERR + ":\n" + 
                  ERROR_ANON_IDS);
        }
        else {
            instances.put(instance, new Integer(1));
        }
    }
    
    /*
     * see addToRelations(Relation relation)
     */
    private void addToRelations(Relation relation) {
        addToRelations(relation, relation);
    }
    
    /*
     * This method adds a given relation to the relations map. If 
     * the relation is already contained in the map, the number of 
     * references to it is increased and a ValidationError is added.
     * 
     * param relation, relation to be added to the relations map
     * param entity, entity to be added to the ValidationError
     */
    private void addToRelations(Relation relation, Entity entity) {
        if (relations.containsKey(relation)) {
            relations.put(relation, new Integer(relations.get(relation).intValue() + 1));
            addError(entity, ValidationError.ANON_ID_ERR + ":\n" + 
                    ERROR_ANON_IDS);
        }
        else {
            relations.put(relation, new Integer(1));
        }
    }
    
    /**
     * This method takes the ontology and checks all its contained 
     * elements for an erronous use of unnumbered anonymous identifiers.
     */
    public void checkAnonIds(Ontology ontology) {
        Iterator i = ontology.listConcepts().iterator();
        while (i.hasNext()) {
            Concept concept = (Concept) i.next();
            visitConcept(concept);
        }
        i = ontology.listInstances().iterator();
        while (i.hasNext()) {
            Instance instance = (Instance) i.next();
            visitInstance(instance);
        }

        i = ontology.listRelations().iterator();
        while (i.hasNext()) {
            Relation relation = (Relation) i.next();
            visitRelation(relation);
        }

        i = ontology.listRelationInstances().iterator();
        while (i.hasNext()) {
            RelationInstance relationInstance = (RelationInstance) i.next();
            visitRelationInstance(relationInstance);
        }
    }
    
    private void addError(Entity entity, String reason) {
        errors.add(new ValidationErrorImpl(entity, reason, WSML.WSML_FULL));
    } 

}
/*
 * $Log: AnonIdCollectHelper.java,v $
 * Revision 1.5  2007/04/02 12:13:20  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.4  2006/04/26 13:11:49  nathaliest
 * fixed a bug at instances anonId check
 *
 * Revision 1.3  2006/03/28 08:16:36  nathaliest
 * added documentation
 *
 * Revision 1.2  2006/03/28 07:56:16  nathaliest
 * fix
 *
 * Revision 1.1  2006/03/27 16:35:01  nathaliest
 * changed anonId check
 *
 * 
 */