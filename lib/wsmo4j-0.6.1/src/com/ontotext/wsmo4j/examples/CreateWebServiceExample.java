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

import java.io.PrintWriter;
import java.util.HashMap;

import org.omwg.logicalexpression.*;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.factory.*;
import org.wsmo.service.*;
import org.wsmo.wsml.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004-2005</p>
 * <p>Company: Ontotext Lab., Sirma AI</p>
 * @author not attributable
 * @version 1.0
 */

public class CreateWebServiceExample {

    private WsmoFactory factory;

    private LogicalExpressionFactory leFactory;

    private DataFactory dataFactory;

    private static String SERVICE_ID = "http://www.wsmo.org/2004/d3/d3.3/v0.1/20041008/resources/ws.wsml";
    
    public static void main(String[] args) throws Exception{
        WebService service = showExample();
      
        Serializer serializer = Factory.createSerializer(new HashMap<String, Object>(0));
        serializer.serialize(new TopEntity[] {service}, new PrintWriter(System.out));
    }
    
    public static WebService showExample() throws InvalidModelException, ParserException {
        CreateWebServiceExample example = new CreateWebServiceExample();
        example.createFactory();
        return example.createWebservice();
    }

    private void createFactory() {
        // use default implementation for the factories
        factory = Factory.createWsmoFactory(null);
        leFactory = Factory.createLogicalExpressionFactory(null);
        dataFactory = Factory.createDataFactory(null);
    }

