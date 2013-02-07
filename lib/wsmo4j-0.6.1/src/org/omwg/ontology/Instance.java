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
 * This class represents an instance of a WSMO concept.
 *
 * @see Concept
 * @author not attributable
 * @version $Revision: 1.20 $ $Date: 2007/04/02 12:13:14 $
 */
public interface Instance
    extends OntologyElement, Value {

    /**
     * Sets the concept this instance is an instance of.
     * <i>The concept will also add this instance to the list of its instances</i>
     * @param memberOf The concept that this instance is an instance of.
     * @see #removeConcept(Concept memberOf)
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see Concept#addInstance(Instance inst)
     */
    void addConcept(Concept memberOf)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes a concept from the set of concepts that this entity is an instance of.
     * @param memberOf the concept (the entity is no longer an instance of it)
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    void removeConcept(Concept memberOf)
        throws SynchronisationException, InvalidModelException;

    /**
     * Returns a list of the concepts this instance belongs to
     * @return The list of the concepts of this instances
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see Concept
     */
    Set <Concept> listConcepts()
        throws SynchronisationException;

    /**
     * Adds a new attribute value to the list of values associated with the specified attribute of this instance.
     * @param id the attribute's indentifier of interest
     * @param value The value to be added
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see Concept#createAttribute(IRI)
     * @see #removeAttributeValue(Identifier, Value)
     * @see #removeAttributeValues(Identifier)
     */
    void addAttributeValue(Identifier id, Value value )
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes a particular value associated with an attribute within this instance.
     * @param id The attribute's indentifier of interest
     * @param value the attribute value to be removed
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeAttributeValues(Identifier id)
     */
    void removeAttributeValue(Identifier key, Value value )
        throws SynchronisationException, InvalidModelException;

    /**
     * Clears all the values associated with a particular attribute of this instance.
     * @param id The attribute's indentifier of interest
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeAttributeValue(Identifier id, Object value)
     */
    void removeAttributeValues(Identifier id)
        throws SynchronisationException, InvalidModelException;

    /**
     * Returns a list of values of a specified attribute.
     * Note that an attribute may be associated with more than one value
     * @param key The attribute of interest.
     * @return A set of values assigned to this attribute.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see #listAttributeValues()
     */
    Set <Value> listAttributeValues(Identifier key)
        throws SynchronisationException;

    /**
     * Returns all attribute values for this instance.
     * @return A Map of [Attribute, Set of values] pairs.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see #listAttributeValues(Identifier id)
     */
    Map <Identifier, Set <Value>> listAttributeValues()
        throws SynchronisationException;

    /**
     * Search this concept and all super concepts for a
     * specified attribute.
     * @param id of the attribute
     * @return Set with all attributes
     * @throws SynchronisationException
     */
    Set <Attribute> findAttributeDefinitions(Identifier id) throws SynchronisationException;

//DO NOT EDIT below this line

    /**
     * @supplierCardinality 0..*
     * @supplierRole specifies-value
     * @directed
     */
    /*# Attribute lnkAttribute; */
}

/*
 * $Log: Instance.java,v $
 * Revision 1.20  2007/04/02 12:13:14  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.19  2006/02/10 14:29:55  vassil_momtchev
 * instance reference attributes by Identifier; no longer handle to Attribute object used;
 *
 * Revision 1.18  2005/09/21 08:15:39  holgerlausen
 * fixing java doc, removing asString()
 *
 * Revision 1.17  2005/06/24 12:51:06  marin_dimitrov
 * now use Value for param/attr values (was: Object)
 *
 * Revision 1.16  2005/06/24 12:47:14  marin_dimitrov
 * added common super interface of Instance and DataValue
 *
 * Revision 1.15  2005/06/01 10:12:38  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.5  2005/05/13 15:38:26  marin
 * fixed javadoc errors
 *
 * Revision 1.4  2005/05/13 13:58:38  marin
 * more @see tags
 *
 * Revision 1.3  2005/05/12 14:44:26  marin
 * javadoc, header, footer, etc
 *
 */
