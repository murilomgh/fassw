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
package org.deri.wsmo4j.io.serializer.xml;


import java.util.*;

import org.deri.wsmo4j.logicalexpression.*;
import org.w3c.dom.*;

import org.omwg.logicalexpression.*;
import org.omwg.logicalexpression.terms.*;


/**
 * Concrete Visitor class. For each visited logical expression,
 * a document element is built.
 * @see org.deri.wsmo4j.io.serializer.xml.LogExprSerializerXML
 * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor
 */

public class VisitorSerializeXML
        extends AbstractVisitor {

    private Vector stack;

    private VisitorSerializeXMLTerms visitor;

    private Document doc;

    /**
     * @param doc Document that will be filled with the xml structure
     * @see org.deri.wsmo4j.io.serializer.xml.LogExprSerializerXML#LogExprSerializerXML(TopEntity, Document)
     */
    public VisitorSerializeXML(Document doc) {
        this.doc = doc;
        visitor = new VisitorSerializeXMLTerms(doc);
        stack = new Vector();
    }

    /**
     * Builds an element in the document with the Atom's identifier
     * as attribute. The Atom's terms are added as children to the node.
     * The element is then added to a vector.
     * @param expr Atom to be serialized
     * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor#visitAtom(Atom)
     */
    public void visitAtom(Atom expr) {
        int nbParams = expr.getArity();
        visitor.setName("atom");
        expr.getIdentifier().accept(visitor);
        Node newExpr = (Node)visitor.getSerializedObject();
        if (nbParams > 0) {
            for (int i = 0; i < nbParams; i++) {
                visitor.setName("arg");
                expr.getParameter(i).accept(visitor);
                newExpr.appendChild((Node)visitor.getSerializedObject());
            }
        }
        stack.add(newExpr);
    }

    public void visitCompoundMolecule(CompoundMolecule expr) {
        Element molecule = doc.createElement("molecule");
        visitor.setName("term");
        //id of molecule
        ((Molecule) expr.listOperands().iterator().next()).getLeftParameter().accept(visitor);
        molecule.appendChild((Node)visitor.getSerializedObject());
        List isa = expr.listMemberShipMolecules();
        int isaType = 0;
        if (isa != null && isa.size() !=0) {
            isaType = 1;
        }
        else {
            isa = expr.listSubConceptMolecules();
            if (isa != null && isa.size() !=0) {
                isaType = 2;
            }
        }
        if (isaType != 0) {
            molecule.appendChild(helpSerializeIsa(isa, isaType, doc));
        }

        //Collect all attributeNames:
        Set attrNames = new HashSet();
        Iterator i = expr.listOperands().iterator();
        while (i.hasNext()){
            Object o = i.next();
            if (o instanceof AttributeMolecule){
                attrNames.add(((AttributeMolecule)o).getAttribute());
            }
        }
        
        Iterator attrNamesIt = attrNames.iterator();
        while (attrNamesIt.hasNext()){
            Term t = (Term)attrNamesIt.next();
            List l = expr.listAttributeConstraintMolecules(t);
            if (l.size()>0){
                molecule.appendChild(helpSerializeAttrSpecification(l, doc));
            }
            l = expr.listAttributeInferenceMolecules(t);
            if (l.size()>0){
                molecule.appendChild(helpSerializeAttrSpecification(l, doc));
            }
            l = expr.listAttributeValueMolecules(t);
            if (l.size()>0){
                molecule.appendChild(helpSerializeAttrSpecification(l, doc));
            }
        }
        stack.add(molecule);
    }

    /**
     * Builds a node in the document with the Unary's operator type as name.
     * The Unary's logical expression is added as child to the node. The node
     * is then added to a vector.
     * @param expr Unary Expression to be serialized, with operator NAF
     * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor#visitNegationAsFailure(NegationAsFailure)
     */
    public void visitNegationAsFailure(NegationAsFailure expr) {
        Node newExpr = doc.createElement("naf");
        expr.getOperand().accept(this);
        newExpr.appendChild((Node)stack.remove(stack.size() - 1));
        stack.add(newExpr);
    }

    /**
     * Builds a node in the document with the Unary's operator type as name.
     * The Unary's logical expression is added as child to the node. The node
     * is then added to a vector.
     * @param expr Unary Expression to be serialized, with operator NEG
     * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor#visitNegation(Negation)
     */
    public void visitNegation(Negation expr) {
        Node newExpr = doc.createElement("neg");
        expr.getOperand().accept(this);
        newExpr.appendChild((Node)stack.remove(stack.size() - 1));
        stack.add(newExpr);
    }

    /**
     * Builds a node in the document with the Unary's operator type as name.
     * The Unary's logical expression is added as child to the node. The node
     * is then added to a vector.
     * @param expr Unary Expression to be serialized, with operator CONSTRAINT
     * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor#visitConstraint(Constraint)
     */
    public void visitConstraint(Constraint expr) {
        Node newExpr = doc.createElement("constraint");
        expr.getOperand().accept(this);
        newExpr.appendChild((Node)stack.remove(stack.size() - 1));
        stack.add(newExpr);
    }

    /**
     * Builds a node in the document with the Binary's operator type as name.
     * The Binary's logical expressions are added as children to the node. The node
     * is then added to a vector.
     * @param expr Binary Expression to be serialized, with operator AND
     * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor#visitConjunction(Conjunction)
     */
    public void visitConjunction(Conjunction expr) {
        Node newExpr = doc.createElement("and");
        finishBinary(expr, newExpr);
    }

    /**
     * Builds a node in the document with the Binary's operator type as name.
     * The Binary's logical expressions are added as children to the node. The node
     * is then added to a vector.
     * @param expr Binary Expression to be serialized, with operator EQUIVALENT
     * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor#visitEquivalence(Equivalence)
     */
    public void visitEquivalence(Equivalence expr) {
        Node newExpr = doc.createElement("equivalent");
        finishBinary(expr, newExpr);
    }

    /**
     * Builds a node in the document with the Binary's operator type as name.
     * The Binary's logical expressions are added as children to the node. The node
     * is then added to a vector.
     * @param expr Binary Expression to be serialized, with operator IMPLIEDBY
     * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor#visitInverseImplication(InverseImplication)
     */
    public void visitInverseImplication(InverseImplication expr) {
        Node newExpr = doc.createElement("impliedBy");
        finishBinary(expr, newExpr);
    }

    /**
     * Builds a node in the document with the Binary's operator type as name.
     * The Binary's logical expressions are added as children to the node. The node
     * is then added to a vector.
     * @param expr Binary Expression to be serialized, with operator IMPLIES
     * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor#visitImplication(Implication)
     */
    public void visitImplication(Implication expr) {
        Node newExpr = doc.createElement("implies");
        finishBinary(expr, newExpr);
    }

    /**
     * Builds a node in the document with the Binary's operator type as name.
     * The Binary's logical expressions are added as children to the node. The node
     * is then added to a vector.
     * @param expr Binary Expression to be serialized, with operator IMPLIEDBYLP
     * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor#visitLogicProgrammingRule(LogicProgrammingRule)
     */
    public void visitLogicProgrammingRule(LogicProgrammingRule expr) {
        Node newExpr = doc.createElement("impliedByLP");
        finishBinary(expr, newExpr);
    }

    /**
     * Builds a node in the document with the Binary's operator type as name.
     * The Binary's logical expressions are added as children to the node. The node
     * is then added to a vector.
     * @param expr Binary Expression to be serialized, with operator OR
     * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor#visitDisjunction(Disjunction)
     */
    public void visitDisjunction(Disjunction expr) {
        Node newExpr = doc.createElement("or");
        finishBinary(expr, newExpr);
    }

    /**
     * This method takes a Node containing the Binary Expression operator and
     * adds the Binary's logical expressions as children to this node. The node
     * is then added to a vector.
     * @param expr Binary Expression to be serialized
     * @param newExpr Node containing the Binary Expression operator
     * @see org.omwg.logicalexpression.Binary
     */
    private void finishBinary(Binary expr, Node newExpr) {
        expr.getLeftOperand().accept(this);
        expr.getRightOperand().accept(this);
        newExpr.appendChild((Node)stack.remove(stack.size() - 2));
        newExpr.appendChild((Node)stack.remove(stack.size() - 1));
        stack.add(newExpr);
    }

    /**
     * Builds a node in the document with the Quantified's operator type as name.
     * The Quantified's Variables and its logical expression are added as
     * children to the node. The node is then added to a vector.
     * @param expr Quantified Expression to be serialized, with operator FORALL
     * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor#visitUniversalQuantification(UniversalQuantification)
     */
    public void visitUniversalQuantification(UniversalQuantification expr) {
        Node newExpr = doc.createElement("forall");
        addVariables(newExpr, expr);
        expr.getOperand().accept(this);
        newExpr.appendChild((Node)stack.remove(stack.size() - 1));
        stack.add(newExpr);
    }

    /**
     * Builds a node in the document with the Quantified's operator type as name.
     * The Quantified's Variables and its logical expression are added as
     * children to the node. The node is then added to a vector.
     * @param expr Quantified Expression to be serialized, with operator EXISTS
     * @see org.deri.wsmo4j.logicalexpression.AbstractVisitor#visitExistentialQuantification(ExistentialQuantification)
     */
    public void visitExistentialQuantification(ExistentialQuantification expr) {
        Node newExpr = doc.createElement("exists");
        addVariables(newExpr, expr);
        expr.getOperand().accept(this);
        newExpr.appendChild((Node)stack.remove(stack.size() - 1));
        stack.add(newExpr);
    }

    /**
     * All serialized elements are added to a vector. This method removes the
     * first serialized element from this vector and shifts any subsequent
     * elements to the left (subtracts one from their indices).
     * @return the serialized document node that is the first element in this vector
     */
    public Object getSerializedObject() {
        return stack.remove(0);
    }

    /**
     * This method serializes the memberOf or subConceptOf Set of a Molecule.
     * An element in the document node, with the type of Set as attribute, is built.
     * The Set's terms are added as children to the node.
     * @param isaSet Set of MemberOf identifiers or of SubConceptOf identifiers
     * @param isaType type of set: 1 = MemberOf; 2 = SubConceptOf
     * @param doc Document
     * @return Element representing serialized Set
     */
    private Node helpSerializeIsa(List isaSet, int isaType, Document doc) {
        Element isa = doc.createElement("isa");
        Iterator it = isaSet.iterator();
        switch (isaType) {
            case 1:
                isa.setAttribute("type", "memberOf");
                break;
            case 2:
                isa.setAttribute("type", "subConceptOf");
                break;
        }
        visitor.setName("term");
        while (it.hasNext()) {
            ((Molecule)it.next()).getRightParameter().accept(visitor);
            isa.appendChild((Node)visitor.getSerializedObject());
        }
        return isa;
    }


    //attrMolecules of one attributeName! and one Type!
    private Node helpSerializeAttrSpecification(List attrMolecules, Document doc) {
        Element attrSpec = null; 
        AttributeMolecule am = (AttributeMolecule)attrMolecules.iterator().next();
        if (am instanceof AttributeConstraintMolecule) {
            attrSpec = doc.createElement("attributeDefinition");
            attrSpec.setAttribute("type", "constraining");
        }
        else if (am instanceof AttributeInferenceMolecule) {
            attrSpec = doc.createElement("attributeDefinition");
            attrSpec.setAttribute("type", "inferring");
        }
        else if (am instanceof AttributeValueMolecule) {
            attrSpec = doc.createElement("attributeValue");
        }
        visitor.setName("name");
        am.getAttribute().accept(visitor);
        attrSpec.appendChild((Node)visitor.getSerializedObject());
        Iterator it = attrMolecules.iterator();
        if (am instanceof AttributeConstraintMolecule || am instanceof AttributeInferenceMolecule) {
            visitor.setName("type");
            while (it.hasNext()) {
                ((AttributeMolecule)it.next()).getRightParameter().accept(visitor);
                attrSpec.appendChild((Node)visitor.getSerializedObject());
            }
        }
        else if (am instanceof AttributeValueMolecule) {
            visitor.setName("value");
            while (it.hasNext()) {
                ((AttributeMolecule)it.next()).getRightParameter().accept(visitor);
                attrSpec.appendChild((Node)visitor.getSerializedObject());
            }
        }
        return attrSpec;
    }

    /**
     * This method serializes the Variables of a Quantified logical expression.
     * It takes a Node containing the Quantified Expression operator and
     * adds the Quantified's variables as children to this node. The node
     * is then added to a vector.An element in the given document node, with the attribute specification type
     * as attribute to the node, is built. The name of the attribute specification
     * is added as child to the node, as are also the arguments of the specification.
     * @param q Node containing the Quantified Expression operator
     * @param log Quantified Expression to be serialized
     */
    private void addVariables(Node q, LogicalExpression log) {
        Set s = ((Quantified)log).listVariables();
        Iterator i = s.iterator();
        visitor.setName("variable");
        while (i.hasNext()) {
            ((Term)i.next()).accept(visitor);
            q.appendChild((Node)visitor.getSerializedObject());
        }
    }

    public void visitSubConceptMolecule(SubConceptMolecule expr) {
        visitConceptMolecule(expr,2);
    }
    
    private void visitConceptMolecule(Molecule expr, int type){
        Element molecule = doc.createElement("molecule");
        visitor.setName("term");
        expr.getLeftParameter().accept(visitor);
        molecule.appendChild((Node)visitor.getSerializedObject());
        List isa = new Vector(); 
        isa.add(expr);
        molecule.appendChild(helpSerializeIsa(isa, type, doc));
        stack.add(molecule);
    }

    /* (non-Javadoc)
     * @see org.omwg.logicalexpression.Visitor#visitMemberShipMolecule(org.omwg.logicalexpression.MembershipMolecule)
     */
    public void visitMemberShipMolecule(MembershipMolecule expr) {
        visitConceptMolecule(expr,1);
    }

    /* (non-Javadoc)
     * @see org.omwg.logicalexpression.Visitor#visitAttributeValueMolecule(org.omwg.logicalexpression.AttributeValueMolecule)
     */
    public void visitAttributeValueMolecule(AttributeValueMolecule expr) {
        visitAttributeMolecule(expr);
    }
    
    private void visitAttributeMolecule(AttributeMolecule expr){
        Element molecule = doc.createElement("molecule");
        visitor.setName("term");
        expr.getLeftParameter().accept(visitor);
        molecule.appendChild((Node)visitor.getSerializedObject());
        
        List l = new Vector();
        l.add(expr);
        molecule.appendChild(helpSerializeAttrSpecification(l, doc));
        stack.add(molecule);
    }

    /* (non-Javadoc)
     * @see org.omwg.logicalexpression.Visitor#visitAttributeContraintMolecule(org.omwg.logicalexpression.AttributeConstraintMolecule)
     */
    public void visitAttributeContraintMolecule(AttributeConstraintMolecule expr) {
        visitAttributeMolecule(expr);
    }

    /* (non-Javadoc)
     * @see org.omwg.logicalexpression.Visitor#visitAttributeInferenceMolecule(org.omwg.logicalexpression.AttributeInferenceMolecule)
     */
    public void visitAttributeInferenceMolecule(AttributeInferenceMolecule expr) {
        visitAttributeMolecule(expr);
    }
}
