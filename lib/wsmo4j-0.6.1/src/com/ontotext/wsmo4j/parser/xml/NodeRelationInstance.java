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

import org.omwg.ontology.*;
import org.w3c.dom.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;

import com.ontotext.wsmo4j.serializer.xml.*;

/**
 * Helper type to serialize/deserialize xml element relation instance
 * 
 * @author not attributable
 */
class NodeRelationInstance {
    static RelationInstance deserialize(Node xmlNode, WsmlXmlParser parser)
            throws InvalidModelException {
        if (xmlNode == null || parser == null || xmlNode.getNodeName() != "relationInstance"
                || xmlNode.getNodeType() != Node.ELEMENT_NODE) {
            throw new IllegalArgumentException();
        }

        Identifier relInstanceId = null;
        RelationInstance relInstance = null;
        if (xmlNode.getAttributes().getNamedItem("name") == null)
            relInstanceId = parser.getFactory().createAnonymousID();
        else {
            relInstanceId = parser.getFactory().createIRI(
                WsmlXmlHelper.getAttrValue(xmlNode, "name"));
        }

        NodeList nodes = xmlNode.getChildNodes();
        byte paramArity = 0;
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeName() == "memberOf" && node.getNodeType() == Node.ELEMENT_NODE) {
                Identifier relationId = null;
                if (WsmlXmlHelper.getElementText(node).equals("_#"))
                    relationId = parser.getFactory().createAnonymousID();
                else    
                    relationId = parser.getFactory().createIRI(WsmlXmlHelper.getElementText(node));
                relInstance = parser.getFactory().createRelationInstance(relInstanceId,
                        parser.getFactory().getRelation(relationId));
            }
            else if (node.getNodeName() == "nonFunctionalProperties"
                    && node.getNodeType() == Node.ELEMENT_NODE) {
                NodeNFP.deserialize(relInstance, node, parser);
            }
            else if (node.getNodeName() == "parameterValue"
                    && node.getNodeType() == Node.ELEMENT_NODE) {                
                NodeList childNodes = node.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node childNode = childNodes.item(j);
                    if (childNode.getNodeName() == "value") {
                        relInstance.setParameterValue(paramArity, 
                                NodeValue.deserialize(childNode, parser));
                    }
                }
            }
        }

        return relInstance;
    }

    static Element serialize(RelationInstance relInstance, WsmlXmlSerializer serializer) {
        Element relInstanceElement = serializer.createElement("relationInstance");

        if (!relInstance.listNFPValues().isEmpty()) {
            NodeNFP.serialize(relInstanceElement, relInstance, serializer);
        }

        if (relInstance.getRelation() != null) {
            Element memberOfElement = serializer.createElement("memberOf");
            memberOfElement.appendChild(serializer.createTextNode(
                    relInstance.getIdentifier().toString()));
            relInstanceElement.appendChild(memberOfElement);
        }

        if (!relInstance.listParameterValues().isEmpty()) {
            Element parameterElement = serializer.createElement("parameterValue");
            for (Iterator i = relInstance.listParameterValues().iterator(); i.hasNext();) {
                parameterElement.appendChild(NodeValue.serialize(i.next(), serializer));
            }
        }

        return relInstanceElement;
    }
}

/*
 * $Log: NodeRelationInstance.java,v $
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
 * Revision 1.4  2005/09/13 09:01:22  vassil_momtchev
 * use WsmoFactory.createRelationInstance(Identifier, Relation) instead of WsmoFactory.createRelationInstance(Identifier)  RelationInstance.setRelation(Relation)
 *
 * Revision 1.3  2005/08/08 08:24:40  vassil_momtchev
 * javadoc added, bugfixes
 *
*/