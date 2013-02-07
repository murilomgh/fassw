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
package org.wsmo.factory;


import java.util.*;

import org.omwg.logicalexpression.*;
import org.omwg.logicalexpression.terms.*;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.wsml.*;


/**
 * This interface represents a logical expression factory
 * it extends the generic logical expression factory by providing
 * methods for the creation of an object model
 *
 * @author DERI Innsbruck, reto.krummenacher@deri.org
 * @version $Revision: 1.17 $ $Date: 2007/04/02 12:13:15 $
 * @see org.wsmo.factory.LogicalExpressionFactory
 */
public interface LogicalExpressionFactory {

    public static final String LEFACTORY_WSMO_FACTORY = "wsmo_factory";
    
    public static final String LEFACTORY_DATA_FACTORY = "data_factory";

    /**
     * Creates a LogicalExpression object from string.
     * @param expr the string representation of the logical expression
     * @return The newly created LogicalExpression object.
     */
    LogicalExpression createLogicalExpression(String expr)
            throws ParserException;

    /**
     * Creates a Logical Expression object from a string given a particular
     * namespace context of a TopEntity
     * @param expr the string representation of the logical expression
     * @param nsHolder some Top entity that contains the namespace
     * context (e.g.default Namespace)
     * @return The newly created LogicalExpression object.
     */
    LogicalExpression createLogicalExpression(String expr, TopEntity nsHolder)
            throws ParserException;

    /**
     * creates a negation (e.g. "not a")
     * @param expr the expression that is affected by the operator
     * @return negation
     * @throws IllegalArgumentException in case the logical expression contains a nested CONSTRAINT
     */
    Negation createNegation(LogicalExpression expr)
            throws IllegalArgumentException;

    /**
     * creates a naf (e.g. "naf a")
     * @param expr the expression that is affected by the operator
     * @return naf
     * @throws IllegalArgumentException in case the logical expression contains a nested CONSTRAINT
     */
    NegationAsFailure createNegationAsFailure(LogicalExpression expr)
            throws IllegalArgumentException;

    /**
     * creates a constraint
     * @param expr the expression that is affected by the operator
     * @return constraint
     * @throws IllegalArgumentException in case the logical expression contains a nested CONSTRAINT
     */
    Constraint createConstraint(LogicalExpression expr)
            throws IllegalArgumentException;

    /**
     * Creates a conjunction (e.g. "a and b")
     *
     * @param exprLeft the left expression that is conjunctively connected
     * @param exprRight the right expression that is conjunctively connected
     * @return conjunction
     * @throws IllegalArgumentException
     * <p>in case one of the two logical expressions is null
     * <p>in case one of the two logical expressions contains a nested CONSTRAINT</p>
     */
    Conjunction createConjunction(LogicalExpression exprLeft, LogicalExpression exprRight)
            throws IllegalArgumentException;

    /**
     * Creates a disjunction (e.g. "a or b").
     *
     * @param exprLeft the left expression that is connected
     * @param exprRight the right expression that is connected
     * @return conjunction
     * @throws IllegalArgumentException
     * <p>in case one of the two logical expressions is null
     * <p>in case one of the two logical expressions contains a nested CONSTRAINT</p>
     */
    Disjunction createDisjunction(LogicalExpression exprLeft, LogicalExpression exprRight)
            throws IllegalArgumentException;

    /**
     * Creates a Implication (e.g. "a implies b").
     *
     * @param exprLeft the left expression that is connected
     * @param exprRight the right expression that is connected
     * @return Implication
     * @throws IllegalArgumentException
     * <p>in case one of the two logical expressions is null
     * <p>in case one of the two logical expressions contains a nested CONSTRAINT</p>
     */
    Implication createImplication(LogicalExpression exprLeft, LogicalExpression exprRight)
            throws IllegalArgumentException;

    /**
     * Creates a Equivalence implication (e.g. "a equivalent b").
     *
     * @param exprLeft the left expression that is connected
     * @param exprRight the right expression that is connected
     * @return Implication
     * @throws IllegalArgumentException
     * <p>in case one of the two logical expressions is null
     * <p>in case one of the two logical expressions contains a nested CONSTRAINT</p>
     */
    Equivalence createEquivalence(LogicalExpression exprLeft, LogicalExpression exprRight)
            throws IllegalArgumentException;

    /**
     * Creates a LogicProgrammingRule (e.g. "a :- b").
     *
     * @param exprLeft the left expression that is connected
     * @param exprRight the right expression that is connected
     * @return LogicProgrammingRule
     * @throws IllegalArgumentException
     * <p>in case one of the two logical expressions is null
     * <p>in case one of the two logical expressions contains a nested CONSTRAINT</p>
     */
    LogicProgrammingRule createLogicProgrammingRule(LogicalExpression exprLeft, LogicalExpression exprRight)
            throws IllegalArgumentException;

