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

package org.wsmo.factory;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.wsmo.datastore.*;
import org.wsmo.locator.*;
import org.wsmo.validator.WsmlValidator;
import org.wsmo.wsml.*;

/**
 * Factory interface
 * The Factory is both a factory (creates objects) and a meta-factory (creates factories)
 * Factory is <b>final</b>
 * @author not attributable
 * @version $Revision: 1.31 $ $Date: 2007/04/02 12:13:15 $
 */

/**
 * @stereotype factory*/
public final class Factory {

    public final static String PROVIDER_CLASS = "provider";

    public static final String WSMO_PARSER = "wsmo_parser";
    public static final String WSMO_SERIALIZER = "wsmo_serializer";
    public static final String WSML_VALIDATOR = "wsml_validator";
    public static final String LE_FACTORY = "le_factory";
    public static final String WSMO_FACTORY = "wsmo_factory";
    public static final String DATA_FACTORY = "data_factory";

    private static LocatorManager locManager;

    private static boolean cachingEnabled;

    private static Map <Map <String, Object>, Object> objectCache;

    private static Properties defaultImplMap;
    private static final String propertiesFile = "wsmo4j.properties";

    static {
        locManager = new LocatorManager();

        //cache by default; we may add setCaching(boolean) later
        cachingEnabled = true;

        //DON'T use weak refs
        objectCache = new HashMap <Map <String, Object>, Object>();

        defaultImplMap = new Properties();
                
        InputStream inProps = Thread.currentThread().getContextClassLoader()
                                      .getResourceAsStream(propertiesFile);
        if (inProps == null) {
	        inProps = Factory.class.getClassLoader().getResourceAsStream(propertiesFile);
        }
        
        if (inProps == null){
            throw new RuntimeException("Error loading config file "+propertiesFile+" from class path");
        }
        try {
            defaultImplMap.load(inProps);
        }
        catch(IOException ioe) {
            throw new RuntimeException("Error loading config file ", ioe);
        }
    }

    public static Serializer createSerializer(HashMap<Object, Object> hashMap) {
        throw new UnsupportedOperationException("Ainda nao implementado."); //To change body of generated methods, choose Tools | Templates.
    }

    public static Serializer createSerializer(HashMap<Object, Object> hashMap) {
        throw new UnsupportedOperationException("Ainda nao implementado."); //To change body of generated methods, choose Tools | Templates.
    }

    //singleton
    private Factory(){
    }

    /**
     * Creates a datastore based on the supplied preferences.
     * At least one preference should be supplied - the provider class
     * @see #PROVIDER_CLASS
     * @param properties the preferences for the datastore. Such preferences
     * should provide all the necessary information for the data store
     * initialisation (e.g. provider class, connection URL,...,
     * etc.)
     * @return a reference to a datastore
     */
    public static DataStore createDatastore(Map properties) {

        //0. preconditions
        if (null == properties || false == properties.containsKey(Factory.PROVIDER_CLASS)) {
            throw new IllegalArgumentException("Datastore class was not specified. There is NO default datastore implementation...");
        }

        Object result = _createObject(properties);

        assert null != result;

        //post-conditions
        if (false == result instanceof DataStore) {
            throw new RuntimeException(properties.get(Factory.PROVIDER_CLASS) + " does not implement the org.wsmo.datastore.Datastore interface");
        }

        return (DataStore)result;
    }

    /**
     * Creates a WsmlValidator based on the supplied preferences.
     * The properties map can contain the factories to be used as Strings or as
     * instances. The WsmlValidator Constructor needs to check this and
     * needs to create an instance of a given factory, if only the String
     * is supplied.
     * At least one preference should be supplied - the provider class.
     * 
     * The validator can either only check a given TopEntity for validity, 
     * or it can also check all imported ontologies. By default, the validator 
     * does not check the imported ontologies. 
     * This feature can be 
     * enabled by adding a property to the properties map when creating the 
     * WsmlValidator. Another preference can be set, which references a map 
     * containing a mapping from logical URIs to physical locations:
	 *	 HashMap prefs = new HashMap();
	 *	 prefs.put(WsmlValidator.VALIDATE_IMPORTS, new Boolean(true));
	 *	 prefs.put(Locator.URI_MAPPING, mapping);
     * 
     * @see #PROVIDER_CLASS
     * @param properties the preferences for the validator.
     * @return a wsmlValidator
     */
    public static WsmlValidator createWsmlValidator(Map <String, Object> properties) {

        //preconditions
        if (null == properties) {
            properties = new HashMap <String, Object>();
        }

        //check provider
        if (!properties.containsKey(Factory.PROVIDER_CLASS)) {
            //set default Wsml Validator
            properties.put(Factory.PROVIDER_CLASS,
                    defaultImplMap.getProperty(Factory.WSML_VALIDATOR));
        }

        // check LE factory
        if (!properties.containsKey(Factory.LE_FACTORY)) {
            //set default LogExpr factory
            properties.put(Factory.LE_FACTORY,
                    defaultImplMap.getProperty(Factory.LE_FACTORY));
        }

        Object result = _createObject(properties);

        assert null != result;

        //post-conditions
        if (false == result instanceof WsmlValidator) {
            throw new RuntimeException(properties.get(Factory.PROVIDER_CLASS) +
                    " does not implement the org.wsmo.validator.WsmlValidator interface");
        }

        return (WsmlValidator) result;
    }

