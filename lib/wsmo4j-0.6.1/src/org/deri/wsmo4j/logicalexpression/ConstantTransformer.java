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
package org.deri.wsmo4j.logicalexpression;


import java.util.*;

import org.omwg.logicalexpression.*;
import org.omwg.ontology.*;


/**
 * <p>This singleton class is used to transform forth and back the built-in predicate names and
 * datatypes from IRI to short notation.</p>
 * <p>The Singleton design pattern allows to ensure that only one instance of a class
 * is created, to provide a global point of access to the object and to allow multiple
 * instances in the future without affecting a singleton class's clients</p>
 * @author DERI Innsbruck, reto.krummenacher@deri.org
 * @version $Revision$ $Date$
 * @see org.omwg.logicalexpression.Constants
 * @see <a href="http://en.wikipedia.org/wiki/Singleton_pattern">Singleton Pattern</a>
 */
public class ConstantTransformer {

    /**
     * The singleton instance of the ConstantTransformer
     */
    private static ConstantTransformer inst;

    /**
     * A hashtable containing the transformation from IRI to short notations
     */
    private Hashtable <String, BuiltInInfoSource> iri2notation;

    /**
     * A hashtable containing the transformation from the short notation to IRI
     */
    private Hashtable <String, String>notation2iri;

    /**
     * Singletons maintain a static reference to the sole singleton instance
     * and return a reference to that instance from a static instance() method.
     * @return the singleton instance of the ConstantTransformer
     */
    public static ConstantTransformer getInstance() {
        if (inst == null) {
            inst = new ConstantTransformer();
        }
        return inst;
    }

    /**
     * @param notation the short notation available
     * @return the full IRI corresponding to the short notation
     * @see java.util.Hashtable#get(java.lang.Object)
     */
    public String findIri(String notation) {
        return notation2iri.get(notation);
    }

    /**
     * @param iri the full IRI available
     * @return the short notation of the given IRI
     */
    public String findNotation(String iri) {
        BuiltInInfoSource nh = iri2notation.get(iri);
        if (nh != null) {
            return nh.getNotation();
        }
        return null;
    }

    /**
     * Some of the built-in predicates and function symbols like e.g.
     * comparisons or mathematical functions have an infix notation in WSML
     * human-readable syntax. This method allows you to find out if the predicate corresponding
     * to a given IRI has an infix notation.
     * @param iri the IRI for which one needs to know if there is an infix notation
     * @return a boolean that is true if there is an infix notation
     */
    public boolean isInfix(String iri) {
        BuiltInInfoSource nh = iri2notation.get(iri);
        if (nh != null) {
            return nh.isInfix();
        }
        return false;
    }
    
    public boolean isSimpleDataType(String iriOrNotation) {
        return iriOrNotation.equals(WsmlDataType.WSML_STRING_NOTATION) ||
                iriOrNotation.equals(WsmlDataType.WSML_INTEGER_NOTATION) ||
                iriOrNotation.equals(WsmlDataType.WSML_DECIMAL_NOTATION) ||
                iriOrNotation.equals(WsmlDataType.WSML_STRING) ||
                iriOrNotation.equals(WsmlDataType.WSML_INTEGER) ||
                iriOrNotation.equals(WsmlDataType.WSML_DECIMAL);
    }

    public boolean isDataType(String iriOrNotion) {
        //sanity check if _"" is still included -> remove
        iriOrNotion = iriOrNotion.trim();
        //FIXME
        if (iriOrNotion.indexOf("_\"") == 0) {
            iriOrNotion = iriOrNotion.substring(2, iriOrNotion.length() - 1);
        }
        BuiltInInfoSource nh = iri2notation.get(iriOrNotion);
        if (nh == null && notation2iri.get(iriOrNotion) != null) {
            nh = iri2notation.get(notation2iri.get(iriOrNotion));
        }
        if (nh != null) {
            return nh.isDataType();
        }
        return false;
    }

    /**
     * There are built-in predicates (atoms) and built-in function symbols in a logical expression.
     * This method allows one to find out if a built-in construction is actually an atom or a function
     * symbol.
     * @param iri the IRI for which the information is required
     * @return a boolean that is true if the construction is a function symbol
     */
    public boolean isBuiltInFunctionSymbol(String iri) {
        BuiltInInfoSource nh = iri2notation.get(iri);
        if (nh != null) {
            return nh.isFSymbol();
        }
        return false;
    }

    /**
     * Check if iri stands for a built-in predicate.
     * @param iri
     * @return true if iri is a built in predicate
     */
    public boolean isBuiltInAtom(String iri) {
        BuiltInInfoSource nh = iri2notation.get(iri);
        if (nh != null) {
            return nh.isBuiltInAtom();
        }
        return false;
    }

