/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2004-2005, OntoText Lab. / SIRMA

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

/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2004-2005, OntoText Lab. / SIRMA

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

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 */

package org.deri.wsmo4j.io.parser.wsml;

import java.util.*;

import org.deri.wsmo4j.io.parser.*;
import org.omwg.logicalexpression.*;
import org.omwg.logicalexpression.terms.*;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.factory.*;
import org.wsmo.mediator.*;
import org.wsmo.service.choreography.rule.*;
import org.wsmo.service.orchestration.*;
import org.wsmo.service.orchestration.rule.*;
import org.wsmo.service.rule.*;
import org.wsmo.wsml.*;
import org.wsmo.wsml.compiler.node.*;

import com.ontotext.wsmo4j.parser.*;
import com.ontotext.wsmo4j.parser.wsml.*;

@SuppressWarnings("unchecked")
public class RuleAnalyzer extends ASTAnalysis {

    private WsmoFactory wsmoFactory;
    private ChoreographyFactory factory; 
    private OrchestrationFactory oFactory;
    private LogicalExpressionFactory leFactory;
    private ASTAnalysisContainer container;
    private Stack ruleStack;
    private Stack ruleListStack;

    public RuleAnalyzer(ASTAnalysisContainer container, WsmoFactory wsmoFactory,
            OrchestrationFactory oFactory, ChoreographyFactory factory, 
            LogicalExpressionFactory leFactory) {
        if (container == null || factory == null || leFactory == null ||
                oFactory == null || wsmoFactory == null) {
            throw new IllegalArgumentException();
        }
        this.container = container;
        this.factory = factory;
        this.oFactory = oFactory;
        this.leFactory = leFactory;
        this.wsmoFactory = wsmoFactory;

        // register the handled nodes
        container.registerNodeHandler(ATransitions.class, this);
        container.registerNodeHandler(AOrchestrationTransitions.class, this);
        container.registerNodeHandler(AIfRule.class, this);
        container.registerNodeHandler(AOrchIfOrchestrationRule.class, this);
        container.registerNodeHandler(AChooseRule.class, this);
        container.registerNodeHandler(AOrchChooseOrchestrationRule.class, this);
        container.registerNodeHandler(AForallRule.class, this);
        container.registerNodeHandler(AOrchForallOrchestrationRule.class, this);
        container.registerNodeHandler(AOrchPerformOrchestrationRule.class, this);
        container.registerNodeHandler(AOrchApplyMediationOrchPerformAlt.class, this);
        container.registerNodeHandler(AOrchInvokeServiceOrchPerformAlt.class, this);
        container.registerNodeHandler(AOrchPerformAchievegoalOrchPerformAlt.class, this);
        container.registerNodeHandler(AOrchPerformSendOrchPerformAlt.class, this);
        container.registerNodeHandler(AOrchPerformReceiveOrchPerformAlt.class, this);
        container.registerNodeHandler(ARestrictedLeCondition.class, this);
        container.registerNodeHandler(AUpdaterule.class, this);
        container.registerNodeHandler(AFactPreferredFact.class, this);
        container.registerNodeHandler(AFactNonpreferredFact.class, this);
        container.registerNodeHandler(AFactMoleculeFact.class, this);
        container.registerNodeHandler(AFactRelationFact.class, this);
        container.registerNodeHandler(ASingleTermUpdate.class, this);
        container.registerNodeHandler(AMoveTermUpdate.class, this);
        container.registerNodeHandler(AAttrFactList.class, this);
        container.registerNodeHandler(AAttrRelationAttrFactList.class, this);
        container.registerNodeHandler(APpmediator.class, this);
        ruleStack = container.getStack(ChoreographyRule.class);
        /* to overcome the problem that ChoreographyRules and OrchestrationRules
        do not have a common super interface, a random type is used to identify
        the stack of Choreography/OrchestratonRules */
        ruleListStack = container.getStack(RuleAnalyzer.class);
    }

    // Rules

