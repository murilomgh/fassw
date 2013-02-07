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

package com.ontotext.wsmo4j.common;


/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */

import java.io.Serializable;

import org.omwg.logicalexpression.terms.Visitor;
import org.wsmo.common.UnnumberedAnonymousID;


/**
 * An interface representing an anonymous unnumbered identifier
 *
 * @author not attributable
 * @version $Revision: 1.4 $ $Date: 2005/09/16 14:02:44 $
 * @since 0.4.0
 */
public class UnnumberedAnonymousIDImpl
    implements Serializable, UnnumberedAnonymousID {
	
    public UnnumberedAnonymousIDImpl() {
    }

    public String toString() {
        return "_#";
    }
    
    /**
     * @see org.omwg.logicalexpression.terms.Term#accept(org.omwg.logicalexpression.terms.Visitor)
     */
    public void accept(Visitor v) {
        v.visitUnnumberedID(this);
    }
}

/*
 * $Log: UnnumberedAnonymousIDImpl.java,v $
 * Revision 1.4  2005/09/16 14:02:44  alex_simov
 * Identifier.asString() removed, use Object.toString() instead
 * (Implementations MUST override toString())
 *
 * Revision 1.3  2005/09/07 07:19:33  holgerlausen
 * equals and hascode have been violating java invariants >> removed.
 *
 * Revision 1.2  2005/09/02 13:32:44  ohamano
 * move logicalexpression packages from ext to core
 * move tests from logicalexpression.test to test module
 *
 * Revision 1.1  2005/09/02 09:43:32  ohamano
 * integrate wsmo-api and le-api on api and reference implementation level; full parser, serializer and validator integration will be next step
 *
 * Revision 1.2  2005/06/22 14:20:15  alex_simov
 * Copyright header added/updated
 *
 * Revision 1.1  2005/06/01 12:00:32  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.4  2005/05/30 15:06:37  alex
 * toString() delegates to asString()
 *
 * Revision 1.3  2005/05/19 13:48:03  marin
 * fixed (_# prefix)
 *
 * Revision 1.2  2005/05/19 12:50:21  marin
 * -
 *
 * Revision 1.1  2005/05/19 12:42:56  marin
 * created
 *
 */
