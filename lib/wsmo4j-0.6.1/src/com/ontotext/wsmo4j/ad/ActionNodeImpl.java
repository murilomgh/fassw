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

import java.util.*;

import org.wsmo.common.*;
import org.wsmo.service.ad.*;

import com.ontotext.wsmo4j.common.*;

public abstract class ActionNodeImpl extends EntityImpl implements ActionNode {

    private LinkedHashSet<Pin> inPins;
    private LinkedHashSet<Pin> outPins;

    public ActionNodeImpl(Identifier id) {
        super(id);
        this.inPins = new LinkedHashSet<Pin>();
        this.outPins = new LinkedHashSet<Pin>();
    }

    /**
     */
    public void addInputPin(Pin pin) {
        if (pin == null) {
            throw new IllegalArgumentException();
        }
        this.inPins.add(pin);
    }

    /**
     */
    public Pin removeInputPin(Pin pin) {
        if (pin == null) {
            throw new IllegalArgumentException();
        }
        this.inPins.remove(pin);
        return pin;
    }

    /**
     */
    public Iterator<Pin> listInputPins() {
        return Collections.unmodifiableSet(this.inPins).iterator();
    }

    /**
     */
    public void addOutputPin(Pin pin) {
        if (pin == null) {
            throw new IllegalArgumentException();
        }
        this.outPins.add(pin);
    }

    /**
     */
    public Pin removeOutputPin(Pin pin) {
        if (pin == null) {
            throw new IllegalArgumentException();
        }
        this.outPins.remove(pin);
        return pin;

    }

    /**
     */
    public Iterator<Pin> listOutputPins() {
        return Collections.unmodifiableSet(this.outPins).iterator();
    }


}

/*
 * $Log: ActionNodeImpl.java,v $
 * Revision 1.1  2006/12/08 17:11:10  vassil_momtchev
 * initial version of the parser
 *
*/
