/*
 wsmo4j extension - a Choreography API and Reference Implementation

 Copyright (c) 2005, University of Innsbruck, Austria
               2006, Ontotext Lab. / SIRMA Group

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

import org.deri.wsmo4j.io.serializer.wsml.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.service.rule.*;

/**
 * Reference implementation for the Update rule.
 * 
 * <pre>
 *        Created on Jul 26, 2005
 *       Committed by $Author: vassil_momtchev $
 *       $Source: /cvsroot/wsmo4j/ext/choreography/src/ri/org/deri/wsmo4j/rule/UpdateRI.java,v $
 * </pre>
 * 
 * @author Thomas Haselwanter
 * @author James Scicluna
 * 
 * @version $Revision: 1.1 $ $Date: 2006/10/24 14:11:48 $
 */
public class UpdateRI extends AbstractUpdateRule implements Update {

    protected CompoundFact oldFact;
    
    public UpdateRI(CompoundFact newFact){
        this.fact = newFact;
    }

    /**
     * @param newFact
     *            A CompoundFact object defining the new fact of the update
     *            rule.
     * 
     * @param oldFact
     *            A CompoundFact object defining the old fact of the update
     *            rule.
     */
    public UpdateRI(CompoundFact newFact, CompoundFact oldFact) throws InvalidModelException {
        if (oldFact == null) {
            this.oldFact = null;
            this.fact = newFact;
        }else if (newFact.getClass() == oldFact.getClass()) {
            this.fact = newFact;
            this.oldFact = oldFact;
        }
        else {
            throw new InvalidModelException("The old and new Fact must be of the Same type");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.rule.Update#setOldFact(org.wsmo.service.choreography.rule.FactMolecule)
     */
    public void setOldFact(CompoundFact oldFact) {
        this.oldFact = oldFact;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.rule.Update#getOldFact()
     */
    public CompoundFact getOldFact() {
        return this.oldFact;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.rule.Update#setNewFact(org.wsmo.service.choreography.rule.FactMolecule)
     */
    public void setNewFact(CompoundFact newFact) {
        this.fact = newFact;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.rule.Update#getNewFact()
     */
    public CompoundFact getNewFact() {
        return super.getFact();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.rule.Rule#accept(org.wsmo.service.choreography.rule.Visitor)
     */
    public void accept(org.wsmo.service.rule.Visitor visitor) {
        visitor.visitUpdate(this);
    }

    /* (non-Javadoc)
     * @see org.wsmo.service.choreography.rule.Rule#toString(org.wsmo.common.TopEntity)
     */
    public String toString(TopEntity te) {
        VisitorSerializeWSMLTransitionRules visitor = new VisitorSerializeWSMLTransitionRules(te);
        visitor.visitUpdate(this);
        return visitor.getSerializedObject();
    }
    
    @Override
    public String toString() {
        return toString(null);
    }
}

/*
 * $Log: UpdateRI.java,v $
 * Revision 1.1  2006/10/24 14:11:48  vassil_momtchev
 * choreography/orchestration rules refactored. different types where appropriate now supported
 *
 * Revision 1.12  2006/04/17 07:51:55  vassil_momtchev
 * method signature toString() changed to toString(TopEntity)
 *
 * Revision 1.11  2006/02/15 15:53:19  alex_simov
 * toString() did not check correctly for empty old fact
 *
 * Revision 1.10  2006/02/03 13:31:25  jamsci001
 * - Reference Implementation for MoleculeFact, PipedRules, RelationFat
 * - Modified UpdateRI to handle also RelationFacts
 * - Rules container is now initialized with an identifier
 * Revision 1.9 2006/01/31 10:25:42 vassil_momtchev
 * defineNewFactsOnly method removed - use getOldFact.isEmpty() instead;
 * implementation fixed according the new interface; log footer added;
 * 
 */
