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
package org.deri.wsmo4j.io.parser.rdf;

import java.io.*;
import java.util.*;

import org.openrdf.model.*;
import org.openrdf.model.impl.GraphImpl;
import org.openrdf.rio.*;
import org.openrdf.rio.ntriples.NTriplesParser;
import org.openrdf.rio.rdfxml.RdfXmlParser;
import org.openrdf.rio.turtle.TurtleParser;
import org.openrdf.sesame.repository.local.LocalRepository;
import org.openrdf.vocabulary.RDFS;
import org.wsmo.common.TopEntity;
import org.wsmo.common.exception.*;
import org.wsmo.factory.*;
import org.wsmo.wsml.ParserException;
import org.wsmo.wsml.Parser;

/**
 * This class parses RDF files and returns an WSML TopEntity.
 * For parsing, the openrdf rio parser is used.
 *
 * <pre>
 *  Created on May 02, 2006
 *  Committed by $Author: nathaliest $
 *  $Source: /cvsroot/wsmo4j/wsmo4j/org/deri/wsmo4j/io/parser/rdf/RDFParserImpl.java,v $
 * </pre>
 *
 * @see org.wsmo.wsml.RDFParser
 * @see org.openrdf.rio.rdfxml.RdfXmlParser
 * @author nathalie.steinmetz@deri.org
 * @version $Revision: 1.8 $ $Date: 2007/04/20 13:30:44 $
 */
public class RDFParserImpl implements Parser {

	private static final String OLD_RDFS = "http://www.w3.org/TR/1999/PR-rdf-schema-19990303#";

    private String properties = null;
	
    /* RDF_PROPERTIES is put into the parsers property map to indicate whether
     * the properties shall be translated to concept attributes or to relations. 
     * This decision also decides whether:
     * - subProperties are translated to subRelations or to axioms.
     * - instances are translated to instances with attribute values or 
     *   instances with relationInstances.
     */
    public static final String RDF_PROPERTIES = "RDF properties";
    
    public static final String RDF_PROP_AS_ATTR = "RDF properties as attributes";
    
    public static final String RDF_PROP_AS_REL = "RDF properties as relations";
    
    /*
     * The following strings describe different RDF syntaxes that can be parsed
     */
    
    public static final String RDF_SYNTAX = "RDF syntax";
    public static final String RDF_XML_SYNTAX = "RDF/XML Syntax";
    
    public static final String N_TRIPLES_SYNTAX = "N-Triples Syntax";
    
    public static final String TURTLE_SYNTAX = "Turtle Syntax";
    
    // A specified LocalRepository to be used as storage backend.
    public static final String LOCAL_REPOSITORY = "Local Repository";
    
    private LocalRepository repository = null;
    
    // This map collects the rdf file's namespaces.
    private HashMap <String, String> namespaces = null;
    
    // The RdfXmlParser receives a StatementHandler, a ParseErrorListener
    // and a NamespaceListener.
    private org.openrdf.rio.Parser parser = null;
    
    private RDFStatementHandler handler = null;
    
    private RDFParseErrorListener errorListener = null;
    
    private RDFNamespaceListener namespaceListener = null;
  
    // The RDFExprParser needs different factories to 
    // build the Wsml objects.
    private WsmoFactory factory = null;
    
    private LogicalExpressionFactory leFactory = null;
    
    private DataFactory dataFactory = null;
    
    private RDFExprParser rdfParser = null;  
    
    // The RDFParseErrorListener fills  lists with warnings and errors
    private List <RDFParserWarning> warnings = null;
    
    private List <RDFParserError> errors = null;
    
    private Graph graph = null;
    