    private WebService createWebservice() throws InvalidModelException, ParserException {
        IRI serviceIRI = factory.createIRI(SERVICE_ID);
        WebService service = factory.createWebService(serviceIRI);
        Namespace targetNamespace = factory.createNamespace("targetnamespace", 
                            factory.createIRI("http://www.wsmo.org/2004/d3/d3.3/v0.1/20041008/resources/ws#"));

        // set namespaces
        service.setDefaultNamespace(factory.createNamespace("", 
                factory.createIRI("http://www.wsmo.org/2004/d3/d3.3/v0.1/20041008/resources/ws#")));
        service.addNamespace(factory.createNamespace("dt", 
                factory.createIRI("http:://www.wsmo.org/ontologies/dateTime#")));
        service.addNamespace(factory.createNamespace("tc", 
                factory.createIRI("http:://www.wsmo.org/ontologies/trainConnection#")));
        service.addNamespace(factory.createNamespace("po", 
                factory.createIRI("http://www.wsmo.org/ontologies/purchase#")));  
        service.addNamespace(factory.createNamespace("loc", 
                factory.createIRI("http:://www.wsmo.org/ontologies/location#")));
        service.addNamespace(factory.createNamespace("ucaswe", 
                factory.createIRI("http://www.wsmo.org/2004/d3/d3.3/v0.1/20041008/resources/")));
        service.addNamespace(targetNamespace);
        
        // set NFP values
        service.addNFPValue(factory.createIRI(NFP.DC_TITLE), dataFactory
                .createWsmlString("OEBB Online Ticket Booking Web Service"));
        service.addNFPValue(factory.createIRI(NFP.DC_CREATOR), dataFactory
                .createWsmlString("DERI International"));
        service.addNFPValue(factory.createIRI(NFP.DC_DESCRIPTION), dataFactory
                .createWsmlString("Web service for booking online train "
                        + " tickets for Austria and Germany"));

        // import used ontologies
        service.addOntology(factory.getOntology(factory
                .createIRI("http:://www.wsmo.org/ontologies/dateTime")));
        service.addOntology(factory.getOntology(factory
                .createIRI("http:://www.wsmo.org/ontologies/trainConnection")));
        service.addOntology(factory.getOntology(factory
                .createIRI("http:://www.wsmo.org/ontologies/purchase")));
        service.addOntology(factory.getOntology(factory
                .createIRI("http:://www.wsmo.org/ontologies/location")));

        // create capability
        Capability capability = factory.createCapability(factory.createIRI(targetNamespace, "capability"));
        Axiom axiom = null; 
        LogicalExpression logExp = null;
        
        // add capability pre-condition
        axiom = factory.createAxiom(factory.createIRI(targetNamespace, "axiom1"));
        logExp = leFactory.createLogicalExpression(
                  "?Buyer memberOf po#buyer and \n"
                + " ?Trip memberOf tc#trainTrip[ \n" 
                + "      tc#start hasValue ?Start, \n"
                + "      tc#end hasValue ?End, \n" 
                + "      tc#departure hasValue ?Departure \n"
                + "] and \n" 
                + "?Start[locatedIn hasValue ?StartLocation] and \n " 
                + "(?StartLocation = austria or ?StartLocation = germany) and \n"
                + "?End[locatedIn hasValue ?EndLocation] and \n"
                + "(?EndLocation = austria or ?EndLocation = germany) and \n"
                + "(?Departure > currentDate()).", service);
        axiom.addDefinition(logExp);
        capability.addPreCondition(axiom);
        
        // add capability assumption
        axiom = factory.createAxiom(factory.createIRI(targetNamespace, "axiom2"));
        logExp = leFactory.createLogicalExpression(
                  "?CreditCard memberOf creditCard[ \n"
                + "      cardholdername hasValue ?someHolder, \n"
                + "      creditcardidentifier hasValue ?someIdentifier, \n"
                + "      expirydate hasValue ?someExpiration, \n"
                + "      globalcreditcardclassificationcode hasValue \"Master Card\" \n"
                + "] \n"
                + "and ?someHolder memberOf po#cardHolderName[ \n"
                + "            po#freeformtext hasValue \"Tim Berners-Lee\"\n "
                + "] \n"
                + "and ?someIdentifier memberOf creditCardIdentifier[ \n"
                + "      proprietaryreferenceidentifier hasValue \"5535 4464 6686 7747\"\n"
                + "] \n" 
                + "and ?someExpiration memberOf expiryDate[ \n"
                + "      expmonth hasValue 09, \n"
                + "      expyear hasValue 2007 \n"
                + "]\n", service);
        axiom.addDefinition(logExp);
        capability.addAssumption(axiom);
        
        // add capability post-condition
        axiom = factory.createAxiom(factory.createIRI(targetNamespace, "axiom3"));
        logExp = leFactory.createLogicalExpression("?Trip memberOf trainTrip[ \n"
                + "      start hasValue ?Start, \n" 
                + "      end hasValue ?End, \n"
                + "      departure hasValue ?Departure \n" 
                + "] and \n"
                + "?Start[locatedIn hasValue ?StartLocation] and \n " 
                + "(?StartLocation = austria or ?StartLocation = germany) and \n"
                + "?End[locatedIn hasValue ?EndLocation] and \n"
                + "(?EndLocation = austria or ?EndLocation = germany) and \n"
                + "(?Departure > currentDate()).", service);
        axiom.addDefinition(logExp);
        capability.addPostCondition(axiom);
        
        // add capability effect
        axiom = factory.createAxiom(factory.createIRI(targetNamespace, "axiom4"));
        logExp = leFactory.createLogicalExpression(
                "?someTrade memberOf trade[\n"
                + "      items hasValue ?Trip, \n"
                + "      payment hasValue ?acceptedPayment\n" 
                + "]\n"
                + "and ?acceptedPayment memberOf creditCard .\n", service);
        axiom.addDefinition(logExp);
        capability.addEffect(axiom);
        service.setCapability(capability);
        
        // set webservice interface
        Interface i = factory.createInterface(factory.createIRI(targetNamespace, "interface"));
        service.addInterface(i);
        
        return service;
    }
}

/*
 * $Log: CreateWebServiceExample.java,v $
 * Revision 1.3  2007/06/07 08:24:45  lcekov
 * fix examples http://jira.sirma.bg/jira/browse/WSMO4J-20
 *
 * Revision 1.2  2005/09/19 13:53:11  holgerlausen
 * corrected example syntactically, still need to do a more senseful
 *
 * Revision 1.1  2005/09/19 10:21:12  vassil_momtchev
 * inherite doc/examples/ CreateServiceExample LoadServiceExample
 *
*/
