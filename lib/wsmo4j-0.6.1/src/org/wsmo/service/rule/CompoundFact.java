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

package org.wsmo.service.rule;

import org.omwg.logicalexpression.*;



/**
 * A Compound Fact is the object used for add(), delete() and update() rules.
 * This upper dummy class is used for MoleculeFact and RelationFact
 * 
 * @author James Scicluna
 * @author Thomas Haselwanter
 * @author Vassil Momtchev
 * 
 * Created on 16-Oct-2005 Committed by $Author: vassil_momtchev $
 * 
 * $Source:
 * /cvsroot/wsmo4j/ext/choreography/src/api/org/wsmo/service/choreography/rule/CompoundFact.java,v $,
 * @version $Revision: 1.1 $ $Date: 2006/10/24 14:11:48 $
 */

public interface CompoundFact extends Iterable<AtomicExpression> {
    
    /**
     * Check if the fact contains any information
     * 
     * @return
     */
    public boolean isEmpty();
}

/*
 * $Log: CompoundFact.java,v $
 * Revision 1.1  2006/10/24 14:11:48  vassil_momtchev
 * choreography/orchestration rules refactored. different types where appropriate now supported
 *
 * Revision 1.10  2006/04/17 07:40:10  vassil_momtchev
 * CompoundFact extends Iterable<AtomicExpression>
 *
 * Revision 1.9  2006/02/10 15:30:35  vassil_momtchev
 * isEmpty method shifted from MoleculeFact to CompoundFact; log footer added
 *
 * Revision 1.8  2006/02/03 13:29:00  jamsci001
 * - CompoundFact is now a "dummy" superclass of MoleculeFact and RelationFact
 * - RelationFact extends CompoundFact and Atom
 * - UpdateRule refactored to support RelationFacts
 * - PipedRules are also allowed
 *
 * Revision 1.7  2006/01/31 10:23:34  vassil_momtchev
 * CompoundFact has two sets now of MembershipMolecules and AttributeValueMoleculs; log footer added
 *
*/
