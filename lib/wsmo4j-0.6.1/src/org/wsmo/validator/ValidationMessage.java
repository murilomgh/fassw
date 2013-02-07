package org.wsmo.validator;

import org.wsmo.common.Entity;

/**
 * Gives Structure to a validation message.
 *
 * <pre>
 *  Created on January 16, 2006
 *  Committed by $Author: nathaliest $
 *  $Source: /cvsroot/wsmo4j/wsmo-api/org/wsmo/validator/ValidationMessage.java,v $,
 * </pre>
 *
 * @author nathalie.steinmetz@deri.org
 * @version $Revision: 1.1 $ $Date: 2006/01/16 13:32:46 $
 */
public interface ValidationMessage {

    /**
     * Returns the entity in which the violatin occured
     * 
     * @return an entity
     */
    public Entity getEntity();
    
    
    /**
     * Returns the String representation of the error in Entity
     * 
     * @return a String
     */
    public String getReason();

    
    
    /**
     * Returns the String representation of the quick Fix possibility.
     * 
     * @return a String
     */
    public String getQuickFix();
    
}