    /**
     * Creates a parser based on the supplied preferences.
     * The properties map can contain the factories to be used as Strings or as
     * instances. The Parser Constructor needs to check this and
     * needs to create an instance of a given factory, if only the String
     * is supplied.
     * At least one preference should be supplied - the provider class
     * @see #PROVIDER_CLASS
     * @see #PARSER_FACTORY
     * @param properties the preferences for the parser. Such preferences
     * should provide all the necessary information for the parser
     * initialisation (e.g. provider class, factory to be used, file type, grammar version,
     * etc.)
     * @return a Parser
     */
    public static Parser createParser(Map <String, Object> properties) {

        //preconditions
        if (null == properties) {
            properties = new HashMap <String, Object>();
        }

        //check provider
        if (false == properties.containsKey(Factory.PROVIDER_CLASS)) {
            //set default parser
            properties.put(Factory.PROVIDER_CLASS,
                           defaultImplMap.getProperty(Factory.WSMO_PARSER));
        }

        //check WSMO factory
        if (false == properties.containsKey(Factory.WSMO_FACTORY)) {
            //set default WSMO factory
            properties.put(Factory.WSMO_FACTORY,
                           defaultImplMap.getProperty(Factory.WSMO_FACTORY));
        }

        //check LE factory
        if (false == properties.containsKey(Factory.LE_FACTORY)) {
            //set default LogExpr factory
            properties.put(Factory.LE_FACTORY,
                           defaultImplMap.getProperty(Factory.LE_FACTORY));
        }

        Object result = _createObject(properties);

        assert null != result;

        //post-conditions
        if (false == result instanceof Parser) {
            throw new RuntimeException(properties.get(Factory.PROVIDER_CLASS) + " does not implement the org.wsmo.wsml.Parser interface");
        }

        return (Parser)result;
    }

    /**
     * Creates a serialiser based on the supplied preferences.
     * At least one preference should be supplied - the provider class
     * @see #PROVIDER_CLASS
     * @param properties the preferences for the parser. Such preferences
     * should provide all the necessary information for the parser
     * initialisation (e.g. provider class, factory to be used, file type, grammar version,
     * etc.)
     * @return a Parser
     */
    public static Serializer createSerializer(Map <String, Object> properties) {

        //preconditions
        if (null == properties) {
            properties = new HashMap <String, Object>();
        }

        //check provider
        if (false == properties.containsKey(Factory.PROVIDER_CLASS)) {
            //set default parser
            properties.put(Factory.PROVIDER_CLASS,
                    defaultImplMap.getProperty(Factory.WSMO_SERIALIZER));
        }

        Object result = _createObject(properties);

        assert null != result;

        //post-conditions
        if (false == result instanceof Serializer) {
            throw new RuntimeException(properties.get(Factory.PROVIDER_CLASS) + " does not implement the org.wsmo.wsml.Serialiser interface");
        }

        return (Serializer)result;
    }

    /**
     * Creates a WsmoFactory based on the supplied preferences.
     * At least one preference should be supplied - the provider class
     * @see #PROVIDER_CLASS
     * @param properties the preferences for the WSMO factory. Such preferences
     * should provide all the necessary information for the factory
     * initialisation (e.g. provider class, etc.)
     * @return a WsmoFactory
     */
    public static WsmoFactory createWsmoFactory(Map <String, Object> properties) {

        //preconditions
        if (null == properties) {
            properties = new HashMap <String, Object>();
        }

        //check provider
        if (false == properties.containsKey(Factory.PROVIDER_CLASS)) {
            //use default WSMO factory
            properties.put(Factory.PROVIDER_CLASS,
                    defaultImplMap.getProperty(Factory.WSMO_FACTORY));
        }

        Object result = _createFactory(properties);

        assert null != result;

        //post-conditions
        if (false == result instanceof WsmoFactory) {
            throw new RuntimeException(properties.get(Factory.PROVIDER_CLASS) + " does not implement the org.wsmo.factory.WsmoFactory interface");
        }

        return (WsmoFactory)result;
    }


