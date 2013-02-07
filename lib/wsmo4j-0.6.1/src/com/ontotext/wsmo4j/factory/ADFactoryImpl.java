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

package com.ontotext.wsmo4j.factory;

import java.util.*;

import org.omwg.logicalexpression.*;
import org.wsmo.common.*;
import org.wsmo.factory.*;
import org.wsmo.service.ad.*;

import com.ontotext.wsmo4j.ad.*;

public class ADFactoryImpl extends ADFactory {

    private Map<Identifier, Object> registry = new HashMap<Identifier, Object>();

    protected Containers createContainers() {
        return new ContainersImpl();
    }

    protected ControlNodes createControlNodes() {
        return new ControlNodesImpl();
    }
    protected ObjectNodes createObjectNodes() {
        return new ObjectNodesImpl();
    }
    protected ActionNodes createActionNodes() {
        return new ActionNodesImpl();
    }
    protected Edges createEdges() {
        return new EdgesImpl();
    }
    protected Guards createGuards() {
        return new GuardsImpl();
    }

    private class ContainersImpl implements Containers {

        public ActivityDiagram createActivityDiagram() {
            return new ActivityDiagramImpl();
        }

        public synchronized ActivityGroup createActivityGroup(Identifier id) {
            Object o = registry.get(id);
            if (o != null && o instanceof ActivityGroup) {
                return (ActivityGroup) o;
            }
            ActivityGroup result = new ActivityGroupImpl(id);
            registry.put(id, result);
            return result;
        }

        public synchronized InterruptibleRegion createInterruptibleRegion(Identifier id) {
            Object o = registry.get(id);
            if (o != null && o instanceof InterruptibleRegion) {
                return (InterruptibleRegion) o;
            }
            InterruptibleRegion result = new InterruptibleRegionImpl(id);
            registry.put(id, result);
            return result;
        }
    }

    private class ControlNodesImpl implements ControlNodes {

        public synchronized Decision createDecision(Identifier id) {
            Object o = registry.get(id);
            if (o != null && o instanceof Decision) {
                return (Decision) o;
            }
            Decision result = new DecisionImpl(id);
            registry.put(id, result);
            return result;
        }

        public synchronized Merge createMerge(Identifier id) {
            Object o = registry.get(id);
            if (o != null && o instanceof Merge) {
                return (Merge) o;
            }
            Merge result = new MergeImpl(id);
            registry.put(id, result);
            return result;
        }

        public synchronized FlowStart createFlowStart(Identifier id) {
            Object o = registry.get(id);
            if (o != null && o instanceof FlowStart) {
                return (FlowStart) o;
            }
            FlowStart result = new FlowStartImpl(id);
            registry.put(id, result);
            return result;
        }

        public synchronized FlowFinal createFlowFinal(Identifier id) {
            Object o = registry.get(id);
            if (o != null && o instanceof FlowFinal) {
                return (FlowFinal) o;
            }
            FlowFinal result = new FlowFinalImpl(id);
            registry.put(id, result);
            return result;
        }

        public synchronized ActivityFinal createActivityFinal(Identifier id) {
            Object o = registry.get(id);
            if (o != null && o instanceof ActivityFinal) {
                return (ActivityFinal) o;
            }
            ActivityFinal result = new ActivityFinalImpl(id);
            registry.put(id, result);
            return result;
        }

        public synchronized Fork createFork(Identifier id) {
            Object o = registry.get(id);
            if (o != null && o instanceof Fork) {
                return (Fork) o;
            }
            Fork result = new ForkImpl(id);
            registry.put(id, result);
            return result;
        }

        public synchronized Join createJoin(Identifier id) {
            Object o = registry.get(id);
            if (o != null && o instanceof Join) {
                return (Join) o;
            }
            Join result = new JoinImpl(id);
            registry.put(id, result);
            return result;
        }
    }

    private class ObjectNodesImpl implements ObjectNodes {

        public synchronized ObjectNode createObjectNode(Identifier id) {
            Object o = registry.get(id);
            if (o != null && o instanceof ObjectNode) {
                return (ObjectNode) o;
            }
            ObjectNode result = new ObjectNodeImpl(id);
            registry.put(id, result);
            return result;
        }
        public synchronized Pin createPin(Identifier id) {
            Object o = registry.get(id);
            if (o != null && o instanceof Pin) {
                return (Pin) o;
            }
            Pin result = new PinImpl(id);
            registry.put(id, result);
            return result;
        }
    }

