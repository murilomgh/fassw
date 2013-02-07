/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c 2004-2005, OntoText Lab. / SIRMA

 This library is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the Free
 Software Foundation; either version 2.1 of the License, or (at your option
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

import org.wsmo.common.WSML;


/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation. RDF vocabulary</p>
 * <p>Copyright:  Copyright (c 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */

public class Constants {
    public static String WSML_variant = WSML.WSML_NAMESPACE+"variant";
    public static String WSML_wsml_core = WSML.WSML_NAMESPACE+"wsml_core";
    public static String WSML_wsml_flight = WSML.WSML_NAMESPACE+"wsml_flight";
    public static String WSML_wsml_rule = WSML.WSML_NAMESPACE+"wsml_rule";
    public static String WSML_wsml_full = WSML.WSML_NAMESPACE+"wsml_full";

    public static String WSML_ontology = WSML.WSML_NAMESPACE+"ontology";
    public static String WSML_concept = WSML.WSML_NAMESPACE+"concept";
    public static String WSML_hasConcept = WSML.WSML_NAMESPACE+"hasConcept";
    public static String WSML_attribute = WSML.WSML_NAMESPACE+"attribute";
    public static String WSML_ofType = WSML.WSML_NAMESPACE+"ofType";
    public static String WSML_impliesType = WSML.WSML_NAMESPACE+"impliesType";
    public static String WSML_hasAttribute = WSML.WSML_NAMESPACE+"hasAttribute";
    public static String WSML_transitiveAttribute = WSML.WSML_NAMESPACE+"transitiveAttribute";
    public static String WSML_symmetricAttribute = WSML.WSML_NAMESPACE+"symmetricAttribute";
    public static String WSML_reflexiveAttribute = WSML.WSML_NAMESPACE+"reflexiveAttribute";
    public static String WSML_inverseOf = WSML.WSML_NAMESPACE+"inverseOf";
    public static String WSML_minCardinality = WSML.WSML_NAMESPACE+"minCardinality";
    public static String WSML_maxCardinality = WSML.WSML_NAMESPACE+"maxCardinality";
    public static String WSML_instance = WSML.WSML_NAMESPACE+"instance";
    public static String WSML_hasInstance = WSML.WSML_NAMESPACE+"hasInstance";
    public static String WSML_relation = WSML.WSML_NAMESPACE+"relation";
    public static String WSML_hasRelation = WSML.WSML_NAMESPACE+"hasRelation";
    public static String WSML_arity = WSML.WSML_NAMESPACE+"arity";
    public static String WSML_param = WSML.WSML_NAMESPACE+"param";
    public static String WSML_subRelationOf = WSML.WSML_NAMESPACE+"subRelationOf";
    public static String WSML_relationInstance = WSML.WSML_NAMESPACE+"relationInstance";
    public static String WSML_hasRelationInstance = WSML.WSML_NAMESPACE+"hasRelationInstance";
    public static String WSML_axiom = WSML.WSML_NAMESPACE+"axiom";
    public static String WSML_hasAxiom = WSML.WSML_NAMESPACE+"hasAxiom";

    public static String WSML_goal = WSML.WSML_NAMESPACE+"goal";
    public static String WSML_ooMediator = WSML.WSML_NAMESPACE+"ooMediator";
    public static String WSML_ggMediator = WSML.WSML_NAMESPACE+"ggMediator";
    public static String WSML_wgMediator = WSML.WSML_NAMESPACE+"wgMediator";
    public static String WSML_wwMediator = WSML.WSML_NAMESPACE+"wwMediator";
    public static String WSML_source = WSML.WSML_NAMESPACE+"source";
    public static String WSML_target = WSML.WSML_NAMESPACE+"target";
    public static String WSML_usesService = WSML.WSML_NAMESPACE+"usesService";
    public static String WSML_webService = WSML.WSML_NAMESPACE+"webService";
    public static String WSML_useInterface = WSML.WSML_NAMESPACE+"useInterface";
    public static String WSML_useCapability = WSML.WSML_NAMESPACE+"useCapability";
    public static String WSML_sharedVariables = WSML.WSML_NAMESPACE+"sharedVariables";
    public static String WSML_precondition = WSML.WSML_NAMESPACE+"precondition";
    public static String WSML_assumption = WSML.WSML_NAMESPACE+"assumption";
    public static String WSML_postcondition = WSML.WSML_NAMESPACE+"postcondition";
    public static String WSML_effect = WSML.WSML_NAMESPACE+"effect";
    public static String WSML_choreography = WSML.WSML_NAMESPACE+"choreography";
    public static String WSML_orchestration = WSML.WSML_NAMESPACE+"orchestration";
    public static String WSML_capability = WSML.WSML_NAMESPACE+"capability";
    public static String WSML_interface = WSML.WSML_NAMESPACE+"interface";
    public static String WSML_nfp = WSML.WSML_NAMESPACE+"nfp";
    public static String WSML_importsOntology = WSML.WSML_NAMESPACE+"importsOntology";
    public static String WSML_usesMediator = WSML.WSML_NAMESPACE+"usesMediator";

    public static String WSML_false = WSML.WSML_NAMESPACE+"false";
    public static String WSML_true = WSML.WSML_NAMESPACE+"true";

    public static String WSML_kind = WSML.WSML_NAMESPACE+"kind";

    public static String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    public static String RDF_type = RDF_NS+"type";
    public static String RDF_XMLLiteral = RDF_NS+"XMLLiteral";
    public static String RDF_List = RDF_NS+"List";
    public static String RDF_first = RDF_NS+"first";
    public static String RDF_rest = RDF_NS+"rest";
    public static String RDF_Property = RDF_NS+"Property";
    public static String RDF_nil = RDF_NS+"nil";

    public static String RDFS_NS = "http://www.w3.org/2000/01/rdf-schema#";

    public static String RDFS_subClassOf = RDFS_NS+"subClassOf";
    public static String RDFS_range = RDFS_NS+"range";
    public static String RDFS_label = RDFS_NS+"label";
    public static String RDFS_comment = RDFS_NS+"comment";
    public static String RDFS_seeAlso = RDFS_NS+"seeAlso";
    public static String RDFS_isDefinedBy = RDFS_NS+"isDefinedBy";
    public static String RDFS_domain = RDFS_NS+"domain";
    public static String RDFS_Resource = RDFS_NS+"Resource";
    public static String RDFS_Datatype = RDFS_NS+"Datatype";
    public static String RDFS_Class = RDFS_NS+"Class";
    public static String RDFS_subPropertyOf = RDFS_NS+"subPropertyOf";
    public static String RDFS_member = RDFS_NS+"member";
    public static String RDFS_Container = RDFS_NS+"Container";
    public static String RDFS_ContainerMembershipProperty = RDFS_NS+"ContainerMembershipProperty";
 
    public static String DC_NS = "http://purl.org/dc/elements/1.1/";
    public static String DC_relation = DC_NS+"relation";
    public static String DC_title = DC_NS+"title";
    public static String DC_subject = DC_NS+"subject";
    public static String DC_description = DC_NS+"description";
    public static String DC_contributor = DC_NS+"contributor";
    public static String DC_date = DC_NS+"date";
    public static String DC_format = DC_NS+"format";
    public static String DC_language = DC_NS+"language";
    public static String DC_rights = DC_NS+"rights";
    public static String DC_type = DC_NS+"type";

    public static String XSD_NS = "http://www.w3.org/2001/XMLSchema#";
    public static String XSD_string = XSD_NS+"string";
    public static String XSD_integer = XSD_NS+"integer";
    public static String XSD_decimal = XSD_NS+"decimal";
    public static String XSD_date = XSD_NS+"date";
    public static String XSD_anyURI = XSD_NS+"anyURI";

}

/* * $Log: Constants.java,v $
/* * Revision 1.4  2006/11/16 09:36:28  holgerlausen
/* * removed duplicated namespace definition occurences
/* *
/* * Revision 1.3  2005/12/14 09:54:07  vassil_momtchev
/* * changed all const from IRIto String [Constants, OWLConstants, WSMLFromOWL] - no more wsmo4j constructors invoked!
/* * organized imports to use com.ontotext.* instead to list all used types (see the rest of code and the code convetion)
/* * commented all non used local variables (all warnings removed)
/* *
*/
