/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2006, University of Innsbruck, Austria

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
package org.deri.wsmo4j.common;

import java.util.*;

import org.omwg.logicalexpression.*;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.mediator.*;
import org.wsmo.service.*;

/**
 * This utility class cleans a given TopEntity from all its
 * previous definitions (if any).
 *
 * <pre>
 * Created on 10.04.2006
 * Committed by $Author$
 * $Source$,
 * </pre>
 *
 * @author Holger Lausen (holger.lausen@deri.org)
 *
 * @version $Revision$ $Date$
 */
public class ClearTopEntity {
    
    
    public static void clearTopEntity(Mediator m) throws SynchronisationException, InvalidModelException{
        if (m == null){
            return;
        }
        clearCommonElements(m);
        if (m.listSources()!=null){
            for (Iterator <IRI> mediatorI = new ArrayList <IRI>(m.listSources()).iterator(); mediatorI.hasNext();){
                m.removeSource(mediatorI.next());
            }
        }
        m.setTarget(null);
        m.setMediationService(null);
    }
    
    public static void clearTopEntity(ServiceDescription s) throws SynchronisationException, InvalidModelException{
        if (s == null){
            return;
        }
        clearCommonElements(s);
        if (s.listInterfaces() != null){
            for (Iterator <Interface> interfaceI = new ArrayList <Interface> (s.listInterfaces()).iterator(); interfaceI.hasNext();){
                Interface i = interfaceI.next();
                clearNfp(i.getChoreography());
                clearNfp(i.getOrchestration());
                i.setChoreography(null);
                i.setOrchestration(null);
                
                s.removeInterface(i);
            }
        }
        Capability cap = s.getCapability();
        if (cap==null){
            return;
        }
        clearCommonElements(cap);
        //shared Variables
        if (cap.listSharedVariables()!=null){
            for (Iterator <Variable> varI = new ArrayList <Variable>(cap.listSharedVariables()).iterator();varI.hasNext();){
                cap.removeSharedVariable(varI.next());
            }
        }
        //assumption
        if (cap.listAssumptions()!=null){
            for (Iterator <Axiom> i = new ArrayList <Axiom>(cap.listAssumptions()).iterator();i.hasNext();){
                Axiom a = i.next();
                clearNfp(a);
                cap.removeAssumption(a);
            }
        }
        if (cap.listEffects()!=null){
            for (Iterator <Axiom> i = new ArrayList <Axiom>(cap.listEffects()).iterator();i.hasNext();){
                Axiom a = i.next();
                clearNfp(a);
                cap.removeEffect(a);
            }
        }
        if (cap.listPostConditions()!=null){
            for (Iterator <Axiom> i = new ArrayList <Axiom>(cap.listPostConditions()).iterator();i.hasNext();){
                Axiom a = i.next();
                clearNfp(a);
                cap.removePostCondition(a);
            }
        }
        if (cap.listPreConditions()!=null){
            for (Iterator <Axiom> i = new ArrayList <Axiom>(cap.listPreConditions()).iterator();i.hasNext();){
                Axiom a = i.next();
                clearNfp(a);
                cap.removePreCondition(a);
            }
        }
        s.setCapability(null);
    }
    
