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
import org.wsmo.common.*;

/**
 * Helper type to serialize/deserialize xml element non functional properties
 * 
 * @author not attributable
 */
class NodeNFP {
 
    static void serialize(Resource subject, Entity entity, WsmlRdfSerializer serializer) {
        if (entity.listNFPValues().isEmpty()) {
            return;
        }
        Map map = entity.listNFPValues();
        for (Iterator i = map.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            URI comment = serializer.getFactory().createURI(entry.getKey().toString());
            Set dataValues = (Set) entry.getValue();
            for (Iterator j = dataValues.iterator(); j.hasNext();) {
                try {
                    if(comment.getURI().equals("http://purl.org/dc/elements/1.1#relation")) {
                        serializer.getWriter().writeComment(comment + ":" + 
                                NodeValue.serialize(j.next(), serializer));
                    } else {
                        serializer.getWriter().writeStatement(
                            subject,
                            comment,
                            NodeValue.serialize(j.next(), serializer));
                    }
                } catch(IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    
    static void serialize(Ontology onto, WsmlRdfSerializer serializer) {
        if (onto.listNFPValues().isEmpty()) {
            return;
        }

        Map map = onto.listNFPValues();
        for (Iterator i = map.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            String comment = entry.getKey().toString() + ":"; 
            Set dataValues = (Set) entry.getValue();
            for (Iterator j = dataValues.iterator(); j.hasNext();) {
                comment = comment + " " + NodeValue.serialize(j.next(), serializer);
            }
            try {
                serializer.getWriter().writeComment(comment);
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

/*
 * $Log: NodeNFP.java,v $
 * Revision 1.2  2006/11/16 14:35:09  ohamano
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