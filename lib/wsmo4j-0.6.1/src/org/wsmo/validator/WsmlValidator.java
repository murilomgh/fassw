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

package org.wsmo.validator;


import java.util.*;

import org.omwg.logicalexpression.LogicalExpression;
import org.wsmo.common.*;



/**
 * Extends the interface of wsmo-api with necessary properties needed during
 * implementation (might be merged with wsmo-api later).
 * <p>Checks only for standard datatypes. </p>
 *
 * <pre>
 *  Created on Aug 18, 2005
 *  Committed by $Author: morcen $
 *  $Source: /cvsroot/wsmo4j/wsmo-api/org/wsmo/validator/WsmlValidator.java,v $,
 * </pre>
 *
 * @author Holger Lausen (holger.lausen@deri.org)
 * @author Nathalie Steinmetz
 * @see org.wsmo.validator.Validator
 * @version $Revision: 1.11 $ $Date: 2007/04/02 12:13:15 $
 */
public interface WsmlValidator extends Validator{

	public static final String VALIDATE_IMPORTS = "Validate imports";
	
    /**
     * Checks whether a WSMO element definition is valid. The notion of
     * "validity" is dependent only on the specific Validator implementation
     * (f.e. a validity check may be whether a definition is compliant with a
     * specific WSML variant)
     *
     * @param te
     *            the TopEntity to be validated (ontology, mediator, goal,...)
     * @param variant
     *            the Variant for which should be checked
     * @param errorMessages
     *            a collection where the validation specific error messages will
     *            be added (if any)
     * @param warningMessages
     *            a collection where the validation specific warning messages will
     *            be added (if any)
     * @return valid/invalid
     * @see org.omwg.ontology.Ontology
     */
    boolean isValid(TopEntity te, String variant, List <ValidationError> errorMessages, List <ValidationWarning> warningMessages);
    
    /**
     * Checks whether a logical expression is valid. The notion of
     * "validity" is dependent only on the specific Validator implementation
     * (f.e. a validity check may be whether a definition is compliant with a
     * specific WSML variant)
     *
     * @param logExpr
     *            the logical expression to be validated
     * @param variant
     *            the Variant for which should be checked
     * @param errorMessages
     *            a collection where the validation specific error messages will
     *            be added (if any)
     * @param warningMessages
     *            a collection where the validation specific warning messages will
     *            be added (if any)
     * @return valid/invalid
     * @see org.omwg.logicalexpression.LogicalExpression
     */
    boolean isValid(LogicalExpression logExpr, String variant, List <ValidationError> errorMessages, List <ValidationWarning> warningMessages);
    
    /**
     * Determines the Variant of a topEntity
     *
     * @param te
     *            topEntity to be checked.
     * @param errorMessages
     *            a collection where the validation specific error messages will
     *            be added (if any)
     * @param warningMessages
     *            a collection where the validation specific warning messages will
     *            be added (if any)
     * @return The IRI of variant that te is in, null if not valid wsml full
     */
    String determineVariant(TopEntity te, List <ValidationError> errorMessages, List <ValidationWarning> warningMessages);
    
    /**
     * Determines the Variant of a logical expression
     *
     * @param logExpr
     *            logical expression to be checked.
     * @param errorMessages
     *            a collection where the validation specific error messages will
     *            be added (if any)
     * @param warningMessages
     *            a collection where the validation specific warning messages will
     *            be added (if any)
     * @return The IRI of variant that logExpr is in, null if not valid wsml full
     */
    String determineVariant(LogicalExpression logExpr, List <ValidationError> errorMessages, List <ValidationWarning> warningMessages);
    
}

/*
 * $Log: WsmlValidator.java,v $
 * Revision 1.11  2007/04/02 12:13:15  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.10  2006/09/20 09:13:04  nathaliest
 * added validator methods for logical expressions
 *
 * Revision 1.9  2006/08/22 16:26:53  nathaliest
 * added support for validating imported ontologies
 *
 * Revision 1.8  2006/01/11 13:01:45  marin_dimitrov
 * common constants moved to Factory
 *
 * Revision 1.7  2006/01/09 16:16:43  nathaliest
 * deleted duplicate entry from wsmo4j.properties and renamed the parser_le_factory and the parser_wsmo_factory to le_factory and wsmo_factory.
 *
 * Revision 1.6  2006/01/05 14:57:48  nathaliest
 * Validator uses leFactory taken in constructor from properties map
 *
 * Revision 1.5  2005/11/15 16:49:45  nathaliest
 * added validation warning to the interface
 *
 * Revision 1.4  2005/09/23 12:06:32  holgerlausen
 * moved wsml varinat constructs to common.wsml
 *
 * Revision 1.3  2005/09/21 08:15:39  holgerlausen
 * fixing java doc, removing asString()
 *
 * Revision 1.2  2005/09/11 22:10:20  nathaliest
 * changed method to determine variant and added doc
 *
 * Revision 1.1  2005/09/09 15:45:06  marin_dimitrov
 * moved from org.wsmo.common
 *
 * Revision 1.3  2005/09/09 15:22:21  nathaliest
 * let WsmlValidator extend Validator
 *
 * Revision 1.2  2005/09/09 10:48:51  marin_dimitrov
 * formatting
 *
 */
