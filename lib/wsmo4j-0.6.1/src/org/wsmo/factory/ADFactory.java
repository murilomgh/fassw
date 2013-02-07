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

package org.wsmo.factory;

import org.omwg.logicalexpression.*;
import org.wsmo.common.*;
import org.wsmo.service.ad.*;

public abstract class ADFactory {

    public interface Containers {
        public ActivityDiagram createActivityDiagram();

        public ActivityGroup createActivityGroup(Identifier id);

        public InterruptibleRegion createInterruptibleRegion(Identifier id);
    }

    public interface ControlNodes {
        public Decision createDecision(Identifier id);

        public Merge createMerge(Identifier id);

        public FlowStart createFlowStart(Identifier id);

        public FlowFinal createFlowFinal(Identifier id);

        public ActivityFinal createActivityFinal(Identifier id);

        public Fork createFork(Identifier id);

        public Join createJoin(Identifier id);
    }

    public interface ActionNodes {
        public Operation createOperation(Identifier id);

        public Aggregation createAggregation(Identifier id);

        public Extraction createExtraction(Identifier id);

        public Mediation createMediation(Identifier id);

        public GeneralAction createGeneralAction(Identifier id);

        public ReceiveEventAction createRecieveEventAction(Identifier id);

        public SendEventAction createSendEventAction(Identifier id);
    }

    public interface ObjectNodes {
        public ObjectNode createObjectNode(Identifier id);

        public Pin createPin(Identifier id);
    }

    public interface Edges {
        public DataFlow createDataFlow(Identifier id);

        public ControlFlow createControlFlow(Identifier id);
    }

    public interface Guards {
        public GuardElse createGuardElse();

        public GuardExpression createGuardExpression(LogicalExpression le);
    }

    public final Containers containers = createContainers();
    public final ControlNodes controlNodes = createControlNodes();
    public final ObjectNodes objectNodes = createObjectNodes();
    public final ActionNodes actionNodes = createActionNodes();
    public final Edges edges = createEdges();
    public final Guards guards = createGuards();


    protected abstract Containers createContainers();

    protected abstract ControlNodes createControlNodes();

    protected abstract ObjectNodes createObjectNodes();

    protected abstract ActionNodes createActionNodes();

    protected abstract Edges createEdges();

    protected abstract Guards createGuards();

    public abstract ActivityEdge getActivityEdge(Identifier id);

    public abstract ActivityNode getActivityNode(Identifier id);

    public abstract ADChoreography createADChoreography(Identifier id);

    public abstract ADOrchestration createADOrchestration(Identifier id);
}

/*
 * $Log: ADFactory.java,v $
 * Revision 1.1  2006/12/08 17:11:06  vassil_momtchev
 * initial version of the parser
 *
*/
