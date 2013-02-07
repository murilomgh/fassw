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

import org.wsmo.common.*;
import org.wsmo.service.ad.*;
import org.wsmo.service.signature.*;

import com.ontotext.wsmo4j.common.*;

public class ADOrchestrationImpl extends EntityImpl implements ADOrchestration {

    protected ActivityDiagram diagram;
    protected StateSignature ss;

    public ADOrchestrationImpl(Identifier id) {
        super(id);
    }

    public ActivityDiagram getActivityDiagram() {
        return diagram;
    }

    public void setActivityDiagram(ActivityDiagram diagram) {
        this.diagram = diagram;
    }

    public StateSignature getStateSignature() {
        return ss;
    }

    public void setStateSignature(StateSignature ss) {
        this.ss = ss;
    }
}

/*
 * $Log: ADOrchestrationImpl.java,v $
 * Revision 1.1  2006/12/08 17:11:10  vassil_momtchev
 * initial version of the parser
 *
*/
