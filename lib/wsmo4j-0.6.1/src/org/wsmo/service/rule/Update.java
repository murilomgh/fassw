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

/**
 * An update may have two forms: - update(oldFact => newFact). -
 * update(newFact). In the first form, the oldFact object must be defined. In
 * the second form, this must not be defined.
 * 
 * <pre>
 *     Created on Jul 26, 2005
 *     Committed by $Author: vassil_momtchev $
 *     $Source: /cvsroot/wsmo4j/ext/choreography/src/api/org/wsmo/service/rule/Update.java,v $
 * </pre>
 * 
 * @author James Scicluna
 * @author Thomas Haselwanter
 * @author Holger Lausen
 * 
 * @version $Revision: 1.1 $ $Date: 2006/10/24 14:11:48 $
 */
public interface Update extends UpdateRule {

    /**
     * Returns the new fact defined by the update rule.
     * 
     * @return A CompoundFact object representing the new fact to be added to
     *         the knowledge base
     */
    public CompoundFact getNewFact();

    /**
     * Sets the new fact associated with the update rule.
     * 
     * @param newFact
     *            A CompoundFact object representing the new fact to be added to
     *            the knowledge base
     */
    public void setNewFact(CompoundFact newFact);

    /**
     * Returns the old fact defined by the update rule.
     * 
     * @return A CompoundFact object representing the old fact to be deleted
     *         from the knowledge base
     */
    public CompoundFact getOldFact();

    /**
     * Sets the old fact associated with the update rule.
     * 
     * @param oldFact
     *            A CompoundFact object representing the old fact to be deleted
     *            from the knowledge base
     */
    public void setOldFact(CompoundFact oldFact);
}

/*
 * $Log: Update.java,v $
 * Revision 1.1  2006/10/24 14:11:48  vassil_momtchev
 * choreography/orchestration rules refactored. different types where appropriate now supported
 *
 * Revision 1.6  2006/01/31 10:22:08  vassil_momtchev
 * Update is composed by 2 CompoundFacts the new facts, and the facts to be deleted; log footer added
 *
*/
