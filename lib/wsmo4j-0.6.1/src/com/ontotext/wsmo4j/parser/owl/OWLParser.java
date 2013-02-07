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
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.wsmo.common.TopEntity;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.factory.DataFactory;
import org.wsmo.factory.Factory;
import org.wsmo.factory.LogicalExpressionFactory;
import org.wsmo.factory.WsmoFactory;
import org.wsmo.wsml.Parser;
import org.wsmo.wsml.ParserException;


/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation - OWL parser implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */
public class OWLParser implements Parser {

	WsmoFactory _factory;
	LogicalExpressionFactory _leFactory;
	DataFactory _dataFactory;

	public OWLParser(Map params) {
		if (null == (_factory = (WsmoFactory)params.get(Factory.WSMO_FACTORY)))
			_factory = Factory.createWsmoFactory(null);

		if (null == (_leFactory = (LogicalExpressionFactory)params.get(Factory.LE_FACTORY)))
			_leFactory = Factory.createLogicalExpressionFactory(null);

		if (null == (_dataFactory = (DataFactory)params.get(Factory.DATA_FACTORY)))
			_dataFactory = Factory.createDataFactory(null);

	}
	/* (non-Javadoc)
	 * @see org.wsmo.io.parser.Parser#parse(java.io.Reader)
	 */
	public TopEntity[] parse(Reader src) throws IOException, ParserException,
			InvalidModelException {
		try {
			return new TopEntity[]{new WSMLFromOWL(_factory, _leFactory, _dataFactory).process(src, null)};
		} catch (Exception e) {
			throw new ParserException("on importing OWL", e);
		}
	}
	/* (non-Javadoc)
	 * @see org.wsmo.io.parser.Parser#parse(java.lang.StringBuffer)
	 */
	public TopEntity[] parse(StringBuffer src) throws ParserException,
			InvalidModelException {
        StringReader r = new StringReader(src.toString());
        try {
            return parse(r);
        }
        catch (IOException e) {
            throw new ParserException("IOException caught", e);
        }
	}
	/* (non-Javadoc)
	 * @see org.wsmo.wsml.Parser#parse(java.io.Reader, java.util.Map)
	 */
	public TopEntity[] parse(Reader src, Map options) throws IOException, ParserException, InvalidModelException {
		// TODO Auto-generated method stub
		return parse(src);
	}
	/* (non-Javadoc)
	 * @see org.wsmo.wsml.Parser#parse(java.lang.StringBuffer, java.util.Map)
	 */
	public TopEntity[] parse(StringBuffer src, Map options) throws ParserException, InvalidModelException {
		// TODO Auto-generated method stub
		return parse(src);
	}
    /* (non-Javadoc)
     * @see org.wsmo.wsml.Parser#listKeywords())
     */
    public Set <String> listKeywords() {
        return new HashSet <String>();
    }
	
    /*
     *  (non-Javadoc)
     * @see org.wsmo.wsml.Parser#getWarnings()
     */
    public List <Object> getWarnings() {
		throw new UnsupportedOperationException("This method is not implemented for OWL parsing");
	}
    
    /*
     *  (non-Javadoc)
     * @see org.wsmo.wsml.Parser#getErrors()
     */
	public List <Object> getErrors() {
		throw new UnsupportedOperationException("This method is not implemented for OWL parsing");
	}
}

/*
 * $Log: OWLParser.java,v $
 * Revision 1.7  2007/04/02 12:13:27  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.6  2006/11/10 11:08:54  nathaliest
 * added getWarnings() and getErrors() methods to Parser interface, implemented them in the rdf parser implementation and added UnsupportedOperationException to the other parser implementations
 *
 * Revision 1.5  2006/01/11 13:03:03  marin_dimitrov
 * common constants moved to Factory
 *
/*
 * Revision 1.4  2005/12/14 09:54:07  vassil_momtchev
/*
 * changed all const from IRIto String [Constants, OWLConstants, WSMLFromOWL] - no more wsmo4j constructors invoked!
/*
 * organized imports to use com.ontotext.* instead to list all used types (see the rest of code and the code convetion)
/*
 * commented all non used local variables (all warnings removed)
/*
 *
*/

