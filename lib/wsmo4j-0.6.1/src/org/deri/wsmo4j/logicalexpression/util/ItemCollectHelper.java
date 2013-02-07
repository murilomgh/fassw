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
package org.deri.wsmo4j.logicalexpression.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
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
import org.omwg.ontology.Variable;

/**
 * Helper class for retrieving the concepts, instances and 
 * attributes from logical expressions.
 * 
 * @author Nathalie Steinmetz, DERI Innsbruck
 *
 */
public class ItemCollectHelper extends AbstractVisitor {

	private Set <Term> conceptIds = new HashSet <Term>();
	
	// key is a concept id, value is an attribute id
	private Map <Term, Term> attributeIds = new HashMap <Term, Term> ();

    private Set <Term> instanceIds = new HashSet <Term> ();
    
    private Set <Term> relationIds = new HashSet <Term> ();
    
    public ItemCollectHelper() {
        super();
    }
	
    public void visitAtom(Atom expr) {
    	Iterator it = expr.listParameters().iterator();
    	while (it.hasNext()) {
    		Term t = (Term) it.next();
    		if (isValid(t)) {
    			instanceIds.add(t);
    		}
    	}
    	relationIds.add(expr.getIdentifier());
    }
    
	public void visitCompoundMolecule(CompoundMolecule expr) {
		Iterator i = expr.listOperands().iterator();
        while(i.hasNext()){
            ((Molecule)i.next()).accept(this);
        }
		
	}

	public void visitSubConceptMolecule(SubConceptMolecule expr) {
		Term left = expr.getLeftParameter();
		Term right = expr.getRightParameter();
		if (isValid(left)) {
			conceptIds.add(left);
		}
		if (isValid(right)) {
			conceptIds.add(right);
		}
	}

	public void visitMemberShipMolecule(MembershipMolecule expr) {
		Term left = expr.getLeftParameter();
		Term right = expr.getRightParameter();
		if (isValid(left)) {
			instanceIds.add(left);
		}
		if (isValid(right)) {
			conceptIds.add(right);
		}
	}

	public void visitAttributeValueMolecule(AttributeValueMolecule expr) {
		Term left = expr.getLeftParameter();
		Term right = expr.getRightParameter();
		if (isValid(left)) {
			instanceIds.add(left);
		}
		if (isValid(right)) {
			instanceIds.add(right);
		}	
	}

	public void visitAttributeContraintMolecule(AttributeConstraintMolecule expr) {
		Term left = expr.getLeftParameter();
		Term right = expr.getRightParameter();
		Term attribute = expr.getAttribute();
		if (isValid(left)) {
			conceptIds.add(left);
		}			
		attributeIds.put(expr.getLeftParameter(), attribute);
		if (isValid(right)) {
			conceptIds.add(right);
		}
	}

	public void visitAttributeInferenceMolecule(AttributeInferenceMolecule expr) {
		Term left = expr.getLeftParameter();
		Term right = expr.getRightParameter();
		Term attribute = expr.getAttribute();
		if (isValid(left)) {
			conceptIds.add(left);
		}			
		attributeIds.put(expr.getLeftParameter(), attribute);
		if (isValid(right)) {
			conceptIds.add(right);
		}
	}

	/**
     * @return Set containing the identifier of the logical expression's concepts
     */
    public Set <Term> getConceptIds() {
        return Collections.unmodifiableSet(conceptIds);
    }

    /**
     * @return Set containing the identifier of the logical expression's instances
     */
    public Set <Term> getInstanceIds() {
        return Collections.unmodifiableSet(instanceIds);
    }

    /**
     * @return Set containing the identifier of the logical expression's attributes
     */
    public Map <Term, Term> getAttributeIds() {
    	return Collections.unmodifiableMap(attributeIds);
    }

    /**
     * @return Set containing the identifier of the logical expression's relations
     */
    public Set <Term> getRelationIds() {
    	return Collections.unmodifiableSet(relationIds);
    }
    
    /*
     * A term must only be a constructed term, an identifier or a numbered anonymous id
     */
    private boolean isValid(Term term) {
    	return (!ConstantTransformer.getInstance().isDataType(term.toString()) 
    			&& !(term instanceof DataValue)
    			&& !(term instanceof Variable));
    }
    
}
