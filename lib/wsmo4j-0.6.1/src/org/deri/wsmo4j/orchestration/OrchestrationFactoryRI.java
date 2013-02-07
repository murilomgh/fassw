/*
 wsmo4j extension - an Orchestration API and Reference Implementation

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

package org.deri.wsmo4j.orchestration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.deri.wsmo4j.choreography.signature.ControlledRI;
import org.deri.wsmo4j.choreography.signature.InRI;
import org.deri.wsmo4j.choreography.signature.OutRI;
import org.deri.wsmo4j.choreography.signature.SharedRI;
import org.deri.wsmo4j.choreography.signature.StateSignatureRI;
import org.deri.wsmo4j.choreography.signature.StaticRI;
import org.deri.wsmo4j.choreography.signature.WSDLGroundingRI;
import org.deri.wsmo4j.orchestration.rule.ChooseRI;
import org.deri.wsmo4j.orchestration.rule.ForAllRI;
import org.deri.wsmo4j.orchestration.rule.IfThenRI;
import org.deri.wsmo4j.orchestration.rule.OrchestrationAchieveGoalRI;
import org.deri.wsmo4j.orchestration.rule.OrchestrationApplyMediationRI;
import org.deri.wsmo4j.orchestration.rule.OrchestrationInvokeServiceRI;
import org.deri.wsmo4j.orchestration.rule.ReceiveRI;
import org.deri.wsmo4j.orchestration.rule.RulesRI;
import org.deri.wsmo4j.orchestration.rule.SendRI;
import org.deri.wsmo4j.rule.AddRI;
import org.deri.wsmo4j.rule.ConditionRI;
import org.deri.wsmo4j.rule.DeleteRI;
import org.deri.wsmo4j.rule.MoleculeFactRI;
import org.deri.wsmo4j.rule.RelationFactRI;
import org.deri.wsmo4j.rule.UpdateRI;
import org.omwg.logicalexpression.Atom;
import org.omwg.logicalexpression.AttributeValueMolecule;
import org.omwg.logicalexpression.LogicalExpression;
import org.omwg.logicalexpression.MembershipMolecule;
import org.omwg.ontology.Concept;
import org.omwg.ontology.Relation;
import org.omwg.ontology.Variable;
import org.wsmo.common.Entity;
import org.wsmo.common.IRI;
import org.wsmo.common.Identifier;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.factory.OrchestrationFactory;
import org.wsmo.service.Goal;
import org.wsmo.service.WebService;
import org.wsmo.service.orchestration.Orchestration;
import org.wsmo.service.orchestration.PpMediator;
import org.wsmo.service.orchestration.rule.OrchestrationAchieveGoal;
import org.wsmo.service.orchestration.rule.OrchestrationApplyMediation;
import org.wsmo.service.orchestration.rule.OrchestrationChoose;
import org.wsmo.service.orchestration.rule.OrchestrationForAll;
import org.wsmo.service.orchestration.rule.OrchestrationIfThen;
import org.wsmo.service.orchestration.rule.OrchestrationInvokeService;
import org.wsmo.service.orchestration.rule.OrchestrationRules;
import org.wsmo.service.orchestration.rule.Receive;
import org.wsmo.service.orchestration.rule.Send;
import org.wsmo.service.rule.Add;
import org.wsmo.service.rule.CompoundFact;
import org.wsmo.service.rule.Condition;
import org.wsmo.service.rule.Delete;
import org.wsmo.service.rule.MoleculeFact;
import org.wsmo.service.rule.RelationFact;
import org.wsmo.service.rule.Rule;
import org.wsmo.service.rule.Update;
import org.wsmo.service.signature.Controlled;
import org.wsmo.service.signature.Grounding;
import org.wsmo.service.signature.In;
import org.wsmo.service.signature.Out;
import org.wsmo.service.signature.Shared;
import org.wsmo.service.signature.StateSignature;
import org.wsmo.service.signature.Static;
import org.wsmo.service.signature.WSDLGrounding;

/**
 * Interface or class description
 * 
 * @author James Scicluna
 * 
 * Created on 30-May-2006 Committed by $Author: morcen $
 * 
 * $Source: /cvsroot/wsmo4j/ext/orchestration/src/ri/org/deri/wsmo4j/orchestration/OrchestrationFactoryRI.java,v $,
 * @version $Revision: 1.5 $ $Date: 2007/04/02 13:05:18 $
 */

