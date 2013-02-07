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

import java.util.Set;

import org.omwg.logicalexpression.AttributeValueMolecule;
import org.omwg.logicalexpression.MembershipMolecule;
import org.wsmo.common.TopEntity;

/**
 * Defines elements needed for normal updates on molecules.
 *
 * @author James Scicluna
 * @author Thomas Haselwanter
 *
 * Created on 02-Feb-2006
 * Committed by $Author: vassil_momtchev $
 *
 * $Source: /cvsroot/wsmo4j/ext/choreography/src/api/org/wsmo/service/rule/MoleculeFact.java,v $,
 * @version $Revision: 1.1 $ $Date: 2006/10/24 14:11:48 $
 */

public interface MoleculeFact extends CompoundFact {

    /**
     * Returns the membership fact defined in the compound fact.
     * 
     * @return A MembershipFact object
     */
    public Set<MembershipMolecule> listMembershipMolecules();

    /**
     * Returns a list of attribute value facts defined in the compound fact.
     * 
     * @return A set of AttributeValueFact objects
     */
    public Set<AttributeValueMolecule> listAttributeValueMolecules();

    /**
     * Add a membership molecule to the Compound Fact.
     * 
     * @param memberof
     *            A MembershipMolecule object defining a membership fact
     */
    public void addMembershipMolecule(MembershipMolecule memberMolecule);
    
    /**
     * Remove a membership molecule from the Compound Fact.
     * 
     * @param memberof
     *            A MembershipMolecule object defining a membership fact
     */
    public void removeMembershipMolecule(MembershipMolecule memberMolecule);

    /**
     * Add  attribute value molecules to the compound fact.
     * 
     * @param attrValues
     *            AttributeValueMolecule objects defining the facts
     *            related to the attribute values
     */
    public void addAttributeValueMolecule(AttributeValueMolecule attrValue);
    
    /**
     * Remove attribute value molecules to the compound fact.
     * 
     * @param attrValues
     *            AttributeValueMolecule objects defining the facts
     *            related to the attribute values
     */
    public void removeAttributeValueMolecule(AttributeValueMolecule attrValue);
    
    /**
     * returns this MoleculeFact as string using the namespace
     * definitions in the topEntity given as parameter
     * @param nsHolder TopEntity that namespace are used to abreviate 
     * full IRIs.
     * @return String representation of this fact using 
     * sQNames to abbreviate full IRIs 
     */
    public String toString(TopEntity nsHolder);
}

/*
 * $Log: MoleculeFact.java,v $
 * Revision 1.1  2006/10/24 14:11:48  vassil_momtchev
 * choreography/orchestration rules refactored. different types where appropriate now supported
 *
 * Revision 1.3  2006/02/15 16:43:47  alex_simov
 * toString(TopEntity) method added
 *
 * Revision 1.2  2006/02/10 15:30:35  vassil_momtchev
 * isEmpty method shifted from MoleculeFact to CompoundFact; log footer added
 *
*/
