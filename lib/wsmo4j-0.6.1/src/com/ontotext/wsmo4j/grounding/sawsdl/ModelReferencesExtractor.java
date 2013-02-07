/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2004-2007, Ontotext Lab. / Sirma Group

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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.xpath.*;

import org.w3c.dom.*;
import org.wsmo.common.IRI;
import org.wsmo.factory.WsmoFactory;
import org.wsmo.grounding.sawsdl.*;

public class ModelReferencesExtractor {

    private Document dom;
    private SimpleNamespaceContext nsContext;
    private WsmoFactory factory;
    
    private String xsdPrefix = "";
    private String wsdlPrefix = "";
    private String sawsdlPrefix = "";
    
    ModelReferencesExtractor(Document doc, 
                             SimpleNamespaceContext nsContext,
                             WsmoFactory factory) {
        this.dom = doc;
        this.nsContext = nsContext;
        this.factory = factory;
        
        this.xsdPrefix = (nsContext.getPrefix(XMLConstants.W3C_XML_SCHEMA_NS_URI) != null)?
                nsContext.getPrefix(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                : "";
        this.wsdlPrefix = (nsContext.getPrefix(SAWSDL.WSDL_NS_URI) != null)?
                nsContext.getPrefix(SAWSDL.WSDL_NS_URI)
                : "";
        this.sawsdlPrefix = (nsContext.getPrefix(SAWSDL.SAWSDL_NS_URI) != null)?
                nsContext.getPrefix(SAWSDL.SAWSDL_NS_URI)
                : "";
    }
    
    public Map<QName, ElementRefsHolder> collectOperationReferences() 
            throws XPathExpressionException, XPathException {
        
        String wsdlPrefixXP = makeNSXPath(this.wsdlPrefix);
        String xpathQuery = "/" + wsdlPrefixXP + "description/"+ wsdlPrefixXP + "interface/" + wsdlPrefixXP + "operation";
        return collectReferences(xpathQuery, 
                                 dom.getDocumentElement(), 
                                 dom.getDocumentElement().getAttribute("targetNamespace"));
    }
    
    public Map<QName, ElementRefsHolder> collectInterfaceReferences() 
            throws XPathExpressionException, XPathException {

        String wsdlPrefixXP = makeNSXPath(this.wsdlPrefix);
        String xpathQuery = "/" + wsdlPrefixXP + "description/"+ wsdlPrefixXP + "interface";
        return collectReferences(xpathQuery, 
                                 dom.getDocumentElement(), 
                                 dom.getDocumentElement().getAttribute("targetNamespace"));
    }

    public Map<QName, ElementRefsHolder> collectFaultReferences() 
            throws XPathExpressionException, XPathException {

        String wsdlPrefixXP = makeNSXPath(this.wsdlPrefix);
        String xpathQuery = "/" + wsdlPrefixXP + "description/"+ wsdlPrefixXP + "interface/" + wsdlPrefixXP + "fault";
        return collectReferences(xpathQuery, 
                        dom.getDocumentElement(), 
                        dom.getDocumentElement().getAttribute("targetNamespace"));
    }

    public Map<QName, ElementRefsHolder> collectSchemaElementReferences() 
            throws XPathExpressionException, XPathException {
        return collectSchemaReferences("element", true);
    }

    public Map<QName, ElementRefsHolder> collectSchemaComplexTypeReferences() 
            throws XPathExpressionException, XPathException {
        return collectSchemaReferences("complexType");
    }

    public Map<QName, ElementRefsHolder> collectSchemaSimpleTypeReferences() 
            throws XPathExpressionException, XPathException {
        return collectSchemaReferences("simpleType");
    }

    public Map<QName, ElementRefsHolder> collectSchemaAttributeReferences() 
            throws XPathExpressionException, XPathException {
        return collectSchemaReferences("attribute", true);
    }
    
    public Map<QName, ElementRefsHolder> collectSchemaReferences(String targetTag) 
    throws XPathExpressionException, XPathException {
        return collectSchemaReferences(targetTag, false);
    }

    public Map<QName, ElementRefsHolder> collectSchemaReferences(String targetTag, boolean keepTypeReference) 
        throws XPathExpressionException, XPathException {

        String xsdPrefixXP = makeNSXPath(this.xsdPrefix);
        String elemensQuery = "descendant::" + xsdPrefixXP + targetTag;

        Map<QName, ElementRefsHolder> result = new HashMap<QName, ElementRefsHolder>();

        NodeList schemaRoots = collectSchemaElements(dom.getDocumentElement());
        for (int i = 0; i < schemaRoots.getLength(); i++) {
            Element schemaRoot = (Element)schemaRoots.item(i);
            result.putAll(collectReferences(elemensQuery, 
                    schemaRoot, 
                    schemaRoot.getAttribute("targetNamespace"),
                    keepTypeReference));
        }
        return result;
    }


    private Map<QName, ElementRefsHolder> collectReferences(String xpathQuery, Node context, String targetNS) 
            throws XPathExpressionException, XPathException {  
        return collectReferences(xpathQuery, context, targetNS, false);
    }
    
    private Map<QName, ElementRefsHolder> collectReferences(String xpathQuery, Node context, String targetNS, boolean keepTypeReference) 
            throws XPathExpressionException, XPathException {
        Map<QName, ElementRefsHolder> result = new HashMap<QName, ElementRefsHolder>();
       
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext(this.nsContext);
        XPathExpression queryExpr = xpath.compile(xpathQuery);

        NodeList all = (NodeList)queryExpr.evaluate(context, XPathConstants.NODESET);  
        if (all == null) {
            return result;
        }
        for(int i = 0; i < all.getLength(); i++) {
            if (all.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue; //must not execute here
            }
            Element el = (Element)all.item(i);
            if (false == isSupportedElement(el.getTagName())) {
                continue; // unsupported tag
            }
            
            NamedNodeMap attrs = el.getAttributes();
            QName sourceName = null;
            QName typeReference = null;
            List<String> refs = null;
            List<String> liftingRefs = null;
            List<String> loweringRefs = null;
            boolean liftingRefPresent = false;
            boolean loweringRefPresent = false;

            for(int k = 0; k < attrs.getLength(); k++) {
                Attr attr = (Attr)attrs.item(k);
                if (attr.getName().equals("name")) {
                    sourceName = new QName(targetNS, attr.getValue());
                } else if (keepTypeReference && attr.getName().equals("type")) {
                    String ns = null;
                    String name = attr.getValue();
                    int colonPos = name.indexOf(':'); 
                    if (colonPos != -1) {
                        ns = el.lookupNamespaceURI(name.substring(0, colonPos));
                        name = name.substring(colonPos+1);
                    } else {
                        ns = el.lookupNamespaceURI(null);
                    }
                    typeReference = new QName(ns, name);
                }

                if (SAWSDL.SAWSDL_NS_URI.equals(attr.getNamespaceURI()) 
                        || (attr.getNamespaceURI() == null 
                                && this.sawsdlPrefix.length() == 0)) {
                    if (SAWSDL.REF_ATTRIBUTE.equals(attr.getLocalName()) ||
                            attr.getName().endsWith(SAWSDL.REF_ATTRIBUTE)) {
                        refs = WSDLUtils.splitIRIsFromString(attr.getValue());
                    }
                    if (SAWSDL.LIFTING_ATTRIBUTE.equals(attr.getLocalName()) ||
                            attr.getName().endsWith(SAWSDL.LIFTING_ATTRIBUTE)) {
                        liftingRefs = WSDLUtils.splitIRIsFromString(attr.getValue());
                        liftingRefPresent = true;
                    }
                    if (SAWSDL.LOWERING_ATTRIBUTE.equals(attr.getLocalName()) ||
                            attr.getName().endsWith(SAWSDL.LOWERING_ATTRIBUTE)) {
                        loweringRefs = WSDLUtils.splitIRIsFromString(attr.getValue());
                        loweringRefPresent = true;
                    }

                }
            }
            if (sourceName == null) {
                continue; // no name for element specified
            }
            List<ModelRef> modelRefs = new LinkedList<ModelRef>();
            List<SchemaMapping> schemaMappings = new LinkedList<SchemaMapping>();
            populateRefsHolder(modelRefs,
                               schemaMappings,
                               el.getTagName(),
                               sourceName, 
                               refs, 
                               liftingRefs,
                               loweringRefs);
            ElementRefsHolder holder = 
                new ElementRefsHolder(el, this.sawsdlPrefix, modelRefs, schemaMappings, 
                            typeReference, liftingRefPresent, loweringRefPresent);
            result.put(sourceName, holder);
        }
        return result;
    }
    
    private boolean isSupportedElement(String tag) {
        return WSDLUtils.matchName(tag, this.wsdlPrefix, "interface") 
                || WSDLUtils.matchName(tag, this.wsdlPrefix, "operation")
                || WSDLUtils.matchName(tag, this.wsdlPrefix, "fault")

                || WSDLUtils.matchName(tag, this.xsdPrefix, "element")
                || WSDLUtils.matchName(tag, this.xsdPrefix, "attribute")
                || WSDLUtils.matchName(tag, this.xsdPrefix, "complexType")
                || WSDLUtils.matchName(tag, this.xsdPrefix, "simpleType");
    }

    private boolean isSupportingSchema(String tag) {
        return WSDLUtils.matchName(tag, this.xsdPrefix, "element")
                || WSDLUtils.matchName(tag, this.xsdPrefix, "complexType")
                || WSDLUtils.matchName(tag, this.xsdPrefix, "simpleType");
    }
    
    private void populateRefsHolder(List<ModelRef> refs,
                                    List<SchemaMapping> schemas,
                                    String ownerTag,
                                    QName source, 
                                    List<String> modelRefs, 
                                    List<String> liftingInfo,
                                    List<String> loweringInfo) {
        if (modelRefs != null) {
            for(String ref : modelRefs) {
                ModelRef newRef = null;
                IRI modelRef;
                try {
                   modelRef = factory.createIRI(ref); 
                }
                catch(IllegalArgumentException iae) {
                    throw new IllegalArgumentException(
                            "Invalid identifier ('"+ ref +"') in <"+ ownerTag + "> : " + iae.getMessage(), iae);
                }
                
                if (ownerTag.endsWith("element")) {
                    newRef = new ElementModelRefImpl(source, modelRef);
                }
                else if (ownerTag.endsWith("complexType")) {
                    newRef = new ComplexTypeModelRefImpl(source, modelRef);
                }
                else if (ownerTag.endsWith("simpleType")) {
                    newRef = new SimpleTypeModelRefImpl(source, modelRef);
                }
                else if (ownerTag.endsWith("attribute")) {
                    newRef = new AttributeModelRefImpl(source, modelRef);
                }

                else if (ownerTag.endsWith("operation")) {
                    newRef = new OperationModelRefImpl(source, modelRef);
                }
                else if (ownerTag.endsWith("interface")) {
                    newRef = new InterfaceCategoryImpl(source, modelRef);
                }
                else if (ownerTag.endsWith("fault")) {
                    newRef = new FaultModelRefImpl(source, modelRef);
                }
                
                if (newRef != null) {
                    refs.add(newRef);
                }
            }
        }
        if (false == isSupportingSchema(ownerTag)) {
            return;
        }
        if (liftingInfo != null) {
            for(String ref : liftingInfo) {
                URI schemaRef;
                try {
                    schemaRef = new URI(ref); 
                }
                catch(URISyntaxException uriEx) {
                    throw new IllegalArgumentException(
                            "Invalid lifting schema reference ('"+ ref +"') in <"+ ownerTag + "> : " + uriEx.getMessage(), uriEx);
                }
                schemas.add(
                        new LiftingSchemaMappingImpl(source, schemaRef));
            }
        }
        if (loweringInfo != null) {
            for(String ref : loweringInfo) {
                URI schemaRef;
                try {
                    schemaRef = new URI(ref); 
                }
                catch(URISyntaxException uriEx) {
                    throw new IllegalArgumentException(
                            "Invalid lowering schema reference ('"+ ref +"') in <"+ ownerTag + "> : " + uriEx.getMessage(), uriEx);
                }
                schemas.add(
                        new LoweringSchemaMappingImpl(source, schemaRef));
            }
        }
    }
    
    private NodeList collectSchemaElements(Node context) throws XPathExpressionException {

        String xpathQuery = new StringBuffer()
                .append("/")
                .append(makeNSXPath(this.wsdlPrefix))
                .append("description/")
                .append(makeNSXPath(this.wsdlPrefix))
                .append("types/")
                .append(makeNSXPath(this.xsdPrefix))
                .append("schema").toString();

        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext(this.nsContext);
        XPathExpression queryExpr = xpath.compile(xpathQuery);
        
        return (NodeList)queryExpr.evaluate(context, XPathConstants.NODESET);  
    }
    
    private String makeNSXPath(String prefix) {
        
        if (prefix.length() > 0) {
            return prefix + ":";
        }
        return prefix;
    }
}

/*
 * $Log: ModelReferencesExtractor.java,v $
 * Revision 1.5  2007/06/18 15:30:53  alex_simov
 * annotation propagation added (on behalf of Jacek)
 *
 * Revision 1.4  2007/04/27 15:53:29  alex_simov
 * no message
 *
 * Revision 1.3  2007/04/26 17:05:57  alex_simov
 * no message
 *
 * Revision 1.2  2007/04/26 15:48:09  alex_simov
 * no message
 *
 * Revision 1.1  2007/04/25 16:38:35  alex_simov
 * no message
 *
 */
