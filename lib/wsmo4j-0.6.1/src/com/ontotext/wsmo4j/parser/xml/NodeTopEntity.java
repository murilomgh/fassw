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

import org.w3c.dom.*;
import org.wsmo.common.*;
import org.wsmo.common.Entity;
import org.wsmo.common.exception.*;

import com.ontotext.wsmo4j.serializer.xml.*;

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */
public class NodeTopEntity {

    static void processTopEntityNode(TopEntity te, Node xmlNode, WsmlXmlParser parser)
            throws InvalidModelException {
        if (te == null || xmlNode == null || parser == null
                || xmlNode.getNodeType() != Element.ELEMENT_NODE) {
            throw new IllegalArgumentException();
        }

        NodeList nodes = xmlNode.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeName() == "importsOntology") {
                te.addOntology(parser.getFactory().createOntology(
                        parser.getFactory().createIRI(WsmlXmlHelper.getElementText(node))));
            }
            else if (node.getNodeName() == "usesMediator") {
                IRI iri = parser.getFactory().createIRI(WsmlXmlHelper.getElementText(node));
                te.addMediator(iri);
            }
        }
    }

    static void serializeTopEntity(Element parent, TopEntity te, WsmlXmlSerializer serializer) {
        if (!te.listOntologies().isEmpty()) {
            Object[] entities = te.listOntologies().toArray();
            for (int i = 0; i < entities.length; i++) {
                Element importOnto = serializer.createElement("importsOntology");
                parent.appendChild(importOnto);
                importOnto.appendChild(serializer.createTextNode(((Entity) entities[i])
                        .getIdentifier().toString()));
            }
        }

        if (!te.listMediators().isEmpty()) {
            Object[] entities = te.listMediators().toArray();
            for (int i = 0; i < entities.length; i++) {
                Element useMediator = serializer.createElement("usesMediator");
                parent.appendChild(useMediator);
                useMediator.appendChild(serializer.createTextNode(((Identifier) entities[i])
                        .toString()));
            }
        }
    }
}

/*
 * $Log: NodeTopEntity.java,v $
 * Revision 1.2  2006/04/05 13:25:26  vassil_momtchev
 * usesMediator now refer mediators by  IRI instead of handle to object
 *
 * Revision 1.1  2006/03/29 11:20:51  vassil_momtchev
 * mediator support added; some code refactored; minor bugs fixed
 *
*/
