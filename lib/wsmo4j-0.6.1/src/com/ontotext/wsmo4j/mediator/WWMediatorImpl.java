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

package com.ontotext.wsmo4j.mediator;

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */

import org.wsmo.common.*;
import org.wsmo.mediator.WWMediator;

public class WWMediatorImpl extends MediatorImpl
        implements WWMediator {

    public WWMediatorImpl(IRI mediatorIRI) {
        super(mediatorIRI);
    }

    public boolean equals(Object object) {
        if (object == null 
                || false == object instanceof WWMediator) {
            return false;
        }
        return super.equals(object);
    }
}

/*
 * $Log: WWMediatorImpl.java,v $
 * Revision 1.12  2006/02/13 10:41:04  vassil_momtchev
 * the constructors of the topentities to disallow Identifier; see WsmoFactoryImpl
 *
 * Revision 1.11  2005/06/01 12:07:31  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.1  2005/05/13 14:05:49  alex
 * initial commit
 *
 */
