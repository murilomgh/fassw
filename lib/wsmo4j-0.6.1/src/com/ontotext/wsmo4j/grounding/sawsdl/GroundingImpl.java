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

// todo the distinction of AttributeModelReference, ElementModelRefrence etc. is wrong, 
// this should be done on the qnames, not on the references

package com.ontotext.wsmo4j.grounding.sawsdl;

import java.net.URI;
import java.util.*;

import javax.xml.namespace.QName;

import org.w3c.dom.Document;
import org.wsmo.common.IRI;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.factory.Factory;
import org.wsmo.factory.WsmoFactory;
import org.wsmo.grounding.sawsdl.*;
import org.wsmo.grounding.sawsdl.events.GroundingChangeEvent;
import org.wsmo.grounding.sawsdl.events.GroundingChangeEventListener;

public class GroundingImpl implements Grounding {

    private Document domModel;
    private WsmoFactory factory = Factory.createWsmoFactory(null);
    
    private Map<QName, ElementRefsHolder> wsdlElements;
    private Map<QName, ElementRefsHolder> wsdlAttributes;
    private Map<QName, ElementRefsHolder> wsdlCompexTypes;
    private Map<QName, ElementRefsHolder> wsdlSimpleTypes;
    private Map<QName, ElementRefsHolder> wsdlOperations;
    private Map<QName, ElementRefsHolder> wsdlInterfaces;
    private Map<QName, ElementRefsHolder> wsdlFaults;
    
    private ArrayList<GroundingChangeEventListener> listeners;
    
