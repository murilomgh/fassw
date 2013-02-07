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

import java.util.Iterator;
import java.util.List;

import org.deri.wsmo4j.io.serializer.wsml.LogExprSerializerWSML;
import org.deri.wsmo4j.logicalexpression.AbstractVisitor;
import org.omwg.logicalexpression.Atom;
import org.omwg.logicalexpression.AttributeConstraintMolecule;
import org.omwg.logicalexpression.AttributeInferenceMolecule;
import org.omwg.logicalexpression.AttributeValueMolecule;
import org.omwg.logicalexpression.CompoundMolecule;
import org.omwg.logicalexpression.LogicalExpression;
import org.omwg.logicalexpression.MembershipMolecule;
import org.omwg.logicalexpression.Molecule;
import org.omwg.logicalexpression.SubConceptMolecule;
import org.omwg.logicalexpression.terms.BuiltInConstructedTerm;
import org.omwg.logicalexpression.terms.ConstructedTerm;
import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.Axiom;
import org.wsmo.validator.ValidationError;

/**
 * The FunctionSymbolHelper checks Atoms and Molecules for unallowed
 * function symbols.
 *
 * <pre>
 * Created on Okt 28, 2005
 * Committed by $Author: morcen $
 * $Source: /cvsroot/wsmo4j/wsmo4j/org/deri/wsmo4j/validator/FunctionSymbolHelper.java,v $,
 * </pre>
 *
 * @author nathalie.steinmetz@deri.org
 *
 * @version $Revision: 1.4 $ $Date: 2007/04/02 12:13:19 $
 */
public class FunctionSymbolHelper extends AbstractVisitor{

    private Axiom axiom = null;
    
    private List <ValidationError> errors = null;
    
    private String variant = null;
    
    private LogExprSerializerWSML leSerializer = null;
    
    
    protected FunctionSymbolHelper(Axiom axiom, List <ValidationError> errors, String variant, WsmlFullValidator validator) {
        leSerializer = validator.leSerializer;
        this.axiom = axiom;
        this.errors = errors;
        this.variant = variant;
    }
    
    public void visitAtom(Atom expr) {
        Iterator it = expr.listParameters().iterator();
        while (it.hasNext()) {
            Term t = (Term) it.next();
            if (t instanceof ConstructedTerm && !(t instanceof BuiltInConstructedTerm)) {
                addError(expr, ValidationErrorImpl.AX_ATOMIC_ERR + ": function symbols are not " +
                        "allowed\n" + leSerializer.serialize(expr));
            }
        }
    }
    
    public void visitAttributeContraintMolecule(AttributeConstraintMolecule expr) {
        checkTerm(expr.getRightParameter(),expr);
        checkTerm(expr.getLeftParameter(), expr);
        checkTerm(expr.getAttribute(), expr);
    }

    public void visitAttributeInferenceMolecule(AttributeInferenceMolecule expr) {
        checkTerm(expr.getRightParameter(),expr);
        checkTerm(expr.getLeftParameter(), expr);
        checkTerm(expr.getAttribute(), expr);
    }

    public void visitAttributeValueMolecule(AttributeValueMolecule expr) {
        checkTerm(expr.getRightParameter(),expr);
        checkTerm(expr.getLeftParameter(), expr);
        checkTerm(expr.getAttribute(), expr);
    }

    public void visitCompoundMolecule(CompoundMolecule expr) {
        Iterator it = expr.listOperands().iterator();
        while(it.hasNext()){
            ((Molecule)it.next()).accept(this);
        }
    }

    public void visitMemberShipMolecule(MembershipMolecule expr) {
        checkTerm(expr.getRightParameter(),expr);
        checkTerm(expr.getLeftParameter(), expr);
    }

    public void visitSubConceptMolecule(SubConceptMolecule expr) {
        checkTerm(expr.getRightParameter(), expr);
        checkTerm(expr.getLeftParameter(), expr);
    }
    
    private void checkTerm(Term t, LogicalExpression expr){
        if (t instanceof ConstructedTerm && !(t instanceof BuiltInConstructedTerm)) {
            addError(expr, ValidationErrorImpl.AX_ATOMIC_ERR + ": function symbols are not " +
                    "allowed\n" + leSerializer.serialize(expr));
        } 
    }
   
    /**
     * @return List of collected errors
     */
    public List getErrors() {
        return errors;
    }  
    
    
    private void addError(LogicalExpression logexp, String msg) {
        errors.add(new LogicalExpressionErrorImpl(axiom, logexp, msg, variant));
    } 
}

/*
 * $Log: FunctionSymbolHelper.java,v $
 * Revision 1.4  2007/04/02 12:13:19  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.3  2005/11/15 16:56:09  nathaliest
 * changed access at ValidationError
 *
 * Revision 1.2  2005/10/31 07:59:10  holgerlausen
 * corrected function symbol check
 *
 * Revision 1.1  2005/10/28 17:19:55  nathaliest
 * new helper file
 *
 *
 */