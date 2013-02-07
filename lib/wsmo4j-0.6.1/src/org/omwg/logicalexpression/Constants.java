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
package org.omwg.logicalexpression;

import org.wsmo.common.WSML;


/**
 * This interface contains constants that can appear in a logical expression, like
 * e.g., delimiters, built-in predicate names and datatypes
 * @author DERI Innsbruck, reto.krummenacher@deri.org
 * @version $Revision: 1.6 $ $Date: 2006/11/16 09:36:28 $
 */
public interface Constants {

    final static String IRI_DEL_START = "_\"";

    final static String STRING_DEL_START = "\"";

    final static String IRI_DEL_END = STRING_DEL_START;

    final static String STRING_DEL_END = STRING_DEL_START;

    final static String SQNAME_DEL = "#";

    final static String VARIABLE_DEL = "?";

    final static String WSML_NAMESPACE = WSML.WSML_NAMESPACE;

    final static String ANONYMOUS_ID = WSML_NAMESPACE + "anonymousID";

    final static String ANONYMOUS_ID_NOTATION = "_#";

    final static String TRUE = "true";

    final static String FALSE = "false";

    final static String UNIV_TRUE = WSML_NAMESPACE + TRUE;

    final static String UNIV_FALSE = WSML_NAMESPACE + FALSE;

    final static String NUMERIC_ADD = WSML_NAMESPACE + "numericAdd";

    final static String NUMERIC_SUB = WSML_NAMESPACE + "numericSubtract";

    final static String NUMERIC_MUL = WSML_NAMESPACE + "numericMultiply";

    final static String NUMERIC_DIV = WSML_NAMESPACE + "numericDivide";

    final static String NUMERIC_ADD_NOTATION = "+";

    final static String NUMERIC_SUB_NOTATION = "-";

    final static String NUMERIC_MUL_NOTATION = "*";

    final static String NUMERIC_DIV_NOTATION = "/";

    final static String EQUAL = WSML_NAMESPACE + "equal";

    final static String STRONG_EQUAL = WSML_NAMESPACE + "strongEqual";

    final static String INEQUAL = WSML_NAMESPACE + "inequal";

    final static String STRING_EQUAL = WSML_NAMESPACE + "stringEqual";

    final static String STRING_INEQUAL = WSML_NAMESPACE + "stringInequal";

    final static String NUMERIC_EQUAL = WSML_NAMESPACE + "numericEqual";

    final static String NUMERIC_INEQUAL = WSML_NAMESPACE + "numericInequal";

    final static String LESS_THAN = WSML_NAMESPACE + "lessThan";

    final static String LESS_EQUAL = WSML_NAMESPACE + "lessEqual";

    final static String GREATER_THAN = WSML_NAMESPACE + "greaterThan";

    final static String GREATER_EQUAL = WSML_NAMESPACE + "greaterEqual";

    final static String EQUAL_NOTATION = "=";

    final static String STRONG_EQUAL_NOTATION = ":=:";

    final static String INEQUAL_NOTATION = "!=";

    final static String STRING_EQUAL_NOTATION = "=";

    final static String STRING_INEQUAL_NOTATION = "!=";

    final static String NUMERIC_EQUAL_NOTATION = "=";

    final static String NUMERIC_INEQUAL_NOTATION = "!=";

    final static String LESS_THAN_NOTATION = "<";

    final static String LESS_EQUAL_NOTATION = "=<";

    final static String GREATER_THAN_NOTATION = ">";

    final static String GREATER_EQUAL_NOTATION = ">=";

}
/*
 * $Log: Constants.java,v $
 * Revision 1.6  2006/11/16 09:36:28  holgerlausen
 * removed duplicated namespace definition occurences
 *
 * Revision 1.5  2005/09/23 07:09:51  holgerlausen
 * moved constanttransformer from API to implementation, removed dublicated constants in logicalexpression.constants
 *
 * Revision 1.4  2005/09/13 09:51:42  holgerlausen
 * fixed problem with less or equal serialization
 *
 * Revision 1.3  2005/09/09 11:58:19  holgerlausen
 * fixed header logexp no longer extension
 *
 * Revision 1.2  2005/09/09 11:12:12  marin_dimitrov
 * formatting
 *
 * Revision 1.1  2005/09/02 13:32:43  ohamano
 * move logicalexpression packages from ext to core
 * move tests from logicalexpression.test to test module
 *
 * Revision 1.6  2005/09/01 10:53:31  nathaliest
 * added strong equality operator
 *
 * Revision 1.5  2005/08/24 16:16:12  nathaliest
 * added array of wsml datatypes
 *
 * Revision 1.4  2005/06/22 13:32:01  ohamano
 * change header
 *
 * Revision 1.3  2005/06/20 08:30:03  holgerlausen
 * formating
 *
 * Revision 1.2  2005/06/18 14:06:10  holgerlausen
 * added local LEFactory, updated javadoc, refactored LEVariable > Variable etc. parse(String) for LEFactory is running now
 *
 * Revision 1.1  2005/06/16 13:55:23  ohamano
 * first import
 *
 *
 */