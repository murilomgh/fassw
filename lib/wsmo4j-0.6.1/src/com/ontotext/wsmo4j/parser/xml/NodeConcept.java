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
 * Helper type to serialize/deserialize xml element concept
 * 
 * @author not attributable
 */
class NodeConcept {
    static Concept deserialize(Node xmlNode, WsmlXmlParser parser) throws InvalidModelException {
        if (xmlNode == null || parser == null || xmlNode.getNodeName() != "concept") {
            throw new IllegalArgumentException();
        }

        IRI iriConcept = parser.getFactory().createIRI(WsmlXmlHelper.getAttrValue(xmlNode, "name"));
        Concept concept = parser.getFactory().createConcept(iriConcept);

        NodeList nodes = xmlNode.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (node.getNodeName() == "superConcept" && node.getNodeType() == Node.ELEMENT_NODE) {
                IRI scIri = parser.getFactory().createIRI(WsmlXmlHelper.getElementText(node));
                Concept superConcept = parser.getFactory().getConcept(scIri);
                concept.addSuperConcept(superConcept);
                superConcept.addSubConcept(concept);
            }
            else if (node.getNodeName() == "nonFunctionalProperties"
                    && node.getNodeType() == Node.ELEMENT_NODE) {
                NodeNFP.deserialize(concept, node, parser);
            }
            else if (node.getNodeName() == "attribute" && node.getNodeType() == Node.ELEMENT_NODE) {
                IRI attrIri = parser.getFactory().createIRI(
                        WsmlXmlHelper.getAttrValue(node, "name"));
                Attribute attr = concept.createAttribute(attrIri);
                String attrType = WsmlXmlHelper.getAttrValue(node, "type");

                if (attrType.equals("inferring")) {
                    attr.setConstraining(false);
                }
                else {
                    attr.setConstraining(true);
                }

                NodeList attrNodes = node.getChildNodes();
                IRI inverseAttrIRI = null;
                for (int j = 0; j < attrNodes.getLength(); j++) {
                    Node attrNode = attrNodes.item(j);
                    if (attrNode.getNodeName() == "range"
                            && attrNode.getNodeType() == Node.ELEMENT_NODE) {
                        ConstantTransformer cf = ConstantTransformer.getInstance();
                        String type = WsmlXmlHelper.getElementText(attrNode);
                        if (cf.isDataType(type)) {
                            attr.addType(parser.getDataFactory().createWsmlDataType(type));
                        }
                        else {
                            attr.addType(parser.getFactory().createConcept(
                                    parser.getFactory().createIRI(type)));
                        }
                    }
                    else if (attrNode.getNodeName() == "symmetric"
                            && attrNode.getNodeType() == Node.ELEMENT_NODE) {
                        attr.setSymmetric(true);
                    }
                    else if (attrNode.getNodeName() == "transitive"
                            && attrNode.getNodeType() == Node.ELEMENT_NODE) {
                        attr.setTransitive(true);
                    }
                    else if (attrNode.getNodeName() == "reflexive"
                            && attrNode.getNodeType() == Node.ELEMENT_NODE) {
                        attr.setReflexive(true);
                    }
                    else if (attrNode.getNodeName() == "inverseOf"
                            && attrNode.getNodeType() == Node.ELEMENT_NODE) {
                        inverseAttrIRI = parser.getFactory().createIRI(
                                WsmlXmlHelper.getAttrValue(attrNode, "type"));
                        attr.setInverseOf(inverseAttrIRI);
                    }
                    else if (attrNode.getNodeName() == "minCardinality"
                            && attrNode.getNodeType() == Node.ELEMENT_NODE) {
                        attr.setMinCardinality(Integer.parseInt(WsmlXmlHelper
                                .getElementText(attrNode)));
                    }
                    else if (attrNode.getNodeName() == "maxCardinality"
                            && attrNode.getNodeType() == Node.ELEMENT_NODE) {
                        attr.setMaxCardinality(Integer.parseInt(WsmlXmlHelper
                                .getElementText(attrNode)));
                    }
                    else if (attrNode.getNodeName() == "nonFunctionalProperties"
                            && attrNode.getNodeType() == Node.ELEMENT_NODE) {
                        NodeNFP.deserialize(attr, attrNode, parser);
                    }
                }
            }
        }

