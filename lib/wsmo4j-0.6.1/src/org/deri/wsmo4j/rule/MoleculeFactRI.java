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

import org.deri.wsmo4j.io.serializer.wsml.VisitorSerializeWSMLTransitionRules;
import org.omwg.logicalexpression.*;
import org.wsmo.common.TopEntity;
import org.wsmo.service.rule.*;

/**
 * Interface or class description
 *
 * @author James Scicluna
 * @author Thomas Haselwanter
 *
 * Created on 02-Feb-2006
 * Committed by $Author: vassil_momtchev $
 *
 * $Source: /cvsroot/wsmo4j/ext/choreography/src/ri/org/deri/wsmo4j/rule/MoleculeFactRI.java,v $,
 * @version $Revision: 1.1 $ $Date: 2006/10/24 14:11:48 $
 */

public class MoleculeFactRI implements MoleculeFact {

    private Set<MembershipMolecule> memberMolecules = new LinkedHashSet<MembershipMolecule>();
    private Set<AttributeValueMolecule> attrValues = new LinkedHashSet<AttributeValueMolecule>();

    /**
     * Default constructor for a moleucle fact.
     */
    public MoleculeFactRI() {
        super();
    }

    /**
     * Constructor for a Molecule Fact. This method accepts also null
     * values within its parameters. Null values are interpreted as having
     * an empty set of molecules.
     * 
     * @param memberMolecules Set of MembershipMolecule objects
     * @param attrValues Set of AttributeValueMolecule objects
     */
    public MoleculeFactRI(Set<MembershipMolecule> memberMolecules, Set<AttributeValueMolecule> attrValues) {
        if(memberMolecules != null) this.memberMolecules = memberMolecules;
        if(attrValues != null) this.attrValues = attrValues;
    }
    
    /**
     * Constructor for a Molecule Fact. Similar to the second constructor,
     * this method accepts also null values
     * 
     * @param memberMolecule A MembershipMolecule object
     * @param attrValues Set of AttributeValueMolecule objects
     */
    public MoleculeFactRI(MembershipMolecule memberMolecule, Set<AttributeValueMolecule> attrValues){
        if(memberMolecule != null){
            this.memberMolecules.add(memberMolecule);
        }
        if(attrValues != null) this.attrValues = attrValues;
    }
    
    /**
     * Constructor for a Molecule Fact. Similar to the second constructor,
     * this method accepts also null values
     * 
     * @param memberMolecules Set of MembershipMolecule objects
     * @param attrValue An AttributeValueMolecule object
     */
    public MoleculeFactRI(Set<MembershipMolecule> memberMolecules, AttributeValueMolecule attrValue) {
        if(memberMolecules != null) this.memberMolecules = memberMolecules;
        if(attrValue != null) this.attrValues.add(attrValue);
    }
    
    /**
     * Constructor for a Molecule Fact. Similar to the second constructor,
     * this method accepts also null values
     * 
     * @param memberMolecule A MembershipMolecule object
     * @param attrValue An AttributeValueMolecule object
     */
    public MoleculeFactRI(MembershipMolecule memberMolecule, AttributeValueMolecule attrValue){
        if(memberMolecule != null) this.memberMolecules.add(memberMolecule);
        if(attrValue != null) this.attrValues.add(attrValue);
    }
    

    /* (non-Javadoc)
     * @see org.wsmo.service.choreography.rule.MoleculeFact#getMembershipMolecule()
     */
    public Set<MembershipMolecule> listMembershipMolecules() {
        return Collections.unmodifiableSet(this.memberMolecules);
    }

    /* (non-Javadoc)
     * @see org.wsmo.service.choreography.rule.MoleculeFact#getAttributeValueMolecules()
     */
    public Set<AttributeValueMolecule> listAttributeValueMolecules() {
        return Collections.unmodifiableSet(this.attrValues);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.rule.MoleculeFact#setMembershipMolecule(org.omwg.logicalexpression.MembershipMolecule)
     */
    public void addMembershipMolecule(MembershipMolecule memberMolecule) {
        this.memberMolecules.add(memberMolecule);
    }
    
    /* (non-Javadoc)
     * @see org.wsmo.service.choreography.rule.MoleculeFact#removeMembershipMolecule(org.omwg.logicalexpression.MembershipMolecule)
     */
    public void removeMembershipMolecule(MembershipMolecule memberMolecule) {
        this.memberMolecules.remove(memberMolecule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.rule.MoleculeFact#setAttributeValueMolecules(java.util.Set)
     */
    public void addAttributeValueMolecule(AttributeValueMolecule attrValue) {
        this.attrValues.add(attrValue);
    }
    
    /* (non-Javadoc)
     * @see org.wsmo.service.choreography.rule.MoleculeFact#removeAttributeValueMolecule(org.omwg.logicalexpression.AttributeValueMolecule)
     */
    public void removeAttributeValueMolecule(AttributeValueMolecule attrValue) {
        this.attrValues.remove(attrValue);
    }
    
    /* (non-Javadoc)
     * @see org.wsmo.service.choreography.rule.CompoundFact#isEmpty()
     */
    public boolean isEmpty() {
        if (attrValues.size() == 0 && memberMolecules.size() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return toString(null);
    }

    public String toString(TopEntity nsHolder) {
        VisitorSerializeWSMLTransitionRules serializer = 
            new VisitorSerializeWSMLTransitionRules(nsHolder);
        return serializer.helpCompoundFact(this, null);
    }
    
    public Iterator<AtomicExpression> iterator() {       
        LinkedList<AtomicExpression> list = new LinkedList<AtomicExpression>();
        list.addAll(attrValues);
        list.addAll(memberMolecules);
        return list.iterator();
    }

}

/*
 * $Log: MoleculeFactRI.java,v $
 * Revision 1.1  2006/10/24 14:11:48  vassil_momtchev
 * choreography/orchestration rules refactored. different types where appropriate now supported
 *
 * Revision 1.6  2006/04/17 07:51:39  vassil_momtchev
 * public Iterator<AtomicExpression> iterator() method added
 *
 * Revision 1.5  2006/02/15 16:44:34  alex_simov
 * toString(TopEntity) method implemented
 *
 * Revision 1.4  2006/02/15 15:55:28  alex_simov
 * serializer visitor used in toString() to produce valid wsml data
 *
 * Revision 1.3  2006/02/10 15:31:03  vassil_momtchev
 * implementation changed according the api change
 *
*/
