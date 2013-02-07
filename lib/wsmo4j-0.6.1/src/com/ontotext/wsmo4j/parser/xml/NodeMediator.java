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
import org.wsmo.mediator.*;

import com.ontotext.wsmo4j.serializer.xml.*;

/**
 * Helper type to serialize/deserialize xml element mediator
 * 
 * @author not attributable
 */
class NodeMediator {
    static TopEntity processMediatorNode(Node xmlNode, WsmlXmlParser parser)
            throws InvalidModelException {
        if (xmlNode == null || parser == null || !xmlNode.getNodeName().endsWith("Mediator")
                || xmlNode.getNodeType() != Element.ELEMENT_NODE) {
            throw new IllegalArgumentException();
        }

        Mediator mediator = null;
        IRI mediatorIri = parser.getFactory()
                .createIRI(WsmlXmlHelper.getAttrValue(xmlNode, "name"));
        if (xmlNode.getNodeName().startsWith("oo")) {
            mediator = parser.getFactory().createOOMediator(mediatorIri);
        }
        else if (xmlNode.getNodeName().startsWith("gg")) {
            mediator = parser.getFactory().createGGMediator(mediatorIri);
        }
        else if (xmlNode.getNodeName().startsWith("wg")) {
            mediator = parser.getFactory().createWGMediator(mediatorIri);
        }
        else if (xmlNode.getNodeName().startsWith("ww")) {
            mediator = parser.getFactory().createWWMediator(mediatorIri);
        }
        else {
            throw new IllegalArgumentException(); // should never happens if
                                                    // schema valid
        }

        NodeTopEntity.processTopEntityNode(mediator, xmlNode, parser);
        NodeList nodes = xmlNode.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeName() == "nonFunctionalProperties") {
                NodeNFP.deserialize(mediator, node, parser);
            }
            else if (node.getNodeName() == "source") {
                mediator.addSource(parser.getFactory().createIRI(WsmlXmlHelper.getElementText(node)));
            }
            else if (node.getNodeName() == "target") {
                mediator.setTarget(parser.getFactory().createIRI(WsmlXmlHelper.getElementText(node)));
            }
            else if (node.getNodeName() == "usesService") {
                mediator.setMediationService(parser.getFactory().createIRI(WsmlXmlHelper.getElementText(node)));
            }
        }

        return mediator;
    }

    static void serializeMediator(Element parent, Mediator mediator, WsmlXmlSerializer serializer) {
        Element mediatorElement =  null;
        if (mediator instanceof OOMediator)
            mediatorElement = serializer.createElement("ooMediator");
        else if (mediator instanceof GGMediator)
            mediatorElement = serializer.createElement("ggMediator");
        else if (mediator instanceof WGMediator)
            mediatorElement = serializer.createElement("wgMediator");
        else if (mediator instanceof WWMediator)
            mediatorElement = serializer.createElement("wwMediator");
        else
            throw new IllegalArgumentException("Invalid mediator type!");
        mediatorElement.setAttribute("name", mediator.getIdentifier().toString());
        parent.appendChild(mediatorElement);
        NodeTopEntity.serializeTopEntity(mediatorElement, mediator, serializer);
        
        if (!mediator.listNFPValues().isEmpty()) {
            NodeNFP.serialize(mediatorElement, mediator, serializer);
        }
        
        if (mediator.getTarget() != null) {
            Element target = serializer.createElement("target");
            mediatorElement.appendChild(target);
            target.appendChild(serializer.createTextNode(mediator.getIdentifier().toString()));
        }
        
        if (mediator.listSources().size() > 0) {
            for (Iterator i = mediator.listSources().iterator(); i.hasNext();) {
                Element source = serializer.createElement("source");
                mediatorElement.appendChild(source);
                source.appendChild(serializer.createTextNode(((IRI) i.next()).toString()));
            }
        }
    }
}

/*
 * $Log: NodeMediator.java,v $
 * Revision 1.3  2007/04/04 08:37:59  alex_simov
 * The serializer is enabled for all types of topentities
 *
 * Revision 1.2  2006/03/29 11:20:51  vassil_momtchev
 * mediator support added; some code refactored; minor bugs fixed
 *
 * Revision 1.1  2005/11/28 14:03:48  vassil_momtchev
 * package refactored from com.ontotext.wsmo4j.xmlparser to com.ontotext.wsmo4j.parser.xml
 *
 * Revision 1.2  2005/08/08 08:24:40  vassil_momtchev
 * javadoc added, bugfixes
 *
*/