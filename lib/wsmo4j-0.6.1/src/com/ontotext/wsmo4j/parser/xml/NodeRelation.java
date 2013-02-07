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

import org.deri.wsmo4j.logicalexpression.*;
import org.omwg.ontology.*;
import org.w3c.dom.*;
import org.wsmo.common.*;
import org.wsmo.common.Entity;
import org.wsmo.common.exception.*;

import com.ontotext.wsmo4j.serializer.xml.*;

/**
 * Helper type to serialize/deserialize xml element relation
 * 
 * @author not attributable
 */
class NodeRelation {
    static Relation deserialize(Node xmlNode, WsmlXmlParser parser) throws InvalidModelException {
        if (xmlNode == null || parser == null || xmlNode.getNodeName() != "relation") {
            throw new IllegalArgumentException();
        }

        IRI relationIri = parser.getFactory()
                .createIRI(WsmlXmlHelper.getAttrValue(xmlNode, "name"));
        Relation relation = parser.getFactory().createRelation(relationIri);

        byte arity = Byte.parseByte(WsmlXmlHelper.getAttrValue(xmlNode, "arity"));
        byte currentArity = 0;

        NodeList nodes = xmlNode.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeName() == "superRelation" && node.getNodeType() == Node.ELEMENT_NODE) {
                IRI superRelIri = parser.getFactory().createIRI(WsmlXmlHelper.getElementText(node));
                Relation superRel = parser.getFactory().getRelation(superRelIri);
                relation.addSuperRelation(superRel);
                superRel.addSubRelation(relation);

            }
            else if (node.getNodeName() == "parameters" && node.getNodeType() == Node.ELEMENT_NODE) {
                NodeList parametersNodes = node.getChildNodes();
                for (int j = 0; j < parametersNodes.getLength(); j++) {
                    Node paramNode = parametersNodes.item(j);
                    if (paramNode.getNodeType() == Node.ELEMENT_NODE
                            && paramNode.getNodeName() == "parameter") {
                        String paramType = WsmlXmlHelper.getAttrValue(paramNode, "type");
                        Parameter param = relation.createParameter(currentArity++);
                        if (paramType.equals("inferring")) {
                            param.setConstraining(false);
                        }
                        else {
                            param.setConstraining(true);
                        }
                        NodeList rangeNodes = paramNode.getChildNodes();
                        for (int k = 0; k < rangeNodes.getLength(); k++) {
                            Node childNode = rangeNodes.item(k);
                            if (childNode.getNodeName() == "range"
                                    && childNode.getNodeType() == Node.ELEMENT_NODE) {
                                ConstantTransformer cf = ConstantTransformer.getInstance();
                                String type = WsmlXmlHelper.getElementText(childNode);
                                if (cf.isDataType(type)) {
                                    param.addType(parser.getDataFactory().createWsmlDataType(type));
                                }
                                else {
                                    param.addType(parser.getFactory().createConcept(
                                            parser.getFactory().createIRI(type)));
                                }
                            }
                        }
                    }
                }
                if (currentArity != arity) {
                    throw new IllegalArgumentException("Relation exepected "
                            + String.valueOf(arity) + "but " + String.valueOf(currentArity)
                            + " parameters were found!");
                }
            }
            else if (node.getNodeName() == "nonFunctionalProperties"
                    && node.getNodeType() == Node.ELEMENT_NODE) {
                NodeNFP.deserialize(relation, node, parser);
            }
        }

        return relation;
    }

    static Element serialize(Relation relation, WsmlXmlSerializer serializer) {
        Element relationElement = serializer.createElement("relation");
        relationElement.setAttribute("name", relation.getIdentifier().toString());
        int arity = 0;

        if (!relation.listSuperRelations().isEmpty()) {
            Object[] entities = relation.listSuperRelations().toArray();
            for (int i = 0; i < entities.length; i++) {
                Element superRel = serializer.createElement("superRelation");
                relationElement.appendChild(superRel);
                superRel.appendChild(serializer.createTextNode(((Entity) entities[i])
                        .getIdentifier().toString()));
            }
        }

        if (!relation.listNFPValues().isEmpty()) {
            NodeNFP.serialize(relationElement, relation, serializer);
        }

        if (!relation.listParameters().isEmpty()) {
            Element parametersElement = serializer.createElement("parameters");
            relationElement.appendChild(parametersElement);

            for (Iterator i = relation.listParameters().iterator(); i.hasNext();) {
                arity++;
                Parameter param = (Parameter) i.next();
                Element paramElement = serializer.createElement("parameter");
                paramElement.setAttribute("type", param.isConstraining() == true ? "constraining"
                        : "inferring");
                parametersElement.appendChild(paramElement);

                for (Iterator j = param.listTypes().iterator(); j.hasNext();) {
                    Type type = (Type) j.next();
                    Element rangeElement = serializer.createElement("range");
                    paramElement.appendChild(rangeElement);
                    if (type instanceof Concept) {
                        rangeElement.appendChild(serializer.createTextNode(
                                ((Concept) type).getIdentifier().toString()));
                    }
                    else {
                        rangeElement.appendChild(serializer.createTextNode(
                                ((WsmlDataType) type).getIRI().toString()));
                    }
                }
            }
        }

        relationElement.setAttribute("arity", String.valueOf(arity));
        return relationElement;
    }
}

/*
 * $Log: NodeRelation.java,v $
 * Revision 1.3  2006/07/04 14:30:30  vassil_momtchev
 * datatype parsing problem fixed
 *
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
 * Revision 1.3  2005/08/31 09:19:14  vassil_momtchev
 * use Type and Value instead of Object where appropriate bug SF 1276677
 *
 * Revision 1.2  2005/08/08 08:24:40  vassil_momtchev
 * javadoc added, bugfixes
 *
*/