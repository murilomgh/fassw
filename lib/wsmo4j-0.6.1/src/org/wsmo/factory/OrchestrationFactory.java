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

package org.wsmo.factory;

import java.util.*;

import org.omwg.logicalexpression.*;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.service.*;
import org.wsmo.service.orchestration.*;
import org.wsmo.service.orchestration.Orchestration;
import org.wsmo.service.orchestration.rule.*;
import org.wsmo.service.rule.*;
import org.wsmo.service.signature.*;

/**
 * Orchestration Factory abstract Class.
 * 
 * @author James Scicluna
 * 
 * Created on 30-May-2006 Committed by $Author: vassil_momtchev $
 * 
 * $Source: /cvsroot/wsmo4j/ext/orchestration/src/api/org/wsmo/factory/OrchestrationFactory.java,v $,
 * @version $Revision: 1.4 $ $Date: 2006/11/08 15:26:53 $
 */

public abstract class OrchestrationFactory
{
  public interface Containers
  {
    public Orchestration createOrchestration(Identifier id);

    public Orchestration createOrchestration(Identifier id,
        StateSignature signature, OrchestrationRules rules);

    public StateSignature createStateSignature(Identifier id);

    public StateSignature createStateSignature(Identifier id, Set<In> inMode,
        Set<Out> outMode, Set<Shared> sharedMode, Set<Static> staticMode,
        Set<Controlled> controlledMode);

    public OrchestrationRules createRules(Identifier id);

    public OrchestrationRules createRules(Identifier id, Set<Rule> rules);
  }

  public interface TransitionRules
  {

    public Condition createConditionFromLogicalExpression(LogicalExpression e)
        throws InvalidModelException;

    public OrchestrationIfThen createIfThen(Condition expression, Rule rule);

    public OrchestrationIfThen createIfThen(Condition expression, Set<Rule> rules);

    public OrchestrationForAll createForAll(Variable variable, Condition condition, Rule rule);

    public OrchestrationForAll createForAll(Set<Variable> variables, Condition condition,
            Rule rule);

    public OrchestrationForAll createForAll(Variable variable, Condition condition,
        Set<Rule> rules);

    public OrchestrationForAll createForAll(Set<Variable> variables, Condition condition,
        Set<Rule> rules);

    public OrchestrationChoose createChoose(Variable variable, Condition condition, Rule rule);

    public OrchestrationChoose createChoose(Set<Variable> variables, Condition condition,
        Rule rule);

    public OrchestrationChoose createChoose(Variable variable, Condition condition,
        Set<Rule> rules);

    public OrchestrationChoose createChoose(Set<Variable> variables, Condition condition,
        Set<Rule> rules);
  }

  public interface UpdateRules
  {

    public Add createAdd(CompoundFact fact);

    public Delete createDelete(CompoundFact fact);

    public Update createUpdate(CompoundFact newFact);

    public Update createUpdate(CompoundFact newFact, CompoundFact oldFact)
        throws InvalidModelException;
    
    public Receive createReceive(IRI perform, IRI source);
    
    public Send createSend(IRI perform, IRI target);
    
    public OrchestrationApplyMediation createOrchestrationApplyMediation(IRI perform, PpMediator mediator);
    
    public OrchestrationInvokeService createOrchestrationInvokeService(IRI perform, WebService service);
    
    public OrchestrationAchieveGoal createOrchestrationAchieveGoal(IRI perform, Goal goal);
    
    public PpMediator createPpMediator(Identifier id);
  }
  

  public interface Facts
  {
    public MoleculeFact createMoleculeFact();

    public MoleculeFact createMoleculeFact(
        Set<MembershipMolecule> memberMolecules,
        Set<AttributeValueMolecule> attrValues);

    public MoleculeFact createMoleculeFact(MembershipMolecule memberMolecule,
        Set<AttributeValueMolecule> attrValues);

    public MoleculeFact createMoleculeFact(
        Set<MembershipMolecule> memberMolecules,
        AttributeValueMolecule attrValue);

    public MoleculeFact createMoleculeFact(MembershipMolecule memberMolecule,
        AttributeValueMolecule attrValue);

    public RelationFact createRelationFact();

    public RelationFact createRelationFact(Atom atom);
  }

  public interface Modes
  {
    public In createIn(Concept concept);

    public In createIn(Concept concept, Set<Grounding> grounding);

    public In createIn(Relation relation);

    public In createIn(Relation relation, Set<Grounding> grounding);

    public Out createOut(Concept concept);

    public Out createOut(Concept concept, Set<Grounding> grounding);

    public Out createOut(Relation relation);

    public Out createOut(Relation relation, Set<Grounding> grounding);

    public Shared createShared(Concept concept);

    public Shared createShared(Concept concept, Set<Grounding> grounding);

    public Shared createShared(Relation relation);

    public Shared createShared(Relation relation, Set<Grounding> grounding);

    public Controlled createControlled(Concept concept);

    public Controlled createControlled(Relation relation);

    public Static createStatic(Concept concept);

    public Static createStatic(Relation relation);

    public WSDLGrounding createWSDLGrounding(IRI iri);
  }

  public final Containers containers = createContainers();

  public final TransitionRules transitionRules = createTransitionRules();

  public final UpdateRules updateRules = createUpdateRules();

  public final Facts facts = createFacts();

  public final Modes modes = createModes();

  protected abstract Containers createContainers();

  protected abstract TransitionRules createTransitionRules();

  protected abstract UpdateRules createUpdateRules();

  protected abstract Facts createFacts();

  protected abstract Modes createModes();

}
