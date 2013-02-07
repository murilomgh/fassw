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
import org.wsmo.service.*;
import org.wsmo.wsml.*;

import com.ontotext.wsmo4j.serializer.xml.*;

/**
 * Helper type to serialize/deserialize xml element capability
 * 
 * @author not attributable
 */
class NodeCapability {
    static Capability deserialize(Node xmlNode, WsmlXmlParser parser)
            throws InvalidModelException, ParserException  {
        if (xmlNode == null || parser == null || xmlNode.getNodeName() != "capability") {
            throw new IllegalArgumentException();
        }

        IRI iriCapability = parser.getFactory().createIRI(
                WsmlXmlHelper.getAttrValue(xmlNode, "name"));
        Capability capability = parser.getFactory().createCapability(iriCapability);
        NodeTopEntity.processTopEntityNode(capability, xmlNode, parser);

        NodeList nodes = xmlNode.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (node.getNodeName() == "nonFunctionalProperties"
                    && node.getNodeType() == Node.ELEMENT_NODE) {
                NodeNFP.deserialize(capability, node, parser);
            }
            else if (node.getNodeName() == "sharedVariable" && node.getNodeType() == Node.ELEMENT_NODE) {
                NodeList childNodes = node.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node childNode = childNodes.item(j);
                    if (childNode.getNodeName() == "variable") {
                        capability.addSharedVariable(
                                parser.getLEFactory().createVariable(
                                        WsmlXmlHelper.getElementText(node)));
                    }
                }
            }
            else if (node.getNodeName() == "precondition") {
                capability.addPreCondition(NodeLogExp.deserialize(node, parser));
            }
            else if (node.getNodeName() == "assumption") {
                capability.addAssumption(NodeLogExp.deserialize(node, parser));
            }
            else if (node.getNodeName() == "postcondition") {
                capability.addPostCondition(NodeLogExp.deserialize(node, parser));
            }
            else if (node.getNodeName() == "effect") {
                capability.addEffect(NodeLogExp.deserialize(node, parser));
            }
        }
        
        return capability;
    }
    
    static Element serialize(Capability capability, WsmlXmlSerializer serializer) {
        Element capabilityElement = serializer.createElement("capability");
        NodeTopEntity.serializeTopEntity(capabilityElement, capability, serializer);
        
        if (!capability.listNFPValues().isEmpty()) {
            NodeNFP.serialize(capabilityElement, capability, serializer);
        }
        
        if (!capability.listSharedVariables().isEmpty()) {
            Element sharedVarElement = serializer.createElement("sharedVariable");
            capabilityElement.appendChild(sharedVarElement);
            for (Iterator i = capability.listSharedVariables().iterator(); i.hasNext();) {
                Variable var = (Variable) i.next();
                Element variableElement = serializer.createElement("variable");
                variableElement.appendChild(serializer.createTextNode("?" + var.getName()));
            }
        }
        
        if (!capability.listPreConditions().isEmpty()) {
            for (Iterator i = capability.listPreConditions().iterator(); i.hasNext();) {
                Axiom axiom = (Axiom) i.next();
                capabilityElement.appendChild(serializer.createElement("precondition").appendChild(
                        NodeLogExp.serialize("precondition", axiom, serializer)));
            }
        }
        
        if (!capability.listAssumptions().isEmpty()) {
            for (Iterator i = capability.listPreConditions().iterator(); i.hasNext();) {
                Axiom axiom = (Axiom) i.next();
                capabilityElement.appendChild(serializer.createElement("assumption").appendChild(
                        NodeLogExp.serialize("assumption", axiom, serializer)));
            }
        }
        
        if (!capability.listPostConditions().isEmpty()) {
            for (Iterator i = capability.listPostConditions().iterator(); i.hasNext();) {
                Axiom axiom = (Axiom) i.next();
                capabilityElement.appendChild(serializer.createElement("postcondition").appendChild(
                        NodeLogExp.serialize("postcondition", axiom, serializer)));
            }
        }
        
        if (!capability.listEffects().isEmpty()) {
            for (Iterator i = capability.listEffects().iterator(); i.hasNext();) {
                Axiom axiom = (Axiom) i.next();
                capabilityElement.appendChild(serializer.createElement("effect").appendChild(
                        NodeLogExp.serialize("effect", axiom, serializer)));
            }
        }
        
        return capabilityElement;
    }
}

/*
 * $Log: NodeCapability.java,v $
 * Revision 1.3  2006/06/21 07:46:29  vassil_momtchev
 * createVariable(String) method moved from WsmoFactory to LogicalExpressionFactory interface
 *
 * Revision 1.2  2006/03/29 11:20:51  vassil_momtchev
 * mediator support added; some code refactored; minor bugs fixed
 *
 * Revision 1.1  2005/11/28 14:03:48  vassil_momtchev
 * package refactored from com.ontotext.wsmo4j.xmlparser to com.ontotext.wsmo4j.parser.xml
 *
 * Revision 1.8  2005/09/16 14:02:45  alex_simov
 * Identifier.asString() removed, use Object.toString() instead
 * (Implementations MUST override toString())
 *
 * Revision 1.7  2005/08/31 09:24:22  vassil_momtchev
 * add InvalidModelException and ParserException to LogicalExpressionFactory::createLogixExpression methods
 *
 * Revision 1.6  2005/08/31 09:04:42  alex_simov
 * fix: Variable objects must be created only by the WsmoFactory
 *
 * Revision 1.5  2005/08/31 08:36:14  alex_simov
 * bugfix: Variable.getName() now returns the name without leading '?'
 *
 * Revision 1.4  2005/08/08 08:24:40  vassil_momtchev
 * javadoc added, bugfixes
 *
*/