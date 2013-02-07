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

import java.util.*;

import javax.xml.namespace.QName;

import org.w3c.dom.*;
import org.wsmo.grounding.sawsdl.*;

public class ElementRefsHolder {

    private Element xmlElement;
    private List<ModelRef> refs;
    private List<SchemaMapping> schemas;
    private String sawsdlPrefix = "";
    private QName typeReference;
    // todo this needs some methods that would allow manipulation of "" as lifting or lowering mapping
    private boolean haveLiftingMapping = false;
    private boolean haveLoweringMapping = false;

    ElementRefsHolder(Element source, 
                      String sawsdlNSPrefix, 
                      List<ModelRef> modelRefs, 
                      List<SchemaMapping> schemaMappings, 
                      QName typeref,
                      boolean haveLifting,
                      boolean haveLowering) {
        this.xmlElement = source;
        if (sawsdlNSPrefix != null) {
            this.sawsdlPrefix = sawsdlNSPrefix;
        }
        this.refs = modelRefs;
        this.schemas = schemaMappings;
        this.typeReference = typeref;
        this.haveLiftingMapping = haveLifting;
        this.haveLoweringMapping = haveLowering;
    }
    
    public boolean hasLiftingSchemaMapping() {
        return this.haveLiftingMapping;
    }
    
    public boolean hasLoweringSchemaMapping() {
        return this.haveLoweringMapping;
    }
    
    public Element getXMLElement() {
        return this.xmlElement;
    }
    
    public QName getTypeReference() { 
        return this.typeReference;
    }
    
    public List<ModelRef> listRefs() {
        return Collections.unmodifiableList(this.refs);
    }

    public List<SchemaMapping> listSchemaMappings() {
        return Collections.unmodifiableList(this.schemas);
    }

    public boolean addReference(ModelRef newRef) {
        if (this.refs.contains(newRef)) {
            return false;
        }
        this.refs.add(newRef);
        if (this.refs.size() > 1) {
            Attr attr = findAnnotationAttribute(SAWSDL.REF_ATTRIBUTE);
            if (attr != null) {
                attr.setValue(attr.getValue() + " " + newRef.getTarget().toString());
                return true; // if attr == null, a new attr will be added
             }
        }
        xmlElement.setAttributeNS(SAWSDL.SAWSDL_NS_URI, 
                    (this.sawsdlPrefix.length() > 0) ? 
                            this.sawsdlPrefix + ":" + SAWSDL.REF_ATTRIBUTE
                            : SAWSDL.REF_ATTRIBUTE,
                    newRef.getTarget().toString());
        return true;
    }
    
    public boolean removeReference(ModelRef oldRef) {
        boolean removed = this.refs.remove(oldRef);
        if (removed) {
            Attr attr = findAnnotationAttribute(SAWSDL.REF_ATTRIBUTE);
            if (this.refs.size() == 0) { // no values remained -> remove attribute
                xmlElement.removeAttributeNode(attr);
            }
            else {
                List<String> vals = WSDLUtils.splitIRIsFromString(attr.getValue());
                vals.remove(oldRef.getTarget().toString());
                attr.setValue(WSDLUtils.constructFromIRIs(vals));
            }
        }
        return removed;
    }

    public boolean addSchemaMapping(SchemaMapping newMapping) {
        if (this.schemas.contains(newMapping)) {
            return false;
        }
        this.schemas.add(newMapping);
        String attrName = (newMapping instanceof LiftingSchemaMapping) ?
                    SAWSDL.LIFTING_ATTRIBUTE
                    : SAWSDL.LOWERING_ATTRIBUTE;
        if (newMapping instanceof LiftingSchemaMapping) {
            this.haveLiftingMapping = true;
        }
        if (newMapping instanceof LoweringSchemaMapping) {
            this.haveLoweringMapping = true;
        }
        if (this.schemas.size() > 1) {
            Attr attr = findAnnotationAttribute(attrName);
            if (attr != null) {
                attr.setValue(attr.getValue() + " " + newMapping.getSchema().toString());
                return true; // if attr == null, a new attr will be added
            }
        }
        xmlElement.setAttributeNS(SAWSDL.SAWSDL_NS_URI, 
                (this.sawsdlPrefix.length() > 0) ? 
                        this.sawsdlPrefix + ":" + attrName
                        : attrName,
                        newMapping.getSchema().toString());
        return true;
    }
    
    public boolean removeSchemaMapping(SchemaMapping oldMapping) {
        
        boolean removed = this.schemas.remove(oldMapping);
        if (removed) {
            String attrName = (oldMapping instanceof LiftingSchemaMapping) ?
                    SAWSDL.LIFTING_ATTRIBUTE
                    : SAWSDL.LOWERING_ATTRIBUTE;

            Attr attr = findAnnotationAttribute(attrName);
            if (this.schemas.size() == 0) { // no values remained -> remove attribute
                xmlElement.removeAttributeNode(attr);
            }
            else {
                List<String> vals = WSDLUtils.splitIRIsFromString(attr.getValue());
                vals.remove(oldMapping.getSchema().toString());
                attr.setValue(WSDLUtils.constructFromIRIs(vals));
            }
        }
        return removed;
    }
    
    private Attr findAnnotationAttribute(String localName) {
        NamedNodeMap attrs = this.xmlElement.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Attr attr = (Attr)attrs.item(i);
            if ((SAWSDL.SAWSDL_NS_URI.equals(attr.getNamespaceURI()) 
                    || (attr.getNamespaceURI() == null 
                            && this.sawsdlPrefix.length() == 0))
                    && (attr.getName().equals(sawsdlPrefix + ":" + localName) 
                            || attr.getName().equals(localName))) {
                return attr;
            }
        }
        return null;
    }

}

/*
 * $Log: ElementRefsHolder.java,v $
 * Revision 1.5  2007/06/18 15:30:52  alex_simov
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
