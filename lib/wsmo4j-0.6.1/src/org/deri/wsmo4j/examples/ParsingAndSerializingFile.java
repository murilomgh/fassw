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


import java.io.*;
import java.util.*;

import org.omwg.logicalexpression.*;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.factory.*;
import org.wsmo.wsml.*;


/**
 * <h2>Parsing and serializing a file</h2>
 * <p>Description: Example - How to parse, create and serialize logical expressions</p>
 * <h4>How to parse logical expressions from a file</h4>
 * <p>
 * A Parser is created using the <a href="http://en.wikipedia.org/wiki/Abstract_factory_pattern">
 * Factory Pattern</a> (org.wsmo.factory.Factory). In order to use the logical
 * expression extension a LogicalExpressionFactory has to be created and passed as parameter
 * when creating the parser.
 * </p>
 * <p>
 * If we don't pass specific parameters, the parser is created
 * using wsmo4j default factories. The Logical Expressions factory used by default
 * handles logical expressions as plain strings, instead of objects
 * (variables, molecules, operators,...).
 * </p>
 * <p>
 * Now we we use the parser to parse the wsml file. As
 * result we obtain an array of TopEntities (Ontologies, Goals, Mediators or Web
 * Services). In our test case we can cast the first TopEntity to an ontology, which can be searched for specified
 * concepts, axioms, instances, namespaces,...
 * </p>
 * <h4>How to create logical expressions</h4>
 * <p>
 * To create logical expressions, one needs to use the LogicalExpressionsFactory. This one
 * allows to build logical expressions in two different ways. Either from a string
 * (ex: ?x memberOf _"http://example.org/animal"), or by building it up directly
 * using different objects like variables, IRIs, molecules, etc. No matter how an expression is
 * created it will be equal.
 * </p>
 * <h4>How to serialize logical expressions</h4>
 * <p>
 * First we need to create a serializer. This one is, analogous to the parser, created by the
 * Factory, with as parameter simply "null".
 * The TopEntity, consisting of the ontology and the ontology items is serialized to a string.
 * This one can be written into a wsml file.
 * </p>
 * <p>
 * With the toString(), it is also possible to convert a logical expression
 * directly to a string, without using the serializer.
 * </p>
 * @author Nathalie.Steinmetz@deri.org
 */
public class ParsingAndSerializingFile {

    private String file = "wsmo4j/org/deri/wsmo4j/examples/locations.wsml";

    private String fileOut = "wsmo4j/org/deri/wsmo4j/examples/locations2.wsml";

    protected Parser parser;

    protected Serializer serializer;

    protected WsmoFactory factory;

    protected LogicalExpressionFactory leFactory;

    protected Ontology ontology;

    public ParsingAndSerializingFile() {

        /* create parameters for the parser */
        HashMap <String, Object> createParams = new HashMap <String, Object> ();
        createParams.put(Factory.PROVIDER_CLASS, "org.deri.wsmo4j.factory.LogicalExpressionFactoryImpl");
        leFactory = Factory.createLogicalExpressionFactory(createParams);
        factory = Factory.createWsmoFactory(null);
        createParams = new HashMap <String, Object> ();
        createParams.put(Factory.WSMO_FACTORY, factory);
        createParams.put(Factory.LE_FACTORY, leFactory);

        /* create parser */
        parser = Factory.createParser(createParams);

        /* create serializer */
        serializer = Factory.createSerializer(null);

        /*
                 * parse the existing wsml-file, add and remove some logical expressions and then serialize the ontology into a new
         * file.
         */
        parseFile();
        try {
            addLogExp();
        }
        catch (Exception e) {}
        serializeToFile();
    }

    /**
     * Read a wsml-file and parse it. See how many concepts, axioms and instances are in the
     * resulting ontology. Search for a specific axiom and for a specific namespace.
     */
    public void parseFile() {
        /* open file, read and parse it */
        try {
            FileReader fReader = new FileReader(file);
            /* assuming first topentity in file is an ontology */
            ontology = (Ontology)parser.parse(fReader)[0];
        }
        catch (FileNotFoundException ef) {
            ef.printStackTrace();
        }
        catch (IOException eio) {
            eio.printStackTrace();
        }
        catch (ParserException pe) {
            pe.printStackTrace();
        }
        catch (InvalidModelException ime) {
            ime.printStackTrace();
        }

        /* get number of concepts, axioms and instances from the ontology */
        int x = ontology.listConcepts().size();
        System.out.println("Number of concepts: " + x);
        int y = ontology.listAxioms().size();
        System.out.println("Number of axioms: " + y);
        int i = ontology.listInstances().size();
        System.out.println("Number of instances: " + i);

        /* search for a specified axiom by indicating the default namespace as identifier */
        Axiom a = ontology.findAxiom(
                factory.createIRI("http://www.example.org/example/validDistance"));
        if (a != null) {
            System.out.println("axiom found " + a.getIdentifier());
            a.getOntology();
        }

        /* search for a specified namespace by indicating the searched-for prefix */
        Namespace n = ontology.findNamespace("dc");
        if (n != null) {
            System.out.println("found Namespace with prefix \"dc\": " + n.getIRI().toString());
        }
    }

    /**
     * A simple axiom is created and added to the ontology. The axiom is defined by a binary
     * logical expression. The LogicalExpressionFactory is used to build the axiom.
     */
    public void addLogExp()
            throws ParserException {
        /* create Molecules (simple logical expressions) */
        try {
            Molecule m1 = leFactory.createMemberShipMolecule(
                    leFactory.createVariable("x"),
                    factory.createIRI("http://www.example.org/example/animal"));
            Molecule m2 = leFactory.createMemberShipMolecule(
                    leFactory.createVariable("x"),
                    factory.createIRI("http://www.example.org/example/human"));

            /* create Binaries (binary logical expressions) */
            Binary b1 = leFactory.createInverseImplication(m1, m2);

            /* Alternative way of creating the same logical expression from a String */
            Binary b2 = (Binary)leFactory.createLogicalExpression(
                    "?x memberOf animal impliedBy ?x memberOf human", ontology);

            /* Check if both Binary Versions are equal */
            if (b1.equals(b2)) {
                System.out.println(b1 + " is equal to " + b2);
            }
            else {
                System.out.println(b1 + " is unequal to " + b2);
            }

            /* creating an axiom and add it to the ontology */
            Axiom a = factory.createAxiom(factory.createIRI(
                    "http://www.example.org/example/animalImpliedByHuman"));

            a.addDefinition(b1);
            /* add axiom */
            ontology.addAxiom(a);
        }
        catch (InvalidModelException ime) {
            ime.getStackTrace();
        }

        /* get number of axioms */
        int y = ontology.listAxioms().size();
        System.out.println("Number of axioms: " + y);
    }

    /**
     * The ontology with the added axiom is serialized and written into the file
     * "SerializedLocations.wsml"
     */
    public void serializeToFile() {
        StringBuffer str = new StringBuffer();
        TopEntity[] tops = {ontology};
        serializer.serialize(tops, str);
        try {
            BufferedWriter out = new BufferedWriter(
                    new FileWriter(fileOut));
            out.write(str.toString());
            out.close();
        }
        catch (IOException ioe) {
            ioe.getStackTrace();
        }
    }

    public static void main(String[] args) {
        new ParsingAndSerializingFile();
    }

}
