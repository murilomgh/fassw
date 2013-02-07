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

import org.wsmo.common.*;


/**
 * An interface representing a service description (e.g. a Goal or a WebService).
 *
 * @author not attributable
 * @version $Revision: 1.2 $ $Date: 2007/04/02 12:13:14 $
 * @since 0.4.0
 */
public interface ServiceDescription
    extends TopEntity {

    /**
     * Adds a new interface to the list of interfaces of this WebService/Goal.
     * @param iface The new interface to be added to the list.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeInterface(Interface iface)
     * @see #removeInterface(Identifier id)
     */
    void addInterface(Interface iface);

    /**
     * Removes the specified interface from the list of iterfaces of this WebService/Goal.
     * @param iface The interface to be removed from the list of interfacaes.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeInterface(Identifier id)
     */
    void removeInterface(Interface iface);

    /**
     * Removes the specified interface from the list of iterfaces of this WebService/Goal.
     * @param id The ID of the interface to be removed from the list of interfacaes.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeInterface(Interface iface)
     */
    void removeInterface(Identifier id);

    /**
     * Lists the interfaces of this WebService/Goal.
     * @return A set of the interfaces of this WebService/Goal
     * @throws org.wsmo.common.exception.SynchronisationException
     */
    Set <Interface> listInterfaces();

    /**
     * Returns the capability of this WebService/Goal.
     * @return The capability associated with this WebService/Goal.
     * @see #setCapability
     * @throws org.wsmo.common.exception.SynchronisationException
     */
    Capability getCapability();

    /**
     * Sets the capability of this WebService/Goal.
     * @param cap The new capability to be associated with this WebService/Goal
     * @see #getCapability
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    void setCapability(Capability cap);

// DO NOT EDIT below this line

    /**
     * @link aggregation
     * @supplierCardinality 0..1
     * @supplierQualifier has-capability
     * @clientCardinality 0..*
     * @directed
     */
    /*# Capability lnkCapability; */

    /**
     * @link aggregation
     * @directed
     * @supplierCardinality 0..*
     * @supplierQualifier has-interface
     * @clientCardinality 0..*
     */
    /*# Interface lnkInterface; */
}

/*
 * $Log: ServiceDescription.java,v $
 * Revision 1.2  2007/04/02 12:13:14  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.1  2005/06/02 12:20:09  alex_simov
 * no message
 *
 * Revision 1.6  2005/05/13 12:45:38  marin
 * more @see tags
 *
 * Revision 1.5  2005/05/12 14:43:56  marin
 * added @since tags
 *
 * Revision 1.4  2005/05/12 10:11:05  marin
 * added javadocs, proper formatting, etc
 *
 */
