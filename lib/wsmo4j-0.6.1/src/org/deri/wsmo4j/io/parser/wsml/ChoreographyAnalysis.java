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

import org.deri.wsmo4j.choreography.signature.*;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.factory.*;
import org.wsmo.service.*;
import org.wsmo.service.choreography.Choreography;
import org.wsmo.service.choreography.rule.*;
import org.wsmo.service.orchestration.Orchestration;
import org.wsmo.service.orchestration.rule.*;
import org.wsmo.service.signature.*;
import org.wsmo.wsml.compiler.node.*;

import com.ontotext.wsmo4j.parser.wsml.*;

@SuppressWarnings("unchecked")
public class ChoreographyAnalysis extends ASTAnalysis {

    private ChoreographyFactory factory;

    private WsmoFactory wsmoFactory;

    private OrchestrationFactory orchFactory;

    private ASTAnalysisContainer container;

    private ASTAnalysis orignalHeaderAnalysis;

    public ChoreographyAnalysis(ASTAnalysisContainer container, ChoreographyFactory factory,
            OrchestrationFactory orchFactory, WsmoFactory wsmoFactory) {
        if (container == null || factory == null || wsmoFactory == null) {
            throw new IllegalArgumentException();
        }
        this.factory = factory;
        this.wsmoFactory = wsmoFactory;
        this.orchFactory = orchFactory;
        this.container = container;

        // register the handled nodes
        container.registerNodeHandler(AChorAsmChoreographyFormalism.class, this);
        container.registerNodeHandler(AOrchAsmOrchestrationFormalism.class, this);
        container.registerNodeHandler(AStateSignature.class, this);
        container.registerNodeHandler(AMode.class, this);
        container.registerNodeHandler(ADefaultModeModeEntry.class, this);
        container.registerNodeHandler(AConceptModeModeEntry.class, this);
        container.registerNodeHandler(ARelationModeModeEntry.class, this);
        container.registerNodeHandler(AIrilistGroundingInfo.class, this);
        container.registerNodeHandler(AIriGroundingInfo.class, this);

        // StateSignature may have use OOMediator or import Ontology 
        container.registerNodeHandler(AImportsontology.class, this);
        container.registerNodeHandler(AUsesmediator.class, this);
        // Get a handle to the original object to process AImportsontology and AUsesmediator
        orignalHeaderAnalysis = container.getNodeHandler(AWsmlvariant.class);
        assert (orignalHeaderAnalysis instanceof TopEntityAnalysis) : "TopEntityAnalysis implemention is required to be registered in the "
                + "container in order to use ChoreographyAnalysis!";
    }

    public void inAOrchAsmOrchestrationFormalism(AOrchAsmOrchestrationFormalism node) {
        org.wsmo.service.Orchestration old = (org.wsmo.service.Orchestration) container.popFromStack(
                org.wsmo.service.Orchestration.class, org.wsmo.service.Orchestration.class);
        Orchestration newOrchestration = orchFactory.containers.createOrchestration(old.getIdentifier());
        ServiceAnalysis.copyNFP(old, newOrchestration);
        Interface iface = (Interface) container.peekFromStack(TopEntity.class, Interface.class);
        iface.setOrchestration(newOrchestration);
        container.getStack(org.wsmo.service.Orchestration.class).push(newOrchestration);
    }

    public void outAOrchAsmOrchestrationFormalism(AOrchAsmOrchestrationFormalism node) {
        Orchestration orch = (Orchestration) container.peekFromStack(
                org.wsmo.service.Orchestration.class, Orchestration.class);
        if (!container.getStack(StateSignature.class).isEmpty()) {
            orch.setStateSignature((StateSignature) container.popFromStack(StateSignature.class,
                    StateSignature.class));
        }
        if (!container.getStack(RuleAnalyzer.class).isEmpty()) {
            orch.setRules((OrchestrationRules) container.popFromStack(RuleAnalyzer.class,
                    OrchestrationRules.class));
        }
    }

    public void inAChorAsmChoreographyFormalism(AChorAsmChoreographyFormalism node) {
        org.wsmo.service.Choreography old = (org.wsmo.service.Choreography) container.popFromStack(
                org.wsmo.service.Choreography.class, org.wsmo.service.Choreography.class);
        Choreography newChoreography = factory.containers.createChoreography(old.getIdentifier());
        ServiceAnalysis.copyNFP(old, newChoreography);
        Interface iface = (Interface) container.peekFromStack(TopEntity.class, Interface.class);
        iface.setChoreography(newChoreography);
        container.getStack(org.wsmo.service.Choreography.class).push(newChoreography);
    }

