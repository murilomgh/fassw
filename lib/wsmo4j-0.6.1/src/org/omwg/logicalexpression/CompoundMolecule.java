/*
 wsmo4j - a WSMO API and Reference Implementation

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
package org.omwg.logicalexpression;

import java.util.*;

import org.omwg.logicalexpression.terms.*;

/**
 * Represents a compound molecule which is a container for simple
 * molecules E.g., "x subConceptOf {y,z}" or "a[b hasValue c] memberOf d". 
 *
 * <pre>
 * Created on Sep 19, 2005
 * Committed by $Author$
 * $Source$,
 * </pre> 
 *
 * @author Holger Lausen (holger.lausen@deri.org)
 *
 * @version $Revision$ $Date$
 */
public interface CompoundMolecule extends CompoundExpression {

    /**
     * retrieving all molecules of the type "a subConcept b"
     * @return list of SubConceptMolecules
     * @see SubConceptMolecule
     */
    public List<SubConceptMolecule> listSubConceptMolecules();
    
    /**
     * retrieving all molecules of the type "a memberOf b"
     * @return list of MemberShipMolecules
     * @see MembershipMolecule
     */
    public List<MembershipMolecule> listMemberShipMolecules();

    /**
     * retrieving all molecules of the type "a[b hasValue c]"
     * @return list of AttributeValueMolecules
     * @see AttributeValueMolecule
     */
    public List<AttributeValueMolecule> listAttributeValueMolecules();

    /**
     * retrieving all molecules of the type "a[b hasValue c]", where
     * Term t equals the attribute name of the molecule
     * @param t Term that acts as filter on the attribute name 
     * (t has to be equal to the name) 
     * @return list of AttributeValueMolecules
     * @see AttributeValueMolecule
     */
    public List<AttributeValueMolecule> listAttributeValueMolecules(Term t);

    /**
     * retrieving all molecules of the type "a[b ofType c]"
     * @return list of AttributeConstraintMolecules
     * @see AttributeConstraintMolecule
     */
    public List<AttributeConstraintMolecule> listAttributeConstraintMolecules();
    
    /**
     * retrieving all molecules of the type "a[b ofType c]", where
     * Term t equals the attribute name of the molecule
     * @param t Term that acts as filter on the attribute name 
     * (t has to be equal to the name) 
     * @return list of AttributeContraintMolecules
     * @see AttributeConstraintMolecule
     */
    public List<AttributeConstraintMolecule> listAttributeConstraintMolecules(Term t);

    /**
     * retrieving all molecules of the type "a[b impliesType c]"
     * @return list of AttributeInferenceMolecules
     * @see AttributeInferenceMolecule
     */
    public List<AttributeInferenceMolecule> listAttributeInferenceMolecules();
    
    /**
     * retrieving all molecules of the type "a[b impliesType c]", where
     * Term t equals the attribute name of the molecule
     * @param t Term that acts as filter on the attribute name 
     * (t has to be equal to the name) 
     * @return list of AttributeInferenceMolecules
     * @see AttributeInferenceMolecule
     */
    public List<AttributeInferenceMolecule> listAttributeInferenceMolecules(Term t);
}


/*
 *$Log$
 *Revision 1.2  2007/04/02 12:13:15  morcen
 *Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 *Revision 1.1  2005/09/20 13:21:31  holgerlausen
 *refactored logical expression API to have simple molecules and compound molecules (RFE 1290043)
 *
 */