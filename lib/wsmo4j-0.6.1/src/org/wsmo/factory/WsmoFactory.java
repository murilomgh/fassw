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

package org.wsmo.factory;

import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.mediator.*;
import org.wsmo.service.*;


/**
 * WsmoFactory interface, used to create WSMO entities (goals, ontologies, ...)
 * @author not attributable
 * @version $Revision: 1.9 $ $Date: 2006/06/21 07:46:13 $
 */
public interface WsmoFactory {

    /** Creates a goal.
     * @param id The ID of the new goal object.
     * @return The newly created goal object.
     */
    Goal createGoal(IRI id);

    /**
     * Creates a new WebService object.
     * @param id The ID of the new service.
     * @return The new WebService object.
     */
    WebService createWebService(IRI id);

    /**
     * Creates a new Capability object.
     * @param id The ID of the new Capapbility.
     * @return The newly created Capability object.
     */
    Capability createCapability(IRI id);

    /**
     * Creates a new Interface object.
     * @param id The ID of the new interface object.
     * @return The newly created interface object.
     */
    Interface createInterface(IRI id);

    /**
     * Creates a new Ontology object.
     * @param id The ID of the new ontology.
     * @return The new Ontology object.
     */
    Ontology createOntology(IRI id);

    /**
     * Creates a mediator.
     * @param id The ID of the new mediator object.
     * @return The newly created mediator.
     */
    OOMediator createOOMediator(IRI id);

    /**
     * Creates a mediator.
     * @param id The ID of the new mediator object.
     * @return The newly created mediator.
     */
    WWMediator createWWMediator(IRI id);

    /**
     * Creates a mediator.
     * @param id The ID of the new mediator object.
     * @return The newly created mediator.
     */
    WGMediator createWGMediator(IRI id);

    /**
     * Creates a mediator.
     * @param id The ID of the new mediator object.
     * @return The newly created mediator.
     */
    GGMediator createGGMediator(IRI id);

    /**
     * Creates a new Axiom object.
     * @param id The ID of the new Axiom object.
     * @return The newly created Axion object.
     */
    Axiom createAxiom(Identifier id);

    /**
     * Creates a new Concept object.
     * @param id The ID of the new Concept object.
     * @return The newly created Concept object.
     */
    Concept createConcept(Identifier id);

    /**
     * Creates a new Relation object.
     * @param id The ID of the new relation.
     * @return The newly created Relation object.
     */
    Relation createRelation(Identifier id);

    /**
     * Creates a new Instance object.
     * @param id The ID of the new instance object.
     * @param concept The Concept this Instance object is an instance of
     * @return The newly created Instance object.
     */
    Instance createInstance(Identifier id);

    /**
     * Creates a new Instance object.
     * @param id The ID of the new instance object.
     * @param concept The Concept of the this Instance object is an instance of
     * @return The newly created Instance object.
     */
    Instance createInstance(Identifier id, Concept concept)
        throws SynchronisationException, InvalidModelException;

    /**
     * Creates a new RelationInstance object.
     * @param rel The relation this RelationInstance object is an instance of.
     * @return The newly created RelationInstance object.
     */
    RelationInstance createRelationInstance(Relation rel)
        throws SynchronisationException, InvalidModelException;

    /**
     * Creates a new RelationInstance object.
     * @param id The ID of the new RelationInstance object.
     * @param rel The relation this RelationInstance object is an instance of.
     * @return The newly created RelationInstance object.
     */
    RelationInstance createRelationInstance(Identifier id, Relation rel)
        throws SynchronisationException, InvalidModelException;

    /**
     * Creates a new Namespace object.
     * @param prefix namespace prefix. If <i>null</i> then the namespace should be used only as a default namespace
     * @param uri the IRI of the namespace
     * @return The newly created Namespace object.
     */
    Namespace createNamespace(String prefix, IRI iri);

    /**
     * Creates a new IRI object. (for example 'http://www.wsmo.org/x/y/z/')
     * @param fullIRI the IRI (as String)
     * @return The newly created IRI object.
     * @see #createIRI(Namespace ns, String localPart)
     */
    IRI createIRI(String fullIRI);

    /**
     * Creates a new IRI object (for example 'ns:x').
     * @param ns the namespace used
     * @param localPart the local part of the IRI (as String)
     * @return The newly created IRI object.
     * @see #createIRI(String fullIRI)
     */
    IRI createIRI(Namespace ns, String localPart);

    /**
     * Creates an unnumbered ID (i.e. '_#')
     * @return The newly created IRI object.
     * @see #createAnonymousID(byte number)
     */
    UnnumberedAnonymousID createAnonymousID();

