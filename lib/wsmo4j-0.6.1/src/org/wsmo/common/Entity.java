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

package org.wsmo.common;


import java.util.*;

import org.wsmo.common.exception.*;
import org.omwg.ontology.*;

/**
 * Base class for all WSMO entities.
 *
 * @author not attributable
 * @version $Revision: 1.12 $ $Date: 2007/04/02 12:13:14 $
 */
public interface Entity {
    /**
     * Returns the list of values associated with the specified key.
     * @param key The key (IRI) of interest.
     * @return A list of values associated with the specified key.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see NFP
     */
    Set <Object> listNFPValues(IRI key)
            throws SynchronisationException;

    /**
     * Returns all the NFP with their values as java.util.Map.
     * The Map entries are (key, set-of-values-for-that-key)
     * @return A Map holding the NFPs and values
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see NFP
     */

    Map <IRI, Set <Object>> listNFPValues()
            throws SynchronisationException;

    /**
     * Adds a value to the list of values associated with this key.
     * @param key The key of interest.
     * @param value Identifier or DataValue to be added to the list of values associated with this key.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see NFP
     */

    void addNFPValue(IRI key, Identifier value)
            throws SynchronisationException, InvalidModelException;

    /**
     * Adds a value to the list of values associated with this key.
     * @param key The key of interest.
     * @param value Identifier or DataValue to be added to the list of values associated with this key.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see NFP
     */

    void addNFPValue(IRI key, Value value)
            throws SynchronisationException, InvalidModelException;

    /**
     * Removes a value from the list of values associated with a specific key.
     * @param key The key of interest.
     * @param value Identifier or DataValue to be removed from the list of values associated with the specified key.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see NFP
     */
    void removeNFPValue(IRI key, Identifier value)
            throws SynchronisationException, InvalidModelException;

    /**
     * Removes a value from the list of values associated with a specific key.
     * @param key The key of interest.
     * @param value Identifier or DataValue to be removed from the list of values associated with the specified key.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see NFP
     */
    void removeNFPValue(IRI key, Value value)
            throws SynchronisationException, InvalidModelException;

    /**
     * Removes a NFP (e.g. all key/value pairs) associated with a specific key.
     * @param key The key of interest.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see NFP
     */
    void removeNFP(IRI key)
            throws SynchronisationException, InvalidModelException;

    /**
     * Returns the ID of this entity.
     * @return The ID of this entity.
     */
    Identifier getIdentifier();

// DO NOT EDIT below this line

    /**
     * @supplierCardinality 1
     * @supplierRole identifiedBy
     * @directed*/
    /*# Identifier lnkIdentifier; */
}
/*
 * $Log: Entity.java,v $
 * Revision 1.12  2007/04/02 12:13:14  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.11  2005/09/27 13:10:24  marin_dimitrov
 * javadoc
 *
 * Revision 1.10  2005/09/16 12:40:39  marin_dimitrov
 * 1. Entity::addNfpValue(IRI, Object) split into Entity::addNfpValue(IRI, Identifier) and Entity::addNfpValue(IRI, Value)
 *
 * 2. same for removeNFP
 *
 * Revision 1.9  2005/08/31 12:28:37  vassil_momtchev
 * addNFPValue and removeNFPValue changed back to accept Object. The Value type limits all kind of identifiers (IRI, AnonymousID)
 *
 * Revision 1.8  2005/08/31 09:16:43  vassil_momtchev
 * use Type and Value instead of Object where appropriate bug SF[1276677]
 *
 * Revision 1.7  2005/08/19 08:57:49  marin_dimitrov
 * added removeNFP(key)
 *
 * Revision 1.6  2005/06/01 10:30:48  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.3  2005/05/12 13:29:15  marin
 * javadoc, header, footer, etc
 *
 */
