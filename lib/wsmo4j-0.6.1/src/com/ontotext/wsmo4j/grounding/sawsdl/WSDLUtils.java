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

import java.util.*;


public class WSDLUtils {

    public static final String ELEMENT_DECLARATION_NS = "wsdl.elementDeclaration";
    public static final String TYPE_DEFINITION_NS = "wsdl.typeDefinition";

    public static final String INTERFACE_NS = "wsdl.interface";
    public static final String INTERFACE_FAULT_NS = "wsdl.interfaceFault";
    public static final String OPERATION_NS = "wsdl.interfaceOperation";
    public static final String MESSAGE_NS = "wsdl.interfaceMessageReference";
    public static final String MESSAGE_FAULT_NS = "wsdl.interfaceFaultReference";
    
    public static final char SEP = '/';
    
    
    public static String createElementIdentifier(String elName) {
        return ELEMENT_DECLARATION_NS + '(' + elName + ')';
    }
    
    public static String createTypeIdentifier(String typeName) {
        return TYPE_DEFINITION_NS + '(' + typeName + ')';
    }

    public static String createInterfaceIdentifier(String ifaceName) {
        return INTERFACE_NS + '(' + ifaceName + ')';
    }

    public static String createInterfaceFaultIdentifier(String ifaceName, String faultName) {
        return INTERFACE_FAULT_NS + '(' + ifaceName + SEP + faultName + ')';
    }
    
    public static String createOperationIdentifier(String ifaceName, String opName) {
        return OPERATION_NS + '(' + ifaceName + SEP + opName + ')';
    }
    public static String createMessageIdentifier(String ifaceName, String opName, String messageName) {
        return MESSAGE_NS + '(' + ifaceName + SEP + opName + SEP + messageName + ')';
    }
    public static String createMessageFaultIdentifier(String ifaceName, 
                                                     String opName, 
                                                     String messageName,
                                                     String faultName) {
        return MESSAGE_FAULT_NS + '(' + ifaceName + SEP + opName + SEP + messageName + SEP + faultName + ')';
    }
    
/*    public static Element locateDOMNode(String fragmentID, Document context, NamespaceContextProvider nsProvider) {
        
        String wsdlPrefix = findPrefixFor(SemWsdlGrounding.WSDL_NS_URI, nsProvider, "wsdl");
        String xsPrefix = findPrefixFor(XMLConstants.W3C_XML_SCHEMA_NS_URI, nsProvider, "xs");
        if (wsdlPrefix.length() > 0) {
            wsdlPrefix += ':';
        }
        if (xsPrefix.length() > 0) {
            xsPrefix += ':';
        }

        int nsStart;
        String queryExpr = null;
        if ((nsStart = fragmentID.indexOf(ELEMENT_DECLARATION_NS + '(')) != -1) {
            queryExpr = buildElementXPath(fragmentID.substring(
                    fragmentID.indexOf('(', nsStart + ELEMENT_DECLARATION_NS.length()) + 1,
                    fragmentID.lastIndexOf(')')).trim(),
                    wsdlPrefix,
                    xsPrefix,
                    nsProvider);
        }
        else if ((nsStart = fragmentID.indexOf(TYPE_DEFINITION_NS + '(')) != -1) {
            queryExpr = buildTypeXPath(fragmentID.substring(
                    fragmentID.indexOf('(', nsStart + TYPE_DEFINITION_NS.length()) + 1,
                    fragmentID.lastIndexOf(')')).trim(),
                    wsdlPrefix,
                    xsPrefix,
                    nsProvider);
        }
        else if ((nsStart = fragmentID.indexOf(INTERFACE_NS + '(')) != -1) {
            queryExpr = buildInterfaceXPath(fragmentID.substring(
                    fragmentID.indexOf('(', nsStart + INTERFACE_NS.length()) + 1,
                    fragmentID.lastIndexOf(')')).trim(),
                    wsdlPrefix,
                    xsPrefix,
                    nsProvider);
        }
        else if ((nsStart = fragmentID.indexOf(OPERATION_NS + '(')) != -1) {
            queryExpr = buildOperationXPath(fragmentID.substring(
                    fragmentID.indexOf('(', nsStart + OPERATION_NS.length()) + 1,
                    fragmentID.lastIndexOf(')')).trim(),
                    wsdlPrefix,
                    xsPrefix,
                    nsProvider);
        }
        else if ((nsStart = fragmentID.indexOf(MESSAGE_NS + '(')) != -1) {
            queryExpr = buildMessageXPath(fragmentID.substring(
                    fragmentID.indexOf('(', nsStart + MESSAGE_NS.length()) + 1,
                    fragmentID.lastIndexOf(')')).trim(),
                    wsdlPrefix,
                    xsPrefix,
                    nsProvider);
        }
        if (queryExpr == null) {
            return null;
        }
        
        XPath xpath = XPathFactory.newInstance().newXPath();
        if (nsProvider != null) {
            xpath.setNamespaceContext(nsProvider);
        }
        xpath.setNamespaceContext(nsProvider);
        try {
            NodeList sourceElements = (NodeList)xpath.evaluate(queryExpr, context, XPathConstants.NODESET);
            if (sourceElements.getLength() > 0) {
                return (Element)sourceElements.item(0);
            }
        }
        catch(XPathExpressionException xpe) {
            xpe.printStackTrace();
        }
        return null;
    }*/
    
