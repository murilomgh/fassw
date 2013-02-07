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
package org.deri.wsmo4j.factory;


import java.util.*;

import org.deri.wsmo4j.io.parser.wsml.*;
import org.deri.wsmo4j.logicalexpression.*;
import org.deri.wsmo4j.logicalexpression.terms.*;
import org.omwg.logicalexpression.*;
import org.omwg.logicalexpression.terms.*;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.factory.*;
import org.wsmo.wsml.*;

import com.ontotext.wsmo4j.ontology.*;


/**
 * <pre>
 *
 *  Created on Jun 20, 2005
 *  Committed by $Author: morcen $
 *
 * </pre>
 *
 * @author reto.krummenacher@deri.org
 * @author holger.lausen@deri.org
 * @author thomas.haselwanter@deri.org
 * @version $Revision: 1.13 $ $Date: 2007/04/02 12:13:24 $
 * @see org.omwg.logexpression.LogicalExpressionFactory
 */
public class LogicalExpressionFactoryImpl
        extends AbstractLogicalExpressionFactoryImpl{

    private Map <String, Object> parserProperties = new HashMap <String, Object>();

    /**
     * Creates a default LogicalExpressionFactory. The LogicalExpressionFactory used by default
     * handles logical expressions as plain strings, instead of objects
     * (variables, molecules, operators,...)
     * @see org.wsmo.factory.Factory#createLogicalExpressionFactory(Map)
     */

    public LogicalExpressionFactoryImpl() {
        this(null);
    }

    /**
     * Creates a LogicalExpressionFactory. The LogicalExpressionFactory used by default
     * handles logical expressions as plain strings, instead of objects
     * (variables, molecules, operators,...)
     * The LogicalExpressionFactory is initialised based on the supplied preferences.
     * The properties map can contain the factories to be used as Strings or as
     * instances. If a factory to use is only indicated as String, the constructor
     * needs to create an instance of this given factory.
     *
     * @param map properties used to create the LogicalExpressionFactory, if properties==null,
     * the default LogicalExpressionFactory is used
     * @see org.wsmo.factory.Factory#createLogicalExpressionFactory(Map)
     */
    public LogicalExpressionFactoryImpl(Map properties) {
        WsmoFactory factory = null;
        if (properties != null && properties.containsKey(LEFACTORY_WSMO_FACTORY)) {
            factory = (WsmoFactory) properties.get(LEFACTORY_WSMO_FACTORY);
        }
        //use default factory if none is given
        if (factory == null) {
            factory = Factory.createWsmoFactory(null);
        }
        this.parserProperties.put(Factory.WSMO_FACTORY, factory);

        DataFactory dataFactory = null;
        if (properties != null && properties.containsKey(LEFACTORY_DATA_FACTORY)) {
            dataFactory = (DataFactory) properties.get(LEFACTORY_DATA_FACTORY);
        }
        //use default factory if none is given
        if (dataFactory == null || !(factory instanceof DataFactory)) {
            dataFactory = Factory.createDataFactory(null);
        }
        this.parserProperties.put(Factory.DATA_FACTORY, dataFactory);

        this.parserProperties.put(Factory.LE_FACTORY, this);
    }

    /**
     * Creates a logical expression object model from a string. Note: only works
     * if no sqname is in the logical expression if sqname is there namespace
     * information are required
     * @param expr String representation of a Logical Expression
     * @return logical expression object model
     * @throws ParserException provides information about where and why the parse process failed
     */
    public LogicalExpression createLogicalExpression(String expr)
            throws ParserException {
        return createLogicalExpression(expr, null);
    }

    /**
     * Creates a logical expression object model from a string.
     * @param expr String representation of a Logical Expression
     * @param nsHolder TopEntity
     * @return logical expression object model
     * @throws ParserException provides information about where and why the parse process failed
     */
    public LogicalExpression createLogicalExpression(String expr,
            TopEntity nsHolder)
            throws ParserException {
        LogExprParserImpl leParser = new LogExprParserImpl(parserProperties, nsHolder);
        return leParser.parse(expr);
    }

    /**
     * @param id identifier of Atom
     * @param params list of parameters for that atom
     * @return an atom
     * @throws IllegalArgumentException in case the parameter id is a Value or the arguments of
     * the list args aren't all of Type Term
     * @see org.omwg.logexpression.LogicalExpressionFactory#createAtom(org.omwg.logexpression.terms.Identifier, java.util.List)
     */
    public Atom createAtom(Identifier id, List <Term> params)
            throws IllegalArgumentException {
        if (ConstantTransformer.getInstance().isBuiltInAtom(id.toString())) {
            return new BuiltInAtomImpl(id, params);
        }
        else {
            return new AtomImpl(id, params);
        }

    }

    /**
     * @see LogicalExpressionFactory#createCompoundMolecule(List)
     */
    public CompoundMolecule createCompoundMolecule(List <Molecule> molecules)
            throws IllegalArgumentException {
        return new CompoundMoleculeImpl(molecules);
    }

    /**
     * @see LogicalExpressionFactory#createSubConceptMolecule(List)
     */
    public SubConceptMolecule createSubConceptMolecule(Term identifier, Term superConcept){
        return new SubConceptMoleculeImpl(identifier, superConcept);
    }

    /**
     * @see LogicalExpressionFactory#createMemberShipMolecule(Term, Term)
     */
    public MembershipMolecule createMemberShipMolecule(Term identifier, Term concept){
        return new MemberShipMoleculeImpl(identifier, concept);
    }

    /**
     * @see LogicalExpressionFactory#createAttributeValue(Term, Term, Term)
     */
    public AttributeValueMolecule createAttributeValue(Term leftTerm, Term attributeName, Term rightTerm)
            throws IllegalArgumentException {
        return new AttributeValueMoleculeImpl(leftTerm, attributeName, rightTerm);
    }

    /**
     * @see LogicalExpressionFactory#createAttributeConstraint(Term, Term, Term)
     */
    public AttributeConstraintMolecule createAttributeConstraint(Term leftTerm, Term attributeName, Term rightTerm)
            throws IllegalArgumentException {
        return new AttributeConstraintMoleculeImpl(leftTerm, attributeName, rightTerm);
    }

    /**
     * @see LogicalExpressionFactory#createAttributeInference(Term, Term, Term)
     */
    public AttributeInferenceMolecule createAttributeInference(Term leftTerm, Term attributeName, Term rightTerm)
            throws IllegalArgumentException {
        return new AttributeInferenceMoleculeImpl(leftTerm, attributeName, rightTerm);
    }

    /**
     * @param functionSymbol identifier of the constructed term
     * @param terms arguments of the constructed term
     * @return a constructed term
     * @throws IllegalArgumentException in case the functionSymbol is null or the arguments of
     * the list aren't all of Type Term
     * @see org.omwg.logexpression.LogicalExpressionFactory#createConstructedTerm(IRI, List)
     */
    public ConstructedTerm createConstructedTerm(IRI functionSymbol, List <Term> terms)
            throws IllegalArgumentException {
        if (ConstantTransformer.getInstance().isBuiltInFunctionSymbol(functionSymbol.toString())) {
            return new BuiltInConstructedTermImpl(functionSymbol, terms);
        }
        else {
            return new ConstructedTermImpl(functionSymbol, terms);
        }

    }

    /**
     * @param number the number of the anonymous id
     * @return a numbered anonymous id
     * @see org.omwg.logexpression.LogicalExpressionFactory#createAnonymousID(byte)
     */
    public NumberedAnonymousID createAnonymousID(byte number) {
        return new NumberedAnonymousIDImpl(number);
    }

    /**
     * @param expr the expression that is affected by the operator
     * @return unary expression
     * @throws in case the logical expression contains a nested CONSTRAINT
     * @see org.omwg.logexpression.LogicalExpressionFactory#createNegation(LogicalExpression)
     */
    public Negation createNegation(LogicalExpression expr)
            throws IllegalArgumentException {
        return new NegationImpl(expr);
    }

    /**
     * @param expr the expression that is affected by the operator
     * @return unary expression
     * @throws in case the logical expression contains a nested CONSTRAINT
     * @see org.omwg.logexpression.LogicalExpressionFactory#createNegationAsFailure(LogicalExpression)
     */
    public NegationAsFailure createNegationAsFailure(LogicalExpression expr)
            throws IllegalArgumentException {
        return new NegationAsFailureImpl(expr);
    }

    /**
     * @param expr the expression that is affected by the operator
     * @return unary expression
     * @throws in case the logical expression contains a nested CONSTRAINT
     * @see org.omwg.logexpression.LogicalExpressionFactory#createConstraint(LogicalExpression)
     */
    public Constraint createConstraint(LogicalExpression expr)
            throws IllegalArgumentException {
        return new ConstraintImpl(expr);
    }

    /**
     * @param exprLeft the left expression that is conected by the operator
     * @param exprRight the right expression that is conected by the operator
     * @return binary expression
     * @throws IllegalArgumentException
     * <p>in case one of the two logical expressions is null
     * <p>in case one of the two logical expressions contains a nested CONSTRAINT</p>
     * @see org.omwg.logexpression.LogicalExpressionFactory#createConjunction(LogicalExpression, LogicalExpression)
     */
    public Conjunction createConjunction(LogicalExpression exprLeft, LogicalExpression exprRight)
            throws IllegalArgumentException {
        return new ConjunctionImpl(exprLeft, exprRight);
    }

    /**
     * @param exprLeft the left expression that is conected by the operator
     * @param exprRight the right expression that is conected by the operator
     * @return binary expression
     * @throws IllegalArgumentException
     * <p>in case one of the two logical expressions is null
     * <p>in case one of the two logical expressions contains a nested CONSTRAINT</p>
     * @see org.omwg.logexpression.LogicalExpressionFactory#createDisjunction(LogicalExpression, LogicalExpression)
     */
    public Disjunction createDisjunction(LogicalExpression exprLeft, LogicalExpression exprRight)
            throws IllegalArgumentException {
        return new DisjunctionImpl(exprLeft, exprRight);
    }

    /**
     * @param exprLeft the left expression that is conected by the operator
     * @param exprRight the right expression that is conected by the operator
     * @return binary expression
     * @throws IllegalArgumentException
     * <p>in case one of the two logical expressions is null
     * <p>in case one of the two logical expressions contains a nested CONSTRAINT</p>
     * @see org.omwg.logexpression.LogicalExpressionFactory#createImplication(LogicalExpression, LogicalExpression)
     */
    public Implication createImplication(LogicalExpression exprLeft, LogicalExpression exprRight)
            throws IllegalArgumentException {
        return new ImplicationImpl(exprLeft, exprRight);
    }

    /**
     * @param exprLeft the left expression that is conected by the operator
     * @param exprRight the right expression that is conected by the operator
     * @return binary expression
     * @throws IllegalArgumentException
     * <p>in case one of the two logical expressions is null
     * <p>in case one of the two logical expressions contains a nested CONSTRAINT</p>
     * @see org.omwg.logexpression.LogicalExpressionFactory#createEquivalence(LogicalExpression, LogicalExpression)
     */
    public Equivalence createEquivalence(LogicalExpression exprLeft, LogicalExpression exprRight)
            throws IllegalArgumentException {
        return new EquationImpl(exprLeft, exprRight);
    }

    /**
     * @param exprLeft the left expression that is conected by the operator
     * @param exprRight the right expression that is conected by the operator
     * @return binary expression
     * @throws IllegalArgumentException
     * <p>in case one of the two logical expressions is null
     * <p>in case one of the two logical expressions contains a nested CONSTRAINT</p>
             * @see org.omwg.logexpression.LogicalExpressionFactory#createLogicProgrammingRule(LogicalExpression, LogicalExpression)
     */
    public LogicProgrammingRule createLogicProgrammingRule(LogicalExpression exprLeft, LogicalExpression exprRight)
            throws IllegalArgumentException {
        return new LogicProgrammingRuleImpl(exprLeft, exprRight);
    }

    /**
     * @param exprLeft the left expression that is conected by the operator
     * @param exprRight the right expression that is conected by the operator
     * @return binary expression
     * @throws IllegalArgumentException
     * <p>in case one of the two logical expressions is null
     * <p>in case one of the two logical expressions contains a nested CONSTRAINT</p>
             * @see org.omwg.logexpression.LogicalExpressionFactory#createInverseImplication(LogicalExpression, LogicalExpression)
     */
    public InverseImplication createInverseImplication(LogicalExpression exprLeft, LogicalExpression exprRight)
            throws IllegalArgumentException {
        return new InverseImplicationImpl(exprLeft, exprRight);
    }

    /**
     * @param variables Set of variables that are quantified (e.g. "?a")
     * @param expr the expression that is quantified
     * @return a universally quantified expression
     * @throws IllegalArgumentException
     * <p>in case the logical expression contains a nested CONSTRAINT</p>
     * <p>in case the Set of variables is null</p>
     * <p>in case the arguments of the list aren't all of Type Variable</p>
     * @see org.omwg.logexpression.LogicalExpressionFactory#createUniversalQuantification(Set, LogicalExpression)
     */
    public UniversalQuantification createUniversalQuantification(Set <Variable> variables, LogicalExpression expr)
            throws IllegalArgumentException {
        return new UniversalQuantificationImpl(variables, expr);
    }

    /**
     * @param variable variable that is quantified (e.g. "?a")
     * @param expr the expression that is quantified
     * @return a universally quantified expression
     * @throws IllegalArgumentException
     * <p>in case the logical expression contains a nested CONSTRAINT</p>
     * <p>in case the Set of variables is null</p>
     * <p>in case the arguments of the list aren't all of Type Variable</p>
     * @see org.omwg.logexpression.LogicalExpressionFactory#createUniversalQuantification(Variable, LogicalExpression)
     */
    public UniversalQuantification createUniversalQuantification(Variable variable, LogicalExpression expr)
            throws IllegalArgumentException {
        return new UniversalQuantificationImpl(variable, expr);
    }

    /**
     * @param variables Set of variables that are quantified (e.g. "?a")
     * @param expr the expression that is quantified
     * @return a existentially quantified expression
     * @throws IllegalArgumentException
     * <p>in case the logical expression contains a nested CONSTRAINT</p>
     * <p>in case the Set of variables is null</p>
     * <p>in case the arguments of the list aren't all of Type Variable</p>
     * @see org.omwg.logexpression.LogicalExpressionFactory#createExistentialQuantification(Set, LogicalExpression)
     */
    public ExistentialQuantification createExistentialQuantification(Set <Variable> variables, LogicalExpression expr)
            throws IllegalArgumentException {
        return new ExistentialQuantificationImpl(variables, expr);
    }

    /**
     * @param variable variable that is quantified (e.g. "?a")
     * @param expr the expression that is quantified
     * @return a existentially quantified expression
     * @throws IllegalArgumentException
     * <p>in case the logical expression contains a nested CONSTRAINT</p>
     * <p>in case the Set of variables is null</p>
     * <p>in case the arguments of the list aren't all of Type Variable</p>
             * @see org.omwg.logexpression.LogicalExpressionFactory#createExistentialQuantification(Variable, LogicalExpression)
     */
    public ExistentialQuantification createExistentialQuantification(Variable variable, LogicalExpression expr)
            throws IllegalArgumentException {
        return new ExistentialQuantificationImpl(variable, expr);
    }
    
    /**
     * Creates a new Variable instance
     * @param varName The name of the new variable
     * @return A newly created variable
     */
    public Variable createVariable(String varName) {
        if (varName == null
                || varName.trim().length() == 0) {
            throw new IllegalArgumentException();
        }
        if (varName.startsWith("?")) {
            varName = varName.substring(1);
        }
        return new VariableImpl(varName);
    }
}