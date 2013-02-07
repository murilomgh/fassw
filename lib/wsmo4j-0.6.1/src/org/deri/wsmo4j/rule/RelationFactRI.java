/*
 wsmo4j extension - a Choreography API and Reference Implementation

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

package org.deri.wsmo4j.rule;

import java.util.*;

import org.omwg.logicalexpression.*;
import org.wsmo.service.rule.*;

/**
 * Interface or class description
 *
 * @author James Scicluna
 * @author Thomas Haselwanter
 * @author Vassil Momtchev
 *
 * Created on 03-Feb-2006
 * Committed by $Author: vassil_momtchev $
 *
 * $Source: /cvsroot/wsmo4j/ext/choreography/src/ri/org/deri/wsmo4j/rule/RelationFactRI.java,v $,
 * @version $Revision: 1.1 $ $Date: 2006/10/24 14:11:48 $
 */

public class RelationFactRI implements RelationFact {

    private Atom atom;
    
    /**
     * Default constructor RelationFactRI
     */
    public RelationFactRI() {
    }
    
    /**
     * Constructor
     * @param atom contained atom in the relation
     * @throws IllegalArgumentException
     */
    public RelationFactRI(Atom atom) {
        if (atom == null) {
            throw new IllegalArgumentException("Null atom!");
        }
        this.atom = atom;
    }
    
    public Atom getAtom() {
        return atom;
    }
    
    public void setAtom(Atom atom) {
        this.atom = atom;
    }

    /* (non-Javadoc)
     * @see org.wsmo.service.choreography.rule.CompoundFact#isEmpty()
     */
    public boolean isEmpty() {
        if (atom == null || atom.listParameters().size() == 0) {
            return true;
        }
        return false;
    }
    
    public Iterator<AtomicExpression> iterator() {
        LinkedList<AtomicExpression> list =  new LinkedList<AtomicExpression>();
        if (atom != null) {
            list.add(atom);
        }
        return list.iterator();
    }
}

/*
 * $Log: RelationFactRI.java,v $
 * Revision 1.1  2006/10/24 14:11:48  vassil_momtchev
 * choreography/orchestration rules refactored. different types where appropriate now supported
 *
 * Revision 1.3  2006/04/17 07:54:26  vassil_momtchev
 * assocition level between RelationFact and Atom descreased to containment from inheritance; public Iterator<AtomicExpression> iterator() method added
 *
 * Revision 1.2  2006/02/10 15:31:03  vassil_momtchev
 * implementation changed according the api change
 *
*/
