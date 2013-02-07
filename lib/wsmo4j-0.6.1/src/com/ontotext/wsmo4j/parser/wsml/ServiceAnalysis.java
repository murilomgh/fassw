/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2004-2005, OntoText Lab. / SIRMA
                          University of Innsbruck, Austria

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

package com.ontotext.wsmo4j.parser.wsml;

import java.util.*;
import java.util.Map.Entry;

import org.deri.wsmo4j.common.*;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.factory.*;
import org.wsmo.service.*;
import org.wsmo.wsml.compiler.analysis.*;
import org.wsmo.wsml.compiler.node.*;

import com.ontotext.wsmo4j.parser.*;

public class ServiceAnalysis extends ASTAnalysis {

    private WsmoFactory factory;
    
    private LogicalExpressionFactory leFactory;
    
    private ASTAnalysisContainer container;
    
    private boolean cleanOnParse = false; 

    public ServiceAnalysis(ASTAnalysisContainer container, WsmoFactory factory, 
            LogicalExpressionFactory leFactory) {
        if (container == null || factory == null) {
            throw new IllegalArgumentException();
        }
        this.factory = factory;
        this.container = container;
        this.leFactory = leFactory;

        // register the handled nodes
        container.registerNodeHandler(AWebservice.class, this);
        container.registerNodeHandler(AGoal.class, this);
        container.registerNodeHandler(ACapability.class, this);
        container.registerNodeHandler(APostconditionPrePostAssOrEff.class, this);
        container.registerNodeHandler(APreconditionPrePostAssOrEff.class, this);
        container.registerNodeHandler(AAssumptionPrePostAssOrEff.class, this);
        container.registerNodeHandler(AEffectPrePostAssOrEff.class, this);
        container.registerNodeHandler(ASharedvardef.class, this);
        container.registerNodeHandler(AInterface.class, this);
        container.registerNodeHandler(AChoreography.class, this);
        container.registerNodeHandler(AOrchestration.class, this);
    }
    
    public void setCleanOnParse(boolean cleanOnParse){
        this.cleanOnParse=cleanOnParse;
    }

    private void cleanServiceDescription(ServiceDescription s){
        //clean previous contents of this mediator:
        try {
            ClearTopEntity.clearTopEntity(s);
        }
        catch (SynchronisationException e) {
            // should never happen
            throw new RuntimeException("Error During Cleaning TopEntity from previous defintions",e);
        }
        catch (InvalidModelException e) {
            // should never happen
            throw new RuntimeException("Error During Cleaning TopEntity from previous defintions",e);
        }

    }

    public void inAWebservice(AWebservice node) {
        TopEntityAnalysis.isValidTopEntityIdentifier(node.getId(),node.getTWebservice());
        node.getId().apply(container.getNodeHandler(PId.class));
        IRI iri = (IRI) container.popFromStack(Identifier.class, IRI.class);
        WebService ws = factory.createWebService(iri);
        if (cleanOnParse){
            cleanServiceDescription(ws);
        }
        copyNSAndAddToStacks(ws);
    }

    public void outAWebservice(AWebservice node) {
        container.popFromStack(Entity.class, WebService.class);
    }

    public void inAGoal(AGoal node) {
        TopEntityAnalysis.isValidTopEntityIdentifier(node.getId(),node.getTGoal());
        node.getId().apply(container.getNodeHandler(PId.class));
        IRI id = (IRI) container.popFromStack(Identifier.class, IRI.class);
        Goal g = factory.createGoal(id);
        if (cleanOnParse){
            cleanServiceDescription(g);
        }
        copyNSAndAddToStacks(g);
    }

    public void outAGoal(AGoal node) {
        container.popFromStack(Entity.class, Goal.class);
    }

