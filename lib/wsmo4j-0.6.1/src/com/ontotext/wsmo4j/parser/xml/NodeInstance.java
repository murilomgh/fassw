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
import org.wsmo.common.Entity;
import org.wsmo.common.exception.*;

import com.ontotext.wsmo4j.serializer.xml.*;

/**
 * Helper type to serialize/deserialize xml element instance
 * 
 * @author not attributable
 */
class NodeInstance {
    static Instance deserialize(Node xmlNode, WsmlXmlParser parser) throws InvalidModelException {
        if (xmlNode == null || parser == null || xmlNode.getNodeName() != "instance"
                || xmlNode.getNodeType() != Node.ELEMENT_NODE) {
            throw new IllegalArgumentException();
        }

        IRI instanceIri = parser.getFactory()
                .createIRI(WsmlXmlHelper.getAttrValue(xmlNode, "name"));
        Instance instance = parser.getFactory().createInstance(instanceIri);

        NodeList nodes = xmlNode.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeName() == "memberOf" && node.getNodeType() == Node.ELEMENT_NODE) {
                IRI conceptIri = parser.getFactory().createIRI(WsmlXmlHelper.getElementText(node));
                instance.addConcept(parser.getFactory().getConcept(conceptIri));
            }
            else if (node.getNodeName() == "nonFunctionalProperties"
                    && node.getNodeType() == Node.ELEMENT_NODE) {
                NodeNFP.deserialize(instance, node, parser);
            }
            else if (node.getNodeName() == "attributeValue"
                    && node.getNodeType() == Node.ELEMENT_NODE) {
                IRI attrIri = parser.getFactory().createIRI(
                        WsmlXmlHelper.getAttrValue(node, "name"));
                Value value = NodeValue.deserialize(WsmlXmlHelper.getFirstChildElement(node),
                        parser);
                instance.addAttributeValue(attrIri, value);
            }
        }

        return instance;
    }

    static Element serialize(Instance instance, WsmlXmlSerializer serializer) {
        Element instanceElement = serializer.createElement("instance");
        instanceElement.setAttribute("name", instance.getIdentifier().toString());

        if (!instance.listConcepts().isEmpty()) {
            Object[] entities = instance.listConcepts().toArray();
            for (int i = 0; i < entities.length; i++) {
                Element memberOf = serializer.createElement("memberOf");
                instanceElement.appendChild(memberOf);
                memberOf.appendChild(serializer.createTextNode(((Entity) entities[i])
                        .getIdentifier().toString()));
            }
        }
        if (!instance.listNFPValues().isEmpty()) {
            NodeNFP.serialize(instanceElement, instance, serializer);
        }
        if (!instance.listAttributeValues().isEmpty()) {
            for (Iterator i = instance.listAttributeValues().keySet().iterator(); i.hasNext();) {
                Identifier attrId = (Identifier) i.next();
                Element attributeValElement = serializer.createElement("attributeValue");
                attributeValElement.setAttribute("name", attrId.toString());
                instanceElement.appendChild(attributeValElement);

                Set datavalues = instance.listAttributeValues(attrId);
                for (Iterator j = datavalues.iterator(); j.hasNext();) {
                    attributeValElement.appendChild(NodeValue.serialize(j.next(), serializer));
                }
            }
        }

        return instanceElement;
    }
}

/*
 * $Log: NodeInstance.java,v $
 * Revision 1.2  2006/02/10 15:06:23  vassil_momtchev
 * addapted to the latest api changes
 *
 * Revision 1.1  2005/11/28 14:03:48  vassil_momtchev
 * package refactored from com.ontotext.wsmo4j.xmlparser to com.ontotext.wsmo4j.parser.xml
 *
 * Revision 1.3  2005/09/16 14:02:45  alex_simov
 * Identifier.asString() removed, use Object.toString() instead
 * (Implementations MUST override toString())
 *
 * Revision 1.2  2005/08/08 08:24:40  vassil_momtchev
 * javadoc added, bugfixes
 *
*/