        return concept;
    }

    static Element serialize(Concept concept, WsmlXmlSerializer serializer) {
        Element conceptElement = serializer.createElement("concept");
        conceptElement.setAttribute("name", concept.getIdentifier().toString());

        if (!concept.listSuperConcepts().isEmpty()) {
            Object[] entities = concept.listSuperConcepts().toArray();
            for (int i = 0; i < entities.length; i++) {
                Element superConcept = serializer.createElement("superConcept");
                conceptElement.appendChild(superConcept);
                superConcept.appendChild(serializer.createTextNode(((Entity) entities[i])
                        .getIdentifier().toString()));
            }
        }
        if (!concept.listNFPValues().isEmpty()) {
            NodeNFP.serialize(conceptElement, concept, serializer);
        }
        if (!concept.listAttributes().isEmpty()) {
            Object[] attrList = concept.listAttributes().toArray();
            for (int i = 0; i < attrList.length; i++) {
                Element attrElement = serializer.createElement("attribute");
                conceptElement.appendChild(attrElement);

                Attribute attribute = (Attribute) attrList[i];
                attrElement.setAttribute("name", attribute.getIdentifier().toString());
                attrElement.setAttribute("type", attribute.isConstraining() ? "constraining"
                        : "inferring");

                if (!attribute.listTypes().isEmpty()) {
                    Element range = serializer.createElement("range");
                    attrElement.appendChild(range);
                    for (Iterator j = attribute.listTypes().iterator(); j.hasNext();) {
                        Object o  = j.next();
                        if (o instanceof Concept)
                            range.appendChild(serializer.createTextNode(((Concept) o)
                                    .getIdentifier().toString()));
                        else if (o instanceof WsmlDataType) {
                            range.appendChild(serializer.createTextNode(
                                    ((WsmlDataType) o).getIRI().toString()));                         
                        }
                        else {
                            throw new RuntimeException("Unknown range for attribute!");
                        }
                        break; // only 1 is allowed in the schema?!
                    }
                }

                if (attribute.isSymmetric()) {
                    attrElement.appendChild(serializer.createElement("symmetric"));
                }
                if (attribute.isTransitive()) {
                    attrElement.appendChild(serializer.createElement("transitive"));
                }
                if (attribute.isReflexive()) {
                    attrElement.appendChild(serializer.createElement("reflexive"));
                }
                if (attribute.getInverseOf() != null) {
                    Element inverseOf = serializer.createElement("inverseOf");
                    attrElement.appendChild(inverseOf);
                    inverseOf.setAttribute("type", attribute.getInverseOf().toString());
                }
                if (attribute.getMinCardinality() > 0) {
                    Element minCardinality = serializer.createElement("minCardinality");
                    attrElement.appendChild(minCardinality);
                    minCardinality.appendChild(serializer.createTextNode(String.valueOf(attribute
                            .getMinCardinality())));
                }
                if (attribute.getMaxCardinality() < Integer.MAX_VALUE) {
                    Element maxCardinality = serializer.createElement("maxCardinality");
                    attrElement.appendChild(maxCardinality);
                    maxCardinality.appendChild(serializer.createTextNode(String.valueOf(attribute
                            .getMaxCardinality())));
                }
                if (!attribute.listNFPValues().isEmpty()) {
                    NodeNFP.serialize(attrElement, attribute, serializer);
                }
            }
        }

        assert conceptElement != null;
        return conceptElement;
    }
}

/*
 * $Log: NodeConcept.java,v $
 * Revision 1.6  2006/07/04 14:30:30  vassil_momtchev
 * datatype parsing problem fixed
 *
 * Revision 1.5  2006/03/29 11:20:51  vassil_momtchev
 * mediator support added; some code refactored; minor bugs fixed
 *
 * Revision 1.4  2006/02/16 09:56:50  vassil_momtchev
 * setInverseOf(Attribute) changed to setInverseOf(Identifier)
 *
 * Revision 1.3  2006/02/10 15:06:23  vassil_momtchev
 * addapted to the latest api changes
 *
 * Revision 1.2  2006/01/17 11:51:53  vassil_momtchev
 * range can be WsmlDataType also
 *
 * Revision 1.1  2005/11/28 14:03:48  vassil_momtchev
 * package refactored from com.ontotext.wsmo4j.xmlparser to com.ontotext.wsmo4j.parser.xml
 *
 * Revision 1.4  2005/09/16 14:02:45  alex_simov
 * Identifier.asString() removed, use Object.toString() instead
 * (Implementations MUST override toString())
 *
 * Revision 1.3  2005/09/01 14:56:59  vassil_momtchev
 * createAttribute method moved from WsmoFactory to Concept interface
 *
 * Revision 1.2  2005/08/08 08:24:40  vassil_momtchev
 * javadoc added, bugfixes
 *
*/