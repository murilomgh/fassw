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

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 */

package org.omwg.ontology;


import java.util.*;

import org.wsmo.common.*;
import org.wsmo.common.exception.*;


/**
 * This interface represents a WSMO ontology.
 *
 * @author not attributable
 * @version $Revision: 1.22 $ $Date: 2007/04/02 12:13:14 $
 */
public interface Ontology
    extends TopEntity {

    /**
     * Adds a new concept to this ontology.
     * Note that a call to addXXX() i.e. adding an element to an ontology, should also
     * invoke the respective element's setOntology() method
     * @param concept The new concept to be added.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeConcept(Concept concept)
     * @see #removeConcept(Identifier id)
     * @see OntologyElement#setOntology(Ontology ontology)
     */
    void addConcept(Concept concept)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes a particular concept from this ontology.
     * @param concept The concept to be removed.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeConcept(Identifier id)
     */
    void removeConcept(Concept concept)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes a particular concept from this ontology.
     * @param concept The concept to be removed.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeConcept(Concept)
     */
    void removeConcept(Identifier id)
        throws SynchronisationException, InvalidModelException;

    /**
     * Lists the concepts defined by this ontology.
     * @return A set of concepts that this ontology contains.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see org.omwg.ontology.Concept
     */
    Set <Concept> listConcepts()
        throws SynchronisationException;

    /**
     * Finds a concept in the set of concepts defined by this ontology (returns <i>null</i> if none found).
     * @param id The Identifier of the requested concept.
     * @throws org.wsmo.common.exception.SynchronisationException
     */
    Concept findConcept(Identifier id)
        throws SynchronisationException;

    /**
     * Adds a new relation to the list of relations defined by this ontology.
     * Note that a call to addXXX() i.e. adding an element to an ontology, should also
     * invoke the respective element's setOntology() method
     * @param relation The relatino ot be added.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeRelation(Relation relation)
     * @see #removeRelation(Identifier id)
     * @see OntologyElement#setOntology(Ontology ontology)
     */

    void addRelation(Relation relation)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes a relation from the list of relations defined by this ontology.
     * @param relation The relation to be removed.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeRelation(Identifier id)
     */
    void removeRelation(Relation relation)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes a relation from the list of relations defined by this ontology.
     * @param relation The relation to be removed.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeRelation(Relation)
     */
    void removeRelation(Identifier id)
        throws SynchronisationException, InvalidModelException;

    /**
     * Lists the relations defined by this ontology.
     * @return A set of relations defined in this ontology.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see Relation
     */
    Set <Relation> listRelations()
        throws SynchronisationException;

    /**
     * Finds a relation in the set of relations defined by this ontology (returns NULL if none found).
     * @param id The Identifier of the requested relation.
     * @throws org.wsmo.common.exception.SynchronisationException
     */
    Relation findRelation(Identifier id)
        throws SynchronisationException;

    /**
     * Adds a new instance to the list of instances defined by this ontology.
     * Note that a call to addXXX() i.e. adding an element to an ontology, should also
     * invoke the respective element's setOntology() method
     * @param instance The new instance to be added.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeInstance(Instance instance)
     * @see #removeInstance(Identifier id)
     * @see OntologyElement#setOntology(Ontology ontology)
     */
    void addInstance(Instance instance)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes an instance from the list of instances defined by this ontology.
     * @param instance The instance to be removed.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeInstance(Identifier id)
     */
    void removeInstance(Instance instance)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes an instance from the list of instances defined by this ontology.
     * @param instance The instance to be removed.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeInstance(Instance)
     */
    void removeInstance(Identifier id)
        throws SynchronisationException, InvalidModelException;

    /**
     * Lists the instances defined by this ontology.
     * @return The list of instances defined by this ontology.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see Instance
     */
    Set <Instance> listInstances()
        throws SynchronisationException;

    /**
     * Finds an instance in the set of instances defined by this ontology (returns NULL if none found).
     * @param id The Identifier of the requested instance.
     * @throws org.wsmo.common.exception.SynchronisationException
     */
    Instance findInstance(Identifier id)
        throws SynchronisationException;

    /**
     * Adds a new axiom to the set of axioms defined by this ontology.
     * Note that a call to addXXX() i.e. adding an element to an ontology, should also
     * invoke the respective element's setOntology() method
     * @param axiom The new axioms to be added.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeAxiom(Axiom axiom)
     * @see #removeAxiom(Identifier id)
     * @see OntologyElement#setOntology(Ontology ontology)
     */
    void addAxiom(Axiom axiom)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes an axiom from the set of axioms defined by this ontology.
     * @param axiom The axiom to be removed.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeAxiom(Identifier id)
     */
    void removeAxiom(Axiom axiom)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes an axiom from the set of axioms defined by this ontology.
     * @param id The ID of the axiom to be removed.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeAxiom(Axiom axiom)
     */
    void removeAxiom(Identifier id)
        throws SynchronisationException, InvalidModelException;

    /**
     * Lists the axioms defined by this ontology.
     * @return The set of axioms defined by this ontology.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see Axiom
     */
    Set <Axiom> listAxioms()
        throws SynchronisationException;

    /**
     * Finds an axiom in the set of axioms defined by this ontology (returns NULL if none found).
     * @param id The Identifier of the requested axiom.
     * @throws org.wsmo.common.exception.SynchronisationException
     */
    Axiom findAxiom(Identifier id)
        throws SynchronisationException;

    /**
     * Lists the relationInstances defined by this ontology.
     * @return The list of relationInstances defined by this ontology.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see RelationInstance
     */
    Set <RelationInstance> listRelationInstances()
        throws SynchronisationException;

    /**
     * Adds a new relationInstance to the list of relationInstances defined by this ontology.
     * Note that a call to addXXX() i.e. adding an element to an ontology, should also
     * invoke the respective element's setOntology() method
     * @param instance The new relationInstance to be added.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeRelationInstance(RelationInstance instance)
     * @see #removeRelationInstance(Identifier id)
     * @see OntologyElement#setOntology(Ontology ontology)
     */
    void addRelationInstance(RelationInstance instance)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes a relationInstance from the list of relationInstances defined by this ontology.
     * @param instance The relationInstance to be removed.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeRelationInstance(Identifier id)
     */
    void removeRelationInstance(RelationInstance instance)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes a relationInstance from the list of relationInstances defined by this ontology.
     * @param id The ID of the relationInstance to be removed.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeRelationInstance(RelationInstance instance)
     */
    void removeRelationInstance(Identifier id)
        throws SynchronisationException, InvalidModelException;

    /**
     * Finds a relation instance in the set of relation instances defined by this ontology (returns NULL if none found).
     * @param id The Identifier of the requested relation instance.
     * @throws org.wsmo.common.exception.SynchronisationException
     */
    RelationInstance findRelationInstance(Identifier id)
        throws SynchronisationException;

    /**
     * Finds an object (axiom, concept, function, instance, relation, etc), returns NULL if none found.
     * Note that if metamodelling is used (e.g. the WSMl variant is more powerful than WSML-Core) then more
     * than one entity may correspond to a given ID.
     * @param id The Identifier of the requested object.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see Entity
     */
    Set <Entity> findEntity(Identifier id)
        throws SynchronisationException;

//DO NOT EDIT below this line

    /**
     * @link aggregationByValue
     * @supplierCardinality 0..*
     * @supplierRole contains
     * @clientCardinality 1
     * @clientRole defined-in
     */
    /*# Axiom lnkAxiom; */

    /**
     * @link aggregationByValue
     * @supplierCardinality 0..*
     * @supplierRole contains
     * @clientCardinality 1
     * @clientRole defined-in
     */
    /*# Relation lnkRelation; */

    /**
     * @link aggregationByValue
     * @supplierCardinality 0..*
     * @supplierRole contains
     * @clientCardinality 1
     * @clientRole defined-in
     */
    /*# RelationInstance lnkRelationInstance; */

    /**
     * @link aggregationByValue
     * @supplierCardinality 0..*
     * @supplierRole contains
     * @clientCardinality 1
     * @clientRole defined-in
     */
    /*# Instance lnkInstance; */

    /**
     * @link aggregationByValue
     * @supplierCardinality 0..*
     * @supplierRole contains
     * @clientCardinality 1
     * @clientRole defined-in
     */
    /*# Concept lnkConcept; */
}

/*
 * $Log: Ontology.java,v $
 * Revision 1.22  2007/04/02 12:13:14  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.21  2005/10/18 09:08:04  marin_dimitrov
 * findEntity resturns Set (was Entity[])
 *
 * Revision 1.20  2005/10/17 13:17:21  marin_dimitrov
 * metamodeliing extensions
 *
 * Revision 1.19  2005/09/21 08:15:39  holgerlausen
 * fixing java doc, removing asString()
 *
 * Revision 1.18  2005/06/01 10:13:40  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.7  2005/05/13 15:38:26  marin
 * fixed javadoc errors
 *
 * Revision 1.6  2005/05/13 13:29:12  marin
 * more comments
 *
 * Revision 1.5  2005/05/13 13:17:38  marin
 * javadoc, header, footer, etc
 *
 */
