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

package com.ontotext.wsmo4j.parser.owl;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.*;

import org.deri.wsmo4j.io.serializer.wsml.VisitorSerializeWSMLTerms;
import org.omwg.ontology.*;
import org.openrdf.model.*;
import org.openrdf.model.Value;
import org.openrdf.sesame.Sesame;
import org.openrdf.sesame.admin.StdOutAdminListener;
import org.openrdf.sesame.config.AccessDeniedException;
import org.openrdf.sesame.config.ConfigurationException;
import org.openrdf.sesame.constants.QueryLanguage;
import org.openrdf.sesame.constants.RDFFormat;
import org.openrdf.sesame.query.QueryResultsTable;
import org.openrdf.sesame.repository.local.LocalRepository;
import org.openrdf.sesame.sail.*;
import org.wsmo.common.*;
import org.wsmo.common.Namespace;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.common.exception.SynchronisationException;
import org.wsmo.factory.*;
import org.wsmo.wsml.ParserException;

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation - OWL processing utility class</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */
public class WSMLFromOWL  {
	
	LocalRepository _repository;
	WsmoFactory _factory;
	LogicalExpressionFactory _leFactory;
	DataFactory _dataFactory;
	
	String allOntologiesQuery = "select O from {O} rdf:type {<"+OWLConstants.OWL_Ontology+">}";
	
	String importedOntologyQuery = 
		"select Y from {O} rdf:type {<" + OWLConstants.OWL_Ontology + ">} "+
		",{O} <" + OWLConstants.OWL_imports + "> {Y}";
	
	String classesQuery = "select X from {X} rdf:type {rdfs:Class}";
	
	String mainOntology = 
		"select O from {O} rdf:type {<"+OWLConstants.OWL_Ontology+">} "+
		" ,[ {} P {O} ] where P = null and O != <http://www.w3.org/2002/07/owl>";

	String annotationsQuery = "select X from {X} rdf:type {<"+OWLConstants.OWL_AnnotationProperty+">}";

	String annotationsOntoPropsQuery = "select X from {X} rdf:type {<"+OWLConstants.OWL_OntologyProperty+">}";
	
	String restrictionsQuery = "select X from {X} rdf:type {<"+OWLConstants.OWL_Restriction+">}";
	
	String SEQHeadsQuery = "select X from "+
		"[{} <"+OWLConstants.OWL_distinctMembers+"> {X}], "+
		"[{} <"+OWLConstants.OWL_intersectionOf+"> {X}], "+
		"[{} <"+OWLConstants.OWL_unionOf+"> {X}], "+
		"[{} <"+OWLConstants.OWL_complementOf+"> {X}], "+
		"[{} <"+OWLConstants.OWL_oneOf+"> {X}] ";

	String propertiesQuery = "select X from {X} rdf:type {rdf:Property}";

	IRI dcSameAs = null;	
	public WSMLFromOWL(WsmoFactory _factory, LogicalExpressionFactory _leFactory, DataFactory _dataFactory) {
		if (_factory == null)
			_factory = Factory.createWsmoFactory(null);
		this._factory = _factory;
		// use the default
		if (_leFactory == null)
			_leFactory = Factory.createLogicalExpressionFactory(null);
		this._leFactory = _leFactory;
		
		if (_dataFactory == null)
			_dataFactory = Factory.createDataFactory(null);
		this._dataFactory = _dataFactory;
		
		try {
			_repository = Sesame.getService().createRepository("owl_helper", true);
			RdfSchemaRepository _src =(RdfSchemaRepository)_repository.getSail();
			ValueFactory fact =_src.getValueFactory();
			URI owlCls = fact.createURI(OWLConstants.OWL_Class); 
			URI rdfsCls = fact.createURI(OWLConstants.RDFS_Class); 
			URI rdfsSubCls = fact.createURI(OWLConstants.RDFS_subClassOf); 
			URI owlDataTypeProp = fact.createURI(OWLConstants.OWL_DatatypeProperty);
			URI owlObjectProp = fact.createURI(OWLConstants.OWL_ObjectProperty); 
			URI rdfProp = fact.createURI(OWLConstants.RDF_Property); 
			_src.startTransaction();
			_src.addStatement(owlCls, rdfsSubCls, rdfsCls);
			_src.addStatement(owlObjectProp, rdfsSubCls, rdfProp);
			_src.addStatement(owlDataTypeProp, rdfsSubCls, rdfProp);
			_src.commitTransaction();
			
		} catch (SailUpdateException sue) {
			throw new RuntimeException("bad configuration", sue);
		} catch (ConfigurationException e) {
			// TODO: handle exception
			throw new RuntimeException("bad configuration", e);
		}
	}
	
