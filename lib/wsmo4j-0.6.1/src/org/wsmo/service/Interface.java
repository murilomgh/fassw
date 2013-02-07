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


import org.wsmo.common.*;


/**
 * This java interface represents a WSMO
 * web service interface.
 *
 * @author not attributable
 * @version $Revision: 1.9 $ $Date: 2005/06/01 10:59:58 $
 */
public interface Interface
    extends TopEntity {

    /**
     * Returns the orchestration of this interface.
     * @return The orchestration of this interface.
     * @see #setOrchestration(org.wsmo.service.Orchestration)
     * @throws org.wsmo.common.exception.SynchronisationException
     */
    Orchestration getOrchestration();

    /**
     * Sets the orchestration of this interface.
     * @param orch The new orchestration.
     * @see #getOrchestration()
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    void setOrchestration(Orchestration orch);

    /**
     * Returns the choreography of this interface.
     * @return The choreography of this interface.
     * @see #setChoreography(org.wsmo.service.Choreography)
     * @throws org.wsmo.common.exception.SynchronisationException
     */
    Choreography getChoreography();

    /**
     * Sets the choreography of the this interface.
     * @param chor A reference to the new choreography.
     * @see #getChoreography()
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    void setChoreography(Choreography chor);

    /**
     * Creates a new Choreography object.
     * @param id The ID of the new Choreography object
     * @return The newly created Choreography object.
     */
    Choreography createChoreography(Identifier id);

    /**
     * Creates a new Orchestration object.
     * @param id The ID of the new Orchestration object.
     * @return The newly created Orchestration object.
     */
    Orchestration createOrchestration(Identifier id);

// DO NOT EDIT below this line

    /**
     * @link aggregation
     * @supplierCardinality 0..1
     * @supplierQualifier has-orch
     * @clientCardinality 0..*
     * @directed
     */
    /*# Orchestration lnkOrchestration; */

    /**
     * @link aggregation
     * @directed
     * @supplierCardinality 0..1
     * @supplierQualifier has-chor
     * @clientCardinality 0..*
     */
    /*# Choreography lnkChoreography; */
}


/*
 * $Log: Interface.java,v $
 * Revision 1.9  2005/06/01 10:59:58  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.4  2005/05/12 10:11:05  marin
 * added javadocs, proper formatting, etc
 *
 */

