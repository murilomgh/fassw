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


import org.wsmo.common.*;
import org.wsmo.validator.ValidationError;


/**
 * Gives Structure to a validation error.
 *
 * <pre>
 *  Created on Jul 28, 2005
 *  Committed by $Author: nathaliest $
 *  $Source: /cvsroot/wsmo4j/wsmo4j/org/deri/wsmo4j/validator/ValidationErrorImpl.java,v $,
 * </pre>
 *
 * @author Holger Lausen
 * @author nathalie.steinmetz@deri.org
 * @see org.wsmo.validator.ValidationError
 * @version $Revision: 1.7 $ $Date: 2006/08/22 16:22:26 $
 */
public class ValidationErrorImpl extends ValidationMessageImpl implements ValidationError{

    private String variant = null;
    
    private String reason = null;

    private String quickFix = null;
    
    public ValidationErrorImpl(Entity entity, String reason, String variant) {
        this(entity, reason, variant, null);
    }

    public ValidationErrorImpl(Entity entity, String reason, String variant, String quickFix) {
        super(entity,reason);
        this.reason = reason;
        if (!variant.equals(WSML.WSML_CORE)
                && !variant.equals(WSML.WSML_DL)
                && !variant.equals(WSML.WSML_FULL)
                && !variant.equals(WSML.WSML_FLIGHT)
                && !variant.equals(WSML.WSML_RULE)) {
                throw new IllegalArgumentException(
                        "Invalid WSML Variant specified:" + variant);
            }
            this.variant = variant;
            this.quickFix = quickFix;
    }
    
    /**
     * @see org.wsmo.validator.ValidationError#getViolatesVariant()
     * @return String representation of URI representing the variant that the
     *         error violates
     */
    public String getViolatesVariant() {
        return variant;
    }

    /**
     * Formats the String representation of the ValidationError.
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        //short for variant if possible
        String shortVariant = variant;
        if (variant.lastIndexOf('/') != -1) {
            shortVariant = variant.substring(variant.lastIndexOf('/') + 1);
        }
        if (quickFix != null) {
            return super.toString() + "\n(Violated variant: " +
                    shortVariant + ")" + "\n Suggestion: " + quickFix;
        }
        else {
            return super.toString() + "\n(Violated variant: " +
                    shortVariant + ")";
        }
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
        if (obj instanceof ValidationError) {
        	if (quickFix == null) {
        		quickFix = "";
        	}
            ValidationErrorImpl vw = (ValidationErrorImpl) obj;
            return (vw.getReason().equals(reason)
                    && vw.getQuickFix().equals(quickFix));
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
/*
 * $Log: ValidationErrorImpl.java,v $
 * Revision 1.7  2006/08/22 16:22:26  nathaliest
 * ameliorated the string output of validator errors and warnings
 *
 * Revision 1.6  2006/08/21 10:44:47  nathaliest
 * fixed problem causing duplicate error messages
 *
 * Revision 1.5  2006/01/16 13:32:46  nathaliest
 * added ValidationMessage to wsmo-api
 *
 * Revision 1.4  2006/01/09 16:12:58  nathaliest
 * added quickFix method to validationError
 *
 * Revision 1.3  2005/12/15 16:47:27  nathaliest
 * check for double error and warning messages
 *
 * Revision 1.2  2005/11/21 13:08:47  holgerlausen
 * added ValidationWarning
 *
 * Revision 1.1  2005/11/15 16:54:37  nathaliest
 * created validation error implementations
 *
 * Revision 1.13  2005/11/08 17:46:10  nathaliest
 * changed error messages
 *
 * Revision 1.12  2005/10/24 10:47:55  holgerlausen
 * added metamodelling error for core / dl
 * fixed checkDatatype
 * fixed ofType bug
 * fixed bug in relation parameter checks
 * fixed null pointer when calling validator without errorlist
 *
 * Revision 1.11  2005/09/23 12:06:32  holgerlausen
 * moved wsml varinat constructs to common.wsml
 *
 * Revision 1.10  2005/09/21 08:15:39  holgerlausen
 * fixing java doc, removing asString()
 *
 * Revision 1.9  2005/09/12 16:02:00  nathaliest
 * added error codes
 *
 * Revision 1.8  2005/09/11 22:11:19  nathaliest
 * added error codes
 *
 * Revision 1.7  2005/09/09 15:51:42  marin_dimitrov
 * formatting
 *
 * Revision 1.6  2005/09/09 15:46:48  marin_dimitrov
 * validators moved from org.wsmo.common to org.wsmo.validator
 *
 * Revision 1.5  2005/09/09 15:23:04  nathaliest
 * changed WsmlValidator imports
 *
 * Revision 1.4  2005/09/09 14:39:05  nathaliest
 * changed wsmlvalidator import, new error code and changed toString method
 *
 * Revision 1.3  2005/09/08 17:09:23  nathaliest
 * changed error codes
 *
 * Revision 1.2  2005/09/07 06:57:14  holgerlausen
 * better readable output of toString
 *
 */