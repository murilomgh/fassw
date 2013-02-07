/*
 wsmo4j extension - a Choreography API and Reference Implementation

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

package org.deri.wsmo4j.choreography;

import java.util.*;

import org.deri.wsmo4j.choreography.rule.*;
import org.deri.wsmo4j.choreography.signature.*;
import org.deri.wsmo4j.rule.*;
import org.omwg.logicalexpression.*;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.factory.*;
import org.wsmo.service.choreography.*;
import org.wsmo.service.choreography.rule.*;
import org.wsmo.service.rule.*;
import org.wsmo.service.signature.*;

/**
 * Reference implementation for the Choreography Factory.
 * 
 * <pre>
 *       Created on Jul 26, 2005
 *       Committed by $Author: vassil_momtchev $
 *       $Source: /cvsroot/wsmo4j/ext/choreography/src/ri/org/deri/wsmo4j/choreography/ChoreographyFactoryRI.java,v $
 * </pre>
 * 
 * @author Thomas Haselwanter
 * @author James Scicluna
 * 
 * @version $Revision: 1.24 $ $Date: 2006/10/24 14:11:47 $
 */
public class ChoreographyFactoryRI extends ChoreographyFactory {

    // TODO loading with different classloaders like the contextclassloader
    Map params;

    /**
     * Choreography Factory default constructor
     * 
     */
    public ChoreographyFactoryRI() {
        this(null);
    }

    /**
     * Choreography Factory constructor accepting a set of parameters for
     * initialization purposes.
     * 
     * @param params
     */
    public ChoreographyFactoryRI(Map params) {
        super();
        this.params = params;
    }

    @Override
    protected Containers createContainers() {
        return new ContainersRI();
    }

    @Override
    protected TransitionRules createTransitionRules() {
        return new TransitionRulesRI();
    }

    @Override
    protected UpdateRules createUpdateRules() {
        return new UpdateRulesRI();
    }

    @Override
    protected Modes createModes() {
        return new ModesRI();
    }

    @Override
    protected Facts createFacts() {
        return new FactsRI();
    }

    private class ContainersRI implements Containers {

        public Choreography createChoreography(Identifier id) {
            return new ChoreographyRI(id);
        }

        public Choreography createChoreography(Identifier id, StateSignature signature, ChoreographyRules rules) {
            return new ChoreographyRI(id, signature, rules);
        }

        public StateSignature createStateSignature(Identifier id) {
            return new StateSignatureRI(id);
        }

        public StateSignature createStateSignature(Identifier id, Set<Mode> modes) {
            return new StateSignatureRI(id, modes);
        }

        public StateSignature createStateSignature(Identifier id, Set<In> inMode, Set<Out> outMode,
                Set<Shared> sharedMode, Set<Static> staticMode, Set<Controlled> controlledMode) {
            return new StateSignatureRI(id, inMode, outMode, sharedMode, staticMode,
                    controlledMode);
        }

        public ChoreographyRules createRules(Identifier id) {
            return new RulesRI(id);
        }

        public ChoreographyRules createRules(Identifier id, Set<ChoreographyRule> rules) {
            return new RulesRI(id, rules);
        }

    }

    private class TransitionRulesRI implements TransitionRules {

        public Condition createConditionFromLogicalExpression(LogicalExpression e) 
            throws InvalidModelException {
            return new ConditionRI(e);
        }

        public ChoreographyIfThen createIfThen(Condition expression, ChoreographyRule rule) {
            return new IfThenRI(expression, rule);
        }

        public ChoreographyIfThen createIfThen(Condition expression, Set<ChoreographyRule> rules) {
            return new IfThenRI(expression, rules);
        }

        public ChoreographyForAll createForAll(Variable variable, Condition condition, ChoreographyRule rule) {
            return new ForAllRI(variable, condition, rule);
        }

        public ChoreographyForAll createForAll(Set<Variable> variables, Condition condition, ChoreographyRule rule) {
            return new ForAllRI(variables, condition, rule);
        }

        public ChoreographyForAll createForAll(Variable variable, Condition condition, Set<ChoreographyRule> rules) {
            return new ForAllRI(variable, condition, rules);
        }

        public ChoreographyForAll createForAll(Set<Variable> variables, Condition condition, Set<ChoreographyRule> rules) {
            return new ForAllRI(variables, condition, rules);
        }

        public ChoreographyChoose createChoose(Variable variable, Condition condition, ChoreographyRule rule) {
            return new ChooseRI(variable, condition, rule);
        }

        public ChoreographyChoose createChoose(Set<Variable> variables, Condition condition, ChoreographyRule rule) {
            return new ChooseRI(variables, condition, rule);
        }

        public ChoreographyChoose createChoose(Variable variable, Condition condition, Set<ChoreographyRule> rules) {
            return new ChooseRI(variable, condition, rules);
        }