    private Object lastRule;

    public void inATransitions(ATransitions node) {
        ChoreographyRules rules = null;
        Stack identifierStack = container.getStack(Identifier.class);
        if (node.getId() instanceof AAnonymousId || node.getId() == null) {
            rules = factory.containers.createRules(wsmoFactory.createAnonymousID(),
                    new HashSet<ChoreographyRule>());
        }
        else {
            node.getId().apply(container.getNodeHandler(PId.class));
            rules = factory.containers
                    .createRules((IRI) identifierStack.pop(), new HashSet<ChoreographyRule>());
        }
        ruleListStack.push(rules);

        // mark the last rule
        if (ruleStack.isEmpty()) {
            lastRule = null;
            return;
        }
        lastRule = ruleStack.peek();
    }
    
    public void inAOrchestrationTransitions(AOrchestrationTransitions node) {
        OrchestrationRules rules = null;
        Stack identifierStack = container.getStack(Identifier.class);
        if (node.getId() instanceof AAnonymousId || node.getId() == null) {
            rules = oFactory.containers.createRules(wsmoFactory.createAnonymousID(),
                    new HashSet<Rule>());
        }
        else {
            node.getId().apply(container.getNodeHandler(PId.class));
            rules = oFactory.containers
                    .createRules((IRI) identifierStack.pop(), new HashSet<Rule>());
        }
        ruleListStack.push(rules);

        // mark the last rule
        if (ruleStack.isEmpty()) {
            lastRule = null;
            return;
        }
        lastRule = ruleStack.peek();
    }

    // Transition rule

    public void inAIfRule(AIfRule node) {
        ChoreographyIfThen rule = factory.transitionRules.createIfThen(null, new HashSet<ChoreographyRule>());
        if (lastRule == null && !ruleStack.isEmpty()) { // if inner rule added to the parent
            ((ChoreographyTransitionRule) ruleStack.peek()).addRule(rule);
        }
        else {
            ((ChoreographyRules) ruleListStack.peek()).addRule(rule);
        }
        ruleStack.push(rule);
    }
    
    public void inAOrchIfOrchestrationRule(AOrchIfOrchestrationRule node) {
        OrchestrationIfThen rule = oFactory.transitionRules.createIfThen(null, new HashSet<Rule>());
        if (lastRule == null && !ruleStack.isEmpty()) { // if inner rule added to the parent
            ((OrchestrationTransitionRule) ruleStack.peek()).addRule(rule);
        }
        else {
            ((OrchestrationRules) ruleListStack.peek()).addRule(rule);
        }
        ruleStack.push(rule);
    }

    public void outAIfRule(AIfRule node) {
        Condition con = (Condition) container.popFromStack(Condition.class, Condition.class);
        ((ChoreographyIfThen) ruleStack.pop()).setCondition(con);
    }
    
    public void outAOrchIfOrchestrationRule(AOrchIfOrchestrationRule node) {
        Condition con = (Condition) container.popFromStack(Condition.class, Condition.class);
        ((OrchestrationIfThen) ruleStack.pop()).setCondition(con);
    }

    public void inAChooseRule(AChooseRule node) {
        ChoreographyChoose rule = factory.transitionRules.createChoose(new HashSet<Variable>(), null,
                new HashSet<ChoreographyRule>());
        if (lastRule == null && !ruleStack.isEmpty()) { // if inner rule added to the parent
            ((ChoreographyTransitionRule) ruleStack.peek()).addRule(rule);
        }
        else {
            ((ChoreographyRules) ruleListStack.peek()).addRule(rule);
        }
        ruleStack.push(rule);
    }
    
    public void inAOrchChooseOrchestrationRule(AOrchChooseOrchestrationRule node) {
        OrchestrationChoose rule = oFactory.transitionRules.createChoose(new HashSet<Variable>(), null,
                new HashSet<Rule>());
        if (lastRule == null && !ruleStack.isEmpty()) { // if inner rule added to the parent
            ((OrchestrationTransitionRule) ruleStack.peek()).addRule(rule);
        }
        else {
            ((OrchestrationRules) ruleListStack.peek()).addRule(rule);
        }
        ruleStack.push(rule);
    }