    public RDFParserImpl(Map map) {
        Object o = map.get(Factory.WSMO_FACTORY);
        if (o == null || !(o instanceof WsmoFactory)) {
            o = Factory.createWsmoFactory(new HashMap <String, Object> ());
        }
        factory = (WsmoFactory) o;
        assert (factory != null);

        o = map.get(Factory.LE_FACTORY);
        if (o == null || !(o instanceof LogicalExpressionFactory)) {
            o = Factory.createLogicalExpressionFactory(new HashMap <String, Object> ());
        }
        leFactory = (LogicalExpressionFactory) o;
        assert (leFactory != null);

        o = map.get(Factory.DATA_FACTORY);
        if (o == null || !(o instanceof DataFactory)) {
            o = Factory.createDataFactory(new HashMap <String, Object> ());
        }
        dataFactory = (DataFactory) o;
        assert (dataFactory != null);
        
        o = map.get(RDF_PROPERTIES);
        if (o == null || !(o.equals(RDF_PROP_AS_ATTR) || o.equals(RDF_PROP_AS_REL))) {
            // by default, rdf properties are translated to concept attributes
            o = RDF_PROP_AS_ATTR;
        }
        properties = (String) o;
        assert (properties != null);
        
        o = map.get(RDF_SYNTAX);
        if (o == null || o.equals(RDF_XML_SYNTAX) || !(o.equals(N_TRIPLES_SYNTAX) || 
        		o.equals(TURTLE_SYNTAX))) {
        	// by default, the parser parses rdf/xml syntax
        	parser = new RdfXmlParser();
        }
        else if (o == N_TRIPLES_SYNTAX) {
        	parser = new NTriplesParser();
        }
        else if (o == TURTLE_SYNTAX) {
        	parser = new TurtleParser();
        }
        
        o = map.get(LOCAL_REPOSITORY);
        if (o == null || !(o instanceof LocalRepository)) {
            repository = null;
        }
        repository = (LocalRepository) o;
        
        warnings = new Vector <RDFParserWarning>();
        errors = new Vector <RDFParserError>();
        
        if (repository == null) {
            // by default, an in-memory RdfRepository is used as storage backend
            graph = new GraphImpl();
        }
        else {
            graph = new GraphImpl(repository);
        }
        namespaces = new HashMap <String, String> ();
        handler = new RDFStatementHandler(graph);
        errorListener = new RDFParseErrorListener(warnings, errors);
        namespaceListener = new RDFNamespaceListener(namespaces);
        rdfParser = new RDFExprParser(factory, leFactory, dataFactory, properties, errorListener);
        parser.setVerifyData(true);
        parser.setStopAtFirstError(false);
        parser.setParseErrorListener(errorListener);
        parser.setNamespaceListener(namespaceListener);  
        parser.setStatementHandler(handler);
    }
    
    /*
     * This is the parse method of the RdfXmlParser. This parser operates 
     * directly on the SAX events generated by a SAX-enabled XML parser. 
     * The supplied baseUri is used to resolve any relative URI references.
     * @param inputStream The input stream to read from
     * @param uri baseUri
     */
    private void parse(Reader src, String uri) throws IOException, ParserException  {
        if (uri == null) {
            uri = "http://www.example.org#";
        }
        Reader result = checkForOldNS(src);
        if (result != null) {
        	src = result;
        }
        // Parse the data from inputStream, resolving any relative
        // URIs against http://www.w3.org/1999/02/22-rdf-syntax-ns#   
        try {
            parser.parse(src, uri);
        } catch (StatementHandlerException e) {
            throw new ParserException(e.getMessage(), e.getCause());
        } catch(ParseException e) {
            throw new ParserException(e.getMessage(), e.getCause());
        }
        try {
            rdfParser.rdf2Wsml(graph, namespaces);
        } catch (GraphException e) {
            throw new ParserException(e.getMessage(), e.getCause());
        } catch (SynchronisationException e) {
            throw new ParserException(e.getMessage(), e.getCause());
        } catch (InvalidModelException e) {
            throw new ParserException(e.getMessage(), e.getCause());
        } catch (ParserException e) {
            throw new ParserException(e.getMessage(), e.getCause());
        }
    }

