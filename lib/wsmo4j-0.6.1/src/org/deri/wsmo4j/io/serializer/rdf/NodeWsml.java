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

import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.mediator.*;
import org.wsmo.service.*;

/**
 * Helper type to serialize/deserialize xml element wsml
 * 
 * @author not attributable
 */
public class NodeWsml {

    public static void serializeTopEntity(TopEntity[] entities, WsmlRdfSerializer serializer) {
        for (int i = 0; i < entities.length; i++) { 
            if (entities[i] instanceof Ontology) {
                NodeOntology.serialize((Ontology) entities[i], serializer);
            }
            else if (entities[i] instanceof Mediator) {
            }
            else if (entities[i] instanceof Goal) {
            }
            else if (entities[i] instanceof WebService) {
            }
        }
    }
}

/*
 * $Log: NodeWsml.java,v $
 * Revision 1.1  2006/11/10 15:02:59  ohamano
 * *** empty log message ***
 *
 * Revision 1.1  2006/11/10 10:08:42  ohamano
 * *** empty log message ***
 *
 *
*/