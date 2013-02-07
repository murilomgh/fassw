package org.deri.wsmo4j.examples;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;

import org.omwg.ontology.Ontology;
import org.wsmo.common.TopEntity;
import org.wsmo.factory.Factory;
import org.wsmo.wsml.Parser;
import org.wsmo.wsml.Serializer;

/**
 * This class is meant to be used as example for how to use the OWL Serializer 
 * from the wsml2reasoner framework.
 * The Serializer transforms a WSML-DL ontology to OWL DL.
 * 
 * !! Dependeny: wsml2reasoner package, including the OWL API, to retrieve from 
 *    http://dev1.deri.at/wsml2reasoner/
 * 
 * !! To use this example, the wsmo4j project java compiler compliance level 
 *    needs to be on Java 5.0.
 * 
 * <pre>
 * Created on Sep 19, 2006
 * Committed by $Author: morcen $
 * $Source: /cvsroot/wsmo4j/wsmo4j/org/deri/wsmo4j/examples/OWLSerializerExample.java,v $,
 * </pre>
 * 
 * @author nathalie.steinmetz@deri.org
 * @version $Revision: 1.3 $ $Date: 2007/04/02 12:13:22 $
 *
 */
public class OWLSerializerExample {

    private Parser parser;
    
    private Ontology ontology;
    
	public OWLSerializerExample() {
		parser = Factory.createParser(null);
	}
	
	public static void main(String[] args) throws Exception {
		OWLSerializerExample ex = new OWLSerializerExample();
		ex.doTestRun();

	}
	
	public void doTestRun() throws Exception {
		// read test file and parse it 
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(
                "org/deri/wsmo4j/examples/wsml2owlExample.wsml");
        // assuming first topentity in file is an ontology  
        ontology = (Ontology)parser.parse(new InputStreamReader(is))[0]; 
		
		/* create parameters for the parser */
        HashMap <String, Object> createParams = new HashMap <String, Object> ();
        createParams.put(Factory.PROVIDER_CLASS,
                         "org.wsml.reasoner.serializer.owl.WsmlOwlSerializer");		
		Serializer serializer = Factory.createSerializer(createParams);
		
		/* use serializer */
		Writer writer = new StringWriter();
		TopEntity[] te = {ontology};
		serializer.serialize(te, writer);
		System.out.println(writer.toString());
	}

}
/*
 * $Log: OWLSerializerExample.java,v $
 * Revision 1.3  2007/04/02 12:13:22  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.2  2006/09/20 09:13:49  nathaliest
 * *** empty log message ***
 *
 * 
 */
