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
package org.deri.wsmo4j.io.serializer.wsml;


import java.util.*;

import org.deri.wsmo4j.io.parser.wsml.*;
import org.deri.wsmo4j.logicalexpression.*;
import org.deri.wsmo4j.logicalexpression.terms.*;
import org.omwg.logicalexpression.*;
import org.omwg.logicalexpression.terms.*;
import org.omwg.ontology.Parameter;
import org.wsmo.common.*;


/**
 * Concrete Visitor class. For each visited logical expression, a String is built.
 * @see org.deri.wsmo4j.io.serializer.xml.LogExprSerializerXML
 * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor
 */
public class VisitorSerializeWSML
        extends AbstractVisitor {

    private Vector stack;

    private Map atoms2Rewrite = new HashMap();

    private VisitorSerializeWSMLTerms visitor;

    private LogicalExpression parentOperator;

    /**
     * @param nsC TopEntity
     * @see org.deri.wsmo4j.io.serializer.wsml.LogExprSerializerWSML#LogExprSerializerWSML(TopEntity)
     */
    public VisitorSerializeWSML(TopEntity nsC) {
        visitor = new VisitorSerializeWSMLTerms(nsC);
        visitor.setAtoms2ConstructedTerms(atoms2Rewrite);
        stack = new Vector();
    }

    /**
     * Builds a String representing the Atom and adds it to a vector.
     * @param expr Atom to be serialized
     * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor#visitAtom(Atom)
     */
    public void visitAtom(Atom expr) {
        boolean infix = false;
        Term t = expr.getIdentifier();
        t.accept(visitor);
        String s = visitor.getSerializedObject();
        String realIRI = ConstantTransformer.getInstance().findIri(s);
        
        //check for infix notion e.g. 1+2
        if (realIRI != null && 
                ConstantTransformer.getInstance().isInfix(realIRI) &&
                expr.getArity()==2){
            expr.getParameter(0).accept(visitor);
            expr.getParameter(1).accept(visitor);
            s = visitor.getSerializedObject() +
                " " + s + " " +
                visitor.getSerializedObject();
        }
        else {
            //avoid +(1,2)
            if (realIRI!= null) {
                s=visitor.resolveIRI(realIRI);
            }
            int nbParams = expr.getArity();
            if (nbParams > 0) {
                s = s + "(";
                for (int i = 0; i < nbParams; i++) {
                    expr.getParameter(i).accept(visitor);
                    s = s + visitor.getSerializedObject();
                    if (i + 1 < nbParams) {
                        s = s + ",";
                    }
                }
                s = s + ")";
            }
        }
        stack.add(s);
    }

    /**
     * Builds a String representing the Unary expression and adds it to a vector.
     * @param expr Unary Expression to be serialized, with operator NAF
     * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor#visitNegationAsFailure(NegationAsFailure)
     */
    public void visitNegationAsFailure(NegationAsFailure expr) {
        expr.getOperand().accept(this);
        if (expr.getOperand()instanceof AtomicExpression) {
            stack.add("naf " + (String)stack.remove(stack.size() - 1));
        }
        else {
            stack.add("naf (" + (String)stack.remove(stack.size() - 1) + ")");
        }
    }

    /**
     * Builds a String representing the Unary expression and adds it to a vector.
     * @param expr Unary Expression to be serialized, with operator NEG
     * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor#visitNegation(Negation)
     */
    public void visitNegation(Negation expr) {
        expr.getOperand().accept(this);
        if (expr.getOperand()instanceof AtomicExpression) {
            stack.add("neg " + (String)stack.remove(stack.size() - 1));
        }
        else {
            stack.add("neg (" + (String)stack.remove(stack.size() - 1) + ")");
        }
    }

    /**
     * Builds a String representing the Unary expression and adds it to a vector.
     * @param expr Unary Expression to be serialized, with operator CONSTRAINT
     * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor#visitConstraint(Constraint)
     */
    public void visitConstraint(Constraint expr) {
        expr.getOperand().accept(this);
        stack.add("!- " + (String)stack.remove(stack.size() - 1));
    }

    /**
     * Builds a String representing the Binary expression and adds it to a vector.
     * @param expr Binary Expression to be serialized, with operator AND
     * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor#visitConjunction(Conjunction)
     */
    public void visitConjunction(Conjunction expr) {
        //check functionsymbols to rewrite
        if (expr.getRightOperand() instanceof Atom){
            Atom a = (Atom)expr.getRightOperand();
            if (a.getArity()>0 && a.getParameter(0) instanceof TempVariable){
                List <Term> params = new LinkedList <Term>(a.listParameters());
                params.remove(0);
                Term key = a.getParameter(0);
                Term subst = new ConstructedTermImpl((IRI)a.getIdentifier(), params);
                atoms2Rewrite.put(a.getParameter(0),subst);
                expr.getLeftOperand().accept(this);
                return;
            }
        }
        parentOperator = expr;
        expr.getLeftOperand().accept(this);
        expr.getRightOperand().accept(this);
        stack.add((String)stack.remove(stack.size() - 2) + "\n  and " + (String)stack.remove(stack.size() - 1));
    }

    /**
     * Builds a String representing the Binary expression and adds it to a vector.
     * @param expr Binary Expression to be serialized, with operator EQUIVALENT
     * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor#visitEquivalence(Equivalence)
     */
    public void visitEquivalence(Equivalence expr) {
        boolean brackets = (parentOperator instanceof Conjunction
                            || parentOperator instanceof Disjunction
                            || parentOperator instanceof InverseImplication
                            || parentOperator instanceof Implication
                            || parentOperator instanceof Equivalence
                            || parentOperator instanceof LogicProgrammingRule);
        parentOperator = expr;
        expr.getLeftOperand().accept(this);
        expr.getRightOperand().accept(this);
        if (brackets) {
            stack.add("(" + (String)stack.remove(stack.size() - 2) + "\nequivalent\n" +
                      (String)stack.remove(stack.size() - 1) + ")");
        }
        else {
            stack.add((String)stack.remove(stack.size() - 2) + "\nequivalent\n" + (String)stack.remove(stack.size() - 1));
        }
    }

    /**
     * Builds a String representing the Binary expression and adds it to a vector.
     * @param expr Binary Expression to be serialized, with operator IMPLIEDBY
     * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor#visitInverseImplication(InverseImplication)
     */
    public void visitInverseImplication(InverseImplication expr) {
        boolean brackets = (parentOperator instanceof Conjunction
                            || parentOperator instanceof Disjunction
                            || parentOperator instanceof InverseImplication
                            || parentOperator instanceof Implication
                            || parentOperator instanceof Equivalence
                            || parentOperator instanceof LogicProgrammingRule);
        parentOperator = expr;
        expr.getLeftOperand().accept(this);
        expr.getRightOperand().accept(this);
        if (brackets) {
            stack.add("(" + (String)stack.remove(stack.size() - 2) + "\nimpliedBy\n" +
                      (String)stack.remove(stack.size() - 1) + ")");
        }
        else {
            stack.add((String)stack.remove(stack.size() - 2) + "\nimpliedBy\n" + (String)stack.remove(stack.size() - 1));
        }
    }

    /**
     * Builds a String representing the Binary expression and adds it to a vector.
     * @param expr Binary Expression to be serialized, with operator IMPLIES
     * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor#visitImplication(Implication)
     */
    public void visitImplication(Implication expr) {
        boolean brackets = (parentOperator instanceof Conjunction
                            || parentOperator instanceof Disjunction
                            || parentOperator instanceof InverseImplication
                            || parentOperator instanceof Implication
                            || parentOperator instanceof Equivalence
                            || parentOperator instanceof LogicProgrammingRule);
        parentOperator = expr;
        expr.getLeftOperand().accept(this);
        expr.getRightOperand().accept(this);
        if (brackets) {
            stack.add("(" + (String)stack.remove(stack.size() - 2) + "\nimplies\n" +
                      (String)stack.remove(stack.size() - 1) + ")");
        }
        else {
            stack.add((String)stack.remove(stack.size() - 2) + "\nimplies\n" + (String)stack.remove(stack.size() - 1));
        }
    }

    /**
     * Builds a String representing the Binary expression and adds it to a vector.
     * @param expr Binary Expression to be serialized, with operator IMPLIEDBYLP
     * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor#visitLogicProgrammingRule(LogicProgrammingRule)
     */
    public void visitLogicProgrammingRule(LogicProgrammingRule expr) {
        boolean brackets = (parentOperator instanceof Conjunction
                            || parentOperator instanceof Disjunction
                            || parentOperator instanceof InverseImplication
                            || parentOperator instanceof Implication
                            || parentOperator instanceof Equivalence
                            || parentOperator instanceof LogicProgrammingRule);
        parentOperator = expr;
        expr.getLeftOperand().accept(this);
        expr.getRightOperand().accept(this);
        if (brackets) {
            stack.add("(" + (String)stack.remove(stack.size() - 2) + "\n:-\n"
                      + (String)stack.remove(stack.size() - 1) + ")");
        }
        else {
            stack.add((String)stack.remove(stack.size() - 2) + "\n:-\n"
                      + (String)stack.remove(stack.size() - 1));
        }
    }

    /**
     * Builds a String representing the Binary expression and adds it to a vector.
     * @param expr Binary Expression to be serialized, with operator OR
     * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor#visitDisjunction(Disjunction)
     */
    public void visitDisjunction(Disjunction expr) {
        boolean bracket = parentOperator instanceof Conjunction;
        parentOperator = expr;
        expr.getLeftOperand().accept(this);
        expr.getRightOperand().accept(this);
        if (bracket) {
            stack.add("(" + (String)stack.remove(stack.size() - 2) + "\n  or\n" + (String)stack.remove(stack.size() - 1) +
                      ")");
        }
        else {
            stack.add((String)stack.remove(stack.size() - 2) + "\n  or\n" + (String)stack.remove(stack.size() - 1));
        }
    }

    /**
     * Builds a String representing the Quantified expression and adds it to a vector.
     * @param expr Quantified Expression to be serialized, with operator FORALL
     * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor#visitUniversalQuantification(UniversalQuantification)
     */
    public void visitUniversalQuantification(UniversalQuantification expr) {
        String res = helpQuantified(expr);
        stack.add("forall " + res + "\n  ( " + (String)stack.remove(stack.size() - 1) + " )");
    }

    /**
     * Builds a String representing the Quantified expression and adds it to a vector.
     * @param expr Quantified Expression to be serialized, with operator EXISTS
     * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor#visitExistentialQuantification(ExistentialQuantification)
     */
    public void visitExistentialQuantification(ExistentialQuantification expr) {
        String res = helpQuantified(expr);
        stack.add("exists " + res + "\n  ( " + (String)stack.remove(stack.size() - 1) + " )");
    }

    /**
     * All serialized elements are added to a vector. This method removes the
     * first serialized object from this vector and shifts any subsequent
     * objects to the left (subtracts one from their indices).
     * @return the serialized String object that is the first element in this vector
     */
    public String getSerializedObject() {
        Object o = stack.remove(0);
        parentOperator = null;
        return (String)o;
    }

    

    /**
     * Builds a String representing the Quantified Expression
     * @param expr Quantified Expression to be serialized, with operator EXISTS
     * @return String representing serialized Quantified Expression
     */
    private String helpQuantified(Quantified expr) {
        String res = "";
        Set s = expr.listVariables();
        Iterator i = s.iterator();
        if (s.size() > 1) {
            res = res + "{";
            while (i.hasNext()) {
                ((Term)i.next()).accept(visitor);
                res = res + visitor.getSerializedObject();
                if (i.hasNext()) {
                    res = res + ",";
                }
            }
            res = res + "}";
        }
        else {
            ((Term)i.next()).accept(visitor);
            res = res + visitor.getSerializedObject();
        }
        expr.getOperand().accept(this);
        return res;
    }

    public void visitAttributeContraintMolecule(AttributeConstraintMolecule expr) {
        expr.getLeftParameter().accept(visitor);
        String id = visitor.getSerializedObject();
        expr.getRightParameter().accept(visitor);
        String value = visitor.getSerializedObject();
        expr.getAttribute().accept(visitor);
        String attributName = visitor.getSerializedObject();
        stack.add(id+"["+attributName+" ofType "+ value+"]");
    }

    public void visitAttributeInferenceMolecule(AttributeInferenceMolecule expr) {
        expr.getLeftParameter().accept(visitor);
        String id = visitor.getSerializedObject();
        expr.getRightParameter().accept(visitor);
        String value = visitor.getSerializedObject();
        expr.getAttribute().accept(visitor);
        String attributName = visitor.getSerializedObject();
        stack.add(id+"["+attributName+" impliesType "+ value+"]");
    }

    public void visitAttributeValueMolecule(AttributeValueMolecule expr) {
        expr.getLeftParameter().accept(visitor);
        String id = visitor.getSerializedObject();
        expr.getRightParameter().accept(visitor);
        String value = visitor.getSerializedObject();
        expr.getAttribute().accept(visitor);
        String attributName = visitor.getSerializedObject();
        stack.add(id+"["+attributName+" hasValue "+ value+"]");
    }

    public void visitCompoundMolecule(CompoundMolecule expr) {
        Term termId = ((Molecule) expr.listOperands().iterator().next()).getLeftParameter(); 
        termId.accept(visitor);
        String str = visitor.getSerializedObject();
        
        List isA = new Vector();
        String isAType = "";
        Set attrNames = new HashSet();
        Iterator i = expr.listOperands().iterator();
        int n = 0;
        while (i.hasNext()){
            n++;
            Molecule m = (Molecule)i.next();
            m.getRightParameter().accept(visitor);
            String right= visitor.getSerializedObject();
            if (m instanceof AttributeMolecule){
                attrNames.add(((AttributeMolecule)m).getAttribute());
            }
            else if (m instanceof SubConceptMolecule){
                isA.add(right);
                isAType = " subConceptOf ";
            }
            else if (m instanceof MembershipMolecule){
                isA.add(right);
                isAType = " memberOf ";
            }
        }
        if (attrNames.size()!=0) {
            str += "["; 
        }
        i = attrNames.iterator();
        int count = 0;
        while(i.hasNext()){
            Term t = (Term)i.next();
            str += helpAttributes(expr.listAttributeValueMolecules(t),t, " hasValue ");
            if (expr.listAttributeValueMolecules(t).size()!=0 &&
                    (expr.listAttributeConstraintMolecules(t).size()!=0l ||
                    expr.listAttributeInferenceMolecules(t).size()!=0)){
                str += ", ";
            }
            str += helpAttributes(expr.listAttributeConstraintMolecules(t),t, " ofType ");
            if (expr.listAttributeConstraintMolecules(t).size()!=0 &&
                    expr.listAttributeInferenceMolecules(t).size()!=0){
                str += ", ";
            }
            str += helpAttributes(expr.listAttributeInferenceMolecules(t),t, " impliesType ");
            if (++count < attrNames.size()){
                str += ", ";
            }
        }
        if (attrNames.size()!=0) {
            str += "]"; 
        }
        String isAString="";
        i = isA.iterator();
        count = 0;
        while(i.hasNext()){
            isAString += (String)i.next();
            if (++count<isA.size()){
                isAString += ",";
            }
        }
        if (isA.size()>1){
            isAString = "{" + isAString + "}";
        }
        stack.add(str + isAType + isAString);
    }
    
    private String helpAttributes(List attributeMolecules, Term t, String operator){
        if (attributeMolecules == null || attributeMolecules.size()==0){
            return "";
        }
        t.accept(visitor);
        String attributName = visitor.getSerializedObject();
        String right = "";
        int count = 0;
        Iterator it = attributeMolecules.iterator();
        while (it.hasNext()){
            Molecule m = (Molecule)it.next();
            m.getRightParameter().accept(visitor);
            right += visitor.getSerializedObject();
            if (++count < attributeMolecules.size()) {
                right += ", ";
            }
        }
        if (count>1){
            right = "{"+right+"}";
        }
        return attributName + operator + right;
    }

    public void visitMemberShipMolecule(MembershipMolecule expr) {
        expr.getLeftParameter().accept(visitor);
        String id = visitor.getSerializedObject();
        expr.getRightParameter().accept(visitor);
        String type = visitor.getSerializedObject();
        stack.add(id +" memberOf " + type);
    }

    public void visitSubConceptMolecule(SubConceptMolecule expr) {
        expr.getLeftParameter().accept(visitor);
        String id = visitor.getSerializedObject();
        expr.getRightParameter().accept(visitor);
        String type = visitor.getSerializedObject();
        stack.add(id +" subConceptOf " + type);
    }
}
