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

package com.ontotext.wsmo4j.service;

/**
 * An implementation of an interface representing a web service
 * capability.
 *
 * @author not attributable
 * @version $Revision: 1.16 $ $Date: 2007/04/02 12:13:21 $
 */

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.omwg.ontology.Axiom;
import org.omwg.ontology.Variable;
import org.wsmo.common.*;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.common.exception.SynchronisationException;
import org.wsmo.service.Capability;

import com.ontotext.wsmo4j.common.TopEntityImpl;


public class CapabilityImpl extends TopEntityImpl
        implements Capability {

    private LinkedHashMap <String, Variable> sharedVars;
    
    private LinkedHashMap <Identifier, Axiom> assumptions;
    private LinkedHashMap <Identifier, Axiom> preconditions;
    private LinkedHashMap <Identifier, Axiom> postconditions;
    private LinkedHashMap <Identifier, Axiom> effects;

    public CapabilityImpl(IRI capabilityIRI) {
        super(capabilityIRI);

        sharedVars = new LinkedHashMap <String, Variable> ();
        preconditions = new LinkedHashMap <Identifier, Axiom> ();
        postconditions = new LinkedHashMap <Identifier, Axiom> ();
        assumptions = new LinkedHashMap <Identifier, Axiom> ();
        effects = new LinkedHashMap <Identifier, Axiom> ();
    }

    /**
     * Adds a new shared variable to the list of
     * variables used by the logical expressions of this capability.
     * @param var The variable to be added
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeSharedVariable(Variable var)
     * @see #removeSharedVariable(String name)
     */
    public void addSharedVariable(Variable var)
            throws SynchronisationException, InvalidModelException {
        if (var == null) {
            throw new IllegalArgumentException();
        }
        sharedVars.put(var.getName(), var);
    }

    /**
     * Removes a shared variable from the list of
     * variables used by the logical expressions of this capability.
     * @param var The variable to be removed.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    public void removeSharedVariable(Variable var)
            throws SynchronisationException, InvalidModelException {
        if (var == null) {
            throw new IllegalArgumentException();
        }
        sharedVars.remove(var.getName());
    }

    /**
     * Removes a shared variable from the list of
     * variables used by the logical expressions of this capability.
     * @param name The name of the variable to be removed.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    public void removeSharedVariable(String name)
            throws SynchronisationException, InvalidModelException {
        if (name == null 
                || name.trim().length() == 0) {
            throw new IllegalArgumentException();
        }
        sharedVars.remove(name);
    }

    /**
     * returns the set of shared variables used by the logical expressions of this capability.
     * @return The set of shared variables
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see org.omwg.ontology.Variable
     */
    public Set <Variable> listSharedVariables()
            throws SynchronisationException {
        return new LinkedHashSet <Variable> (sharedVars.values());
    }

    /**
     * Adds a new post-condition to the list of
     * post-conditions associated with this capability.
     * @param axiom The new post-condition to be added
     * to the lsit of post-conditions associated with this
     * capability.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removePostCondition(Axiom axiom)
     * @see #removePostCondition(Identifier id)
     */
    public void addPostCondition(Axiom axiom)
            throws SynchronisationException, InvalidModelException {
        if (axiom == null) {
            throw new IllegalArgumentException();
        }
        postconditions.put(axiom.getIdentifier(), axiom);
    }

    /**
     * Removes a post-condition from the list of
     * post-conditions associated with this capability.
     * @param axiom The post-condition to be removed.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    public void removePostCondition(Axiom axiom)
            throws SynchronisationException, InvalidModelException {
        if (axiom == null) {
            throw new IllegalArgumentException();
        }
        postconditions.remove(axiom.getIdentifier());
    }

    /**
     * Removes a post-condition from the list of
     * post-conditions associated with this capability.
     * @param id The ID of post-condition to be removed.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    public void removePostCondition(Identifier id)
            throws SynchronisationException, InvalidModelException {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        postconditions.remove(id);
    }

    /**
     * Returns the list of post-conditions associated
     * with this capability.
     * @return The set of post-conditions
     * associated with this capability.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see org.omwg.ontology.Axiom
     */
    public Set <Axiom> listPostConditions()
            throws SynchronisationException {
        return new LinkedHashSet <Axiom> (postconditions.values());
    }

    /**
     * Adds a pre-condition to the list of pre-conditions
     * of this capability.
     * @param axiom The pre-condition to be added
     * to the list of pre-conditions of this capability.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removePreCondition(Axiom axiom)
     * @see #removePreCondition(Identifier id)
     */
    public void addPreCondition(Axiom axiom)
            throws SynchronisationException, InvalidModelException {
        if (axiom == null) {
            throw new IllegalArgumentException();
        }
        preconditions.put(axiom.getIdentifier(), axiom);
    }

    /**
     * Removes a pre-condition from the list of pre-conditions
     * associated with this capability.
     * @param axiom The pre-condition to be removed
     * from the list.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    public void removePreCondition(Axiom axiom)
            throws SynchronisationException, InvalidModelException {
        if (axiom == null) {
            throw new IllegalArgumentException();
        }
        preconditions.remove(axiom.getIdentifier());
    }

    /**
     * Removes a pre-condition from the list of pre-conditions
     * associated with this capability.
     * @param id The ID of the pre-condition to be removed
     * from the list.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    public void removePreCondition(Identifier id)
            throws SynchronisationException, InvalidModelException {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        preconditions.remove(id);
    }

    /**
     * Lists the pre-conditions of this capability.
     * @return The list of pre-conditions of this
     * capability.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see org.omwg.ontology.Axiom
     */
    public Set <Axiom>  listPreConditions()
            throws SynchronisationException {
        return new LinkedHashSet <Axiom> (preconditions.values());
    }

    /**
     * Adds an effect to the list of effects of this
     * capability.
     * @param axiom The effect to be added to the list
     * of effects of this capability.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeEffect(Axiom axiom)
     * @see #removeEffect(Identifier id)
     */
    public void addEffect(Axiom axiom)
            throws SynchronisationException, InvalidModelException {
        if (axiom == null) {
            throw new IllegalArgumentException();
        }
        effects.put(axiom.getIdentifier(), axiom);
    }

    /**
     * Removes an effect from the list of effects
     * associated with this capability.
     * @param axiom The effect to be removed from
     * the list.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    public void removeEffect(Axiom axiom)
            throws SynchronisationException, InvalidModelException {
        if (axiom == null) {
            throw new IllegalArgumentException();
        }
        effects.remove(axiom.getIdentifier());
    }

    /**
     * Removes an effect from the list of effects
     * associated with this capability.
     * @param id The ID of the effect to be removed from
     * the list.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    public void removeEffect(Identifier id)
            throws SynchronisationException, InvalidModelException {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        effects.remove(id);
    }

    /**
     * Lists the effects of this capability.
     * @return The list of effects that this capability
     * produces.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see org.omwg.ontology.Axiom
     */
    public Set <Axiom>  listEffects()
            throws SynchronisationException {
        return new LinkedHashSet <Axiom> (effects.values());
    }

    /**
     * Adds an assumption to the list of assumptions
     * associated with this capability.
     * @param axiom The new assumption to be added
     * to the list.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeAssumption(Axiom axiom)
     * @see #removeAssumption(Identifier id)
     */
    public void addAssumption(Axiom axiom)
            throws SynchronisationException, InvalidModelException {
        if (axiom == null) {
            throw new IllegalArgumentException();
        }
        assumptions.put(axiom.getIdentifier(), axiom);
    }

    /**
     * Removes an assumption from the list of assumptions
     * associated with this capability.
     * @param axiom The assumption to be removed
     * from the list of assumtions.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    public void removeAssumption(Axiom axiom)
            throws SynchronisationException, InvalidModelException {
        if (axiom == null) {
            throw new IllegalArgumentException();
        }
        assumptions.remove(axiom.getIdentifier());
    }

    /**
     * Removes an assumption from the list of assumptions
     * associated with this capability.
     * @param id The ID of the assumption to be removed
     * from the list of assumtions.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    public void removeAssumption(Identifier id)
            throws SynchronisationException, InvalidModelException {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        assumptions.remove(id);
    }

    /**
     * Lists the assumptions associated with
     * this capability.
     * @return The list of assumptions associated
     * with this capability.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see org.omwg.ontology.Axiom
     */
    public Set <Axiom>  listAssumptions()
            throws SynchronisationException {
        return new LinkedHashSet <Axiom> (assumptions.values());
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true; // instance match
        }
        if (object == null 
                || false == object instanceof Capability) {
            return false;
        }
        return super.equals(object);
    }
}

/*
* $Log: CapabilityImpl.java,v $
* Revision 1.16  2007/04/02 12:13:21  morcen
* Generics support added to wsmo-api, wsmo4j and wsmo-test
*
* Revision 1.15  2006/02/13 10:41:05  vassil_momtchev
* the constructors of the topentities to disallow Identifier; see WsmoFactoryImpl
*
* Revision 1.14  2005/06/22 09:16:06  alex_simov
* Simplified equals() method. Identity strongly relies on identifier string
*
* Revision 1.13  2005/06/01 12:14:23  marin_dimitrov
* v0.4.0
*
* Revision 1.2  2005/05/17 12:01:49  alex
* equals() fixed to compare compatible collection types
*
* Revision 1.1  2005/05/13 09:27:03  alex
* initial commit
*
*/

