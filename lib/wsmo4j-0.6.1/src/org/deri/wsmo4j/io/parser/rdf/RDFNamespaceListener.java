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

import java.util.*;

import org.openrdf.rio.*;

/**
 * Implementation of an interface defining methods 
 * for receiving namespace declarations from an RDF parser.
 *
 * <pre>
 *  Created on May 02, 2006
 *  Committed by $Author: morcen $
 *  $Source: /cvsroot/wsmo4j/wsmo4j/org/deri/wsmo4j/io/parser/rdf/RDFNamespaceListener.java,v $
 * </pre>
 *
 * @author nathalie.steinmetz@deri.org
 * @version $Revision: 1.3 $ $Date: 2007/04/02 12:13:23 $
 */
public class RDFNamespaceListener implements NamespaceListener{

    private HashMap <String, String> namespaces = null;
    
    public RDFNamespaceListener(HashMap <String, String> namespaces) {
        this.namespaces = namespaces;
    }
    
    /**
     * The namespace listener adds all the namespaces to a HashMap.
     * The prefix is the entry's key, the uri is the value.
     *
     * @param prefix the namespace's prefix
     * @param uri the namespace's uri
     */
    public void handleNamespace(String prefix, String uri) {
        namespaces.put(prefix, uri);
    }

}
/*
 * $Log: RDFNamespaceListener.java,v $
 * Revision 1.3  2007/04/02 12:13:23  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.2  2006/05/15 07:58:12  holgerlausen
 * corrected issues with namespaces and ontology ids
 *
 * Revision 1.1  2006/05/03 13:32:49  nathaliest
 * adding RDF parser
 *
 * 
 */