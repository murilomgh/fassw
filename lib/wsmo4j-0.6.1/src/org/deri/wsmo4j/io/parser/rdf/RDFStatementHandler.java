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

import org.openrdf.model.Graph;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.rio.StatementHandler;
import org.openrdf.rio.StatementHandlerException;

/**
 * Implementation of an interface defining methods 
 * for receiving RDF statements from an RDF parser.
 *
 * <pre>
 *  Created on May 02, 2006
 *  Committed by $Author: nathaliest $
 *  $Source: /cvsroot/wsmo4j/wsmo4j/org/deri/wsmo4j/io/parser/rdf/RDFStatementHandler.java,v $
 * </pre>
 *
 * @see org.openrdf.rio#StatementHandler
 * @author nathalie.steinmetz@deri.org
 * @version $Revision: 1.1 $ $Date: 2006/05/03 13:32:49 $
 */
public class RDFStatementHandler implements StatementHandler{
    
    private Graph graph = null;
    
    public RDFStatementHandler(Graph graph) {
        this.graph = graph;
    }
    
    /**
     * The statement handler adds all the statements to a graph.
     * 
     * @see org.openrdf.rio.StatementHandler#handleStatement(org.openrdf.model.Resource, org.openrdf.model.URI, org.openrdf.model.Value)
     */
    public void handleStatement(Resource subject, URI predicate, Value object) 
            throws StatementHandlerException {
//        System.out.println("---------------------------------------");
//        System.out.println(subject.toString());
//        System.out.println(predicate.toString());
//        System.out.println(object.toString());
        graph.add(subject, predicate, object);      
    }

}
/*
 * $log: $
 * 
 */
