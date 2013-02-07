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

package com.ontotext.wsmo4j.serializer.wsml;

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */
import java.util.*;

import org.omwg.logicalexpression.*;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.mediator.*;
import org.wsmo.service.*;

public class WSMLEnumerator implements Visitor {
    
    private final static boolean USE_MULTIPLE_INTERFACE_LIST = false;

    public void visit(IRI uri) {
        throw new UnsupportedOperationException("this should not happen");
    }

    public void visit(Entity item) {
        Map map = item.listNFPValues();
        if (map.size() > 0) {
            inEntity(item);

            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry en = (Map.Entry)iter.next();
                IRI key = (IRI)en.getKey();
                Set vals = (Set)en.getValue();
                onNFPKeyValues(key, vals);
            }

            outEntity(item);
        }
    }
    public void inEntity(Entity item) {    }
    public void onNFPKeyValues(IRI key, Set vals) {    }
    public void outEntity(Entity item) {    }


    public void visit(TopEntity item) {
        inTopEntity(item);
        visit((Entity)item);
        Set list = item.listOntologies();
        if (list.size() > 0) {
            inImportsOntologies(item);
            onImportedOntologies(list);
            outImportsOntologies(item);
        }
        list = item.listMediators();
        if (list.size() > 0) {
            inUsesMediators(item);
            onUsedMediators(list);
            outUsesMediators(item);
        }

        outTopEntity(item);
    }
    
    public void inTopEntity(TopEntity item) {   
        if (item.getIdentifier() instanceof IRI == false) {
            throw new RuntimeException("Cannot serialize " +
                    item.getClass() + " identified not by IRI!");
        }
    }
    public void outTopEntity(TopEntity item) {   }

    public void inImportsOntologies(TopEntity item) {    }
    public void outImportsOntologies(TopEntity item) {    }
    public void onImportedOntologies(Set list) {    }

    public void inUsesMediators(TopEntity item) {    }
    public void outUsesMediators(TopEntity item) {    }
    public void onUsedMediators(Set list) {    }

    public void visit(Ontology item) {
        inOntology(item);
        visit((TopEntity)item);
        Iterator items = null;
        // dump axioms
        items = item.listAxioms().iterator();
        while (items.hasNext()) {
            new WSMLItem((Entity)items.next()).apply(this);
        }
        // dump concepts
        items = item.listConcepts().iterator();
        while (items.hasNext()) {
            new WSMLItem((Entity)items.next()).apply(this);
        }
        // dump relations
        items = item.listRelations().iterator();
        while (items.hasNext()) {
            Entity relItem = (Entity)items.next();
            new WSMLItem(relItem).apply(this);
        }
        // dump instances
        items = item.listInstances().iterator();
        while (items.hasNext()) {
            new WSMLItem((Entity)items.next()).apply(this);
        }
        // dump relationInstances
        items = item.listRelationInstances().iterator();
        while (items.hasNext()) {
            new WSMLItem((Entity)items.next()).apply(this);
        }

        outOntology(item);
    }

    public void visit(Axiom item) {
        inAxiom(item);
        onAxiomDefinition(item);
        outAxiom(item);
    }

    public void onAxiomDefinition(Axiom item) {
        visit((Entity)item);
        Set list = item.listDefinitions();
        if (list.size() > 0 ) {
            inLogExpressionDefinition(item);
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                visit((LogicalExpression)iter.next());
            }
            outLogExpressionDefinition(item);
        }
    }
    public void visit(LogicalExpression item) {
        onLogExpression(item);
    }
    public void onLogExpression(LogicalExpression item) {   }
    public void inLogExpressionDefinition(Axiom item) {    }
    public void outLogExpressionDefinition(Axiom item) {    }

    public void visit(Concept item) {
        inConcept(item);
        Set list = item.listSuperConcepts();
        if (list.size() > 0) {
            onSuperConcepts(list);
        }
        visit((Entity)item);
        list = item.listAttributes();
        if (list.size() > 0) {
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                new WSMLItem((Entity)iter.next()).apply(this);
            }
        }
        outConcept(item);
    }
    public void onSuperConcepts(Set superconcepts) {    }
    public void visit(Attribute item) {
        inAttribute(item);
        visit((Entity)item);
        outAttribute(item);
    }
    public void inAttribute(Attribute item) { }
    public void outAttribute(Attribute item) { }
    
    public void visit(Relation item) {
        inRelation(item);
        List aList = item.listParameters();
        if (aList.size() > 0) {
            onParameters(aList);
        }
        Set list = item.listSuperRelations();
        if (list.size() > 0) {
            onSuperRelations(list);
        }
        visit((Entity)item);
        outRelation(item);
    }
    public void onSuperRelations(Set list) {    }

    public void onParameters(List list) { }

    public void visit(Instance item) {
        inInstance(item);
        Set list = item.listConcepts();
        onInstanceMemberOf(list);

        visit((Entity)item);
        Map map = item.listAttributeValues();
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry e = (Map.Entry)iter.next();
            Identifier key = (Identifier)e.getKey();
            Set val = (Set)e.getValue();
            onAttributeKeyValue(key, val);
        }
        outInstance(item);
    }
    public void onInstanceMemberOf(Set list) { }
    public void onAttributeKeyValue(Identifier key, Set values) { }

    public void visit(RelationInstance item) {
        inRelationInstance(item);
        onRelationInstanceMemberOf(item.getRelation());
        List values = item.listParameterValues();
        onParameterValues(values);
        visit((Entity)item);
        outRelationInstance(item);
    }
    public void onRelationInstanceMemberOf(Relation concept) { }
    public void onParameterValues(List values) { }

    public void visit(ServiceDescription item) {
        visit((TopEntity)item);
        Capability capability = item.getCapability();
        if (capability != null)
            new WSMLItem(capability).apply(this);
        Set list = item.listInterfaces();
        if (list.size()> 0) {
            if (!USE_MULTIPLE_INTERFACE_LIST || list.size() == 1) {
                for (Iterator i = list.iterator(); i.hasNext();) {
                    new WSMLItem((Entity) i.next()).apply(this);
                }
            }
            else {
                onMultipleInterfaces(list);
            }
        }
    }
    public void onMultipleInterfaces(Set list) {
    }

    public void onSharedVariables(Set list) {
    }

    public void visit(Capability item) {
        inCapability(item);
        visit((TopEntity)item);
        Set list = null;
        list = item.listSharedVariables();
        if (list.size() > 0) {
            onSharedVariables(list);
        }

        list = item.listAssumptions();
        if (list.size() > 0) {
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                Axiom ax = (Axiom)iter.next();
                onHasAssumptions(ax);
                onAxiomDefinition(ax);
            }
        }
        list = item.listEffects();
        if (list.size() > 0) {
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                Axiom ax = (Axiom)iter.next();
                onHasEffects(ax);
                onAxiomDefinition(ax);
            }
        }
        list = item.listPreConditions();
        if (list.size() > 0) {
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                Axiom ax = (Axiom)iter.next();
                onHasPreconditions(ax);
                onAxiomDefinition(ax);
            }
        }
        list = item.listPostConditions();
        if (list.size() > 0) {
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                Axiom ax = (Axiom)iter.next();
                onHasPostconditions(ax);
                onAxiomDefinition(ax);
            }
        }
        outCapability(item);
    }
    public void onHasAssumptions(Entity item) { }
    public void onHasEffects(Entity item) { }
    public void onHasPreconditions(Entity item) { }
    public void onHasPostconditions(Entity item) { }

    public void visit(Interface item) {
        inInterface(item);
        visit((TopEntity)item);
        if (item.getChoreography() != null)
            new WSMLItem(item.getChoreography()).apply(this);
        if (item.getOrchestration() != null)
            new WSMLItem(item.getOrchestration()).apply(this);
        outInterface(item);
    }
    public void visit(Orchestration item) {
        inOrchestration(item);
        visit((Entity)item);
        outOrchestration(item);
    }
    public void visit(Choreography item) {
        inChoreography(item);
        visit((Entity)item);
        outChoreography(item);
    }

    public void visit(WebService item) {
        inWebService(item);
        visit((ServiceDescription)item);
        outWebService(item);
    }

    public void visit(Goal item) {
        inGoal(item);
        visit((ServiceDescription)item);
        outGoal(item);
    }

    public void visit(Mediator item) {
        visit((TopEntity)item);
        Set list = item.listSources();
        if (list.size() > 0) {
            onMediatorSources(list);
        }
        if (item.getTarget() != null)
            onMediatorTarget((Identifier)item.getTarget());
        if (item.getMediationService() != null)
            onMediatorUseService(item.getMediationService());
    }
    public void visit(OOMediator item) {
        inOOMediator(item);
        visit((Mediator)item);
        outOOMediator(item);
    }
    public void visit(WGMediator item) {
        inWGMediator(item);
        visit((Mediator)item);
        outWGMediator(item);
    }
    public void visit(WWMediator item) {
        inWWMediator(item);
        visit((Mediator)item);
        outWWMediator(item);
    }
    public void visit(GGMediator item) {
        inGGMediator(item);
        visit((Mediator)item);
        outGGMediator(item);
    }