	public void addData(Reader content, String baseURL) {
		assert baseURL != null;
		StdOutAdminListener listener = new StdOutAdminListener();
		try {
			_repository.addData(content, baseURL, RDFFormat.RDFXML, false, listener);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (AccessDeniedException e2) {
			e2.printStackTrace();
		}
	}
	
	void collectNodesInSEQ(Resource v, Collection here){
		try {
			Resource next = null;
			do {
				if (next != null)
					v = next;
				next = null;
				StatementIterator iter = v.getSubjectStatements();
				while (iter.hasNext()) {
					Statement st = iter.next();
					if (0==st.getPredicate().getURI().compareTo(Constants.RDF_first)) {
						Value val = st.getObject();
						here.add(val);
					}
					else if (0==st.getPredicate().getURI().compareTo(Constants.RDF_rest))
						next = (Resource)st.getObject();
				}
				iter.close();
			} while(next != null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
	void initial_collect() {
		//HashMap theClasses = new HashMap();
		ArrayList theCollection = new ArrayList();
		try {
			QueryResultsTable result = _repository.performTableQuery(QueryLanguage.SERQL, SEQHeadsQuery);
			for (int i = 0; i < result.getRowCount(); i++) {
				theCollection.add(result.getValue(i, 0));

				ArrayList here = new ArrayList();
				collectNodesInSEQ((Resource)result.getValue(i, 0),  here);	
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	String xForm(String val){
		int pos = val.indexOf('#');
		if (pos < 0 || pos == val.length())
			return VisitorSerializeWSMLTerms.escapeNonSQNameSymbols(val);
		//find
		Iterator iter = theOntology.listNamespaces().iterator();
		while (iter.hasNext()) {
			Namespace ns = (Namespace)iter.next();
			if(ns.getIRI().toString().startsWith( val.substring(0,pos))) {
				return VisitorSerializeWSMLTerms.escapeNonSQNameSymbols(
                        ns.getPrefix()+val.substring(pos)); 
			}
		}
		Namespace ns = theOntology.getDefaultNamespace();
		if(ns.getIRI().toString().startsWith( val.substring(0,pos))) {
			return VisitorSerializeWSMLTerms.escapeNonSQNameSymbols(
                    val.substring(pos+1)); 
		}
		return "_\""+val+"\"";
	}
	
	DataValue createDataValueFromLiteral(Literal literal) {
		WsmlDataType type = null;
		URI data = literal.getDatatype();
		if (data != null) {
			try {
				type = _dataFactory.createWsmlDataType(data.toString());
			} catch (IllegalArgumentException iae) {
				return _dataFactory.createWsmlString(literal.getLabel()); 
			}
			try {
				return _dataFactory.createDataValueFromJavaObject(type, literal.getLabel());
			} catch (IllegalArgumentException iae) {
				String lit = literal.getLabel();
				try {
					return _dataFactory.createWsmlInteger(lit);
				} catch (NumberFormatException nfe) {
					try {
						return _dataFactory.createWsmlDecimal(lit);
					} catch (NumberFormatException nfe2) {
						return _dataFactory.createWsmlString(lit);
					}
				}
			}
		} else {
			String lit = literal.getLabel();
			try {
				return _dataFactory.createWsmlInteger(lit);
			} catch (NumberFormatException nfe) {
				try {
					return _dataFactory.createWsmlDecimal(lit);
				} catch (NumberFormatException nfe2) {
					return _dataFactory.createWsmlString(lit);
				}
			}
		}
/*		String foo = '"'+literal.getLabel()+'"';
		if (data != null)
			foo = foo + "^^"+data.toString();
		return WSMLAnalyser.createDataValue(foo);
		*/
	}

	int carinalities = 1;
	
	void WSMLminCardinalityExpr(StringBuffer result, String prop, int card){
		result.append("?x memberOf |class| implies exists ");
		ArrayList distinctVars = new ArrayList();
		if (card > 1) 
			result.append('{');
		for (int i = 0; i < card; i++) {
			String var = "?y"+(i+1);
			distinctVars.add(var);
			if (i > 0)
				result.append(", ");
			result.append(var);
		}
		if (card > 1) 
			result.append('}');
		result.append("( ?x[");

//		result.append("pnew"+(++carinalities)+"(?x) :- ?x memberOf |class| and ?x[");
		for (int i = 0; i < card; i++) {
			if (i > 0)
				result.append(", ");
			result.append(prop + " hasValue "+distinctVars.get(i));
		} 
		result.append("]");
		
		if (card > 1) {
			for (int i = 0; i < distinctVars.size()-1; i++) {
				for (int j = i+1; j < distinctVars.size(); j++) {
					result.append(" and "+distinctVars.get(i)+" != "+distinctVars.get(j));
				}				 
			}
		}				 
		result.append(").\n");
//		result.append(".\n	!- ?x memberOf |class| and naf pnew"+carinalities+"(?x).\n");
	}

	void WSMLmaxCardinalityExpr(StringBuffer result, String prop, int card){
		result.append("?x memberOf |class| implies exists ");
//		result.append("!- ?x memberOf |class| and ?x[");
		ArrayList distinctVars = new ArrayList();
		if (card > 0)
			result.append('{');

		for (int i = 0; i <= card; i++) {
			String var = "?y"+(i+1);
			distinctVars.add(var);
			if (i > 0)
				result.append(", ");
			result.append(var);
		} 
		if (card > 0)
			result.append('}');
		result.append("( ?x[");

		for (int i = 0; i <= card; i++) {
			if (i > 0)
				result.append(", ");
			result.append(prop + " hasValue "+distinctVars.get(i));
		}
		result.append("] ");
		
		boolean bComma= false;
		if (card > 0) {
			result.append("and (");
			for (int i = 0; i < distinctVars.size()-1; i++) {
				for (int j = i+1; j < distinctVars.size(); j++) {
					if (bComma)
						result.append(" or ");
					bComma = true;
					result.append(distinctVars.get(i)+" = "+distinctVars.get(j));
				}				 
			}
			result.append(")");
		}				 
		result.append(" ).\n");
	}
	
	void helperXXXValuesFromBody(StringBuffer result, Value argument) {
		StatementIterator iter2 = null;
		try {
			iter2 = ((Resource)argument).getSubjectStatements();
			
			boolean anRestrictionAsargument = false;
			URI restrictionOnProperty = null;
			URI restrictionKind = null;
			Value restrictionKindValue = null;
			
			while (iter2.hasNext()) {
				Statement st = iter2.next();
				String pred = st.getPredicate().getURI();
				if (0==pred.compareTo(OWLConstants.OWL_intersectionOf)) {
					//intersection
					handleSetClass((Resource)argument);
					ArrayList list = new ArrayList();
					collectNodesInSEQ((Resource)st.getObject(), list);
					boolean bFirst = true;
					for (Iterator iter = list.iterator(); iter.hasNext();) {
						Resource r = (Resource)iter.next();
						if (bFirst == false)
							result.append(" and ");
						bFirst = false;
						if (r instanceof URI) {
							result.append("?y1 memberOf "+xForm(((URI)r).getURI()));
						}
					} 
					break;
				} else if (0==pred.compareTo(OWLConstants.OWL_unionOf)) {
					//union
					handleSetClass((Resource)argument);
					ArrayList list = new ArrayList();
					collectNodesInSEQ((Resource)st.getObject(), list);
					boolean bFirst = true;
					for (Iterator iter = list.iterator(); iter.hasNext();) {
						Resource r = (Resource)iter.next();
						if (bFirst == false)
							result.append(" or ");
						bFirst = false;
						if (r instanceof URI) {
							result.append("?y1 memberOf "+xForm(((URI)r).getURI()));
						}
					} 
					break;
				} else if (0==pred.compareTo(OWLConstants.OWL_oneOf)) {
					//oneOf
					handleSetClass((Resource)argument);
					ArrayList list = new ArrayList();
					collectNodesInSEQ((Resource)st.getObject(), list);
					boolean bFirst = true;
					for (Iterator iter = list.iterator(); iter.hasNext();) {
						Resource r = (Resource)iter.next();
						if (bFirst == false)
							result.append(" or ");
						bFirst = false;
						if (r instanceof URI) {
							result.append("?y1 = "+xForm(((URI)r).getURI()));
						}
					} 
					break;
				} if (0==pred.compareTo(OWLConstants.OWL_complementOf)) {
					// complemnt
					handleSetClass((Resource)argument);
					result.append("not ?y1 memberOf "+xForm(((URI)st.getObject()).getURI()));
					break;
				} if (0==pred.compareTo(OWLConstants.OWL_onProperty)) {
					restrictionOnProperty = (URI)st.getObject();
				} if (0==pred.compareTo(OWLConstants.OWL_hasValue)) {
					restrictionKind = (URI)st.getPredicate();
					restrictionKindValue = (URI)st.getObject();
				} if (0==pred.compareTo(OWLConstants.OWL_allValuesFrom)) {
					restrictionKind = (URI)st.getPredicate();
					restrictionKindValue = st.getObject();						
				} else if (0==pred.compareTo(Constants.RDF_type) &&
						0==OWLConstants.OWL_Restriction.compareTo(st.getObject().toString())) {
					anRestrictionAsargument = true;
				}
			} // while statements
			if (anRestrictionAsargument) {
				if (0==restrictionKind.getLocalName().compareTo("hasValue")) {
					String value = null;
					if (restrictionKindValue instanceof URI) {
						value = xForm(((URI)restrictionKindValue).getURI()); 
					} else if (restrictionKindValue instanceof Literal) {
						Literal literal = (Literal)restrictionKindValue;
						value = createDataValueFromLiteral(literal).toString();
					}
					result.append("?y1["+xForm(restrictionOnProperty.getURI())+" hasValue "+value+"]");
				} else if (0==restrictionKind.getLocalName().compareTo("allValuesFrom")) {
					StatementIterator collFind = null;
					ArrayList enumeratedClass = new ArrayList(); 
					try {
						collFind = ((Resource)restrictionKindValue).getSubjectStatements();
						while (collFind.hasNext()) {
							Statement st = collFind.next();
							if (0==st.getPredicate().getLocalName().compareTo("oneOf")) {
								collectNodesInSEQ((Resource)st.getObject(), enumeratedClass);
								break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						collFind.close();
					}
					boolean bcomma = false;
					for (Iterator iterEnum = enumeratedClass.iterator(); iterEnum.hasNext();){
						if (bcomma)
							result.append(" or ");
						Value inst = (Value)iterEnum.next();
						String value = null;
						if (inst instanceof URI) {
							value = xForm(((URI)inst).getURI()); 
						} else if (inst instanceof Literal) {
							Literal literal = (Literal)inst;
							value = createDataValueFromLiteral(literal).toString();
						}

						result.append ("?y1["+xForm(restrictionOnProperty.getURI())+ " hasValue "+value+"]");
						bcomma = true;
					}
					
				} else {
					throw new RuntimeException("restrictionKind "+restrictionKind.getURI()+"not supported!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			iter2.close();
		}
	}
	void WSMLRestrictionBuilder(StringBuffer result, Resource restriction, URI onProperty, String kind, Value argument) {
		if (0 == kind.compareTo("cardinality")) {
			if (!(argument instanceof Literal))
				return ; 
			Literal arg = (Literal)argument;
			String value = arg.getLabel();
			int card = 0;
			try {
				card = Integer.parseInt(value);
			} catch (NumberFormatException e) {
				//@todo: better error handling: bad value as cardinality passed
				return ; 
			}
			if (card <= 0) {
				//@todo: better error handling: bad value as cardinality passed
				return;
			}
			String prop = xForm(onProperty.getURI());
			WSMLminCardinalityExpr(result, prop, card);
			WSMLmaxCardinalityExpr(result, prop, card);
		} else if (0 == kind.compareTo("minCardinality")) {
			if (!(argument instanceof Literal))
				return ; 
			Literal arg = (Literal)argument;
			String value = arg.getLabel();
			int card = 0;
			try {
				card = Integer.parseInt(value);
			} catch (NumberFormatException e) {
				//@todo: better error handling: bad value as cardinality passed
				return ; 
			}
			String prop = xForm(onProperty.getURI());
			WSMLminCardinalityExpr(result, prop, card);
		} else if (0 == kind.compareTo("maxCardinality")) {
			if (!(argument instanceof Literal))
				return ; 
			Literal arg = (Literal)argument;
			String value = arg.getLabel();
			int card = 0;
			try {
				card = Integer.parseInt(value);
			} catch (NumberFormatException e) {
				//@todo: better error handling: bad value as cardinality passed
				return ; 
			}
			String prop = xForm(onProperty.getURI());
			WSMLmaxCardinalityExpr(result, prop, card);
		} else if (0 == kind.compareTo("hasValue")) {
			String value = null;
			if (argument instanceof URI) {
				value = xForm(((URI)argument).getURI()); 
			} else if (argument instanceof Literal) {
				Literal literal = (Literal)argument;
				value = createDataValueFromLiteral(literal).toString();
			}
			result.append("?x memberOf |class| implies ?x["+xForm(onProperty.getURI())+" hasValue "+value+"].\n");
		} else if(0 == kind.compareTo("someValuesFrom")) {
			result.append("?x memberOf |class| implies exists ?y1 (?x["+xForm(onProperty.getURI())+" hasValue ?y1] and (");
			if (argument instanceof URI)
				result.append("?y1 memberOf "+xForm(((URI)argument).getURI()));
			else {
				helperXXXValuesFromBody(result, argument);
			}
			result.append(") ).\n");
		} else if (0 == kind.compareTo("allValuesFrom")) {
			
			result.append("?x memberOf |class| implies ?x["+xForm(onProperty.getURI())+" hasValue ?y1] implies (");
			//?lhs(C, xnew) "
			if (argument instanceof URI)
				result.append("?y1 memberOf "+xForm(((URI)argument).getURI())+").\n");
			else {
				helperXXXValuesFromBody(result, argument);
				result.append(" ).\n");
				
			} // else
			
		}	
	}
	
	HashMap allRestrictions;
	
	void handleRestrictionOnProperty() {
		allRestrictions = new HashMap();
		String query = "select R, P, card, minc, maxc, hasv, some, allv  from "+
			"{R} rdf:type {<"+OWLConstants.OWL_Restriction+">} "+
			", {R} <"+OWLConstants.OWL_onProperty+"> {P} "+
			",[ {R} <"+OWLConstants.OWL_cardinality+"> {card} ] "+
			",[ {R} <"+OWLConstants.OWL_minCardinality+"> {minc} ] "+
			",[ {R} <"+OWLConstants.OWL_maxCardinality+"> {maxc} ] "+
			",[ {R} <"+OWLConstants.OWL_hasValue+"> {hasv} ] "+
			",[ {R} <"+OWLConstants.OWL_someValuesFrom+"> {some} ]"+
			",[ {R} <"+OWLConstants.OWL_allValuesFrom+"> {allv} ] "
			;
		try {
			QueryResultsTable result = _repository.performTableQuery(QueryLanguage.SERQL, query);
			for (int i = 0; i < result.getRowCount(); i++) {
				Resource restr = (Resource)result.getValue(i, 0);
				StringBuffer out = new StringBuffer();
				
				if (null != result.getValue(i, 2)) {
					WSMLRestrictionBuilder(out, restr, (URI)result.getValue(i, 1), "cardinality", result.getValue(i, 2));
				}
				
				if (null != result.getValue(i, 3)) {
					WSMLRestrictionBuilder(out, restr, (URI)result.getValue(i, 1), "minCardinality", result.getValue(i, 3));
				}
				
				if (null != result.getValue(i, 4)) {
					WSMLRestrictionBuilder(out, restr, (URI)result.getValue(i, 1), "maxCardinality", result.getValue(i, 4));
				}
				
				if (null != result.getValue(i, 5)){
					WSMLRestrictionBuilder(out, restr, (URI)result.getValue(i, 1), "hasValue", result.getValue(i, 5));
				}
				
				if (null != result.getValue(i, 6)) {
					WSMLRestrictionBuilder(out, restr, (URI)result.getValue(i, 1), "someValuesFrom", (Resource)result.getValue(i, 6));
				}
				
				if (null != result.getValue(i, 7)) {
					WSMLRestrictionBuilder(out, restr, (URI)result.getValue(i, 1), "allValuesFrom", (Resource)result.getValue(i, 7));

				}
				
				if (out.length() == 0) {
					continue;
				}
				
				allClasses.remove(restr);
				allRestrictions.put(restr, out.toString());
			}
			result = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	HashMap defaultClasses = new HashMap();
	HashMap defaultProperties = new HashMap();
	
	void collectDefaultClasses() {
		defaultClasses.clear();
		try {
			QueryResultsTable result = _repository.performTableQuery(QueryLanguage.SERQL, classesQuery);
			for (int i = 0; i < result.getRowCount(); i++) {
				defaultClasses.put(result.getValue(i, 0), result.getValue(i, 0));
			}
			result = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void collectDefaultProperties() {
		defaultProperties.clear();
		try {
			QueryResultsTable result = _repository.performTableQuery(QueryLanguage.SERQL, propertiesQuery);
			for (int i = 0; i < result.getRowCount(); i++) {
				defaultProperties.put(result.getValue(i, 0), result.getValue(i, 0));
			}
			result = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	HashMap setClasses = new HashMap();
	void handleSetClass(Resource node) {
		setClasses.put(node, node);
		allClasses.remove(node);
	} 
	
	HashMap allClasses;
	
	void handleClasses(){
		allClasses = new HashMap();
		try {
			QueryResultsTable result = _repository.performTableQuery(QueryLanguage.SERQL, classesQuery);
			for (int i = 0; i < result.getRowCount(); i++) {
			Value v = result.getValue(i, 0);
				if (null == defaultClasses.get(v))
					allClasses.put(v,v);
			}
			result = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	HashMap allProperties;

	void handleProperties(){
		allProperties = new HashMap();
		try {
			QueryResultsTable result = _repository.performTableQuery(QueryLanguage.SERQL, propertiesQuery);
			for (int i = 0; i < result.getRowCount(); i++) {
			Value v = result.getValue(i, 0);
				if (null == defaultProperties.get(v))
					allProperties.put(v,v);
			}
			result = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	HashMap allInstances;
	
	void handleInstances() {
		allInstances = new HashMap();
		
		String query = "select distinct I from {I} rdf:type {C} "+
		" , [{I} rdfs:subClassOf {Q} ] "+
		" , [{I} rdfs:subPropertyOf {P}] "+
		" , [{I} L {rdf:List} ]"+
		" , [{I} R {<"+OWLConstants.OWL_Restriction+">} ]"+
		" , [{I} <"+OWLConstants.OWL_distinctMembers+"> {F} ]"+
		" , [{I} O {<"+OWLConstants.OWL_Ontology+">} ]"+
		" , [{DEF} rdfs:isDefinedBy {I} ]"+
		" where Q = NULL AND P = NULL and L = NULL and R = NULL and F = NULL and O = NULL and DEF = NULL"
		;
		
		try {
			QueryResultsTable result = _repository.performTableQuery(QueryLanguage.SERQL, query);
			for (int i = 0; i < result.getRowCount(); i++) {
				Value v = result.getValue(i, 0);
				allInstances.put(v,v);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	boolean isExternalClass(Resource cls) {
		RdfSchemaSource _src =(RdfSchemaSource)_repository.getSail();
		ValueFactory fact =_src.getValueFactory();
		URI typeP = fact.createURI(OWLConstants.RDF_type); 
		URI owlCls = fact.createURI(OWLConstants.OWL_Class.toString()); 
		return !_src.hasExplicitStatement(cls, typeP, owlCls);
	}

	boolean isExternalIndividual(Resource cls) {
		RdfSchemaSource _src =(RdfSchemaSource)_repository.getSail();
		ValueFactory fact =_src.getValueFactory();
		URI typeP = fact.createURI(OWLConstants.RDF_type); 
//		URI owlCls = fact.createURI(OWLConstants.OWL_Class.toString()); 
		return !_src.hasExplicitStatement(cls, typeP, null);
	}
	
	boolean isInferred(Resource s, URI pred, Value v) {
		RdfSchemaSource _src =(RdfSchemaSource)_repository.getSail();
		return !_src.hasExplicitStatement(s, pred, v);
	}

	boolean isExternalProperty(Resource cls) {
		RdfSchemaSource _src =(RdfSchemaSource)_repository.getSail();
		ValueFactory fact =_src.getValueFactory();
		URI typeP = fact.createURI(OWLConstants.RDF_type); 
		URI owlObjectProp = fact.createURI(OWLConstants.OWL_ObjectProperty); 
		URI owlDataProp = fact.createURI(OWLConstants.OWL_DatatypeProperty); 
		URI rdfProperty = fact.createURI(OWLConstants.RDF_Property);
		if (_src.hasExplicitStatement(cls, typeP, owlObjectProp))
			return false; 
		if (_src.hasExplicitStatement(cls, typeP, owlDataProp))
			return false; 
		if (_src.hasExplicitStatement(cls, typeP, rdfProperty)) 
			return false; 
		return true;		
	}
	
	ArrayList getSameAs(Resource res) {
		ArrayList result = new ArrayList();
		RdfSchemaSource _src =(RdfSchemaSource)_repository.getSail();
		ValueFactory fact =_src.getValueFactory();
		URI sameAsP = fact.createURI(OWLConstants.OWL_sameAs); 
		StatementIterator iter = null;
		try {
			iter = _src.getStatements(res, sameAsP, null);
			while (iter.hasNext()) {
				result.add(iter.next().getObject());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			iter.close();
		}
		return result;
	}
	
	void handleRestrictionSet() {
		String query = "select R from"+
			"{R} rdf:type {<"+OWLConstants.OWL_Restriction+">} "+
			", [{R} <"+OWLConstants.OWL_onProperty+"> {P} ] "+
			"where P = NULL";
		try {
			QueryResultsTable result = _repository.performTableQuery(QueryLanguage.SERQL, query);
			for (int i = 0; i < result.getRowCount(); i++) {
				Value v = result.getValue(i, 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	HashMap allIntersections;
	HashMap allUnions;
	void handleClassPartial() {
		allIntersections = new HashMap();
		allUnions = new HashMap();
		String query = "select C, inter, eq, uni, comp from"+
			"{C} rdf:type {<"+OWLConstants.OWL_Class+">} "+
			",[ {C} <"+OWLConstants.OWL_intersectionOf+"> {inter}] "+
			",[ {C} <"+OWLConstants.OWL_equivalentClass+"> {eq}] "+
			",[ {C} <"+OWLConstants.OWL_unionOf+"> {uni}] "+
			",[ {C} <"+OWLConstants.OWL_complementOf+"> {comp}] ";
		try {
			QueryResultsTable result = _repository.performTableQuery(QueryLanguage.SERQL, query);
			for (int i = 0; i < result.getRowCount(); i++) {
				Value v = result.getValue(i, 0);
				if (null != result.getValue(i, 1)) {
					allIntersections.put(v, result.getValue(i, 1));
				} else if (null != result.getValue(i, 3)) {
 					allUnions.put(v, result.getValue(i, 3));
				} else if (null != result.getValue(i, 2) ||
					null != result.getValue(i, 4)) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	HashMap allAttributes;	
	void handlePossibleAttributes() {
		allAttributes = new HashMap();
		for (Iterator iter = allProperties.keySet().iterator(); iter.hasNext();) {
			Resource res = (Resource)iter.next();
			if (isExternalProperty(res))
				continue;
			String query = "select C from {<"+res+">} rdfs:domain {C}";
			try {
				QueryResultsTable result = _repository.performTableQuery(QueryLanguage.SERQL, query);
				for (int i = 0; i < result.getRowCount(); i++) {
					Value v = result.getValue(i, 0);
					if (allClasses.containsKey(v))
						allAttributes.put(res, v);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (Iterator iter = allAttributes.keySet().iterator(); iter.hasNext();) {
			allProperties.remove(iter.next());
		}
	}
	
    /**
     * 
     * @param content
     * @param defaultNs the baseURL
     * @return ontology
     */
	public Ontology process(Reader content, String defaultNs) throws Exception {
		// defaultNs is always null!
        //assert defaultNs != null;
		try {
			dcSameAs = _factory.createIRI(Constants.DC_NS+"sameAs");
            
//            InputStream resStream = Thread.currentThread().getContextClassLoader()
//                    .getResourceAsStream("resource/owl.rdfs");
//            Reader reader = new InputStreamReader(resStream);
//			addData(reader, "http://www.w3.org/2002/07/owl#");
            
            collectDefaultClasses();
			collectDefaultProperties();
			if (defaultNs == null)
				addData(content, "");
			else
				addData(content, defaultNs);
			
			processOntologies();
			assert theOntology != null;
			if (defaultNs == null) {
				defaultNs = theOntology.getIdentifier().toString();
				if (defaultNs.endsWith("#") == false)
					defaultNs+='#';
			}
			theOntology.setDefaultNamespace(_factory.createIRI(defaultNs));
			
			theOntology.addNamespace(_factory.createNamespace("dc", _factory.createIRI(Constants.DC_NS)));
			theOntology.addNamespace(_factory.createNamespace("wsml", _factory.createIRI(WSML.WSML_NAMESPACE)));
			theOntology.addNamespace(_factory.createNamespace("xsd", _factory.createIRI(Constants.XSD_NS)));
			handleClasses();
			handleRestrictionOnProperty();
			handleProperties();
			handleInstances();
			handleClassPartial();
			
			
			processClassDefinitions();
			processRelationDefinitions();
			processIndividuals();
			
		} catch (Exception ex) {
			// @todo: rise proper exception 
			ex.printStackTrace();
            throw ex;
		}
		return theOntology;
	}
	
	int axiom_counter = 0;

	void processAsUnionClass(Resource res) throws InvalidModelException {
		String uriStr = getGenerateID(res, "Concept"); 
		IRI id = _factory.createIRI(uriStr);
		String logExpr = "?x memberOf "+xForm(uriStr)+"\n"+
		"impliedBy ";
		Concept concept = _factory.getConcept(id);
//		theOntology.addConcept(concept);
		
		ArrayList list = new ArrayList();
		collectNodesInSEQ((Resource)allUnions.get(res), list);
		boolean bHasOne = false;
		for (Iterator sc = list.iterator(); sc.hasNext();) {
			Resource element = (Resource) sc.next();
			if (allClasses.containsKey(element)) {
				//case 1 known names Class
				IRI ref = _factory.createIRI(getGenerateID(element, "Concept"));
//				concept.addSubConcept(_factory.getConcept(ref));
				if (bHasOne)
					logExpr +=" or ";
				logExpr += "?x memberOf "+xForm(ref.toString());
				bHasOne = true;
			} else if (allRestrictions.containsKey(element)){
				IRI asId = _factory.createIRI(uriStr + (++axiom_counter));
				Axiom ax = _factory.createAxiom(asId);
				theOntology.addAxiom(ax);
				
				ax.addNFPValue(_factory.createIRI(Constants.DC_relation), id);
				String foo = (String)allRestrictions.get(element);
				do {
					int pos = foo.indexOf("|class|");
					if (pos < 0)
						break;
					foo = foo.substring(0, pos)+ xForm(uriStr)+foo.substring(pos + "|class|".length());
				} while (true);
				try {
					ax.addDefinition(_leFactory.createLogicalExpression(foo, theOntology));
				} catch (ParserException pe) {
					throw new InvalidModelException(pe);
				}
				concept.addNFPValue(_factory.createIRI(Constants.DC_relation), asId);
			}									
		}
		try {
			{
				IRI asId = _factory.createIRI(uriStr + (++axiom_counter));
				Axiom ax = _factory.createAxiom(asId);
				theOntology.addAxiom(ax);
				logExpr+= " .";
				ax.addNFPValue(_factory.createIRI(Constants.DC_relation), id);
				ax.addDefinition(_leFactory.createLogicalExpression(logExpr, theOntology));
				concept.addNFPValue(_factory.createIRI(Constants.DC_relation), asId);
			}
		
			String query = "select R from "+
			" {<"+uriStr+">} serql:directSubClassOf {R}, {R} rdf:type {<"+OWLConstants.OWL_Restriction+">}"
			;
			QueryResultsTable result = _repository.performTableQuery(QueryLanguage.SERQL, query);
			for (int i = 0; i < result.getRowCount(); i++) {
				Resource element = (Resource)result.getValue(i, 0);
				IRI asId = _factory.createIRI(uriStr + (++axiom_counter));
				Axiom ax = _factory.createAxiom(asId);
				theOntology.addAxiom(ax);
				
				ax.addNFPValue(_factory.createIRI(Constants.DC_relation), id);
				String foo = (String)allRestrictions.get(element);
				do {
					int pos = foo.indexOf("|class|");
					if (pos < 0)
						break;
					foo = foo.substring(0, pos)+ xForm(uriStr)+foo.substring(pos + "|class|".length());
				} while (true);
				ax.addDefinition(_leFactory.createLogicalExpression(foo, theOntology));
				concept.addNFPValue(_factory.createIRI(Constants.DC_relation), asId);

			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void processAsIntersectionClass(Resource res) throws InvalidModelException {
		String uriStr = getGenerateID(res, "Concept"); 
		IRI id = _factory.createIRI(uriStr);
		Concept concept = _factory.getConcept(id);
		
		ArrayList list = new ArrayList();
		collectNodesInSEQ((Resource)allIntersections.get(res), list);
		
		for (Iterator sc = list.iterator(); sc.hasNext();) {
			Resource element = (Resource) sc.next();
			if (allClasses.containsKey(element)) {
				//case 1 known names Class
				IRI ref = _factory.createIRI(getGenerateID(element, "Concept"));
				concept.addSuperConcept(_factory.getConcept(ref));
				
			} else if (allRestrictions.containsKey(element)){
				IRI asId = _factory.createIRI(uriStr + (++axiom_counter));
				Axiom ax = _factory.createAxiom(asId);
				theOntology.addAxiom(ax);
				
				ax.addNFPValue(_factory.createIRI(Constants.DC_relation), id);
				String foo = (String)allRestrictions.get(element);
				do {
					int pos = foo.indexOf("|class|");
					if (pos < 0)
						break;
					foo = foo.substring(0, pos)+ xForm(uriStr)+foo.substring(pos + "|class|".length());
				} while (true);
				try {
					ax.addDefinition(_leFactory.createLogicalExpression(foo, theOntology));
				} catch (ParserException pe) {
					throw new InvalidModelException(pe);
				}
				concept.addNFPValue(_factory.createIRI(Constants.DC_relation), asId);
			}									
		}
		try {
			String query = "select R from "+
			" {<"+uriStr+">} serql:directSubClassOf {R}, {R} rdf:type {<"+OWLConstants.OWL_Restriction+">}"
			;
			QueryResultsTable result = _repository.performTableQuery(QueryLanguage.SERQL, query);
			for (int i = 0; i < result.getRowCount(); i++) {
				Resource element = (Resource)result.getValue(i, 0);
				IRI asId = _factory.createIRI(uriStr + (++axiom_counter));
				Axiom ax = _factory.createAxiom(asId);
				theOntology.addAxiom(ax);
				
				ax.addNFPValue(_factory.createIRI(Constants.DC_relation), id);
				String foo = (String)allRestrictions.get(element);
				do {
					int pos = foo.indexOf("|class|");
					if (pos < 0)
						break;
					foo = foo.substring(0, pos)+ xForm(uriStr)+foo.substring(pos + "|class|".length());
				} while (true);
				ax.addDefinition(_leFactory.createLogicalExpression(foo, theOntology));
				concept.addNFPValue(_factory.createIRI(Constants.DC_relation), asId);

			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String anonIDPrefix;
	String getGenerateID(Resource res, String type) {
		if (res instanceof URI) {
			return ((URI)res).getURI();
		}
		if (anonIDPrefix == null) {
			anonIDPrefix = (theOntology.getDefaultNamespace() == null) ?
					theOntology.getIdentifier().toString()
					: theOntology.getDefaultNamespace().getIRI().toString();
			anonIDPrefix += (anonIDPrefix.endsWith("#")) ? "anonymous" : "#anonymous";
		}
		return anonIDPrefix + type + '_' + ((BNode)res).getID();
	}
	
	void processClassDefinitions() throws InvalidModelException {
		// forward create
		for (Iterator iter = allClasses.keySet().iterator(); iter.hasNext();) {
			Resource res = (Resource)iter.next();
			if (isExternalClass(res))
				continue;	//System.out.print("EXTERNAL:");

			IRI id = _factory.createIRI(getGenerateID(res, "Concept"));
			
			Concept concept = _factory.createConcept(id);
			ArrayList sameAs = getSameAs(res);
			for (Iterator i = sameAs.iterator(); i.hasNext();) {
				concept.addNFPValue(dcSameAs, _factory.createIRI( i.next().toString()));
			}
			theOntology.addConcept(concept);
		}
		
		for (Iterator iter = allClasses.keySet().iterator(); iter.hasNext();) {
			Resource res = (Resource)iter.next();
			if (isExternalClass(res))
				continue;	//System.out.print("EXTERNAL:");
			if (allIntersections.containsKey(res)) {
				processAsIntersectionClass(res);
			} if (allUnions.containsKey(res)) {
				processAsUnionClass(res);
			} else 
			{
				IRI id = _factory.createIRI(getGenerateID(res, "Concept"));;
				Concept concept = _factory.getConcept(id);
				String query = "select SC from {<"+id.toString()+">} serql:directSubClassOf {SC} "
					;
				try {
					QueryResultsTable result = _repository.performTableQuery(QueryLanguage.SERQL, query);

					for (int i = 0; i < result.getRowCount(); i++) {
						Value v = result.getValue(i, 0);
						if (allClasses.containsKey(v)) {
							IRI ref = _factory.createIRI( getGenerateID((Resource)v, "Concept"));
							concept.addSuperConcept(_factory.getConcept(ref));
							
						} else if (allRestrictions.containsKey(v)) {
							IRI asId = _factory.createIRI(id.toString() +(++axiom_counter));
							Axiom ax = _factory.createAxiom(asId);
							ax.addNFPValue(_factory.createIRI(Constants.DC_relation), id);
							theOntology.addAxiom(ax);
							String foo = (String)allRestrictions.get(v);
							do {
								int pos = foo.indexOf("|class|");
								if (pos < 0)
									break;
								foo = foo.substring(0, pos)+ xForm(id.toString())+foo.substring(pos + "|class|".length());
							} while (true);
							ax.addDefinition(_leFactory.createLogicalExpression(foo, theOntology));
							concept.addNFPValue(_factory.createIRI(Constants.DC_relation), asId);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} 
			} // if
			
		} //for
	} // processClassDefinitions
	
	void processRelationDefinitions() throws InvalidModelException {
		//forward create
		for (Iterator iter = allProperties.keySet().iterator(); iter.hasNext();) {
			Resource res = (Resource)iter.next();
			if (isExternalProperty(res))
				continue;
			IRI id = _factory.createIRI(getGenerateID(res, "Relation"));
			Relation relation = _factory.createRelation(id);

			ArrayList sameAs = getSameAs(res);
			for (Iterator i = sameAs.iterator(); i.hasNext();) {
				relation.addNFPValue(dcSameAs, _factory.createIRI( i.next().toString()));
			}
			theOntology.addRelation(relation);
			relation.createParameter((byte)0);
			relation.createParameter((byte)1);
			
		}
		for (Iterator iter = allProperties.keySet().iterator(); iter.hasNext();) { 
			Resource res = (Resource)iter.next();
			if (isExternalProperty(res))
				continue;
			String query = "select SP from {<"+res+">} serql:directSubPropertyOf {SP} ";
			IRI id  = _factory.createIRI(getGenerateID(res, "Relation"));
			Relation relation = _factory.getRelation(id);
			try {
				QueryResultsTable result = _repository.performTableQuery(QueryLanguage.SERQL, query);
				
				for (int i = 0; i < result.getRowCount(); i++) {
					URI v = (URI)result.getValue(i, 0);
					IRI idSuper = _factory.createIRI(v.getURI());
					relation.addSuperRelation(_factory.getRelation(idSuper));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// collect features
			
			StatementIterator stIter = null;
			ArrayList axioms = new ArrayList();
			try {
				stIter = res.getSubjectStatements();
				boolean bDataTypeProp = false;
				boolean bObjectProp = false;
				boolean bHasRange = false;
				boolean bHasDomain = false;
				while (stIter.hasNext()) {
					Statement st = stIter.next();
					URI u = st.getPredicate();
					if (0==u.getURI().compareTo(Constants.RDFS_domain)) {
						String domainConcept = getGenerateID((Resource)st.getObject(), "Concept");
//						URI domainConcept = (URI)st.getObject();
						String concept = xForm(domainConcept);
						IRI domainIRI = _factory.createIRI(domainConcept);
						relation.getParameter((byte)0).addType(_factory.getConcept(domainIRI));
						String attr = xForm(res.toString());
						axioms.add("?x memberOf "+concept+" impliedBy "+attr+"(?x,?y).");
						bHasDomain = true;
						continue;
					}
					if (0==u.getURI().compareTo(Constants.RDFS_range)) {
						String rangeConceptID = getGenerateID((Resource)st.getObject(), "Concept");
						String concept = xForm(rangeConceptID);
						IRI rangeIRI = _factory.createIRI(rangeConceptID);
						relation.getParameter((byte)1).addType(_factory.getConcept(rangeIRI));
						String attr = xForm(res.toString());
						axioms.add("?y memberOf "+concept+" impliedBy "+attr+"(?x,?y).");
						bHasRange = true;
						continue;
					}
					if (0==u.getURI().compareTo(Constants.RDF_type)) {
						String value = st.getObject().toString();
						if (0 == value.compareTo(OWLConstants.OWL_TransitiveProperty)) {
							String attr = xForm(res.toString());
							axioms.add("?x["+attr+" hasValue ?z] impliedBy  ?x["+attr+" hasValue ?y] and ?y["+attr+" hasValue ?z].");
							continue;
						}
						if (0 == value.compareTo(OWLConstants.OWL_SymmetricProperty)) {
							String attr = xForm(res.toString());
							axioms.add("?x["+attr+" hasValue ?y] impliedBy ?y["+attr+" hasValue ?x].");
							continue;
						}
						if (0 == value.compareTo(OWLConstants.OWL_DatatypeProperty)) {
							bDataTypeProp = true;
						}
						if (0 == value.compareTo(OWLConstants.OWL_ObjectProperty)) {
							bObjectProp = true;
						}
					}
					if (0==u.getURI().compareTo(OWLConstants.OWL_inverseOf)) {
						String value = xForm(st.getObject().toString());
						String attr = xForm(res.toString());
						axioms.add("?x["+attr+" hasValue ?y] impliedBy ?y["+value+" hasValue ?x].");
						if (0 != value.compareTo(attr))
							axioms.add("?x["+value+" hasValue ?y] impliedBy ?y["+attr+" hasValue ?x].");
						continue;
					}
					// handle annotations later
				}
				if (!bHasDomain && (bDataTypeProp || bObjectProp )) {
					IRI thingIRI = _factory.createIRI(OWLConstants.OWL_Thing);
					relation.getParameter((byte)0).addType(_factory.getConcept(thingIRI));
				}
				if (!bHasRange) {
					if (bDataTypeProp) {
						IRI stringIRI = _factory.createIRI(Constants.XSD_string);
						relation.getParameter((byte)1).addType(_factory.getConcept(stringIRI));
					} else if (bObjectProp) {
						IRI thingIRI = _factory.createIRI(OWLConstants.OWL_Thing);
						relation.getParameter((byte)1).addType(_factory.getConcept(thingIRI));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				stIter.close();
			}
			for (Iterator axIter = axioms.iterator(); axIter.hasNext();) {
				IRI asId = _factory.createIRI(id.toString() + (++axiom_counter));
				Axiom ax = _factory.createAxiom(asId);
				theOntology.addAxiom(ax);
				
				ax.addNFPValue(_factory.createIRI(Constants.DC_relation), id);
				String foo = (String)axIter.next(); 
				try {
					ax.addDefinition(_leFactory.createLogicalExpression(foo, theOntology));
				} catch (ParserException pe) {
					throw new InvalidModelException(pe);
				}
				relation.addNFPValue(_factory.createIRI(Constants.DC_relation), asId);
			}
		}
	} //processRelationDefinitions
	
	void processIndividuals() throws InvalidModelException {
		for (Iterator iter = allInstances.keySet().iterator(); iter.hasNext();) {
			Resource res = (Resource)iter.next();
			if (isExternalIndividual(res)) {
				continue;
			}
			if (allClasses.containsKey(res)) {
			    continue;
			}
			if (allProperties.containsKey(res)) {
				continue;
			}
			IRI id = _factory.createIRI(getGenerateID(res, "Instance"));
			Instance instance = _factory.createInstance(id);
			theOntology.addInstance(instance);
			ArrayList sameAs = getSameAs(res);
			for (Iterator i = sameAs.iterator(); i.hasNext();) {
				instance.addNFPValue(dcSameAs, _factory.createIRI( i.next().toString()));
			}
			
			String query = "select C from {<" + id.toString() +">} serql:directType {C} "+
				" where C != <"+OWLConstants.OWL_Thing+">";
			try {
				QueryResultsTable result = _repository.performTableQuery(QueryLanguage.SERQL, query);
				for (int i = 0; i < result.getRowCount(); i++) {
					URI v = (URI)result.getValue(i, 0);
					IRI ref = _factory.createIRI(v.getURI());
					try {
						instance.addConcept(_factory.getConcept(ref));
					} catch (UndeclaredThrowableException se) {
						instance.addConcept(_factory.createConcept(ref));
					} catch (SynchronisationException se3) {
						instance.addConcept(_factory.createConcept(ref));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// all properties as realationInstances
			query = "select P,V,SP from {<"+ id.toString() +">} P {V} "+
				" ,[{V} rdfs:subClassOf {Q}]"+
				" ,[{P} serql:directSubPropertyOf {SP}]"+
				" where Q = NULL";
			try {
				QueryResultsTable result = _repository.performTableQuery(QueryLanguage.SERQL, query);
				for (int i = 0; i < result.getRowCount(); i++) {
					URI v = (URI)result.getValue(i, 0);
					if (!allProperties.containsKey(v))
						continue;
					if (isInferred(res,v, result.getValue(i, 1)))
						continue; 
					
					IRI ref = _factory.createIRI(v.getURI());
					Relation parent = _factory.getRelation(ref);
					
					Identifier anon = _factory.createAnonymousID();
					RelationInstance relInst = _factory.createRelationInstance(anon, parent);
					theOntology.addRelationInstance(relInst);
					
					try {
						relInst.setRelation(parent);
					} catch (SynchronisationException se) {
						parent = _factory.createRelation(ref);
						parent.createParameter((byte)0);
						parent.createParameter((byte)1);
						relInst.setRelation(parent);
					}
					try {
						relInst.setParameterValue((byte)0, instance);
					} catch (IllegalArgumentException e) {
//						e.printStackTrace();
					}
					
					Value val = result.getValue(i, 1);
						if (val instanceof URI) {
							IRI refInst = _factory.createIRI(((URI)val).getURI());
							relInst.setParameterValue((byte)1, _factory.getInstance( refInst));
						} else if (val instanceof Literal) {
							DataValue dataValue = createDataValueFromLiteral((Literal)val);
							relInst.setParameterValue((byte)1, dataValue);
						}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	} // processIndividuals
	
	Ontology theOntology = null;
	
	void processOntologies() throws Exception {
			// detect the main ontology and create It
			QueryResultsTable result = _repository.performTableQuery(QueryLanguage.SERQL, mainOntology);
			if (result.getRowCount() != 1) {
				throw new RuntimeException("the content should have a single OWL ontology! numOntologies="+result.getRowCount());
			}
			for (int i = 0; i < result.getRowCount(); i++) {
				URI asUri = (URI)result.getValue(i, 0);
				IRI id = _factory.createIRI(asUri.getURI());
				theOntology = _factory.createOntology(id); 
			}

			// collect the imported ontologies
			result = _repository.performTableQuery(QueryLanguage.SERQL, importedOntologyQuery);
			for (int i = 0; i < result.getRowCount(); i++) {
				URI asUri = (URI)result.getValue(i, 0);
				IRI id = _factory.createIRI(asUri.getURI());
				theOntology.addOntology( _factory.getOntology(id)); 
			}

			// collect annotations
			String query = "select P, V from "+
			 " {<"+theOntology.getIdentifier().toString()+">} P {V} "+
			 " ,{P} rdf:type {<"+OWLConstants.OWL_AnnotationProperty+">} ";
			result = _repository.performTableQuery(QueryLanguage.SERQL, query);
			for (int i = 0; i < result.getRowCount(); i++) {
				Value val = result.getValue(i, 1);
				if (val instanceof URI)
					theOntology.addNFPValue(_factory.createIRI(Constants.DC_description), 
                            _factory.createIRI(val.toString()));
				else {
					theOntology.addNFPValue(_factory.createIRI(Constants.DC_description), 
                            createDataValueFromLiteral((Literal)val));
				}
			}
			RdfSchemaSource src = (RdfSchemaSource)_repository.getSail();
			NamespaceIterator nsIter = src.getNamespaces();
			while (nsIter.hasNext()) {
				nsIter.next();
                theOntology.addNamespace(_factory.createNamespace(nsIter.getPrefix(), 
                        _factory.createIRI( nsIter.getName())));
			}
			
	}	
	
/*	public static void main(String[] args) {
		WSMLFromOWL test = new WSMLFromOWL(Factory.createWsmoFactory(null), Factory.createLogicalExpressionFactory(null), null);
		try {
			if (true) {
				//"http://www.w3.org/TR/2003/CR-owl-guide-20030818/food#"
				//"http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine"
				FileReader content = new FileReader("Pizza.owl");
				Ontology o = test.process(content, "http://www.w3.org/TR/2003/CR-owl-guide-20030818/food");
				StringWriter wrt = new StringWriter();
				WSMLTextExportHelper helper = new WSMLTextExportHelper(wrt);
				helper.process(new Entity[]{o});
				System.out.println(wrt.getBuffer().toString());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
*/	
}

/* * $Log: WSMLFromOWL.java,v $
/* * Revision 1.12  2006/11/22 12:06:58  alex_simov
/* * no message
/* *
/* * Revision 1.11  2006/11/16 09:36:28  holgerlausen
/* * removed duplicated namespace definition occurences
/* *
/* * Revision 1.10  2006/10/31 15:52:58  vassil_momtchev
/* * consider # as the only valid namespace ending (simulate the behaviour of the RIO parser)
/* *
/* * Revision 1.9  2006/09/13 08:58:08  alex_simov
/* * bugfix: sQNames were not checked for containing symbols to be escaped
/* *
 * * Revision 1.8  2006/09/07 11:04:07  alex_simov
 * * minor fix
 * * Revision 1.7  2006/08/22 08:30:55  alex_simov
 * * no message
 * * Revision 1.6  2006/08/21 16:05:32  alex_simov
 * 1) support for anonymois classes added (bugfix)
 * 2) union classes
 * 3) incorrect treatment of properties fixed
 * * Revision 1.5  2006/02/22 14:29:25  nathaliest
 * *** empty log message ***
 *
 * Revision 1.4  2005/12/14 09:54:07  vassil_momtchev
 * changed all const from IRIto String [Constants, OWLConstants, WSMLFromOWL] - no more wsmo4j constructors invoked!
 * organized imports to use com.ontotext.* instead to list all used types (see the rest of code and the code convetion)
 * commented all non used local variables (all warnings removed)
*/
