/*
 wsmo4j - a WSMO API and Reference Implementation
 Copyright (c) 2005, University of Innsbruck, Austria
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
package org.deri.wsmo4j.io.parser.rdf;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.omwg.logicalexpression.LogicalExpression;
import org.omwg.ontology.Attribute;
import org.omwg.ontology.Axiom;
import org.omwg.ontology.Concept;
import org.omwg.ontology.DataValue;
import org.omwg.ontology.Instance;
import org.omwg.ontology.Ontology;
import org.omwg.ontology.Parameter;
import org.omwg.ontology.Relation;
import org.omwg.ontology.RelationInstance;
import org.omwg.ontology.Type;
import org.omwg.ontology.WsmlDataType;
import org.openrdf.model.BNode;
import org.openrdf.model.Graph;
import org.openrdf.model.GraphException;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.GraphImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.sesame.sail.StatementIterator;
import org.openrdf.vocabulary.RDF;
import org.openrdf.vocabulary.RDFS;
import org.openrdf.vocabulary.XmlSchema;
import org.wsmo.common.Entity;
import org.wsmo.common.IRI;
import org.wsmo.common.Namespace;
import org.wsmo.common.WSML;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.common.exception.SynchronisationException;
import org.wsmo.factory.DataFactory;
import org.wsmo.factory.LogicalExpressionFactory;
import org.wsmo.factory.WsmoFactory;
import org.wsmo.wsml.ParserException;

/**
* Transformation from native rdf to wsml.
*
* <pre>
*  Created on May 02, 2006
*  Committed by $Author: morcen $
*  $Source: /cvsroot/wsmo4j/wsmo4j/org/deri/wsmo4j/io/parser/rdf/RDFExprParser.java,v $
* </pre>
* 
* @see RDFParserImpl
* @author nathalie.steinmetz@deri.org
* @version $Revision: 1.15 $ $Date: 2007/04/02 12:13:23 $
*/
public class RDFExprParser {
    
    private String DC = "http://purl.org/dc/elements/1.1#";
    private String OWL = "http://www.w3.org/2002/07/owl#";
    private String LABEL = "title";
    private String COMMENT = "description";
    private String RELATION = "relation";
    
    private Set <String> idConcepts	= null;
    private Set <String> idInstances = null;
    private Set <String> idRelations = null;
    
    private WsmoFactory factory                 = null;
    private LogicalExpressionFactory leFactory  = null;
    private DataFactory dataFactory             = null; 
    private RDFParseErrorListener errorListener = null;
    
    private String properties   = null;
    private Ontology ontology   = null;
    private Graph graph2        = null;
    private Namespace defaultNS = null;
    
    public RDFExprParser(WsmoFactory factory, LogicalExpressionFactory leFactory, 
            DataFactory dataFactory, String properties, RDFParseErrorListener errorListener) {
        this.factory = factory;
        this.leFactory = leFactory;
        this.dataFactory = dataFactory;
        this.properties = properties;
        this.errorListener = errorListener;
        this.graph2 = new GraphImpl();
        this.defaultNS = null;
        this.idConcepts = new HashSet <String> ();
        this.idInstances = new HashSet <String> ();
        this.idRelations = new HashSet <String> ();
    }
    
    /**
     * This method takes an rdf graph as input and checks through all 
     * the statements, to transform the rdf input into wsmo objects.
     * 
     * @param graph RDF Graph, containing all the parsed statements.
     * @param namespaces HashMap containing all the namespaces of the RDF file.
     * @throws GraphException
     * @throws SynchronisationException
     * @throws InvalidModelException
     * @throws ParserException 
     */
    public void rdf2Wsml(Graph graph, Map namespaces) 
            throws GraphException, SynchronisationException, InvalidModelException, ParserException {
        
        // create wsmo ontology and set namespaces
        createOntology(graph, namespaces);
        setNamespaces(namespaces);
        
        // check through all statements
        StatementIterator iterator = graph.getStatements();
        while(iterator.hasNext()) {
            Statement statement = iterator.next();
            // if the statement describes a class, a new wsml concept is created
            if ((statement.getPredicate().getURI().equals(RDF.TYPE)) &&
                    (statement.getObject().toString().equals(RDFS.CLASS))){
                visitClass(statement);
                graph2.add(statement);
            }
            // if the statement describes a property, either an attribute is added 
            // to a concept, or a relation is created
            else if ((statement.getPredicate().getURI().equals(RDF.TYPE)) &&
                    (statement.getObject().toString().equals(RDF.PROPERTY))){
                if (properties.equals(RDFParserImpl.RDF_PROP_AS_ATTR)) {
                    visitPropertyAsAttribute(statement); 
                    graph2.add(statement);
                }    
                else if (properties.equals(RDFParserImpl.RDF_PROP_AS_REL)) {
                    visitPropertyAsRelation(statement);
                    graph2.add(statement);
                }
            }
            // if the statement describes an instance, an instance is created
            else if (statement.getPredicate().getURI().equals(RDF.TYPE)) {
                visitInstance(statement);
                graph2.add(statement);
            }
        }
        // a warning is put into the list of warnings for all statements that 
        // have not been transformed to WSML 
        graph.remove(graph2);
        iterator = graph.getStatements();
        while(iterator.hasNext()) {
            Statement stat = iterator.next();
            addWarning("Statement/-s containing the predicate \"" + 
                    stat.getPredicate().getLocalName() +
                    "\" has/have not been transformed to WSML!", stat);
        }
        setWsmlVariant();
    }
    
