/*
 wsmo4j extension - a Choreography API and Reference Implementation

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

package org.wsmo.service.rule;

import org.omwg.logicalexpression.LogicalExpression;
import org.wsmo.common.exception.*;

/**
 * Condition is a sub-class of LogicalExpression and defines the condition under
 * which a rule should be fired or not
 * 
 * @author James Scicluna
 * @author Thomas Haselwanter
 * @author Holger Lausen
 * @author Vassil Momtchev
 * 
 * Created on 17-Oct-2005 Committed by $Author: vassil_momtchev $
 * 
 * $Source: /cvsroot/wsmo4j/ext/choreography/src/api/org/wsmo/service/rule/Condition.java,v $,
 * @version $Revision: 1.1 $ $Date: 2006/10/24 14:11:48 $
 */

public interface Condition {

    public LogicalExpression getRestrictedLogicalExpression();
    
    public void setRestrictedLogicalExpression(LogicalExpression le) throws InvalidModelException;
	
}