    public void outAChooseRule(AChooseRule node) {
        Variable[] vars = (Variable[]) container.popFromStack(Variable[].class, Variable[].class);
        Condition con = (Condition) container.popFromStack(Condition.class, Condition.class);
        ChoreographyChoose rule = (ChoreographyChoose) ruleStack.pop();
        for (int i = 0; i < vars.length; i++) {
            rule.addVariable(vars[i]);
        }
        rule.setCondition(con);
    }
    
    public void outAOrchChooseOrchestrationRule(AOrchChooseOrchestrationRule node) {
        Variable[] vars = (Variable[]) container.popFromStack(Variable[].class, Variable[].class);
        Condition con = (Condition) container.popFromStack(Condition.class, Condition.class);
        OrchestrationChoose rule = (OrchestrationChoose) ruleStack.pop();
        for (int i = 0; i < vars.length; i++) {
            rule.addVariable(vars[i]);
        }
        rule.setCondition(con);
    }

    public void inAForallRule(AForallRule node) {
        ChoreographyForAll rule = factory.transitionRules.createForAll(new HashSet<Variable>(), null,
                new HashSet<ChoreographyRule>());
        if (lastRule == null && !ruleStack.isEmpty()) { // if inner rule added to the parent
            ((ChoreographyTransitionRule) ruleStack.peek()).addRule(rule);
        }
        else {
            ((ChoreographyRules) ruleListStack.peek()).addRule(rule);
        }
        ruleStack.push(rule);
    }
    
    public void inAOrchForallOrchestrationRule(AOrchForallOrchestrationRule node) {
        OrchestrationForAll rule = oFactory.transitionRules.createForAll(new HashSet<Variable>(), null,
                new HashSet<Rule>());
        if (lastRule == null && !ruleStack.isEmpty()) { // if inner rule added to the parent
            ((OrchestrationTransitionRule) ruleStack.peek()).addRule(rule);
        }
        else {
            ((OrchestrationRules) ruleListStack.peek()).addRule(rule);
        }
        ruleStack.push(rule);
    }

    public void outAForallRule(AForallRule node) {
        Variable[] vars = (Variable[]) container.popFromStack(Variable[].class, Variable[].class);
        Condition con = (Condition) container.popFromStack(Condition.class, Condition.class);
        ChoreographyForAll rule = (ChoreographyForAll) ruleStack.pop();
        for (int i = 0; i < vars.length; i++) {
            rule.addVariable(vars[i]);
        }
        rule.setCondition(con);
    }
    
    public void outAOrchForallOrchestrationRule(AOrchForallOrchestrationRule node) {
        Variable[] vars = (Variable[]) container.popFromStack(Variable[].class, Variable[].class);
        Condition con = (Condition) container.popFromStack(Condition.class, Condition.class);
        OrchestrationForAll rule = (OrchestrationForAll) ruleStack.pop();
        for (int i = 0; i < vars.length; i++) {
            rule.addVariable(vars[i]);
        }
        rule.setCondition(con);
    }
    
    private IRI performId;
    
    public void inAOrchPerformOrchestrationRule(AOrchPerformOrchestrationRule node) {
        TopEntityAnalysis.isValidTopEntityIdentifier(node.getId(), node.getTPerform());
        node.getId().apply(container.getNodeHandler(PId.class));
        performId = (IRI) container.popFromStack(Identifier.class, Identifier.class);
    }
    
