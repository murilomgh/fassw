/*
 wsmo4j - a WSMO API and Reference Implementation
 Copyright (c) 2005, University of Innsbruck, Austria
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
package org.deri.wsmo4j.logicalexpression.util;


import java.util.*;


/**
 * Utility to check typing of sets
 *
 * <pre>
 * Created on Jul 24, 2005
 * Committed by $Author: marin_dimitrov $
 * $Source: /cvsroot/wsmo4j/wsmo4j/org/deri/wsmo4j/logicalexpression/util/SetUtil.java,v $,
 * </pre>
 *
 * @author Holger Lausen
 *
 * @version $Revision: 1.3 $ $Date: 2005/09/09 15:51:42 $
 */
public class SetUtil {

    /**
     * Checks if all object in a collection are of specfic type.
     * @param collection collection to be checked
     * @param type the Type that the objects have to be an instance of
     * @return true if all objects in Collection are of specified type
     */
    public static boolean allOfType(Collection collection, Class type) {
        if (collection == null) {
            return true;
        }
        Iterator i1 = collection.iterator();
        boolean ok = true;
        while (i1.hasNext()) {
            Object o = i1.next();
            if (!type.isInstance(o)) {
                ok = false;
            }
        }
        return ok;
    }

    /**
     * Create an HashSet, add an Object to it and return the Set
     * @param o Object to be added to the HashSet
     * @return HashSet containing the Object o
     */
    public static Set createSet(Object o) {
        HashSet s = new HashSet();
        s.add(o);
        return s;
    }

    /**
     * Create an HashSet, add two Objects to it and return the Set
     * @param o1 Object 1 to be added to the HashSet
     * @param o2 Object 2 to be added to the HashSet
     * @return HashSet containing the Objects o1 and o2
     */
    public static Set createSet(Object o1, Object o2) {
        HashSet s = new HashSet();
        s.add(o1);
        s.add(o2);
        return s;
    }

    /**
     * Create an HashSet, add three Objects to it and return the Set
     * @param o1 Object 1 to be added to the HashSet
     * @param o2 Object 2 to be added to the HashSet
     * @param o3 Object 3 to be added to the HashSet
     * @return HashSet containing the Objects o1, o2 and o3
     */
    public static Set createSet(Object o1, Object o2, Object o3) {
        HashSet s = new HashSet();
        s.add(o1);
        s.add(o2);
        s.add(o3);
        return s;
    }

    /**
     * Create an HashSet, add four Objects to it and return the Set
     * @param o1 Object 1 to be added to the HashSet
     * @param o2 Object 2 to be added to the HashSet
     * @param o3 Object 3 to be added to the HashSet
     * @param o4 Object 4 to be added to the HashSet
     * @return HashSet containing the Objects o1, o2, o3 and o4
     */
    public static Set createSet(Object o1, Object o2, Object o3, Object o4) {
        HashSet s = new HashSet();
        s.add(o1);
        s.add(o2);
        s.add(o3);
        s.add(o4);
        return s;
    }
}
