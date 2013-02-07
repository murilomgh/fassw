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

import java.lang.reflect.*;
import java.util.*;

import org.wsmo.common.*;
import org.wsmo.factory.*;

/**
 * The locator manager responsible to validate and manage
 * a queue of locators.
 *
 * @author not attributable
 * @version $Revision: 1.10 $ $Date: 2007/04/02 12:13:15 $
 */
public final class LocatorManager {

	private static final String LOCATOR = "org.deri.wsmo4j.locator.LocatorImpl";
	
    private LinkedList <Locator> locList = new LinkedList <Locator> ();

    /**
     * Adds new locator to the queue of locators
     * @param locator The locator to add
     */
    public void addLocator(Locator locator) {
        if (locator == null) {
            throw new IllegalArgumentException();
        }
        if (locList.contains(locator)) {
            throw new IllegalArgumentException("The locator was already added!");
        }
        locList.addLast(locator);
    }


    /**
     * Attempts to find an entity based on its IRI.
     * If the first locator in the queue is unable to locate the entity
     * then it will pass the request to
     * the next locator in the locator chain
     * @param id The ID of the entity to find
     * @return The entity corresponding to the supplied ID or <i>null</i> if none found
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see #setNext(Locator loc)
     * @see Entity
     */
    public Set lookup(Identifier id) {
        Set entities = new HashSet();
        for (int i = 0; i < locList.size(); i++) {
            entities = locList.get(i).lookup(id);
            if (entities != null) {
                break;
            }
        }
        return entities;
    }

    /**
     * Attempts to find an entity based on its IRI.
     * If the first locator in the queue is unable to locate the entity
     * then it will pass the request to
     * the next locator in the locator chain
     * @param id The ID of the entity to find
     * @param clazz The type of the identifier
     * @return The entity corresponding to the supplied ID or <i>null</i> if none found
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see #setNext(Locator loc)
     */
    public Entity lookup(Identifier id, Class clazz) {
        Entity entity = null;
        for (int i = 0; i < locList.size(); i++) {
            entity = locList.get(i).lookup(id, clazz);
            if (entity != null) {
                break;
            }
        }
        return entity;
    }

    /**
     * Removes a locator from the queue
     * @param locator The locator to remove
     */
    public void removeLocator(Locator locator) {
        if (locator == null) {
            throw new IllegalArgumentException();
        }
        locList.remove(locator);
    }

    /**
     * Creates a locator based on the supplied preferences.
     * @param properties the preferences for the locator. Such preferences
     * should provide all the necessary information for the locator
     * initialisation (e.g. provider class, type...)
     * @return a Locator
     */
    public static Locator createLocator(Map <String, Object> map) {
    	String className = null;
        if (null == map || false == map.containsKey(Factory.PROVIDER_CLASS)) {
            className = LOCATOR;
        }

        if (className == null) {
        	className = (String) map.get(Factory.PROVIDER_CLASS);
        }
        Object locator = null;

        try {
            Class providerLocator = Class.forName(className);
            Constructor locatorConstructor =
                    providerLocator.getConstructor(new Class[] {java.util.Map.class});
            locator = locatorConstructor.newInstance(new Object[] {map});
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException("Provider's class not found in classpath...", e);
        }
        catch (NoSuchMethodException nsme) {
            throw new RuntimeException(
                    "The provider class should have a constuctor which takes a single java.util.Map argument...",
                    nsme);
        }
        catch (Exception e) {
            throw new RuntimeException("Can not instantiate locator", e);
        }

        assert locator != null;

        if (false == locator instanceof Locator) {
            throw new RuntimeException(map.get(Factory.PROVIDER_CLASS) +
                                       " does not implement the org.wsmo.locator.Locator interface");
        }
        return (Locator)locator;
    }
}

/*
 * $Log: LocatorManager.java,v $
 * Revision 1.10  2007/04/02 12:13:15  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.9  2006/08/22 16:25:48  nathaliest
 * added locator implementation and set it as default locator at the locator manager
 *
 * Revision 1.8  2006/01/09 12:32:45  vassil_momtchev
 * locator manager class changed to final
 *
 * Revision 1.7  2005/10/18 09:16:47  marin_dimitrov
 * lookup() returns a Set (was Entity[])
 *
 * Revision 1.6  2005/10/18 09:13:01  marin_dimitrov
 * lookup() returns a Set (was Entity[])
 *
 * Revision 1.5  2005/10/18 08:48:06  vassil_momtchev
 * Entity lookup(Identifier) changed to Entity[] lookup(Identifier) and Entity lookup(Identifier, Class)
 *
 * Revision 1.4  2005/09/14 08:54:13  marin_dimitrov
 * removed getLocatorCount()
 *
 * Revision 1.3  2005/09/09 10:40:49  marin_dimitrov
 * formatting
 *
 */