    /*
     * This method creates a wsml concept from the rdf class.
     * If a class is a subClass of another, the corresponding concept adds 
     * the super class as superConcept.
     * If a class has associated comments or labels, those are added as non 
     * functional properties to the corresponding concept.
     */
    private void visitClass(Statement statement) 
            throws GraphException, SynchronisationException, InvalidModelException {
        // create concept
        Concept concept = createConcept(statement.getSubject().toString(), statement);
        if (concept != null) {
            // check for nfps and superConcepts
            StatementIterator it = statement.getSubject().getSubjectStatements();
            while (it != null && it.hasNext()) {             
                Statement stat = it.next();            
                checkForNFP(concept,stat);
                // add any superConcept to the list of superConcepts of this concept
                if (stat.getPredicate().getURI().equals(RDFS.SUBCLASSOF)) {
                    // create superConcept and add superConcept as superConcept to concept
                    Concept superConcept = createConcept(stat.getObject().toString(), stat);
                    if (superConcept != null) {
                        addSuperConcept(concept, superConcept, stat);
                    }
                    graph2.add(stat);
                }
            }
        }
    }
    
    private void checkForNFP(Entity entity, Statement stat) throws InvalidModelException {
        // add any label, a human readable name of the resource, to the nfps
        if (stat.getPredicate().getURI().equals(RDFS.LABEL)) {
            addNFP(entity, DC + LABEL, stat.getObject().toString());
            graph2.add(stat);
        }
        // add any comment, a human readable description of a resource, to the nfps
        else if (stat.getPredicate().getURI().equals(RDFS.COMMENT)) {
            addNFP(entity, DC + COMMENT, stat.getObject().toString());
            graph2.add(stat);
        }
        // map any rdfs:seeAlso to dc#relation
        else if (stat.getPredicate().getURI().equals(RDFS.SEEALSO)) {
            addNFP(entity, DC + RELATION, stat.getObject().toString());
            graph2.add(stat);
        }
        // map any rdfs:isDefinedBy to dc#relation
        else if (stat.getPredicate().getURI().equals(RDFS.ISDEFINEDBY)) {
            addNFP(entity, DC + RELATION, stat.getObject().toString());
            graph2.add(stat);
        }
    }
    
