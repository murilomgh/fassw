/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2004-2007, Ontotext Lab. / SIRMA

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

package org.wsmo.grounding.sawsdl;

import java.net.URI;
import java.util.List;

import javax.xml.namespace.QName;

import org.wsmo.common.IRI;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.grounding.sawsdl.SchemaMapping;

import org.wsmo.grounding.sawsdl.events.GroundingChangeEventListener;

/**
 * A grounding object containing all SAWSDL annotations of a single WSDL document. 
 * Each model annotation is represented by a single ModelRef object which can not be
 * shared with other groundings.
 * 
 * The Grounding API offers a set of factory methods for creating the various types of
 * semantic model references.
 *
 */
public interface Grounding {

    /**
     * Creates and includes in this grounding object a single model reference from an 
     * XML Schema attribute to a concept in a semantic model.
     * 
     * @param attribute the name of the attribute to be annotated
     * @param target the semantic model id reference 
     * @return A new attribute model reference
     * @throws InvalidModelException if the supplied attribute name is not defined in the
     * current WSDL document
     */
    public AttributeModelRef createAttributeModelRef(QName attribute, IRI target)
    throws InvalidModelException;

    /**
     * Creates and includes in this grounding object a single model reference from an 
     * XML Schema complexType to a concept in a semantic model.
     * 
     * @param type the name of the complexType to be annotated
     * @param target the semantic model id reference 
     * @return A new complexType model reference
     * @throws InvalidModelException if the supplied complexType name is not defined in the
     * current WSDL document
     */
    public ComplexTypeModelRef createComplexTypeModelRef(QName type, IRI target)
    throws InvalidModelException;

    /**
     * Creates and includes in this grounding object a single model reference from an 
     * XML Schema simpleType to a concept in a semantic model.
     * 
     * @param type the name of the simpleType to be annotated
     * @param target the semantic model id reference 
     * @return A new simpleType model reference
     * @throws InvalidModelException if the supplied simpleType name is not defined in the
     * current WSDL document
     */
    public SimpleTypeModelRef createSimpleTypeModelRef(QName type, IRI target)
    throws InvalidModelException;

    /**
     * Creates and includes in this grounding object a single model reference from an 
     * XML Schema element to a concept in a semantic model.
     * 
     * @param element the name of the element to be annotated
     * @param target the semantic model id reference 
     * @return A new element model reference
     * @throws InvalidModelException if the supplied element name is not defined in the
     * current WSDL document
     */
    public ElementModelRef createElementModelRef(QName element, IRI target)
    throws InvalidModelException;

    /**
     * Creates and includes in this grounding object a single model reference from a 
     * WSDL interface to a concept in a semantic model. This reference plays role of a
     * classification property.
     * 
     * @param iface the name of the interface to be annotated
     * @param target the semantic model id reference 
     * @return A new interface category reference
     * @throws InvalidModelException if the supplied interface name is not defined in the
     * current WSDL document
     */
    public InterfaceCategory createInterfaceCategory(QName iface, IRI target)
    throws InvalidModelException;

    /**
     * Creates and includes in this grounding object a single model reference from a 
     * WSDL operation to a concept in a semantic model. 
     * 
     * @param operation the name of the operation to be annotated
     * @param target the semantic model id reference 
     * @return A new operation model reference
     * @throws InvalidModelException if the supplied operation name is not defined in the
     * current WSDL document
     */
    public OperationModelRef createOperationModelRef(QName operation, IRI target)
    throws InvalidModelException;

    /**
     * Creates and includes in this grounding object a single model reference from a 
     * WSDL operation fault to a concept in a semantic model. 
     * 
     * @param fault the name of the operation fault to be annotated
     * @param target the semantic model id reference 
     * @return A new fault model reference
     * @throws InvalidModelException if the supplied operation fault name is not defined in the
     * current WSDL document
     */
    public FaultModelRef createFaultModelRef(QName fault, IRI target)
    throws InvalidModelException;

    /**
     * Removes a single model reference from this grounding object.
     * 
     * @param reference the model reference to be removed
     * @throws InvalidModelException if the supplied reference does not belong to this grounding
     * object
     */
    public void removeModelRef(ModelRef reference)
    throws InvalidModelException;;

    /**
     * Lists all (regardless of specific type) model references in this grounding object.
     * Only the declared model references are listed, not the propagated ones.
     * 
     * @return A list of model reference objects
     */
    public List<ModelRef> listDeclaredModelRefs();

