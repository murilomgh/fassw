/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2005 University of Innsbruck, Austria
               2005 National University of Ireland, Galway

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


import java.util.*;


/**
 * Representing Compound Expressions.
 *
 * <pre>
 * Created on Sep 7, 2005
 * Committed by $Author$
 * $Source$,
 * </pre>
 *
 * @author Holger Lausen (holger.lausen@deri.org)
 *
 * @version $Revision$ $Date$
 */
public interface CompoundExpression
        extends LogicalExpression {
    /**
     *
     * @return returns a list of all operands grouped by a Compound Expression
     */
    public List <LogicalExpression> listOperands();
    
    /**
     * 
     * @param operands sets the operands of the compound expression
     */
    public void setOperands(List <LogicalExpression> operands);
}
/*
 * $Log$
 * Revision 1.5  2007/04/02 12:13:15  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.4  2005/09/21 06:31:55  holgerlausen
 * allowing to set arguments rfe  1290049
 *
 * Revision 1.3  2005/09/09 11:46:48  holgerlausen
 * fixed license error MIT > LGPL
 *
 */

