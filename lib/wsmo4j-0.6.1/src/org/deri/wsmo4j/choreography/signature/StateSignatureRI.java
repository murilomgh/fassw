/*
 wsmo4j extension - a Choreography API and Reference Implementation

 Copyright (c) 2005, University of Innsbruck, Austria

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

package org.deri.wsmo4j.choreography.signature;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.omwg.ontology.Ontology;
import org.wsmo.common.IRI;
import org.wsmo.common.Identifier;
import org.wsmo.mediator.OOMediator;
import org.wsmo.service.signature.*;

import com.ontotext.wsmo4j.common.EntityImpl;

/**
 * Reference implementation for the State Signature.
 * 
 * <pre>
 *    Created on Jul 26, 2005
 *    Committed by $Author: vassil_momtchev $
 *    $Source: /cvsroot/wsmo4j/ext/choreography/src/ri/org/deri/wsmo4j/choreography/signature/StateSignatureRI.java,v $
 * </pre>
 * 
 * @author Thomas Haselwanter
 * @author James Scicluna
 * 
 * @version $Revision: 1.13 $ $Date: 2006/10/24 14:11:47 $
 */
public class StateSignatureRI extends EntityImpl implements StateSignature {

    /**
     * @uml.property   name="inModeDefinition"
     * @uml.associationEnd   multiplicity="(0 -1)" elementType="org.wsmo.service.signature.In"
     */
    protected Set<In> inModeDefinition;

    /**
     * @uml.property   name="outModeDefinition"
     * @uml.associationEnd   multiplicity="(0 -1)" elementType="org.wsmo.service.signature.Out"
     */
    protected Set<Out> outModeDefinition;

    /**
     * @uml.property   name="sharedModeDefinition"
     * @uml.associationEnd   multiplicity="(0 -1)" elementType="org.wsmo.service.signature.Shared"
     */
    protected Set<Shared> sharedModeDefinition;

    /**
     * @uml.property   name="staticModeDefinition"
     * @uml.associationEnd   multiplicity="(0 -1)" elementType="org.wsmo.service.signature.Static"
     */
    protected Set<Static> staticModeDefinition;

    /**
     * @uml.property   name="controlledModeDefinition"
     * @uml.associationEnd   multiplicity="(0 -1)" elementType="org.wsmo.service.signature.Controlled"
     */
    protected Set<Controlled> controlledModeDefinition;

    private LinkedHashMap<Identifier, Ontology> ontologies;
    
    private LinkedHashMap<Identifier, OOMediator> mediators;

    /**
     * Constructor initializing a state signature with an IRI and empty sets of
     * modes
     * 
     * @param id
     *            An IRI object defining the identifier of the state signature
     */
    public StateSignatureRI(Identifier id) {
        this(id, new HashSet<In>(), new HashSet<Out>(), new HashSet<Shared>(),
                new HashSet<Static>(), new HashSet<Controlled>());
        this.mediators = new LinkedHashMap<Identifier,OOMediator>();
    }

    /**
     * Constructor initializing a state signature with an IRI and a set of modes
     * 
     * @param id
     *            An IRI object defining the identifier of the state signature
     * @param modes
     *            A Set of Mode objects which define the modes in the state
     *            signature.
     */
    public StateSignatureRI(Identifier id, Set<Mode> modes) {
        this(id, new HashSet<In>(), new HashSet<Out>(), new HashSet<Shared>(),
                new HashSet<Static>(), new HashSet<Controlled>());
        for (Mode mode : modes)
            addMode(mode);
    }

