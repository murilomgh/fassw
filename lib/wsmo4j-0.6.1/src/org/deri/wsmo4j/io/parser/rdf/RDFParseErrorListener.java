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

import java.util.List;

import org.openrdf.model.Statement;
import org.openrdf.rio.ParseErrorListener;

/**
 * Implementation of an interface defining methods 
 * for receiving warning and error messages from an RDF parser.
 *
 * <pre>
 *  Created on May 02, 2006
 *  Committed by $Author: morcen $
 *  $Source: /cvsroot/wsmo4j/wsmo4j/org/deri/wsmo4j/io/parser/rdf/RDFParseErrorListener.java,v $
 * </pre>
 *
 * @author nathalie.steinmetz@deri.org
 * @version $Revision: 1.3 $ $Date: 2007/04/02 12:13:23 $
 */
public class RDFParseErrorListener implements ParseErrorListener{
    
    private List <RDFParserWarning> warnings = null;
    
    private List <RDFParserError> errors = null;
    
    public RDFParseErrorListener(List <RDFParserWarning> warnings, List <RDFParserError> errors) {
        this.warnings = warnings;
        this.errors = errors;
    }

    /**
     * All warnings that occur during the parsing or the transformation 
     * to a WSMO object model are collected in a List of warnings.
     * 
     * @param msg warning message
     * @param lineNo a line number related to the warning, or -1 if not available or applicable
     * @param colNo a column number related to the warning, or -1 if not available or applicable
     * @see org.openrdf.rio.ParseErrorListener#warning(java.lang.String, int, int)
     * @see RDFParseErrorListener#warning(String msg, int lineNo, int colNo, Statement statement)
     */
    public void warning(String msg, int lineNo, int colNo) {
        warning(msg, lineNo, colNo, null);
    }
    
    /**
     * All warnings that occur during the parsing or the transformation 
     * to a WSMO object model are collected in a List of warnings. All statements that 
     * are concerned by one type of warning are put into a Set of triples belonging 
     * to this warning.
     * 
     * @param msg warning message
     * @param lineNo a line number related to the warning, or -1 if not available or applicable
     * @param colNo a column number related to the warning, or -1 if not available or applicable
     * @param statement the RDF triple that is concerned by this warning
     */
    public void warning(String msg, int lineNo, int colNo, Statement statement) {
        RDFParserWarning w = new RDFParserWarning(msg, lineNo, colNo);
        if (!warnings.contains(w)) {
            warnings.add(w);
        }
        if (statement != null) {
            w = warnings.get(warnings.indexOf(w));
            w.addToTriples(statement);
        }
    }
    
    /**
     * All errors that occur during the parsing or the transformation 
     * to a WSMO object model are collected in a List of erros.
     * 
     * @param msg error message
     * @param lineNo a line number related to the error, or -1 if not available or applicable
     * @param colNo a column number related to the error, or -1 if not available or applicable
     * @see org.openrdf.rio.ParseErrorListener#error(java.lang.String, int, int)
     */
    public void error(String msg, int lineNo, int colNo) {
        RDFParserError e = new RDFParserError(msg, lineNo, colNo);
        if (!errors.contains(e)) {
        	errors.add(e);
        }
    }

    /**
     * 
     * @param msg fatal error message
     * @param lineNo a line number related to the fatalError, or -1 if not available or applicable
     * @param colNo a column number related to the fatalError, or -1 if not available or applicable
     * @see org.openrdf.rio.ParseErrorListener#fatalError(java.lang.String, int, int)
     */
    public void fatalError(String msg, int lineNo, int colNo) {
    	RDFParserError e = new RDFParserError(msg, lineNo, colNo);
        if (!errors.contains(e)) {
        	errors.add(e);
        }
    }
    
}
/*
 * $Log: RDFParseErrorListener.java,v $
 * Revision 1.3  2007/04/02 12:13:23  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.2  2006/11/16 09:53:28  nathaliest
 * added RDFParserError
 *
 * Revision 1.1  2006/05/03 13:32:49  nathaliest
 * adding RDF parser
 *
 * 
 */