    /**
     * 
     * @throws IOException 
     * @throws ParserException 
     * @throws ParseException
     * @see org.wsmo.wsml.Parser#parse(java.io.Reader)
     */
    public TopEntity[] parse(Reader src) throws IOException, ParserException, InvalidModelException{
        parse(src, "");
        TopEntity[] te = {rdfParser.getOntology()};
        return te;
    }

    /**
     * 
     * @see org.wsmo.wsml.Parser#parse(java.io.Reader, java.util.Map)
     */
    public TopEntity[] parse(Reader src, Map options) throws IOException, ParserException, InvalidModelException {
        throw new UnsupportedOperationException("Use method parse(Reader) instead!");
    }

    /**
     * 
     * @see org.wsmo.wsml.Parser#parse(java.lang.StringBuffer)
     */
    public TopEntity[] parse(StringBuffer src) throws ParserException, InvalidModelException {
        try {
            return parse(new StringReader(src.toString()));
        } catch (IOException e) {
            // should never happens
            throw new RuntimeException("I/O error occured!", e);
        }
    }

    /**
     * 
     * @see org.wsmo.wsml.Parser#parse(java.lang.StringBuffer, java.util.Map)
     */
    public TopEntity[] parse(StringBuffer src, Map options) throws ParserException, InvalidModelException {
        throw new UnsupportedOperationException("Use method parse(StringBuffer) instead!");
    }

    /**
     * 
     * @see org.wsmo.wsml.Parser#listKeywords()
     */
    public Set <String> listKeywords() {
        return null;
    }
    
    /**
     * This method returns a List containing warnings that 
     * occured during the RDF parsing and the transformation 
     * to the WSMO object model.
     * <br />
     * Some warnings don't simply refer to one "error", but represent 
     * a "kind" of errors. Each warning contains a Set of triples 
     * that belong to that warning. 
     * 
     * @return List of collected warnings
     */
    public List <Object> getWarnings() {
        return new ArrayList <Object>(warnings);
    }
    
    /**
     * This method returns a List containing errors that occured during 
     * the RDF parsing and the transformation to the WSMO object model.
     * 
     * @return List of collected errors
     */
    public List <Object> getErrors() {
        return new ArrayList <Object>(errors);
    }
    
    private Reader checkForOldNS(Reader in) {
    	BufferedReader reader = new BufferedReader(in);
        Reader out = null;
    	String line;
    	String text = "";
        try {
        	while ((line = reader.readLine()) != null) {
        		text = text + line + "\n";
        	}
            reader.close();
        } catch (IOException e) {
        	return null;
        }
        if (text.indexOf(OLD_RDFS) != -1) {
        	text = text.replace(OLD_RDFS, RDFS.NAMESPACE);
        }
        out = new StringReader(text);
        return out;
    }
    
}
/*
 * $Log: RDFParserImpl.java,v $
 * Revision 1.8  2007/04/20 13:30:44  nathaliest
 * added parsing of rdf N-Triple and Turtle syntaxes
 *
 * Revision 1.7  2007/04/02 12:13:23  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.6  2007/03/15 16:38:49  nathaliest
 * fixed bug in rdfParser parse(StringBuffer buf) method
 *
 * Revision 1.5  2006/11/30 13:46:44  nathaliest
 * fixed a bug concerning the call of the parse method with a stringbuffer as parameter
 *
 * Revision 1.4  2006/11/16 11:05:22  nathaliest
 * replace old rdf-schema namespace with new one
 *
 * Revision 1.3  2006/11/16 09:53:28  nathaliest
 * added RDFParserError
 *
 * Revision 1.2  2006/11/10 11:08:54  nathaliest
 * added getWarnings() and getErrors() methods to Parser interface, implemented them in the rdf parser implementation and added UnsupportedOperationException to the other parser implementations
 *
 * Revision 1.1  2006/05/03 13:32:49  nathaliest
 * adding RDF parser
 *
 * 
 */
