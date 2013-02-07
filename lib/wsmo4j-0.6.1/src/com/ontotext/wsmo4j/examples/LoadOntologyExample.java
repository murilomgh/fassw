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

import java.io.*;
import java.util.*;

import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.factory.*;
import org.wsmo.wsml.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004-2005</p>
 * <p>Company: Ontotext Lab., Sirma AI</p>
 * @author not attributable
 * @version 1.0
 */

public class LoadOntologyExample {

    private WsmoFactory factory;

    private LogicalExpressionFactory leFactory;

    private DataFactory dataFactory;

    private Parser parser;

    private Serializer serializer;

    public static void showExample() throws InvalidModelException {
        LoadOntologyExample example = new LoadOntologyExample();
        example.createFactoryParserSerializer();
        example.serializeParseOntology();
    }

    private void createFactoryParserSerializer() {
        HashMap <String, Object> props = new HashMap <String, Object>();

        // use default implementation for factory
        factory = Factory.createWsmoFactory(null);
        leFactory = Factory.createLogicalExpressionFactory(null);
        dataFactory = Factory.createDataFactory(null);

        props.put(Factory.WSMO_FACTORY, factory);
        props.put(Factory.LE_FACTORY, leFactory);
        props.put(Factory.DATA_FACTORY, dataFactory);

        parser = Factory.createParser(props);
        serializer = Factory.createSerializer(null);
    }

    private void serializeParseOntology() throws InvalidModelException {

        // create ontology to be serialzed
        Ontology ontology = CreateOntologyExample.showExample();

        try {
            // tries to serialize ontology with all its elements to a text
            StringWriter writer = new StringWriter();
            serializer.serialize(new TopEntity[] { ontology }, writer);
            System.out.println(writer.getBuffer().toString());

            // tries to parse it back to the memmory
            TopEntity[] entities = parser.parse(new StringReader(writer.getBuffer().toString()));
            if (entities.length != 1) {
                throw new RuntimeException("The number of parsed entities is not 1!");
            }
        }
        catch (IOException ex) {
            // should never happen using StringWriter and StringReader objects;
        }
        catch (ParserException ex) {
            System.out.println("Invalid WSML token encountered at line " + ex.getErrorLine()
                    + " position " + ex.getErrorPos());
        }
    }
    
    public static void main(String[] args) throws InvalidModelException {
        showExample();
    }
}

/*
 * $Log: LoadOntologyExample.java,v $
 * Revision 1.4  2007/06/07 08:24:45  lcekov
 * fix examples http://jira.sirma.bg/jira/browse/WSMO4J-20
 *
 * Revision 1.3  2007/04/02 12:13:27  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.2  2006/01/11 13:02:41  marin_dimitrov
 * common constants moved to Factory
 *
 * Revision 1.1  2005/09/19 10:20:22  vassil_momtchev
 * inherite OntologyExample
 *
*/