// ------------------ complete ---
    public void inOntology(Ontology item) {
    }
    public void inAxiom(Axiom item) {
    }
    public void inConcept(Concept item) {
    }
    public void inRelation(Relation item) {
    }
    public void inInstance(Instance item) {
    }
    public void inRelationInstance(RelationInstance item) {
    }

    public void inWebService(WebService item) {
    }
    public void inCapability(Capability item) {
    }
    public void inInterface(Interface item) {
    }
    public void inOrchestration(Orchestration item) {
    }
    public void inChoreography(Choreography item) {
    }

    public void inGoal(Goal item) {
    }

    public void inOOMediator(OOMediator item) {
    }
    public void inWGMediator(WGMediator item) {
    }
    public void inWWMediator(WWMediator item) {
    }
    public void inGGMediator(GGMediator item) {
    }

    public void onMediatorSources(Set item) {
    }
    public void onMediatorTarget(Identifier item) {
    }
    public void onMediatorUseService(Identifier item) {
    }

    public void outOntology(Ontology item) {
    }
    public void outAxiom(Axiom item) {
    }
    public void outConcept(Concept item) {
    }
    public void outRelation(Relation item) {
    }
    public void outInstance(Instance item) {
    }
    public void outRelationInstance(RelationInstance item) {
    }

    public void outWebService(WebService item) {
    }
    public void outCapability(Capability item) {
    }
    public void outInterface(Interface item) {
    }
    public void outOrchestration(Orchestration item) {
    }
    public void outChoreography(Choreography item) {
    }

    public void outGoal(Goal item) {
    }

    public void outOOMediator(OOMediator item) {
    }
    public void outWGMediator(WGMediator item) {
    }
    public void outWWMediator(WWMediator item) {
    }
    public void outGGMediator(GGMediator item) {
    }

}

