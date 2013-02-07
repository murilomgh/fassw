/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2004-2005, OntoText Lab. / SIRMA

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
package com.ontotext.wsmo4j.parser.xml;

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */

import org.w3c.dom.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.service.*;

import com.ontotext.wsmo4j.serializer.xml.*;

/**
 * Helper type to serialize/deserialize xml element interface
 * 
 * @author not attributable
 */
class NodeInterface {
    static Interface deserialize(Node xmlNode, WsmlXmlParser parser)
            throws InvalidModelException {
        if (xmlNode == null || parser == null || xmlNode.getNodeName() != "interface") {
            throw new IllegalArgumentException();
        }

        IRI iriInterface = parser.getFactory().createIRI(
                WsmlXmlHelper.getAttrValue(xmlNode, "name"));
        Interface intrface = parser.getFactory().createInterface(iriInterface);
        NodeTopEntity.processTopEntityNode(intrface, xmlNode, parser);

        NodeList nodes = xmlNode.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE
                    && node.getNodeName() == "nonFunctionalProperties") {
                NodeNFP.deserialize(intrface, node, parser);
            }
            else {
                // TODO: Handle choreography and orchestration
            }
        }
        
        return intrface;
    }
    
    static Element serialize(Interface intrface, WsmlXmlSerializer serializer) {
        Element interfaceElement = serializer.createElement("interface");
        NodeTopEntity.serializeTopEntity(interfaceElement, intrface, serializer);
        
        if (!intrface.listNFPValues().isEmpty()) {
            NodeNFP.serialize(interfaceElement, intrface, serializer);
        }
        
        //TODO: Handle choreography and orchestration
        
        return interfaceElement;
    }
}

/*
 * $Log: NodeInterface.java,v $
 * Revision 1.2  2006/03/29 11:20:51  vassil_momtchev
 * mediator support added; some code refactored; minor bugs fixed
 *
 * Revision 1.1  2005/11/28 14:03:48  vassil_momtchev
 * package refactored from com.ontotext.wsmo4j.xmlparser to com.ontotext.wsmo4j.parser.xml
 *
 * Revision 1.4  2005/09/16 14:02:45  alex_simov
 * Identifier.asString() removed, use Object.toString() instead
 * (Implementations MUST override toString())
 *
 * Revision 1.3  2005/08/08 08:24:40  vassil_momtchev
 * javadoc added, bugfixes
 *
*/