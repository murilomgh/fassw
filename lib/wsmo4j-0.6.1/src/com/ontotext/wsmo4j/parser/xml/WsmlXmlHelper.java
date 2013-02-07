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

import org.w3c.dom.Node;

/**
 * Helper type to handle common xml routines
 * 
 * @author not attributable
 */
class WsmlXmlHelper {
    static Node getFirstChildElement(Node node) {
        if (node == null) {
            throw new IllegalArgumentException();
        }

        Node result = node.getFirstChild();
        while (result != null) {
            if (result.getNodeType() == Node.ELEMENT_NODE) {
                return result;
            }
            result = result.getNextSibling();
        }
        return null;
    }

    static String getAttrValue(Node node, String attrName) {
        if (node == null || attrName == null || node.getNodeType() != Node.ELEMENT_NODE) {
            throw new IllegalArgumentException();
        }
        
        try {
            return node.getAttributes().getNamedItem(attrName).getNodeValue().trim();
        }
        catch (NullPointerException e) {
            throw new RuntimeException("Could not find a mandatory attribute [" + 
                    attrName + "] for the node: " + node.toString());
        }
    }

    static String getElementText(Node node) {
        if (node == null || node.getNodeType() != Node.ELEMENT_NODE) {
            throw new IllegalArgumentException();
        }
        Node child = node.getFirstChild();
        if (child.getNodeType() == Node.TEXT_NODE) {
            return child.getNodeValue().trim();
        }
        throw new IllegalArgumentException();
    }
}

/*
 * $Log: WsmlXmlHelper.java,v $
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
