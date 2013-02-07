/*
 wsmo4j - a WSMO API and Reference Implementation
 Copyright (c) 2006, DERI Innsbruck
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


import org.deri.wsmo4j.io.serializer.wsml.LogExprSerializerWSML;
import org.omwg.logicalexpression.LogicalExpression;
import org.wsmo.common.TopEntity;


/**
 * This class reunites all Logical Expression
 * @author DERI Innsbruck, holger.lausen@deri.org
 * @version $Revision: 1.1 $ $Date: 2006/11/17 15:07:59 $
 */
public abstract class LogicalExpressionImpl
        implements LogicalExpression {
    
    private String orgString;
    
    public void setStringRepresentation (String le){
        orgString=le;
    }
    
    public String getStringRepresentation (){
        return orgString;
    }

    /**
     * @return The String representation of the logical expression
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new LogExprSerializerWSML(null).serialize(this);
    }
    
    /**
     * Returns String representation of a logical expression.
     * @return the string representation of the Logical expression
     * @param nsHolder namespace container used to abbreviate IRIs to 
     * sQNames.
     */
    public String toString(TopEntity nsHolder) {
        return new LogExprSerializerWSML(nsHolder).serialize(this);
    }
}