    public GroundingImpl(Document wsdlDoc) {
        this.domModel = wsdlDoc;
        listeners = new ArrayList<GroundingChangeEventListener>();
        SimpleNamespaceContext snc = new SimpleNamespaceContext(wsdlDoc);
        
        ModelReferencesExtractor extractor = new ModelReferencesExtractor(wsdlDoc, snc, factory);
        
        try {
            wsdlElements = extractor.collectSchemaElementReferences();
            wsdlCompexTypes = extractor.collectSchemaComplexTypeReferences();
            wsdlAttributes = extractor.collectSchemaAttributeReferences();
            wsdlSimpleTypes = extractor.collectSchemaSimpleTypeReferences();
            wsdlInterfaces = extractor.collectInterfaceReferences();
            wsdlFaults = extractor.collectFaultReferences();
            wsdlOperations = extractor.collectOperationReferences();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public Document getWSMLDocument() {
        return this.domModel;
    }
    
    public void addGroundingChangeListener(GroundingChangeEventListener listener) {
        if (false == listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public AttributeModelRef createAttributeModelRef(QName attribute, IRI target) 
            throws InvalidModelException {

        ElementRefsHolder holder = wsdlAttributes.get(attribute);
        
        if (holder == null) {
            throw new InvalidModelException("Attribute definition not found (" + attribute.getLocalPart() + ")!");
        }
        AttributeModelRef newRef = new AttributeModelRefImpl(attribute, target);
        
        // if the new one is equivalent to any existing -> return the canonical reference
        int oldIndex = holder.listRefs().indexOf(newRef);
        if (oldIndex != -1) {
            return (AttributeModelRef)holder.listRefs().get(oldIndex);
        }
        
        if (holder.addReference(newRef)) {
            fireModelRefCreated(attribute, target);
        }
        return newRef;
    }

    public ComplexTypeModelRef createComplexTypeModelRef(QName type, IRI target) 
            throws InvalidModelException {
        
        ElementRefsHolder holder = wsdlCompexTypes.get(type);
        
        if (holder == null) {
            throw new InvalidModelException("ComplexType definition not found (" + type.getLocalPart() + ")!");
        }
        ComplexTypeModelRef newRef = new ComplexTypeModelRefImpl(type, target);
        
        // if the new one is equivalent to any existing -> return the canonical reference
        int oldIndex = holder.listRefs().indexOf(newRef);
        if (oldIndex != -1) {
            return (ComplexTypeModelRef)holder.listRefs().get(oldIndex);
        }
        
        if (holder.addReference(newRef)) {
            fireModelRefCreated(type, target);
        }
        return newRef;
    }

    public ElementModelRef createElementModelRef(QName element, IRI target)
            throws InvalidModelException {
        ElementRefsHolder holder = wsdlElements.get(element);
        
        if (holder == null) {
            throw new InvalidModelException("Element definition not found (" + element.getLocalPart() + ")!");
        }
        ElementModelRef newRef = new ElementModelRefImpl(element, target);
        
        // if the new one is equivalent to any existing -> return the canonical reference
        int oldIndex = holder.listRefs().indexOf(newRef);
        if (oldIndex != -1) {
            return (ElementModelRef)holder.listRefs().get(oldIndex);
        }
        
        if (holder.addReference(newRef)) {
            fireModelRefCreated(element, target);
        }
        return newRef;
    }

    public FaultModelRef createFaultModelRef(QName fault, IRI target) 
            throws InvalidModelException {
        ElementRefsHolder holder = wsdlFaults.get(fault);
        
        if (holder == null) {
            throw new InvalidModelException("Fault definition not found (" + fault.getLocalPart() + ")!");
        }
        FaultModelRef newRef = new FaultModelRefImpl(fault, target);
        
        // if the new one is equivalent to any existing -> return the canonical reference
        int oldIndex = holder.listRefs().indexOf(newRef);
        if (oldIndex != -1) {
            return (FaultModelRef)holder.listRefs().get(oldIndex);
        }
        
        if (holder.addReference(newRef)) {
            fireModelRefCreated(fault, target);
        }
        return newRef;
    }

    public InterfaceCategory createInterfaceCategory(QName iface, IRI target) 
            throws InvalidModelException {
        ElementRefsHolder holder = wsdlInterfaces.get(iface);
        
        if (holder == null) {
            throw new InvalidModelException("Interface definition not found (" + iface.getLocalPart() + ")!");
        }
        InterfaceCategory newRef = new InterfaceCategoryImpl(iface, target);
        
        // if the new one is equivalent to any existing -> return the canonical reference
        int oldIndex = holder.listRefs().indexOf(newRef);
        if (oldIndex != -1) {
            return (InterfaceCategory)holder.listRefs().get(oldIndex);
        }
        
        if (holder.addReference(newRef)) {
            fireModelRefCreated(iface, target);
        }
        return newRef;
    }

    public LiftingSchemaMapping createLiftingSchemaMapping(QName source, URI schemaRef)
            throws InvalidModelException {
        ElementRefsHolder holder = findReferencesHolder(source);
        LiftingSchemaMapping newRef = new LiftingSchemaMappingImpl(source, schemaRef);
        
        // if the new one is equivalent to any existing -> return the canonical reference
        int oldIndex = holder.listSchemaMappings().indexOf(newRef);
        if (oldIndex != -1) {
            return (LiftingSchemaMapping)holder.listSchemaMappings().get(oldIndex);
        }
        
        if (holder.addSchemaMapping(newRef)) {
            fireGroundingChanged(
                    new GroundingChangeEvent(
                            GroundingChangeEvent.ADD_ACTION,
                            GroundingChangeEvent.LIFTING_REF, 
                            source, 
                            schemaRef));
        }
        return newRef;
    }

    public LoweringSchemaMapping createLoweringSchemaMapping(QName source, URI schemaRef) 
            throws InvalidModelException {
        ElementRefsHolder holder = findReferencesHolder(source);
        LoweringSchemaMapping newRef = new LoweringSchemaMappingImpl(source, schemaRef);
        
        // if the new one is equivalent to any existing -> return the canonical reference
        int oldIndex = holder.listSchemaMappings().indexOf(newRef);
        if (oldIndex != -1) {
            return (LoweringSchemaMapping)holder.listSchemaMappings().get(oldIndex);
        }
        
        if (holder.addSchemaMapping(newRef)) {
            fireGroundingChanged(
                    new GroundingChangeEvent(
                            GroundingChangeEvent.ADD_ACTION,
                            GroundingChangeEvent.LOWERING_REF, 
                            source, 
                            schemaRef));
        }
        return newRef;
    }

    public OperationModelRef createOperationModelRef(QName operation, IRI target) 
            throws InvalidModelException {
        ElementRefsHolder holder = wsdlOperations.get(operation);
        
        if (holder == null) {
            throw new InvalidModelException("Operation definition not found (" + operation.getLocalPart() + ")!");
        }
        OperationModelRef newRef = new OperationModelRefImpl(operation, target);
        
        // if the new one is equivalent to any existing -> return the canonical reference
        int oldIndex = holder.listRefs().indexOf(newRef);
        if (oldIndex != -1) {
            return (OperationModelRef)holder.listRefs().get(oldIndex);
        }
        
        if (holder.addReference(newRef)) {
            fireModelRefCreated(operation, target);
        }
        return newRef;
    }

    public SimpleTypeModelRef createSimpleTypeModelRef(QName type, IRI target) 
            throws InvalidModelException {
        ElementRefsHolder holder = wsdlSimpleTypes.get(type);
        
        if (holder == null) {
            throw new InvalidModelException("SimpleType definition not found (" + type.getLocalPart() + ")!");
        }
        SimpleTypeModelRef newRef = new SimpleTypeModelRefImpl(type, target);
        
        // if the new one is equivalent to any existing -> return the canonical reference
        int oldIndex = holder.listRefs().indexOf(newRef);
        if (oldIndex != -1) {
            return (SimpleTypeModelRef)holder.listRefs().get(oldIndex);
        }
        
        if (holder.addReference(newRef)) {
            fireModelRefCreated(type, target);
        }
        return newRef;
    }

    public List<ModelRef> listDeclaredModelRefs() {
        
        assert wsdlElements != null;
        assert wsdlAttributes != null;
        assert wsdlCompexTypes != null;
        assert wsdlSimpleTypes != null;
        assert wsdlInterfaces != null;
        assert wsdlOperations != null;
        assert wsdlFaults != null;

        List<ModelRef> result = new LinkedList<ModelRef>();
        for (ElementRefsHolder holder : wsdlElements.values()) {
            result.addAll(holder.listRefs());
        }
        for (ElementRefsHolder holder : wsdlAttributes.values()) {
            result.addAll(holder.listRefs());
        }
        for (ElementRefsHolder holder : wsdlCompexTypes.values()) {
            result.addAll(holder.listRefs());
        }
        for (ElementRefsHolder holder : wsdlSimpleTypes.values()) {
            result.addAll(holder.listRefs());
        }
        for (ElementRefsHolder holder : wsdlOperations.values()) {
            result.addAll(holder.listRefs());
        }
        for (ElementRefsHolder holder : wsdlInterfaces.values()) {
            result.addAll(holder.listRefs());
        }
        for (ElementRefsHolder holder : wsdlFaults.values()) {
            result.addAll(holder.listRefs());
        }
        return result;
    }

    public List<ModelRef> listDeclaredModelRefs(QName source) throws InvalidModelException {
        ElementRefsHolder holder = findReferencesHolder(source);
        assert holder != null;
        return holder.listRefs();
    }

    public List<ModelRef> listModelRefs(QName source) throws InvalidModelException {
        ElementRefsHolder holder = findReferencesHolder(source);
        assert holder != null;
        QName typeref = holder.getTypeReference();
        if (typeref == null) {
            return holder.listRefs();
        } else {
            List<ModelRef> result = new LinkedList<ModelRef>();
            result.addAll(holder.listRefs());
            addPropagatedModelRefs(source, holder, result);
            return result;
        }
    }
    
    private void addPropagatedModelRefs(QName source, ElementRefsHolder srcHolder, List<ModelRef> result) throws InvalidModelException {
        QName typeref = srcHolder.getTypeReference();
        if (typeref == null) {
            return;
        }
        
        ElementRefsHolder holder = findReferencesHolderSafely(typeref);
        if (holder == null) {
            return;
        }

        for (ModelRef ref : holder.listRefs()) {
            if (srcHolder.getXMLElement().getLocalName().equals("element")) {
                result.add(new ElementModelRefImpl(source, ref.getTarget()));
            } else if (srcHolder.getXMLElement().getLocalName().equals("attribute")) {
                result.add(new AttributeModelRefImpl(source, ref.getTarget()));
            }
        }

    }

    private void addPropagatedSchemaMappings(QName source, ElementRefsHolder srcHolder, List<SchemaMapping> result) throws InvalidModelException {
        QName typeref = srcHolder.getTypeReference();
        if (typeref == null) {
            return;
        }
        
        boolean haveLifting = srcHolder.hasLiftingSchemaMapping();
        boolean haveLowering = srcHolder.hasLoweringSchemaMapping();
        
        if (haveLifting && haveLowering) {
            return;
        }
        
        ElementRefsHolder holder = findReferencesHolderSafely(typeref);
        if (holder == null) {
            return;
        }

        for (SchemaMapping ref : holder.listSchemaMappings()) {
            if (ref instanceof LiftingSchemaMapping && !haveLifting) {
                result.add(new LiftingSchemaMappingImpl(source, ref.getSchema()));
            }
            if (ref instanceof LoweringSchemaMapping && !haveLowering) {
                result.add(new LoweringSchemaMappingImpl(source, ref.getSchema()));
            }
        }
    }

    public List<ModelRef> listModelRefs() throws InvalidModelException {
        assert wsdlElements != null;
        assert wsdlAttributes != null;
        assert wsdlCompexTypes != null;
        assert wsdlSimpleTypes != null;
        assert wsdlInterfaces != null;
        assert wsdlOperations != null;
        assert wsdlFaults != null;
        
        List<ModelRef> result = new LinkedList<ModelRef>();
        for (Map<QName, ElementRefsHolder> map : 
                        new Map[] { wsdlElements, wsdlAttributes, wsdlCompexTypes, wsdlSimpleTypes,
                                             wsdlOperations, wsdlInterfaces, wsdlFaults }) {
            
            for (QName source : map.keySet()) {
                ElementRefsHolder holder = map.get(source);
                result.addAll(holder.listRefs());
                addPropagatedModelRefs(source, holder, result);
            }
        }

        return result;
    }

    public List<SchemaMapping> listDeclaredSchemaMappings() {
        assert wsdlElements != null;
        assert wsdlAttributes != null;
        assert wsdlCompexTypes != null;
        assert wsdlSimpleTypes != null;
        assert wsdlInterfaces != null;
        assert wsdlOperations != null;
        assert wsdlFaults != null;

        List<SchemaMapping> result = new LinkedList<SchemaMapping>();
        for (ElementRefsHolder holder : wsdlElements.values()) {
            result.addAll(holder.listSchemaMappings());
        }
        for (ElementRefsHolder holder : wsdlAttributes.values()) {
            result.addAll(holder.listSchemaMappings());
        }
        for (ElementRefsHolder holder : wsdlCompexTypes.values()) {
            result.addAll(holder.listSchemaMappings());
        }
        for (ElementRefsHolder holder : wsdlSimpleTypes.values()) {
            result.addAll(holder.listSchemaMappings());
        }
        for (ElementRefsHolder holder : wsdlOperations.values()) {
            result.addAll(holder.listSchemaMappings());
        }
        for (ElementRefsHolder holder : wsdlInterfaces.values()) {
            result.addAll(holder.listSchemaMappings());
        }
        for (ElementRefsHolder holder : wsdlFaults.values()) {
            result.addAll(holder.listSchemaMappings());
        }
        return result;
    }

    public List<SchemaMapping> listDeclaredSchemaMappings(QName source) throws InvalidModelException {
        ElementRefsHolder holder = findReferencesHolder(source);
        assert holder != null;
        return holder.listSchemaMappings();
    }

    public List<SchemaMapping> listSchemaMappings(QName source) throws InvalidModelException {
        ElementRefsHolder holder = findReferencesHolder(source);
        assert holder != null;
        QName typeref = holder.getTypeReference();
        if (typeref == null) {
            return holder.listSchemaMappings();
        } else {
            List<SchemaMapping> result = new LinkedList<SchemaMapping>();
            result.addAll(holder.listSchemaMappings());
            addPropagatedSchemaMappings(source, holder, result);
            return result;
        }
    }

    public List<SchemaMapping> listSchemaMappings() throws InvalidModelException {
        assert wsdlElements != null;
        assert wsdlAttributes != null;
        assert wsdlCompexTypes != null;
        assert wsdlSimpleTypes != null;
        assert wsdlInterfaces != null;
        assert wsdlOperations != null;
        assert wsdlFaults != null;
        
        List<SchemaMapping> result = new LinkedList<SchemaMapping>();
        for (Map<QName, ElementRefsHolder> map : 
                        new Map[] { wsdlElements, wsdlAttributes, wsdlCompexTypes, wsdlSimpleTypes,
                                             wsdlOperations, wsdlInterfaces, wsdlFaults }) {
            
            for (QName source : map.keySet()) {
                ElementRefsHolder holder = map.get(source);
                result.addAll(holder.listSchemaMappings());
                addPropagatedSchemaMappings(source, holder, result);
            }
        }

        return result;    }

    public void removeGroundingChangeListener(GroundingChangeEventListener listener) {
        listeners.remove(listener);
    }

    public void removeModelRef(ModelRef oldReference) throws InvalidModelException {
        if (oldReference == null) {
            throw new IllegalArgumentException();
        }
        ElementRefsHolder holder = findReferencesHolder(oldReference.getSource());
        assert holder != null;
        if (holder.removeReference(oldReference)) {
            fireModelRefRemoved(oldReference.getSource(), oldReference.getTarget());
        }
    }

    public void removeSchemaMapping(SchemaMapping mapping) throws InvalidModelException {
        if (mapping == null) {
            throw new IllegalArgumentException();
        }
        ElementRefsHolder holder = findReferencesHolder(mapping.getSource());
        assert holder != null;
        if (holder.removeSchemaMapping(mapping)) {
            fireGroundingChanged(
                    new GroundingChangeEvent(
                            GroundingChangeEvent.REMOVE_ACTION,
                            (mapping instanceof LiftingSchemaMapping) ? 
                                    GroundingChangeEvent.LIFTING_REF
                                    : GroundingChangeEvent.LOWERING_REF,
                            mapping.getSource(),
                            mapping.getSchema()));
        }
    }
    
    protected void fireModelRefCreated(QName source, IRI ref) {
        GroundingChangeEvent event = new GroundingChangeEvent(
                GroundingChangeEvent.ADD_ACTION,
                GroundingChangeEvent.MODEL_REF,
                source,
                ref);
        fireGroundingChanged(event);
    }
    protected void fireModelRefRemoved(QName source, IRI ref) {
        GroundingChangeEvent event = new GroundingChangeEvent(
                GroundingChangeEvent.REMOVE_ACTION,
                GroundingChangeEvent.MODEL_REF,
                source,
                ref);
        fireGroundingChanged(event);
    }
    
    private void fireGroundingChanged(GroundingChangeEvent event) {
        for (GroundingChangeEventListener listener : this.listeners) {
            try {
                listener.groundingChanged(event);
            }
            catch(Throwable error) {
                error.printStackTrace();
            }
        }
    }
    
    private ElementRefsHolder findReferencesHolder(QName owner)
            throws InvalidModelException {
        if (wsdlElements.containsKey(owner)) {
            return wsdlElements.get(owner);
        }
        if (wsdlAttributes.containsKey(owner)) {
            return wsdlAttributes.get(owner);
        }
        if (wsdlCompexTypes.containsKey(owner)) {
            return wsdlCompexTypes.get(owner);
        }
        if (wsdlSimpleTypes.containsKey(owner)) {
            return wsdlSimpleTypes.get(owner);
        }
        if (wsdlInterfaces.containsKey(owner)) {
            return wsdlInterfaces.get(owner);
        }
        if (wsdlOperations.containsKey(owner)) {
            return wsdlOperations.get(owner);
        }
        if (wsdlFaults.containsKey(owner)) {
            return wsdlFaults.get(owner);
        }
        throw new InvalidModelException("No definition found for :" + owner.getLocalPart());
    }

    private ElementRefsHolder findReferencesHolderSafely(QName owner) {
        if (wsdlElements.containsKey(owner)) {
            return wsdlElements.get(owner);
        }
        if (wsdlAttributes.containsKey(owner)) {
            return wsdlAttributes.get(owner);
        }
        if (wsdlCompexTypes.containsKey(owner)) {
            return wsdlCompexTypes.get(owner);
        }
        if (wsdlSimpleTypes.containsKey(owner)) {
            return wsdlSimpleTypes.get(owner);
        }
        if (wsdlInterfaces.containsKey(owner)) {
            return wsdlInterfaces.get(owner);
        }
        if (wsdlOperations.containsKey(owner)) {
            return wsdlOperations.get(owner);
        }
        if (wsdlFaults.containsKey(owner)) {
            return wsdlFaults.get(owner);
        }
        return null;
    }

}

/*
 * $Log: GroundingImpl.java,v $
 * Revision 1.4  2007/06/18 15:30:53  alex_simov
 * annotation propagation added (on behalf of Jacek)
 *
 * Revision 1.3  2007/04/27 13:34:40  alex_simov
 * bugfix
 *
 * Revision 1.2  2007/04/26 15:48:09  alex_simov
 * no message
 *
 * Revision 1.1  2007/04/25 16:38:35  alex_simov
 * no message
 *
 */
