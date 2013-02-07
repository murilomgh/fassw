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

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 */

package org.omwg.ontology;

import org.wsmo.common.*;

public interface WsmlDataType extends Type {

    String WSML_STRING = WSML.WSML_NAMESPACE+"string";
    String WSML_DECIMAL = WSML.WSML_NAMESPACE+"decimal";
    String WSML_INTEGER = WSML.WSML_NAMESPACE+"integer";
    String WSML_FLOAT = WSML.WSML_NAMESPACE+"float";
    String WSML_DOUBLE = WSML.WSML_NAMESPACE+"double";
    String WSML_IRI = WSML.WSML_NAMESPACE+"iri";
    String WSML_SQNAME = WSML.WSML_NAMESPACE+"sqname";
    String WSML_BOOLEAN = WSML.WSML_NAMESPACE+"boolean";
    String WSML_DURATION = WSML.WSML_NAMESPACE+"duration";
    String WSML_DATETIME = WSML.WSML_NAMESPACE+"dateTime";
    String WSML_TIME = WSML.WSML_NAMESPACE+"time";
    String WSML_DATE = WSML.WSML_NAMESPACE+"date";
    String WSML_GYEARMONTH = WSML.WSML_NAMESPACE+"gyearmonth";
    String WSML_GYEAR = WSML.WSML_NAMESPACE+"gyear";
    String WSML_GMONTHDAY = WSML.WSML_NAMESPACE+"gmonthday";
    String WSML_GDAY = WSML.WSML_NAMESPACE+"gday";
    String WSML_GMONTH = WSML.WSML_NAMESPACE+"gmonth";
    String WSML_HEXBINARY = WSML.WSML_NAMESPACE+"hexbinary";
    String WSML_BASE64BINARY = WSML.WSML_NAMESPACE+"base64binary";

    final static String WSML_STRING_NOTATION = "_string";
    final static String WSML_INTEGER_NOTATION = "_integer";
    final static String WSML_DECIMAL_NOTATION = "_decimal";
    final static String WSML_IRI_NOTATION = "_iri";
    final static String WSML_SQNAME_NOTATION = "_sqname";
    final static String WSML_FLOAT_NOTATION = "_float";
    final static String WSML_DOUBLE_NOTATION = "_double";
    final static String WSML_BOOLEAN_NOTATION = "_boolean";
    final static String WSML_DURATION_NOTATION = "_duration";
    final static String WSML_DATETIME_NOTATION = "_dateTime";
    final static String WSML_TIME_NOTATION = "_time";
    final static String WSML_DATE_NOTATION = "_date";
    final static String WSML_GYEARMONTH_NOTATION = "_gyearmonth";
    final static String WSML_GYEAR_NOTATION = "_gyear";
    final static String WSML_GMONTHDAY_NOTATION = "_gmonthday";
    final static String WSML_GDAY_NOTATION = "_gday";
    final static String WSML_GMONTH_NOTATION = "_gmonth";
    final static String WSML_HEXBINARY_NOTATION = "_hexbinary";
    final static String WSML_BASE64BINARY_NOTATION = "_base64binary";    

    /**
     * Returns the IRI of this type
     * @return The IRI of this type
     */
    IRI getIRI();
}

/*
 * $Log: WsmlDataType.java,v $
 * Revision 1.9  2006/11/16 09:36:28  holgerlausen
 * removed duplicated namespace definition occurences
 *
 * Revision 1.8  2005/09/23 07:09:51  holgerlausen
 * moved constanttransformer from API to implementation, removed dublicated constants in logicalexpression.constants
 *
 * Revision 1.7  2005/08/19 08:59:38  marin_dimitrov
 * -
 *
 * Revision 1.6  2005/07/06 12:22:48  marin_dimitrov
 * built-in datatypes
 *
 * Revision 1.5  2005/07/04 12:23:51  marin_dimitrov
 * no message
 *
 */
