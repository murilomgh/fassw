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

import java.util.Vector;

import org.deri.wsmo4j.validator.WsmlDLValidator;
import org.omwg.logicalexpression.LogicalExpression;
import org.omwg.ontology.Variable;
import org.wsmo.factory.Factory;
import org.wsmo.validator.ValidationError;
import org.wsmo.validator.ValidationWarning;

/**
 * Utility class to extract the root/s variable/s from a WSML-DL 
 * logical expression.
 * 
 * <pre>
 * Created on Sep 27, 2006
 * Committed by $Author: morcen $
 * $Source: /cvsroot/wsmo4j/wsmo4j/org/deri/wsmo4j/logicalexpression/util/GetRootUtil.java,v $,
 * </pre>
 * 
 * @author Nathalie Steinmetz, DERI Innsbruck
 * @version $Revision: 1.3 $ $Date: 2007/04/02 12:13:23 $
 */
public class GetRootUtil {

	public GetRootUtil() {
		
	}
	
	/**
	 * Validates a given formula and gets the root variable if it is a valid 
	 * WSML-DL formula.
	 * 
	 * @param logExpr
	 * @return root Variable or null, if there is no root variable or if the 
	 * 		   formula is not valid WSML-DL
	 */
	public Variable getRootVariable(LogicalExpression logExpr) {
		WsmlDLValidator validator = new WsmlDLValidator(
				Factory.createLogicalExpressionFactory(null));
		boolean valid = validator.isValid(logExpr, new Vector <ValidationError>(), new Vector <ValidationWarning>());
		if (valid) {
			return validator.getRootVariable(logExpr);
		}
		else {
			return null;
		}
	}
	
}
/*
 * $Log: GetRootUtil.java,v $
 * Revision 1.3  2007/04/02 12:13:23  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.2  2006/09/28 14:45:10  nathaliest
 * *** empty log message ***
 *
 * Revision 1.1  2006/09/28 13:11:34  nathaliest
 * added utility method to geet root variable of valid WSML-DL formula and added corresponding tests
 *
 * 
 */