    public void inACapability(ACapability node) {
        TopEntityAnalysis.isValidTopEntityIdentifier(node.getId(), node.getTCapability());
        node.getId().apply(container.getNodeHandler(PId.class));
        IRI id = (IRI) container.popFromStack(Identifier.class, IRI.class);
        Capability capability = factory.createCapability(id);

        // find if it's in a Goal
        Stack entities = container.getStack(Entity.class);
        for (int i = entities.size() - 1; i >= 0; i--) {
            Entity entity = (Entity) entities.elementAt(i);
            if (entity instanceof ServiceDescription) {
                ((ServiceDescription) entity).setCapability(capability);
                break;
            }
        }

        copyNSAndAddToStacks(capability);
    }

    public void outACapability(ACapability node) {
        container.popFromStack(Entity.class, Capability.class);
    }

    public void outAPostconditionPrePostAssOrEff(APostconditionPrePostAssOrEff node) {
        Capability capability = (Capability) container.peekFromStack(TopEntity.class,
                Capability.class);
        try {
            capability.addPostCondition((Axiom) container.peekFromStack(Axiom.class, Axiom.class));
        }
        catch (InvalidModelException e) {
            throw new WrappedInvalidModelException(e);
        }
    }

    public void outAPreconditionPrePostAssOrEff(APreconditionPrePostAssOrEff node) {
        Capability capability = (Capability) container.peekFromStack(TopEntity.class,
                Capability.class);
        try {
            capability.addPreCondition((Axiom) container.popFromStack(Axiom.class, Axiom.class));
        }
        catch (InvalidModelException e) {
            throw new WrappedInvalidModelException(e);
        }
    }

    public void outAAssumptionPrePostAssOrEff(AAssumptionPrePostAssOrEff node) {
        Capability capability = (Capability) container.peekFromStack(TopEntity.class,
                Capability.class);
        try {
            capability.addAssumption((Axiom) container.popFromStack(Axiom.class, Axiom.class));
        }
        catch (InvalidModelException e) {
            throw new WrappedInvalidModelException(e);
        }
    }

    public void outAEffectPrePostAssOrEff(AEffectPrePostAssOrEff node) {
        Capability capability = (Capability) container.peekFromStack(TopEntity.class,
                Capability.class);
        try {
            capability.addEffect((Axiom) container.popFromStack(Axiom.class, Axiom.class));
        }
        catch (InvalidModelException e) {
            throw new WrappedInvalidModelException(e);
        }
    }

    public void inASharedvardef(ASharedvardef node) {
        final Capability capability = (Capability) container.peekFromStack(TopEntity.class,
                Capability.class);
        if (node != null) {
            node.apply(new DepthFirstAdapter() {
                public void caseTVariable(TVariable var) {
                    String str = var.getText().trim();
                    try {
                        capability.addSharedVariable(leFactory.createVariable(str));
                    }
                    catch (InvalidModelException ime) {
                        throw new WrappedInvalidModelException(ime);
                    }
                }
            } // close anonymous class
                    );
        }
    }

    public void inAInterface(AInterface node) {
        TopEntityAnalysis.isValidTopEntityIdentifier(node.getId(),node.getTInterface());
        node.getId().apply(container.getNodeHandler(PId.class));
        IRI iri = (IRI) container.popFromStack(Identifier.class, IRI.class);
        Interface iface = factory.createInterface(iri);

        // find if it's a child of WebService or Goal
        Stack entities = container.getStack(Entity.class);
        for (int i = entities.size() - 1; i >= 0; i--) {
            Entity entity = (Entity) entities.elementAt(i);
            if (entity instanceof WebService) {
                ((WebService) entity).addInterface(iface);
                break;
            }
            else if (entity instanceof Goal) {
                ((Goal) entity).addInterface(iface);
                break;
            }
        }

        copyNSAndAddToStacks(iface);
    }

    public void outAInterface(AInterface node) {
        container.popFromStack(Entity.class, Interface.class);
    }
    
    public void inAOrchestration(AOrchestration node) {
        Identifier id = null;
        if (node.getId() != null) {
            node.getId().apply(container.getNodeHandler(PId.class));
            id = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        }
        else {
            id = factory.createAnonymousID();
        }
        Interface iface = (Interface) container.peekFromStack(TopEntity.class, Interface.class);
        Orchestration orchestration = iface.createOrchestration(id);
        container.getStack(Orchestration.class).push(orchestration);
        container.getStack(Entity.class).push(orchestration);
    }
    
