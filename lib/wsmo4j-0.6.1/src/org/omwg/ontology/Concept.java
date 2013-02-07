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
 * This interface represents a WSMO concept.
 *
 * @see Instance
 * @author not attributable
 * @version $Revision: 1.26 $ $Date: 2007/04/02 12:13:14 $
 */
public interface Concept
    extends OntologyElement, Type {

    /**
     * Adds a new concept to the set of super-concepts of this concept
     * <i>The super concept will also add this concept to the list of its sub-concepts</i>
     * @param superConcept The new super-concept to be added.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeSuperConcept(Concept superConcept)
     */
    void addSuperConcept(Concept superConcept)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes a concept from the set of super-concepts of this concept
     * <i>The super concept will also remove this concept from the list of its sub-concepts</i>.
     * @param superConcept The super-concept to be removed
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    void removeSuperConcept(Concept superConcept)
        throws SynchronisationException, InvalidModelException;

    /**
     * Lists the super-concepts of this concept.
     * @return The set of super-concepts defined by this ontology.
     * @throws org.wsmo.common.exception.SynchronisationException
     */
    Set <Concept> listSuperConcepts()
        throws SynchronisationException;

    /**
     * Adds a new concept to the set of sub-concepts of this concept
     * <i>The sub concept will also add this concept to the list of its super-concepts</i>.
     * @param subConcept The new sub-concept to be added.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeSubConcept(Concept subConcept)
     */
    void addSubConcept(Concept subConcept)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes a concept from the set of sub-concepts of this concept
     * <i>The sub concept will also remove this concept from the list of its super-concepts</i>.
     * @param subConcept The sub-concept to be removed
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    void removeSubConcept(Concept subConcept)
        throws SynchronisationException, InvalidModelException;

    /**
     * Lists the sub-concepts of this concept
     * @return The set of sub-concepts defined by this ontology.
     * @throws org.wsmo.common.exception.SynchronisationException
     */
    Set <Concept> listSubConcepts()
        throws SynchronisationException;

    /**
     * Create an Attribute for a Concept
     * @return a new Attribute for the Concept
     * @param id The ID of the new Attribute
     * @throws InvalidModelException 
     */
    Attribute createAttribute(Identifier id) throws InvalidModelException;

    /**
     * Removes an attribute from this concept's list of attributes.
     * @param identifier The identifier of the attribute to be removed 
     * from the concept's list of attributes.
     */
    void removeAttribute(Identifier identifier);
    
    /**
     * Removes an attribute from this concept's list of attributes.
     * @param attr The attribute to be removed from this concept's list of attributes.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    void removeAttribute(Attribute attr)
        throws SynchronisationException, InvalidModelException;

    /**
     * Returns a list of this concept's attributes.
     * @return The list of this concept's attributes.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see Attribute
     */
    Set <Attribute> listAttributes()
        throws SynchronisationException;

    /**
     * Searches for all attributes with the specified ID.
     * @param id identifier of the attribute
     * @return the attribute or null if not found
     * @throws org.wsmo.common.exception.SynchronisationException
     */
    Set <Attribute> findAttributes(Identifier id);

    /**
     * Adds a new instance to the set of instances of this concept
     * <i>The instance will also add this concept to the list of its concepts</i>
     * @param inst The new instance to be added.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeInstance(Instance inst)
     * @see Instance#addConcept(Concept memberOf)
     */
    void addInstance(Instance inst)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes an instance from the set of instances of this concept
     * <i>The instance will also remove this concept from the list of its concepts</i>
     * @param inst The instance to be removed
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    void removeInstance(Instance inst)
        throws SynchronisationException, InvalidModelException;

    /**
     * Returns a list of this concept's instances
     * @return The list of this concept's instances
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see Instance
     */
    Set <Instance> listInstances()
        throws SynchronisationException;

//DO NOT EDIT below this line

    /**
     * @supplierCardinality 0..*
     * @clientCardinality 0..*
     * @supplierRole has-instance
     * @clientRole has-concept */
    /*# Instance lnkInstance; */

    /**
     * @supplierCardinality 0..*
     * @clientCardinality 0..*
     * @supplierRole super-concept
     * @clientRole sub-concept
     */
    /*# Concept lnkConcept; */

    /**
     * @supplierCardinality 0..*
     * @clientCardinality 1
     * @supplierRole has-attribute
     * @clientRole defined-in
     * @directed
     * @link aggregationByValue
     */
    /*# Attribute lnkConceptAttribute; */
}

/*
 * $Log: Concept.java,v $
 * Revision 1.26  2007/04/02 12:13:14  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.25  2006/02/16 14:34:57  nathaliest
 * added removeAttribute(Identifier) method
 *
 * Revision 1.24  2006/02/13 22:49:23  nathaliest
 * - changed concept.createAttribute() and Parameter.addType to throw InvalidModelException.
 * - small change at check AnonIds in ConceptImpl
 *
 * Revision 1.23  2006/02/10 14:29:13  vassil_momtchev
 * Attribute findAttribute(Identifier) method changed to Set findAttributes(Identifier); now search all superconcept attributes also
 *
 * Revision 1.22  2005/09/21 08:15:39  holgerlausen
 * fixing java doc, removing asString()
 *
 * Revision 1.21  2005/09/01 14:55:09  vassil_momtchev
 * createAttribute(IRI) replaced addAttribute(Attribute)
 *
 * Revision 1.20  2005/07/14 09:01:23  vassil_momtchev
 * addAttribute method is restored to Concept interface (problem with the proxies)
 *
 * Revision 1.19  2005/07/05 08:59:35  alex_simov
 * removeAttribute() restored
 *
 * Revision 1.18  2005/07/04 14:20:17  marin_dimitrov
 * Concept::createAttribute is deprecated now. Use only the respective WsmoFactory method
 *
 * Revision 1.17  2005/06/24 12:47:58  marin_dimitrov
 * added common super interface of Concept and WsmlDataType
 *
 * Revision 1.16  2005/06/01 10:11:00  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.7  2005/05/31 13:12:26  damian
 * createAttribute changed
 *
 * Revision 1.6  2005/05/13 15:38:26  marin
 * fixed javadoc errors
 *
 * Revision 1.5  2005/05/13 13:58:37  marin
 * more @see tags
 *
 * Revision 1.4  2005/05/12 14:44:26  marin
 * javadoc, header, footer, etc
 *
 */