    /**
     * Creates a DataFactory based on the supplied preferences.
     * The properties map can contain the factories to be used as Strings or as
     * instances. The DataFactory Constructor needs to check this and
     * needs to create an instance of a given factory, if only the String
     * is supplied.
     * At least one preference should be supplied - the provider class
     * @see #PROVIDER_CLASS
     * @param properties the preferences for the Data factory. Such preferences
     * should provide all the necessary information for the factory
     * initialisation (e.g. provider class, etc.)
     * @return a DataFactory
     */
    public static DataFactory createDataFactory(Map <String, Object> properties) {

        //preconditions
        if (null == properties) {
            properties = new HashMap <String, Object> ();
        }

        //check provider
        if (false == properties.containsKey(Factory.PROVIDER_CLASS)) {
            //use default data factory
            properties.put(Factory.PROVIDER_CLASS,
                    defaultImplMap.getProperty(Factory.DATA_FACTORY));
        }

        Object result = _createFactory(properties);

        assert null != result;

        //post-conditions
        if (false == result instanceof DataFactory) {
            throw new RuntimeException(properties.get(Factory.PROVIDER_CLASS) + " does not implement the org.wsmo.factory.DataFactory interface");
        }

        return (DataFactory)result;
    }

    /**
     * Creates a LogicalExpressionFactory based on the supplied preferences.
     * The properties map can contain the factories to be used as Strings or as
     * instances. The LogicalExpressionFactory Constructor needs to check this and
     * needs to create an instance of a given factory, if only the String
     * is supplied.
     * At least one preference should be supplied - the provider class
     * @see #PROVIDER_CLASS
     * @param properties the preferences for the LogicalExpression factory. Such preferences
     * should provide all the necessary information for the factory
     * initialisation (e.g. provider class, etc.)
     * @return a LogicalExpressionFactory
     */
    public static LogicalExpressionFactory createLogicalExpressionFactory(Map <String, Object> properties) {
        //preconditions
        if (null == properties) {
            properties = new HashMap <String, Object> ();
        }

        //check provider
        if (false == properties.containsKey(Factory.PROVIDER_CLASS)) {
            //use default WSMO factory
            properties.put(Factory.PROVIDER_CLASS,
                    defaultImplMap.getProperty(Factory.LE_FACTORY));
        }

        Object result = _createFactory(properties);

        assert null != result;

        //post-conditions
        if (false == result instanceof LogicalExpressionFactory) {
            throw new RuntimeException(properties.get(Factory.PROVIDER_CLASS) + " does not implement the org.wsmo.factory.LogicalExpressionFactory interface");
        }

        return (LogicalExpressionFactory)result;
    }
    
    /**
     * Returns a LocatorManager. This LocatorManager is used to create a Locator, 
     * which then can be added to / removed from the LocatorManager. The locator 
     * is used to lookup wsmo entities based on their IRI. The default locator does 
     * also accept a mapping from logical URIs to physical locations, that can be 
     * used to locate ontologies:<br /><br />
     *   prefs.put(Locator.URI_MAPPING, mapping);<br />
     *   Locator locator = LocatorManager.createLocator(prefs); 	
     * 
     * @return a LocatorManager
     * @see Locator
     */
    public static LocatorManager getLocatorManager() {
    	return locManager;
    }

    private static Object _createObject(Map properties) {

        Object result = null;

        //0. preconditions
        assert properties != null;
        assert properties.containsKey(Factory.PROVIDER_CLASS);

        String clazzName;
        clazzName = (String)properties.get(Factory.PROVIDER_CLASS);
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
            throw new RuntimeException("cannot invoke the constructor a DataStore!", ite);
        }
        catch (IllegalAccessException ile) {
            throw new RuntimeException("cannot access the constructor!", ile);
        }
        catch (InstantiationException inse) {
            throw new RuntimeException("cannot instantiate a DataStore!", inse);
        }

        return result;
    }


    private static Object _createFactory(Map <String, Object> properties) {

        //0. preconditions
        assert properties != null;
        assert properties.containsKey(Factory.PROVIDER_CLASS);

        Object result = null;

        if (Factory.cachingEnabled) {
            //check cache
            synchronized (Factory.objectCache) {

                result = Factory.objectCache.get(properties);

                if (null == result) {
                    //NOT FOUND, create new instance and add to cache
                    result = _createObject(properties);
                    Factory.objectCache.put(new HashMap <String, Object> (properties), result);
                }
            }
        }
        else {
            //directly create a new instance
            result = _createObject(properties);
        }

        assert null != result;
        return result;
    }

}

