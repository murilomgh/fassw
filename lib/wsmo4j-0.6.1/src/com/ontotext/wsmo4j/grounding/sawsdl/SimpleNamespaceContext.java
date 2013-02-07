/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2004-2006, Ontotext Lab. / Sirma Group

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

package com.ontotext.wsmo4j.grounding.sawsdl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.*;

import org.w3c.dom.*;
import org.wsmo.grounding.sawsdl.SAWSDL;

public class SimpleNamespaceContext implements NamespaceContext {
    
    protected Map<String, String> nsContainer;

    public SimpleNamespaceContext() {
        nsContainer = new HashMap<String, String>();
    }
    
    public SimpleNamespaceContext(Document doc) {
        this();
        XPath xpath = XPathFactory.newInstance().newXPath();
        SimpleNamespaceContext snc = new SimpleNamespaceContext();
        xpath.setNamespaceContext(snc);
        
        try {
            String expression = "/descendant-or-self::*/namespace::*";
            NodeList nsDeclarations = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
            String defaultNS = null;
            for(int i = 0; i<nsDeclarations.getLength(); i++) {
                Attr attr = (Attr) nsDeclarations.item(i);
                if (attr.getName().equals(XMLConstants.XMLNS_ATTRIBUTE)) {
                    defaultNS = attr.getValue();
                }
                else {
                    this.nsContainer.put(attr.getLocalName(), attr.getValue());
                }
            }
            if (defaultNS != null) {
                if (defaultNS.equals(SAWSDL.WSDL_NS_URI)) {
                    String prefx = "wsdl";
                    while (this.nsContainer.containsKey(prefx)) {
                        prefx += '1';
                    }
                    this.nsContainer.put(prefx, defaultNS);
                }
                else if (defaultNS.equals(XMLConstants.W3C_XML_SCHEMA_NS_URI)) {
                    String prefx = "xs";
                    while (this.nsContainer.containsKey(prefx)) {
                        prefx += '1';
                    }
                    this.nsContainer.put(prefx, defaultNS);
                }
                else {
                    this.nsContainer.put("", defaultNS);
                }
            }
        }
        catch(XPathExpressionException xpathError) {
        // unrechable state
        }
        if (false == this.nsContainer.containsValue(SAWSDL.SAWSDL_NS_URI)) {
            this.nsContainer.put(SAWSDL.DEFAULT_SAWSDL_NS_PREFIX, SAWSDL.SAWSDL_NS_URI);
        }
    }

    public String getNamespaceURI(String prefix) {
        if (nsContainer.containsKey(prefix)) {
            return nsContainer.get(prefix);
        }
        if (prefix.equals(XMLConstants.XML_NS_PREFIX)) {
            return XMLConstants.XML_NS_URI;
        }
        if (prefix.equals(XMLConstants.XMLNS_ATTRIBUTE)) {
            return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
        }
        return XMLConstants.NULL_NS_URI;
    }

    public String getPrefix(String namespaceURI) {
        for(Iterator it = nsContainer.keySet().iterator(); it.hasNext() ;) {
            String prefix = (String)it.next();
            if (nsContainer.get(prefix).equals(namespaceURI)) {
                return prefix;
            }
        }
        if (namespaceURI.equals(XMLConstants.XML_NS_URI)) {
            return XMLConstants.XML_NS_PREFIX;
        }
        if (namespaceURI.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) {
            return XMLConstants.XMLNS_ATTRIBUTE;
        }
        return null;
    }

    public Iterator getPrefixes(String namespaceURI) {
        Set<String> result = new HashSet<String>();
        for(Iterator it = nsContainer.keySet().iterator(); it.hasNext() ;) {
            String prefix = (String)it.next();
            if (nsContainer.get(prefix).equals(namespaceURI)) {
                result.add(prefix);
            }
        }
        return result.iterator();
    }
    
    public void addMapping(String prefix,String uri) {
        this.nsContainer.put(prefix, uri);
    }
}

/*
 * $Log: SimpleNamespaceContext.java,v $
 * Revision 1.4  2007/04/27 13:34:40  alex_simov
 * bugfix
 *
 * Revision 1.3  2007/04/26 17:05:57  alex_simov
 * no message
 *
 * Revision 1.2  2007/04/25 16:52:01  alex_simov
 * no message
 *
 * Revision 1.1  2007/04/24 14:52:10  alex_simov
 * moved to sawsdl subpackage
 *
 * Revision 1.1  2006/07/03 15:40:15  mihail-ik
 * Created for manual mapping of prefix <-> uri
 *
 */
