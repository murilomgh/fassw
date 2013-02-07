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
package com.ontotext.wsmo4j.parser.xml;

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.deri.wsmo4j.io.parser.xml.LogExprParserImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wsmo.common.TopEntity;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.factory.DataFactory;
import org.wsmo.factory.Factory;
import org.wsmo.factory.LogicalExpressionFactory;
import org.wsmo.factory.WsmoFactory;
import org.wsmo.wsml.Parser;
import org.wsmo.wsml.ParserException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ontotext.wsmo4j.parser.WrappedInvalidModelException;

/**
 * Parse XML file and creates WSMO obkjects (check wsml-xml-syntax.xsd)
 *
 * @author not attributable
 */
public class WsmlXmlParser implements Parser {

    private static final boolean isValidating = false;

    private WsmoFactory wsmoFactory;

    private LogicalExpressionFactory leFactory;

    private DataFactory dataFactory;

    private LogExprParserImpl leParser;

    /**
     *  Map properties = new TreeMap();
     *  properties.put(Factory.PROVIDER_CLASS, "com.ontotext.wsmo4j.xmlparser.WsmlXmlParser");
     *  properties.put(Parser.PARSER_WSMO_FACTORY, factory);
     *  properties.put(Parser.PARSER_LE_FACTORY, leFactory);
     *  parser = Factory.createParser(properties);
     *
     * @param map Initialization properties, requires Parser.PARSER_LE_FACTORY
     *  and Parser.PARSER_WSMO_FACTORY
     */
    public WsmlXmlParser(Map map) {
        wsmoFactory = (WsmoFactory) map.get(Factory.WSMO_FACTORY);
        leFactory = (LogicalExpressionFactory) map.get(Factory.LE_FACTORY);
        dataFactory = (DataFactory) map.get(Factory.DATA_FACTORY);
        if (wsmoFactory == null) {
            wsmoFactory = Factory.createWsmoFactory(new HashMap <String, Object> ());
        }
        if (leFactory == null) {
            leFactory = Factory.createLogicalExpressionFactory(new HashMap <String, Object> ());
        }
        if (dataFactory == null) {
            dataFactory = Factory.createDataFactory(new HashMap <String, Object> ());
        }

        assert wsmoFactory != null;
        assert leFactory != null;
        assert dataFactory != null;

        leParser = new LogExprParserImpl(map);
    }

    WsmoFactory getFactory() {
        return wsmoFactory;
    }

    LogicalExpressionFactory getLEFactory() {
        return leFactory;
    }

    DataFactory getDataFactory() {
        return dataFactory;
    }

    LogExprParserImpl getLEParser() {
        return leParser;
    }

    /**
     * Parses a XML source
     *
     * @param src Reader to the xml file
     */
    public TopEntity[] parse(Reader src) throws IOException, ParserException {
        return parse(new InputSource(src));
    }

    /**
     * Parses a XML source
     *
     * @param src Reader to the xml file
     * @param options Ignored
     */
    public TopEntity[] parse(Reader src, Map options) throws IOException, ParserException {
        return parse(src);
    }

    /**
     * Parses a XML source
     *
     * @param src Source xml
     */
    public TopEntity[] parse(StringBuffer src) throws ParserException {
        try {
            return parse(new InputSource(new StringReader(src.toString())));
        }
        catch (IOException e) {
            throw new RuntimeException("IOException occured! " + e.getMessage(), e);
        }
    }

    /**
     * Parses a XML source
     *
     * @param src Source xml
     * @param options Ignored
     */
    public TopEntity[] parse(StringBuffer src, Map options) throws ParserException {
        return parse(src);
    }

    private TopEntity[] parse(InputSource source) throws IOException, ParserException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            factory.setNamespaceAware(true);

            if (isValidating) {
                try {
                    /*factory.setValidating(true);
                    factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                            "http://www.w3.org/2001/XMLSchema");
                    factory
                            .setAttribute(
                                    "http://java.sun.com/xml/jaxp/properties/schemaSource",
                                    new File("URL TO THE SCHEMA"));
                                            */
                }
                catch (IllegalArgumentException e) {
                    System.out.println("Schema validation is disabled!");
                    System.out
                            .println("Xml parser implementaion does not support JAXP 1.2 "
                                    + "To change the JAXP implementation with Xerces (or another alternative JAXP "
                                    + "1.2 enabled parser start with -Djavax.xml.parsers.DocumentBuilderFactory="
                                    + "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
                }
            }

            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            Document document = docBuilder.parse(source);
            NodeList nodes = document.getElementsByTagName("wsml");
            Vector <TopEntity> topEntities = new Vector <TopEntity> ();

            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                topEntities.add(NodeWsml.deserialzieTopEntity(node, this));
                assert topEntities.elementAt(i) != null;
            }

            return topEntities.toArray(new TopEntity[]{});
        }
        catch (InvalidModelException e) {
            throw new WrappedInvalidModelException(e);
        }
        catch (SAXException e) {
            throw new ParserException(e.getMessage(), e);
        }
        catch (ParserConfigurationException e) {
            throw new ParserException(e.getMessage(), e);
        }
    }

    public Set <String> listKeywords() {
        return new HashSet <String>();
    }
    
    /*
     *  (non-Javadoc)
     * @see org.wsmo.wsml.Parser#getWarnings()
     */
    public List <Object> getWarnings() {
		throw new UnsupportedOperationException("This method is not implemented for XML parsing");
	}
    
    /*
     *  (non-Javadoc)
     * @see org.wsmo.wsml.Parser#getErrors()
     */
	public List <Object> getErrors() {
		throw new UnsupportedOperationException("This method is not implemented for XML parsing");
	}
}

/*
 * $Log: WsmlXmlParser.java,v $
 * Revision 1.8  2007/04/02 12:19:08  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.7  2007/04/02 12:13:22  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.6  2006/11/10 11:08:53  nathaliest
 * added getWarnings() and getErrors() methods to Parser interface, implemented them in the rdf parser implementation and added UnsupportedOperationException to the other parser implementations
 *
 * Revision 1.5  2006/01/13 10:06:56  vassil_momtchev
 * removed hardcoded schema url. schema validation is still not used.
 *
 * Revision 1.4  2006/01/13 09:59:10  vassil_momtchev
 * exceptions handling refactored; isValidation const added
 *
 * Revision 1.3  2006/01/11 13:03:30  marin_dimitrov
 * common constants moved to Factory
 *
 * Revision 1.2  2005/12/09 11:29:32  vassil_momtchev
 * listkeywords method added; implemented to return empty set
 *
 * Revision 1.1  2005/11/28 14:03:48  vassil_momtchev
 * package refactored from com.ontotext.wsmo4j.xmlparser to com.ontotext.wsmo4j.parser.xml
 *
 * Revision 1.6  2005/09/21 06:28:56  holgerlausen
 * removing explicit factory creations and introducing parameter maps for parsers
 *
 * Revision 1.5  2005/09/17 09:02:51  vassil_momtchev
 * datafactory is passed with the map in constructor
 *
 * Revision 1.4  2005/09/02 09:43:32  ohamano
 * integrate wsmo-api and le-api on api and reference implementation level; full parser, serializer and validator integration will be next step
 *
 * Revision 1.3  2005/08/08 08:15:00  vassil_momtchev
 * javadoc added, implemented all parse methods, optimize xml transformation
 *
 */
