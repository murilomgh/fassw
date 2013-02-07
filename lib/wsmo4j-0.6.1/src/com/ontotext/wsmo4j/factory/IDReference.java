/*
 wsmo4j - a WSMO API and Reference Implementation
 
 Copyright (c) 2004-2005, OntoText Lab. / SIRMA
 University of Innsbruck, Austria 
 
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

import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.*;

import org.omwg.ontology.*;
import org.wsmo.common.Identifier;
import org.wsmo.common.Entity;
import org.wsmo.common.exception.SynchronisationException;

import org.wsmo.factory.Factory;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004-2005</p>
 * <p>Company: Ontotext Lab., Sirma AI</p>
 * @author unascribed
 * @version 1.0
 */

public class IDReference implements InvocationHandler {
    private Identifier handle;

    private Entity ref = null;

    private LinkedList invokeQueue;

    private Class[] supportedInterfaces;

    private IDReference(Identifier handle, Class[] ifaces) {
        this.handle = handle;
        this.supportedInterfaces = ifaces;
        invokeQueue = new LinkedList();
    }

    public boolean isResolved() {
        return ref != null;
    }
    
    Class[] getSupportedInterfaces() {
        return supportedInterfaces;
    }

    void setDelegate(Entity delegate) {
        if (ref != null
                && 0 == delegate.getIdentifier().toString().compareTo(
                        ref.getIdentifier().toString()))
            return;
        if (delegate instanceof Proxy)
            throw new RuntimeException("cannot set a Proxy as Delegate!");
        ref = delegate;
        for (Iterator i = invokeQueue.iterator(); i.hasNext();) {
            try {
                Object[] args = (Object[]) i.next();
                invoke(args[0], (Method) args[1], (Object[]) args[2]);
            }
            catch (Throwable t) {
                throw new RuntimeException("Could not apply the proxy call history!", t);
            }
        }
        invokeQueue.clear();
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // handle Entity.getIdentifier();
        if (0 == method.getName().compareTo("getIdentifier"))
            return handle;
        // route since we have a deleagte already set
        if (ref != null) {
            return method.invoke(ref, args);
        }
        if (0 == method.getName().compareTo("hashCode")) {
            return method.invoke(handle, args);
        }
        if (0 == method.getName().compareTo("equals")) {
            if (args[0] instanceof Proxy) {
                Object temp = Proxy.getInvocationHandler(args[0]);
                if (temp instanceof IDReference) {
                    IDReference invocHandler = (IDReference) temp;
                    if (invocHandler.getSupportedInterfaces() == getSupportedInterfaces()
                            && invocHandler.handle.equals(handle)) {
                        return Boolean.TRUE;
                    }
                }
            }
            return Boolean.FALSE;
        }
        // handle Object's public methods 'hashCode', 'toString', equals
        if (method.getDeclaringClass().equals(Object.class)) {
            //            if (ref != null)
            //                return method.invoke(ref, args);
            return method.invoke(handle, args);
        }
        
        // try to resolve it
        Entity temp = null;
        for (int i = 0; i < supportedInterfaces.length; i++) {
            temp = Factory.getLocatorManager().lookup(handle, supportedInterfaces[i]);
            if (temp != null) {
                setDelegate(temp);
                return method.invoke(ref, args);
            }
        }

        // if not resolved try to work with the proxy
        if (method.getReturnType() == void.class) {
            invokeQueue.add(new Object[] { proxy, method, args });
            return null;
        }
        if (method.getReturnType() == Set.class) {
            return new LinkedHashSet();
        }
        if (method.getReturnType() == List.class) {
            return new LinkedList();
        }
        if (method.getReturnType() == Map.class) {
            return new HashMap();
        }
        if (method.getReturnType() == Ontology.class) {
            return null;
        }
		throw new SynchronisationException(handle.toString());
    }

    static Object createNewReference(Identifier id, Class[] ifaces) {
        // @todo: some runtime checks to ensure that there is no impl already created
        // @todo: some threadsafty should be added to avoid multiple refs with single id to be
        //   created across the active treads
        Object obj = null;
        //        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        ClassLoader loader = Identifier.class.getClassLoader();
        try {
            obj = Proxy.newProxyInstance(loader, ifaces, new IDReference(id, ifaces));
        }
        catch (RuntimeException ia) {
            loader = null;
        }

        if (obj == null && loader == null)
            loader = Thread.currentThread().getContextClassLoader();
        try {
            obj = Proxy.newProxyInstance(loader, ifaces, new IDReference(id, ifaces));
        }
        catch (RuntimeException ia) {
            loader = null;
        }

        if (obj == null && loader == null)
            loader = ClassLoader.getSystemClassLoader();

        obj = Proxy.newProxyInstance(loader, ifaces, new IDReference(id, ifaces));

        return obj;
    }
}