    /**
     * Creates a InverseImplication (e.g. "a impliedBy b").
     *
     * @param exprLeft the left expression that is connected
     * @param exprRight the right expression that is connected
     * @return LogicProgrammingRule
     * @throws InverseImplication
     * <p>in case one of the two logical expressions is null
     * <p>in case one of the two logical expressions contains a nested CONSTRAINT</p>
     */
    InverseImplication createInverseImplication(LogicalExpression exprLeft, LogicalExpression exprRight)
            throws IllegalArgumentException;

    /**
     * creates a universally quantified expression (e.g. "forall {?a,?b} (?a memberOf ?b)")
     * @param variables Set of variables that are quantified (e.g. [?a, ?b])
     * @param expr the expression that is quantified
     * @return a quantified expression
     * @throws IllegalArgumentException
     * <p>in case the logical expression contains a nested CONSTRAINT</p>
     * <p>in case the Set of variables is null</p>
     * <p>in case the arguments of the list aren't all of Type Variable</p>
     */
    UniversalQuantification createUniversalQuantification(Set <Variable> variables, LogicalExpression expr)
            throws IllegalArgumentException;

    /**
     * creates a universally quantified expression (e.g. "forall ?a (?a memberOf A)")
     * @param variables that is quantified (e.g. "?a")
     * @param expr the expression that is quantified
     * @return a quantified expression
     * @throws IllegalArgumentException
     * <p>in case the logical expression contains a nested CONSTRAINT</p>
     */
    UniversalQuantification createUniversalQuantification(Variable variable, LogicalExpression expr)
            throws IllegalArgumentException;

    /**
     * creates a existentialy quantified expression (e.g. "exists {?a,?b} (?a memberOf ?b)")
     * @param variables that is quantified (e.g. [?a,?b])
     * @param expr the expression that is quantified
     * @return a quantified expression
     * @throws IllegalArgumentException
     * <p>in case the logical expression contains a nested CONSTRAINT</p>
     * <p>in case the Set of variables is null</p>
     * <p>in case the arguments of the list aren't all of Type Variable</p>
     */
    ExistentialQuantification createExistentialQuantification(Set <Variable> variables, LogicalExpression expr)
            throws IllegalArgumentException;

    /**
     * creates a universally quantified expression (e.g. "forall ?a (?a memberOf A)")
     * @param variable that is quantified (e.g. "?a")
     * @param expr the expression that is quantified
     * @return a quantified expression
     * @throws IllegalArgumentException
     * <p>in case the logical expression contains a nested CONSTRAINT</p>
     */
    ExistentialQuantification createExistentialQuantification(Variable variable, LogicalExpression expr)
            throws IllegalArgumentException;

    /**
     * creates an atom (e.g. distance(a,2,3))
     * @param id identifier of Atom
     * @param params list of parameters for that atom
     * @return an atom
     * @throws IllegalArgumentException in case the parameter id is a Value or the arguments of
     * the list args aren't all of Type Term
     */
    Atom createAtom(Identifier id, List <Term> params)
            throws IllegalArgumentException;

    /**
     * creates an compund molecule (e.g. a[b hasValue c]memberOf d).
     * @param molecules a list of molecules that shall be grouped
     * @return A CompoundMolecule
     * @throws IllegalArgumentException in case:
     * <li> the list does not only contain molecules or contains has less then 2 molecules
     * <li> not all contained molecules have the same identifier
     * <li> both MemberShip and SubConcept molecules are contained  
     */
    CompoundMolecule createCompoundMolecule(List <Molecule> molecules)
            throws IllegalArgumentException;

    /**
     * Creates a simple molecule of the form: a memberOf b
     * @param idInstance A Term identifying the Molecule (left parameter)
     * @param idConcept A Term identifying the Concept the idInstance is member of
     * @return A MemberShipMolecule
     * @throws IllegalArgumentException if one of the arguments is null
     */
    MembershipMolecule createMemberShipMolecule(Term idInstance, Term idConcept)
            throws IllegalArgumentException;

    /**
     * Creates a compound molecule of the form: a memberOf {b,c}.
     * This is just a convinience method for creating the compoung molecule
     * using other existing methods of this factory
     * @param idInstance A Term identifying the Molecule (left parameter)
     * @param idConcept A list of terms identifying the concepts that idInstance is member of
     * @return A CompoundMolecule
     * @throws IllegalArgumentException if one of the arguments is null, or idConcepts 
     * contains less then 2 terms or an object that is not a term 
     */
    CompoundMolecule createMemberShipMolecules(Term idInstance, List idConcepts)
            throws IllegalArgumentException;

