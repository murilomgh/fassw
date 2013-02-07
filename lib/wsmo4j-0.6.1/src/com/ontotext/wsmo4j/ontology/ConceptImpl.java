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

import com.ontotext.wsmo4j.factory.*;


public class ConceptImpl extends OntologyElementImpl
        implements Concept {

    private LinkedHashSet <Concept> superConcepts;
    
    private LinkedHashSet <Concept> subConcepts;
    
    private LinkedHashSet <Instance> instances;
    
    private LinkedHashMap <Identifier, Attribute> attributes;
    
    private final static String ERROR_CYCLE = "Cycle in concept hierarchy detected!";
    
    public ConceptImpl(Identifier thisID, WsmoFactoryImpl factory) {
        super(thisID);
        superConcepts = new LinkedHashSet <Concept> ();
        subConcepts = new LinkedHashSet <Concept> ();
        attributes = new LinkedHashMap <Identifier, Attribute> ();
        instances = new LinkedHashSet <Instance> ();
    }

    public Set <Concept> listSuperConcepts() throws SynchronisationException {
        return Collections.unmodifiableSet(superConcepts);
    }

    public void addSuperConcept(Concept superConcept)
            throws SynchronisationException, InvalidModelException {
        if (superConcept == null) {
            throw new IllegalArgumentException();
        }
        if (checkInheritanceCycles(superConcept)) {
            throw new InvalidModelException(ERROR_CYCLE);
        }
        superConcepts.add(superConcept);
        // establishing the inverse connection
        if (false == superConcept.listSubConcepts().contains(this)) {
            superConcept.addSubConcept(this);
        }
    }

    public void removeSuperConcept(Concept superConcept)
            throws SynchronisationException, InvalidModelException {
        if (superConcept == null) {
            throw new IllegalArgumentException();
        }
        superConcepts.remove(superConcept);
        // removing the inverse connection
        if (superConcept.listSubConcepts().contains(this)) {
            superConcept.removeSubConcept(this);
        }
    }

    public Set <Concept> listSubConcepts() throws SynchronisationException {
        return Collections.unmodifiableSet(subConcepts);
    }

    public void addSubConcept(Concept subConcept)
            throws SynchronisationException, InvalidModelException {
        if (subConcept == null) {
            throw new IllegalArgumentException();
        }
        subConcepts.add(subConcept);
        // establishing the inverse connection
        if (false == subConcept.listSuperConcepts().contains(this)) {
            subConcept.addSuperConcept(this);
        }
    }

    public void removeSubConcept(Concept subConcept)
            throws SynchronisationException, InvalidModelException {
        if (subConcept == null) {
            throw new IllegalArgumentException();
        }
        subConcepts.remove(subConcept);
        // removing the inverse connection
        if (subConcept.listSuperConcepts().contains(this)) {
            subConcept.removeSuperConcept(this);
        }
    }

    public Set <Instance> listInstances() throws SynchronisationException {
        return Collections.unmodifiableSet(instances);
    }

    public void addInstance(Instance instance)
            throws SynchronisationException, InvalidModelException {
        if (instance == null) {
            throw new IllegalArgumentException();
        }
        instances.add(instance);
        // establishing the inverse connection
        if (false == instance.listConcepts().contains(this)) {
            instance.addConcept(this);
        }
    }

    public void removeInstance(Instance instance)
            throws SynchronisationException, InvalidModelException {
        if (instance == null) {
            throw new IllegalArgumentException();
        }
        instances.remove(instance);
        // removing the inverse connection
        if (instance.listConcepts().contains(this)) {
            instance.removeConcept(this);
        }
    }

    public Set <Attribute> listAttributes() throws SynchronisationException {
        return new LinkedHashSet <Attribute> (attributes.values());
    }

    public Set <Attribute> findAttributes(Identifier id)
            throws SynchronisationException {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        
        Set <Attribute> attrs = new HashSet <Attribute> ();
        Attribute att = attributes.get(id);
        if (att != null) {
            attrs.add(att);
        }
        
        for (Iterator i = superConcepts.iterator(); i.hasNext();) {
            attrs.addAll(((Concept) i.next()).findAttributes(id));
        }
        
        return attrs;
    }

    public Attribute createAttribute(Identifier id) throws InvalidModelException{
        Attribute attribute = attributes.get(id);
        if (attribute == null) {   
            attribute = new AttributeImpl(id, this);
            attributes.put(id, attribute);
        }
        return attribute;
    }

    public void removeAttribute(Identifier identifier) {
        if (identifier == null) {
            throw new IllegalArgumentException();
        }
        attributes.remove(identifier);
    }
    
    public void removeAttribute(Attribute attribute)
            throws SynchronisationException, InvalidModelException {
        if (attribute == null) {
            throw new IllegalArgumentException();
        }
        AttributeImpl attr = (AttributeImpl) attributes.remove(attribute.getIdentifier());
        if (attr != null && attr.getConcept() != null)
            attr.removeFromConcept();
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true; // instance match
        }
        if (object == null
                || false == object instanceof Concept) {
            return false;
        }
        return super.equals(object);
    }
    
    private boolean checkInheritanceCycles(Concept concept) {
        if (concept.equals(this))
            return true;
        
        for (Iterator i = concept.listSuperConcepts().iterator(); i.hasNext();) {
            Concept superConcept = (Concept) i.next();
            if (checkInheritanceCycles(superConcept))
                return true;
        }
        
        return false;
    }
}

