/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2006, University of Innsbruck, Austria

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
package org.deri.wsmo4j.io.serializer.rdf;

import java.io.*;
import java.util.*;

import org.omwg.ontology.*;
import org.openrdf.model.*;
import org.openrdf.model.Value;
import org.openrdf.vocabulary.*;
import org.wsmo.common.*;

/**
 * Helper type to serialize/deserialize xml element concept
 * 
 * @author not attributable
 */
class NodeConcept {

    static void serialize(Concept concept, WsmlRdfSerializer serializer) {
        if(concept.getIdentifier().toString().equals(RDFS.CLASS)) {
            return;
        } else {
            try {
                Resource name = serializer.getFactory().createURI(concept.getIdentifier().toString());
                
                serializer.getWriter().writeStatement(
                        name,
                        serializer.getFactory().createURI(RDF.TYPE),
                        serializer.getFactory().createURI(RDFS.CLASS));
                
                if (!concept.listSuperConcepts().isEmpty()) {
                    Object[] entities = concept.listSuperConcepts().toArray();
                    for (int i = 0; i < entities.length; i++) {
                        serializer.getWriter().writeStatement(
                                name,
                                serializer.getFactory().createURI(RDFS.SUBCLASSOF),
                                serializer.getFactory().createURI(
                                        ((Entity)entities[i]).getIdentifier().toString()));
                    }
                }
                if (!concept.listNFPValues().isEmpty()) {
                    NodeNFP.serialize(name, concept, serializer);
                }
                if (!concept.listAttributes().isEmpty()) {
                    Object[] attrList = concept.listAttributes().toArray();
                    for (int i = 0; i < attrList.length; i++) {
                        Attribute attribute = (Attribute) attrList[i];
                        
                        serializer.getWriter().writeStatement(
                                serializer.getFactory().createURI(attribute.getIdentifier().toString()),
                                serializer.getFactory().createURI(RDF.TYPE),
                                serializer.getFactory().createURI(RDF.PROPERTY));
                        serializer.getWriter().writeStatement(
                                serializer.getFactory().createURI(attribute.getIdentifier().toString()),
                                serializer.getFactory().createURI(RDFS.DOMAIN),
                                name);
                        Resource attr = serializer.getFactory().createURI(attribute.getIdentifier().toString());                
                        if (!attribute.listTypes().isEmpty()) {
                            Value value = null;
                            for (Iterator j = attribute.listTypes().iterator(); j.hasNext();) {
                                Object o  = j.next();
                                if (o instanceof Concept)
                                    value = serializer.getFactory().createURI(
                                            ((Concept)o).getIdentifier().toString());                        
                                else if (o instanceof WsmlDataType) {
                                    value = serializer.getFactory().createURI(
                                            XmlSchema.NAMESPACE + 
                                            ((WsmlDataType) o).getIRI().getLocalName().toString());                         
                                }
                                else {
                                    throw new RuntimeException("Unknown range for attribute!");
                                }
                                serializer.getWriter().writeStatement(
                                        attr,
                                        serializer.getFactory().createURI(RDFS.RANGE),
                                        value);
                            }
                        }
                        if (!attribute.listNFPValues().isEmpty()) {
                            NodeNFP.serialize(attr,attribute, serializer);
                        }
                    }
                }
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

/*
 * $Log: NodeConcept.java,v $
 * Revision 1.3  2006/11/16 14:35:09  ohamano
 * *** empty log message ***
 *
 * Revision 1.2  2006/11/10 15:38:49  ohamano
 * *** empty log message ***
 *
 * Revision 1.1  2006/11/10 15:02:59  ohamano
 * *** empty log message ***
 *
 * Revision 1.1  2006/11/10 10:08:42  ohamano
 * *** empty log message ***
 *
 *
*/