public class OrchestrationFactoryRI extends OrchestrationFactory
{

  /**
   * Default Constructor for the Orchestration Factory.
   */
  public  OrchestrationFactoryRI() {
    super();
    synchronized(OrchestrationFactoryRI.class) {
    if (registry == null)
        registry = new HashMap<Identifier, Entity>();
    }
  }
  
  private static Map<Identifier, Entity> registry;

  /*
   * (non-Javadoc)
   * 
   * @see org.wsmo.factory.orchestration.OrchestrationFactory#createContainers()
   */
  @Override
  protected Containers createContainers()
  {
    return new ContainersRI();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.wsmo.factory.orchestration.OrchestrationFactory#createTransitionRules()
   */
  @Override
  protected TransitionRules createTransitionRules()
  {
    return new TransitionRulesRI();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.wsmo.factory.orchestration.OrchestrationFactory#createUpdateRules()
   */
  @Override
  protected UpdateRules createUpdateRules()
  {
    return new UpdateRulesRI();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.wsmo.factory.orchestration.OrchestrationFactory#createFacts()
   */
  @Override
  protected Facts createFacts()
  {
    return new FactsRI();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.wsmo.factory.orchestration.OrchestrationFactory#createModes()
   */
  @Override
  protected Modes createModes()
  {
    return new ModesRI();
  }

  private class ContainersRI implements Containers
  {

    public Orchestration createOrchestration(Identifier id)
    {
      return new OrchestrationRI(id);
    }

    public Orchestration createOrchestration(Identifier id,
        StateSignature signature, OrchestrationRules rules)
    {
      return new OrchestrationRI(id, signature, rules);
    }

    public StateSignature createStateSignature(Identifier id)
    {
      return new StateSignatureRI(id);
    }

    public StateSignature createStateSignature(Identifier id, Set<In> inMode,
        Set<Out> outMode, Set<Shared> sharedMode, Set<Static> staticMode,
        Set<Controlled> controlledMode)
    {
      return new StateSignatureRI(id, inMode, outMode, sharedMode, staticMode,
          controlledMode);
    }

    public OrchestrationRules createRules(Identifier id)
    {
      return new RulesRI(id);
    }

    public OrchestrationRules createRules(Identifier id, Set<Rule> rules)
    {
      return new RulesRI(id, rules);
    }

  }

  private class TransitionRulesRI implements TransitionRules
  {

    public Condition createConditionFromLogicalExpression(LogicalExpression e)
        throws InvalidModelException
    {
      return new ConditionRI(e);
    }

    public OrchestrationIfThen createIfThen(Condition expression, Rule rule)
    {
      return new IfThenRI(expression, rule);
    }

    public OrchestrationIfThen createIfThen(Condition expression, Set<Rule> rules)
    {
      return new IfThenRI(expression, rules);
    }

    public OrchestrationForAll createForAll(Variable variable, Condition condition, Rule rule)
    {
      return new ForAllRI(variable, condition, rule);
    }

    public OrchestrationForAll createForAll(Set<Variable> variables, Condition condition,
        Rule rule)
    {
      return new ForAllRI(variables, condition, rule);
    }

    public OrchestrationForAll createForAll(Variable variable, Condition condition,
        Set<Rule> rules)
    {
      return new ForAllRI(variable, condition, rules);
    }

    public OrchestrationForAll createForAll(Set<Variable> variables, Condition condition,
        Set<Rule> rules)
    {
      return new ForAllRI(variables, condition, rules);
    }

    public OrchestrationChoose createChoose(Variable variable, Condition condition, Rule rule)
    {
      return new ChooseRI(variable, condition, rule);
    }

    public OrchestrationChoose createChoose(Set<Variable> variables, Condition condition,
        Rule rule)
    {
      return new ChooseRI(variables, condition, rule);
    }

    public OrchestrationChoose createChoose(Variable variable, Condition condition,
        Set<Rule> rules)
    {
      return new ChooseRI(variable, condition, rules);
    }

    public OrchestrationChoose createChoose(Set<Variable> variables, Condition condition,
        Set<Rule> rules)
    {
      return new ChooseRI(variables, condition, rules);
    }

  }

  private class UpdateRulesRI implements UpdateRules
  {

    public Add createAdd(CompoundFact fact)
    {
      return new AddRI(fact);
    }

    public Delete createDelete(CompoundFact fact)
    {
      return new DeleteRI(fact);
    }

    public Update createUpdate(CompoundFact newFact)
    {
      return new UpdateRI(newFact);
    }

    public Update createUpdate(CompoundFact newFact, CompoundFact oldFact)
        throws InvalidModelException
    {
      return new UpdateRI(newFact, oldFact);
    }

	public OrchestrationAchieveGoal createOrchestrationAchieveGoal(IRI perform, Goal goal) {
		return new OrchestrationAchieveGoalRI(perform, goal);
	}

	public OrchestrationApplyMediation createOrchestrationApplyMediation(IRI perform, PpMediator mediator) {
		return new OrchestrationApplyMediationRI(perform, mediator);
	}

	public OrchestrationInvokeService createOrchestrationInvokeService(IRI perform, WebService service) {
		return new OrchestrationInvokeServiceRI(perform, service);
	}

    public synchronized PpMediator createPpMediator(Identifier id) {
        Object o = registry.get(id);
        if (o instanceof PpMediator) {
            return (PpMediator) o;
        }
        PpMediator mediator = new PpMediatorRi(id);
        registry.put(id, mediator);
        return mediator;
    }

	public Receive createReceive(IRI performance, IRI source) {
		return new ReceiveRI(performance, source);
	}

	public Send createSend(IRI performance, IRI target) {
		return new SendRI(performance, target);
	}


  }

  private class ModesRI implements Modes
  {

    public In createIn(Concept concept)
    {
      return new InRI(concept);
    }

    public In createIn(Concept concept, Set<Grounding> grounding)
    {
      return new InRI(concept, grounding);
    }

    public In createIn(Relation relation)
    {
      return new InRI(relation);
    }

    public In createIn(Relation relation, Set<Grounding> grounding)
    {
      return new InRI(relation, grounding);
    }

    public Out createOut(Concept concept)
    {
      return new OutRI(concept);
    }

    public Out createOut(Concept concept, Set<Grounding> grounding)
    {
      return new OutRI(concept, grounding);
    }

    public Out createOut(Relation relation)
    {
      return new OutRI(relation);
    }

    public Out createOut(Relation relation, Set<Grounding> grounding)
    {
      return new OutRI(relation, grounding);
    }

    public Shared createShared(Concept concept)
    {
      return new SharedRI(concept);
    }

    public Shared createShared(Concept concept, Set<Grounding> grounding)
    {
      return new SharedRI(concept, grounding);
    }

    public Shared createShared(Relation relation)
    {
      return new SharedRI(relation);
    }

    public Shared createShared(Relation relation, Set<Grounding> grounding)
    {
      return new SharedRI(relation, grounding);
    }

    public Controlled createControlled(Concept concept)
    {
      return new ControlledRI(concept);
    }

    public Controlled createControlled(Relation relation)
    {
      return new ControlledRI(relation);
    }

    public Static createStatic(Concept concept)
    {
      return new StaticRI(concept);
    }

    public Static createStatic(Relation relation)
    {
      return new StaticRI(relation);
    }

    public WSDLGrounding createWSDLGrounding(IRI iri)
    {
      return new WSDLGroundingRI(iri);
    }

  }

  private class FactsRI implements Facts
  {

    public MoleculeFact createMoleculeFact()
    {
      return new MoleculeFactRI();
    }

    public MoleculeFact createMoleculeFact(
        Set<MembershipMolecule> memberMolecules,
        Set<AttributeValueMolecule> attrValues)
    {
      return new MoleculeFactRI(memberMolecules, attrValues);
    }

    public MoleculeFact createMoleculeFact(MembershipMolecule memberMolecule,
        Set<AttributeValueMolecule> attrValues)
    {
      return new MoleculeFactRI(memberMolecule, attrValues);
    }

    public MoleculeFact createMoleculeFact(
        Set<MembershipMolecule> memberMolecules,
        AttributeValueMolecule attrValue)
    {
      return new MoleculeFactRI(memberMolecules, attrValue);
    }

    public MoleculeFact createMoleculeFact(MembershipMolecule memberMolecule,
        AttributeValueMolecule attrValue)
    {
      return new MoleculeFactRI(memberMolecule, attrValue);
    }

    public RelationFact createRelationFact()
    {
      return new RelationFactRI();
    }

    public RelationFact createRelationFact(Atom atom)
    {
      return new RelationFactRI(atom);
    }

  }

}
