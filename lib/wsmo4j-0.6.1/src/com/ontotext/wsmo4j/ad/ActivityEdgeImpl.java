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

import com.ontotext.wsmo4j.common.*;

public abstract class ActivityEdgeImpl extends EntityImpl implements ActivityEdge {

    private ActivityNode source;
    private ActivityNode target;
    private InterruptibleRegion iRegion;
    private Guard guard;

    public ActivityEdgeImpl(Identifier id) {
        super(id);
    }

    public ActivityNode getSource() {
        return this.source;
    }

    public void setSource(ActivityNode node) {
        this.source = node;
    }

    public ActivityNode getTarget() {
        return this.target;
    }

    public void setTarget(ActivityNode node) {
        this.target = node;
    }

    public void setInterruptibleRegion(InterruptibleRegion group) {
        this.iRegion = group;
    }

    public InterruptibleRegion getInterruptibleRegion() {
        return this.iRegion;
    }

    public void setGuard(Guard guard) {
        this.guard = guard;
    }

    public Guard getGuard() {
        return this.guard;
    }

}

/*
 * $Log: ActivityEdgeImpl.java,v $
 * Revision 1.1  2006/12/08 17:11:11  vassil_momtchev
 * initial version of the parser
 *
*/
