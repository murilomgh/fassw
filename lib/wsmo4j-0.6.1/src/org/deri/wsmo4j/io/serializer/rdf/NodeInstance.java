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
import org.openrdf.vocabulary.*;
import org.wsmo.common.*;

/**
 * Helper type to serialize/deserialize xml element instance
 * 
 * @author not attributable
 */
class NodeInstance {
    
    static void serialize(Instance instance, WsmlRdfSerializer serializer) {
        try {
            Resource inst = serializer.getFactory().createURI(instance.getIdentifier().toString());
            
            if (!instance.listConcepts().isEmpty()) {
                Object[] entities = instance.listConcepts().toArray();
                for (int i = 0; i < entities.length; i++) {
                    serializer.getWriter().writeStatement(
                            inst,
                            serializer.getFactory().createURI(RDF.TYPE),
                            serializer.getFactory().createURI(((Entity) entities[i])
                                    .getIdentifier().toString()));
                }
            } else {
                serializer.getWriter().writeStatement(
                        inst,
                        serializer.getFactory().createURI(RDF.TYPE),
                        serializer.getFactory().createURI(RDFS.RESOURCE));
            }
            if (!instance.listNFPValues().isEmpty()) {
                NodeNFP.serialize(inst,instance, serializer);
            }
            if (!instance.listAttributeValues().isEmpty()) {
                for (Iterator i = instance.listAttributeValues().keySet().iterator(); i.hasNext();) {
                    Identifier attrId = (Identifier)i.next();
                    URI attrURI = serializer.getFactory().createURI(attrId.toString());
                    
                    Set datavalues = instance.listAttributeValues(attrId);
                    for (Iterator j = datavalues.iterator(); j.hasNext();) {
                        serializer.getWriter().writeStatement(
                                inst,
                                attrURI,
                                NodeValue.serialize(j.next(), serializer));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

/*
 * $Log: NodeInstance.java,v $
 * Revision 1.1  2006/11/10 15:02:59  ohamano
 * *** empty log message ***
 *
 * Revision 1.1  2006/11/10 10:08:42  ohamano
 * *** empty log message ***
 *
 *
*/