    public void inAOrchApplyMediationOrchPerformAlt(AOrchApplyMediationOrchPerformAlt node) {
        TopEntityAnalysis.isValidTopEntityIdentifier(node.getId(), node.getTApplymediation());
        node.getId().apply(container.getNodeHandler(PId.class));
        IRI iri = (IRI) container.popFromStack(Identifier.class, Identifier.class);
        OrchestrationApplyMediation rule = 
            oFactory.updateRules.createOrchestrationApplyMediation(performId, 
                    oFactory.updateRules.createPpMediator(iri));
        
        if (lastRule == null && !ruleStack.isEmpty()) { // if inner rule added to the parent
            ((OrchestrationTransitionRule) ruleStack.peek()).addRule(rule);
        }
        else {
            ((OrchestrationRules) ruleListStack.peek()).addRule(rule);
        }
        ruleStack.push(rule);
    }
    
    public void outAOrchApplyMediationOrchPerformAlt(AOrchApplyMediationOrchPerformAlt node) {
        ruleStack.pop();
    }
    
    public void inAOrchInvokeServiceOrchPerformAlt(AOrchInvokeServiceOrchPerformAlt node) {
        TopEntityAnalysis.isValidTopEntityIdentifier(node.getId(), node.getTInvokeservice());
        node.getId().apply(container.getNodeHandler(PId.class));
        IRI iri = (IRI) container.popFromStack(Identifier.class, Identifier.class);
        OrchestrationInvokeService rule = 
            oFactory.updateRules.createOrchestrationInvokeService(performId, 
                    wsmoFactory.getWebService(iri));
        
        if (lastRule == null && !ruleStack.isEmpty()) { // if inner rule added to the parent
            ((OrchestrationTransitionRule) ruleStack.peek()).addRule(rule);
        }
        else {
            ((OrchestrationRules) ruleListStack.peek()).addRule(rule);
        }
        ruleStack.push(rule);
    }
    
    public void outAOrchInvokeServiceOrchPerformAlt(AOrchInvokeServiceOrchPerformAlt node) {
        ruleStack.pop();
    }
    
    public void inAOrchPerformAchievegoalOrchPerformAlt(AOrchPerformAchievegoalOrchPerformAlt node) {
        TopEntityAnalysis.isValidTopEntityIdentifier(node.getId(), node.getTAchievegoal());
        node.getId().apply(container.getNodeHandler(PId.class));
        IRI iri = (IRI) container.popFromStack(Identifier.class, Identifier.class);
        OrchestrationAchieveGoal rule = 
            oFactory.updateRules.createOrchestrationAchieveGoal(performId, 
                    wsmoFactory.getGoal(iri));
        
        if (lastRule == null && !ruleStack.isEmpty()) { // if inner rule added to the parent
            ((OrchestrationTransitionRule) ruleStack.peek()).addRule(rule);
        }
        else {
            ((OrchestrationRules) ruleListStack.peek()).addRule(rule);
        }
        ruleStack.push(rule);
    }
    
    public void outAOrchPerformAchievegoalOrchPerformAlt(AOrchPerformAchievegoalOrchPerformAlt node) {
        ruleStack.pop();
    }
    
    public void inAOrchPerformSendOrchPerformAlt(AOrchPerformSendOrchPerformAlt node) {
        TopEntityAnalysis.isValidTopEntityIdentifier(node.getId(), node.getTTarget());
        node.getId().apply(container.getNodeHandler(PId.class));
        IRI iri = (IRI) container.popFromStack(Identifier.class, Identifier.class);
        Send rule = 
            oFactory.updateRules.createSend(performId, iri);
        
        if (lastRule == null && !ruleStack.isEmpty()) { // if inner rule added to the parent
            ((OrchestrationTransitionRule) ruleStack.peek()).addRule(rule);
        }
        else {
            ((OrchestrationRules) ruleListStack.peek()).addRule(rule);
        }
        ruleStack.push(rule);
    }
    
    public void outAOrchPerformSendOrchPerformAlt(AOrchPerformSendOrchPerformAlt node) {
        ruleStack.pop();
    }
    