/*
 * $Log: Factory.java,v $
 * Revision 1.31  2007/04/02 12:13:15  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.30  2006/11/27 11:36:08  nathaliest
 * changed the validator to not import ontologies on its own and added documentation how to use the default locator to import ontologies
 *
 * Revision 1.29  2006/02/13 02:19:54  haselwanter
 * The resource loading of the properties file consulted only the context classloader, which a less likely place than the current classloader.
 *
 * Revision 1.28  2006/02/06 01:44:21  haselwanter
 * Added a fallback to the metafactory to retrieve classes via the context classloader if the current classloader is unable to deliver. Fixes #1314733
 *
 * Revision 1.27  2006/01/31 09:18:39  nathaliest
 * small change at documentation
 *
 * Revision 1.26  2006/01/11 13:01:17  marin_dimitrov
 * common constants moved to Factory
 *
 * Revision 1.25  2006/01/09 14:18:43  nathaliest
 * javadoc added
 *
 * Revision 1.24  2006/01/05 14:57:48  nathaliest
 * Validator uses leFactory taken in constructor from properties map
 *
 * Revision 1.23  2005/11/18 14:36:28  nathaliest
 * added Factory.createWsmlValidator(Map properties) method for creating the validator
 *
 * Revision 1.22  2005/09/23 07:09:50  holgerlausen
 * moved constanttransformer from API to implementation, removed dublicated constants in logicalexpression.constants
 *
 * Revision 1.21  2005/09/16 12:31:13  marin_dimitrov
 * DataFactory can now be created from the meta factory
 *
 * Revision 1.20  2005/09/16 12:07:05  marin_dimitrov
 * wsmo4j.properties moved
 *
 * Revision 1.19  2005/09/15 13:04:12  alex_simov
 * 'config.ini' renamed to 'wsmo4j.properties'
 *
 * Revision 1.18  2005/09/15 11:23:24  alex_simov
 * Factory's implementations info fields extracted in an external config file
 *
 * Revision 1.17  2005/09/06 18:19:23  holgerlausen
 * better error message
 *
 * Revision 1.16  2005/09/02 15:25:33  alex_simov
 * default log.expr. factory class relocated
 *
 * Revision 1.15  2005/07/11 10:19:26  vassil_momtchev
 * createLocator method moved to LocatarManager class
 *
 * Revision 1.14  2005/07/06 11:43:06  marin_dimitrov
 * factory caching
 *
 * Revision 1.13  2005/07/06 10:56:36  marin_dimitrov
 * static constructor
 *
 * Revision 1.12  2005/07/06 07:10:13  vassil_momtchev
 * Locators now are managed by LocatorManager (attribute of the Factory)
 *
 * Revision 1.11  2005/06/30 12:37:00  alex_simov
 * Implementation class split: WSMLParserImpl/WSMLSerializerImpl
 *
 * Revision 1.10  2005/06/30 12:24:23  alex_simov
 * Rename: Serialiser -> Serializer
 *
 * Revision 1.9  2005/06/30 12:21:07  alex_simov
 * Rename: Serialiser -> Serializer
 *
 * Revision 1.8  2005/06/30 09:56:46  marin_dimitrov
 * createSerialiser added
 *
 * Revision 1.7  2005/06/30 09:32:55  alex_simov
 * refactoring: org.wsmo.parser -> org.wsmo.wsml.*
 *
 * Revision 1.6  2005/06/27 14:09:32  alex_simov
 * refactoring: *.io.locator -> *.locator
 * refactoring: *.io.parser -> *.parser
 * refactoring: *.io.datastore -> *.datastore
 *
 * Revision 1.5  2005/06/27 08:51:49  alex_simov
 * refactoring: *.io.locator -> *.locator
 * refactoring: *.io.parser -> *.parser
 * refactoring: *.io.datastore -> *.datastore
 *
 * Revision 1.4  2005/06/22 14:40:48  alex_simov
 * Copyright header added/updated
 *
 * Revision 1.3  2005/06/02 14:22:47  alex_simov
 * Default parser implementation fix
 *
 * Revision 1.2  2005/06/01 16:39:11  marin_dimitrov
 * Datastore --> DataStore
 *
 * Revision 1.1  2005/06/01 10:34:43  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.10  2005/05/31 14:42:00  damian
 * detDefault was not implemented
 *
 * Revision 1.9  2005/05/31 13:13:16  damian
 * get/setDefaultLocator added
 *
 * Revision 1.8  2005/05/19 12:53:54  marin
 * fixed factory package
 *
 * Revision 1.7  2005/05/18 14:30:31  marin
 * implementation + javadoc comments
 *
 */
