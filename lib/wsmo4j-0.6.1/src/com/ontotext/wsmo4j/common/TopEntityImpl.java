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

package com.ontotext.wsmo4j.common;


import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.omwg.ontology.Ontology;
import org.wsmo.common.IRI;
import org.wsmo.common.Identifier;
import org.wsmo.common.Namespace;
import org.wsmo.common.TopEntity;

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */
public class TopEntityImpl extends EntityImpl implements TopEntity {

    private String wsmlVariant;

    private Namespace defaultNS;

    private LinkedHashSet <IRI> mediators;

    private LinkedHashMap <String, Namespace> namespaces;

    private LinkedHashMap <Identifier, Ontology> ontologies;

    public TopEntityImpl(Identifier id) {
        super(id);
        mediators = new LinkedHashSet <IRI>();
        namespaces = new LinkedHashMap <String, Namespace> ();
        ontologies = new LinkedHashMap <Identifier, Ontology> ();
    }

    
    public String getWsmlVariant() {
        /* 
         * If variant is not set, you cannot conclude that it's core,
         * you need the validator to determine the variant.
         */

//        if (this.wsmlVariant == null) {
//            return WSML.WSML_CORE;
//        }
//        else {
            return this.wsmlVariant;
//        }
    }

    public void setWsmlVariant(String variant) {
        if (variant != null 
                && variant.trim().length() == 0) {
            variant = null;
        }
        this.wsmlVariant = variant;
    }

    public Set <Namespace> listNamespaces() {
        return new LinkedHashSet <Namespace>(namespaces.values());
    }

    public void addNamespace(Namespace ns) {
        if (ns == null) {
            throw new IllegalArgumentException();
        }
        namespaces.put(ns.getPrefix(), ns);
    }

    public void removeNamespace(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        namespaces.remove(prefix);
    }

    public void removeNamespace(Namespace ns) {
        if (ns == null) {
            throw new IllegalArgumentException();
        }
        removeNamespace(ns.getPrefix());
    }

    public Namespace findNamespace(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        return namespaces.get(prefix);
    }

    public Namespace getDefaultNamespace() {
        return this.defaultNS;
    }

    public void setDefaultNamespace(Namespace ns) {
        this.defaultNS = ns;
    }

    /**
     * sets a default namespace for the container
     * @param iri IRI of namespace to set as default
     * @see #setDefaultNamespace(Namespace ns)
     */
    public void setDefaultNamespace(IRI iri) {
        if (iri == null) {
            this.defaultNS = null;
        }
        else {
            this.defaultNS = new NamespaceImpl("", iri);
        }
    }

    public void addMediator(IRI mediator) {
        if (mediator == null) {
            throw new IllegalArgumentException();
        }
        mediators.add(mediator);
    }

    public void removeMediator(IRI mediator) {
        if (mediator == null) {
            throw new IllegalArgumentException();
        }
        mediators.remove(mediator);
    }


    public Set <IRI> listMediators() {
        return Collections.unmodifiableSet(mediators);
    }

    public void addOntology(Ontology ontology) {
        if (ontology == null) {
            throw new IllegalArgumentException();
        }
        ontologies.put(ontology.getIdentifier(), ontology);
    }

    public void removeOntology(IRI iri) {
        if (iri == null) {
            throw new IllegalArgumentException();
        }
        ontologies.remove(iri);
    }

    public void removeOntology(Ontology ontology) {
        if (ontology == null) {
            throw new IllegalArgumentException();
        }
        ontologies.remove(ontology.getIdentifier());
    }

    public Set <Ontology> listOntologies() {
        return new LinkedHashSet <Ontology> (ontologies.values());
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true; // instance match
        }
        if (object == null
                || false == object instanceof TopEntity) {
            return false;
        }
        return super.equals(object);
    }
}

/*
 * $Log: TopEntityImpl.java,v $
 * Revision 1.7  2007/04/02 12:13:20  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.6  2006/04/05 13:24:03  vassil_momtchev
 * usesMediator now refer mediators by  IRI instead of handle to object
 *
 * Revision 1.5  2005/11/09 12:32:06  alex_simov
 * setDefaultNamespace(IRI) did not handle null IRI argument
 *
 * Revision 1.4  2005/09/14 14:10:03  alex_simov
 * setWsmlVariant() allows null argument values
 *
 * Revision 1.3  2005/09/09 14:57:51  nathaliest
 * changed method getWsmlVariant
 *
 * Revision 1.2  2005/06/22 09:16:05  alex_simov
 * Simplified equals() method. Identity strongly relies on identifier string
 *
 * Revision 1.1  2005/06/01 12:00:32  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.5  2005/05/17 11:16:19  alex
 * 'import ... Mediator' added
 *
 * Revision 1.4  2005/05/17 10:49:52  marin
 * 1. added pre-condition on setWsmlVariant
 * 2. getWsmlVariant return WSML_CORE by default
 *
 * Revision 1.3  2005/05/16 08:50:21  alex
 * implementation method setDefaultNamespace(IRI) added
 *
 * Revision 1.2  2005/05/11 13:34:26  alex
 * equals() method added
 *
 * Revision 1.1  2005/05/11 11:50:56  alex
 * initial commit
 *
 */