    private class ActionNodesImpl implements ActionNodes {

        public synchronized Operation createOperation(Identifier id) {
            Object o = registry.get(id);
            if (o != null && o instanceof Operation) {
                return (Operation) o;
            }
            Operation result = new OperationImpl(id);
            registry.put(id, result);
            return result;
        }
        public synchronized Aggregation createAggregation(Identifier id) {
            Object o = registry.get(id);
            if (o != null && o instanceof Aggregation) {
                return (Aggregation) o;
            }
            Aggregation result = new AggregationImpl(id);
            registry.put(id, result);
            return result;
        }
        public synchronized Extraction createExtraction(Identifier id) {
            Object o = registry.get(id);
            if (o != null && o instanceof Extraction) {
                return (Extraction) o;
            }
            Extraction result = new ExtractionImpl(id);
            registry.put(id, result);
            return result;
        }
        public synchronized Mediation createMediation(Identifier id) {
            Object o = registry.get(id);
            if (o != null && o instanceof Mediation) {
                return (Mediation) o;
            }
            Mediation result = new MediationImpl(id);
            registry.put(id, result);
            return result;
        }
        public synchronized GeneralAction createGeneralAction(Identifier id) {
            Object o = registry.get(id);
            if (o != null && o instanceof GeneralAction) {
                return (GeneralAction) o;
            }
            GeneralAction result = new GeneralActionImpl(id);
            registry.put(id, result);
            return result;
        }
        public synchronized ReceiveEventAction createRecieveEventAction(Identifier id) {
            Object o = registry.get(id);
            if (o != null && o instanceof ReceiveEventAction) {
                return (ReceiveEventAction) o;
            }
            ReceiveEventAction result = new ReceiveEventActionImpl(id);
            registry.put(id, result);
            return result;
        }
        public synchronized SendEventAction createSendEventAction(Identifier id) {
            Object o = registry.get(id);
            if (o != null && o instanceof SendEventAction) {
                return (SendEventAction) o;
            }
            SendEventAction result = new SendEventActionImpl(id);
            registry.put(id, result);
            return result;
        }
    }

    private class EdgesImpl implements Edges {

        public synchronized DataFlow createDataFlow(Identifier id) {
            Object o = registry.get(id);
            if (o != null && o instanceof DataFlow) {
                return (DataFlow) o;
            }
            DataFlow result = new DataFlowImpl(id);
            registry.put(id, result);
            return result;
        }
        public synchronized ControlFlow createControlFlow(Identifier id) {
            Object o = registry.get(id);
            if (o != null && o instanceof ControlFlow) {
                return (ControlFlow) o;
            }
            ControlFlow result = new ControlFlowImpl(id);
            registry.put(id, result);
            return result;
        }
    }

    private class GuardsImpl implements Guards {

        public GuardElse createGuardElse() {
            return new GuardElse() {};
        }
        public GuardExpression createGuardExpression(LogicalExpression le) {
            return new GuardExpressionImpl(le);
        }

    }

    public ActivityEdge getActivityEdge(Identifier id) {
        Object o = registry.get(id);
        if (o != null && o instanceof ActivityEdge) {
            return (ActivityEdge) o;
        }
        return null;
    }

    public ActivityNode getActivityNode(Identifier id) {
        Object o = registry.get(id);
        if (o != null && o instanceof ActivityNode) {
            return (ActivityNode) o;
        }
        return null;
    }

    public ADChoreography createADChoreography(Identifier id) {
        Object o = registry.get(id);
        if (o != null && o instanceof ADChoreography) {
            return (ADChoreography) o;
        }
        ADChoreography result = new ADChoreographyImpl(id);
        registry.put(id, result);
        return result;
    }

    public ADOrchestration createADOrchestration(Identifier id) {
        Object o = registry.get(id);
        if (o != null && o instanceof ADOrchestration) {
            return (ADOrchestration) o;
        }
        ADOrchestration result = new ADOrchestrationImpl(id);
        registry.put(id, result);
        return result;
    }
}

/*
 * $Log: ADFactoryImpl.java,v $
 * Revision 1.1  2006/12/08 17:11:14  vassil_momtchev
 * initial version of the parser
 *
*/
