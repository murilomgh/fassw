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

package org.wsmo.datastore;

import java.util.*;

import org.wsmo.common.*;


public interface DataStore {

    /**
     * Loads an Entity given its identifier
     * @param id The ID of the Entity to Load
     * @return all Entities corresponding to the povided ID (or null if not found). Note that if metamodelling
     * is not used, e.g. the language subset is WSML-Core, then there will be only one entity with a given ID.
     * @see Entity
     */
    Set <Entity> load(Identifier id);

    /**
     * Loads an Entity given its identifier
     * @param id The ID of the Entity to Load
     * @param clazz The type of the desired entity to be loaded
     * @return the entity with the specified identifier and type or <code>null</code> if no such
     * entity is found.
     * @see Entity
     */
    Entity load(Identifier id, Class clazz);

    /**
     * Store an Entity into datastore
     * @param item The entity to Store
     */
    void save(Entity item);

    /**
     * Remove <b>all</b> entities identified by the provided Identifier from the Datastore
     * @param id The ID of the Entity(ies) to be removed
     */
    void remove(Identifier id);

    /**
     * Remove an entity identified by its Identifier and class type from the Datastore. The class is the WSMO API
     * interface that the entity implements (e.g. org.omwg.ontology.Concept, etc) and <b>not</b> a specific class
     * implementating the WSMO API interface
     * @param id The ID of the Entity to be removed
     */
    void remove(Identifier id, Class clazz);

}

/*
 * $Log: DataStore.java,v $
 * Revision 1.7  2007/04/02 12:13:16  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.6  2005/10/19 08:32:58  alex_simov
 * load(Identifier, Class) method added
 *
 * Revision 1.5  2005/10/18 09:15:55  marin_dimitrov
 * load() returns a Set (was Entity[])
 *
 * Revision 1.3  2005/10/17 12:41:22  marin_dimitrov
 * metamodeliing extensions
 *
 */
