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

package org.wsmo.factory;

import java.util.*;

import org.omwg.logicalexpression.*;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.service.choreography.*;
import org.wsmo.service.choreography.rule.*;
import org.wsmo.service.rule.*;
import org.wsmo.service.signature.*;

/**
 * Factory for creating <code>Choreographies</code>. Nested inner interfaces
 * are used in combination with public fields to create a pseudo-namespace
 * mechanism, which groups the objects that are created by this factory in a
 * type safe way using the reference type of the inner interfaces. It is also
 * safe for the fields to be public since they're final. Inner interfaces create
 * a new reference type like a any other top-level interface, which let
 * code-completion IDEs work with the exposed fields. It also allows the client
 * programmer to either keep a reference to the main factory around or
 * references to the individual subfactories, which is usefull if subsystems
 * exist in the client programmers code that need/should create only a certain
 * type of objects.
 * 
 * Note that it is ok for the inner interface names to be in plural, unlike most
 * singularily named top-level interfaces including
 * <code>ChoreographyFactory</code>.
 * 
 * <pre>
 *         Created on Jul 26, 2005
 *         Committed by $Author: vassil_momtchev $
 *         $Source: /cvsroot/wsmo4j/ext/choreography/src/api/org/wsmo/factory/ChoreographyFactory.java,v $
 * </pre>
 * 
 * @author James Scicluna
 * @author Thomas Haselwanter
 * @author Holger Lausen
 * 
 * @version $Revision: 1.27 $ $Date: 2006/10/24 14:11:46 $
 */
public abstract class ChoreographyFactory {

    public interface Containers {
        public Choreography createChoreography(Identifier id);

        public Choreography createChoreography(Identifier id, StateSignature signature, ChoreographyRules rules);

        public StateSignature createStateSignature(Identifier id);

        public StateSignature createStateSignature(Identifier id, Set<In> inMode, Set<Out> outMode,
                Set<Shared> sharedMode, Set<Static> staticMode, Set<Controlled> controlledMode);

        public ChoreographyRules createRules(Identifier id);

        public ChoreographyRules createRules(Identifier id, Set<ChoreographyRule> rules);
    }

    public interface TransitionRules {

        public Condition createConditionFromLogicalExpression(LogicalExpression e) throws InvalidModelException;

        public ChoreographyIfThen createIfThen(Condition expression, ChoreographyRule rule);

        public ChoreographyIfThen createIfThen(Condition expression, Set<ChoreographyRule> rules);

        public ChoreographyForAll createForAll(Variable variable, Condition condition, ChoreographyRule rule);

        public ChoreographyForAll createForAll(Set<Variable> variables, Condition condition, ChoreographyRule rule);

        public ChoreographyForAll createForAll(Variable variable, Condition condition, Set<ChoreographyRule> rules);

        public ChoreographyForAll createForAll(Set<Variable> variables, Condition condition, Set<ChoreographyRule> rules);

        public ChoreographyChoose createChoose(Variable variable, Condition condition, ChoreographyRule rule);

        public ChoreographyChoose createChoose(Set<Variable> variables, Condition condition, ChoreographyRule rule);

        public ChoreographyChoose createChoose(Variable variable, Condition condition, Set<ChoreographyRule> rules);

        public ChoreographyChoose createChoose(Set<Variable> variables, Condition condition, Set<ChoreographyRule> rules);
    }

    public interface UpdateRules {

        public Add createAdd(CompoundFact fact);

        public Delete createDelete(CompoundFact fact);

        public Update createUpdate(CompoundFact newFact);

        public Update createUpdate(CompoundFact newFact, CompoundFact oldFact)
                throws InvalidModelException;
    }

    public interface Facts {
        public MoleculeFact createMoleculeFact();

        public MoleculeFact createMoleculeFact(Set<MembershipMolecule> memberMolecules,
                Set<AttributeValueMolecule> attrValues);
        
        public MoleculeFact createMoleculeFact(MembershipMolecule memberMolecule,
                Set<AttributeValueMolecule> attrValues);

        public MoleculeFact createMoleculeFact(Set<MembershipMolecule> memberMolecules,
                AttributeValueMolecule attrValue);

        public MoleculeFact createMoleculeFact(MembershipMolecule memberMolecule, AttributeValueMolecule attrValue);
        
        public RelationFact createRelationFact();
        
        public RelationFact createRelationFact(Atom atom);
    }

    public interface Modes {
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

/*
 * $Log: ChoreographyFactory.java,v $
 * Revision 1.27  2006/10/24 14:11:46  vassil_momtchev
 * choreography/orchestration rules refactored. different types where appropriate now supported
 *
 * Revision 1.26  2006/05/30 16:49:25  jamsci001
 * - removed redundant comment
 *
 * Revision 1.25  2006/04/17 09:44:06  vassil_momtchev
 * method public Choreography createChoreography(Identifier id) added; public Condition createConditionFromLogicalExpression(LogicalExpression e) changed to public Condition createConditionFromLogicalExpression(LogicalExpression e) throws InvalidModelException;
 *
 * Revision 1.24  2006/04/17 07:37:43  vassil_momtchev
 * method signature createRelationFact(Identifier, List)  -> createRelationFact(Atom); method createRelationFact() added
 *
 * Revision 1.23  2006/02/10 09:58:45  jamsci001
 * - Added convenience methods for MoleculeFact
 *
 * Revision 1.22  2006/02/03 13:27:41  jamsci001
 * - support for relation updates
 * - consequently modified creator methods for facts and update rules
 * Revision 1.21 2006/01/31 10:36:54
 * vassil_momtchev MembershipUpdate and AttributeValueUpdate types removed. use
 * two CompoundFacts instead; log footer added
 * 
 */

