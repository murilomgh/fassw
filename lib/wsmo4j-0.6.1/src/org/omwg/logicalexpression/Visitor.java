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


/**
 * <p>This interface represents an visitor for the logical expression
 * tree structure.</p>
 * <p>The visitor design pattern is a way of separating an algorithm from an object
 * structure. A practical result of this separation is the ability to add new
 * operations to existing object structures without modifying those structures.</p>
 * <p>The idea is to use a structure of element classes, each of which has an accept
 * method that takes a visitor object as an argument. The visitor is an interface
 * that has a different visit() method for each element class. The accept() method
 * of an element class calls back the visit() method for its class. Separate
 * concrete visitor classes can then be written that perform some particular
 * operations.</p>
 * @author DERI Innsbruck, reto.krummenacher@deri.org
 * @version $Revision: 1.6 $ $Date: 2005/09/20 13:21:30 $
 * @see <a href="http://en.wikipedia.org/wiki/Visitor_pattern">Visitor Pattern</a>
 */
public interface Visitor {

    /**
     * @param expr Atom
     */
    void visitAtom(Atom expr);

    /**
     * @param expr Molecule
     */
    void visitCompoundMolecule(CompoundMolecule expr);
    
    /**
     * @param expr SubConceptMolecule
     */
    void visitSubConceptMolecule(SubConceptMolecule expr);
    
    /**
     * @param expr
     */
    void visitMemberShipMolecule(MembershipMolecule expr);
    
    /**
     * @param expr
     */
    void visitAttributeValueMolecule(AttributeValueMolecule expr);

    /**
     * @param expr
     */
    void visitAttributeContraintMolecule(AttributeConstraintMolecule expr);

    /**
     * @param expr
     */
    void visitAttributeInferenceMolecule(AttributeInferenceMolecule expr);

    /**
     * @param expr NEG
     */
    void visitNegation(Negation expr);

    /**
     * @param expr NAF
     */
    void visitNegationAsFailure(NegationAsFailure expr);

    /**
     * @param expr CONSTRAINT
     */
    void visitConstraint(Constraint expr);

    /**
     * @param expr AND
     */
    void visitConjunction(Conjunction expr);

    /**
     * @param expr OR
     */
    void visitDisjunction(Disjunction expr);

    /**
     * @param expr IMPLIEDBY
     */
    void visitInverseImplication(InverseImplication expr);

    /**
     * @param expr IMPLIES
     */
    void visitImplication(Implication expr);

    /**
     * @param expr EQUIVALENT
     */
    void visitEquivalence(Equivalence expr);

    /**
     * @param expr IMPLIESLP
     */
    void visitLogicProgrammingRule(LogicProgrammingRule expr);

    /**
     * @param expr FORALL
     */
    void visitUniversalQuantification(UniversalQuantification expr);

    /**
     * @param expr EXISTS
     */
    void visitExistentialQuantification(ExistentialQuantification expr);

}