    /**
     * Creates a simple molecule of the form: a subConceptOf b
     * @param idConcept A Term identifying the Molecule (left parameter)
     * @param idSuperConcept A Term identifying the super concept of IdConcept
     * @return A MemberShipMolecule
     * @throws IllegalArgumentException if one of the arguments is null
     */
    SubConceptMolecule createSubConceptMolecule(Term idConcept, Term idSuperConcept)
            throws IllegalArgumentException;
    
    /**
     * Creates a simple molecule of the form: a subConceptOf {b,c}
     * @param idConcept A Term identifying the Molecule (left parameter)
     * @param idSuperConcepts A list of terms identifying the super concepts of IdConcept
     * @return A CompoundMolecule
     * @throws IllegalArgumentException if one of the arguments is null, or if
     * idConcepts has less then 2 elements or one of them is not a Term.
     */
    CompoundMolecule createSubConceptMolecules(Term idConcept, List idSuperConcept)
            throws IllegalArgumentException;

    /**
     * Creates a simple molecule of the form: john[age hasValue 2]
     * @param instanceID A Term identifying the Molecule (left parameter)
     * @param attributeID A Term identifying the Attribute of the Molecule
     * @param attributeValue A Term identifying the Value of the attributeID
     * @return AttributeValueMolecule
     * @throws IllegalArgumentException in case one of the terms is null
     */
    AttributeValueMolecule createAttributeValue(Term instanceID, Term attributeID, Term attributeValue)
            throws IllegalArgumentException;

    /**
     * Creates a compound molecule of the form: john[relative hasValue {mary,lisa}]
     * @param instanceID A Term identifying the Molecule (left parameter)
     * @param attributeID A Term identifying the Attribute of the Molecule
     * @param attributeValue A List of terms identifying the Values of the attributeID
     * @return CompoundMolecule
     * @throws IllegalArgumentException in case one of the parameters is null, or attributeValues
     * contains less then 2 elements or one element in the list is not a term.
     */
    CompoundMolecule createAttribusteValues(Term instanceID, Term attributeID, List attributeValues)
            throws IllegalArgumentException;
    
    /**
     * Creates a simple molecule of the form: john[age ofType _integer]
     * @param instanceID A Term identifying the Molecule (left parameter)
     * @param attributeID A Term identifying the Attribute of the Molecule
     * @param attributeType A Term identifying the range of attributeID
     * @return AttributeConstraintMolecule
     * @throws IllegalArgumentException in case one of the terms is null
     */
    AttributeConstraintMolecule createAttributeConstraint(Term instanceID, Term attributeID, Term attributeType)
            throws IllegalArgumentException;

    /**
     * Creates a compund molecule of the form: john[age ofType {human,man}]
     * @param instanceID A Term identifying the Molecule (left parameter)
     * @param attributeID A Term identifying the Attribute of the Molecule
     * @param attributeTypes A List of terms identifying the range of attributeID
     * @return CompoundMolecule
     * @throws IllegalArgumentException in case one of the terms is null or attributeTypes 
     * is null, contains less then 2 molecules or an object that is not a term.
     */
    CompoundMolecule createAttributeConstraints(Term instanceID, Term attributeID, List attributeTypes)
            throws IllegalArgumentException;

    /**
     * Creates a simple molecule of the form: john[anchestor impliesType human]
     * @param instanceID A Term identifying the Molecule (left parameter)
     * @param attributeID A Term identifying the Attribute of the Molecule
     * @param attributeType A Term identifying the range of attributeID
     * @return CompoundMolecule
     * @throws IllegalArgumentException in case one of the terms is null
     */
    AttributeInferenceMolecule createAttributeInference(Term instanceID, Term attributeID, Term attributeType)
            throws IllegalArgumentException;

    /**
     * Creates a compund molecule of the form: john[anchestor impliesType {human,man}]
     * @param instanceID A Term identifying the Molecule (left parameter)
     * @param attributeID A Term identifying the Attribute of the Molecule
     * @param attributeTypes A List of terms identifying the range of attributeID
     * @return CompoundMolecule
     * @throws IllegalArgumentException in case one of the terms is null
     */
    CompoundMolecule createAttributeInferences(Term instanceID, Term attributeID, List attributeType)
            throws IllegalArgumentException;

    /**
     * creates a constructed term (e.g. "_date(2005,2,2)")
     *
     * @param functionSymbol identifier of the constructed term
     * @param terms arguments of the constructed term
     * @return a constructed term
     * @throws IllegalArgumentException in case the functionSymbol is null or the arguments of
     * the list aren't all of Type Term
     */
    ConstructedTerm createConstructedTerm(IRI functionSymbol, List <Term> terms)
            throws IllegalArgumentException;

    /**
     * creates a numbered anonymous ID (e.g. "_#2")
     * @param number the number of the anonymous ID
     * @return a numbered anonymous id
     */
    NumberedAnonymousID createAnonymousID(byte number);
    
