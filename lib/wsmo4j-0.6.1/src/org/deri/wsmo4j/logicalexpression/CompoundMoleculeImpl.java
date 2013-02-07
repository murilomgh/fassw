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
package org.deri.wsmo4j.logicalexpression;

import java.util.*;

import org.deri.wsmo4j.io.serializer.wsml.*;
import org.deri.wsmo4j.logicalexpression.util.*;
import org.omwg.logicalexpression.*;
import org.omwg.logicalexpression.Visitor;
import org.omwg.logicalexpression.terms.*;
import org.wsmo.common.*;

/**
 * Represents a Compount Molecule
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
public class CompoundMoleculeImpl implements CompoundMolecule {
    
    List <Molecule> molecules;
        
    public CompoundMoleculeImpl(List <Molecule> molecules){
        setOperands(new ArrayList <LogicalExpression> (molecules));
    }

    public List <AttributeConstraintMolecule> listAttributeConstraintMolecules() {
        return filter(AttributeConstraintMolecule.class, null);
    }

    public List <AttributeInferenceMolecule> listAttributeInferenceMolecules() {
        return filter(AttributeInferenceMolecule.class, null);
    }

    public List <AttributeValueMolecule> listAttributeValueMolecules() {
        return filter(AttributeValueMolecule.class, null);
    }

    public List <AttributeConstraintMolecule> listAttributeConstraintMolecules(Term t) {
        return filter(AttributeConstraintMolecule.class, t);
    }

    public List <AttributeInferenceMolecule> listAttributeInferenceMolecules(Term t) {
        return filter(AttributeInferenceMolecule.class, t);
    }

    public List <AttributeValueMolecule> listAttributeValueMolecules(Term t) {
        return filter(AttributeValueMolecule.class, t);
    }

    public List <MembershipMolecule> listMemberShipMolecules() {
        return filter(MembershipMolecule.class, null);
    }

    public List <SubConceptMolecule> listSubConceptMolecules() {
        return filter(SubConceptMolecule.class, null);
    }

    public List <LogicalExpression> listOperands() {
        return new ArrayList <LogicalExpression> (Collections.unmodifiableList(molecules));
    }
    
    private List filter(Class clazz, Term attributeName){
        List ret = new Vector();
        Iterator i = molecules.iterator();
        while (i.hasNext()){
            Object o = i.next();
            if (clazz.isInstance(o) && //classfilter
                (attributeName == null || //if attributeName given check as well
                        attributeName.equals(((AttributeMolecule)o).getAttribute()))){
                ret.add(o);
            }
        }
        return Collections.unmodifiableList(ret);
    }

    public void accept(Visitor v) {
        v.visitCompoundMolecule(this);
    }

    /**
     * 
     */
    public CompoundMoleculeImpl() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    public boolean equals(Object o){
        if (!(o instanceof CompoundMolecule)){
            return false;
        }
        if (o==this){
            return true;
        }
        CompoundMolecule cm = (CompoundMolecule)o;
        if (cm.listOperands().size()!=molecules.size()){
            return false;
        }
        Iterator i = cm.listOperands().iterator();
        while (i.hasNext()){
            Object molecule = i.next();
            if (!molecules.contains(molecule)){
                return false;
            }
        }
        return true;
    }
    
    public String toString(){
        return new LogExprSerializerWSML(null).serialize(this);
    }

    /* (non-Javadoc)
     * @see org.omwg.logicalexpression.CompoundExpression#setOperands(java.util.List)
     */
    public void setOperands(List <LogicalExpression> operands) {
        if (operands.size() < 2){
            throw new IllegalArgumentException("Compound Molecules have to consist of at least 2 Molecules");
        }
        if (!SetUtil.allOfType(operands, Molecule.class)){
            throw new IllegalArgumentException("Compound Molecule can only consist of Molecules");
        }
        Iterator i = operands.iterator();
        Term t = ((Molecule)i.next()).getLeftParameter();
        while (i.hasNext()){
            Term tNext = ((Molecule)i.next()).getLeftParameter();
            if (!tNext.equals(t)){
                throw new IllegalArgumentException("Only Molecules with the same ID can be grouped to a Compound Molecule");
            }
        }
        
        List <Molecule> temp = new ArrayList <Molecule> (); 
        for(LogicalExpression le : operands){
            if (le instanceof Molecule){
                temp.add((Molecule) le);
            }
        }
        
        this.molecules = temp;
        if (this.listMemberShipMolecules().size() !=0 &&
                this.listSubConceptMolecules().size() != 0){
            throw new IllegalArgumentException("Compound Molecules can have either memberOf or subConceptOf Satement not both!");
        }
    }
    
    /**
     * Returns String representation of a logical expression.
     * @return the string representation of the Logical expression
     * @param nsHolder namespace container used to abbreviate IRIs to 
     * sQNames.
     */
    public String toString(TopEntity nsHolder) {
        return new LogExprSerializerWSML(nsHolder).serialize(this);
    }
    
    public int hashCode() {
        return CompoundMoleculeImpl.class.hashCode()+molecules.size();
    }
}


/*
 *$Log$
 *Revision 1.7  2007/04/02 12:13:20  morcen
 *Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 *Revision 1.6  2006/05/04 15:16:45  holgerlausen
 *fixed bug 1481907 -
 *for logicalexpression hashCode() and equals where not correctly implemented
 *
 *Revision 1.5  2005/11/28 15:46:02  holgerlausen
 *added support for using a TopEntity with namespace information to shorten string representation of logical expressions (RFE 1363559)
 *
 *Revision 1.4  2005/09/21 08:15:38  holgerlausen
 *fixing java doc, removing asString()
 *
 *Revision 1.3  2005/09/21 06:31:55  holgerlausen
 *allowing to set arguments rfe  1290049
 *
 *Revision 1.2  2005/09/20 19:41:01  holgerlausen
 *removed superflouis interfaces for IO in logical expression (since intgeration not necessary)
 *
 *Revision 1.1  2005/09/20 13:21:31  holgerlausen
 *refactored logical expression API to have simple molecules and compound molecules (RFE 1290043)
 *
 */