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
import org.wsmo.validator.ValidationMessage;


/**
 * Gives Structure to a validation message.
 *
 * <pre>
 *  Created on Jul 28, 2005
 *  Committed by $Author: nathaliest $
 *  $Source: /cvsroot/wsmo4j/wsmo4j/org/deri/wsmo4j/validator/ValidationMessageImpl.java,v $,
 * </pre>
 *
 * @author Holger Lausen
 * @author nathalie.steinmetz@deri.org
 * @see org.wsmo.validator.ValidationError
 * @see org.wsmo.validator.ValidationWarning
 * @version $Revision: 1.1 $ $Date: 2006/01/16 13:32:46 $
 */
public abstract class ValidationMessageImpl implements ValidationMessage{

    private Entity entity = null;

    private String reason = null;
    
    private String quickFix = null;

    public ValidationMessageImpl(Entity entity, String reason) {
        this(entity, reason, null);
    }

    public ValidationMessageImpl(Entity entity, String reason, String quickFix) {
        this.entity = entity;
        this.reason = reason;
        this.quickFix = quickFix;
    }
    
    /**
     * 
     * @see org.wsmo.validator.ValidationError#getEntity()
     * @return the entity in which the violation occured
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * @see org.wsmo.validator.ValidationError#getReason()
     * @return String representation of error in Entity
     */
    public String getReason() {
        return reason;
    }

    /**
     * @see org.wsmo.validator.ValidationError#getQuickFix()
     * @see org.wsmo.validator.ValidationWarning#getQuickFix()
     * @return String representation of a quick fix possibility
     */
    public String getQuickFix() {
        if (quickFix == null) {
            quickFix = "";
        }
        return quickFix;
    }
    
    /**
     * Formats the String representation of the ValidationError.
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String id = "<Identifier>";

        //short id if possible:
        if (entity != null) {
            id = entity.getIdentifier().toString();
            if (entity.getIdentifier()instanceof IRI) {
                id = ((IRI)entity.getIdentifier()).getLocalName();
            }
        }
        return id + ":\n" + reason;
    }
}
/*
 * $Log: ValidationMessageImpl.java,v $
 * Revision 1.1  2006/01/16 13:32:46  nathaliest
 * added ValidationMessage to wsmo-api
 *
 * Revision 1.3  2006/01/09 16:12:58  nathaliest
 * added quickFix method to validationError
 *
 * Revision 1.2  2005/11/22 16:19:32  nathaliest
 * organized imports
 *
 * Revision 1.1  2005/11/21 13:08:47  holgerlausen
 * added ValidationWarning
 *
 */