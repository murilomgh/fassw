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

import org.omwg.logicalexpression.terms.Term;


/**
 * Data Value reoresents wsml data values (correspond to xml data values).
 * 
 * The following is a table of the java binding for wsml data values:
 * <pre>
 * _string         _string("any-character*")                           java.lang.String    
 * _decimal        _decimal("'-'?numeric+.numeric+")                   java.math.BigDecimal    
 * _integer        _integer("'-'?numeric+")                            java.math.BigInteger    
 * _float          _float("see XML Schema document")                   java.lang.Float 
 * _double         _double("see XML Schema document")                  java.lang.Double    
 * _iri            _iri("iri-according-to-rfc3987")                    java.lang.String    
 * _sqname         _sqname("iri-rfc3987", "localname")                 java.lang.String[]  
 * _boolean        _boolean("true-or-false")                           java.lang.Boolean   
 * _duration       _duration(year, month, day, hour, minute, second)   java.lang.String    
 * _dateTime       _dateTime(year, month, day, hour, minute, second, timezone-hour, timezone-minute)
 *                 _dateTime(year, month, day, hour, minute, second)   java.util.Calendar  
 * _time           _time(hour, minute, second, timezone-hour, timezone-minute) 
 *                 _time(hour, minute, second)                         java.util.Calendar  
 * _date           _date(year, month, day, timezone-hour, timezone-minute) 
 *                 _date(year, month, day)                             java.util.Calendar
 * _gyearmonth     _gyearmonth(year, month)                            java.lang.Integer[] 
 * _gyear          _gyear(year)    java.lang.Integer   
 * _gmonthday      _gmonthday(month, day)                              java.lang.Integer[] 
 * _gday           _gday(day)                                          java.lang.Integer   
 * _gmonth         _gmonth(month)                                      java.lang.Integer   
 * _hexbinary      _hexbinary(hexadecimal-encoding)                    java.lang.String    
 * _base64binary   _base64binary(hexadecimal-encoding)                 java.lang.String    
 * </pre>
 * <p>Note that for the Calendar types you one can for the mapping only rely on the fields 
 * given by the corresponding wsml value, e.g. if you ask the Calendar object for Calendar.hour 
 * that you got from a _wsml#date value, the API provides no gurantue
 * on the return value.</p>
 * <pre>
 * Created on Sep 6, 2005
 * Committed by $Author: holgerlausen $
 * $Source: /cvsroot/wsmo4j/wsmo-api/org/omwg/ontology/DataValue.java,v $,
 * </pre>
 *
 * @author Holger Lausen (holger.lausen@deri.org)
 *
 * @see org.omwg.ontology.ComplexDataValue
 * @see org.omwg.ontology.SimpleDataValue
 * @version $Revision: 1.9 $ $Date: 2005/09/21 08:15:39 $
 */
public interface DataValue extends Value, Term {

    WsmlDataType getType();

    Object getValue();

    /**
     * @supplierCardinality 1
     * @supplierRole has-type
     * @directed
     */
    /*# WsmlDataType lnkWsmlDataType; */
}

/*
 * $Log: DataValue.java,v $
 * Revision 1.9  2005/09/21 08:15:39  holgerlausen
 * fixing java doc, removing asString()
 *
 * Revision 1.8  2005/09/06 18:23:53  holgerlausen
 * removed createSimpleTypes from LogicalExpressionFactory
 * splited DataValue into simple and complex value classes
 * removed explicit classes for simple datavalues (now all org.omwg.ontology.SimpledataValue)
 * adopted Term visitor to new data structure
 *
 * Revision 1.7  2005/09/02 13:32:43  ohamano
 * move logicalexpression packages from ext to core
 * move tests from logicalexpression.test to test module
 *
 * Revision 1.6  2005/09/02 09:43:28  ohamano
 * integrate wsmo-api and le-api on api and reference implementation level; full parser, serializer and validator integration will be next step
 *
 * Revision 1.5  2005/08/19 08:59:38  marin_dimitrov
 * -
 *
 */