    public void outAOrchestration(AOrchestration node) {
        container.popFromStack(Orchestration.class, Orchestration.class);
        container.popFromStack(Entity.class, Orchestration.class);
    }
    
    public void inAChoreography(AChoreography node) {        
        Identifier id = null;
        if (node.getId() != null) {
            node.getId().apply(container.getNodeHandler(PId.class));
            id = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        }
        else {
            id = factory.createAnonymousID();
        }
        Interface iface = (Interface) container.peekFromStack(TopEntity.class, Interface.class);
        Choreography choreography = iface.createChoreography(id);
        container.getStack(Choreography.class).push(choreography);
        container.getStack(Entity.class).push(choreography);
    }
    
    public void outAChoreography(AChoreography node) {
        container.popFromStack(Choreography.class, Choreography.class);
        container.popFromStack(Entity.class, Choreography.class);
    }

    private void copyNSAndAddToStacks(TopEntity entity) {
        TopEntityAnalysis.addNamespaceAndVariant(entity, container.getStack(Namespace.class),
                container.getStack(AWsmlvariant.class));
        container.getStack(TopEntity.class).push(entity);
        container.getStack(Entity.class).push(entity);
    }
    
    public static void copyNFP(Entity oldE, Entity newE) {
        for (Iterator i = oldE.listNFPValues().entrySet().iterator(); i.hasNext();) {
            Entry entry = (Entry) i.next();
            Iterator j = ((Set) entry.getValue()).iterator();
            try {
                while (j.hasNext()) {
                    Object value = j.next();
                    if (value instanceof Identifier) {
                        newE.addNFPValue((IRI)entry.getKey(), (Identifier) value);
                    }
                    else if (value instanceof Value) {
                        newE.addNFPValue((IRI)entry.getKey(), (Value) value);
                    }
                }
            }
            catch (InvalidModelException ex) {
                throw new WrappedInvalidModelException(ex);
            }
        }
    }
}

/*
 * $Log: ServiceAnalysis.java,v $
 * Revision 1.11  2006/12/04 11:17:19  vassil_momtchev
 * wsmo4j parser always processe the choreography/orchestration; the extended parser will swap the implementation with the appropriate
 *
 * Revision 1.10  2006/10/25 07:05:49  vassil_momtchev
 * adapted to handle the node from the new grammar
 *
 * Revision 1.9  2006/06/21 07:46:29  vassil_momtchev
 * createVariable(String) method moved from WsmoFactory to LogicalExpressionFactory interface
 *
 * Revision 1.8  2006/04/24 08:04:58  holgerlausen
 * improved error handling in case of topentities without identifier
 * moved thomas unit test to "open" package, since it does not break expected behavior, but just document some derivations from the spec
 *
 * Revision 1.7  2006/04/11 16:06:58  holgerlausen
 * addressed RFE 1468651 ( http://sourceforge.net/tracker/index.php?func=detail&aid=1468651&group_id=113501&atid=665349)
 * currently the default behaviour of the parser is still as before
 *
 * Revision 1.6  2006/02/13 09:48:52  vassil_momtchev
 * the code to handle the topentities identifier validity refactored
 *
 * Revision 1.5  2006/02/10 14:37:25  vassil_momtchev
 * parser addapted to the grammar changes; unused class variables removed;
 *
 * Revision 1.4  2005/12/21 14:40:38  vassil_momtchev
 * choreography orchestration parsing added
 *
 * Revision 1.3  2005/12/07 09:12:10  vassil_momtchev
 * refactored the ServiceDescription check in inACapability method
 *
 * Revision 1.2  2005/12/07 09:03:54  vassil_momtchev
 * capabality was not assigned to the webservice
 *
 * Revision 1.1  2005/11/28 13:55:26  vassil_momtchev
 * AST analyses
 *
*/
