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

import org.omwg.ontology.*;
import org.w3c.dom.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.mediator.*;
import org.wsmo.service.*;
import org.wsmo.wsml.*;

import com.ontotext.wsmo4j.serializer.xml.*;

/**
 * Helper type to serialize/deserialize xml element wsml
 * 
 * @author not attributable
 */
public class NodeWsml {

    static TopEntity deserialzieTopEntity(Node xmlNode, WsmlXmlParser parser)
            throws InvalidModelException, ParserException {
        if (xmlNode == null || xmlNode.getNodeName() != "wsml") {
            throw new IllegalArgumentException();
        }

        // TODO: Process wsmlVariant information

        NodeList nodes = xmlNode.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeName() == "ontology" && node.getNodeType() == Node.ELEMENT_NODE) {
                return NodeOntology.deserialize(node, parser);
            }
            else if (node.getNodeName().endsWith("Mediator")
                    && node.getNodeType() == Node.ELEMENT_NODE) {
                return NodeMediator.processMediatorNode(node, parser);
            }
            else if (node.getNodeName() == "goal" && node.getNodeType() == Node.ELEMENT_NODE) {
                return NodeGoal.deserialize(node, parser);
            }
            else if (node.getNodeName() == "webService" && node.getNodeType() == Node.ELEMENT_NODE) {
                return NodeWebService.deserialize(node, parser);
            }
        }

        return null;
    }

    public static Element serializeTopEntity(TopEntity[] entities, WsmlXmlSerializer serializer) {
        Element wsmlElement = serializer.createElement("wsml");
        for (int i = 0; i < entities.length; i++) {
            if (entities[i] instanceof Ontology) {
                wsmlElement.appendChild(NodeOntology.serialize((Ontology) entities[i], serializer));
            }
            else if (entities[i] instanceof Mediator) {
                NodeMediator.serializeMediator(wsmlElement, (Mediator) entities[i], serializer);
            }
            else if (entities[i] instanceof Goal) {
                wsmlElement.appendChild(NodeGoal.serialize((Goal) entities[i], serializer));
            }
            else if (entities[i] instanceof WebService) {
                wsmlElement.appendChild(NodeWebService.serialize((WebService) entities[i], serializer));
            }
        }

        return wsmlElement;
    }
}

/*
 * $Log: NodeWsml.java,v $
 * Revision 1.2  2007/04/04 08:37:59  alex_simov
 * The serializer is enabled for all types of topentities
 *
 * Revision 1.1  2005/11/28 14:03:48  vassil_momtchev
 * package refactored from com.ontotext.wsmo4j.xmlparser to com.ontotext.wsmo4j.parser.xml
 *
 * Revision 1.4  2005/08/31 09:24:22  vassil_momtchev
 * add InvalidModelException and ParserException to LogicalExpressionFactory::createLogixExpression methods
 *
 * Revision 1.3  2005/08/08 08:24:40  vassil_momtchev
 * javadoc added, bugfixes
 *
*/