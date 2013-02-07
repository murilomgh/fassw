/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2004-2007, Ontotext Lab. / SIRMA

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

package org.wsmo.grounding.sawsdl.io;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


public class Factory {

    public static final String DEFAULT_PARSER_IMPL =
        "com.ontotext.wsmo4j.grounding.sawsdl.io.GroundingParserImpl";
    public static final String DEFAULT_SERIALIZER_IMPL = 
        "com.ontotext.wsmo4j.grounding.sawsdl.io.GroundingSerializerImpl";
    
    public final static String PROVIDER_CLASS = "provider";
    public final static String ENCODING = "encoding";
    
    private Factory() {
    } // no instances are necessary
    
    public static GroundingParser createParser(Map<String, Object> props) {

        if (null == props) {
            props = new HashMap<String, Object>();
        }

        if (false == props.containsKey(Factory.PROVIDER_CLASS)) {
            //use default WSMO factory
            props.put(Factory.PROVIDER_CLASS, DEFAULT_PARSER_IMPL);
        }

        Object result = _createObject(props);
        if (false == result instanceof GroundingParser) {
            throw new RuntimeException(props.get(Factory.PROVIDER_CLASS) 
                    + " does not implement the org.wsmo.grounding.sawsdl.io.GroundingParser interface");
        }
        return (GroundingParser)result;
    }
    
    public static GroundingSerializer createSerializer(Map<String, Object> props) {
        if (null == props) {
            props = new HashMap<String, Object>();
        }

        if (false == props.containsKey(Factory.PROVIDER_CLASS)) {
            //use default WSMO factory
            props.put(Factory.PROVIDER_CLASS, DEFAULT_SERIALIZER_IMPL);
        }

        Object result = _createObject(props);
        if (false == result instanceof GroundingSerializer) {
            throw new RuntimeException(props.get(Factory.PROVIDER_CLASS) 
                    + " does not implement the org.wsmo.grounding.sawsdl.io.GroundingSerializer interface");
        }
        return (GroundingSerializer)result;
    }
    
    @SuppressWarnings("unchecked")
    private static Object _createObject(Map properties) {

        Object result = null;

        String clazzName = (String)properties.get(Factory.PROVIDER_CLASS);
        Class providerClass = null;

        try {
            providerClass = Class.forName(clazzName);
        }
        catch (ClassNotFoundException e) {
            try {
                providerClass = Class.forName(clazzName,
                                              true,
                                              Thread.currentThread().getContextClassLoader());
            }
            catch (ClassNotFoundException ne) {
                throw new RuntimeException("Provider's class not found in classpath..."+clazzName, ne);
            }
        }

        Constructor providerConstructor = null;

        try {
            Class[] param = new Class[] {java.util.Map.class};
            providerConstructor = providerClass.getConstructor(param);
        }
        catch (NoSuchMethodException nsme) {
            throw new RuntimeException(
                    "The provider class should have a constuctor which takes a single java.util.Map argument...",
                    nsme);
        }

        try {
            result = providerConstructor.newInstance(new Object[] {properties});
        }
        catch (InvocationTargetException ite) {
            throw new RuntimeException("cannot invoke the constructor!", ite);
        }
        catch (IllegalAccessException ile) {
            throw new RuntimeException("cannot access the constructor!", ile);
        }
        catch (InstantiationException inse) {
            throw new RuntimeException("cannot instantiate the object!", inse);
        }

        return result;
    }

}

/*
 * $Log: Factory.java,v $
 * Revision 1.2  2007/04/25 16:36:54  alex_simov
 * no message
 *
 * Revision 1.1  2007/04/24 15:32:06  alex_simov
 * generic parser/serializer factory added
 *
 */