    /*
     * This method creates concept attributes. If the property has 
     * a domain, this domain is used as concept, whom the attribute 
     * belongs to. Otherwise rdfs:Class is used as domain.
     * The property's range specifies the type of the attribute. If 
     * no range is specified, rdfs:Class is used as range.
     * If a property has a subProperty, an axiom, containing an 
     * implication, is created.
     * If a property has associated comments and labels, those are 
     * added to the non functional properties of the corresponding 
     * concept.
     */
    private void visitPropertyAsAttribute(Statement statement) 
            throws GraphException, SynchronisationException, InvalidModelException, ParserException {
        Concept concept = null;
        Attribute attribute = null;
        
        // check for property domain
        StatementIterator it = statement.getSubject().getSubjectStatements();
        while (it != null && it.hasNext()) {
            Statement stat = it.next();
            // the domain specifies the concept, whom the attribute belongs to.
            if (stat.getPredicate().getURI().equals(RDFS.DOMAIN)) {
                concept = createConcept(stat.getObject().toString(), stat);
                graph2.add(stat);
            }         
        }
        // if no domain was specified, rdfs:Class is used instead.
        if (concept == null) {
            concept = createConcept(RDFS.CLASS, null);
        }
        
        // create an attribute
        attribute = createAttribute(concept, statement.getSubject().toString(), statement);
        
        // check for labels or comments
        it = statement.getSubject().getSubjectStatements();
        while (it != null && it.hasNext()) {
            Statement stat = it.next();
            checkForNFP(attribute,stat);
        }
        
        // check for property range
        it = statement.getSubject().getSubjectStatements();
        while (it != null && it.hasNext()) {
            Statement stat = it.next();
            if (stat.getPredicate().getURI().equals(RDFS.RANGE)) {
            	boolean range = false;
                IRI iri = null;
                // check for anonymous ids
                try {
                    iri = factory.createIRI(stat.getObject().toString());
                } catch (IllegalArgumentException e) {
                    addWarning("Anonymous identifiers problem - One or more " +
                            "anonymous concepts have not been added to " +
                            "the WSMO object model!", statement);
                    return;
                }
                iri = factory.createIRI(stat.getObject().toString());
                if (attribute != null) {
                	if (ontology.listConcepts().size() > 0) {
	                    Iterator iter = ontology.listConcepts().iterator();
	                    while (iter.hasNext()) {
	                        Concept conc = (Concept) iter.next();
	                        if (conc.getIdentifier().equals(iri)) {
	                        	range = true;
	                            addAttributeType(attribute, conc);
	                            graph2.add(stat);
	                        }
	                        else if (iri.toString().equals(RDFS.CLASS)) {
	                            conc = createConcept(iri.toString(), null);
	                            range = true;
	                            addAttributeType(attribute, conc);
	                            graph2.add(stat);
	                        }
	                    }
                	}
                	if (!range) {
                		if (iri.toString().equals(RDFS.LITERAL)) {
                            Type t = dataFactory.createWsmlDataType(WsmlDataType.WSML_STRING);
                            addWarning("A property range of type rdfs:Literal is transformed " +
                                    "to WSML_STRING", stat);
                            addAttributeType(attribute, t);   
                            graph2.add(stat);
                        }
                        else if (iri.getNamespace().equals(XmlSchema.NAMESPACE)) {
                            Type t = createWsmlDataType(iri);
                            if (t != null) {
                                addAttributeType(attribute, t);        
                                graph2.add(stat);
                            }
                        }
                        else {
                        	Concept conc = createConcept(iri.toString(), null);
                            addAttributeType(attribute, conc);
                            graph2.add(stat);
                        }
                    }
                }         
            }
        }
        if (attribute.listTypes().size() == 0 && attribute != null) {
            Concept tmp = createConcept(RDFS.CLASS, null);
            addAttributeType(attribute, tmp);
        }
        
        // check for subProperties
        it = statement.getSubject().getSubjectStatements(); 
        while(it != null && it.hasNext()) {
            Statement stat = it.next();
            if (stat.getPredicate().getURI().equals(RDFS.SUBPROPERTYOF)) {
                Attribute b = null;
                String superProperty = stat.getObject().toString();
                if (ontology.listConcepts().size() > 0) {
                    Iterator iter = ontology.listConcepts().iterator();
                    while (iter.hasNext()) {
                        Concept conc = (Concept) iter.next();
                        if (conc.listAttributes().size() > 0) {
                            Iterator iter2 = conc.listAttributes().iterator();
                            while (iter2.hasNext()) {
                                Attribute attr = (Attribute) iter2.next();
                                if (attr.getIdentifier().toString().equals(superProperty)) {
                                    b = attr;
                                }
                            }
                        }
                    }
                }
                if (b == null) {
                    Concept conc = createConcept(RDFS.CLASS, null);
                    b = createAttribute(conc, superProperty, null);
                }
                // create axiom, defined by an implication and add nfps to axiom and concept
                Axiom axiom = createAxiom(attribute, b);
                addNFP(axiom, DC + RELATION, ((IRI) concept.getIdentifier()).getLocalName());
                addNFP(concept, DC + RELATION, ((IRI) axiom.getIdentifier()).getLocalName());
            
                graph2.add(stat);
            }
        }
    }
    