    /*
     * Creates an numbered ID (i.e. '_#1', '_#2',...)
     * @return The newly created IRI object.
     * @see #createAnonymousID()
     *
    NumberedAnonymousID createAnonymousID(byte number);
     */

    /**
     * Return an existing Goal instance
     * @param id The ID of the existing Goal objec
     * @return An existing Goal instance
     */
    Goal getGoal(IRI id);

    /**
     * Return existing WebService instance
     * @param id The identifier of the WebService
     * @return WebService instance
     */
    WebService getWebService(IRI id);

    /**
     * Return an existing Interface instance
     * @param id The identifier of the Interface instance
     * @return An exisiting Interface instance
     */
    Interface getInterface(IRI id);

    /**
     * Return existing Capability instance
     * @param id The Capability identifier
     * @return existing Capability instance
     */
    Capability getCapability(IRI id);

    /**
     * Return exisitng Ontology instance
     * @param id The Ontology
     * @return existing Ontology instance
     */
    Ontology getOntology(IRI id);

    /**
     * Return an existing OOMediator instance
     * @param id The ID of an existing OOMediator
     * @return existing OOMediator insatnce
     */
    OOMediator getOOMediator(IRI id);

    /**
     * Return existing WWMediator instance
     * @param d Teh ID of the WWMediator instance
     * @return xiasing WWMedaitor instance
     */
    WWMediator getWWMediator(IRI id);

    /**
     * Return existing WGMediator instance
     * @param id The ID of the WGMediator instance
     * @return existing WGMediator instance
     */
    WGMediator getWGMediator(IRI id);

    /**
     * Return exiasing GGMediator instance
     * @param id The ID of the GGMediator instance
     * @return existing GGMediator instance
     */
    GGMediator getGGMediator(IRI id);

    /**
     * Return existing Axiom instance
     * @param id The ID of the Axiom
     * @return existing Axiom insatnce
     */
    Axiom getAxiom(Identifier id);

    /**
     * Return existing Concept insatnce
     * @param id The ID of the Concept
     * @return existing Concept Instance
     */
    Concept getConcept(Identifier id);

    /**
     * Return existing Instance object
     * @param id The ID of the Instance
     * @return existing Instance object
     */
    Instance getInstance(Identifier id);

    /**
     * Return existing Relation object
     * @param id The ID of the Relation
     * @return existing Relation object
     */
    Relation getRelation(Identifier id);

    /**
     * Return existing RelationInstance object
     * @param id The ID of the RelationInstance
     * @return existing RelationInstance object
     */
    RelationInstance getRelationInstance(Identifier id);

    /**
     * Return existing Varible instance
     * @param name The name of the var
     * @return existing var
     */
    Variable getVariable(String name);
}

/*
 * $Log: WsmoFactory.java,v $
 * Revision 1.9  2006/06/21 07:46:13  vassil_momtchev
 * createVariable(String) method moved from WsmoFactory to LogicalExpressionFactory interface
 *
 * Revision 1.8  2006/02/10 14:27:46  vassil_momtchev
 * getAttribute(IRI) method dropped from factory; attributes are strictly local to the scope of a concept
 *
 * Revision 1.7  2005/09/13 08:58:48  vassil_momtchev
 * WsmoFactory.createRelationInstance(Identifier) replaced by WsmoFactory.createRelationInstance(Relation) (anonymous identifier is used)
 *
 * Revision 1.6  2005/09/02 09:43:32  ohamano
 * integrate wsmo-api and le-api on api and reference implementation level; full parser, serializer and validator integration will be next step
 *
 * Revision 1.5  2005/09/02 08:44:52  marin_dimitrov
 * createVariable() does not throw InvalidModelEx anymore
 *
 * Revision 1.4  2005/09/01 14:55:53  vassil_momtchev
 * method createAttribute(IRI, Concept) removed
 *
 * Revision 1.3  2005/06/06 13:58:56  marin_dimitrov
 * 1. createSharedvariable --> createVariable
 *
 * 2. getVariable added
 *
 * Revision 1.2  2005/06/06 13:42:51  alex_simov
 * createSharedVariable() added
 *
 * Revision 1.1  2005/06/01 10:34:43  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.9  2005/05/31 09:27:31  damian
 * new family of 'get' methods added
 *
 * Revision 1.8  2005/05/31 08:57:20  damian
 * new family of 'get' methods added
 *
 * Revision 1.7  2005/05/19 14:21:16  marin
 * 1. createXXXInstance throws exceptions
 * 2. createNamespace accepts IRI (was: String)
 *
 * Revision 1.6  2005/05/19 13:56:06  marin
 * javadocs
 *
 */
