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

import org.omwg.ontology.Attribute;
import org.wsmo.common.*;
import org.wsmo.validator.AttributeError;

public class AttributeErrorImpl extends ValidationErrorImpl implements AttributeError {

    private Attribute attribute = null;

    
    public AttributeErrorImpl(Entity entity, Attribute attribute, String reason, String variant) {
        super(entity, reason, variant);
        this.attribute = attribute;
    }
    
    /**
     * 
     * @see org.wsmo.validator.AttributeError#getAttribute()
     * @return String representation of the attribute in which the violation occured
     */
    public Attribute getAttribute() {
        return attribute;
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
            shortVariant = getViolatesVariant().substring(getViolatesVariant().lastIndexOf('/') + 1);
        }
        return id + ":\n" + getReason() + "\n(violated variant: " +
                shortVariant + ")";
    }

}