/*
 * $Log: ConceptImpl.java,v $
 * Revision 1.30  2007/04/02 12:13:21  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.29  2006/11/17 10:52:41  vassil_momtchev
 * conceptImpl now uses attributeImpl, when attribute is removed to reset the domain of the attribute
 *
 * Revision 1.28  2006/03/23 16:12:13  nathaliest
 * moving the anon Id check to the validator
 *
 * Revision 1.27  2006/02/16 14:34:57  nathaliest
 * added removeAttribute(Identifier) method
 *
 * Revision 1.26  2006/02/13 22:49:23  nathaliest
 * - changed concept.createAttribute() and Parameter.addType to throw InvalidModelException.
 * - small change at check AnonIds in ConceptImpl
 *
 * Revision 1.25  2006/02/13 11:49:58  nathaliest
 * changed anonId-check error String
 *
 * Revision 1.24  2006/02/13 09:21:21  nathaliest
 * added AnonIds Check
 *
 * Revision 1.23  2006/02/10 14:33:04  vassil_momtchev
 * attributes are now stricly local to the scope of a concept; findAttributes implemented to search all superconcept attributes
 *
 * Revision 1.22  2005/09/15 14:59:41  vassil_momtchev
 * cycles in the inheritance hierarchy are not allowed - checkInheritanceCycles method implemented; imports organized;
 *
 * Revision 1.21  2005/09/07 14:33:01  vassil_momtchev
 * createAttribute did not add the created attribute to the internal map of attributes
 *
 * Revision 1.20  2005/09/01 14:55:19  vassil_momtchev
 * createAttribute(IRI) replaced addAttribute(Attribute)
 *
 * Revision 1.19  2005/07/04 14:25:04  marin_dimitrov
 * createAttribute-->addAttribute
 *
 * Revision 1.18  2005/06/22 09:16:05  alex_simov
 * Simplified equals() method. Identity strongly relies on identifier string
 *
 * Revision 1.17  2005/06/01 12:09:33  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.4  2005/05/31 14:41:30  damian
 * createAttribute changed
 *
 * Revision 1.3  2005/05/17 12:04:57  alex
 * Collections.unmodifiableSet() used instead of new set construction
 *
 * Revision 1.2  2005/05/12 11:14:05  alex
 * createAttribute() instantiated Attribute instead of  ConceptAttribute
 *
 * Revision 1.1  2005/05/11 14:19:53  alex
 * initial commit
 *
 */