    /*
     * This method creates relations. The property's domain and range 
     * specify the types of the relation. If no domain or range is 
     * specified, rdfs:Class is used instead.
     * If a property is a subProperty of another, a subRelation is 
     * created.
     * If a property has associated comments and labels, those are 
     * added as non functional properties to the corresponding relation.
     */
    private void visitPropertyAsRelation(Statement statement) 
            throws GraphException, SynchronisationException, InvalidModelException {
        Concept concept = null;
        Relation relation = null;
        
        // create a relation
        relation = createRelation(statement.getSubject().toString(), statement);
        
        if (relation != null) {
            // check for labels or comments
            StatementIterator it = statement.getSubject().getSubjectStatements();
            while (it != null && it.hasNext()) {
                Statement stat = it.next();
                checkForNFP(relation,stat);
            }
            
            // check for property domain
            it = statement.getSubject().getSubjectStatements();
            while (it != null && it.hasNext()) {
                Statement stat = it.next();
                // the domain specifies one type of the relation.
                if (stat.getPredicate().getURI().equals(RDFS.DOMAIN)) {
                    concept = createConcept(stat.getObject().toString(), stat);
                    graph2.add(stat);
                }         
            }
            // if no domain was specified, rdfs:Class is used instead.
            if (concept == null) {
                concept = createConcept(RDFS.CLASS, null);
            }
            
            // check for property range
            it = statement.getSubject().getSubjectStatements();
            while (it != null && it.hasNext()) {
                Statement stat = it.next();
                if (stat.getPredicate().getURI().equals(RDFS.RANGE)) {
                	boolean range = false;
                    IRI iri = factory.createIRI(stat.getObject().toString());
                    String localName = iri.getLocalName();
                    if (ontology.listConcepts().size() > 0) {
                        Iterator iter = ontology.listConcepts().iterator();
                        while (iter.hasNext()) {
                            Concept conc = (Concept) iter.next();
                            if (conc.getIdentifier().equals(iri)) {
                            	range = true;
                                addRelationType(relation, concept, conc);
                                graph2.add(stat);
                            }
                            else if (iri.toString().equals(RDFS.CLASS)) {
                            	range = true;
                                conc = createConcept(iri.toString(), stat);
                                addRelationType(relation, concept, conc);
                                graph2.add(stat);
                            }
                        }
                    }
                    if (!range) {
                    	if (localName.endsWith("Literal")) {
                    		Type t = dataFactory.createWsmlDataType(WsmlDataType.WSML_STRING);
                    		addWarning("A property range of type rdfs:Literal is transformed " +
                    				"to WSML_STRING", stat);
                    		addRelationType(relation, concept, t);
                    		graph2.add(stat);
                    	}
                    	else if (iri.getNamespace().equals(XmlSchema.NAMESPACE)) {
                    		Type t = createWsmlDataType(iri);
                    		if (t != null) {
                    			addRelationType(relation, concept, t);   
                    			graph2.add(stat);
                    		}
                    	}
                    	else {
                    		Concept conc = createConcept(iri.toString(), stat);
                            addRelationType(relation, concept, conc);
                            graph2.add(stat);
                    	}
                    }         
                }
            }
            if (relation.listParameters().get(0).listTypes().size() == 0) {
                Concept tmp = createConcept(RDFS.CLASS, null);
                addRelationType(relation, concept, tmp);  
            }
            // check if a property is subProperty of another property
            it = statement.getSubject().getSubjectStatements(); 
            while(it != null && it.hasNext()) {
                Statement stat = it.next();
                if (stat.getPredicate().getURI().equals(RDFS.SUBPROPERTYOF)) {        
                    Relation superRelation = createRelation(stat.getObject().toString(), stat);
                    if (superRelation != null) {
                        addSuperRelation(relation, superRelation);
                    }
                    graph2.add(stat);
                }
            }
        }
    }
    
    /*
     * This method creates instances. 
     */
    private void visitInstance(Statement statement) 
            throws SynchronisationException, InvalidModelException, GraphException {
        Concept concept = null;
        
        // create an instance
        Instance instance = createInstance(statement.getSubject().toString(), statement);
        
        if (instance != null) {
            // add an instance to a concept
            Iterator iter = ontology.listConcepts().iterator();
            while (iter.hasNext()) {
                Concept tmp = (Concept) iter.next();
                if (tmp.getIdentifier().toString().equals(statement.getObject().toString())) {
                    concept = tmp;
                }
            }
            if (concept == null) {
                concept = createConcept(statement.getObject().toString(), statement);
            }
            concept.addInstance(instance);
            
            // check for labels or comments
            StatementIterator it = statement.getSubject().getSubjectStatements();
            while (it != null && it.hasNext()) {
                Statement stat = it.next();
                checkForNFP(instance,stat);
                if (properties.equals(RDFParserImpl.RDF_PROP_AS_ATTR)) {
                    if (!addAttributeValue(concept, instance, stat) 
                            && nonRDFSProperty(stat.getPredicate().getURI())) {
                        Attribute attribute = createAttribute(concept, stat.getPredicate().toString(), stat);
                        Concept tmp = createConcept(RDFS.CLASS, stat);
                        addAttributeType(attribute, tmp);
                        if (stat.getObject() instanceof URI) {
                            Instance temp = createInstance(stat.getObject().toString(), stat);
                            instance.addAttributeValue(attribute.getIdentifier(), temp);
                        } else if (stat.getObject() instanceof BNode) {
                            String name = "blank:" + stat.getObject().toString();
                            Instance temp = createInstance(name, stat);
                            instance.addAttributeValue(attribute.getIdentifier(), temp);
                        } else {
                            DataValue dataValue = dataFactory.createWsmlString(stat.getObject().toString());
                            instance.addAttributeValue(attribute.getIdentifier(), dataValue);
                        }
                    }
                    graph2.add(stat);
                } else if (properties.equals(RDFParserImpl.RDF_PROP_AS_REL)) {
                    if (nonRDFSProperty(stat.getPredicate().getURI())) {
                        addRelationInstances(concept, instance, stat);
                        graph2.add(stat);
                    }
                }
                
            }
        }
    }
    
    private boolean nonRDFSProperty(String uri) {
        return !uri.equals(RDF.TYPE) 
            && !uri.equals(RDFS.LABEL)
            && !uri.equals(RDFS.COMMENT)
            && !uri.equals(RDFS.SEEALSO);
    }
    
