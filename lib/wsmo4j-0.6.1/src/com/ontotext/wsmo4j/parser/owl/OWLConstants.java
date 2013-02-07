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


/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation - OWL vocabulary</p>
 * <p>Copyright:  Copyright (c 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */
public class OWLConstants extends Constants {

    public static String OWL_NS = "http://www.w3.org/2002/07/owl#";
    public static String OWL_AllDifferent = OWL_NS+"AllDifferent";
    public static String OWL_allValuesFrom = OWL_NS+"allValuesFrom";
    public static String OWL_AnnotationProperty = OWL_NS+"AnnotationProperty";
    public static String OWL_cardinality = OWL_NS+"cardinality";
    public static String OWL_Class = OWL_NS+"Class";
    public static String OWL_complementOf = OWL_NS+"complementOf";
    public static String OWL_DataRange = OWL_NS+"DataRange";
    public static String OWL_DatatypeProperty = OWL_NS+"DatatypeProperty";
    public static String OWL_DeprecatedClass = OWL_NS+"DeprecatedClass";
    public static String OWL_ObjectProperty = OWL_NS+"ObjectProperty";
    public static String OWL_minCardinality = OWL_NS+"minCardinality";
    public static String OWL_maxCardinality = OWL_NS+"maxCardinality";
    public static String OWL_inverseOf = OWL_NS+"inverseOf";
    public static String OWL_InverseFunctionalProperty = OWL_NS+"InverseFunctionalProperty";
    public static String OWL_intersectionOf = OWL_NS+"intersectionOf";
    public static String OWL_hasValue = OWL_NS+"hasValue";
    public static String OWL_FunctionalProperty = OWL_NS+"FunctionalProperty";
    public static String OWL_equivalentProperty = OWL_NS+"equivalentProperty";
    public static String OWL_equivalentClass = OWL_NS+"equivalentClass";
    public static String OWL_distinctMembers = OWL_NS+"distinctMembers";
    public static String OWL_disjointWith = OWL_NS+"disjointWith";
    public static String OWL_differentFrom = OWL_NS+"differentFrom";
    public static String OWL_DeprecatedProperty = OWL_NS+"DeprecatedProperty";
    public static String OWL_oneOf = OWL_NS+"oneOf";
    public static String OWL_onProperty = OWL_NS+"onProperty";
    public static String OWL_Ontology = OWL_NS+"Ontology";
    public static String OWL_OntologyProperty = OWL_NS+"OntologyProperty";
    public static String OWL_Restriction = OWL_NS+"Restriction";
    public static String OWL_sameAs = OWL_NS+"sameAs";
    public static String OWL_someValuesFrom = OWL_NS+"someValuesFrom";
    public static String OWL_SymmetricProperty = OWL_NS+"SymmetricProperty";
    public static String OWL_TransitiveProperty = OWL_NS+"TransitiveProperty";
    public static String OWL_unionOf = OWL_NS+"unionOf";
    public static String OWL_imports = OWL_NS+"imports";
    public static String OWL_Thing = OWL_NS+"Thing";
    public static String OWL_Nothing = OWL_NS+"Nothing";
}

/*
 * $Log: OWLConstants.java,v $
 * Revision 1.3  2005/12/14 09:54:07  vassil_momtchev
 * changed all const from IRIto String [Constants, OWLConstants, WSMLFromOWL] - no more wsmo4j constructors invoked!
 * organized imports to use com.ontotext.* instead to list all used types (see the rest of code and the code convetion)
 * commented all non used local variables (all warnings removed)
 *
 * Revision 1.2  2005/12/12 11:44:10  marin_dimitrov
 * log
 *
 *
*/