    /**
     * Constructor initializing a state signature with an IRI and 5 sets of the
     * specific types of modes
     * 
     * @param id
     *            An IRI object defining the identifier of the state signature
     * @param inModeDefinition
     *            A Set of In mode definitions
     * @param outModeDefinition
     *            A Set of Out mode definitions
     * @param sharedModeDefinition
     *            A Set of Shared mode definitions
     * @param staticModeDefinition
     *            A Set of Static mode definitions
     * @param controlledModeDefinition
     *            A Set of Controlled mode definitions
     */
    public StateSignatureRI(Identifier id, Set<In> inModeDefinition, Set<Out> outModeDefinition,
            Set<Shared> sharedModeDefinition, Set<Static> staticModeDefinition,
            Set<Controlled> controlledModeDefinition) {
        super(id);
        if ((this.inModeDefinition = inModeDefinition) == null)
            this.inModeDefinition = new HashSet<In>();
        if ((this.outModeDefinition = outModeDefinition) == null)
            this.outModeDefinition = new HashSet<Out>();
        if ((this.sharedModeDefinition = sharedModeDefinition) == null)
            this.sharedModeDefinition = new HashSet<Shared>();
        if ((this.staticModeDefinition = staticModeDefinition) == null)
            this.staticModeDefinition = new HashSet<Static>();
        if ((this.controlledModeDefinition = controlledModeDefinition) == null)
            this.controlledModeDefinition = new HashSet<Controlled>();
        this.ontologies = new LinkedHashMap<Identifier, Ontology>();
        // this.setupModeSet();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.signature.StateSignature#listInModes()
     */
    public Set<In> listInModes() {
        return Collections.unmodifiableSet(inModeDefinition);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.signature.StateSignature#listOutModes()
     */
    public Set<Out> listOutModes() {
        return Collections.unmodifiableSet(outModeDefinition);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.signature.StateSignature#listSharedModes()
     */
    public Set<Shared> listSharedModes() {
        return Collections.unmodifiableSet(sharedModeDefinition);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.signature.StateSignature#listStaticModes()
     */
    public Set<Static> listStaticModes() {
        return Collections.unmodifiableSet(staticModeDefinition);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.signature.StateSignature#listControlledModes()
     */
    public Set<Controlled> listControlledModes() {
        return Collections.unmodifiableSet(controlledModeDefinition);
    }

    /**
     * Adds a mode of type In
     * 
     * @param mode
     *            An In mode object
     */
    public void add(In mode) {
        inModeDefinition.add(mode);
    }

    /**
     * Removes an In mode from the state signature
     * 
     * @param mode
     *            An In mode object to be removed
     */
    public void remove(In mode) {
        inModeDefinition.remove(mode);
    }

    /**
     * Adds a mode of type Out
     * 
     * @param mode
     *            An Out mode object
     */
    public void add(Out mode) {
        outModeDefinition.add(mode);
    }

    /**
     * Removes an Out mode from the state signature
     * 
     * @param mode
     *            An Out mode object to be removed
     */
    public void remove(Out mode) {
        outModeDefinition.remove(mode);
    }

    /**
     * Adds a mode of type Shared
     * 
     * @param mode
     *            An Shared mode object
     */
    public void add(Shared mode) {
        sharedModeDefinition.add(mode);
    }

    /**
     * Removes an Out mode from the state signature
     * 
     * @param mode
     *            An Out mode object to be removed
     */
    public void remove(Shared mode) {
        sharedModeDefinition.remove(mode);
    }

    /**
     * Adds a mode of type Static
     * 
     * @param mode
     *            An Static mode object
     */
    public void add(Static mode) {
        staticModeDefinition.add(mode);
    }

    /**
     * Removes an Out mode from the state signature
     * 
     * @param mode
     *            An Out mode object to be removed
     */
    public void remove(Static mode) {
        staticModeDefinition.remove(mode);
    }

    /**
     * Adds a mode of type Controlled
     * 
     * @param mode
     *            An Controlled mode object
     */
    public void add(Controlled mode) {
        controlledModeDefinition.add(mode);
    }

    /**
     * Removes an Out mode from the state signature
     * 
     * @param mode
     *            An Out mode object to be removed
     */
    public void remove(Controlled mode) {
        controlledModeDefinition.remove(mode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.signature.StateSignature#add(org.wsmo.service.choreography.signature.Mode)
     */
    public void addMode(Mode mode) {
        if (mode instanceof In)
            add((In) mode);
        if (mode instanceof Out)
            add((Out) mode);
        if (mode instanceof Shared)
            add((Shared) mode);
        if (mode instanceof Static)
            add((Static) mode);
        if (mode instanceof Controlled)
            add((Controlled) mode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.signature.StateSignature#removeMode(org.wsmo.service.choreography.signature.Mode)
     */
    public void removeMode(Mode mode) {
        if (mode instanceof In)
            remove((In) mode);
        if (mode instanceof Out)
            remove((Out) mode);
        if (mode instanceof Shared)
            remove((Shared) mode);
        if (mode instanceof Static)
            remove((Static) mode);
        if (mode instanceof Controlled)
            remove((Controlled) mode);

    }

    /**
     * Adds a set of modes to the respective containers
     * 
     * @param modes
     *            A set of Mode objects to be added to the state signature
     */
    public void add(Set<Mode> modes) {
        for (Mode mode : modes)
            addMode(mode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.signature.StateSignature#addOntology(org.omwg.ontology.Ontology)
     */
    public void addOntology(Ontology ontology) {
        if (ontology == null) {
            throw new IllegalArgumentException();
        }
        ontologies.put(ontology.getIdentifier(), ontology);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.signature.StateSignature#removeOntology(org.wsmo.common.IRI)
     */
    public void removeOntology(IRI iri) {
        if (iri == null) {
            throw new IllegalArgumentException();
        }
        ontologies.remove(iri);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.signature.StateSignature#removeOntology(org.omwg.ontology.Ontology)
     */
    public void removeOntology(Ontology ontology) {
        if (ontology == null) {
            throw new IllegalArgumentException();
        }
        ontologies.remove(ontology.getIdentifier());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.signature.StateSignature#listOntologies()
     */
    public Set<Ontology> listOntologies() {
        return new LinkedHashSet<Ontology>(ontologies.values());
    }

    public void addMediator(OOMediator mediator) {
        if (mediator == null) {
            throw new IllegalArgumentException();
        }
        mediators.put(mediator.getIdentifier(), mediator);
    }

    public void removeMediator(OOMediator mediator) {
        if (mediator == null) {
            throw new IllegalArgumentException();
        }
        mediators.remove(mediator.getIdentifier());
    }

    public void removeMediator(IRI iri) {
        if (iri == null) {
            throw new IllegalArgumentException();
        }
        mediators.remove(iri);
    }


    public Set listMediators() {
        return new LinkedHashSet<OOMediator>(mediators.values());
    }

    public Iterator<Mode> iterator() {
        HashSet<Mode> allModes = new HashSet<Mode>();
        allModes.addAll(this.inModeDefinition);
        allModes.addAll(this.outModeDefinition);
        allModes.addAll(this.sharedModeDefinition);
        allModes.addAll(this.staticModeDefinition);
        allModes.addAll(this.controlledModeDefinition);
        return allModes.iterator();
    }
}