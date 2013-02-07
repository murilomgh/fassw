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

package com.ontotext.wsmo4j.examples;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.omwg.ontology.Instance;
import org.omwg.ontology.Ontology;
import org.wsmo.common.Entity;
import org.wsmo.common.Identifier;
import org.wsmo.common.TopEntity;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.common.exception.SynchronisationException;
import org.wsmo.factory.Factory;
import org.wsmo.factory.LogicalExpressionFactory;
import org.wsmo.factory.WsmoFactory;
import org.wsmo.locator.Locator;
import org.wsmo.locator.LocatorManager;
import org.wsmo.wsml.Parser;
import org.wsmo.wsml.ParserException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004-2005
 * </p>
 * <p>
 * Company: Ontotext Lab., Sirma AI
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class LocatorExample implements Locator {
  public final static String FILE = "File";

  private TopEntity[] wsmlEntities = new TopEntity[0];

  /**
   * Constructor of InstanceLocator.
   * 
   * @param map
   *          map containing the directory to be used
   * @throws InvalidModelException
   * @throws ParserException
   * @throws IOException
   * @throws FileNotFoundException
   */
  public LocatorExample(Map<String, File> map) throws FileNotFoundException, IOException, ParserException, InvalidModelException {
    if (map == null || map.get(FILE) == null) {
      throw new IllegalArgumentException();
    }

    File wsmlFile = map.get(FILE);

    // set up the wsmo factory and parser
    HashMap<String, Object> props = new HashMap<String, Object>();
    WsmoFactory factory = Factory.createWsmoFactory(null);
    LogicalExpressionFactory leFactory = Factory.createLogicalExpressionFactory(null);
    props.put(Factory.WSMO_FACTORY, factory);
    props.put(Factory.LE_FACTORY, leFactory);
    Parser parser = Factory.createParser(props);
    
    
    if (wsmlFile.exists()) {
      wsmlEntities = parser.parse(new FileReader(wsmlFile));
    }
  }

  /**
   * Method to retrieve Entity based on Identifier; It's supposed that the
   * Entity is located in a file with name corresponding to it's namespace.
   * 
   * @param id
   *          the id to search for
   * @return instance or null if the identifier was not found in the directory
   */
  public Entity lookup(Identifier id, Class clazz) throws SynchronisationException {
      return findEntity(wsmlEntities, id);
  }
  
  /**
   * Method to retrieve Entity based on Identifier; It's supposed that the
   * Entity is located in a file with name corresponding to it's namespace.
   * 
   * @param id
   *          the id to search for
   * @return instance or null if the identifier was not found in the directory
   */
  public Set<Entity> lookup(Identifier id) throws SynchronisationException {
    throw new UnsupportedOperationException();
  }

  /**
   * Scan all top entities for instances
   * 
   * @param entities
   *          the entities to scan
   * @param id
   *          the identifier of an instance to search for
   * @return instance with the same identifier or null
   */
  private Entity findEntity(TopEntity[] entities, Identifier id) {
    for (int i = 0; i < entities.length; i++) {
      if (entities[i] instanceof Ontology) {
        Ontology ontology = (Ontology) entities[i];
        for (Iterator j = ontology.listInstances().iterator(); j.hasNext();) {
          if (((Instance) j.next()).getIdentifier().equals(id)) {
            return (Instance) j.next();
          }
        }
      }
    }

    // instance was not found
    return null;
  }
  
  /**
   * Runs the example
   * @param args[0] - path to a wsml file, args[1] an identifier IRI of and instance 
   */
  public static void main(String[] args) {
    if (args.length == 2) {
      test(new File(args[0]), args[1]);
    } else {
      test(new File("testWsml.wsml"), "http://testIRI#test");
    }

  }

  public static void test(File wsmlFile, String lacateIRI) {
    HashMap<String, Object> locatorInit = new HashMap<String, Object>();
    locatorInit.put(Factory.PROVIDER_CLASS, "com.ontotext.wsmo4j.examples.LocatorExample");
    locatorInit.put(LocatorExample.FILE, wsmlFile);

    // create Locator
    Locator locator = LocatorManager.createLocator(locatorInit);
    

    // register the locator to the locator manager
    Factory.getLocatorManager().addLocator(locator);

    // create wsmo factory
    WsmoFactory factory = Factory.createWsmoFactory(null);

    // get the Instance Test from Ontology file locatorExample
    Instance instance = factory.getInstance(factory.createIRI(lacateIRI));

    try {
      // ensure that we have the implementation but not a dummy proxy by calling
      // any method except getIdentifier() f.e. getOntology()
      System.out.println("Instance is part from ontology: " + instance.getOntology().getIdentifier());
    } catch (SynchronisationException e) {
      throw new RuntimeException("The locator did not succed to find the isntance!");
    }
  }
}

/*
 * $Log: LocatorExample.java,v $
 * Revision 1.10  2007/06/07 08:24:45  lcekov
 * fix examples http://jira.sirma.bg/jira/browse/WSMO4J-20
 * Revision 1.9 2007/04/02 12:13:27 morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 * 
 * Revision 1.8 2006/01/11 13:02:41 marin_dimitrov common constants moved to
 * Factory
 * 
 * Revision 1.7 2005/10/18 09:13:34 marin_dimitrov lookup() returns a Set (was
 * Entity[])
 * 
 * Revision 1.6 2005/10/18 08:58:30 vassil_momtchev example updated
 * 
 * Revision 1.5 2005/09/20 19:41:01 holgerlausen removed superflouis interfaces
 * for IO in logical expression (since intgeration not necessary)
 * 
 * Revision 1.4 2005/09/19 10:18:45 vassil_momtchev header and log added
 * 
 */