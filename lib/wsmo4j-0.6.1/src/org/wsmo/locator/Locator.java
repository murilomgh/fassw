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

package org.wsmo.locator;

import java.util.*;

import org.wsmo.common.*;
import org.wsmo.common.exception.*;


/**
 * An interface representing an entity locator.
 *
 * @author not attributable
 * @version $Revision: 1.7 $ $Date: 2007/04/02 12:13:15 $
 */
public interface Locator {

	public static final String URI_MAPPING = "Mapping logical URI to physical location identifier";
	
    /**
     * Attempts to find an entity based on its IRI. If metamodelling is used (e.g. an extension of WSML-Core)
     * then more than one entity may correspond to the provied IRI.
     * @param id The ID of the entity to find
     * @return The entity(ies) corresponding to the supplied ID or <i>null</i> if none found
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see Entity
     */
    Set <Entity> lookup(Identifier id)
            throws SynchronisationException;

    /**
     * Attempts to find an entity based on its IRI and the type of entity of interest.
     * Note that the class name is the WSMO API interface name and <b>not</b> the name of a
     * particular class implementing the WSMO API interface.
     * @param id The ID of the entity to find
     * @param clazz the type of entity (e.g. the name of the WSMO API interface name)
     * @return The entity corresponding to the supplied ID or <i>null</i> if none found
     * @throws org.wsmo.common.exception.SynchronisationException
     */
    Entity lookup(Identifier id, Class clazz)
            throws SynchronisationException;

}
/*
 * $Log: Locator.java,v $
 * Revision 1.7  2007/04/02 12:13:15  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.6  2006/08/22 16:25:48  nathaliest
 * added locator implementation and set it as default locator at the locator manager
 *
 * Revision 1.5  2005/10/18 09:16:47  marin_dimitrov
 * lookup() returns a Set (was Entity[])
 *
 * Revision 1.4  2005/10/18 09:13:01  marin_dimitrov
 * lookup() returns a Set (was Entity[])
 *
 * Revision 1.3  2005/10/17 13:14:44  marin_dimitrov
 * metamodeliing extensions
 *
 * Revision 1.2  2005/07/06 07:09:20  vassil_momtchev
 * Locators now are managed by LocatorManager (attribute of the Factory)
 *
 * Revision 1.1  2005/06/27 08:51:50  alex_simov
 * refactoring: *.io.locator -> *.locator
 * refactoring: *.io.parser -> *.parser
 * refactoring: *.io.datastore -> *.datastore
 *
 * Revision 1.4  2005/06/01 10:55:46  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.4  2005/05/31 13:13:59  damian
 * looku changed to accept Identifier instead of IRI
 *
 * Revision 1.3  2005/05/31 09:38:05  marin
 * Locator interface
 *
 */
