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


import org.omwg.logicalexpression.*;


/**
 * Utility to check if a specific operator is included in a logical expression.
 *
 * <pre>
 * Created on Aug 2, 2005
 * Committed by $Author: marin_dimitrov $
 * </pre>
 *
 * @author Holger Lausen (holger.lausen@deri.org)
 *
 * @version $Revision: 1.3 $ $Date: 2005/09/09 15:51:42 $
 */
public class ExpressionContainmentUtil {

    /**
     * Checks if a specific operator is contained within a logical expression.
     * @param logexp logical expression that will be checked
     * @param operator operator that will be checked for (e.g. CompoundExpression.AND)
     * @return true if an compound expression with specified operator is contained.
     */
    public static boolean contains(LogicalExpression logexp, Class operator) {
        return contains(logexp, new Class[] {operator});
    }

    /**
     * Checks if a specific operator is contained within a logical expression.
     * @param logexp logical expression that will be checked
     * @param operators array of operators that will be checked for
     *        (e.g. new int[] {CompoundExpression.AND,CompoundExpression.OR)
     * @return true if an compound expression with specified operator is contained.
     */
    public static boolean contains(LogicalExpression logexp, Class[] operators) {
        VisitorContainsCheck vCC = new VisitorContainsCheck(operators);
        logexp.accept(vCC);
        return vCC.isContained();
    }

}