    public void outAChorAsmChoreographyFormalism(AChorAsmChoreographyFormalism node) {
        Choreography choreography = (Choreography) container.peekFromStack(
                org.wsmo.service.Choreography.class, Choreography.class);
        if (!container.getStack(StateSignature.class).isEmpty()) {
            choreography.setStateSignature((StateSignature) container.popFromStack(
                    StateSignature.class, StateSignature.class));
        }
        if (!container.getStack(RuleAnalyzer.class).isEmpty()) {
            choreography.setRules((ChoreographyRules) container.popFromStack(RuleAnalyzer.class,
                    ChoreographyRules.class));
        }
    }

    public void inAStateSignature(AStateSignature node) {
        Identifier id = null;
        if (node.getId() instanceof AAnonymousId || node.getId() == null) {
            id = wsmoFactory.createAnonymousID();
        }
        else {
            node.getId().apply(container.getNodeHandler(PId.class));
            id = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        }
        StateSignature stateSignature = factory.containers.createStateSignature(id);
        container.getStack(StateSignature.class).push(stateSignature);
        container.getStack(Entity.class).push(stateSignature);
    }

    public void outAStateSignature(AStateSignature node) {
        container.popFromStack(Entity.class, StateSignature.class);
    }

    // Mode section

    private PModeId modeType;

    public void inAMode(AMode node) {
        modeType = node.getModeId();
    }

    public void outADefaultModeModeEntry(ADefaultModeModeEntry node) {
        outModeEntry(node.getGrounding(), true);
    }

    public void outAConceptModeModeEntry(AConceptModeModeEntry node) {
        outModeEntry(node.getGrounding(), true);
    }

    public void outARelationModeModeEntry(ARelationModeModeEntry node) {
        outModeEntry(node.getGrounding(), false);
    }

    private void outModeEntry(PGrounding groundingNode, boolean isConcept) {
        Set<Grounding> groundings = new HashSet<Grounding>();
        if (groundingNode != null) {
            groundings.addAll(Arrays.asList((Grounding[]) container.popFromStack(Grounding[].class,
                    Grounding[].class)));
        }
        IRI iri = (IRI) container.popFromStack(Identifier.class, IRI.class);
        if (isConcept)
            createMode(wsmoFactory.createConcept(iri), groundings);
        else
            createMode(wsmoFactory.createRelation(iri), groundings);
    }

    private void createMode(Concept concept, Set<Grounding> groundings) {
        StateSignature signature = (StateSignature) container.peekFromStack(StateSignature.class,
                StateSignature.class);
        if (modeType instanceof AInModeId) {
            signature.addMode(factory.modes.createIn(concept, groundings));
        }
        else if (modeType instanceof AOutModeId) {
            signature.addMode(factory.modes.createOut(concept, groundings));
        }
        else if (modeType instanceof ASharedModeId) {
            signature.addMode(factory.modes.createShared(concept, groundings));
            ;
        }
        else if (modeType instanceof AStaticModeId) {
            signature.addMode(factory.modes.createStatic(concept));
        }
        else if (modeType instanceof AControlledModeId) {
            signature.addMode(factory.modes.createControlled(concept));
        }
    }

    private void createMode(Relation relation, Set<Grounding> groundings) {
        StateSignature signature = (StateSignature) container.peekFromStack(StateSignature.class,
                StateSignature.class);
        if (modeType instanceof AInModeId) {
            signature.addMode(factory.modes.createIn(relation, groundings));
        }
        else if (modeType instanceof AOutModeId) {
            signature.addMode(factory.modes.createOut(relation, groundings));
        }
        else if (modeType instanceof ASharedModeId) {
            signature.addMode(factory.modes.createShared(relation, groundings));
            ;
        }
        else if (modeType instanceof AStaticModeId) {
            signature.addMode(factory.modes.createStatic(relation));
        }
        else if (modeType instanceof AControlledModeId) {
            signature.addMode(factory.modes.createControlled(relation));
        }
    }

    // end Mode section

    // Grounding iri lists section

    private Object lastIdentifierItem = null;

    public void inAIrilistGroundingInfo(AIrilistGroundingInfo node) {
        if (container.getStack(Identifier.class).isEmpty())
            lastIdentifierItem = null;
        else
            lastIdentifierItem = container.peekFromStack(Identifier.class, Identifier.class);
    }

    public void outAIrilistGroundingInfo(AIrilistGroundingInfo node) {
        Stack identifierStack = container.getStack(Identifier.class);
        Set<Grounding> groundings = new HashSet<Grounding>();
        while (!identifierStack.isEmpty() && lastIdentifierItem != identifierStack.peek()) {
            groundings.add(new WSDLGroundingRI((IRI) identifierStack.pop()));
        }
        container.getStack(Grounding[].class).push(groundings.toArray(new Grounding[] {}));
    }