    /**
     * Lists all model references for the specified WSDL or XML Schema entity in this grounding
     * object.
     * Only the declared model references are listed, not the propagated ones.
     * @param source the name of the WSDL or XML Schema entity
     * @return A list of model reference objects
     * @throws InvalidModelException if the supplied entity name is not defined in the
     * current WSDL document
     */
    public List<ModelRef> listDeclaredModelRefs(QName source)  throws InvalidModelException;

    /**
     * Lists all (regardless of specific type) model references in this grounding object, including the propagated ones.
     * 
     * @return A list of model reference objects
     * @throws InvalidModelException if the model is somehow inconsistent
     */
    public List<ModelRef> listModelRefs() throws InvalidModelException;

    /**
     * Lists all model references for the specified WSDL or XML Schema entity in this grounding
     * object, including the propagated ones.
     * @param source the name of the WSDL or XML Schema entity
     * @return A list of model reference objects
     * @throws InvalidModelException if the supplied entity name is not defined in the
     * current WSDL document
     */
    public List<ModelRef> listModelRefs(QName source)  throws InvalidModelException;

    /**
     * Creates and includes in this grounding object a mapping to a transformation schema responsible
     * for the lifting of the source entity to the corresponding semantic model entity.
     *  
     * @param source the name of the XML Schema entity
     * @param schemaRef the identifier of the lifting schema
     * @return A new lifting schema mapping
     * @throws InvalidModelException if the supplied entity name is not defined in the
     * current WSDL document
     */
    public LiftingSchemaMapping createLiftingSchemaMapping(QName source, URI schemaRef)
    throws InvalidModelException;

    /**
     * Creates and includes in this grounding object a mapping to a transformation schema responsible
     * for the lowering to the source entity from the corresponding semantic model entity.
     *  
     * @param source the name of the XML Schema entity
     * @param schemaRef the identifier of the lowering schema
     * @return A new lowering schema mapping
     * @throws InvalidModelException if the supplied entity name is not defined in the
     * current WSDL document
     */
    public LoweringSchemaMapping createLoweringSchemaMapping(QName source, URI schemaRef)
    throws InvalidModelException;

    /**
     * Removes a single lifting or lowering schema mapping from this grounding object. 
     * @param mapping the mapping object to be removed
     * @throws InvalidModelException if the mapping object does not belong to this grounding
     * object
     */
    public void removeSchemaMapping(SchemaMapping mapping)
    throws InvalidModelException;

    /**
     * Lists all lifting and lowering schema mappings of this grounding object.
     * Only the declared schema mappings are listed, not the propagated ones.
     * @return A list of schema mappings
     */
    public List<SchemaMapping> listDeclaredSchemaMappings();

    /**
     * Lists all lifting and lowering schema mappings for the source entity belonging to this
     * grounding object.
     * Only the declared schema mappings are listed, not the propagated ones.
     * @return A list of schema mappings
     */
    public List<SchemaMapping> listDeclaredSchemaMappings(QName source)  throws InvalidModelException;

    /**
     * Lists all lifting and lowering schema mappings of this grounding object, including the propagated ones.
     * @return A list of schema mappings
     * @throws InvalidModelException if the model is somehow inconsistent
     */
    public List<SchemaMapping> listSchemaMappings() throws InvalidModelException;

    /**
     * Lists all lifting and lowering schema mappings for the source entity belonging to this
     * grounding object, including the propagated ones.
     * @return A list of schema mappings
     */
    public List<SchemaMapping> listSchemaMappings(QName source)  throws InvalidModelException;

    /**
     * Registers a grounding change listener to be notified when new model references and
     * schema mappings are created or removed from this grounding object. 
     * @param listener
     */
    public void addGroundingChangeListener(GroundingChangeEventListener listener);

    /**
     * Unregisters a grounding change listener. 
     * @param listener
     */
    public void removeGroundingChangeListener(GroundingChangeEventListener listener);

}

/*
 * $Log: Grounding.java,v $
 * Revision 1.6  2007/06/18 15:30:51  alex_simov
 * annotation propagation added (on behalf of Jacek)
 *
 * Revision 1.5  2007/04/27 17:46:31  alex_simov
 * javadoc added
 *
 * Revision 1.4  2007/04/27 13:34:07  alex_simov
 * bugfix
 *
 * Revision 1.3  2007/04/26 15:47:13  alex_simov
 * methods might throw InvalidModelExceptions
 *
 * Revision 1.2  2007/04/24 15:32:45  alex_simov
 * imports fix
 *
 * Revision 1.1  2007/04/24 14:09:44  alex_simov
 * new SA-WSDL api
 *
 */
