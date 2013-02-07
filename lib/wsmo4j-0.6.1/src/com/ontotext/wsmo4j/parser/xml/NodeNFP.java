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
 * Helper type to serialize/deserialize xml element non functional properties
 * 
 * @author not attributable
 */
class NodeNFP {
    static void deserialize(Entity entity, Node xmlNode, WsmlXmlParser parser)
            throws InvalidModelException {
        if (entity == null || xmlNode == null || parser == null
                || xmlNode.getNodeName() != "nonFunctionalProperties") {
            throw new IllegalArgumentException();
        }

        NodeList nodes = xmlNode.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeName() == "attributeValue") {
                IRI nfpIri = parser.getFactory()
                        .createIRI(WsmlXmlHelper.getAttrValue(node, "name"));

                Node nodeValue = WsmlXmlHelper.getFirstChildElement(node);
                Object value = NodeValue.deserialize(nodeValue, parser);
                
                if (value instanceof DataValue) {
                    entity.addNFPValue(nfpIri, (DataValue) value);
                }
                else if (value instanceof Identifier) {
                    entity.addNFPValue(nfpIri, (Identifier) value);
                }
                else if (value instanceof Instance) {
                    entity.addNFPValue(nfpIri, ((Instance) value).getIdentifier());
                }
            }
        }
    }

    static void serialize(Element parent, Entity entity, WsmlXmlSerializer serializer) {
        if (entity.listNFPValues().isEmpty()) {
            return;
        }

        Element nfpElement = serializer.createElement("nonFunctionalProperties");
        parent.appendChild(nfpElement);
        Map map = entity.listNFPValues();
        for (Iterator i = map.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            Element attr = serializer.createElement("attributeValue");
            nfpElement.appendChild(attr);
            attr.setAttribute("name", entry.getKey().toString());

            Set dataValues = (Set) entry.getValue();
            for (Iterator j = dataValues.iterator(); j.hasNext();) {
                attr.appendChild(NodeValue.serialize(j.next(), serializer));
            }
        }
    }
}

/*
 * $Log: NodeNFP.java,v $
 * Revision 1.2  2006/07/04 14:31:32  vassil_momtchev
 * no longer instances used as nfp values; identifier used instead
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