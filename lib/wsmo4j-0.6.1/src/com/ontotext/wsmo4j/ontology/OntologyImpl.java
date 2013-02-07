/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2004-2005, OntoText Lab. / SIRMA

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

package com.ontotext.wsmo4j.ontology;


/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */
import java.util.*;

import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;

import com.ontotext.wsmo4j.common.*;


public class OntologyImpl
    extends TopEntityImpl
    implements Ontology {

    private LinkedHashMap <Identifier, Concept> concepts;

    private LinkedHashMap <Identifier, Relation> relations;

    private LinkedHashMap <Identifier, Instance> instances;

    private LinkedHashMap <Identifier, Axiom> axioms;

    private LinkedHashMap <Identifier, RelationInstance> relationInstances;

    public OntologyImpl(IRI thisID) {
        super(thisID);

        concepts = new LinkedHashMap <Identifier, Concept> ();
        relations = new LinkedHashMap <Identifier, Relation> ();
        instances = new LinkedHashMap <Identifier, Instance> ();
        axioms = new LinkedHashMap <Identifier, Axiom> ();
        relationInstances = new LinkedHashMap <Identifier, RelationInstance> ();
    }

    public Set <Concept> listConcepts()
        throws SynchronisationException {
        return new LinkedHashSet <Concept> (concepts.values());
    }

    public void addConcept(Concept concept)
        throws SynchronisationException, InvalidModelException {
        if (concept == null) {
            throw new IllegalArgumentException();
        }
        concepts.put(concept.getIdentifier(), concept);
        if (false == this.equals(concept.getOntology())) {
            concept.setOntology(this);
        }
    }

    public void removeConcept(Concept concept)
        throws SynchronisationException, InvalidModelException {
        if (concept == null) {
            throw new IllegalArgumentException();
        }
        concepts.remove(concept.getIdentifier());
        concept.setOntology(null);
    }

    public void removeConcept(Identifier conceptID)
        throws SynchronisationException, InvalidModelException {
        if (conceptID == null) {
            throw new IllegalArgumentException();
        }
        Concept concept = concepts.get(conceptID);
        if (concept != null) {
            concept.setOntology(null);
        }
        concepts.remove(conceptID);
    }

    public Set <Relation> listRelations()
        throws SynchronisationException {
        return new LinkedHashSet <Relation> (relations.values());
    }

    public void addRelation(Relation relation)
        throws SynchronisationException, InvalidModelException {
        if (relation == null) {
            throw new IllegalArgumentException();
        }
        relations.put(relation.getIdentifier(), relation);
        if (false == this.equals(relation.getOntology())) {
            relation.setOntology(this);
        }
    }

    public void removeRelation(Relation relation)
        throws SynchronisationException, InvalidModelException {
        if (relation == null) {
            throw new IllegalArgumentException();
        }
        relations.remove(relation.getIdentifier());
        relation.setOntology(null);
    }

    public void removeRelation(Identifier relationID)
        throws SynchronisationException, InvalidModelException {
        if (relationID == null) {
            throw new IllegalArgumentException();
        }
        Relation relation = relations.get(relationID);
        if (relation != null) {
            relation.setOntology(null);
        }
        relations.remove(relationID);
    }

    public Set <Instance> listInstances()
        throws SynchronisationException {
        return new LinkedHashSet <Instance> (instances.values());
    }

    public void addInstance(Instance instance)
        throws SynchronisationException, InvalidModelException {
        if (instance == null) {
            throw new IllegalArgumentException();
        }
        instances.put(instance.getIdentifier(), instance);
        if (false == this.equals(instance.getOntology())) {
            instance.setOntology(this);
        }
    }

    public void removeInstance(Instance instance)
        throws SynchronisationException, InvalidModelException {
        if (instance == null) {
            throw new IllegalArgumentException();
        }
        instance.setOntology(null);
        instances.remove(instance.getIdentifier());
    }

    public void removeInstance(Identifier instanceID)
        throws SynchronisationException, InvalidModelException {
        if (instanceID == null) {
            throw new IllegalArgumentException();
        }
        Instance instance = instances.get(instanceID);
        if (instance != null) {
            instance.setOntology(null);
        }
        instances.remove(instanceID);
    }

    public Set <Axiom> listAxioms()
        throws SynchronisationException {
        return new LinkedHashSet <Axiom> (axioms.values());
    }

    public void addAxiom(Axiom axiom)
        throws SynchronisationException, InvalidModelException {
        if (axiom == null) {
            throw new IllegalArgumentException();
        }
        axioms.put(axiom.getIdentifier(), axiom);
        if (false == this.equals(axiom.getOntology())) {
            axiom.setOntology(this);
        }
    }

    public void removeAxiom(Axiom axiom)
        throws SynchronisationException, InvalidModelException {
        if (axiom == null) {
            throw new IllegalArgumentException();
        }
        axioms.remove(axiom.getIdentifier());
        axiom.setOntology(null);
    }

    public void removeAxiom(Identifier axiomID)
        throws SynchronisationException, InvalidModelException {
        if (axiomID == null) {
            throw new IllegalArgumentException();
        }
        Axiom axiom = axioms.get(axiomID);
        if (axiom != null) {
            axiom.setOntology(null);
        }
        axioms.remove(axiomID);
    }

    public Set <RelationInstance> listRelationInstances()
        throws SynchronisationException {
        return new LinkedHashSet <RelationInstance> (relationInstances.values());
    }

    public void addRelationInstance(RelationInstance instance)
        throws SynchronisationException, InvalidModelException {
        if (instance == null) {
            throw new IllegalArgumentException();
        }
        relationInstances.put(instance.getIdentifier(), instance);
        if (false == this.equals(instance.getOntology())) {
            instance.setOntology(this);
        }
    }

    public void removeRelationInstance(RelationInstance instance)
        throws SynchronisationException, InvalidModelException {
        if (instance == null) {
            throw new IllegalArgumentException();
        }
        relationInstances.remove(instance.getIdentifier());
        instance.setOntology(null);
    }

    public void removeRelationInstance(Identifier instanceID)
        throws SynchronisationException, InvalidModelException {
        if (instanceID == null) {
            throw new IllegalArgumentException();
        }
        RelationInstance instance = relationInstances.get(instanceID);
        if (instance != null) {
            instance.setOntology(null);
        }

        relationInstances.remove(instanceID);
    }

    public Concept findConcept(Identifier id)
        throws SynchronisationException {

        if (null == id) {
            throw new IllegalArgumentException();
        }
        return concepts.get(id);
    }

    public Relation findRelation(Identifier id)
        throws SynchronisationException {

        if (null == id) {
            throw new IllegalArgumentException();
        }
        return relations.get(id);
    }

    public Instance findInstance(Identifier id)
        throws SynchronisationException {

        if (null == id) {
            throw new IllegalArgumentException();
        }

        return instances.get(id);
    }

    public Axiom findAxiom(Identifier id)
        throws SynchronisationException {

        if (null == id) {
            throw new IllegalArgumentException();
        }
        return axioms.get(id);
    }

    public RelationInstance findRelationInstance(Identifier id)
        throws SynchronisationException {

        if (null == id) {
            throw new IllegalArgumentException();
        }
        return relationInstances.get(id);
    }

    public Set <Entity> findEntity(Identifier id)
        throws SynchronisationException {

        if (null == id) {
            throw new IllegalArgumentException();
        }
        //iterate all collections
        Set <Entity> entities = new LinkedHashSet <Entity> ();
        Entity entity = findAxiom(id);
        if (entity != null)
            entities.add(entity);
        entity = findConcept(id);
        if (entity != null)
            entities.add(entity);
        entity = findInstance(id);
        if (entity != null)
            entities.add(entity);
        entity = findRelation(id);
        if (entity != null)
            entities.add(entity);
        entity = findRelationInstance(id);
        if (entity != null)
            entities.add(entity);

        //no need to clone/copy since this is a temporary set
        return entities;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null
            || false == object instanceof Ontology) {
            return false;
        }
        return super.equals(object);
    }

}
/*
 * $Log: OntologyImpl.java,v $
 * Revision 1.27  2007/04/02 12:13:21  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.26  2005/10/18 10:36:04  vassil_momtchev
 * bug fix; findEntity(Identifier) returned Set with null values
 *
 * Revision 1.25  2005/10/18 09:08:34  marin_dimitrov
 * findEntity resturns Set (was Entity[])
 *
 * Revision 1.24  2005/10/18 08:46:47  vassil_momtchev
 * Entity findEntity(Identifier) changed to Entity[] findEntity(Identifier)
 *
 * Revision 1.23  2005/06/22 09:16:05  alex_simov
 * Simplified equals() method. Identity strongly relies on identifier string
 *
 * Revision 1.22  2005/06/07 07:30:58  alex_simov
 * owner ontology not set/cleared for addXXX() and removeXXX() methods
 *
 * Revision 1.21  2005/06/01 12:09:33  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.2  2005/05/19 14:05:43  marin
 * constructor accepts an IRI (was: Identifier)
 *
 * Revision 1.1  2005/05/12 15:47:15  alex
 * initial commit
 *
 */
