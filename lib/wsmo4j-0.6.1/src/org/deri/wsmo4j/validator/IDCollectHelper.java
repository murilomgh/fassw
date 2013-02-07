/*
 wsmo4j - a WSMO API and Reference Implementation
 Copyright (c) 2004, DERI Innsbruck
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
package org.deri.wsmo4j.validator;


import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.deri.wsmo4j.logicalexpression.AbstractVisitor;
import org.deri.wsmo4j.logicalexpression.ConstantTransformer;
import org.omwg.logicalexpression.Atom;
import org.omwg.logicalexpression.AttributeConstraintMolecule;
import org.omwg.logicalexpression.AttributeInferenceMolecule;
import org.omwg.logicalexpression.AttributeValueMolecule;
import org.omwg.logicalexpression.CompoundMolecule;
import org.omwg.logicalexpression.MembershipMolecule;
import org.omwg.logicalexpression.Molecule;
import org.omwg.logicalexpression.SubConceptMolecule;
import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.DataValue;


/**
 * The IDCollectHelper collects the concepts, instances and relations from 
 * axiom definitions into specified Sets.
 *
 * <pre>
 * Created on Sep 9, 2005
 * Committed by $Author$
 * $Source$,
 * </pre>
 *
 * @author Holger Lausen (holger.lausen@deri.org)
 * @author nathalie.steinmetz@deri.org
 *
 * @version $Revision$ $Date$
 */
public class IDCollectHelper
        extends AbstractVisitor {

    Set <Term> idConcepts = new HashSet <Term> ();
    
    Set <Term> explicitConcepts = new HashSet <Term> ();

    Set <Term> idInstances = new HashSet <Term> ();
    
    Set <Term> explicitInstances = new HashSet <Term> ();

    Set <Term> idRelations = new HashSet <Term> ();

    Set <Term> explicitRelations = new HashSet <Term> ();
    
    Set <Term> idConcreteRelations = new HashSet <Term> ();
    
    Set <Term> idAbstractRelations = new HashSet <Term> ();
    
    public IDCollectHelper() {
        super();
    }

    /**
     * The identifiers of atoms must be relations in wsml-core. So the 
     * identifier is put into the relations Set.
     * 
     * @see org.omwg.logicalexpression.Visitor#visitAtom(org.omwg.logicalexpression.Atom)
     */
    public void visitAtom(Atom expr) {
        idRelations.add(expr.getIdentifier());
    }


    public void visitAttributeContraintMolecule(AttributeConstraintMolecule expr) {
        idConcepts.add(expr.getLeftParameter());
        explicitConcepts.add(expr.getLeftParameter());
        Term t = expr.getRightParameter();
        if (!ConstantTransformer.getInstance().isDataType(t.toString())) {
            idConcepts.add(t);
            idAbstractRelations.add(expr.getAttribute());
        }
        else {
            idConcreteRelations.add(expr.getAttribute());
        }
        idRelations.add(expr.getAttribute());
    }

    public void visitAttributeInferenceMolecule(AttributeInferenceMolecule expr) {
        idConcepts.add(expr.getLeftParameter());
        Term t = expr.getRightParameter();
        if (!ConstantTransformer.getInstance().isDataType(t.toString())) {
            idConcepts.add(t);
            idAbstractRelations.add(expr.getAttribute());
        } 
        else {
            idConcreteRelations.add(expr.getAttribute());
        }
        idRelations.add(expr.getAttribute());
    }

    public void visitAttributeValueMolecule(AttributeValueMolecule expr) {
        idInstances.add(expr.getLeftParameter());
        Term t = expr.getRightParameter();
        if (!(t instanceof DataValue)){
            idInstances.add(t);
            idAbstractRelations.add(expr.getAttribute());
        }
        else {
            idConcreteRelations.add(expr.getAttribute());
        }
        idRelations.add(expr.getAttribute());
    }

    public void visitCompoundMolecule(CompoundMolecule expr) {
        Iterator i = expr.listOperands().iterator();
        while(i.hasNext()){
            ((Molecule)i.next()).accept(this);
        }
    }

    public void visitMemberShipMolecule(MembershipMolecule expr) {
        idConcepts.add(expr.getRightParameter());
        idInstances.add(expr.getLeftParameter());
//        explicitInstances.add(expr.getLeftParameter());
    }

    public void visitSubConceptMolecule(SubConceptMolecule expr) {
        idConcepts.add(expr.getRightParameter());
        idConcepts.add(expr.getLeftParameter());
        explicitConcepts.add(expr.getLeftParameter());
    }
    
    /**
     * @return Set containing the logical expression's concepts
     */
    public Set <Term> getConceptIds() {
        return Collections.unmodifiableSet(idConcepts);
    }

    /**
     * @return Set containing the logical expression's explicitly declared concepts
     */
    public Set <Term> getExplicitConcepts() {
        return explicitConcepts;
    }

    /**
     * @return Set containing the logical expression's instances
     */
    public Set <Term> getInstanceIds() {
        return Collections.unmodifiableSet(idInstances);
    }

    /**
     * @return Set containing the logical expression's explicitly declared instances
     */
    public Set <Term> getExplicitInstances() {
        return explicitInstances;
    }

    /**
     * @return Set containing the logical expression's relations
     */
    public Set <Term> getRelationIds() {
        return Collections.unmodifiableSet(idRelations);
    }

    /**
     * @return Set containing the logical expression's explicitly declared relations
     */
    public Set <Term> getExplicitRelations() {
        return explicitRelations;
    }
    
    /**
     * @return Set containing the logical expression's relations with abstract range
     */
    public Set <Term> getIdAbstractRelations() {
        return idAbstractRelations;
    }

    /**
     * @return Set containing the logical expression's relations with concrete range
     */
    public Set <Term> getIdConcreteRelations() {
        return idConcreteRelations;
    }

}
/*
 * $Log$
 * Revision 1.10  2007/04/02 12:13:19  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.9  2005/12/15 16:46:24  nathaliest
 * check for implicit concepts
 *
 * Revision 1.8  2005/10/28 17:21:06  nathaliest
 * fixed bugs
 *
 * Revision 1.7  2005/10/11 15:03:31  nathaliest
 * organized imports
 *
 * Revision 1.6  2005/09/20 13:21:30  holgerlausen
 * refactored logical expression API to have simple molecules and compound molecules (RFE 1290043)
 *
 * Revision 1.5  2005/09/11 22:12:52  nathaliest
 * fixed problem and added javadoc
 *
 * Revision 1.4  2005/09/09 15:51:42  marin_dimitrov
 * formatting
 *
 * Revision 1.3  2005/09/09 14:45:28  nathaliest
 * implementation of idCollectHelper
 *
 * Revision 1.2  2005/09/09 11:58:20  holgerlausen
 * fixed header logexp no longer extension
 *
 * Revision 1.1  2005/09/09 10:37:57  holgerlausen
 * remplate for nathalie
 *
 */