    /**
     * The private constructor of the ConstantTransformer
     */
    private ConstantTransformer() {
        //IRI => [notation, infix]
        iri2notation = new Hashtable <String, BuiltInInfoSource>();
        //notation => IRI
        notation2iri = new Hashtable <String, String>();

        fillHashTables();
    }

    /**
     * This method fills the two hashtables, which contain the transformations. The method is called
     * only once in the singleton constructor of the ConstantTransformer
     */
    private void fillHashTables() {
        add(Constants.UNIV_TRUE, new BuiltInInfoSource(Constants.TRUE, false, false, false, false));
        add(Constants.UNIV_FALSE, new BuiltInInfoSource(Constants.FALSE, false, false, false, false));

        add(Constants.NUMERIC_ADD, new BuiltInInfoSource(Constants.NUMERIC_ADD_NOTATION, true, false, false, false));
        add(Constants.NUMERIC_SUB, new BuiltInInfoSource(Constants.NUMERIC_SUB_NOTATION, true, false, false, false));
        add(Constants.NUMERIC_MUL, new BuiltInInfoSource(Constants.NUMERIC_MUL_NOTATION, true, false, false, false));
        add(Constants.NUMERIC_DIV, new BuiltInInfoSource(Constants.NUMERIC_DIV_NOTATION, true, false, false, false));

        add(Constants.EQUAL, new BuiltInInfoSource(Constants.EQUAL_NOTATION, true, false, true, false));
        add(Constants.STRONG_EQUAL, new BuiltInInfoSource(Constants.STRONG_EQUAL_NOTATION, true, false, true, false));
        add(Constants.INEQUAL, new BuiltInInfoSource(Constants.INEQUAL_NOTATION, true, false, true, false));
        add(Constants.STRING_EQUAL, new BuiltInInfoSource(Constants.STRING_EQUAL_NOTATION, true, false, true, false));
        add(Constants.STRING_INEQUAL, new BuiltInInfoSource(Constants.STRING_INEQUAL_NOTATION, true, false, true, false));
        add(Constants.NUMERIC_EQUAL, new BuiltInInfoSource(Constants.NUMERIC_EQUAL_NOTATION, true, false, true, false));
        add(Constants.NUMERIC_INEQUAL, new BuiltInInfoSource(Constants.NUMERIC_INEQUAL_NOTATION, true, false, true, false));
        add(Constants.LESS_THAN, new BuiltInInfoSource(Constants.LESS_THAN_NOTATION, true, false, true, false));
        add(Constants.LESS_EQUAL, new BuiltInInfoSource(Constants.LESS_EQUAL_NOTATION, true, false, true, false));
        add(Constants.GREATER_THAN, new BuiltInInfoSource(Constants.GREATER_THAN_NOTATION, true, false, true, false));
        add(Constants.GREATER_EQUAL, new BuiltInInfoSource(Constants.GREATER_EQUAL_NOTATION, true, false, true, false));

        add(WsmlDataType.WSML_STRING, new BuiltInInfoSource(WsmlDataType.WSML_STRING_NOTATION, false, true, false, true));
        add(WsmlDataType.WSML_INTEGER, new BuiltInInfoSource(WsmlDataType.WSML_INTEGER_NOTATION, false, true, false, true));
        add(WsmlDataType.WSML_DECIMAL, new BuiltInInfoSource(WsmlDataType.WSML_DECIMAL_NOTATION, false, true, false, true));
        add(WsmlDataType.WSML_IRI, new BuiltInInfoSource(WsmlDataType.WSML_IRI_NOTATION, false, true, false, true));
        add(WsmlDataType.WSML_SQNAME, new BuiltInInfoSource(WsmlDataType.WSML_SQNAME_NOTATION, false, true, false, true));
        add(WsmlDataType.WSML_FLOAT, new BuiltInInfoSource(WsmlDataType.WSML_FLOAT_NOTATION, false, true, false, true));
        add(WsmlDataType.WSML_DOUBLE, new BuiltInInfoSource(WsmlDataType.WSML_DOUBLE_NOTATION, false, true, false, true));
        add(WsmlDataType.WSML_BOOLEAN, new BuiltInInfoSource(WsmlDataType.WSML_BOOLEAN_NOTATION, false, true, false, true));
        add(WsmlDataType.WSML_DURATION, new BuiltInInfoSource(WsmlDataType.WSML_DURATION_NOTATION, false, true, false, true));
        add(WsmlDataType.WSML_DATETIME, new BuiltInInfoSource(WsmlDataType.WSML_DATETIME_NOTATION, false, true, false, true));
        add(WsmlDataType.WSML_TIME, new BuiltInInfoSource(WsmlDataType.WSML_TIME_NOTATION, false, true, false, true));
        add(WsmlDataType.WSML_DATE, new BuiltInInfoSource(WsmlDataType.WSML_DATE_NOTATION, false, true, false, true));
        add(WsmlDataType.WSML_GYEARMONTH, new BuiltInInfoSource(WsmlDataType.WSML_GYEARMONTH_NOTATION, false, true, false, true));
        add(WsmlDataType.WSML_GYEAR, new BuiltInInfoSource(WsmlDataType.WSML_GYEAR_NOTATION, false, true, false, true));
        add(WsmlDataType.WSML_GMONTHDAY, new BuiltInInfoSource(WsmlDataType.WSML_GMONTHDAY_NOTATION, false, true, false, true));
        add(WsmlDataType.WSML_GDAY, new BuiltInInfoSource(WsmlDataType.WSML_GDAY_NOTATION, false, true, false, true));
        add(WsmlDataType.WSML_GMONTH, new BuiltInInfoSource(WsmlDataType.WSML_GMONTH_NOTATION, false, true, false, true));
        add(WsmlDataType.WSML_HEXBINARY, new BuiltInInfoSource(WsmlDataType.WSML_HEXBINARY_NOTATION, false, true, false, true));
        add(WsmlDataType.WSML_BASE64BINARY, new BuiltInInfoSource(WsmlDataType.WSML_BASE64BINARY_NOTATION, false, true, false, true));
    }