/*
 * $Log: WSMLEnumerator.java,v $
 * Revision 1.6  2006/03/29 08:51:57  vassil_momtchev
 * mediator reference source/target by IRI (changed from TopEntity); mediator reference mediationService by IRI (changed from Identifier)
 *
 * Revision 1.5  2006/03/06 15:22:40  vassil_momtchev
 * when multiple interfaces used in a webservice, serialize the full interface data instead of minterfaces = 'interface' '{' id moreids* '}' reference construction
 *
 * Revision 1.4  2006/02/13 10:42:36  vassil_momtchev
 * throw exception if topentity identified by anonId (created by another impl) serialized
 *
 * Revision 1.3  2006/02/10 14:39:49  vassil_momtchev
 * serializer addapted to the new api changes
 *
 * Revision 1.2  2005/11/28 14:51:37  vassil_momtchev
 * moved from com.ontotext.wsmo4j.parser
 *
 * Revision 1.1.2.1  2005/11/28 13:59:35  vassil_momtchev
 * package refactored from com.ontotext.wsmo4j.parser to com.ontotext.wsmo4j.serializer.wsml
 *
 * Revision 1.3.2.1  2005/11/21 17:38:36  holgerlausen
 * added more logexp support
 *
 * Revision 1.3  2005/09/02 13:32:45  ohamano
 * move logicalexpression packages from ext to core
 * move tests from logicalexpression.test to test module
 *
 * Revision 1.2  2005/07/05 12:45:51  alex_simov
 * attributes refactored
 *
 * Revision 1.1  2005/06/27 08:32:00  alex_simov
 * refactoring: *.io.parser -> *.parser
 *
 * Revision 1.12  2005/06/22 15:53:36  alex_simov
 * capability parse bug fix
 *
 * Revision 1.11  2005/06/22 14:49:27  alex_simov
 * Copyright header added/updated
 *
 * Revision 1.10  2005/06/02 14:19:19  alex_simov
 * v0.4.0
 *
 * Revision 1.2  2005/06/02 12:33:09  damian
 * fixes
 *
 * Revision 1.1  2005/05/26 09:36:03  damian
 * io package
 *
 * Revision 1.9  2005/03/02 10:56:41  morcen
 * fixed bugs 1154273, 1154263, 1154190 with the serializers
 *
 * Revision 1.8  2005/01/25 14:03:55  alex_simov
 * Refactoring:
 * 1. Instance: getMemberOf()/setMemberOf() replaced by getConcept()/setConcept()
 * 2. RelationInstance: getInstanceOf()/setInstanceOf() replaced by getRelation()/setRelation()
 *
 * Revision 1.7  2005/01/12 15:20:18  alex_simov
 * checkstyle formatting
 *
 */
