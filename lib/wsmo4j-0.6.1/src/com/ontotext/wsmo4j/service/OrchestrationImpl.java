/*
 wsmo4j - a WSMO API and Reference Implementation
 
 Copyright (c) 2004-2005, OntoText Lab. / SIRMA
 
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

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 */

package com.ontotext.wsmo4j.service;


import org.wsmo.common.*;

import com.ontotext.wsmo4j.common.TopEntityImpl;
import org.wsmo.service.Orchestration;

/**
 * This interface represents the orchestration
 * of a web service.
 *
 * @author not attributable
 * @version $Revision: 1.12 $ $Date: 2005/06/01 12:14:23 $
 */
public class OrchestrationImpl extends TopEntityImpl
        implements Orchestration {
    
    public OrchestrationImpl(Identifier id) {
        super(id);
    }
    
    public boolean equals(Object object) {
        if (object == null 
                || false == object instanceof Orchestration) {
            return false;
        }
        return super.equals(object);
    }
}

/*
* $Log: OrchestrationImpl.java,v $
* Revision 1.12  2005/06/01 12:14:23  marin_dimitrov
* v0.4.0
*
* Revision 1.1  2005/05/13 09:27:03  alex
* initial commit
*
*/