    public static String buildElementXPath(String elName,
                                           String wsdlPrefix,
                                           String xsPrefix,
                                           SimpleNamespaceContext nsProvider) {
        if (elName.length() == 0) {
            return null;
        }
        return "/" + wsdlPrefix + "description/"+ wsdlPrefix + "types/"+ xsPrefix + "schema//"+ xsPrefix + "element[@name='"
                + elName
                + "']";
    }

    public static String buildTypeXPath(String typeName,
                                        String wsdlPrefix,
                                        String xsPrefix, 
                                        SimpleNamespaceContext nsProvider) {
        if (typeName.length() == 0) {
            return null;
        }
        return "/" + wsdlPrefix + "description/"+ wsdlPrefix + "types/"
                + xsPrefix + "schema//"+ xsPrefix + "complexType[@name='"
                + typeName
                + "'] | /" + wsdlPrefix + "description/"+ wsdlPrefix + "types/"
                + xsPrefix + "schema//"+ xsPrefix + "simpleType[@name='"
                + typeName
                + "']";
    }
    
    public static String buildInterfaceXPath(String ifaceName,
                                             String wsdlPrefix,
                                             String xsPrefix, 
                                             SimpleNamespaceContext nsProvider) {
        if (ifaceName.length() == 0) {
            return null;
        }
        return "/" + wsdlPrefix + "description/"+ wsdlPrefix + "interface[@name='"
                + ifaceName
                + "']";
    }
    public static String buildOperationXPath(String operationPath,
                                             String wsdlPrefix,
                                             String xsPrefix, 
                                             SimpleNamespaceContext nsProvider) {
        if (operationPath.length() == 0) {
            return null;
        }
        int sepIndex = operationPath.indexOf('/');
        if (sepIndex == -1) {
            return null;
        }
        String ifaceName = operationPath.substring(0, sepIndex).trim();
        String opName = operationPath.substring(sepIndex + 1).trim();
        
        return "/" + wsdlPrefix + "description/"+ wsdlPrefix + "interface[@name='"
                + ifaceName
                + "']/"+ wsdlPrefix +"operation[@name='" + opName + "']";
    }

    public static String buildMessageXPath(String messagePath,
                                           String wsdlPrefix,
                                           String xsPrefix, 
                                           SimpleNamespaceContext nsProvider) {
        if (messagePath.length() == 0) {
            return null;
        }
        int sepIndex = messagePath.indexOf('/');
        if (sepIndex == -1) {
            return null;
        }
        int sepIndex2 = messagePath.indexOf('/', sepIndex + 1);
        
        String ifaceName = messagePath.substring(0, sepIndex).trim();
        String opName = messagePath.substring(sepIndex + 1, sepIndex2).trim();
        String messageName = messagePath.substring(sepIndex2+ 1).trim();
        
        return "/" + wsdlPrefix + "description/"+ wsdlPrefix + "interface[@name='"
        + ifaceName
        + "']/" + wsdlPrefix + "operation[@name='" + opName + "']"
        + "/child::*[@messageLabel='" + messageName + "']";
    }

    static String findPrefixFor(String URI, SimpleNamespaceContext nsContainer, String defaultVal) {
        
        String prefix = defaultVal;
        prefix = nsContainer.getPrefix(URI);
        if (prefix == null) {
            return defaultVal;
        }
        return prefix;
    }

    public static List<String> splitIRIsFromString(String attrValue) {
        List<String> result = new LinkedList<String>();
        StringTokenizer tokenizer = new StringTokenizer(attrValue);
        while(tokenizer.hasMoreTokens()) {
            result.add(tokenizer.nextToken());
        }
        return result;
    }

    public static String constructFromIRIs(List<String> iris) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < iris.size(); i++) {
            if (i > 0) {
                result.append(' ');
            }
            result.append(iris.get(i));
        }
        return result.toString();
    }

    public static boolean matchName(String tag, String qNS, String qLocal) {
        if (qNS.length() == 0) {
            return tag.equals(qLocal);
        }
        else {
            return tag.equals(qNS + ':' + qLocal) || tag.equals(qLocal);
        }
    }

}

/*
 * $Log: WSDLUtils.java,v $
 * Revision 1.3  2007/04/27 13:34:40  alex_simov
 * bugfix
 *
 * Revision 1.2  2007/04/25 16:52:01  alex_simov
 * no message
 *
 *
 */
