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


import org.wsmo.common.*;
import org.wsmo.service.Choreography;
import org.wsmo.service.Interface;
import org.wsmo.service.Orchestration;

import com.ontotext.wsmo4j.common.TopEntityImpl;

/**
 * This java interface represents a WSMO
 * web service interface.
 *
 * @author not attributable
 * @version $Revision: 1.16 $ $Date: 2006/02/13 10:41:05 $
 */

public class InterfaceImpl extends TopEntityImpl
        implements Interface {

    private Choreography chor;
    private Orchestration orch;
    
    public InterfaceImpl(IRI interfaceIRI) {
        super(interfaceIRI);
    }
    
    /**
     * Returns the orchestration of this interface.
     * @return The orchestration of this interface.
     * @see #setOrchestration(org.wsmo.service.Orchestration)
     * @throws org.wsmo.common.exception.SynchronisationException
     */
    public Orchestration getOrchestration() {
        return orch;
    }

    /**
     * Sets the orchestration of this interface.
     * @param orch The new orchestration.
     * @see #getOrchestration()
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    public void setOrchestration(Orchestration orch) {
        this.orch = orch; 
    }

    /**
     * Returns the choreography of this interface.
     * @return The choreography of this interface.
     * @see #setChoreography(org.wsmo.service.Choreography)
     * @throws org.wsmo.common.exception.SynchronisationException
     */
    public Choreography getChoreography() {
        return chor;
    }

    /**
     * Sets the choreography of the this interface.
     * @param chor A reference to the new choreography.
     * @see #getChoreography()
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    public void setChoreography(Choreography chor) {
        this.chor = chor;
    }

    /**
     * Creates a new Choreography object.
     * @param id The ID of the new Choreography object
     * @return The newly created Choreography object.
     */
    public Choreography createChoreography(Identifier id) {
        ChoreographyImpl chor = new ChoreographyImpl(id);
        this.chor = chor;
        return chor;
//        throw new RuntimeException("Method not implemented! - createChoreography");
    }

    /**
     * Creates a new Orchestration object.
     * @param id The ID of the new Orchestration object.
     * @return The newly created Orchestration object.
     */
    public Orchestration createOrchestration(Identifier id) {
        OrchestrationImpl orch = new OrchestrationImpl(id);
        this.orch = orch;
        return orch;
//        throw new RuntimeException("Method not implemented! - createOrchestration");
    }
    
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null
                || false == object instanceof Interface) {
            return false;
        }
        return super.equals(object);
    }
}

/*
 * $Log: InterfaceImpl.java,v $
 * Revision 1.16  2006/02/13 10:41:05  vassil_momtchev
 * the constructors of the topentities to disallow Identifier; see WsmoFactoryImpl
 *
 * Revision 1.15  2005/10/07 13:55:51  vassil_momtchev
 * createXXX(Identifier) set the result to chor/orch hanlde; fix of 1315669
 *
 * Revision 1.14  2005/06/22 09:16:06  alex_simov
 * Simplified equals() method. Identity strongly relies on identifier string
 *
 * Revision 1.13  2005/06/06 17:34:06  alex_simov
 * no message
 *
 * Revision 1.12  2005/06/01 12:14:23  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.1  2005/05/13 12:34:06  alex
 * initial commit
 *
 */