    /**
     * Adds the transformation information to the hashtables
     * @param iri the IRI on the one side of the transformations
     * @param bi an instance of BuiltInInfoSource containing the notation, infix and function symbol information
     */
    private void add(String iri, BuiltInInfoSource bi) {
        iri2notation.put(iri, bi);
        notation2iri.put(bi.getNotation(), iri);
    }

    /**
     * This internal class reunites the information needed about built-in datatypes, predicates
     * and function symbols. In more detail that includes the short notation and the booleans infix
     * and fsymbol that indicate if there exists an infix notation, resp. if an construct is a predicate
     * or a function symbol
     * @author DERI Innsbruck, reto.krummenacher@deri.org
     *
     */
    private class BuiltInInfoSource {

        /**
         * The short notation
         */
        private String notation;

        private boolean isDataType;

        /**
         * builtInAtom
         */
        private boolean builtInAtom;

        /**
         * Indication if there is an infix notation
         */
        private boolean infix;

        /**
         * Indication if the construct is a function symbol
         */
        private boolean fsymb;

        protected BuiltInInfoSource(String notation, boolean infix,
                                    boolean fsymb, boolean builtInAtom, boolean isDataType) {
            this.notation = notation;
            this.infix = infix;
            this.fsymb = fsymb;
            this.builtInAtom = builtInAtom;
            this.isDataType = isDataType;
        }

        /**
         * @return true if the construct is a function symbol
         */
        protected boolean isFSymbol() {
            return fsymb;
        }

        /**
         * @return true if the construct is a built in atom
         */
        protected boolean isBuiltInAtom() {
            return builtInAtom;
        }

        /**
         * @return true if there exists an infix notation
         */
        protected boolean isInfix() {
            return infix;
        }

        /**
         * @return the short notation of a built-in element
         */
        protected String getNotation() {
            return notation;
        }

        protected boolean isDataType() {
            return isDataType;
        }
    }
}
/*
 * $Log$
 * Revision 1.5  2007/04/02 12:13:20  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.4  2005/11/09 06:58:38  holgerlausen
 * fixed bug parsing wsml#add(?x,1,2) was converted to a constructed term instead of atom
 *
 * Revision 1.3  2005/10/17 08:36:17  holgerlausen
 * fixed null pointer in unit tests
 *
 * Revision 1.2  2005/10/12 13:32:07  ohamano
 * add isSimpleDataType
 *
 * Revision 1.1  2005/09/23 07:09:51  holgerlausen
 * moved constanttransformer from API to implementation, removed dublicated constants in logicalexpression.constants
 *
 * Revision 1.7  2005/09/15 13:52:11  holgerlausen
 * allow for strong equality in parsing serializing
 *
 * Revision 1.6  2005/09/09 11:58:19  holgerlausen
 * fixed header logexp no longer extension
 *
 * Revision 1.5  2005/09/09 11:12:12  marin_dimitrov
 * formatting
 *
 * Revision 1.4  2005/09/08 15:16:26  holgerlausen
 * fixes for parsing and serializing datatypes
 *
 * Revision 1.3  2005/09/07 18:45:56  holgerlausen
 * add isDataType() support
 *
 * Revision 1.2  2005/09/07 14:43:50  holgerlausen
 * Added Explicit Support for BuiltIn Atoms and Constructed Terms
 *
 * Revision 1.1  2005/09/02 13:32:43  ohamano
 * move logicalexpression packages from ext to core
 * move tests from logicalexpression.test to test module
 *
 * Revision 1.6  2005/08/30 23:41:34  haselwanter
 * Making the compiler happy: removing unneccesarily nested clauses.
 *
 * Revision 1.5  2005/08/18 16:15:40  nathaliest
 * JavaDoc added
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
 */