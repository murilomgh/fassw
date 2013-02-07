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
package com.ontotext.wsmo4j.serializer.xml;

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.w3c.dom.*;
import org.wsmo.common.*;
import org.wsmo.wsml.*;

import com.ontotext.wsmo4j.parser.xml.*;

/**
 * XML serializer of WSMO object (check wsml-xml-syntax.xsd)
 * 
 * @author not attributable
 */
public class WsmlXmlSerializer implements Serializer {

    private Document document;

    /**
     * Constructor should not be invoked directly.
     *  Map properties = new TreeMap();
     *  properties.put(Factory.PROVIDER_CLASS, "com.ontotext.wsmo4j.xmlparser.WsmlXmlSerializer");
     *  serializer = Factory.createSerializer(properties);
     * @param map All parameters are ignored
     */
    public WsmlXmlSerializer(Map <String, Object> map) {
    }

    public Element createElement(String name) {
        return document.createElement(name);
    }

    public Text createTextNode(String value) {
        return document.createTextNode(value);
    }

    /**
     * Serialize array of top entities to XML
     * @param item Entities to serialize
     * @param target Output
     */
    public void serialize(TopEntity[] item, Writer target) throws IOException {
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            document.appendChild(NodeWsml.serializeTopEntity(item, this));
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty(OutputKeys.ENCODING, "UTF8");
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            xformer.transform(new DOMSource(document), new StreamResult(target));
            target.flush();
        }
        catch (ParserConfigurationException e) {
            throw new RuntimeException("Cannot create instance of xml parser!", e);
        }
        catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Serialize array of top entities to XML
     * @param item Entities to serialize
     * @param target Output
     * @param options Ignored
     */
    public void serialize(TopEntity[] item, Writer target, Map options) throws IOException {
        serialize(item, target);
    }

    /**
     * Serialize array of top entities to XML
     * @param item Entities to serialize
     * @param target Output
     */
    public void serialize(TopEntity[] item, final StringBuffer target) {
        Writer myWriter = new Writer() {

            public void write(String arg0) throws IOException {
                target.append(arg0);
            }

            public void write(String arg0, int arg1, int arg2) throws IOException {
                target.append(arg0.toCharArray(), arg1, arg2);
            }

            public void write(int arg0) throws IOException {
                if (arg0 <= 255) {
                    target.append((char) arg0);
                }
                else {
                    byte[] bytes = new byte[] {(byte) (arg0 & 0x00FF), (byte) (arg0 & 0xFF00)};
                    target.append(new String(bytes));
                }
            }

            public void write(char[] arg0) throws IOException {
                target.append(arg0);
            }

            public void write(char[] arg0, int arg1, int arg2) throws IOException {
                target.append(arg0, arg1, arg2);
            }

            public void flush() throws IOException {
                return;
            }

            public void close() throws IOException {
                return;
            }
        };

        try {
            serialize(item, myWriter);
        }
        catch (IOException e) {
            return;
        }
    }

    /**
     * Serialize array of top entities to XML
     * @param item Entities to serialize
     * @param target Output
     * @param options Ignored
     */
    public void serialize(TopEntity[] item, StringBuffer target, Map options) {
        serialize(item, target);
    }
}

/*
 * $Log: WsmlXmlSerializer.java,v $
 * Revision 1.4  2007/04/02 12:13:28  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.3  2005/11/28 14:58:20  vassil_momtchev
 * imports organized; createElement(String) and createTextNode(String) visibility changed to public
 *
 * Revision 1.2  2005/11/28 14:39:44  vassil_momtchev
 * moved from com.ontotext.wsmo4j.xmlparser
 *
 * Revision 1.3  2005/08/08 08:13:10  vassil_momtchev
 * javadoc added, implemented all serialize methods, optimized the xml transformation, well formatted output xml
 *
*/