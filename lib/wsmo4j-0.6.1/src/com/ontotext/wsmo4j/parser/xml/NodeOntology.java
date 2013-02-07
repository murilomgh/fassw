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
import org.wsmo.wsml.*;

import com.ontotext.wsmo4j.serializer.xml.*;

/**
 * Helper type to serialize/deserialize xml element ontology
 * 
 * @author not attributable
 */
class NodeOntology {

    static Ontology deserialize(Node xmlNode, WsmlXmlParser parser) throws InvalidModelException,
            ParserException {
        if (parser == null || xmlNode == null || xmlNode.getNodeName() != "ontology") {
            throw new IllegalArgumentException();
        }

        IRI iri = parser.getFactory().createIRI(WsmlXmlHelper.getAttrValue(xmlNode, "name"));
        Ontology ontology = parser.getFactory().createOntology(iri);
        NodeTopEntity.processTopEntityNode(ontology, xmlNode, parser);

        NodeList nodes = xmlNode.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeName() == "nonFunctionalProperties") {
                NodeNFP.deserialize(ontology, node, parser);
            }
            else if (node.getNodeName() == "concept") {
                ontology.addConcept(NodeConcept.deserialize(node, parser));
            }
            else if (node.getNodeName() == "relation") {
                ontology.addRelation(NodeRelation.deserialize(node, parser));
            }
            else if (node.getNodeName() == "instance") {
                ontology.addInstance(NodeInstance.deserialize(node, parser));
            }
            else if (node.getNodeName() == "relationInstance") {
                ontology.addRelationInstance(NodeRelationInstance.deserialize(node, parser));
            }
            else if (node.getNodeName() == "axiom") {
                ontology.addAxiom(NodeLogExp.deserialize(node, parser));
            }
        }

        return ontology;
    }

    static Element serialize(Ontology ontology, WsmlXmlSerializer serializer) {
        Element ontologyElement = serializer.createElement("ontology");
        ontologyElement.setAttribute("name", ontology.getIdentifier().toString());
        NodeTopEntity.serializeTopEntity(ontologyElement, ontology, serializer);

        if (!ontology.listNFPValues().isEmpty()) {
            NodeNFP.serialize(ontologyElement, ontology, serializer);
        }

        if (!ontology.listConcepts().isEmpty()) {
            Object[] concepts = ontology.listConcepts().toArray();
            for (int i = 0; i < concepts.length; i++) {
                ontologyElement.appendChild(NodeConcept
                        .serialize((Concept) concepts[i], serializer));
            }
        }

        if (!ontology.listInstances().isEmpty()) {
            Object[] instances = ontology.listInstances().toArray();
            for (int i = 0; i < instances.length; i++) {
                ontologyElement.appendChild(NodeInstance.serialize((Instance) instances[i],
                        serializer));
            }
        }

        if (!ontology.listRelations().isEmpty()) {
            Object[] relations = ontology.listRelations().toArray();
            for (int i = 0; i < relations.length; i++)
                ontologyElement.appendChild(NodeRelation.serialize((Relation) relations[i],
                        serializer));
        }

        if (!ontology.listRelationInstances().isEmpty()) {
            Object[] relationInstances = ontology.listRelationInstances().toArray();
            for (int i = 0; i < relationInstances.length; i++) {
                ontologyElement.appendChild(NodeRelationInstance.serialize(
                        (RelationInstance) relationInstances[i], serializer));
            }
        }

        if (!ontology.listAxioms().isEmpty()) {
            Object[] axioms = ontology.listAxioms().toArray();
            for (int i = 0; i < axioms.length; i++) {
                ontologyElement.appendChild(
                        NodeLogExp.serialize("axiom", (Axiom) axioms[i], serializer));
            }
        }

        return ontologyElement;
    }
}

/*
 * $Log: NodeOntology.java,v $
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