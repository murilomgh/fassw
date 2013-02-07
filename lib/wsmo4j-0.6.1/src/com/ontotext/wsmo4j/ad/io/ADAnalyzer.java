/*
 AD extension - a WSMO API and Reference Implementation

 Copyright (c) 2004-2006, OntoText Lab. / SIRMA

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

package com.ontotext.wsmo4j.ad.io;

import java.util.Iterator;

import org.omwg.logicalexpression.LogicalExpression;
import org.wsmo.common.Entity;
import org.wsmo.common.Identifier;
import org.wsmo.common.TopEntity;
import org.wsmo.factory.ADFactory;
import org.wsmo.factory.ChoreographyFactory;
import org.wsmo.factory.WsmoFactory;
import org.wsmo.service.Interface;
import org.wsmo.service.ad.ADChoreography;
import org.wsmo.service.ad.ADOrchestration;
import org.wsmo.service.ad.ActionNode;
import org.wsmo.service.ad.ActivityDiagram;
import org.wsmo.service.ad.ActivityEdge;
import org.wsmo.service.ad.ActivityFinal;
import org.wsmo.service.ad.ActivityGroup;
import org.wsmo.service.ad.ActivityNode;
import org.wsmo.service.ad.Aggregation;
import org.wsmo.service.ad.ControlFlow;
import org.wsmo.service.ad.ControlNode;
import org.wsmo.service.ad.DataFlow;
import org.wsmo.service.ad.EventActionNode;
import org.wsmo.service.ad.Extraction;
import org.wsmo.service.ad.FlowFinal;
import org.wsmo.service.ad.FlowStart;
import org.wsmo.service.ad.Fork;
import org.wsmo.service.ad.GeneralAction;
import org.wsmo.service.ad.Join;
import org.wsmo.service.ad.Mediation;
import org.wsmo.service.ad.Merge;
import org.wsmo.service.ad.ObjectNode;
import org.wsmo.service.ad.Operation;
import org.wsmo.service.ad.Pin;
import org.wsmo.service.ad.ReceiveEventAction;
import org.wsmo.service.ad.SendEventAction;
import org.wsmo.service.signature.Mode;
import org.wsmo.service.signature.StateSignature;
import org.wsmo.wsml.compiler.node.AAccepteventactionNode;
import org.wsmo.wsml.compiler.node.AActivityDiagram;
import org.wsmo.wsml.compiler.node.AActivityfinalNode;
import org.wsmo.wsml.compiler.node.AActivitygroupGroup;
import org.wsmo.wsml.compiler.node.AAdlink;
import org.wsmo.wsml.compiler.node.AAggregationNode;
import org.wsmo.wsml.compiler.node.AAnonymousId;
import org.wsmo.wsml.compiler.node.ACarriesconcept;
import org.wsmo.wsml.compiler.node.AChorAdChoreographyFormalism;
import org.wsmo.wsml.compiler.node.AControlflowEdge;
import org.wsmo.wsml.compiler.node.ADataflowEdge;
import org.wsmo.wsml.compiler.node.ADecisionNode;
import org.wsmo.wsml.compiler.node.AEdgecontents;
import org.wsmo.wsml.compiler.node.AElseGuard;
import org.wsmo.wsml.compiler.node.AExtractionNode;
import org.wsmo.wsml.compiler.node.AFlowfinalNode;
import org.wsmo.wsml.compiler.node.AFlowstartNode;
import org.wsmo.wsml.compiler.node.AForkNode;
import org.wsmo.wsml.compiler.node.AGeneralactionNode;
import org.wsmo.wsml.compiler.node.AInputpinPin;
import org.wsmo.wsml.compiler.node.AInterruptibleregionGroup;
import org.wsmo.wsml.compiler.node.AJoinNode;
import org.wsmo.wsml.compiler.node.AMergeNode;
import org.wsmo.wsml.compiler.node.AObjectnodeNode;
import org.wsmo.wsml.compiler.node.AOomediatorNode;
import org.wsmo.wsml.compiler.node.AOperationNode;
import org.wsmo.wsml.compiler.node.AOrchAdOrchestrationFormalism;
import org.wsmo.wsml.compiler.node.AOutputpinPin;
import org.wsmo.wsml.compiler.node.ASendeventactionNode;
import org.wsmo.wsml.compiler.node.ASslink;
import org.wsmo.wsml.compiler.node.AStartnode;
import org.wsmo.wsml.compiler.node.PId;

import com.ontotext.wsmo4j.parser.wsml.ASTAnalysis;
import com.ontotext.wsmo4j.parser.wsml.ASTAnalysisContainer;
import com.ontotext.wsmo4j.parser.wsml.ServiceAnalysis;

@SuppressWarnings("unchecked")
public class ADAnalyzer extends ASTAnalysis {

    private ADFactory adFactory;

    private ChoreographyFactory cFactory;

    private WsmoFactory wsmoFactory;

    private ASTAnalysisContainer container;

    public ADAnalyzer(ASTAnalysisContainer container, WsmoFactory wsmoFactory,
            ChoreographyFactory cFactory, ADFactory adFactory) {
        if (container == null || cFactory == null || adFactory == null) {
            throw new IllegalArgumentException();
        }

        this.adFactory = adFactory;
        this.cFactory = cFactory;
        this.container = container;
        this.wsmoFactory = wsmoFactory;

        container.registerNodeHandler(AActivityDiagram.class, this);
        container.registerNodeHandler(AStartnode.class, this);
        container.registerNodeHandler(AActivitygroupGroup.class, this);
        container.registerNodeHandler(AInterruptibleregionGroup.class, this);
        container.registerNodeHandler(AOomediatorNode.class, this);
        container.registerNodeHandler(AOperationNode.class, this);
        container.registerNodeHandler(AExtractionNode.class, this);
        container.registerNodeHandler(AAggregationNode.class, this);
        container.registerNodeHandler(AGeneralactionNode.class, this);
        container.registerNodeHandler(ASendeventactionNode.class, this);
        container.registerNodeHandler(AAccepteventactionNode.class, this);
        container.registerNodeHandler(AAdlink.class, this);
        container.registerNodeHandler(AActivityfinalNode.class, this);
        container.registerNodeHandler(AForkNode.class, this);
        container.registerNodeHandler(AMergeNode.class, this);
        container.registerNodeHandler(AFlowfinalNode.class, this);
        container.registerNodeHandler(AFlowstartNode.class, this);
        container.registerNodeHandler(AJoinNode.class, this);
        container.registerNodeHandler(ADecisionNode.class, this);
        container.registerNodeHandler(AInputpinPin.class, this);
        container.registerNodeHandler(AOutputpinPin.class, this);
        container.registerNodeHandler(AControlflowEdge.class, this);
        container.registerNodeHandler(ADataflowEdge.class, this);
        container.registerNodeHandler(AEdgecontents.class, this);
        container.registerNodeHandler(AChorAdChoreographyFormalism.class, this);
        container.registerNodeHandler(AOrchAdOrchestrationFormalism.class, this);
    }

    // ADChoreography and ADOrchestration

    public void inAChorAdChoreographyFormalism(AChorAdChoreographyFormalism node) {
        org.wsmo.service.Choreography old = (org.wsmo.service.Choreography) container.popFromStack(
                org.wsmo.service.Choreography.class, org.wsmo.service.Choreography.class);
        ADChoreography newChoreography = adFactory.createADChoreography(old.getIdentifier());
        ServiceAnalysis.copyNFP(old, newChoreography);
        Interface iface = (Interface) container.peekFromStack(TopEntity.class, Interface.class);
        iface.setChoreography(newChoreography);
        container.getStack(org.wsmo.service.Choreography.class).push(newChoreography);
    }

    public void outAChorAdChoreographyFormalism(AChorAdChoreographyFormalism node) {
        ADChoreography newChor  = (ADChoreography) container.peekFromStack(
                org.wsmo.service.Choreography.class, ADChoreography.class);
        if (!container.getStack(StateSignature.class).isEmpty()) {
            newChor.setStateSignature((StateSignature) container.popFromStack(StateSignature.class,
                    StateSignature.class));
        }
        if (!container.getStack(ActivityGroup.class).isEmpty()) {
            newChor.setActivityDiagram((ActivityDiagram) container.popFromStack(ActivityDiagram.class,
                    ActivityDiagram.class));
        }
    }

    public void inAOrchAdOrchestrationFormalism(AOrchAdOrchestrationFormalism node) {
        org.wsmo.service.Orchestration old = (org.wsmo.service.Orchestration) container.popFromStack(
                org.wsmo.service.Orchestration.class, org.wsmo.service.Orchestration.class);
        ADOrchestration adOrch = adFactory.createADOrchestration(old.getIdentifier());
        ServiceAnalysis.copyNFP(old, adOrch);
        Interface iface = (Interface) container.peekFromStack(TopEntity.class, Interface.class);
        iface.setOrchestration(adOrch);
        container.getStack(org.wsmo.service.Orchestration.class).push(adOrch);
    }

    public void outAOrchAdOrchestrationFormalism(AOrchAdOrchestrationFormalism node) {
        ADOrchestration adOrch = (ADOrchestration) container.peekFromStack(
                org.wsmo.service.Orchestration.class, ADOrchestration.class);
        if (!container.getStack(StateSignature.class).isEmpty()) {
            adOrch.setStateSignature((StateSignature) container.popFromStack(StateSignature.class,
                    StateSignature.class));
        }
        if (!container.getStack(ActivityGroup.class).isEmpty()) {
            adOrch.setActivityDiagram((ActivityDiagram) container.popFromStack(ActivityDiagram.class,
                    ActivityDiagram.class));
        }
    }

    // ActivityDiagram process

    private Identifier startNodeId;

    public void inAActivityDiagram(AActivityDiagram node) {
        container.getStack(ActivityDiagram.class)
                .push(adFactory.containers.createActivityDiagram());
    }

    public void outAActivityDiagram(AActivityDiagram node) {
        ActivityDiagram ad = (ActivityDiagram) container.getStack(ActivityDiagram.class).peek();
        if (startNodeId != null) {
            ActivityNode an = adFactory.getActivityNode(startNodeId);
            if (an != null)
                ad.setStartNode(an);
        }
    }

    public void outAStartnode(AStartnode node) {
        startNodeId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
    }

    // ActivityGroup processing (self-registring to ActivityDiagram)

    public void inAActivitygroupGroup(AActivitygroupGroup node) {
        Identifier activityGroupId = null;
        if (node.getId() instanceof AAnonymousId || node.getId() == null) {
            activityGroupId = wsmoFactory.createAnonymousID();
        }
        else {
            node.getId().apply(container.getNodeHandler(PId.class));
            activityGroupId = (Identifier) container.popFromStack(Identifier.class,
                    Identifier.class);
        }
        ActivityGroup group = adFactory.containers.createActivityGroup(activityGroupId);
        processActivityGroup(group);
    }

    public void outAActivitygroupGroup(AActivitygroupGroup node) {
        container.getStack(ActivityGroup.class).pop();
    }

    public void inAInterruptibleregionGroup(AInterruptibleregionGroup node) {
        Identifier activityGroupId = null;
        if (node.getId() instanceof AAnonymousId || node.getId() == null) {
            activityGroupId = wsmoFactory.createAnonymousID();
        }
        else {
            node.getId().apply(container.getNodeHandler(PId.class));
            activityGroupId = (Identifier) container.popFromStack(Identifier.class,
                    Identifier.class);
        }
        ActivityGroup group = adFactory.containers.createInterruptibleRegion(activityGroupId);
        processActivityGroup(group);
    }

    public void outAInterruptibleregionGroup(AInterruptibleregionGroup node) {
        container.getStack(ActivityGroup.class).pop();
    }

    private void processActivityGroup(ActivityGroup group) {
        if (container.getStack(ActivityGroup.class).size() == 0) {
            container.getStack(ActivityGroup.class).push(group);
            ((ActivityDiagram) container.getStack(ActivityDiagram.class).peek())
                    .setActivityGroup(group);
        }
        else {
            ((ActivityGroup) container.getStack(ActivityGroup.class).peek()).addGroup(group);
        }
        container.getStack(ActivityGroup.class).push(group);
    }

    private void registerToLastGroup(ActivityNode node) {
        ((ActivityGroup) container.getStack(ActivityGroup.class).peek()).addNode(node);
    }

    private void registerToLastGroup(ActivityEdge edge) {
        ((ActivityGroup) container.getStack(ActivityGroup.class).peek()).addEdge(edge);
    }

    // ActionNode process (self-registring to ActivityGroup)

    public void inAOomediatorNode(AOomediatorNode node) {
        Identifier nodeId = null;
        if (node.getId() instanceof AAnonymousId || node.getId() == null) {
            nodeId = wsmoFactory.createAnonymousID();
        }
        else {
            node.getId().apply(container.getNodeHandler(PId.class));
            nodeId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        }
        Mediation mediationNode = adFactory.actionNodes.createMediation(nodeId);
        container.getStack(Entity.class).push(mediationNode);
        container.getStack(ActionNode.class).push(mediationNode);
        registerToLastGroup(mediationNode);
        // Get DefineBy part if available
        if (node.getDefinedby() != null) {
            node.getDefinedby().apply(container.getNodeHandler(PId.class));
            Identifier definedBy = (Identifier) container.popFromStack(Identifier.class,
                    Identifier.class);
            mediationNode.setDefinedBy(definedBy);
        }
    }

    public void outAOomediatorNode(AOomediatorNode node) {
        container.getStack(Entity.class).pop();
        container.getStack(ActionNode.class).pop();
    }

    public void inAOperationNode(AOperationNode node) {
        Identifier nodeId = null;
        if (node.getId() instanceof AAnonymousId || node.getId() == null) {
            nodeId = wsmoFactory.createAnonymousID();
        }
        else {
            node.getId().apply(container.getNodeHandler(PId.class));
            nodeId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        }
        Operation operationNode = adFactory.actionNodes.createOperation(nodeId);
        container.getStack(Entity.class).push(operationNode);
        container.getStack(ActionNode.class).push(operationNode);
        registerToLastGroup(operationNode);
        // Get DefineBy part if available
        if (node.getDefinedby() != null) {
            node.getDefinedby().apply(container.getNodeHandler(PId.class));
            Identifier definedBy = (Identifier) container.popFromStack(Identifier.class,
                    Identifier.class);
            operationNode.setDefinedBy(definedBy);
        }
    }

    public void outAOperationNode(AOperationNode node) {
        container.getStack(Entity.class).pop();
        container.getStack(ActionNode.class).pop();
    }

    public void inAExtractionNode(AExtractionNode node) {
        Identifier nodeId = null;
        if (node.getId() instanceof AAnonymousId || node.getId() == null) {
            nodeId = wsmoFactory.createAnonymousID();
        }
        else {
            node.getId().apply(container.getNodeHandler(PId.class));
            nodeId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        }
        Extraction extractionNode = adFactory.actionNodes.createExtraction(nodeId);
        container.getStack(Entity.class).push(extractionNode);
        container.getStack(ActionNode.class).push(extractionNode);
        registerToLastGroup(extractionNode);
        // Get DefineBy part if available
        if (node.getDefinedby() != null) {
            node.getDefinedby().apply(container.getNodeHandler(PId.class));
            Identifier definedBy = (Identifier) container.popFromStack(Identifier.class,
                    Identifier.class);
            extractionNode.setDefinedBy(definedBy);
        }
    }

    public void outAExtractionNode(AExtractionNode node) {
        container.getStack(Entity.class).pop();
        container.getStack(ActionNode.class).pop();
    }

    public void inAAggregationNode(AAggregationNode node) {
        Identifier nodeId = null;
        if (node.getId() instanceof AAnonymousId || node.getId() == null) {
            nodeId = wsmoFactory.createAnonymousID();
        }
        else {
            node.getId().apply(container.getNodeHandler(PId.class));
            nodeId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        }
        Aggregation aggregation = adFactory.actionNodes.createAggregation(nodeId);
        container.getStack(Entity.class).push(aggregation);
        container.getStack(ActionNode.class).push(aggregation);
        registerToLastGroup(aggregation);
        // Get DefineBy part if available
        if (node.getDefinedby() != null) {
            node.getDefinedby().apply(container.getNodeHandler(PId.class));
            Identifier definedBy = (Identifier) container.popFromStack(Identifier.class,
                    Identifier.class);
            aggregation.setDefinedBy(definedBy);
        }
    }

    public void outAAggregationNode(AAggregationNode node) {
        container.getStack(Entity.class).pop();
        container.getStack(ActionNode.class).pop();
    }

    public void inAGeneralactionNode(AGeneralactionNode node) {
        Identifier nodeId = null;
        if (node.getId() instanceof AAnonymousId || node.getId() == null) {
            nodeId = wsmoFactory.createAnonymousID();
        }
        else {
            node.getId().apply(container.getNodeHandler(PId.class));
            nodeId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        }
        GeneralAction genAction = adFactory.actionNodes.createGeneralAction(nodeId);
        container.getStack(Entity.class).push(genAction);
        container.getStack(ActionNode.class).push(genAction);
        registerToLastGroup(genAction);
    }

    public void outAGeneralactionNode(AGeneralactionNode node) {
        container.getStack(Entity.class).pop();
        container.getStack(ActionNode.class).pop();
    }

    public void inASendeventactionNode(ASendeventactionNode node) {
        Identifier nodeId = null;
        if (node.getId() instanceof AAnonymousId || node.getId() == null) {
            nodeId = wsmoFactory.createAnonymousID();
        }
        else {
            node.getId().apply(container.getNodeHandler(PId.class));
            nodeId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        }
        SendEventAction send = adFactory.actionNodes.createSendEventAction(nodeId);
        container.getStack(Entity.class).push(send);
        container.getStack(ActionNode.class).push(send);
        container.getStack(EventActionNode.class).push(send);
        registerToLastGroup(send);
        if (node.getSslink() != null) {
            Identifier sslinkId = null;
            ((ASslink) node.getSslink()).getId().apply(container.getNodeHandler(PId.class));
            sslinkId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
            send.setMode(getSSLinkMode(sslinkId));
        }
    }

    public void outASendeventactionNode(ASendeventactionNode node) {
        container.getStack(Entity.class).pop();
        container.getStack(ActionNode.class).pop();
        container.getStack(EventActionNode.class).pop();
    }

    public void inAAccepteventactionNode(AAccepteventactionNode node) {
        Identifier nodeId = null;
        if (node.getId() instanceof AAnonymousId || node.getId() == null) {
            nodeId = wsmoFactory.createAnonymousID();
        }
        else {
            node.getId().apply(container.getNodeHandler(PId.class));
            nodeId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        }
        ReceiveEventAction recieve = adFactory.actionNodes.createRecieveEventAction(nodeId);
        container.getStack(Entity.class).push(recieve);
        container.getStack(ActionNode.class).push(recieve);
        container.getStack(EventActionNode.class).push(recieve);
        registerToLastGroup(recieve);
        if (node.getSslink() != null) {
            Identifier sslinkId = null;
            ((ASslink) node.getSslink()).getId().apply(container.getNodeHandler(PId.class));
            sslinkId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
            recieve.setMode(getSSLinkMode(sslinkId));
        }
    }

    public void outAAccepteventactionNode(AAccepteventactionNode node) {
        container.getStack(Entity.class).pop();
        container.getStack(ActionNode.class).pop();
        container.getStack(EventActionNode.class).pop();
    }

    public void inAAdlink(AAdlink node) {
        EventActionNode ean = (EventActionNode) container.getStack(EventActionNode.class).peek();
        if (node.getCarriesconcept() != null) {
            Identifier carriesConcept = null;
            ((ACarriesconcept) node.getCarriesconcept()).getId().apply(
                    container.getNodeHandler(PId.class));
            carriesConcept = (Identifier) container
                    .popFromStack(Identifier.class, Identifier.class);
            ean.setCarriesConcept(wsmoFactory.getConcept(carriesConcept));
        }
        if (node.getPartnerlink() != null) {
            Identifier partnerId = null;
            node.getPartnerlink().apply(container.getNodeHandler(PId.class));
            partnerId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
            ean.setPartnerLink(partnerId);
        }
    }

    public void inAObjectnodeNode(AObjectnodeNode node) {
        Identifier nodeId = null;
        if (node.getId() instanceof AAnonymousId || node.getId() == null) {
            nodeId = wsmoFactory.createAnonymousID();
        }
        else {
            node.getId().apply(container.getNodeHandler(PId.class));
            nodeId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        }
        ObjectNode objNode = adFactory.objectNodes.createObjectNode(nodeId);
        if (node.getCarriesconcept() != null) {
            Identifier carriesConcept = null;
            ((ACarriesconcept) node.getCarriesconcept()).getId().apply(
                    container.getNodeHandler(PId.class));
            carriesConcept = (Identifier) container
                    .popFromStack(Identifier.class, Identifier.class);
            objNode.setCarriesConcept(wsmoFactory.getConcept(carriesConcept));
        }
        if (node.getSslink() != null) {
            Identifier sslinkId = null;
            ((ASslink) node.getSslink()).getId().apply(container.getNodeHandler(PId.class));
            sslinkId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
            objNode.setMode(getSSLinkMode(sslinkId));
        }
        container.getStack(Entity.class).push(objNode);
        container.getStack(ActionNode.class).push(objNode);
    }

    public void outAObjectnodeNode(AObjectnodeNode node) {
        container.getStack(Entity.class).pop();
        container.getStack(ActionNode.class).pop();
    }

    // ControlNode (self-regestring to ActivityGroup)

    public void inAActivityfinalNode(AActivityfinalNode node) {
        Identifier nodeId = null;
        if (node.getId() instanceof AAnonymousId || node.getId() == null) {
            nodeId = wsmoFactory.createAnonymousID();
        }
        else {
            node.getId().apply(container.getNodeHandler(PId.class));
            nodeId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        }
        ActivityFinal activityFinal = adFactory.controlNodes.createActivityFinal(nodeId);
        container.getStack(Entity.class).push(activityFinal);
        container.getStack(ControlNode.class).push(activityFinal);
        registerToLastGroup(activityFinal);
    }

    public void outAActivityfinalNode(AActivityfinalNode node) {
        container.getStack(Entity.class).pop();
        container.getStack(ControlNode.class).pop();
    }

    public void inAForkNode(AForkNode node) {
        Identifier nodeId = null;
        if (node.getId() instanceof AAnonymousId || node.getId() == null) {
            nodeId = wsmoFactory.createAnonymousID();
        }
        else {
            node.getId().apply(container.getNodeHandler(PId.class));
            nodeId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        }
        Fork fork = adFactory.controlNodes.createFork(nodeId);
        container.getStack(Entity.class).push(fork);
        container.getStack(ControlNode.class).push(fork);
        registerToLastGroup(fork);
    }

    public void outAForkNode(AForkNode node) {
        container.getStack(Entity.class).pop();
        container.getStack(ControlNode.class).pop();
    }

    public void inAMergeNode(AMergeNode node) {
        Identifier nodeId = null;
        if (node.getId() instanceof AAnonymousId || node.getId() == null) {
            nodeId = wsmoFactory.createAnonymousID();
        }
        else {
            node.getId().apply(container.getNodeHandler(PId.class));
            nodeId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        }
        Merge merge = adFactory.controlNodes.createMerge(nodeId);
        container.getStack(Entity.class).push(merge);
        container.getStack(ControlNode.class).push(merge);
        registerToLastGroup(merge);
    }

    public void outAMergeNode(AMergeNode node) {
        container.getStack(Entity.class).pop();
        container.getStack(ControlNode.class).pop();
    }

    public void inAFlowfinalNode(AFlowfinalNode node) {
        Identifier nodeId = null;
        if (node.getId() instanceof AAnonymousId || node.getId() == null) {
            nodeId = wsmoFactory.createAnonymousID();
        }
        else {
            node.getId().apply(container.getNodeHandler(PId.class));
            nodeId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        }
        FlowFinal finall = adFactory.controlNodes.createFlowFinal(nodeId);
        container.getStack(Entity.class).push(finall);
        container.getStack(ControlNode.class).push(finall);
        registerToLastGroup(finall);
    }

    public void outAFlowfinalNode(AFlowfinalNode node) {
        container.getStack(Entity.class).pop();
        container.getStack(ControlNode.class).pop();
    }

    public void inAFlowstartNode(AFlowstartNode node) {
        Identifier nodeId = null;
        if (node.getId() instanceof AAnonymousId || node.getId() == null) {
            nodeId = wsmoFactory.createAnonymousID();
        }
        else {
            node.getId().apply(container.getNodeHandler(PId.class));
            nodeId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        }
        FlowStart start = adFactory.controlNodes.createFlowStart(nodeId);
        container.getStack(Entity.class).push(start);
        container.getStack(ControlNode.class).push(start);
        registerToLastGroup(start);
    }

    public void outAFlowstartNode(AFlowstartNode node) {
        container.getStack(Entity.class).pop();
        container.getStack(ControlNode.class).pop();
    }

    public void inAJoinNode(AJoinNode node) {
        Identifier nodeId = null;
        if (node.getId() instanceof AAnonymousId || node.getId() == null) {
            nodeId = wsmoFactory.createAnonymousID();
        }
        else {
            node.getId().apply(container.getNodeHandler(PId.class));
            nodeId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        }
        Join join = adFactory.controlNodes.createJoin(nodeId);
        container.getStack(Entity.class).push(join);
        container.getStack(ControlNode.class).push(join);
        registerToLastGroup(join);
    }

    public void outAJoinNode(AJoinNode node) {
        container.getStack(Entity.class).pop();
        container.getStack(ControlNode.class).pop();
    }

    public void inADecisionNode(ADecisionNode node) {
        Identifier nodeId = null;
        if (node.getId() instanceof AAnonymousId || node.getId() == null) {
            nodeId = wsmoFactory.createAnonymousID();
        }
        else {
            node.getId().apply(container.getNodeHandler(PId.class));
            nodeId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        }
        Join join = adFactory.controlNodes.createJoin(nodeId);
        container.getStack(Entity.class).push(join);
        container.getStack(ControlNode.class).push(join);
        registerToLastGroup(join);
    }

    public void outADecisionNode(ADecisionNode node) {
        container.getStack(Entity.class).pop();
        container.getStack(ControlNode.class).pop();
    }

    // ObjectNodes (self-regestring to appropriate ActionNode)

    public void inAInputpinPin(AInputpinPin node) {
        Identifier pinId = null;
        if (node.getId() instanceof AAnonymousId || node.getId() == null) {
            pinId = wsmoFactory.createAnonymousID();
        }
        else {
            node.getId().apply(container.getNodeHandler(PId.class));
            pinId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        }
        Pin pin = adFactory.objectNodes.createPin(pinId);

        if (node.getSslink() != null) {
            Identifier sslinkId = null;
            ((ASslink) node.getSslink()).getId().apply(container.getNodeHandler(PId.class));
            sslinkId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
            pin.setMode(getSSLinkMode(sslinkId));
        }
        if (node.getCarriesconcept() != null) {
            Identifier carriesConcept = null;
            ((ACarriesconcept) node.getCarriesconcept()).getId().apply(
                    container.getNodeHandler(PId.class));
            carriesConcept = (Identifier) container
                    .popFromStack(Identifier.class, Identifier.class);
            pin.setCarriesConcept(wsmoFactory.getConcept(carriesConcept));
        }
        ((ActionNode) container.getStack(ActionNode.class).peek()).addInputPin(pin);
        container.getStack(Entity.class).push(pin);
    }

    public void outAInputpinPin(AInputpinPin node) {
        container.getStack(Entity.class).pop();
    }

    public void inAOutputpinPin(AOutputpinPin node) {
        Identifier pinId = null;
        if (node.getId() instanceof AAnonymousId || node.getId() == null) {
            pinId = wsmoFactory.createAnonymousID();
        }
        else {
            node.getId().apply(container.getNodeHandler(PId.class));
            pinId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        }
        Pin pin = adFactory.objectNodes.createPin(pinId);

        if (node.getSslink() != null) {
            Identifier sslinkId = null;
            ((ASslink) node.getSslink()).getId().apply(container.getNodeHandler(PId.class));
            sslinkId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
            pin.setMode(getSSLinkMode(sslinkId));
        }
        if (node.getCarriesconcept() != null) {
            Identifier carriesConcept = null;
            ((ACarriesconcept) node.getCarriesconcept()).getId().apply(
                    container.getNodeHandler(PId.class));
            carriesConcept = (Identifier) container
                    .popFromStack(Identifier.class, Identifier.class);
            pin.setCarriesConcept(wsmoFactory.getConcept(carriesConcept));
        }
        ((ActionNode) container.getStack(ActionNode.class).peek()).addOutputPin(pin);
    }

    public void outAOutputpinPin(AOutputpinPin node) {
        container.getStack(Entity.class).pop();
    }

    //TODO: The Mode type is unknown if not already defined in the StateSignature
    private Mode getSSLinkMode(Identifier id) {
        if (container.getStack(StateSignature.class).size() > 0) {
            StateSignature ss = (StateSignature) container.getStack(StateSignature.class).peek();
            for (Iterator i = ss.iterator(); i.hasNext();) {
                Mode mode = (Mode) i.next();
                if (mode.getConcept() != null && mode.getConcept().getIdentifier().equals(id))
                    return mode;
                if (mode.getRelation() != null && mode.getRelation().getIdentifier().equals(id))
                    return mode;
            }
        }
        //TODO: Remove the hardcoded generated In mode
        return cFactory.modes.createIn(wsmoFactory.getConcept(id));
    }

    // Edges (self-registring in the ActivityGroup)
    public void inAControlflowEdge(AControlflowEdge node) {
        Identifier edgeId = null;
        if (node.getId() instanceof AAnonymousId || node.getId() == null) {
            edgeId = wsmoFactory.createAnonymousID();
        }
        else {
            node.getId().apply(container.getNodeHandler(PId.class));
            edgeId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        }
        ControlFlow cf = adFactory.edges.createControlFlow(edgeId);
        container.getStack(Entity.class).push(cf);
        container.getStack(ActivityEdge.class).push(cf);
        registerToLastGroup(cf);
    }

    public void outAControlflowEdge(AControlflowEdge node) {
        container.getStack(Entity.class).pop();
        container.getStack(ActivityEdge.class).pop();
    }

    public void inADataflowEdge(ADataflowEdge node) {
        Identifier edgeId = null;
        if (node.getId() instanceof AAnonymousId || node.getId() == null) {
            edgeId = wsmoFactory.createAnonymousID();
        }
        else {
            node.getId().apply(container.getNodeHandler(PId.class));
            edgeId = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        }
        DataFlow df = adFactory.edges.createDataFlow(edgeId);
        container.getStack(Entity.class).push(df);
        container.getStack(ActivityEdge.class).push(df);
        registerToLastGroup(df);
    }

    public void outADataflowEdge(ADataflowEdge node) {
        container.getStack(Entity.class).pop();
        container.getStack(ActivityEdge.class).pop();
    }

    public void inAEdgecontents(AEdgecontents node) {
        ActivityEdge ae = (ActivityEdge) container.getStack(ActivityEdge.class).peek();
        node.getSource().apply(container.getNodeHandler(PId.class));
        ae.setSource(adFactory.getActivityNode((Identifier) container.popFromStack(
                Identifier.class, Identifier.class)));

        node.getId().apply(container.getNodeHandler(PId.class));
        ae.setTarget(adFactory.getActivityNode((Identifier) container.popFromStack(
                Identifier.class, Identifier.class)));

        if (node.getInterrupting() != null) {
            node.getInterrupting().apply(container.getNodeHandler(PId.class));
            ae.setInterruptibleRegion(adFactory.containers
                    .createInterruptibleRegion((Identifier) container.popFromStack(
                            Identifier.class, Identifier.class)));
        }
    }

    public void outAEdgecontents(AEdgecontents node) {
        if (node.getGuard() == null)
            return;
        ActivityEdge ae = (ActivityEdge) container.getStack(ActivityEdge.class).peek();
        if (node.getGuard() instanceof AElseGuard) {
            ae.setGuard(adFactory.guards.createGuardElse());
        }
        else {
            ae.setGuard(adFactory.guards.createGuardExpression((LogicalExpression) container
                    .getStack(LogicalExpression.class).pop()));
        }
    }
}

/*
 * $Log: ADAnalyzer.java,v $
 * Revision 1.2  2007/04/02 13:05:17  morcen
 * Generics support added to wsmo-ext
 *
 * Revision 1.1  2006/12/08 17:11:13  vassil_momtchev
 * initial version of the parser
 *
*/
