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

package org.wsmo.service;


import java.util.*;

import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;


/**
 * An interface representing a web service
 * capability.
 *
 * @author not attributable
 * @version $Revision: 1.10 $ $Date: 2007/04/02 12:13:14 $
 */

public interface Capability
    extends TopEntity {

    /**
     * Adds a new shared variable to the list of
     * variables used by the logical expressions of this capability.
     * @param var The variable to be added
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeSharedVariable(Variable var)
     * @see #removeSharedVariable(String name)
     */
    void addSharedVariable(Variable var)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes a shared variable from the list of
     * variables used by the logical expressions of this capability.
     * @param var The variable to be removed.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeSharedVariable(String name)
     */
    void removeSharedVariable(Variable var)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes a shared variable from the list of
     * variables used by the logical expressions of this capability.
     * @param name The name of the variable to be removed.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeSharedVariable(Variable var)
     */
    void removeSharedVariable(String name)
        throws SynchronisationException, InvalidModelException;

    /**
     * returns the set of shared variables used by the logical expressions of this capability.
     * @return The set of shared variables
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see org.omwg.ontology.Variable
     */
    Set <Variable> listSharedVariables()
        throws SynchronisationException;

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
    void addPostCondition(Axiom axiom)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes a post-condition from the list of
     * post-conditions associated with this capability.
     * @param axiom The post-condition to be removed.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removePostCondition(Identifier id)
     */
    void removePostCondition(Axiom axiom)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes a post-condition from the list of
     * post-conditions associated with this capability.
     * @param id The ID of post-condition to be removed.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removePostCondition(Axiom axiom)
     */
    void removePostCondition(Identifier id)
        throws SynchronisationException, InvalidModelException;

    /**
     * Returns the list of post-conditions associated
     * with this capability.
     * @return The set of post-conditions
     * associated with this capability.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see org.omwg.ontology.Axiom
     */
    Set <Axiom> listPostConditions()
        throws SynchronisationException;

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
    void addPreCondition(Axiom axiom)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes a pre-condition from the list of pre-conditions
     * associated with this capability.
     * @param axiom The pre-condition to be removed
     * from the list.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removePreCondition(Identifier id)
     */
    void removePreCondition(Axiom axiom)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes a pre-condition from the list of pre-conditions
     * associated with this capability.
     * @param id The ID of the pre-condition to be removed
     * from the list.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removePreCondition(Axiom axiom)
     */
    void removePreCondition(Identifier id)
        throws SynchronisationException, InvalidModelException;

    /**
     * Lists the pre-conditions of this capability.
     * @return The list of pre-conditions of this
     * capability.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see org.omwg.ontology.Axiom
     */
    Set <Axiom> listPreConditions()
        throws SynchronisationException;

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
    void addEffect(Axiom axiom)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes an effect from the list of effects
     * associated with this capability.
     * @param axiom The effect to be removed from
     * the list.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeEffect(Identifier id)
     */
    void removeEffect(Axiom axiom)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes an effect from the list of effects
     * associated with this capability.
     * @param id The ID of the effect to be removed from
     * the list.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeEffect(Axiom axiom)
     */
    void removeEffect(Identifier id)
        throws SynchronisationException, InvalidModelException;

    /**
     * Lists the effects of this capability.
     * @return The list of effects that this capability
     * produces.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see org.omwg.ontology.Axiom
     */
    Set <Axiom> listEffects()
        throws SynchronisationException;

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
    void addAssumption(Axiom axiom)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes an assumption from the list of assumptions associated with this capability.
     * @param axiom The assumption to be removed from the list of assumtions.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeAssumption(Identifier id)
     */
    void removeAssumption(Axiom axiom)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes an assumption from the list of assumptions
     * associated with this capability.
     * @param id The ID of the assumption to be removed
     * from the list of assumtions.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeAssumption(Axiom axiom)
     */
    void removeAssumption(Identifier id)
        throws SynchronisationException, InvalidModelException;

    /**
     * Lists the assumptions associated with
     * this capability.
     * @return The list of assumptions associated
     * with this capability.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see org.omwg.ontology.Axiom
     */
    Set <Axiom> listAssumptions()
        throws SynchronisationException;
}

/*
 * $Log: Capability.java,v $
 * Revision 1.10  2007/04/02 12:13:14  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.9  2005/06/01 10:59:58  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.8  2005/05/13 12:47:06  marin
 * more @see tags
 *
 * Revision 1.7  2005/05/13 09:24:38  alex
 * listSharedVariables() does not throw InvalidModelException anymore
 *
 * Revision 1.6  2005/05/12 14:43:43  marin
 * added @see links
 *
 * Revision 1.5  2005/05/12 10:11:05  marin
 * added javadocs, proper formatting, etc
 *
 */
