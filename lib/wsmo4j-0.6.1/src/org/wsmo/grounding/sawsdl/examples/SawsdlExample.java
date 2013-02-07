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

package org.wsmo.grounding.sawsdl.examples;


import java.io.*;
import java.net.URI;
import java.util.List;

import javax.xml.namespace.QName;

import org.wsmo.factory.WsmoFactory;
import org.wsmo.grounding.sawsdl.*;
import org.wsmo.grounding.sawsdl.io.*;

/*
 * An example program demonstrating the usage of the SAWSDL grounding API.
 * The scenario is quite simple:
 *    - opening a WSDL file (SAWSDLexample.wsdl) which is based on the latest SAWSDL specification
 *    - constructing a grounding object (one for the whole file)
 *    - listing the detected SAWSDL annotations
 *    - adding various types of annotation to the grounding object
 *    - listing the new state of the annotation
 *    - printing the result in a new separate file (SAWSDLexampleNew.wsdl)
 */

public class SawsdlExample {

    private static final String INPUT_FILE = "WSDL-input.wsdl";
    private static final String OUTPUT_FILE = "SAWSDL-output.wsdl";

    public static void main(String[] args) throws Exception {

        // Reading the SAWSDL annotation
        Grounding grounding = readGrounding(INPUT_FILE);
        
        // Printing the current state of the annotation
        System.out.println("* * * * * * * *  INITIAL GROUNDING STATE * * * * * * ");
        dumpGrounding(grounding);
        
        // Constructing and adding new annotation objects:
        WsmoFactory factory = org.wsmo.factory.Factory.createWsmoFactory(null);
        String targetNamespace = "http://www.w3.org/2002/ws/sawsdl/spec/wsdl/order#";
        
        grounding.createElementModelRef(
                new QName(targetNamespace, "OrderRequest"), 
                factory.createIRI("http://www.example-one.org#elementRef"));

        grounding.createAttributeModelRef(
                new QName(targetNamespace, "quantity"), 
                factory.createIRI("http://www.example-one.org#attrRef"));

        grounding.createComplexTypeModelRef(
                new QName(targetNamespace, "item"), 
                factory.createIRI("http://www.example-one.org#complexTypeRef"));

        grounding.createSimpleTypeModelRef(
                new QName(targetNamespace, "confirmation"), 
                factory.createIRI("http://www.example-one.org#simpleTypeRef"));

        grounding.createOperationModelRef(
                new QName(targetNamespace, "order"), 
                factory.createIRI("http://www.example-one.org#operationRef"));

        grounding.createFaultModelRef(
                new QName(targetNamespace, "orderFault"), 
                factory.createIRI("http://www.example-one.org#faultRef"));

        grounding.createInterfaceCategory(
                new QName(targetNamespace, "Order"), 
                factory.createIRI("http://www.example-one.org#orderRef"));

        grounding.createLiftingSchemaMapping(
                new QName(targetNamespace, "OrderRequest"), 
                new URI("http://www.example-one.org/Request2RDFOnt.xml"));

        // printing the new state of the annotation
        System.out.println("\n* * * * * * * *  FINAL GROUNDING STATE * * * * * * ");
        dumpGrounding(grounding);

        // Saving the result to a new file
        System.out.print("Saving to " + OUTPUT_FILE + " ...");
        writeGrounding(OUTPUT_FILE, grounding);
        System.out.println("Done");
    }
    
    private static Grounding readGrounding(String inputWSDL) throws Exception {

        Reader in = new InputStreamReader(
                ClassLoader.getSystemResourceAsStream(inputWSDL));
        
        // Instanciate the default implementation of the Grounding parser
        GroundingParser parser = Factory.createParser(null);
        return parser.parse(in);
    }
    
    private static void writeGrounding(String outputWSDL, Grounding grounding) throws Exception {

        Writer out = new OutputStreamWriter(
                new FileOutputStream(outputWSDL));
        
        // Instanciate the default implementation of the Grounding serializer
        GroundingSerializer serializer = Factory.createSerializer(null);
        
        serializer.serialize(grounding, out);
    }

    private static void dumpGrounding(Grounding grounding) throws Exception {
        List<ModelRef> refs = grounding.listModelRefs();
        System.out.println(refs.size() + " model reference(s) detected:");
        for(ModelRef ref : refs) {
            String type;
            if (ref instanceof AttributeModelRef) {
                type = "Attribute";
            }
            else if (ref instanceof ElementModelRef) {
                type = "Element";
            } 
            else if (ref instanceof ComplexTypeModelRef) {
                type = "ComplexType";
            } 
            else if (ref instanceof SimpleTypeModelRef) {
                type = "SimpleType";
            } 
            else if (ref instanceof InterfaceCategory) {
                type = "Interface";
            } 
            else if (ref instanceof OperationModelRef) {
                type = "Operation";
            } 
            else if (ref instanceof FaultModelRef) {
                type = "Fault";
            }
            else {
                type = "unknown";
            }
            System.out.println("  " + type + " [" + ref.getSource().getLocalPart() + "  ->  " + ref.getTarget() + "]");
        }
        List<SchemaMapping> mappings = grounding.listSchemaMappings();
        
        System.out.println(mappings.size() + " schema mappings detected:");
        for(SchemaMapping ref : mappings) {
            String type;
            if (ref instanceof LiftingSchemaMapping) {
                type = "  Lifting";
            }
            else {
                type = "  Lowering";
            } 
            System.out.println(type + " schema mapping [" + ref.getSource() + "  ->  " + ref.getSchema() + "]");
        }

    }

}