    public void inAOrchPerformReceiveOrchPerformAlt(AOrchPerformReceiveOrchPerformAlt node) {
        TopEntityAnalysis.isValidTopEntityIdentifier(node.getId(), node.getTSource());
        node.getId().apply(container.getNodeHandler(PId.class));
        IRI iri = (IRI) container.popFromStack(Identifier.class, Identifier.class);
        Receive rule =  oFactory.updateRules.createReceive(performId, iri);
        
        if (lastRule == null && !ruleStack.isEmpty()) { // if inner rule added to the parent
            ((OrchestrationTransitionRule) ruleStack.peek()).addRule(rule);
        }
        else {
            ((OrchestrationRules) ruleListStack.peek()).addRule(rule);
        }
        ruleStack.push(rule);
    }
    
    public void outAOrchPerformReceiveOrchPerformAlt(AOrchPerformReceiveOrchPerformAlt node) {
        ruleStack.pop();
    }

    // end Transition rule

    // Condition

    public void outARestrictedLeCondition(ARestrictedLeCondition node) {
        LogicalExpression expr = (LogicalExpression) container.popFromStack(
                LogicalExpression.class, LogicalExpression.class);
        
        try {
            Condition c = factory.transitionRules.createConditionFromLogicalExpression(expr);
            container.getStack(Condition.class).push(c);
        }
        catch (InvalidModelException ex) {
            ParserException pe = new ParserException(ex.getMessage(), null);
            throw new WrappedParsingException(pe);
        }
    }

    // end Condition
    
    private PModifier modifier;
    
    public void inAUpdaterule(AUpdaterule node) {
        modifier = node.getModifier();
    }

    public void outAUpdaterule(AUpdaterule node) {
        ChoreographyRule rule = null;
        Stack compoundStack = container.getStack(CompoundFact[].class);
        CompoundFact[] facts = (CompoundFact[]) compoundStack.pop();
        if (!facts[1].isEmpty() && node.getModifier() instanceof AUpdateModifier == false) {
            ParserException ex = new ParserException(
                    "Updates in add|delete(...) rules not allowed!", null);
            if (modifier instanceof AAddModifier) {
                ex.setErrorLine(((AAddModifier) modifier).getTAdd().getLine());
                ex.setErrorPos(((AAddModifier) modifier).getTAdd().getPos());
            }
            else if (modifier instanceof ADeleteModifier) {
                ex.setErrorLine(((ADeleteModifier) modifier).getTDelete().getLine());
                ex.setErrorPos(((ADeleteModifier) modifier).getTDelete().getPos());
            }
            throw new WrappedParsingException(ex);
        }
        if (node.getModifier() instanceof AAddModifier) {
            rule = factory.updateRules.createAdd(facts[0]);
        }
        else if (node.getModifier() instanceof ADeleteModifier) {
            rule = factory.updateRules.createDelete(facts[0]);
        }
        else { // AUpdatedModifier
            try {
                rule = factory.updateRules.createUpdate(facts[0], facts[1]);
            }
            catch (InvalidModelException ex) {
                throw new WrappedInvalidModelException(ex);
            }
        }

        if (ruleStack.isEmpty()) { // no parent = add it in the Rules
            ((ChoreographyRules) ruleListStack.peek()).addRule(rule);
        }
        else { // if parent = add it as a sub-rule to the parent
            if (ruleStack.peek() instanceof ChoreographyTransitionRule)
                ((ChoreographyTransitionRule) ruleStack.peek()).addRule(rule);
            else
                ((OrchestrationTransitionRule) ruleStack.peek()).addRule(rule);
        }

        // sanity check that all molecule buffers are empty
        assert newMembMolecules.size() + oldMembMolecules.size() + newAttrMolecules.size()
                + oldAttrMolecules.size() + newRelationParams.size() + oldRelationParams.size() == 0;
    }

    public void outAFactPreferredFact(AFactPreferredFact node) {
        processMoleculeFact(node.getFactUpdate(), true);
    }

    public void outAFactNonpreferredFact(AFactNonpreferredFact node) {
        processMoleculeFact(node.getFactUpdate(), true);
    }

