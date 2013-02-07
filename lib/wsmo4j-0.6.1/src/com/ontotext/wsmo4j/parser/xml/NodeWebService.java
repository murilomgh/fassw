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

import java.util.*;

import org.w3c.dom.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.service.*;
import org.wsmo.wsml.*;

import com.ontotext.wsmo4j.serializer.xml.*;

/**
 * Helper type to serialize/deserialize xml element web service
 * 
 * @author not attributable
 */
class NodeWebService {
    static WebService deserialize(Node xmlNode, WsmlXmlParser parser)
            throws InvalidModelException, ParserException {
        if (xmlNode == null || parser == null || xmlNode.getNodeName() != "webService"
                || xmlNode.getNodeType() != Element.ELEMENT_NODE) {
            throw new IllegalArgumentException();
        }

        WebService webService = null;
        IRI webServiceIri = parser.getFactory().createIRI(
                WsmlXmlHelper.getAttrValue(xmlNode, "name"));
        webService = parser.getFactory().createWebService(webServiceIri);
        NodeTopEntity.processTopEntityNode(webService, xmlNode, parser);

        NodeList nodes = xmlNode.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeName() == "nonFunctionalProperties") {
                NodeNFP.deserialize(webService, node, parser);
            }
            else if (node.getNodeName() == "capability") {
                webService.setCapability(NodeCapability.deserialize(node, parser));
            }
            else if (node.getNodeName() == "interface") {
                webService.addInterface(NodeInterface.deserialize(node, parser));
            }
        }

        return webService;
    }

    static Element serialize(WebService webService, WsmlXmlSerializer serializer) {
        Element webServiceElement = serializer.createElement("webService");
        webServiceElement.setAttribute("name", webService.getIdentifier().toString());
        NodeTopEntity.serializeTopEntity(webServiceElement, webService, serializer);
        
        if (!webService.listNFPValues().isEmpty()) {
            NodeNFP.serialize(webServiceElement, webService, serializer);
        }
        
        webServiceElement.appendChild(NodeCapability.serialize(webService.getCapability(), serializer));
        
        if (!webService.listInterfaces().isEmpty()) {
            for (Iterator i = webService.listInterfaces().iterator(); i.hasNext();) {
                Interface intrface = (Interface) i.next();
                webServiceElement.appendChild(NodeInterface.serialize(intrface, serializer));
            } 
        }
        
        return webServiceElement;
    }
}

/*
 * $Log: NodeWebService.java,v $
 * Revision 1.2  2006/03/29 11:20:51  vassil_momtchev
 * mediator support added; some code refactored; minor bugs fixed
 *
 * Revision 1.1  2005/11/28 14:03:48  vassil_momtchev
 * package refactored from com.ontotext.wsmo4j.xmlparser to com.ontotext.wsmo4j.parser.xml
 *
 * Revision 1.5  2005/09/16 14:02:45  alex_simov
 * Identifier.asString() removed, use Object.toString() instead
 * (Implementations MUST override toString())
 *
 * Revision 1.4  2005/08/31 09:24:22  vassil_momtchev
 * add InvalidModelException and ParserException to LogicalExpressionFactory::createLogixExpression methods
 *
 * Revision 1.3  2005/08/08 08:24:40  vassil_momtchev
 * javadoc added, bugfixes
 *
*/