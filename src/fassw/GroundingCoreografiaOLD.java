package fassw;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import org.omwg.logicalexpression.LogicalExpression;
import org.omwg.ontology.Axiom;
import org.omwg.ontology.Concept;
import org.omwg.ontology.Ontology;
import org.wsmo.common.IRI;
import org.wsmo.common.Identifier;
import org.wsmo.common.NFP;
import org.wsmo.common.Namespace;
import org.wsmo.common.TopEntity;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.common.exception.SynchronisationException;
import org.wsmo.factory.DataFactory;
import org.wsmo.factory.Factory;
import org.wsmo.factory.LogicalExpressionFactory;
import org.wsmo.factory.WsmoFactory;
import org.wsmo.service.Capability;
import org.wsmo.service.Interface;
import org.wsmo.service.WebService;
import org.wsmo.wsml.Serializer;

/**
 * 
 * @author Murilo Honorio
 * @version 0.0
 */
public class GroundingCoreografiaOLD {

    public void algoritmo() throws SynchronisationException, InvalidModelException, IOException {
        WsmoFactory factory = Factory.createWsmoFactory(null);
        LogicalExpressionFactory leFactory = Factory.createLogicalExpressionFactory(null);
        DataFactory dataFactory = Factory.createDataFactory(null);
        
        String SERVICE_ID = "http://www.wsmo.org/2004/d3/d3.3/v0.1/20041008/resources/ws.wsml";
        IRI serviceIRI = factory.createIRI(SERVICE_ID);
        
        WebService service = factory.createWebService(serviceIRI);
        
        Namespace targetNamespace = factory.createNamespace("targetnamespace", 
                factory.createIRI("http://www.wsmo.org/2004/d3/d3.3/v0.1/20041008/resources/ws#"));
        
        service.setDefaultNamespace(factory.createNamespace("", 
                factory.createIRI("http://www.wsmo.org/2004/d3/d3.3/v0.1/20041008/resources/ws#")));
      
          service.addNamespace(factory.createNamespace("dt", 
                  factory.createIRI("http://www.wsmo.org/ontologies/dateTime#")));
          service.addNamespace(factory.createNamespace("tc", 
                  factory.createIRI("http://www.wsmo.org/ontologies/trainConnection#")));
          service.addNamespace(factory.createNamespace("po", 
                  factory.createIRI("http://www.wsmo.org/ontologies/purchase#")));  
          service.addNamespace(factory.createNamespace("loc", 
                  factory.createIRI("http://www.wsmo.org/ontologies/location#")));
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
                 .createIRI("http://www.wsmo.org/ontologies/dateTime")));
         service.addOntology(factory.getOntology(factory
                 .createIRI("http://www.wsmo.org/ontologies/trainConnection")));
         service.addOntology(factory.getOntology(factory
                 .createIRI("http://www.wsmo.org/ontologies/purchase")));
         service.addOntology(factory.getOntology(factory
                 .createIRI("http://www.wsmo.org/ontologies/location")));
 
         // create capability
         Capability capability = factory.createCapability(factory.createIRI(targetNamespace, "capability"));
         Axiom axiom = null; 
         LogicalExpression logExp = null;
         
         // set webservice interface
         Interface i = factory.createInterface(factory.createIRI(targetNamespace, "interface"));
         service.addInterface(i);
        
         Ontology ontology = factory.createOntology(serviceIRI);
         Concept concept = factory.createConcept(factory.createIRI("httt://www.wsmo.org/ontologies/x"));
         Identifier id = factory.createAnonymousID();
         concept.createAttribute(id);
        
         Serializer serializer = Factory.createSerializer(new HashMap<String, Object>(0));
          serializer.serialize(new TopEntity[] {service}, new PrintWriter(System.out));
    }
}