    /*
     * This method adds attribute values to instances
     */
    private boolean addAttributeValue(Concept concept, Instance instance, Statement statement) 
            throws SynchronisationException, InvalidModelException {
        Iterator iter = concept.listAttributes().iterator();
        while (iter.hasNext()) {
            Attribute attribute = (Attribute) iter.next();
            if (statement.getPredicate().toString().equals(attribute.getIdentifier().toString())) {
                Instance tmp = getInstance(statement.getObject());
                if (tmp == null) {
                    if (statement.getObject() instanceof URI) {
                        tmp = createInstance(statement.getObject().toString(), statement);
                        instance.addAttributeValue(attribute.getIdentifier(), tmp);
                    } else if (statement.getObject() instanceof BNode) {
                        String name = "blank:" + statement.getObject().toString();
                        Instance temp = createInstance(name, statement);
                        instance.addAttributeValue(attribute.getIdentifier(), temp);
                    } else {
                        DataValue dataValue = dataFactory.createWsmlString(statement.getObject().toString());
                        instance.addAttributeValue(attribute.getIdentifier(), dataValue);
                    }
                }
                else {
                    instance.addAttributeValue(attribute.getIdentifier(), tmp);
                }
                return true;
            }
        }
        if (concept.listSuperConcepts().size() > 0) {
            Iterator iter2 = concept.listSuperConcepts().iterator();
            while (iter2.hasNext()) {
                Concept tmp = (Concept) iter2.next();
                return addAttributeValue(tmp, instance, statement);
            }
        }
        return false;
    }
    
    /*
     * This method adds a relation instance to a relation
     */
    private void addRelationInstances(Concept concept, Instance instance, Statement statement) 
            throws SynchronisationException, InvalidModelException {
        Relation relation = null;
        RelationInstance relationInstance = null;
        Iterator it = ontology.listRelations().iterator();
        while (it.hasNext()) {
            relation = (Relation) it.next();
            if (relation.getIdentifier().toString().equals(statement.getPredicate().toString())) {
                relationInstance = createRelationInstance(instance, relation, statement);
            }
        }
        if (relationInstance == null) {
            relation = createRelation(statement.getPredicate().toString(), statement);
            if (relation != null) {
                relationInstance = createRelationInstance(instance, relation, statement);
            }
        }
    }
    
    /*
     * Create an wsmo ontology and set the wsml variant.
     */
    private void createOntology(Graph g, Map ns) throws GraphException {
        String ontID = null;
        //if it is OWL/RDF there might be an id:
        Resource r = new URIImpl(OWL+"Ontology");
        StatementIterator i = g.getStatements(null, null, r);
        while (i.hasNext()){
            Statement s = i.next();
            URI pred = s.getPredicate();
            if (pred!=null && pred.getURI().equals(RDF.TYPE)){
                ontID = s.getSubject().toString();
                g.remove(s);
            }
        }
        if (ontID==null){
            //an rdf file in general does not have an ID so just creat some
            ontID = getDefaultNamespace(ns).getIRI() + "GENID" + System.currentTimeMillis();
        }
        ontology = factory.createOntology(factory.createIRI(ontID));
    }
    
    /*
     * This method checks whether metamodelling is used in this ontology. If so, 
     * the resulting WSML Variant is set to WSML-FLIGHT, and if not, the resulting 
     * WSML Variant is set to WSML-CORE.
     */
    private void setWsmlVariant() {
    	Iterator it = idConcepts.iterator();
    	while (it.hasNext()) {
    		String id = (String) it.next();
    		if (idInstances.contains(id) || idRelations.contains(id)) {
    			ontology.setWsmlVariant(org.wsmo.common.WSML.WSML_FLIGHT);
            }
    	}
    	it = idInstances.iterator();
    	while (it.hasNext()) {
    		String id = (String) it.next();
    		if (idConcepts.contains(id) || idRelations.contains(id)) {
    			ontology.setWsmlVariant(org.wsmo.common.WSML.WSML_FLIGHT);
    		}
    	}
    	it = idRelations.iterator();
    	while (it.hasNext()) {
    		String id = (String) it.next();
    		if (idConcepts.contains(id) || idInstances.contains(id)) {
    			ontology.setWsmlVariant(org.wsmo.common.WSML.WSML_FLIGHT);
    		}
    	}
    	if (ontology.getWsmlVariant() == null) {
    		ontology.setWsmlVariant(org.wsmo.common.WSML.WSML_CORE);	
    	}
    }
    
