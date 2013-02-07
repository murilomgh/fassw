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
package org.deri.wsmo4j.logicalexpression.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.omwg.logicalexpression.LogicalExpression;
import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.*;
import org.wsmo.common.Identifier;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.common.exception.SynchronisationException;


/**
 * Utility class to extract concepts, instances, relations and attributes 
 * from a given ontology.
 * 
 * @author Martin Tanler, DERI Innsbruck
 * @author Nathalie Steinmetz, DERI Innsbruck
 */
public class OntologyUtil { 
	
	private static ItemCollectHelper helper = new ItemCollectHelper();

	private static List <Term> conceptList = new ArrayList <Term> (15);
	
	private static List <Term> instanceList = new ArrayList <Term> (15);
	
	private static List <Term> relationList = new ArrayList <Term> (15);
	
	private static List <Term> attributeList = new ArrayList <Term> (15);
	
	private static Map attributeMap = new HashMap();
	
	/**
	 * Collecting all concepts from a given ontology.
	 * 
	 * @param o Ontology to be checked for contained concepts
	 * @return List of terms, identifying all concepts from the ontology
	 * @throws InvalidModelException 
	 * @throws SynchronisationException 
	 */
	public static List getConcepts(Ontology o) 
			throws SynchronisationException, InvalidModelException{
		conceptList = new ArrayList <Term> (15);
		collectConcepts(o);
		collectInstances(o);
		checkLogicalExpressions(o);
		return conceptList;
	}
	
	/**
	 * Collecting all relations from a given ontology.
	 * 
	 * @param o Ontolgoy to be checked for contained relations
	 * @return List of terms, identifying all relations from the ontology
	 * @throws InvalidModelException 
	 * @throws SynchronisationException 
	 */
	public static List getRelations(Ontology o) 
			throws SynchronisationException, InvalidModelException{
		relationList = new ArrayList <Term> (15);
		collectRelations(o);
		checkLogicalExpressions(o);
		return relationList;
	}

	/**
	 * Collecting all attributes from a given concept.
	 * 
	 * @param c Identifier of the concept to be checked for belonging attributes.
	 * @param o Ontology to be checked for concepts and their attributes.
	 * @return List of terms, identifying all Attributes belonging to 
	 * 		  a concept
	 * @throws InvalidModelException 
	 * @throws SynchronisationException 
	 */
	public static List getAttributes(Term c, Ontology o) 
			throws SynchronisationException, InvalidModelException{
		attributeList = new ArrayList <Term> (15);
		collectConceptAttributes(c, o);
		checkLogicalExpressions(o);
		if (!attributeMap.isEmpty()) {
			Set entrySet = attributeMap.entrySet();
			Iterator itSet = entrySet.iterator();
			while (itSet.hasNext()) {
				Entry entry = (Entry) itSet.next();
				Term conId = (Term) entry.getKey();
				Term attId = (Term) entry.getValue();
				if (conId.equals(c)) {
					if (!attributeList.contains(attId)) {
						attributeList.add(attId);
					}
				}
			}
		}
		return attributeList;
	}	
	
	/**
	 * Collecting all attributes from a given concept. It is preferebly to 
	 * use the method getAttributes(Term c, Ontology o) for collecting 
	 * the attributes from a concept, as that method is more complete in 
	 * regard to implicitly defined concepts.
	 * 
	 * @param c Concept to be checked for belonging attributes.
	 * @return List of terms, identifying all Attributes belonging to 
	 * 		  a concept
	 * @throws InvalidModelException 
	 * @throws SynchronisationException 
	 * @see #getAttributes(Term, Ontology)
	 */
	public static List getAttributes(Concept c) 
			throws SynchronisationException, InvalidModelException{
		attributeList = new ArrayList <Term> (15);
		collectConceptAttributes(c);
		checkLogicalExpressions(c.getOntology());
		if (!attributeMap.isEmpty()) {
			Set entrySet = attributeMap.entrySet();
			Iterator itSet = entrySet.iterator();
			while (itSet.hasNext()) {
				Entry entry = (Entry) itSet.next();
				Term conId = (Term) entry.getKey();
				Term attId = (Term) entry.getValue();
				if (conId.equals(c)) {
					if (!attributeList.contains(attId)) {
						attributeList.add(attId);
					}
				}
			}
		}
		return attributeList;
	}	

	/**
	 * Collecting all instances from a given ontology.
	 * 
	 * @param o Ontology to be checked for contained instances
	 * @return List of terms, identifying all instances from the ontology
	 * @throws InvalidModelException 
	 * @throws SynchronisationException 
	 */
	public static List getInstances(Ontology o) 
			throws SynchronisationException, InvalidModelException{
		instanceList = new ArrayList <Term> (15);
		collectInstances(o);
		checkLogicalExpressions(o);
		return instanceList;
	}
	