    public static void clearTopEntity(Ontology ont) throws SynchronisationException, InvalidModelException{
        if (ont == null){
            return;
        }
        //clear concepts
        if (ont.listConcepts()!=null){
            for (Iterator <Concept> conceptI = new ArrayList <Concept>(ont.listConcepts()).iterator(); conceptI.hasNext(); ){
                Concept concept = conceptI.next();
                if (concept.listSuperConcepts()!=null){
                    for (Iterator <Concept> superI = new ArrayList <Concept>(concept.listSuperConcepts()).iterator(); superI.hasNext(); ){
                        concept.removeSuperConcept(superI.next());
                    }
                }
                if (concept.listAttributes()!=null){
                    for (Iterator <Attribute> attrI = new ArrayList <Attribute>(concept.listAttributes()).iterator(); attrI.hasNext(); ){
                        Attribute attr = attrI.next();
                        attr.setConstraining(false);
                        attr.setInverseOf(null);
                        attr.setReflexive(false);
                        attr.setSymmetric(false);
                        attr.setReflexive(false);
                        attr.setTransitive(false);
                        attr.setMaxCardinality(Integer.MAX_VALUE);
                        attr.setMinCardinality(0);
                        for (Iterator <Type> typesI = new ArrayList <Type>(attr.listTypes()).iterator(); typesI.hasNext();){
                           attr.removeType(typesI.next());
                        }
                        concept.removeAttribute(attr);
                        clearNfp(attr);
                    }
                }
                clearNfp(concept);
                concept.setOntology(null);
            }
        }
        //clear instances
        if (ont.listInstances()!=null){
            for (Iterator <Instance> instanceI = new ArrayList <Instance>(ont.listInstances()).iterator(); instanceI.hasNext();){
                Instance instance = instanceI.next();
                if (instance.listConcepts()!=null){
                    for (Iterator <Concept> memberOfI = new ArrayList <Concept>(instance.listConcepts()).iterator(); memberOfI.hasNext();){
                        instance.removeConcept(memberOfI.next());
                    }
                }
                if (instance.listAttributeValues()!=null){
                    for (Iterator <Identifier> attributeI = new ArrayList <Identifier> (instance.listAttributeValues().keySet()).iterator(); attributeI.hasNext();){
                        instance.removeAttributeValues(attributeI.next());
                    }
                }
                clearNfp(instance);
                instance.setOntology(null);
            }
        }
        
        //clear RelationInstances
        if (ont.listRelationInstances()!=null){
            for (Iterator <RelationInstance> instanceI = new ArrayList <RelationInstance> (ont.listRelationInstances()).iterator();instanceI.hasNext();){
                RelationInstance instance = instanceI.next();
                if (instance.listParameterValues()!=null){
                    for (byte i = (byte)(instance.listParameterValues().size()-1); i>=0; i--){
                        instance.setParameterValue(i,null);
                    }
                }
                clearNfp(instance);
                instance.setOntology(null);
            }
        }

        //clear Relations
        if (ont.listRelations()!=null){
            for (Iterator <Relation> relI = new ArrayList <Relation> (ont.listRelations()).iterator();relI.hasNext();){
                Relation rel = relI.next();
                if (rel.listParameters()!=null){
                    for (byte i = (byte)(rel.listParameters().size()-1); i>=0; i--){
                        rel.removeParameter(i);
                    }
                }
                if (rel.listSuperRelations()!=null){
                    for (Iterator <Relation> superI = new ArrayList <Relation> (rel.listSuperRelations()).iterator();superI.hasNext();){
                        rel.removeSuperRelation(superI.next());
                    }
                }
                if (rel.listRelationInstances()!= null) {
                	for (Iterator <RelationInstance> relInstI = new ArrayList <RelationInstance> (rel.listRelationInstances()).iterator();relInstI.hasNext();) {
                		rel.removeRelationInstance(relInstI.next());
                	}
                }
                clearNfp(rel);
                rel.setOntology(null);
            }
        }
        
        //clearAxioms
        if(ont.listAxioms()!=null){
            for (Iterator <Axiom>  axiomI = new ArrayList <Axiom> (ont.listAxioms()).iterator();axiomI.hasNext();){
                Axiom a = axiomI.next();
                for (Iterator <LogicalExpression> defI = new ArrayList <LogicalExpression> (a.listDefinitions()).iterator(); defI.hasNext();){
                    a.removeDefinition(defI.next());
                }
                clearNfp(a);
                a.setOntology(null);
            }
        }

        
        clearCommonElements(ont);
    }

    private static void clearNfp(Entity e) throws SynchronisationException, InvalidModelException{
        if (e!=null && e.listNFPValues()!=null){
            for (Iterator <IRI> nfpI = new ArrayList <IRI> (e.listNFPValues().keySet()).iterator(); nfpI.hasNext(); ){
                e.removeNFP(nfpI.next());
            }
        }
            
    }
    
    private static void clearCommonElements(TopEntity te) throws SynchronisationException, InvalidModelException{
        //clear importsMediator
        if (te==null){
            return;
        }
        if (te.listMediators()!=null){
            for (Iterator <IRI>  medI = new ArrayList <IRI> (te.listMediators()).iterator(); medI.hasNext();){
                te.removeMediator(medI.next());
            }
        }
        //clear namespace
        if (te.listNamespaces()!=null){
            for (Iterator <Namespace> nsI = new ArrayList <Namespace>(te.listNamespaces()).iterator(); nsI.hasNext();){
                te.removeNamespace(nsI.next());
            }
        }
        //clear importsOntology
        if(te.listOntologies()!=null){
            for (Iterator <Ontology> ontI = new ArrayList <Ontology> (te.listOntologies()).iterator(); ontI.hasNext();){
                te.removeOntology(ontI.next());
            }
        }
        te.setWsmlVariant(null);
        te.setDefaultNamespace((IRI)null);
        clearNfp(te);
    }


}


/*
 *$Log$
 *Revision 1.7  2007/04/02 12:13:28  morcen
 *Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 *Revision 1.6  2007/02/13 13:01:56  alex_simov
 *bugfix: cleaning webservices/goals did not remove the interfaces
 *
 *Revision 1.5  2006/12/04 09:01:59  alex_simov
 *bugfix: ConcurrentModificationException was caused in several FOR loops
 * using iterators
 *
 *Revision 1.4  2006/08/21 14:46:22  nathaliest
 *fixed invalid model exception
 *
 *Revision 1.3  2006/08/10 07:36:51  nathaliest
 *completed clearing of ontology objects
 *
 *Revision 1.2  2006/04/27 10:34:10  holgerlausen
 *fixed potential null pointer exceptions
 *
 *Revision 1.1  2006/04/11 16:06:59  holgerlausen
 *addressed RFE 1468651 ( http://sourceforge.net/tracker/index.php?func=detail&aid=1468651&group_id=113501&atid=665349)
 *currently the default behaviour of the parser is still as before
 *
 */