    /*
     * This method adds all the namespaces that are used in the rdf file to 
     * the wsml ontology.
     * Also added are the dublin core and the wsml namespaces. If no rdf and 
     * rdfs namespaces are provided from the rdf file, they are added too.
     * One namespace is set as default namespace to the ontology.
     */
    private void setNamespaces(Map namespaces) {       
        boolean rdf = false;
        boolean rdfs = false;
        
        ontology.addNamespace(
                factory.createNamespace("dc", factory.createIRI(DC)));
        ontology.addNamespace(
                factory.createNamespace("wsml", factory.createIRI(WSML.WSML_NAMESPACE)));
        
        Iterator it = namespaces.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            
            //attention some tools use the old rdf namespace
            if (key.equals("rdf")) {
                if (value.equals(RDF.NAMESPACE)){
                    rdf = true;
                }else{//we have someone using prefix rdf with somehting else :/
                    key = "rdf-old";
                }
            }
            else if (key.equals("rdfs")) {
                if (value.equals(RDFS.NAMESPACE)){
                    rdfs=true;
                }else{
                    key = "rdfs-old";
                }
            }
            
            ontology.addNamespace(
                    factory.createNamespace(key , factory.createIRI(value)));
        }

        ontology.setDefaultNamespace(getDefaultNamespace(namespaces));

