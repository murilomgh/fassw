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

import org.deri.wsmo4j.io.serializer.xml.*;
import org.omwg.logicalexpression.*;
import org.omwg.ontology.*;
import org.w3c.dom.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.wsml.*;

import com.ontotext.wsmo4j.serializer.xml.*;

/**
 * Helper type to serialize/deserialize xml elements with logical expressions
 * 
 * @author not attributable
 */
class NodeLogExp {
    static Axiom deserialize(Node xmlNode, WsmlXmlParser parser) throws InvalidModelException,
            ParserException {
        if (xmlNode == null || parser == null || xmlNode.getNodeType() != Node.ELEMENT_NODE) {
            throw new IllegalArgumentException();
        }

        IRI axiomIri = parser.getFactory().createIRI(WsmlXmlHelper.getAttrValue(xmlNode, "name"));
        Axiom axiom = parser.getFactory().createAxiom(axiomIri);
        NodeList nodes = xmlNode.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeName() == "nonFunctionalProperties"
                    && node.getNodeType() == Node.ELEMENT_NODE) {
                NodeNFP.deserialize(axiom, node, parser);
            }
            else if (node.getNodeName() == "definedBy" && node.getNodeType() == Node.ELEMENT_NODE) {
                //TODO better integration?
                NodeList xmlles = node.getChildNodes();
                for (int n=0; n<xmlles.getLength();n++){
                    Node logexpnode = xmlles.item(n);
                    if (logexpnode.getNodeType() == Node.ELEMENT_NODE){
                        LogicalExpression le = parser.getLEParser().parse(xmlles.item(n));
                        axiom.addDefinition(le);
                    }
                }
                //axiom.addDefinition(parser.getLEFactory().createLogicalExpression(node.toString()));
            }
        }

        return axiom;
    }

    static Element serialize(String elementName, Axiom axiom, WsmlXmlSerializer serializer) {
        
        Element logExpElement = serializer.createElement(elementName);
        logExpElement.setAttribute("name", axiom.getIdentifier().toString());

        if (!axiom.listNFPValues().isEmpty()) {
            NodeNFP.serialize(logExpElement, axiom, serializer);
        }

        if (!axiom.listDefinitions().isEmpty()) {
            Node definedByNode = serializer.createElement("definedBy");
            logExpElement.appendChild(definedByNode);
                
            for (Iterator i = axiom.listDefinitions().iterator(); i.hasNext();) {
                LogicalExpression logExp = (LogicalExpression) i.next();
                LogExprSerializerXML leSerXML = new LogExprSerializerXML(
                        logExpElement.getOwnerDocument());
                Node leNode = leSerXML.serialize(logExp);
                definedByNode.appendChild(leNode);
                break; // schema allows only one definition
            }
        }

        return logExpElement;
    }
}

/*
 * $Log: NodeLogExp.java,v $
 * Revision 1.2  2006/01/13 10:01:17  vassil_momtchev
 * defineBy element was missing in axiom serialization
 *
 * Revision 1.1  2005/11/28 14:03:48  vassil_momtchev
 * package refactored from com.ontotext.wsmo4j.xmlparser to com.ontotext.wsmo4j.parser.xml
 *
 * Revision 1.9  2005/09/21 06:28:56  holgerlausen
 * removing explicit factory creations and introducing parameter maps for parsers
 *
 * Revision 1.8  2005/09/20 19:41:02  holgerlausen
 * removed superflouis interfaces for IO in logical expression (since intgeration not necessary)
 *
 * Revision 1.7  2005/09/16 14:02:45  alex_simov
 * Identifier.asString() removed, use Object.toString() instead
 * (Implementations MUST override toString())
 *
 * Revision 1.6  2005/09/08 20:20:06  holgerlausen
 * fixed XML serialization LE integration
 *
 * Revision 1.5  2005/09/06 18:35:14  holgerlausen
 * adopted parser serializer to use datatypefactory
 *
 * Revision 1.4  2005/09/02 13:32:44  ohamano
 * move logicalexpression packages from ext to core
 * move tests from logicalexpression.test to test module
 *
 * Revision 1.3  2005/08/31 09:24:22  vassil_momtchev
 * add InvalidModelException and ParserException to LogicalExpressionFactory::createLogixExpression methods
 *
 * Revision 1.2  2005/08/08 08:24:40  vassil_momtchev
 * javadoc added, bugfixes
 *
*/