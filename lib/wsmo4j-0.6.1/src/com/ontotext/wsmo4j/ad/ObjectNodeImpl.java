/*
 AD extension - a WSMO API and Reference Implementation

 Copyright (c) 2004-2006, OntoText Lab. / SIRMA

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

package com.ontotext.wsmo4j.ad;

import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.service.ad.*;
import org.wsmo.service.signature.*;

import com.ontotext.wsmo4j.common.*;

public class ObjectNodeImpl extends EntityImpl implements ObjectNode {

    private Mode mode;
    private Concept concept;

    public ObjectNodeImpl(Identifier id) {
        super(id);
    }

    public Mode getMode() {
        return this.mode;
    }

    public void setMode(Mode mode) {
        if (mode == null) {
            throw new IllegalArgumentException();
        }
        this.mode = mode;
    }

    public Concept getCarriesConcept() {
        return this.concept;
    }

    public void setCarriesConcept(Concept concept) {
        if (concept == null) {
            throw new IllegalArgumentException();
        }
        this.concept = concept;
    }

    // TODO: (Alex) instanceof check is not sufficient enough /Pin and ObjectNode are not distinguished
    public boolean equals(Object obj) {
        if (obj == null
                || false == obj instanceof ObjectNode) {
            return false;
        }
        return super.equals(obj);
    }


}

/*
 * $Log: ObjectNodeImpl.java,v $
 * Revision 1.1  2006/12/08 17:11:12  vassil_momtchev
 * initial version of the parser
 *
*/
