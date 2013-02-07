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
package org.deri.wsmo4j.validator;

import org.omwg.logicalexpression.LogicalExpression;
import org.wsmo.common.*;
import org.wsmo.validator.LogicalExpressionError;

public class LogicalExpressionErrorImpl extends ValidationErrorImpl implements LogicalExpressionError {

    private LogicalExpression logicalexpression = null;
    
    private Entity entity = null;
    
    private String reason = null;

    
    public LogicalExpressionErrorImpl(Entity entity, LogicalExpression logexp, String reason, String variant) {
        super(entity, reason, variant);
        this.entity = entity;
        this.reason = reason;
        this.logicalexpression = logexp;
    }
    
    /**
     * @see org.wsmo.validator.LogicalExpressionError#getLogExp()
     * @return logical expression in which the violation occured
     */
    public LogicalExpression getLogExp() {
        return logicalexpression;
    }

    /**
     * Formats the String representation of the ValidationError.
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String id = "<Identifier>";
        //short id if possible:
        if (getEntity() != null) {
            id = getEntity().getIdentifier().toString();
            if (getEntity().getIdentifier()instanceof IRI) {
                id = ((IRI)getEntity().getIdentifier()).getLocalName();
            }
        }
        //short for variant if possible
        String shortVariant = getViolatesVariant();
        if (getViolatesVariant().lastIndexOf('/') != -1) {
            shortVariant =getViolatesVariant().substring(getViolatesVariant().lastIndexOf('/') + 1);
        }
        return id + "\n" + getReason() + 
            "\n(violated variant: " + shortVariant + ")";
    }
    
    /**
     * <p>
     * The <code>equals</code> method implements an equivalence relation
     * on non-null object references. ValidationWarnings are equal if their 
     * warning and quickFix messages are equal.
     * </p>
     * <p>
     * It is generally necessary to override the <code>hashCode</code> method whenever this method
     * is overridden.
     * </p>
     * @param o the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     *          argument; <code>false</code> otherwise.
     * @see java.lang.Object#equals(java.lang.Object)
     * @see java.lang.Object#hashCode()
     */
    public boolean equals(Object obj) {
        if (obj instanceof LogicalExpressionError) {
        	LogicalExpressionErrorImpl lee = (LogicalExpressionErrorImpl) obj;
            if (entity == null) {
            	return super.equals(obj);
            }
            else {
            	return (lee.getReason().equals(reason)
            			&& lee.getEntity().equals(entity));
            }
        }
        return false;
    }
 
    /**
     * <p>
     * If two objects are equal according to the <code>equals(Object)</code> method, then calling
     * the <code>hashCode</code> method on each of the two objects must produce the same integer
     * result. However, it is not required that if two objects are unequal according to
     * the <code>equals(Object)</code> method, then calling the <code>hashCode</code> method on each of the two
     * objects must produce distinct integer results.
     * </p>
     * <p>
     * This method should be overriden, when the <code>equals(Object)</code> method is overriden.
     * </p>
     * @return A hash code value for this Object.
     * @see java.lang.Object#hashCode()
     * @see java.lang.Object#equals(Object)
     */
    public int hashCode() {
        return super.hashCode();
    }
    
}
