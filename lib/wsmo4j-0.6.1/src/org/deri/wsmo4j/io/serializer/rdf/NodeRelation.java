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
 * Helper type to serialize/deserialize xml element relation
 * 
 * @author not attributable
 */
class NodeRelation {

    static void serialize(Relation relation, WsmlRdfSerializer serializer) {
        if (!relation.listParameters().isEmpty() && relation.listParameters().size()==2) {
            try {
                URI rel = serializer.getFactory().createURI(relation.getIdentifier().toString());
                
                serializer.getWriter().writeStatement(
                        rel,
                        serializer.getFactory().createURI(RDF.TYPE),
                        serializer.getFactory().createURI(RDF.PROPERTY));
                
                Set domain = relation.listParameters().get(0).listTypes();
                Set range = relation.listParameters().get(1).listTypes();
                
                for (Iterator j = domain.iterator(); j.hasNext();) {
                    Type type = (Type)j.next();
                    Value val = null;
                    if (type instanceof Concept) {
                        val = serializer.getFactory().createURI(
                                ((Concept)type).getIdentifier().toString());
                    }
                    else {
                        val = serializer.getFactory().createURI(
                                XmlSchema.NAMESPACE + 
                                ((WsmlDataType)type).getIRI().getLocalName().toString());
                    }
                    serializer.getWriter().writeStatement(
                            rel,
                            serializer.getFactory().createURI(RDFS.DOMAIN),
                            val);
                }
                for(Iterator i = range.iterator(); i.hasNext();) {
                    Type type = (Type)i.next();
                    Value val = null;
                    if (type instanceof Concept) {
                        val = serializer.getFactory().createURI(
                                ((Concept)type).getIdentifier().toString());
                    }
                    else {
                        val = serializer.getFactory().createURI(
                                XmlSchema.NAMESPACE + 
                                ((WsmlDataType)type).getIRI().getLocalName().toString());
                    }
                    serializer.getWriter().writeStatement(
                            rel,
                            serializer.getFactory().createURI(RDFS.RANGE),
                            val);
                }
                if (!relation.listSuperRelations().isEmpty()) {
                    Object[] entities = relation.listSuperRelations().toArray();
                    for (int i = 0; i < entities.length; i++) {
                        serializer.getWriter().writeStatement(
                                rel,
                                serializer.getFactory().createURI(RDFS.SUBPROPERTYOF),
                                serializer.getFactory().createURI(
                                        ((Entity) entities[i]).getIdentifier().toString()));
                    }
                }
                if (!relation.listNFPValues().isEmpty()) {
                    NodeNFP.serialize(rel, relation, serializer);
                }
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return;
        }
    }
}

/*
 * $Log: NodeRelation.java,v $
 * Revision 1.3  2007/04/02 12:13:23  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
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