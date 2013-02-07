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
import org.wsmo.validator.*;

/**
 * Validation Warning
 *
 * <pre>
 * Created on 18.11.2005
 * Committed by $Author$
 * $Source$,
 * </pre>
 *
 * @author Holger Lausen (holger.lausen@deri.org)
 *
 * @version $Revision$ $Date$
 */
public class ValidationWarningImpl extends ValidationMessageImpl
        implements ValidationWarning {
    
    private String quickFix = null;
    
    private String warning = null;
    
    public ValidationWarningImpl(Entity entity, String warning) {
        this(entity, warning, null);
    }
    
    public ValidationWarningImpl(Entity entity , String warning, String quickFix){
        super(entity,warning);
        this.warning = warning;
        this.quickFix=quickFix;
    }
    
    public String toString(){
        if (quickFix != null) {
            return super.toString() + "\n Suggestion: " + quickFix;
        }
        else {
            return super.toString();
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
        if (obj instanceof ValidationWarning) {
            ValidationWarningImpl vw = (ValidationWarningImpl) obj;
            return ((vw.getReason().equals(warning)
                     && vw.getQuickFix().equals(quickFix)));
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
 *$Log$
 *Revision 1.6  2006/08/22 16:22:26  nathaliest
 *ameliorated the string output of validator errors and warnings
 *
 *Revision 1.5  2006/01/16 13:32:46  nathaliest
 *added ValidationMessage to wsmo-api
 *
 *Revision 1.4  2006/01/12 14:39:34  alex_simov
 *a null-check performed in toString()
 *
 *Revision 1.3  2006/01/09 16:12:58  nathaliest
 *added quickFix method to validationError
 *
 *Revision 1.2  2005/12/15 16:47:27  nathaliest
 *check for double error and warning messages
 *
 *Revision 1.1  2005/11/21 13:08:47  holgerlausen
 *added ValidationWarning
 *
 */