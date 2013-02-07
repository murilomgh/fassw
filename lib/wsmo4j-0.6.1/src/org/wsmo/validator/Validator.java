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


package org.wsmo.validator;


import java.util.*;
import org.wsmo.common.*;


/**
 * An interface representing validator for WSMO entities.
 * The validator may perform specific model checks to ensure that the entity definition is
 * compliant with the respective WSML variant
 *
 * @author not attributable
 * @version $Revision: 1.3 $ $Date: 2007/04/02 12:13:16 $
 * @since 0.4.0
 */
public interface Validator {

    /**
     * Checks whether a WSMO element definition is valid. The notion of "validity" is dependent only
     * on the specific Validator implementation (f.e. a validity check may be whether a definition is
     * compliant with a specific WSML variant)
     * @param te the TopEntity to be validated (ontology, mediator, goal,...)
     * @param errorMessages a collection where the validation specific error messages will be added (if any)
     * @param warningMessages a collection where the validation specific warning messages will be added (if any)
     * @return valid/invalid
     * @see org.omwg.ontology.Ontology
     */
    boolean isValid(TopEntity te, List <ValidationError> errorMessages, List <ValidationWarning> warningMessages);
}

/*
 * $Log: Validator.java,v $
 * Revision 1.3  2007/04/02 12:13:16  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.2  2005/11/15 16:49:45  nathaliest
 * added validation warning to the interface
 *
 * Revision 1.1  2005/09/09 15:45:06  marin_dimitrov
 * moved from org.wsmo.common
 *
 * Revision 1.1  2005/06/01 10:30:24  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.5  2005/05/12 14:42:57  marin
 * added @since tags
 *
 * Revision 1.4  2005/05/12 13:29:16  marin
 * javadoc, header, footer, etc
 *
 */
