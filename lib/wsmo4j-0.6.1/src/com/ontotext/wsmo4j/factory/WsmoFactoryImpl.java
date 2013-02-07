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

package com.ontotext.wsmo4j.factory;

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */

import java.lang.reflect.*;
import java.util.*;

import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.factory.*;
import org.wsmo.mediator.*;
import org.wsmo.service.*;

import com.ontotext.wsmo4j.service.*;
import com.ontotext.wsmo4j.ontology.*;
import com.ontotext.wsmo4j.common.*;
import com.ontotext.wsmo4j.mediator.*;


public class WsmoFactoryImpl
    implements WsmoFactory {

    static EntityRegistry getReg() {
        return EntityRegistry.get();
    }

    public WsmoFactoryImpl(Map <String, Object> map) {
        getReg();
        Factory.getLocatorManager().addLocator(getReg());
    }

    public Goal createGoal(IRI id) {
        _validateIdentifier(id);
        Goal alredyUsed = (Goal)_prologCreate(id, Goal.class);
        if (alredyUsed != null) {
            IDReference ref = _checkNotResolved(alredyUsed);
            if (ref == null)
                return alredyUsed;
            ref.setDelegate(new GoalImpl(id));
            return alredyUsed;
        }
        alredyUsed = new GoalImpl(id);
        getReg().registerEntity(alredyUsed);
        return alredyUsed;
    }

    public WebService createWebService(IRI id) {
        _validateIdentifier(id);

        WebService alredyUsed = (WebService)_prologCreate(id, WebService.class);
        if (alredyUsed != null) {
            IDReference ref = _checkNotResolved(alredyUsed);
            if (ref == null)
                return alredyUsed;
            ref.setDelegate(new WebServiceImpl(id));
            return alredyUsed;
        }
        alredyUsed = new WebServiceImpl(id);
        getReg().registerEntity(alredyUsed);
        return alredyUsed;
    }

    public Capability createCapability(IRI id) {
        _validateIdentifier(id);
        Capability alredyUsed = (Capability)_prologCreate(id, Capability.class);
        if (alredyUsed != null) {
            IDReference ref = _checkNotResolved(alredyUsed);
            if (ref == null)
                return alredyUsed;
            ref.setDelegate(new CapabilityImpl(id));
            return alredyUsed;
        }
        alredyUsed = new CapabilityImpl(id);
        getReg().registerEntity(alredyUsed);
        return alredyUsed;
    }

    public Interface createInterface(IRI id) {
        _validateIdentifier(id);
        Interface alredyUsed = (Interface)_prologCreate(id, Interface.class);
        if (alredyUsed != null) {
            IDReference ref = _checkNotResolved(alredyUsed);
            if (ref == null)
                return alredyUsed;
            ref.setDelegate(new InterfaceImpl(id));
            return alredyUsed;
        }
        alredyUsed = new InterfaceImpl(id);
        getReg().registerEntity(alredyUsed);
        return alredyUsed;
    }

    public Ontology createOntology(IRI id) {
        _validateIdentifier(id);
        Ontology alredyUsed = (Ontology)_prologCreate(id, Ontology.class);
        if (alredyUsed != null) {
            IDReference ref = _checkNotResolved(alredyUsed);
            if (ref == null)
                return alredyUsed;
            ref.setDelegate(new OntologyImpl(id));
            return alredyUsed;
        }
        alredyUsed = new OntologyImpl(id);
        getReg().registerEntity(alredyUsed);
        return alredyUsed;
    }


    public OOMediator createOOMediator(IRI id) {
        OOMediator alredyUsed = (OOMediator)_prologCreate(id, OOMediator.class);
        if (alredyUsed != null) {
            IDReference ref = _checkNotResolved(alredyUsed);
            if (ref == null)
                return alredyUsed;
            ref.setDelegate(new OOMediatorImpl(id));
            return alredyUsed;
        }
        alredyUsed = new OOMediatorImpl(id);
        getReg().registerEntity(alredyUsed);
        return alredyUsed;
    }
    public WWMediator createWWMediator(IRI id) {
        WWMediator alredyUsed = (WWMediator)_prologCreate(id, WWMediator.class);
        if (alredyUsed != null) {
            IDReference ref = _checkNotResolved(alredyUsed);
            if (ref == null)
                return alredyUsed;
            ref.setDelegate(new WWMediatorImpl(id));
            return alredyUsed;
        }
        alredyUsed = new WWMediatorImpl(id);
        getReg().registerEntity(alredyUsed);
        return alredyUsed;
    }
    public WGMediator createWGMediator(IRI id) {
        WGMediator alredyUsed = (WGMediator)_prologCreate(id, WGMediator.class);
        if (alredyUsed != null) {
            IDReference ref = _checkNotResolved(alredyUsed);
            if (ref == null)
                return alredyUsed;
            ref.setDelegate(new WGMediatorImpl(id));
            return alredyUsed;
        }
        alredyUsed = new WGMediatorImpl(id);
        getReg().registerEntity(alredyUsed);
        return alredyUsed;
    }
    public GGMediator createGGMediator(IRI id) {
        GGMediator alredyUsed = (GGMediator)_prologCreate(id, GGMediator.class);
        if (alredyUsed != null) {
            IDReference ref = _checkNotResolved(alredyUsed);
            if (ref == null)
                return alredyUsed;
            ref.setDelegate(new GGMediatorImpl(id));
            return alredyUsed;
        }
        alredyUsed = new GGMediatorImpl(id);
        getReg().registerEntity(alredyUsed);
        return alredyUsed;
    }

    public Axiom createAxiom(Identifier id) {
        _validateIdentifier(id);
        Axiom alredyUsed = (Axiom)_prologCreate(id, Axiom.class);
        if (alredyUsed != null) {
            IDReference ref = _checkNotResolved(alredyUsed);
            if (ref == null)
                return alredyUsed;
            ref.setDelegate(new AxiomImpl(id));
            return alredyUsed;
        }
        alredyUsed = new AxiomImpl(id);
        getReg().registerEntity(alredyUsed);
        return alredyUsed;
    }

    public Concept createConcept(Identifier id) {
        _validateIdentifier(id);
        Concept alredyUsed = (Concept)_prologCreate(id, Concept.class);
        if (alredyUsed != null) {
            IDReference ref = _checkNotResolved(alredyUsed);
            if (ref == null)
                return alredyUsed;
            ref.setDelegate(new ConceptImpl(id, this));
            return alredyUsed;
        }
        alredyUsed = new ConceptImpl(id, this);
        getReg().registerEntity(alredyUsed);
        return alredyUsed;
    }

    public Relation createRelation(Identifier id) {
        _validateIdentifier(id);

        Relation alredyUsed = (Relation)_prologCreate(id, Relation.class);
        if (alredyUsed != null) {
            IDReference ref = _checkNotResolved(alredyUsed);
            if (ref == null)
                return alredyUsed;
            ref.setDelegate(new RelationImpl(id));
            return alredyUsed;
        }
        alredyUsed = new RelationImpl(id);
        getReg().registerEntity(alredyUsed);
        return alredyUsed;
    }

    public Instance createInstance(Identifier id) {
        _validateIdentifier(id);

        Instance alredyUsed = (Instance)_prologCreate(id, Instance.class);
        if (alredyUsed != null) {
            IDReference ref = _checkNotResolved(alredyUsed);
            if (ref == null)
                return alredyUsed;
            ref.setDelegate(new InstanceImpl(id));
            return alredyUsed;
        }
        alredyUsed = new InstanceImpl(id);
        getReg().registerEntity(alredyUsed);
        return alredyUsed;
    }

    //@todo: fix the exception that should be thrown
    /**
     * Method is not part of the interface. It's internally used by the ConceptImpl
     * to register the Attribute to the EntityRegistry
     * @param attribute to add to the list of the created objects
     */
    public void createAttribute(Attribute attribute) {
        _validateIdentifier(attribute.getIdentifier());
        Attribute alredyUsed = (Attribute)_prologCreate(attribute.getIdentifier(), Attribute.class);
        if (alredyUsed != null) {
            IDReference ref = _checkNotResolved(alredyUsed);
            if (ref == null)
                return;
            ref.setDelegate(attribute);
            return;
        }
        alredyUsed = attribute;
        getReg().registerEntity(alredyUsed);
        return;
    }

    public Instance createInstance(Identifier id, Concept concept)
        throws SynchronisationException, InvalidModelException {

        Instance result = createInstance(id);
        result.addConcept(concept);

        return result;
    }

    public RelationInstance createRelationInstance(Relation rel)
            throws SynchronisationException, InvalidModelException {
        Identifier anonId = createAnonymousID();
        return createRelationInstance(anonId, rel);
    }

    public RelationInstance createRelationInstance(Identifier id, Relation rel)
        throws SynchronisationException, InvalidModelException {
        _validateIdentifier(id);
        RelationInstance alredyUsed = (RelationInstance)_prologCreate(id, RelationInstance.class);
        if (alredyUsed != null) {
            IDReference ref = _checkNotResolved(alredyUsed);
            if (ref == null) {
                // The following lines fix an inconsistency exposed after ClearTopEntity#clearTopEntity().
                // The problem is caused by inability to untie the connection RelationInstance -> Relation
                // and this disables automatic adding the reverse connection on rebuilding.
                if (false == rel.listRelationInstances().contains(alredyUsed)) {
                    rel.addRelationInstance(alredyUsed);
                }
                return alredyUsed;
            }
            ref.setDelegate(new RelationInstanceImpl(id, rel));
            return alredyUsed;
        }
        alredyUsed = new RelationInstanceImpl(id, rel);
        getReg().registerEntity(alredyUsed);
        return alredyUsed;       
    }

    public Namespace createNamespace(String prefix, IRI iri) {
        if (null == iri) {
            throw new IllegalArgumentException("Illegal namespace URI supplied: null value ...");
        }

        return new NamespaceImpl(prefix, iri);

    }

    public IRI createIRI(String fullIRI) {

        if (null == fullIRI || 0 == fullIRI.trim().length()) {
            throw new IllegalArgumentException("Illegal IRI supplied: ["+ fullIRI +"] ...");
        }

        return new IRIImpl(fullIRI);
    }

    public IRI createIRI(Namespace ns, String localPart) {
        
        if (ns == null) {
            throw new IllegalArgumentException("Namespace not supplied: it can't be null.");
        }
        if (null == localPart 
                || 0 == localPart.trim().length()) {
            throw new IllegalArgumentException("Illegal local name supplied.");
        }
        StringBuffer rawIRI = new StringBuffer();
        String nsIRI = ns.getIRI().toString();
        rawIRI.append(nsIRI);
        if (false == nsIRI.endsWith("#")
                && false == nsIRI.endsWith("/")) {
            rawIRI.append('#');
        }
        rawIRI.append(localPart);
        return new IRIImpl(rawIRI.toString());
    }

    public UnnumberedAnonymousID createAnonymousID() {
        return new UnnumberedAnonymousIDImpl();
    }
    
    private void _validateIdentifier(Identifier id) {
        //@todo - add more checks, e.g. whether an entity with such ID already exists?
        if (null == id) {
            throw new IllegalArgumentException("Invalid ID supplied: null value ...");
        }
    }


    private Object _prologCreate(Identifier id, Class iface) {
        Object o = null;
        try {
            o = getReg().isRegistered(id, iface);
        } catch (RuntimeException rte) {
            rte.printStackTrace();
            throw new SynchronisationException(rte);
        }
        return o;
    }

    private IDReference _checkNotResolved(Entity obj) {
        if (! (obj instanceof Proxy) )
            return null;
        Object ref = Proxy.getInvocationHandler(obj);
        if (ref instanceof IDReference) {
            if (((IDReference)ref).isResolved())
                return null;
            return (IDReference)ref;
        }
        throw new SynchronisationException();
    }

    public Goal getGoal(IRI id) {
        Entity inst = null;
        try {
             inst = getReg().isRegistered(id, Goal.class);
            if (inst == null) {
                inst = (Entity)IDReference.createNewReference(id, new Class[]{Goal.class});
                getReg().registerEntity(inst);
            }
        } catch (RuntimeException rte) {
            throw new SynchronisationException(rte);
        }
        return (Goal)inst;
    }
    public WebService getWebService(IRI id) {
        Entity inst = null;
        try {
             inst = getReg().isRegistered(id, WebService.class);
            if (inst == null) {
                inst = (Entity)IDReference.createNewReference(id, new Class[]{WebService.class});
                getReg().registerEntity(inst);
            }
        } catch (RuntimeException rte) {
            throw new SynchronisationException(rte);
        }
        return (WebService)inst;
    }
    public Interface getInterface(IRI id) {
        Entity inst = null;
        try {
             inst = getReg().isRegistered(id, Interface.class);
            if (inst == null) {
                inst = (Entity)IDReference.createNewReference(id, new Class[]{Interface.class});
                getReg().registerEntity(inst);
            }
        } catch (RuntimeException rte) {
            throw new SynchronisationException(rte);
        }
        return (Interface)inst;
    }
    public Capability getCapability(IRI id) {
        Entity inst = null;
        try {
             inst = getReg().isRegistered(id, Capability.class);
            if (inst == null) {
                inst = (Entity)IDReference.createNewReference(id, new Class[]{Capability.class});
                getReg().registerEntity(inst);
            }
        } catch (RuntimeException rte) {
            throw new SynchronisationException(rte);
        }
        return (Capability)inst;
    }
    public Ontology getOntology(IRI id) {
        Entity inst = null;
        try {
             inst = getReg().isRegistered(id, Ontology.class);
            if (inst == null) {
                inst = (Entity)IDReference.createNewReference(id, new Class[]{Ontology.class});
                getReg().registerEntity(inst);
            }
        } catch (RuntimeException rte) {
            throw new SynchronisationException(rte);
        }
        return (Ontology)inst;
    }
    public OOMediator getOOMediator(IRI id) {
        Entity inst = null;
        try {
             inst = getReg().isRegistered(id, OOMediator.class);
            if (inst == null) {
                inst = (Entity)IDReference.createNewReference(id, new Class[]{OOMediator.class});
                getReg().registerEntity(inst);
            }
        } catch (RuntimeException rte) {
            throw new SynchronisationException(rte);
        }
        return (OOMediator)inst;
    }
    public WWMediator getWWMediator(IRI id) {
        Entity inst = null;
        try {
             inst = getReg().isRegistered(id, WWMediator.class);
            if (inst == null) {
                inst = (Entity)IDReference.createNewReference(id, new Class[]{WWMediator.class});
                getReg().registerEntity(inst);
            }
        } catch (RuntimeException rte) {
            throw new SynchronisationException(rte);
        }
        return (WWMediator)inst;
    }
    public WGMediator getWGMediator(IRI id) {
        Entity inst = null;
        try {
             inst = getReg().isRegistered(id, WGMediator.class);
            if (inst == null) {
                inst = (Entity)IDReference.createNewReference(id, new Class[]{WGMediator.class});
                getReg().registerEntity(inst);
            }
        } catch (RuntimeException rte) {
            throw new SynchronisationException(rte);
        }
        return (WGMediator)inst;
    }
    public GGMediator getGGMediator(IRI id) {
        Entity inst = null;
        try {
             inst = getReg().isRegistered(id, GGMediator.class);
            if (inst == null) {
                inst = (Entity)IDReference.createNewReference(id, new Class[]{GGMediator.class});
                getReg().registerEntity(inst);
            }
        } catch (RuntimeException rte) {
            throw new SynchronisationException(rte);
        }
        return (GGMediator)inst;
    }
    public Axiom getAxiom(Identifier id) {
        Entity inst = null;
        try {
             inst = getReg().isRegistered(id, Axiom.class);
            if (inst == null) {
                inst = (Entity)IDReference.createNewReference(id, new Class[]{Axiom.class});
                getReg().registerEntity(inst);
            }
        } catch (RuntimeException rte) {
            throw new SynchronisationException(rte);
        }
        return (Axiom)inst;
    }
    public Concept getConcept(Identifier id) {
        Entity inst = null;
        try {
             inst = getReg().isRegistered(id, Concept.class);
            if (inst == null) {
                inst = (Entity)IDReference.createNewReference(id, new Class[]{Concept.class});
                getReg().registerEntity(inst);
            }
        } catch (RuntimeException rte) {
            throw new SynchronisationException(rte);
        }
        return (Concept)inst;
    }
    public Instance getInstance(Identifier id) {
        Entity inst = null;
        try {
             inst = getReg().isRegistered(id, Instance.class);
            if (inst == null) {
                inst = (Entity)IDReference.createNewReference(id, new Class[]{Instance.class});
                getReg().registerEntity(inst);
            }

        } catch (RuntimeException rte) {
            throw new SynchronisationException(rte);
        }
        return (Instance)inst;
    }
    public Relation getRelation(Identifier id) {
        Entity inst = null;
        try {
             inst = getReg().isRegistered(id, Relation.class);
            if (inst == null) {
                inst = (Entity)IDReference.createNewReference(id, new Class[]{Relation.class});
                getReg().registerEntity(inst);
            }
        } catch (RuntimeException rte) {
            throw new SynchronisationException(rte);
        }
        return (Relation)inst;
    }
    public RelationInstance getRelationInstance(Identifier id) {
        Entity inst = null;
        try {
             inst = getReg().isRegistered(id, RelationInstance.class);
            if (inst == null) {
                inst = (Entity)IDReference.createNewReference(id, new Class[]{RelationInstance.class});
                getReg().registerEntity(inst);
            }
        } catch (RuntimeException rte) {
            throw new SynchronisationException(rte);
        }
        return (RelationInstance)inst;
    }

    /**
     * Return existing Varible instance
     * @param name The name of the var
     * @return existing var
     */
    public Variable getVariable(String name) {
        throw new UnsupportedOperationException();
    }

}