	private static void collectConcepts(Ontology o) {
		Iterator it = o.listConcepts().iterator();
		while (it.hasNext()) {
			Concept concept = (Concept) it.next();
			if (!conceptList.contains(concept.getIdentifier()))
				conceptList.add(concept.getIdentifier());
			
	        //checking for SuperConcepts and adding them to the list
			Iterator it2 = concept.listSuperConcepts().iterator();
			while (it2.hasNext()) {
				Concept superConcept = (Concept) it2.next();
				if (!conceptList.contains(superConcept.getIdentifier()))
					conceptList.add(superConcept.getIdentifier());
			}
			
			//checking attribute types and adding them evtl. to the list
			Iterator it3 = concept.listAttributes().iterator();
			while (it3.hasNext()) {
				Attribute attribute = (Attribute) it3.next();
				Iterator it4 = attribute.listTypes().iterator();
				while (it4.hasNext()) {
					Type type = (Type) it4.next();
					if (type instanceof Concept) {
						if (!conceptList.contains(((Concept) type).getIdentifier()))
							conceptList.add(((Concept) type).getIdentifier());
					}
				}
			}
		}
	}
	
	private static void collectConceptAttributes(Term t, Ontology o) {
		Concept concept = null;
		Instance instance = null;
		if (t instanceof Identifier) {
			concept = o.findConcept((Identifier) t);
			instance = o.findInstance((Identifier) t);
		}
		if (concept != null) {
			Iterator it = concept.listAttributes().iterator();
			while (it.hasNext()) {
				Attribute attribute = (Attribute) it.next();
				if (!attributeList.contains(attribute.getIdentifier()))
					attributeList.add(attribute.getIdentifier());
			}
		}
		if (instance != null) {
			Map instanceMap = instance.listAttributeValues();
			Set keySet = instanceMap.keySet();
			Iterator it = keySet.iterator();
			while (it.hasNext()) {
				Identifier attId = (Identifier) it.next();
				if (!attributeList.contains(attId))
					attributeList.add(attId);
			}
		}
	}
	
	private static void collectConceptAttributes(Concept c) {
		Iterator it = c.listAttributes().iterator();
		while (it.hasNext()) {
			Attribute attribute = (Attribute) it.next();
			if (!attributeList.contains(attribute.getIdentifier()))
				attributeList.add(attribute.getIdentifier());
		}
	}
	
	private static void collectInstances(Ontology o) {
		Iterator it = o.listInstances().iterator();
		while (it.hasNext()) {
			Instance instance = (Instance) it.next();
			if (!instanceList.contains(instance.getIdentifier()))
				instanceList.add(instance.getIdentifier());
			
	        //checking for memberOf concepts and adding them to the concept list
			Iterator it2 = instance.listConcepts().iterator();
			while (it2.hasNext()) {
				Concept concept = (Concept) it2.next();
				if (!conceptList.contains(concept.getIdentifier()))
					conceptList.add(concept.getIdentifier());
			}
		}
	}
	
	private static void collectRelations(Ontology o) {
		Iterator it = o.listRelations().iterator();
		while (it.hasNext()) {
			Relation relation = (Relation) it.next();
			if (!relationList.contains(relation.getIdentifier()))
				relationList.add(relation.getIdentifier());
			
	        //checking for SuperConcepts and adding them to the list
			Iterator it2 = relation.listSuperRelations().iterator();
			while (it2.hasNext()) {
				Relation superRelation = (Relation) it2.next();
				if (!relationList.contains(superRelation.getIdentifier()))
					relationList.add(superRelation.getIdentifier());
			}
		}
	}
	
	private static void checkLogicalExpressions(Ontology o) 
			throws SynchronisationException, InvalidModelException {
		Iterator it = o.listAxioms().iterator();
		while (it.hasNext()) {
			Axiom axiom = (Axiom) it.next();
			Iterator itLogExp = axiom.listDefinitions().iterator();
			while (itLogExp.hasNext()) {
				LogicalExpression logExpr = (LogicalExpression) itLogExp.next();
				logExpr.accept(helper);
			}
			// collect concepts from logical expressions
			Iterator itCon = helper.getConceptIds().iterator();
			while (itCon.hasNext()) {
				Term conIdentifier = (Term) itCon.next();
				if (!conceptList.contains(conIdentifier)) {
					conceptList.add(conIdentifier);
				}
			}
			// collect instances from logical expressions
			Iterator itIns = helper.getInstanceIds().iterator();
			while (itIns.hasNext()) {
				Term insIdentifier = (Term) itIns.next();
				if (!instanceList.contains(insIdentifier)) {
					instanceList.add(insIdentifier);
				}
			}
			// collect relations from logical expressions
			Iterator itRel = helper.getRelationIds().iterator();
			while (itRel.hasNext()) {
				Term relIdentifier = (Term) itRel.next();
				if (!relationList.contains(relIdentifier)) {
					relationList.add(relIdentifier);
				}
			}
			// collect attributes from logical expressions
			attributeMap = helper.getAttributeIds();
		}
	}
	
}

