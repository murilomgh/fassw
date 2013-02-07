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

import org.omwg.ontology.*;
import org.openrdf.model.*;
import org.openrdf.model.Value;

/**
 * Helper type to serialize/deserialize xml element relation instance
 * 
 * @author not attributable
 */
class NodeRelationInstance {
    
    static void serialize(RelationInstance relInstance, WsmlRdfSerializer serializer) {
        if (relInstance.getRelation() != null) {
            URI pred = serializer.getFactory().createURI(
                    relInstance.getRelation().getIdentifier().toString());
            
            if (!relInstance.listParameterValues().isEmpty() && relInstance.listParameterValues().size()==2) {
                Value rv = NodeValue.serialize(
                        relInstance.listParameterValues().get(0), serializer);
                if(rv instanceof URI) {
                    Resource r = (URI)rv;
                    Value v = NodeValue.serialize(
                            relInstance.listParameterValues().get(1), serializer);
                    try {
                        serializer.getWriter().writeStatement(r,pred,v);
                        
                        if (!relInstance.listNFPValues().isEmpty()) {
                            NodeNFP.serialize(r, relInstance, serializer);
                        }
                        
                    } catch(IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    return;
                }
            }
        } else {
            return;
        }
    }
}

/*
 * $Log: NodeRelationInstance.java,v $
 * Revision 1.1  2006/11/10 15:02:59  ohamano
 * *** empty log message ***
 *
 * Revision 1.1  2006/11/10 10:08:42  ohamano
 * *** empty log message ***
 *
 *
*/