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


import java.util.*;
import org.wsmo.common.*;
import org.wsmo.service.*;
import com.ontotext.wsmo4j.common.*;


/**
* An implementation of ServiceDescroption interface representing a service description 
* (e.g. a Goal or a WebService).
*
* @author not attributable
* @version $Revision: 1.3 $ $Date: 2007/04/02 12:13:21 $
* @since 0.4.0
*/
public abstract class ServiceDescriptionImpl extends TopEntityImpl 
        implements ServiceDescription {

    private Capability capability;
    private LinkedHashMap <Identifier, Interface> ifaces;
    
    public ServiceDescriptionImpl(Identifier id) {
        super(id);
        ifaces = new LinkedHashMap <Identifier, Interface> ();
    }
    
    /**
     * Adds a new interface to the list of interfaces of this WebService/Goal.
     * @param iface The new interface to be added to the list.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    public void addInterface(Interface iface) {
        if (iface == null) {
            throw new IllegalArgumentException();
        }
        ifaces.put(iface.getIdentifier(), iface);
    }
    
    /**
     * Removes the specified interface from the list of iterfaces of this WebService/Goal.
     * @param iface The interface to be removed from the list of interfacaes.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    public void removeInterface(Interface iface) {
        if (iface == null) {
            throw new IllegalArgumentException();
        }
        ifaces.remove(iface.getIdentifier());
    }
    
    /**
     * Removes the specified interface from the list of iterfaces of this WebService/Goal.
     * @param id The ID of the interface to be removed from the list of interfacaes.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    public void removeInterface(Identifier id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        ifaces.remove(id);
    }
    
    /**
     * Lists the interfaces of this WebService/Goal.
     * @return A set of the interfaces of this WebService/Goal
     * @throws org.wsmo.common.exception.SynchronisationException
     */
    public Set <Interface> listInterfaces() {
        return new LinkedHashSet <Interface> (ifaces.values());
    }
    
    /**
     * Returns the capability of this WebService/Goal.
     * @return The capability associated with this WebService/Goal.
     * @see #setCapability
     * @throws org.wsmo.common.exception.SynchronisationException
     */
    public Capability getCapability() {
        return this.capability;
    }
    
    /**
     * Sets the capability of this WebService/Goal.
     * @param cap The new capability to be associated with this WebService/Goal
     * @see #getCapability
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    public void setCapability(Capability cap) {
        this.capability = cap;
    }
    
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null
                || false == object instanceof ServiceDescription) {
            return false;
        }
        return super.equals(object);
    }
}

/*
* $Log: ServiceDescriptionImpl.java,v $
* Revision 1.3  2007/04/02 12:13:21  morcen
* Generics support added to wsmo-api, wsmo4j and wsmo-test
*
* Revision 1.2  2005/06/22 09:16:06  alex_simov
* Simplified equals() method. Identity strongly relies on identifier string
*
* Revision 1.1  2005/06/01 12:14:05  marin_dimitrov
* v0.4.0
*
* Revision 1.1  2005/05/13 08:33:29  alex
* initial commit
*
*/