    public void outAIriGroundingInfo(AIriGroundingInfo node) {
        IRI iri = (IRI) container.popFromStack(Identifier.class, IRI.class);
        container.getStack(Grounding[].class).push(new Grounding[] { new WSDLGroundingRI(iri) });
    }

    // end Grounding iri lists section

    public void outAImportsontology(AImportsontology node) {
        if (!container.getStack(StateSignature.class).isEmpty()) {
            // case 1 header in StateSignature
            Identifier[] iris = (Identifier[]) container.popFromStack(Identifier[].class,
                    Identifier[].class);
            StateSignature state = (StateSignature) container.getStack(StateSignature.class).peek();
            for (int i = 0; i < iris.length; i++) {
                state.addOntology(wsmoFactory.getOntology((IRI) iris[i]));
            }
        }
        else { // header of a TopEntity
            orignalHeaderAnalysis.outAImportsontology(node);
        }
    }

    public void outAUsesmediator(AUsesmediator node) {
        if (!container.getStack(StateSignature.class).isEmpty()) {
            // case 1 header in StateSignature
            Identifier[] iris = (Identifier[]) container.popFromStack(Identifier[].class,
                    Identifier[].class);
            StateSignature state = (StateSignature) container.getStack(StateSignature.class).peek();
            for (int i = 0; i < iris.length; i++) {
                assert iris[i] instanceof IRI;
                state.addMediator(wsmoFactory.getOOMediator((IRI) iris[i]));
            }
        }
        else {
            // case 2 header of a topEntity
            orignalHeaderAnalysis.outAUsesmediator(node);
        }
    }
}

/*
 * $Log: ChoreographyAnalysis.java,v $
 * Revision 1.17  2006/12/04 11:17:53  vassil_momtchev
 * wsmo4j parser always processe the choreography/orchestration; the extended parser will swap the implementation with the appropriate one
 *
 * Revision 1.16  2006/11/22 12:54:23  vassil_momtchev
 * Choreography/Orchestration parsing is now correct; warning: StateSignature of Chor/Orch will be ignroed if no formalism like Cashew/ASM/AD is defined
 *
 * Revision 1.15  2006/11/17 10:09:53  vassil_momtchev
 * fixed bug in orchestration parsing. the rules container was not set properly;
 *
 * Revision 1.14  2006/10/24 14:11:47  vassil_momtchev
 * choreography/orchestration rules refactored. different types where appropriate now supported
 *
 * Revision 1.13  2006/06/07 13:03:42  vassil_momtchev
 * orchestration parsing added
 *
 * Revision 1.12  2006/04/17 10:53:54  vassil_momtchev
 * outAChoreography and inAChoreography methods use ChoreographyFactory.createChoreography(Identifier); no longer need of dummy Entity object to collect NFPs required
 *
 * Revision 1.11  2006/03/06 16:00:30  vassil_momtchev
 * transitionRules are no longer mandatory for a statesignature (rules container with anonymous id by default is used)
 *
 * Revision 1.10  2006/02/17 12:20:19  vassil_momtchev
 * helper dummy entity was not removed if choreography is only declared
 *
 * Revision 1.9  2006/02/10 15:35:34  vassil_momtchev
 * new grammar implemented
 *
 * Revision 1.8  2006/01/31 15:36:04  vassil_momtchev
 * choreography nfps were assigned to the last previous entity; dummy entity is used now to collect them
 *
 * Revision 1.7  2006/01/31 10:51:38  vassil_momtchev
 * unused fields removed; log footer added
 *
 * Revision 1.6  2006/01/10 14:05:21  vassil_momtchev
 * importOntology in StateSignature node is handled correct now sf:1365514
 *
 * Revision 1.5  2005/12/21 14:39:54  vassil_momtchev
 * choreography refered by id only are created as ChoreographyImpl
 *
 * Revision 1.4  2005/12/21 11:36:49  vassil_momtchev
 * analyse of rules shifted to RuleAnalyzer class
 *
 * Revision 1.3  2005/12/07 11:48:09  vassil_momtchev
 * modified after the latest api changes
 *
 * Revision 1.2  2005/11/29 14:09:11  vassil_momtchev
 * bug fixed in the grounding irilist parsing
 *
 * Revision 1.1  2005/11/28 16:00:41  vassil_momtchev
 * choreography parser added extending com.ontotext.wsmo4j.parser.wsml.ParserImpl (ParserImpl.java)
 * AST analysis of choreography (ChoreographyAnalysis.java)
 *
 */
