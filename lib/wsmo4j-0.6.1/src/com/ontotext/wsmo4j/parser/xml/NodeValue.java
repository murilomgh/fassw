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

import java.math.*;
import java.util.*;

import org.deri.wsmo4j.logicalexpression.*;
import org.omwg.ontology.*;
import org.w3c.dom.*;
import org.wsmo.common.*;

import com.ontotext.wsmo4j.serializer.xml.*;

/**
 * Helper type to serialize/deserialize xml element of data values
 * 
 * @author not attributable
 */
class NodeValue {

    private static ConstantTransformer conTran = ConstantTransformer.getInstance();

    static Value deserialize(Node xmlNode, WsmlXmlParser parser) {
        if (xmlNode == null || parser == null || xmlNode.getNodeName() != "value"
                || xmlNode.getNodeType() != Node.ELEMENT_NODE) {
            throw new IllegalArgumentException();
        }

        String type = xmlNode.getAttributes().getNamedItem("type").getNodeValue();
        
        if (conTran.isSimpleDataType(type)) { // SimpleDataType
            WsmlDataType dt = parser.getDataFactory().createWsmlDataType(type);
            String valueText = WsmlXmlHelper.getElementText(xmlNode).trim();
            return parser.getDataFactory().createDataValueFromJavaObject(dt, valueText);
        }
        else if (conTran.isDataType(type)) {
            ComplexDataType cdt = (ComplexDataType) parser.getDataFactory()
                    .createWsmlDataType(type);
            NodeList nodes = xmlNode.getChildNodes();
            Vector <SimpleDataValue> argVal = new Vector <SimpleDataValue>();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE
                        && node.getNodeName().equals("argument")) {
                    String val = WsmlXmlHelper.getElementText(node).trim();

                    // try to guess the type of each argument
                    try {
                        Integer.parseInt(val);
                        argVal.add(parser.getDataFactory().createWsmlInteger(new BigInteger(val)));
                        continue;
                    }
                    catch (NumberFormatException e) {
                    }
                    try {
                        Double.parseDouble(val);
                        argVal.add(parser.getDataFactory().createWsmlDecimal(new BigDecimal(val)));
                    }
                    catch (NumberFormatException e1) {
                        argVal.add(parser.getDataFactory().createWsmlString(val));
                    }
                }
            }
            return parser.getDataFactory().createDataValue(cdt, argVal.toArray(new SimpleDataValue[] {}));
        }
        else { // Instance IRI
            return parser.getFactory().getInstance(parser.getFactory().createIRI(type));
        }
    }

    static Element serialize(Object value, WsmlXmlSerializer serializer) {
        if (value == null || serializer == null) {
            throw new IllegalArgumentException();
        }

        Element valueElement = serializer.createElement("value");

        if (value instanceof SimpleDataValue) {
            valueElement.setAttribute("type", ((SimpleDataValue) value).getType().getIRI()
                    .toString());
            Text numVal = serializer
                    .createTextNode(((SimpleDataValue) value).getValue().toString());
            valueElement.appendChild(numVal);
        }
        else if (value instanceof ComplexDataValue) {
            ComplexDataValue t = (ComplexDataValue) value;
            valueElement.setAttribute("type", t.getType().getIRI().toString());
            int nbParams = t.getArity();
            for (byte i = 0; i < nbParams; i++) {
                Element argumentElement = serializer.createElement("argument");
                argumentElement.appendChild(serializer.createTextNode(t.getArgumentValue(i)
                        .toString()));
                valueElement.appendChild(argumentElement);
            }
        }
        else if (value instanceof IRI) {
            valueElement.setAttribute("type", ((IRI) value).toString());
        }
        else if (value instanceof Instance) {
            valueElement.setAttribute("type", ((Instance) value).getIdentifier().toString());
        }
        else {
            throw new RuntimeException("Invalid value to serialize!");
        }

        return valueElement;
    }
}

/*
 * $Log: NodeValue.java,v $
 * Revision 1.4  2007/04/02 12:13:22  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.3  2006/01/17 11:52:16  vassil_momtchev
 * value can be instance also
 *
 * Revision 1.2  2006/01/13 10:00:01  vassil_momtchev
 * fixed sf:1401579 and sf:1401577
 *
 * Revision 1.1  2005/11/28 14:03:48  vassil_momtchev
 * package refactored from com.ontotext.wsmo4j.xmlparser to com.ontotext.wsmo4j.parser.xml
 *
 * Revision 1.5  2005/09/17 08:56:25  vassil_momtchev
 * Factory.createDataFactory(map) replaced new DataFactoryImpl()
 *
 * Revision 1.4  2005/09/16 14:02:45  alex_simov
 * Identifier.asString() removed, use Object.toString() instead
 * (Implementations MUST override toString())
 *
 * Revision 1.3  2005/09/06 18:35:14  holgerlausen
 * adopted parser serializer to use datatypefactory
 *
 * Revision 1.2  2005/08/08 08:24:40  vassil_momtchev
 * javadoc added, bugfixes
 *
 */