    public void outAFactMoleculeFact(AFactMoleculeFact node) {
        processMoleculeFact(null, false);
    }
    
    public void inAFactRelationFact(AFactRelationFact node) {
        node.getId().apply(container.getNodeHandler(PId.class));
        relationId = (IRI) container.popFromStack(Identifier.class, IRI.class);
    }
    
    private Identifier relationId;
    
    public void outAFactRelationFact(AFactRelationFact node) {
        CompoundFact[] facts = new CompoundFact[2];
        facts[0] = factory.facts.createRelationFact(leFactory.createAtom(relationId, 
                new LinkedList<Term>(newRelationParams)));
        facts[1] = factory.facts.createRelationFact(leFactory.createAtom(relationId,
                new LinkedList<Term>(oldRelationParams)));
        container.getStack(CompoundFact[].class).push(facts);
        newRelationParams.clear();
        oldRelationParams.clear();
    }

    private void processMoleculeFact(PFactUpdate node, boolean hasMembershipMolecules) {
        if (hasMembershipMolecules) {
            processMembershipMolecules(node);
        }
        container.getStack(Term.class).pop(); // remove the instanceId
        CompoundFact[] facts = new MoleculeFact[2];
        facts[0] = factory.facts.createMoleculeFact(new LinkedHashSet<MembershipMolecule>(
                newMembMolecules), new LinkedHashSet<AttributeValueMolecule>(newAttrMolecules));
        facts[1] = factory.facts.createMoleculeFact(new LinkedHashSet<MembershipMolecule>(
                oldMembMolecules), new LinkedHashSet<AttributeValueMolecule>(oldAttrMolecules));
        container.getStack(CompoundFact[].class).push(facts);
        newAttrMolecules.clear();
        oldAttrMolecules.clear();
        if (hasMembershipMolecules) {
            newMembMolecules.clear();
            oldMembMolecules.clear();
        }
    }

    public void outASingleTermUpdate(ASingleTermUpdate node) {
        // if update rule add all not modified term as old
        // e.g.  update(@a x1, x2 => y2) == update(@a null => x1, x2 => y2)
        Term term = (Term) container.getStack(Term.class).pop();
        newRelationParams.add(term);
        if (modifier instanceof AUpdateModifier) {
            oldRelationParams.add(term);
        }
    }

    public void outAMoveTermUpdate(AMoveTermUpdate node) {
        newRelationParams.add((Term) container.getStack(Term.class).pop());
        oldRelationParams.add((Term) container.getStack(Term.class).pop());
    }

    // the new values of RelationFact
    private List<Term> newRelationParams = new LinkedList<Term>();

    // the new parameter values
    private List<Term> oldRelationParams = new LinkedList<Term>();

    // the new values of MembershipMolecule 
    private Set<MembershipMolecule> newMembMolecules = new LinkedHashSet<MembershipMolecule>();

    //the values of MembershipMolecule to be replaced "instance memberOf a => replacedValue"
    private Set<MembershipMolecule> oldMembMolecules = new LinkedHashSet<MembershipMolecule>();

    private void processMembershipMolecules(PFactUpdate node) {
        Term[] membAferMoveTo = new Term[0];
        if (node != null) {
            membAferMoveTo = (Term[]) container.getStack(Term[].class).pop();
        }
        Term[] membBeforeMoveTo = (Term[]) container.getStack(Term[].class).pop();
        Term instanceId = (Term) container.getStack(Term.class).peek();

        for (int i = 0; i < membBeforeMoveTo.length; i++) {
            if (membAferMoveTo.length == 0)
                newMembMolecules.add(leFactory.createMemberShipMolecule(instanceId,
                        membBeforeMoveTo[i]));
            else
                oldMembMolecules.add(leFactory.createMemberShipMolecule(instanceId,
                        membBeforeMoveTo[i]));
        }
        for (int i = 0; i < membAferMoveTo.length; i++) {
            newMembMolecules.add(leFactory
                    .createMemberShipMolecule(instanceId, membAferMoveTo[i]));
        }
    }

