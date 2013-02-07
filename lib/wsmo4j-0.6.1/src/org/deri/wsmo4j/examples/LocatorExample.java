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
package org.deri.wsmo4j.examples;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.omwg.ontology.Concept;
import org.omwg.ontology.Ontology;
import org.wsmo.common.Identifier;
import org.wsmo.common.exception.SynchronisationException;
import org.wsmo.factory.Factory;
import org.wsmo.locator.Locator;
import org.wsmo.locator.LocatorManager;
import org.wsmo.validator.ValidationError;
import org.wsmo.validator.ValidationWarning;
import org.wsmo.validator.WsmlValidator;
import org.wsmo.wsml.Parser;

/**
 * This class is meant to be used as example for how to use the default locator 
 * implementation. This is shown in combination with the use of the validator, 
 * that, by default, is enabled to also validate imported ontologies. This 
 * feature can nevertheless be disabled.
 * 
 * <pre>
 * Created on Nov 22, 2006
 * Committed by $Author: nathaliest $
 * $Source: /cvsroot/wsmo4j/wsmo4j/org/deri/wsmo4j/examples/LocatorExample.java,v $,
 * </pre>
 * 
 * @author nathalie.steinmetz@deri.org
 * @version $Revision: 1.3 $ $Date: 2007/06/08 07:33:42 $
 */
public class LocatorExample {
	
	private Parser parser = null;
	
	private Locator locator = null;
	
	private WsmlValidator validator = null;
	
	public HashSet <Identifier> visitedOntologies = new HashSet <Identifier> ();
	
	private String phys_ex8 = this.getClass().getClassLoader().getResource(
			"org/deri/wsmo4j/examples/valid_wsml_rule_test.wsml").toString();

	private String phys_ex3 = this.getClass().getClassLoader().getResource(
			"org/deri/wsmo4j/examples/invalid_wsml_flight_test.wsml").toString();
	
	private List<ValidationError> errors = new Vector<ValidationError>();
	
	private List<ValidationWarning> warnings = new Vector<ValidationWarning>();
	
	private void doTestRun() throws Exception {
		// read test file and parse it 
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(
                "test/wsmo4j/validator/importsOntology.wsml");
        // assuming first topentity in file is an ontology  
        Ontology ontology  = (Ontology)parser.parse(new InputStreamReader(is))[0]; 
    	visitedOntologies.add(ontology.getIdentifier());
    	
    	// set Locator
    	Factory.getLocatorManager().addLocator(locator);
    	
        // Creating the WSML Validator and disable feature to validate imported files
        HashMap <String, Object> prefs = new HashMap <String, Object> ();
		prefs.put(WsmlValidator.VALIDATE_IMPORTS, new Boolean(false));
		prefs.put(Factory.WSMO_FACTORY, Factory.createWsmoFactory(null));
		prefs.put(Factory.LE_FACTORY, Factory.createLogicalExpressionFactory(null));
		prefs.put(Parser.CACHE_LOGICALEXPRESSION_STRING, "true");
		validator = Factory.createWsmlValidator(prefs);
		prefs.clear();
        validator.isValid(ontology, errors, warnings);
        System.out.println("\n\nErrors and warnings without validation of imported files:\n");
        printError(errors);
        printWarning(warnings);
        errors.clear();
        warnings.clear();
        
        // check imported ontologies
    	importOntologies(ontology);
        
        // Creating the WSML Validator with enabling the feature to validate imported files
        prefs.put(WsmlValidator.VALIDATE_IMPORTS, new Boolean(true));
		prefs.put(Factory.WSMO_FACTORY, Factory.createWsmoFactory(null));
		prefs.put(Factory.LE_FACTORY, Factory.createLogicalExpressionFactory(null));
		prefs.put(Parser.CACHE_LOGICALEXPRESSION_STRING, "true");
		validator = Factory.createWsmlValidator(prefs);
        validator.isValid(ontology, errors, warnings);
        System.out.println("\n\nErrors and warnings with validation of imported files:\n");
        printError(errors);
        printWarning(warnings);      
    	Factory.getLocatorManager().removeLocator(locator);
	}
	
	private void importOntologies(Ontology ont) {
		System.out.println("Number of ontologies: " + ont.listOntologies().size());
    	for (Iterator it = ont.listOntologies().iterator(); it.hasNext();){
    		Ontology ontology = (Ontology) it.next();
    		if (!visitedOntologies.contains(ontology.getIdentifier())){
    			visitedOntologies.add(ontology.getIdentifier());
    			try {
    				ontology.getWsmlVariant();
					System.out.println("Loaded imported ontology: " + ontology.getIdentifier());
					System.out.println("Number of concepts: " + ontology.listConcepts().size());
					Iterator iterator = ontology.listConcepts().iterator();
					while(iterator.hasNext()) {
			        	Concept concept = (Concept) iterator.next();
			        	System.out.println("   concept: " + concept.getIdentifier());
			        }
					if (ontology.listOntologies().size() > 0) {
	    				for(Iterator ite = ontology.listOntologies().iterator(); ite.hasNext();) {
	    					Ontology o = (Ontology) ite.next();
	    					importOntologies(o);
	    				}
	    			}
    			} catch (SynchronisationException e) {
    				System.out.println("Could not load ontology " + ontology.getIdentifier());
    			}
    		}
    	}
	}
	
	private void setup() {
		// This map contains a mapping from locigal URIs to physical locations. 
        HashMap <String, String> mapping = new HashMap <String, String> ();
        mapping.put("http://www.example.org/ontologies/example8", phys_ex8);
        mapping.put("http://www.example.org/ontologies/example3", phys_ex3);
		
        // This map contains preferences for the creation of the wsml parser, 
        // the locator and the wsml validator
        HashMap <String, Object> prefs = new HashMap <String, Object> ();
        
        // Creating a WSML Parser
        prefs.put(Factory.WSMO_FACTORY, Factory.createWsmoFactory(null));
    	prefs.put(Factory.LE_FACTORY, Factory.createLogicalExpressionFactory(null));
    	parser = Factory.createParser(prefs);
    	prefs.clear();
    	
    	// Creating the Locator
    	prefs.put(Factory.PROVIDER_CLASS, "org.deri.wsmo4j.locator.LocatorImpl");
    	prefs.put(Locator.URI_MAPPING, mapping);
    	prefs.put(Factory.WSMO_PARSER, parser);		
    	locator = LocatorManager.createLocator(prefs); 	
		prefs.clear();
	}
	
    private void printError(java.util.List l){
        for (int i=0; i<l.size(); i++) {
            System.out.println("\nError " + (i+1) + ":\n" + l.get(i) + "\n\n --------------------------------");
        }
    }
    
    private void printWarning(List l) {
        for (int i=0; i<l.size(); i++) {
            System.out.println("\nWarning " + (i+1) + ":\n" + l.get(i) + "\n\n -------------------------------");
        }
    }
	
	public static void main(String[] args) throws Exception {
		LocatorExample ex = new LocatorExample();
		ex.setup();
		ex.doTestRun();
	}
	
}
/*
 * $Log: LocatorExample.java,v $
 * Revision 1.3  2007/06/08 07:33:42  nathaliest
 * changed example to load files from package
 *
 * Revision 1.2  2007/04/02 12:13:22  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.1  2006/11/27 11:36:55  nathaliest
 * added an example to show how to use the locator to import ontologies and the validator to either validate imported ontologies or not
 *
 * 
 */