    /**
     * Creates a new Variable instance
     * @param varName The name of the new variable
     * @return A newly created variable
     */
    Variable createVariable(String varName);
}
/*
 * $Log: LogicalExpressionFactory.java,v $
 * Revision 1.17  2007/04/02 12:13:15  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.16  2006/06/21 07:46:13  vassil_momtchev
 * createVariable(String) method moved from WsmoFactory to LogicalExpressionFactory interface
 *
 * Revision 1.15  2006/02/16 19:40:28  holgerlausen
 * added convinience methods to logical expression factory:
 * RFE 113501
 *
 * Revision 1.14  2005/09/21 06:28:56  holgerlausen
 * removing explicit factory creations and introducing parameter maps for parsers
 *
 * Revision 1.13  2005/09/20 13:21:31  holgerlausen
 * refactored logical expression API to have simple molecules and compound molecules (RFE 1290043)
 *
 * Revision 1.12  2005/09/09 11:58:20  holgerlausen
 * fixed header logexp no longer extension
 *
 * Revision 1.11  2005/09/09 10:45:51  marin_dimitrov
 * formatting
 *
 * Revision 1.10  2005/09/09 06:32:42  holgerlausen
 * organize imports
 *
 * Revision 1.9  2005/09/07 13:13:28  holgerlausen
 * Equation -> Equivalence (the former sounds more like an atom (a=b))
 *
 * Revision 1.8  2005/09/06 18:23:53  holgerlausen
 * removed createSimpleTypes from LogicalExpressionFactory
 * splited DataValue into simple and complex value classes
 * removed explicit classes for simple datavalues (now all org.omwg.ontology.SimpledataValue)
 * adopted Term visitor to new data structure
 *
 * Revision 1.7  2005/09/02 13:32:44  ohamano
 * move logicalexpression packages from ext to core
 * move tests from logicalexpression.test to test module
 *
 * Revision 1.17  2005/09/02 09:43:28  ohamano
 * integrate wsmo-api and le-api on api and reference implementation level; full parser, serializer and validator integration will be next step
 *
 * Revision 1.16  2005/08/30 14:14:20  haselwanter
 * Merging LE API to HEAD.
 *
 * Revision 1.15.2.2  2005/08/30 11:50:28  haselwanter
 * Adapting javadocs.
 *
 * Revision 1.15.2.1  2005/08/29 14:35:02  haselwanter
 * Additional creators for the added interfaces.
 *
 * Revision 1.15  2005/08/19 14:37:45  nathaliest
 * JavaDoc added
 *
 * Revision 1.14  2005/08/18 16:15:40  nathaliest
 * JavaDoc added
 *
 * Revision 1.13  2005/08/16 16:28:29  nathaliest
 * JavaDoc added
 * Method getArgument(int) at UnaryImpl, QuantifiedImpl and BinaryImpl changed
 * Method equals(Object) at QuantifiedImpl changed
 *
 * Revision 1.12  2005/08/09 08:34:35  holgerlausen
 * check now for valid varname in VariableImpl
 *
 * Revision 1.11  2005/08/02 19:48:04  holgerlausen
 * AnonymousID->UnNbAnonymousID
 * fixed all hash codes, such that one can rely on HasSet.equal()
 * added serializedUID
 *
 * Revision 1.10  2005/08/02 15:25:42  holgerlausen
 * Vector -> List IF
 * no InvalidModelExcpetion anymore
 *
 * Revision 1.9  2005/07/29 07:27:45  ohamano
 * organize imports
 *
 * Revision 1.8  2005/07/24 18:38:26  holgerlausen
 * WSMLInterger ~ BigInter
 * added WrappedParserException for logexp parsing
 * delete SQName -> IRI with namespace heuristics as discussed [1]
 * implemented equals for logexp (non equality reasoning but simple term identity)
 * [1] http://sourceforge.net/tracker/index.php?func=detail&aid=1225411&group_id=113501&atid=665349
 *
 * Revision 1.7  2005/07/18 13:59:06  holgerlausen
 * refactored: createLEXXX ->  createWSMLXXX
 * refined integration test (brakes on serializing)
 * added unit testing to build xml
 *
 * Revision 1.6  2005/07/13 12:37:56  holgerlausen
 * removed interfaces for SimpleDataType and aligned with wsmo4j
 * XXXValue -> WSMLXXX
 *
 * Revision 1.5  2005/07/13 09:32:37  holgerlausen
 * changed to compile with main wsmo4j / changed correspondence of wsml2java types
 *
 * Revision 1.4  2005/06/22 13:32:01  ohamano
 * change header
 *
 * Revision 1.3  2005/06/21 14:01:43  holgerlausen
 * compliant to new interface
 *
 * Revision 1.2  2005/06/20 08:30:03  holgerlausen
 * formating
 *
 */