        public ChoreographyChoose createChoose(Set<Variable> variables, Condition condition, Set<ChoreographyRule> rules) {
            return new ChooseRI(variables, condition, rules);
        }

    }

    private class UpdateRulesRI implements UpdateRules {

        public Add createAdd(CompoundFact fact) {
            return new AddRI(fact);
        }

        public Delete createDelete(CompoundFact fact) {
            return new DeleteRI(fact);
        }

        public Update createUpdate(CompoundFact newFact) {
            return new UpdateRI(newFact);
        }
        
        public Update createUpdate(CompoundFact newFact, CompoundFact oldFact) throws InvalidModelException {
            return new UpdateRI(newFact, oldFact);
        }
    }

    private class ModesRI implements Modes {

        public In createIn(Concept concept) {
            return new InRI(concept);
        }

        public In createIn(Concept concept, Set<Grounding> grounding) {
            return new InRI(concept, grounding);
        }

        public Out createOut(Concept concept) {
            return new OutRI(concept);
        }

        public Out createOut(Concept concept, Set<Grounding> grounding) {
            return new OutRI(concept, grounding);
        }

        public Shared createShared(Concept concept) {
            return new SharedRI(concept);
        }

        public Shared createShared(Concept concept, Set<Grounding> grounding) {
            return new SharedRI(concept, grounding);
        }

        public Controlled createControlled(Concept concept) {
            return new ControlledRI(concept);
        }

        public Static createStatic(Concept concept) {
            return new StaticRI(concept);
        }

        public WSDLGrounding createWSDLGrounding(IRI iri) {
            return new WSDLGroundingRI(iri);
        }

        public In createIn(Relation relation) {
            // TODO Auto-generated method stub
            return new InRI(relation);
        }

        public In createIn(Relation relation, Set<Grounding> grounding) {
            // TODO Auto-generated method stub
            return new InRI(relation,grounding);
        }

        public Out createOut(Relation relation) {
            // TODO Auto-generated method stub
            return new OutRI(relation);
        }

        public Out createOut(Relation relation, Set<Grounding> grounding) {
            // TODO Auto-generated method stub
            return new OutRI(relation,grounding);
        }

        public Shared createShared(Relation relation) {
            // TODO Auto-generated method stub
            return new SharedRI(relation);
        }

        public Shared createShared(Relation relation, Set<Grounding> grounding) {
            // TODO Auto-generated method stub
            return new SharedRI(relation,grounding);
        }

        public Controlled createControlled(Relation relation) {
            // TODO Auto-generated method stub
            return new ControlledRI(relation);
        }

        public Static createStatic(Relation relation) {
            // TODO Auto-generated method stub
            return new StaticRI(relation);
        }

    }

    private class FactsRI implements Facts {

        public MoleculeFact createMoleculeFact() {
            return new MoleculeFactRI();
        }

        public MoleculeFact createMoleculeFact(Set<MembershipMolecule> memberMolecules,
                Set<AttributeValueMolecule> attrValues) {
            return new MoleculeFactRI(memberMolecules, attrValues);
        }

        public MoleculeFact createMoleculeFact(MembershipMolecule memberMolecule, Set<AttributeValueMolecule> attrValues) {
            return new MoleculeFactRI(memberMolecule,attrValues);
        }

        public MoleculeFact createMoleculeFact(Set<MembershipMolecule> memberMolecules, AttributeValueMolecule attrValue) {
            return new MoleculeFactRI(memberMolecules,attrValue);
        }

        public MoleculeFact createMoleculeFact(MembershipMolecule memberMolecule, AttributeValueMolecule attrValue) {
            return new MoleculeFactRI(memberMolecule,attrValue);
        }
        
        public RelationFact createRelationFact() {
            return new RelationFactRI();
        }

        public RelationFact createRelationFact(Atom atom) {
            return new RelationFactRI(atom);
        }
    }
}

/*
 * $Log: ChoreographyFactoryRI.java,v $
 * Revision 1.24  2006/10/24 14:11:47  vassil_momtchev
 * choreography/orchestration rules refactored. different types where appropriate now supported
 *
 * Revision 1.23  2006/04/17 09:46:32  vassil_momtchev
 * createChoreography(Identifier id) method added;
 *
 * Revision 1.22  2006/04/17 07:47:36  vassil_momtchev
 * method signature public RelationFact createRelationFact(Identifier, List) changed to RelationFact createRelationFact(Atom); new method RelationFact createRelationFact() added
 *
 * Revision 1.21  2006/02/10 09:59:02  jamsci001
 * - Added convenience methods for MoleculeFact
 *
 * Revision 1.20  2006/02/03 13:30:22  jamsci001
 * - Modified ChoreographyRI to be created with an Identifier
 *
 * Revision 1.19  2006/01/31 10:31:03  vassil_momtchev
 * MembershipUpdate type removed. Update interface use old and new CompoundFact; appropriate methods added; log footer added
 *
*/