        if (!rdf) {
            ontology.addNamespace(
                    factory.createNamespace("rdf", factory.createIRI(RDF.NAMESPACE)));
        }
        if (!rdfs) {
            ontology.addNamespace(
                    factory.createNamespace("rdfs", factory.createIRI(RDFS.NAMESPACE)));
        }
    }
    
    
    private Namespace getDefaultNamespace(Map namespaces){    
        if(this.defaultNS!=null) {
            return defaultNS;
        } else{
            String defaultNsCandidate="http://www.example.org/ontologies/example#";
            String defaultNsPrefix=null;
            Iterator it = namespaces.entrySet().iterator();
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                if (key.equals("")){
                    defaultNS = factory.createNamespace("",factory.createIRI(value));
                }
                //could be improved to guess better a suitable namespace if
                //no default was in input
                if (!key.equals("dc") && !key.equals("owl")
                    && !key.equals("xsd") && !key.equals("part-whole")
                    && !key.equals("rdf") && !key.equals("rdfs")
                    && !key.equals("wsmo") && !key.equals("foaf")) {
                    defaultNsCandidate = value;
                    defaultNsPrefix = key;
                }
            }
            if (defaultNS == null){
                if(defaultNsPrefix!=null) {
                    namespaces.remove(defaultNsPrefix);
                }
                defaultNS = factory.createNamespace("",factory.createIRI(defaultNsCandidate)); 
            }
            return defaultNS;
        }
    }
    /**
     * This method returns the ontology that results from transforming the 
     * rdf statements to a wsmo object model.
     * 
     * @return Ontology, the ontology that results from the parsed rdf file
     */
    public Ontology getOntology() {
        return ontology;
    }
    
    /*
     * Create a wsmo concept and add it to the ontology, if it is not in yet.
     */
    private Concept createConcept(String name, Statement statement) 
            throws SynchronisationException, InvalidModelException {
        Concept concept = null;
        IRI iri = null;
        
        // check for spaces in names
        if (name.indexOf(" ") != -1) {
            name = name.replaceAll(" ", "%20");
            addWarning("No spaces allowed in identifiers! - Spaces in " +
                    "one or more concept identifiers " +
                    "have been replaced by '%20'!", statement);
        } 
        
        // check for anonymous ids
        try {
            iri = factory.createIRI(name);
        } catch (IllegalArgumentException e) {
            addWarning("Anonymous identifiers problem - One or more " +
                    "anonymous concepts have not been added to " +
                    "the WSMO object model!", statement);
            return null;
        }
        if (ontology.findConcept(iri) == null) {
            concept = factory.createConcept(iri);
            ontology.addConcept(concept);
            idConcepts.add(concept.getIdentifier().toString());
            return concept;
        }
        else {
            return ontology.findConcept(iri);
        }
    }
    
    /*
     * Add a SuperConcept to a specified concept.
     */
    private void addSuperConcept(Concept concept, Concept superConcept, Statement statement) 
    throws SynchronisationException, InvalidModelException {
        String localName = ((IRI) superConcept.getIdentifier()).getLocalName();
        if (localName.equals("Resource") && !concept.getIdentifier().toString().equals(RDFS.CLASS)) {
            //superConcept = createConcept(RDFS.CLASS, statement);
        }
        if (!concept.getIdentifier().equals(superConcept.getIdentifier())) {
            concept.addSuperConcept(superConcept);
        }
    }
    
    /*
     * Add non functional properties to a specified entity.
     */
    private void addNFP(Entity entity, String identifier, String value) 
            throws SynchronisationException, InvalidModelException {
        IRI key = factory.createIRI(identifier);
        DataValue dataValue = dataFactory.createWsmlString(value);
        entity.addNFPValue(key, dataValue);
    }
    
    /*
     * Add an attribute to a specified concept.
     */
    private Attribute createAttribute(Concept concept, String name, Statement statement) 
            throws InvalidModelException {
        IRI iri = null;
        
        // check for spaces in names
        if (name.indexOf(" ") != -1) {
            name = name.replaceAll(" ", "%20");
            addWarning("No spaces allowed in identifiers! - Spaces in " +
                    "one or more attribute identifiers " +
                    "have been replaced by '%20'!", statement);
        }  
        
        // check for anonymous ids
        try {
            iri = factory.createIRI(name);
        } catch (IllegalArgumentException e) {
            addWarning("Anonymous identifiers problem - One or more " +
                    "anonymous attributes have not been added to " +
                    "the WSMO object model!", statement);
            return null;
        }
        return concept.createAttribute(iri);
    }
    
    /*
     * Add a type to a specified attribute.
     */
    private void addAttributeType(Attribute attribute, Type type) 
            throws InvalidModelException {
        attribute.addType(type);
    }
    
    /*
     * Create an axiom, containing an implication as logicalexpression, 
     * and add it to the ontology.
     */
    private Axiom createAxiom(Attribute a, Attribute b) 
            throws ParserException, SynchronisationException, InvalidModelException{
        
        Axiom axiom = factory.createAxiom(factory.createIRI(
                defaultNS.getIRI().toString() + 
                ((IRI) a.getIdentifier()).getLocalName() + "_is_" + 
                ((IRI) b.getIdentifier()).getLocalName()));
        
        ontology.addAxiom(axiom);
        String le = "?x[" + ((IRI) a.getIdentifier()).getLocalName() + " hasValue ?y] implies ?x[" + 
                ((IRI) b.getIdentifier()).getLocalName() + " hasValue ?y].";
        LogicalExpression logExpr = leFactory.createLogicalExpression(le, ontology);
        axiom.addDefinition(logExpr);      
        return axiom;
    }
    
    /*
     * Create a releation and add it to the ontology.
     */
    private Relation createRelation(String name, Statement statement) 
            throws SynchronisationException, InvalidModelException {
        IRI iri = null;
        
        // check for spaces in names
        if (name.indexOf(" ") != -1) {
            name = name.replaceAll(" ", "%20");
            addWarning("No spaces allowed in identifiers! - Spaces in " +
                    "one or more relation identifiers " +
                    "have been replaced by '%20'!", statement);
        }  
        
        // check for anonymous ids
        try {
            iri = factory.createIRI(name);
        } catch (IllegalArgumentException e) {
            addWarning("Anonymous identifiers problem - One or more " +
                    "anonymous relations have not been added to " +
                    "the WSMO object model!", statement);
            return null;
        }
        if (ontology.findRelation(iri) == null) {
            Relation relation = factory.createRelation(iri);
            relation.createParameter((byte) 0);
            relation.createParameter((byte) 1);
            ontology.addRelation(relation);
            idRelations.add(relation.getIdentifier().toString());
            return relation;
        }
        else {           
            return ontology.findRelation(iri);
        }       
    }
    
    /*
     * Add a SuperRelation to a specified relation.
     */
    private void addSuperRelation(Relation relation, Relation superRelation) 
            throws SynchronisationException, InvalidModelException {
        relation.addSuperRelation(superRelation);
    }
    
    /*
     * Add a type to a specified relation.
     */
    private void addRelationType(Relation relation, Type type1, Type type2) 
            throws SynchronisationException, InvalidModelException {
        Parameter p1 = relation.listParameters().get(0);
        Parameter p2 = relation.listParameters().get(1);
        p1.addType(type1);
        p2.addType(type2); 
    }
    
    /*
     * Create an instance and add it to the ontology.
     */
    private Instance createInstance(String name, Statement statement) 
            throws SynchronisationException, InvalidModelException {
        IRI iri = null;
        
        // check for spaces in names
        if (name.indexOf(" ") != -1) {
            name = name.replaceAll(" ", "%20");
            addWarning("No spaces allowed in identifiers! - Spaces in " +
                    "one or more instances identifiers " +
                    "have been replaced by '%20'!", statement);
        }  
        
        // check for anonymous ids
        try {
            iri = factory.createIRI(name);
        } catch (IllegalArgumentException e) {
            if(name.startsWith("node")) {
                iri = factory.createIRI("blank:" + name);
            } else {
                addWarning("Anonymous identifiers problem - One or more " +
                        "anonymous instances have not been added to " +
                        "the WSMO object model!", statement);
                return null;
            }
        }
        if (ontology.findInstance(iri) == null) {
            Instance instance = factory.createInstance(iri);
            ontology.addInstance(instance);
            idInstances.add(instance.getIdentifier().toString());
            return instance;
        }
        else {           
            return ontology.findInstance(iri);
        }       
    }
    
    /*
     * Check if the ontology contains a specified instance and return 
     * it. If the instance is not contained, null is returned.
     */
    private Instance getInstance(Value object) {
        IRI iri = null;
        try {
            iri = factory.createIRI(object.toString());
        } catch (IllegalArgumentException e) {
            if(object instanceof URI) {
                iri = factory.createIRI(ontology.getDefaultNamespace().getIRI() + object.toString());
            } else if (object instanceof BNode) {
                iri = factory.createIRI("blank:" + object.toString());
            } else {
                return null;
            }
        }     
        if (ontology.findInstance(iri) != null) {
            return ontology.findInstance(iri);
        }
        else {
            return null;
        }
    }
    
    /*
     * Create a RelationInstance and add it to the ontology. As first 
     * parameter value the instance is added, and as second parameter 
     * value a DataValue from the statement's object value is created 
     * and added.
     */
    private RelationInstance createRelationInstance(Instance instance, Relation relation, Statement statement) 
            throws SynchronisationException, InvalidModelException {  
        RelationInstance relationInstance = factory.createRelationInstance(relation);
        ontology.addRelationInstance(relationInstance);
        Instance tmp = getInstance(statement.getObject());
        if (tmp == null) {
            if (statement.getObject() instanceof URI) {
                tmp = createInstance(statement.getObject().toString(), statement);
                relationInstance.setParameterValue((byte) 1, tmp);
            }
            else {
                DataValue dataValue = dataFactory.createWsmlString(statement.getObject().toString());
                relationInstance.setParameterValue((byte) 1, dataValue);
            }
        }
        else {
            relationInstance.setParameterValue((byte) 1, tmp);
        }
        relationInstance.setParameterValue((byte) 0, instance);
        
        return relationInstance; 
    }
    
    private Type createWsmlDataType(IRI iri) {
        Type t = null;
        if (iri.toString().equals(XmlSchema.STRING)) {
            t = dataFactory.createWsmlDataType(WsmlDataType.WSML_STRING);
        }
        else if (iri.toString().equals(XmlSchema.DECIMAL)) {
            t = dataFactory.createWsmlDataType(WsmlDataType.WSML_DECIMAL);
        }
        else if (iri.toString().equals(XmlSchema.INTEGER)) {
            t = dataFactory.createWsmlDataType(WsmlDataType.WSML_INTEGER);
        }
        else if (iri.toString().equals(XmlSchema.BOOLEAN)) {
            t = dataFactory.createWsmlDataType(WsmlDataType.WSML_BOOLEAN);
        }
        else if (iri.toString().equals(XmlSchema.DATE)) {
            t = dataFactory.createWsmlDataType(WsmlDataType.WSML_DATE);
        }
        else if (iri.toString().equals(XmlSchema.DATETIME)) {
            t = dataFactory.createWsmlDataType(WsmlDataType.WSML_DATETIME);
        }
        else if (iri.toString().equals(XmlSchema.TIME)) {
            t = dataFactory.createWsmlDataType(WsmlDataType.WSML_TIME);
        }
        else if (iri.toString().equals(XmlSchema.ANYURI)) {
            t = dataFactory.createWsmlDataType(WsmlDataType.WSML_IRI);
        }
        return t;
    }
    
    /*
     * This methods sends a warning to the RDFParseErrorListener.
     */
    private void addWarning(String message, Statement statement) {
        errorListener.warning(message, -1, -1, statement);
    }
    
}
/*
 * $Log: RDFExprParser.java,v $
 * Revision 1.15  2007/04/02 12:13:23  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.14  2007/01/11 12:33:48  nathaliest
 * added metamodelling check for detecting correct wsml variant
 *
 * Revision 1.13  2006/11/30 13:49:46  nathaliest
 * *** empty log message ***
 *
 * Revision 1.12  2006/11/30 13:45:17  nathaliest
 * fixed a bug concerning the call of the parse method with a stringbuffer as parameter
 *
 * Revision 1.11  2006/11/17 16:43:04  nathaliest
 * fixed rdf parser to replace spaces by %20
 *
 * Revision 1.10  2006/11/16 13:24:22  ohamano
 * *** empty log message ***
 *
 * Revision 1.9  2006/11/16 09:52:21  ohamano
 * no more internal constant strings
 *
 * Revision 1.8  2006/11/15 17:01:51  ohamano
 * first attempts to more modularization
 *
 * Revision 1.7  2006/11/15 16:25:20  ohamano
 * find a means to "model" blank nodes (bnodes were ignored)
 * disambiguate rdf:type from xxx:type (e.g. xsi:type)
 *
 * Revision 1.6  2006/11/15 14:05:40  ohamano
 * remove explict statement for subconcept of rdfs:class resp rdfs:resource
 * include wsml.datetime
 *
 * Revision 1.5  2006/06/06 11:36:12  nathaliest
 * fixed problem with xml schema ranges
 *
 * Revision 1.4  2006/05/15 07:58:12  holgerlausen
 * corrected issues with namespaces and ontology ids
 *
 * Revision 1.2  2006/05/03 15:30:36  nathaliest
 * *** empty log message ***
 *
 * Revision 1.1  2006/05/03 13:32:49  nathaliest
 * adding RDF parser
 *
 * 
 */