    // the new values of AttributeValueMolecule  
    private Set<AttributeValueMolecule> newAttrMolecules = new LinkedHashSet<AttributeValueMolecule>();

    // the values of AttributeValueMolecule to be replaced "instance[attr hasValue a => replacedValue]"
    private Set<AttributeValueMolecule> oldAttrMolecules = new LinkedHashSet<AttributeValueMolecule>();

    public void outAAttrFactList(AAttrFactList node) {
        processAttributeValueMolecule(node.getFactUpdate());
    }

    public void outAAttrRelationAttrFactList(AAttrRelationAttrFactList node) {
        processAttributeValueMolecule(node.getFactUpdate());
    }

    private void processAttributeValueMolecule(PFactUpdate node) {
        Stack terms = container.getStack(Term.class);
        Stack termList = container.getStack(Term[].class);

        Term[] valAfterMoveTo = new Term[0];
        if (node != null) {
            valAfterMoveTo = (Term[]) termList.pop();
        }
        Term attId = (Term) terms.pop();
        Term instanceId = (Term) terms.peek();
        Term[] valBeforeMoveTo = (Term[]) termList.pop();
        for (int i = 0; i < valBeforeMoveTo.length; i++) {
            if (valAfterMoveTo.length == 0)
                newAttrMolecules.add(leFactory.createAttributeValue(instanceId, attId,
                        valBeforeMoveTo[i]));
            else
                oldAttrMolecules.add(leFactory.createAttributeValue(instanceId, attId,
                        valBeforeMoveTo[i]));
        }
        for (int i = 0; i < valAfterMoveTo.length; i++) {
            newAttrMolecules.add(leFactory.createAttributeValue(instanceId, attId,
                    valAfterMoveTo[i]));
        }
    }
    
    // process ppMediator
    public void inAPpmediator(APpmediator node) {
        TopEntityAnalysis.isValidTopEntityIdentifier(node.getId(),node.getTPpmediator());
        node.getId().apply(container.getNodeHandler(PId.class));
        IRI iri = (IRI) container.popFromStack(Identifier.class, IRI.class);
        Mediator mediator = oFactory.updateRules.createPpMediator(iri);

        container.getStack(Entity.class).push(mediator);
        container.getStack(TopEntity.class).push(mediator);

        TopEntityAnalysis.addNamespaceAndVariant(mediator, container.getStack(Namespace.class),
                container.getStack(AWsmlvariant.class));
    }
    
    public void outAPpmediator(APpmediator node) {
        container.popFromStack(Entity.class, PpMediator.class);
    }
}

/*
 * $Log: RuleAnalyzer.java,v $
 * Revision 1.9  2006/11/17 13:11:14  vassil_momtchev
 * parsing of send and receieve types added
 *
 * Revision 1.8  2006/11/17 10:09:53  vassil_momtchev
 * fixed bug in orchestration parsing. the rules container was not set properly;
 *
 * Revision 1.7  2006/11/08 15:26:53  vassil_momtchev
 * support of ppMediator and orchestration perform rules added
 *
 * Revision 1.6  2006/10/24 14:11:47  vassil_momtchev
 * choreography/orchestration rules refactored. different types where appropriate now supported
 *
 * Revision 1.5  2006/04/17 09:50:09  vassil_momtchev
 * raise parser exception if non restricted LE used in condition
 *
 * Revision 1.4  2006/04/17 08:07:20  vassil_momtchev
 * bug fixed in update of RelationFact expression;
 *
 * Revision 1.3  2006/02/10 15:35:34  vassil_momtchev
 * new grammar implemented
 *
 * Revision 1.2  2006/01/31 10:33:14  vassil_momtchev
 * all facts now are parsed according the API. BogusFact type removed
 *
 * Revision 1.1  2005/12/21 11:35:56  vassil_momtchev
 * analyzer of rules created
 *
 */
