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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.omwg.ontology.Attribute;
import org.omwg.ontology.Concept;
import org.omwg.ontology.Instance;
import org.omwg.ontology.Value;
import org.wsmo.common.Identifier;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.common.exception.SynchronisationException;


public class InstanceImpl extends OntologyElementImpl
        implements Instance {

    private LinkedHashSet <Concept> concepts;
    
    private LinkedHashMap <Identifier, Set<Value>> attributeValues;
    
    public InstanceImpl(Identifier thisID) {
        super(thisID);
        concepts = new LinkedHashSet <Concept> ();
        attributeValues = new LinkedHashMap <Identifier, Set<Value>> ();
    }

    /**
     * Sets the concept this instance is an instance of.
     * @param concept The concept that this instance
     * is an instance of.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    public void addConcept(Concept memberOf) throws InvalidModelException {
        if (memberOf == null) {
            throw new IllegalArgumentException();
        }
        concepts.add(memberOf);
        if (false == memberOf.listInstances().contains(this)) {
            memberOf.addInstance(this);
        }
    }

    /**
     * Removes an axiom from the set of axioms defined
     * by this ontology.
     * @param axiom The axiom to be removed.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    public void removeConcept(Concept memberOf) throws InvalidModelException {
        if (memberOf == null) {
            throw new IllegalArgumentException();
        }
        concepts.remove(memberOf);
        if (memberOf.listInstances().contains(this)) {
            memberOf.removeInstance(this);
        }
    }

    /**
     * Lists the axioms defined by this ontology.
     * @return The set of axioms defined by this ontology.
     * @throws org.wsmo.common.exception.SynchronisationException
     */
    public Set <Concept> listConcepts() throws SynchronisationException {
        return Collections.unmodifiableSet(concepts);
    }

    /**
     * Adds a new attribute value to the
     * list of values associated with the specified
     * attribute of this instance.
     * @param value The value to be added.
     * @param id The attribute's identifier of interest.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    public Set <Value> listAttributeValues(Identifier id)
            throws SynchronisationException {
        Set <Value> collection = attributeValues.get(id);
        if (collection != null) {
            return Collections.unmodifiableSet(collection);
        }

        return Collections.unmodifiableSet(new HashSet <Value> ()); //immutable
    }

    /**
     * Returns a list of values of a specified attribute.
     * @return A Map of [Attribute, Set] pairs.
     * @throws org.wsmo.common.exception.SynchronisationException
     */
    public synchronized Map <Identifier, Set <Value>> listAttributeValues()
            throws SynchronisationException {
        Iterator <Map.Entry <Identifier, Set <Value>>> i = attributeValues.entrySet().iterator();
        LinkedHashMap <Identifier, Set <Value>> ret = new LinkedHashMap <Identifier, Set <Value>> ();
        while (i.hasNext()) {
            Map.Entry <Identifier, Set <Value>> curEntry = i.next();
            LinkedHashSet <Value> curVal = (LinkedHashSet <Value>) curEntry.getValue();
            ret.put(curEntry.getKey(), Collections.unmodifiableSet(curVal));
        }
        return ret;
    }

    /**
     * Adds a new attribute value to the
     * list of values associated with the specified
     * attribute of this instance.
     * @param value The value to be added.
     * @param id The attribute's identifier of interest.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    public synchronized void addAttributeValue(Identifier id, Value value)
            throws SynchronisationException, InvalidModelException {
        Set <Value> collection = attributeValues.get(id);
        if (collection == null) {
            collection = new LinkedHashSet <Value>();
            attributeValues.put(id, collection);
        }
        collection.add(value);
    }

    /**
     * Removes a particular value associated with an attribute within this instance.
     * @param id The attribute's identifier of interest.
     * @param value the attribute value to be removed.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeAttributeValues(Attribute key)
     */
    public synchronized void removeAttributeValue(Identifier id, Value attrVal) 
            throws SynchronisationException, InvalidModelException {
        Set <Value> collection = attributeValues.get(id);
        if (collection != null) {
            collection.remove(attrVal);
            if (collection.size() == 0) {
                attributeValues.remove(id);
            }
        }
    }

    /**
     * clears all the values associated with a particular attribute of this instance.
     * @param attribute The attribute of interest.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see removeAttributeValue(Attribute key, Object value)
     */
    public synchronized void removeAttributeValues(Identifier id)
            throws SynchronisationException, InvalidModelException {
        attributeValues.remove(id);
        }

    public Set <Attribute> findAttributeDefinitions(Identifier id) {
        Set <Attribute> attrs = new HashSet <Attribute> ();
        for (Iterator <Concept> i = concepts.iterator(); i.hasNext();) {
            attrs.addAll(i.next().findAttributes(id));
        }
        return attrs;
    }

    public boolean equals(Object object) {
        if (object == null
                || false == object instanceof Instance) {
            return false;
        }
        return super.equals(object);
    }
    
}

/*
 * $Log: InstanceImpl.java,v $
 * Revision 1.27  2007/04/02 12:19:08  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.26  2007/04/02 12:13:21  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.25  2007/02/22 15:32:48  alex_simov
 * unnecessary check removed from equals()
 *
 * Revision 1.24  2006/03/23 16:12:13  nathaliest
 * moving the anon Id check to the validator
 *
 * Revision 1.23  2006/02/13 11:49:58  nathaliest
 * changed anonId-check error String
 *
 * Revision 1.22  2006/02/13 09:21:21  nathaliest
 * added AnonIds Check
 *
 * Revision 1.21  2006/02/10 14:34:13  vassil_momtchev
 * instance refere attributes by Identifier; no longer handle to Attribute object is used
 *
 * Revision 1.20  2005/06/27 09:04:03  alex_simov
 * Object param substituted by Value
 *
 * Revision 1.19  2005/06/22 09:16:05  alex_simov
 * Simplified equals() method. Identity strongly relies on identifier string
 *
 * Revision 1.18  2005/06/03 13:02:12  alex_simov
 * fix
 *
 * Revision 1.17  2005/06/01 12:09:33  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.3  2005/05/17 12:43:17  alex
 * immutable Collections.EMPTY_SET instead of a new empty set
 *
 * Revision 1.2  2005/05/17 12:04:57  alex
 * Collections.unmodifiableSet() used instead of new set construction
 *
 * Revision 1.1  2005/05/13 08:01:39  alex
 * initial commit
 *
 */