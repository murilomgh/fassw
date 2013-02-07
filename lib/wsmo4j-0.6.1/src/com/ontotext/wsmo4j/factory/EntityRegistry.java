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

package com.ontotext.wsmo4j.factory;

import org.wsmo.common.*;
import org.wsmo.locator.Locator;
import org.wsmo.common.exception.SynchronisationException;

import java.util.*;
import java.lang.reflect.Proxy;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004-2005</p>
 * <p>Company: Ontotext Lab., Sirma AI</p>
 * @author unascribed
 * @version 1.0
 */

final class EntityRegistry implements Locator {

    private static WeakHashMap <Identifier, WeakHashMap> weakMap = new WeakHashMap  <Identifier, WeakHashMap>();

    //private static ReferenceQueue weakMapQueue = new ReferenceQueue();

    private static EntityRegistry theOnlyInstance = new EntityRegistry();

    private EntityRegistry() {
        //Factory.setDefaultLocator(this);
    }

    static EntityRegistry get() {
        return theOnlyInstance;
    }

    /**
     * Method tries to find in the internal registry if there is object for a 
     * given iri of fiven type available
     * @param uri identifier to search for
     * @param clazz supported interface from by the object
     * @return Entity or null if it's not found
     */
    static Entity isRegistered(Identifier uri, Class clazz) {
        if (!clazz.isInterface()) {
            throw new RuntimeException("supply an Interface as clazz parameter!");
        }

        WeakHashMap innerMap = null;
        synchronized(weakMap) {
            try {
                innerMap = weakMap.get(uri);
            }
            catch (ClassCastException e) {
                throw new RuntimeException("Invalid entry type. WeakHashMap expected!");
            }

            if (innerMap != null) {
                Object entity = null;
                Set keySet = innerMap.keySet();
                for (Iterator i = keySet.iterator(); i.hasNext();) {
                    entity = i.next();
                    
                    // for proxy instances try to match every supported interface to clazz
                    if (entity instanceof Proxy) {
                        Object invocHandler = Proxy.getInvocationHandler(entity);
                        if (invocHandler instanceof IDReference) {
                            IDReference ref = (IDReference) invocHandler;
                            Class[] supportedIfaces = ref.getSupportedInterfaces();
                            for (int j = 0; j < supportedIfaces.length; j++) {
                                if (supportedIfaces[j].isAssignableFrom(clazz)) {
                                    return (Entity) entity;
                                }
                            }
                        }
                    }
                    
                    // for Entity object find if it's instances of the specified interface
                    if (clazz.isInstance(entity)) {
                        return (Entity) entity;
                    }
                }
            }
        }

        return null;
    }

    static void registerEntity(Entity entity) {
        synchronized (weakMap) {
            WeakHashMap innerMap = null;
            try {
                innerMap = weakMap.get(entity.getIdentifier());
            }
            catch (ClassCastException e) {
                throw new RuntimeException("Invalid entry type. WeakHashMap expected!");
            }
            if (innerMap == null) {
                innerMap = new WeakHashMap();
                weakMap.put(entity.getIdentifier(), innerMap);
            }
            /* We may have attributes with type _"http://www.wsmo.org/wsml/wsml-syntax#goal"
             * If this check is enabled, we cannot set attribute value to goal instace
             * However, if it is not, there would exist 2 objects of different type with the same IRI
             */
            else if (entity instanceof TopEntity) {
                Iterator iterator = innerMap.keySet().iterator();
                if (iterator.hasNext()) {
                    StringBuffer errorMsg = getEntityType((Entity) innerMap.keySet().iterator().next());
                    throw new RuntimeException("IRI " + entity.getIdentifier().toString() + 
                        " is already registered as [" + errorMsg + "]. " +
                        "Metamodeling is disallowed for TopEntity objects!");
                }
            }
			
            
            innerMap.put(entity, null);
        }
    }
    
    // Helper method to get the supported types of an Entity/Proxy to Entity as String
    private static StringBuffer getEntityType(Entity entity) {
        StringBuffer resultStr = new StringBuffer();
        if (entity instanceof Proxy == false) {
            resultStr.append(entity.getClass().getName());
            return resultStr;
        }
        if (Proxy.getInvocationHandler(entity) instanceof IDReference) {
            IDReference ref = (IDReference) Proxy.getInvocationHandler(entity);
            for(int i = 0; i < ref.getSupportedInterfaces().length; i++) {
                resultStr.append(ref.getSupportedInterfaces()[i].getName());
                if (i+1 < ref.getSupportedInterfaces().length) {
                    resultStr.append(", ");
                }
            }
        }
        return resultStr;
    }

    /**
     * Attempts to find an entity based on its IRI.
     * @param id The ID of the entity to find
     * @return The entity corresponding to the supplied ID or <i>null</i> if none found
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see #setNext(Locator loc)
     */
    public Entity lookup(Identifier id, Class clazz) throws SynchronisationException {
        Entity temp = isRegistered(id, clazz);
        if (temp != null) {
            if (temp instanceof Proxy) {
                Object ref = Proxy.getInvocationHandler(temp);
                if (ref instanceof IDReference) {
                    if (((IDReference)ref).isResolved())
                        return temp;
                }
            }
            else {
                return temp;
            }
        }
        return null;
    }

    public Set <Entity> lookup(Identifier id) throws SynchronisationException {
        throw new UnsupportedOperationException("Use lookup(Identifier, Class) instead!");
    }
}