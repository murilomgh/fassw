/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2004-2006, Ontotext Lab. / SIRMA, DERI Innsbruck

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

import java.io.*;
import java.util.*;

import org.deri.wsmo4j.io.serializer.wsml.*;
import org.deri.wsmo4j.logicalexpression.*;
import org.omwg.logicalexpression.*;
import org.omwg.logicalexpression.terms.Visitor;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.mediator.*;
import org.wsmo.service.*;

import com.ontotext.wsmo4j.common.*;


/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */
public class WSMLTextExportHelper extends WSMLEnumerator {

    //padders to give some decent formatting to output
    String elementPadder = "     ";
    String subElementPadder = elementPadder + "     ";

    protected Writer writer;

    private TopEntity nsContainer = new TopEntityImpl(new Identifier() { 
    													public String toString(){return "";}
    													public void accept(Visitor v) { throw new UnsupportedOperationException(); }												
    												});
    
    //to serialize terms especially values
    protected VisitorSerializeWSMLTerms visitorSerializeTerm;

    public WSMLTextExportHelper(Writer writer) {
        this.writer = writer;
    }

    WSMLTextExportHelper outnl() {
        return out("\n");
    }
    WSMLTextExportHelper outnl(String str) {
        return out(str).outnl();
    }
    WSMLTextExportHelper out(String str) {
        try {
            writer.write(str);
        }
        catch (IOException ioe) {
            throw new RuntimeException("while writing", ioe);
        }
        return this;
    }


    String transformUri(Identifier id) {
        id.accept(visitorSerializeTerm);
        return visitorSerializeTerm.getSerializedObject().toString();
    }
    
    public void process(Entity[] items) {
        boolean hasNS = false;
        String lastWsmlVariant = null; 
        for (int i = 0; i < items.length; i++) {
            if (items[i] instanceof TopEntity) {
                TopEntity nsC = (TopEntity)items[i];
                if (nsC.getWsmlVariant()!=null){
                    if (lastWsmlVariant != null && !lastWsmlVariant.equals(nsC.getWsmlVariant())) {
                        throw new RuntimeException("Cannot serialize multiple TopEntity type " +
                                "with different wsml variants in one document!");
                    }
                    if (lastWsmlVariant == null) {
                        lastWsmlVariant = nsC.getWsmlVariant();
                        out("wsmlVariant " + Constants.IRI_DEL_START + 
                            nsC.getWsmlVariant() + Constants.IRI_DEL_END + "\n");
                    }
                }
                Iterator iter = nsC.listNamespaces().iterator();
                while (iter.hasNext()) {
                    Namespace ns = (Namespace)iter.next();
                    nsContainer.addNamespace(ns);
                    hasNS = true;
                }
                if (nsContainer.getDefaultNamespace() == null || nsContainer.getDefaultNamespace().getIRI().toString().length() == 0) {
                    nsContainer.setDefaultNamespace(nsC.getDefaultNamespace());
                }
            }
        } // enum namespaces
        visitorSerializeTerm = new VisitorSerializeWSMLTerms(nsContainer);
        
        boolean bHasDefNS = nsContainer.getDefaultNamespace() != null;
        if (hasNS || bHasDefNS) {
            out("namespace { ");
            boolean bComma = false;
            if (bHasDefNS) {
                out("_\"").out(nsContainer.getDefaultNamespace().getIRI().toString()).outnl("\"");
                bComma = true;
            }
            Iterator iter = nsContainer.listNamespaces().iterator();
            while (iter.hasNext()) {
                Namespace ns = (Namespace)iter.next();
//                nsContainer.addNamespace(ns.getPrefix(), ns.getURI());
                if (bComma)
                    out(", ").outnl();
                bComma = true;
                out(elementPadder 
                        + VisitorSerializeWSMLTerms.escapeNonSQNameSymbols(ns.getPrefix()))
                        .out(" _\"")
                        .out(ns.getIRI().toString())
                        .out("\"");
                hasNS = true;
            }
//            if (nsContainer.getTargetNamespace().length() > 0) {
//                outnl(elementPadder + "targetNamespace" + ": " + "<<" + nsContainer.getTargetNamespace() + ">>");
//            }
            outnl(" }");
        }
        visitorSerializeTerm = new VisitorSerializeWSMLTerms(nsContainer);

        for (int i = 0; i < items.length; i++) {
            if (items[i] instanceof TopEntity) {
                TopEntity nsC = (TopEntity)items[i];
                nsContainer.setDefaultNamespace(nsC.getDefaultNamespace());
//                nsContainer.setTargetNamespace(nsC.getTargetNamespace());
            }
            new WSMLItem(items[i]).apply(this);
        }
        outnl();
        
        try {
            writer.flush();
        }
        catch (IOException e) {
            throw new RuntimeException("Error while flushing the writer!", e);
        }
    }
// -------- NFPs ------------
    public void inEntity(Entity item) {
        outnl().out(elementPadder + "nonFunctionalProperties");
    }
    public void onNFPKeyValues(IRI key, Set vals) {
        if (vals.size() == 0) {
            return;
        }
        outnl().out(subElementPadder + transformUri(key)).out(" ");
        if (vals.size() == 1) {
            Object obj = vals.iterator().next();
            if (obj instanceof Identifier) {
                out("hasValue ").out(transformUri((Identifier)obj));
            }
            if (obj instanceof Instance) {
                out("hasValue ").out(transformUri(((Instance)obj).getIdentifier()));
            }
            else if (obj instanceof DataValue){
                ((DataValue)obj).accept(visitorSerializeTerm);
                String ss = visitorSerializeTerm.getSerializedObject();
                
                //String ss = ((DataValue)obj).asString();
//                if (ss.length() >= 4 && ss.startsWith("<<") && ss.endsWith(">>")) {
//                    out("hasValue ").outnl(ss);
//                }
//                else {
                    out("hasValue ").out(ss);
//                }
            }
//                out("hasValue ").out(obj.toString()).outnl();
        }
        else {
            out("hasValue {");
            Iterator iter = vals.iterator();
            boolean bComma = false;
            while (iter.hasNext()) {
                if (bComma) {
                    out(", ");
                }
                Object obj = iter.next();
                if (obj instanceof Identifier) {
                    out(transformUri((Identifier)obj));
                }
                if (obj instanceof Instance) {
                   out(transformUri(((Instance)obj).getIdentifier()));
                }
                else  if (obj instanceof DataValue) {
                    ((DataValue)obj).accept(visitorSerializeTerm);
                    String ss = visitorSerializeTerm.getSerializedObject();
//                    if (ss.length() >= 4 && ss.startsWith("<<") && ss.endsWith(">>")) {
                        out(ss);
//                    }
//                    else {
//                        out("\"").out(ss).out("\"");
//                    }
                }
                bComma = true;
            }
            out("}");
        }
    }
    public void outEntity(Entity item) {
        outnl().out(elementPadder + "endNonFunctionalProperties");
    }
// -------- log expression
//    public void inLogExpression(LogicalExpression item) {
//        if (item != null) {
//            outnl().out(elementPadder + "definedBy");
//        }
//        else {
//            int i = 0;
//        }
//    }
//    public void onLogExpression(LogicalExpression item) {
//        String expr = item.asString();
//        int pos = -1;
//        while (-1 != (pos = expr.indexOf(" . "))) {
//            expr = expr.substring(0, pos) + "." + expr.substring(pos + 3);
//        }
//        outnl().out(subElementPadder + expr);
//    }
// -------- imports ontology
/*    public void inOntologyContainer(OntologyContainer item) {
        outnl().outnl().out(elementPadder + "importedOntologies");
    } */
    public void inImportsOntologies(TopEntity _) {
        outnl().outnl().out(elementPadder + "importsOntology");
    }
    public void onImportedOntologies(Set list) {
        outnl();
        if (list.size() > 1) {
            out(subElementPadder + "{ ");
        }
        boolean bComma = false;
        boolean onlyOne = list.size() == 1;
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            String pad = "";
            if (bComma) {
                outnl(", ");
            }
            if (bComma || onlyOne){
                pad = subElementPadder + "  ";
            }
            Identifier entityID = ((Entity)iter.next()).getIdentifier();
            out(pad + "_\"" +  entityID.toString() + "\"");
            bComma = true;
        }
        if (list.size() > 1) {
            out("}");
        }
    }

    public void inUsesMediators(TopEntity item) {
        outnl().out(elementPadder + "usesMediator");
    }

    public void onUsedMediators(Set list) {
        outnl();
        if (list.size() > 1) {
            out(subElementPadder + "{ ");
        }
        boolean bComma = false;
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            String pad = subElementPadder;
            if (bComma) {
                outnl(", ");
                pad = subElementPadder + "  ";
            }
            Identifier id = ((Identifier)iter.next());
            out(pad + transformUri(id));
            bComma = true;
        }
        if (list.size() > 1) {
            out("}");
        }
    }

    public void inOntology(Ontology item) {
        outnl().out("ontology ").out(transformUri(item.getIdentifier()));
    }

    public void inAxiom(Axiom item) {
        outnl().outnl().out("axiom ").out(transformUri(item.getIdentifier()));
    }
    public void inLogExpressionDefinition(Axiom item) {
        outnl().out(elementPadder).out("definedBy ");
    }
    public void onLogExpression(LogicalExpression item) {
        LogExprSerializerWSML leSerializer = new LogExprSerializerWSML(nsContainer);
        outnl().out(subElementPadder).out(leSerializer.serialize(item));
    }

    public void inConcept(Concept item) {
        outnl().outnl().out("concept ").out(transformUri(item.getIdentifier()));
    }
    public void onSuperConcepts(Set list) {
        out(" subConceptOf ");
        boolean bComma = false;
        boolean bAddBrackets = list.size() > 1;
        if (bAddBrackets) {
            out("{ ");
        }
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            if (bComma) {
                out(", ");
            }
            Entity sc = (Entity)iter.next();
            out(transformUri(sc.getIdentifier()));
            bComma = true;
        }
        if (bAddBrackets) {
            out("}");
        }
    }
    public void inAttribute(Attribute item) {
        outnl().out(elementPadder + transformUri(item.getIdentifier()));
        if (item.isTransitive()) {
            out(" transitive ");
        }
        if (item.isReflexive()) {
            out(" reflexive ");
        }
        if (item.isSymmetric()) {
            out(" symmetric ");
        }
        Identifier invAttr = item.getInverseOf();
        if (invAttr != null) {
            out(" inverseOf(").out(transformUri(invAttr)).out(")");
        }
        if (item.isConstraining()) {
            out(" ofType ");
        } else {
            out(" impliesType ");
        }
        // cardinality only if not default
        if (!(item.getMinCardinality()==0 && item.getMaxCardinality()==Integer.MAX_VALUE)){
            out(" ("+item.getMinCardinality());
            if (item.getMaxCardinality() == Integer.MAX_VALUE) {
                out(" *) ");
            } else {
                out(" "+item.getMaxCardinality()+") ");
            }
        }
        Set list = item.listTypes();
        if (list.size() > 1) {
            out("{ ");
        }
        boolean bComma = false;
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Type c = (Type)iter.next();
            if (bComma)
                out(", ");
            bComma = true;
            if (c instanceof Concept) {
                out(transformUri(((Concept)c).getIdentifier()));
            }else {
                String iri = ((WsmlDataType)c).getIRI().toString();
                out (ConstantTransformer.getInstance().findNotation(iri));
            }
        }
        if (list.size() > 1) {
            out("}");
        }
    }


    public void inRelation(Relation item) {
        outnl().outnl().out("relation ").out(transformUri(item.getIdentifier()));
    }
    public void onSuperRelations(Set list) {
        out(" subRelationOf ");
        boolean bComma = false;
        if (list.size() > 1) {
            out("{ ");
        }
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            if (bComma) {
                out(", ");
            }
            Entity sc = (Entity)iter.next();
            out(transformUri(sc.getIdentifier()));
            bComma = true;
        }
        if (list.size() > 1) {
            out("}");
        }
    }
    public void onParameters(List list) {
        boolean bComma = false;
        Iterator iter = list.iterator();
        //dont print parameters if all don't have types (then it just has arity
        boolean arityOnly=true;
        while (iter.hasNext()){
            if (((Parameter)iter.next()).listTypes().size()!=0){
                arityOnly=false;
            }
        }
        if (arityOnly){
            out("/"+(list.size()));
            return;
        }
        
        out("(");
        // enum the relation parameters
        iter = list.iterator();
        while (iter.hasNext()) {
            if (bComma)
                out(", ");
            bComma = true;
            Parameter param = (Parameter)iter.next();
            // range semantics
            if (param.isConstraining()) {
                out(" ofType ");
            } else {
                out(" impliesType ");
            }

            // parameter range(s)
            Set types = param.listTypes();
            if (types.size() > 1)
                out("{");
            Iterator typesIter = types.iterator();
            boolean bTypesComma = false;
            while (typesIter.hasNext()) {
                if (bTypesComma)
                    out(", ");
                bTypesComma = true;
                Type theType = (Type)typesIter.next();
                if (! (theType instanceof WsmlDataType)){
                    out(transformUri(((Entity)theType).getIdentifier()));
                }else {
                    out(ConstantTransformer.getInstance().findNotation(
                            ((WsmlDataType)theType).getIRI().toString()));
                }
            }
            if (types.size() > 1)
                out("}");
        }
        out(")");
        /*
        outnl().out(elementPadder + transformUri(item.getIdentifier()));
        Concept range = item.getRange();
        if (range != null) {
            out(" ofType ").out(transformUri(range.getIdentifier()));
        }
        */
    }


    public void inInstance(Instance item) {
        outnl().outnl().out("instance ").out(transformUri(item.getIdentifier()));
    }
    public void onInstanceMemberOf(Set list) {
        if (0 == list.size())
            return;
        out(" memberOf ");

        if (list.size() > 1)
            out("{ ");
        boolean bComma = false;
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            if (bComma) {
                out(", ");
            }
            bComma = true;
            Concept c = (Concept)iter.next();
            out(transformUri(c.getIdentifier()));
        }
        if (list.size() > 1)
            out("} ");
    }
    public void onAttributeKeyValue(Identifier key, Set values) {
        if (values.size() == 0) {
            return;
        }
        outnl().out(elementPadder + transformUri(key));
        out(" hasValue ");
        if (values.size() > 1) {
            out("{");
        }
        Iterator iter = values.iterator();
        boolean bComma = false;
        while (iter.hasNext()) {
            if (bComma) {
                out(", ");
            }
            Object o = iter.next();
            if (o instanceof DataValue){
                ((DataValue)o).accept(visitorSerializeTerm);
                String ss = visitorSerializeTerm.getSerializedObject();
                out(ss);
            }else if (o instanceof Entity)
                out(transformUri(((Entity)o).getIdentifier()));
            bComma = true;
        }
        if (values.size() > 1) {
            out(" }");
        }
    }
    // relation instance
    public void inRelationInstance(RelationInstance item) {
        //do not print _# for anonymous relation instances (standard case)
        String id = "";
        if (! (item.getIdentifier() instanceof UnnumberedAnonymousID)){
            id = " "+transformUri(item.getIdentifier());
        }
        outnl().outnl().out("relationInstance").out(id);
    }
    public void onRelationInstanceMemberOf(Relation relation) {
        out(" ").out(transformUri(relation.getIdentifier()));
    }
    public void onParameterValues(List list) {
        out("(");
        boolean bComma = false;
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Object val = iter.next();
            if (bComma)
                out(", ");
            if (val instanceof DataValue) {
                ((DataValue)val).accept(visitorSerializeTerm);
                String ss = visitorSerializeTerm.getSerializedObject();
                out(ss);
                bComma = true;
            } else if (val instanceof Entity) {
                Identifier id = ((Entity)val).getIdentifier();
                out(transformUri(id));
                bComma = true;
            }
        }
        out(")");
    }
    // ----- webservice
    public void inWebService(WebService item) {
        outnl().outnl().out("webService ").out(transformUri(item.getIdentifier()));
    }
    public void inCapability(Capability item) {
        outnl().outnl().out("capability ").out(transformUri(item.getIdentifier()));
    }
    public void onSharedVariables(Set list) {
        outnl().outnl().out("sharedVariables ");
        if (list.size() > 1)
            out("{");
        boolean bComma = false;

        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            if (bComma)
                out(", ");
            bComma = true;
            Variable en = (Variable)iter.next();
            out("?");
            out(en.getName());
        }

        if (list.size() > 1)
            out("}");
    }
    public void onHasAssumptions(Entity item) {
        outnl().outnl().out("assumption ");
        if (item.getIdentifier() instanceof IRI)
        	out(transformUri(item.getIdentifier()));
    }
    public void onHasEffects(Entity item) {
        outnl().outnl().out("effect ");
        if (item.getIdentifier() instanceof IRI)
        	out(transformUri(item.getIdentifier()));
    }
    public void onHasPreconditions(Entity item) {
        outnl().outnl().out("precondition ");
        if (item.getIdentifier() instanceof IRI)
        	out(transformUri(item.getIdentifier()));
    }
    public void onHasPostconditions(Entity item) {
        outnl().outnl().out("postcondition ");
        if (item.getIdentifier() instanceof IRI)
        	out(transformUri(item.getIdentifier()));
    }

    public void inInterface(Interface item) {
        outnl().outnl().out("interface ").out(transformUri(item.getIdentifier()));
    }

    public void onMultipleInterfaces(Set list) {
        outnl().outnl().out("interface {");

        boolean bComma = false;

        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            if (bComma)
                out(", ");
            bComma = true;
            Entity en = (Entity)iter.next();
            out(transformUri(en.getIdentifier()));
        }

        outnl().out("}");
    }

    public void inOrchestration(Orchestration item) {
        outnl().out("orchestration ").out(transformUri(item.getIdentifier()));
    }
    public void inChoreography(Choreography item) {
        outnl().outnl().out("choreography ").out(transformUri(item.getIdentifier()));
    }
    // --- goals
    public void inGoal(Goal item) {
        outnl().outnl().out("goal ").out(transformUri(item.getIdentifier()));
    }
    // --- mediators
    public void onMediatorSources(Set list) {
        outnl().out(" source ");
        if (list.size() > 1)
            out("{ ");
        boolean bComma = false;
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            if (bComma)
                out(", ");
            bComma = true;
            out(transformUri((Identifier)iter.next()));
        }
        if (list.size() > 1)
            out(" }");
    }
    public void onMediatorTarget(Identifier item) {
        outnl().out(" target ");
        out(transformUri(item));
    }
    public void onMediatorUseService(Identifier item) {
        outnl().out(" usesService ");
        out(transformUri(item));
    }

    public void inOOMediator(OOMediator item) {
        outnl().outnl().out("ooMediator ").out(transformUri(item.getIdentifier()));
    }

//    public void outOOMediator(OOMediator item) {
//        Set list = item.listSourceComponents();
//        Iterator iter = list.iterator();
//        while (iter.hasNext()) {
//            outnl().out(elementPadder + "source ").out("<<" + ((MediatableReference) iter.next()).getIdentifier() + ">>");
//        }
//        if (item.getTargetComponent() != null) {
//            outnl().out(elementPadder + "target ").out("<<" + ((MediatableReference) item.getTargetComponent()).getIdentifier() + ">>");
//        }
//        if (item.getMediationServiceID() != null) {
//            outnl().out(elementPadder + "useService ").out(item.getMediationServiceID().toString());
//        }
//    }

    public void inWGMediator(WGMediator item) {
        outnl().outnl().out("wgMediator ").out(transformUri(item.getIdentifier()));
    }
//    public void outWGMediator(WGMediator item) {
//        Set list = item.listSourceComponents();
//        Iterator iter = list.iterator();
//        while (iter.hasNext()) {
//            outnl().out(elementPadder + "source ").out("<<" + ((MediatableReference) iter.next()).getIdentifier() + ">>");
//        }
//        if (item.getTargetComponent() != null) {
//            outnl().out(elementPadder + "target ").out("<<" + ((MediatableReference) item.getTargetComponent()).getIdentifier() + ">>");
//        }
//        if (item.getMediationServiceID() != null) {
//            outnl().out(elementPadder + "useService ").out(item.getMediationServiceID().toString());
//        }
//    }
    public void inWWMediator(WWMediator item) {
        outnl().outnl().out("wwMediator ").out(transformUri(item.getIdentifier()));
    }
//    public void outWWMediator(WWMediator item) {
//        Set list = item.listSourceComponents();
//        Iterator iter = list.iterator();
//        while (iter.hasNext()) {
//            outnl().out(elementPadder + "source ").out("<<" + ((MediatableReference) iter.next()).getIdentifier() + ">>");
//        }
//        if (item.getTargetComponent() != null) {
//            outnl().out(elementPadder + "target ").out("<<" + ((MediatableReference) item.getTargetComponent()).getIdentifier() + ">>");
//        }
//        if (item.getMediationServiceID() != null) {
//            outnl().out(elementPadder + "useService ").out(item.getMediationServiceID().toString());
//        }
//    }

    public void inGGMediator(GGMediator item) {
        outnl().outnl().out("ggMediator ").out(transformUri(item.getIdentifier()));
    }
//    public void outGGMediator(GGMediator item) {
//        Set list = item.listSourceComponents();
//        Iterator iter = list.iterator();
//        while (iter.hasNext()) {
//            outnl().out(elementPadder + "source ").out("<<" + ((MediatableReference) iter.next()).getIdentifier() + ">>");
//        }
//        if (item.getTargetComponent() != null) {
//            outnl().out(elementPadder + "target ").out("<<" + ((MediatableReference) item.getTargetComponent()).getIdentifier() + ">>");
//        }
//        if (item.getMediationServiceID() != null) {
//            outnl().out(elementPadder + "useService ").out(item.getMediationServiceID().toString());
//        }
//    }
}

/*
 * $Log: WSMLTextExportHelper.java,v $
 * Revision 1.14  2007/06/03 13:07:29  alex_simov
 * bugfix: nfp values of type Instance were not serialized properly,
 * causing invalid wsml
 *
 * Revision 1.13  2007/04/02 12:13:27  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.12  2006/06/28 11:14:06  alex_simov
 * bugfix: instance references set as values in NFPs were not serialized
 *
 * Revision 1.11  2006/05/04 14:17:09  alex_simov
 * bugfix: invalid sQName parsing/serializing - illegal symbols not escaped
 *
 * Revision 1.10  2006/04/27 10:33:41  holgerlausen
 * improved readability of serialzation of relation instances
 *
 * Revision 1.9  2006/04/13 14:40:24  alex_simov
 * bugfix: mediator's sources were still handled like Entities
 *
 * Revision 1.8  2006/04/05 13:26:32  vassil_momtchev
 * usesMediator now refer mediators by  IRI instead of handle to object
 *
 * Revision 1.7  2006/02/16 09:56:50  vassil_momtchev
 * setInverseOf(Attribute) changed to setInverseOf(Identifier)
 *
 * Revision 1.6  2006/02/10 14:39:49  vassil_momtchev
 * serializer addapted to the new api changes
 *
 * Revision 1.5  2006/02/01 15:23:54  vassil_momtchev
 * duplicated usage of two VisitorSerializeWSMLTerms, only one used now; write field changed from private to protected
 *
 * Revision 1.4  2006/01/23 11:01:56  vassil_momtchev
 * wsml line was added N times for each topentity with specified wsml variant; topentities with different wsml variants are not allowed in one document
 *
 * Revision 1.3  2005/12/13 11:13:37  holgerlausen
 * fixed serialization of IRIs that contain keywords
 *
 * Revision 1.2  2005/11/28 14:51:37  vassil_momtchev
 * moved from com.ontotext.wsmo4j.parser
 *
 * Revision 1.1.2.1  2005/11/28 13:59:35  vassil_momtchev
 * package refactored from com.ontotext.wsmo4j.parser to com.ontotext.wsmo4j.serializer.wsml
 *
 * Revision 1.17  2005/10/06 17:57:34  holgerlausen
 * fixed anon id bug in serializing
 *
 * Revision 1.16  2005/09/23 07:09:51  holgerlausen
 * moved constanttransformer from API to implementation, removed dublicated constants in logicalexpression.constants
 *
 * Revision 1.15  2005/09/20 19:41:01  holgerlausen
 * removed superflouis interfaces for IO in logical expression (since intgeration not necessary)
 *
 * Revision 1.14  2005/09/16 14:02:44  alex_simov
 * Identifier.asString() removed, use Object.toString() instead
 * (Implementations MUST override toString())
 *
 * Revision 1.13  2005/09/15 11:57:09  holgerlausen
 * call le Serializer explicit with nscontainer to resolve existing namespaces
 *
 * Revision 1.12  2005/09/14 09:43:43  alex_simov
 * bugfix: relation paramenters not serialized correctly
 *
 * Revision 1.11  2005/09/13 10:18:42  holgerlausen
 * fixed bug 1237280
 * parse/serialize of relation with arity only
 *
 * Revision 1.10  2005/09/09 10:00:15  holgerlausen
 * fixed serializing problem eported by jan (for data types in relations)
 *
 * Revision 1.9  2005/09/08 16:28:30  holgerlausen
 * bug1252521 support for parsing / serializing wsmlVariant
 *
 * Revision 1.8  2005/09/08 15:16:27  holgerlausen
 * fixes for parsing and serializing datatypes
 *
 * Revision 1.7  2005/09/02 13:32:45  ohamano
 * move logicalexpression packages from ext to core
 * move tests from logicalexpression.test to test module
 *
 * Revision 1.6  2005/09/02 09:43:32  ohamano
 * integrate wsmo-api and le-api on api and reference implementation level; full parser, serializer and validator integration will be next step
 *
 * Revision 1.5  2005/08/31 08:36:14  alex_simov
 * bugfix: Variable.getName() now returns the name without leading '?'
 *
 * Revision 1.4  2005/08/10 10:43:08  vassil_momtchev
 * always flush the output writer
 *
 * Revision 1.3  2005/07/08 12:45:59  alex_simov
 * fix
 *
 * Revision 1.2  2005/07/05 12:45:51  alex_simov
 * attributes refactored
 *
 * Revision 1.1  2005/06/27 08:32:00  alex_simov
 * refactoring: *.io.parser -> *.parser
 *
 * Revision 1.22  2005/06/23 08:41:10  damyan_rm
 * constuctor made public to ease showing partial entities in textual form
 *
 * Revision 1.21  2005/06/23 08:18:17  damyan_rm
 * fix: serialization of multiple interfaces was using wrong kind of brackets
 *
 * Revision 1.20  2005/06/22 15:53:36  alex_simov
 * capability parse bug fix
 *
 * Revision 1.19  2005/06/22 14:49:27  alex_simov
 * Copyright header added/updated
 *
 * Revision 1.18  2005/06/22 14:40:27  ohamano
 * elementPadder for definedBy is enough. subElementPadder introduces too many empty spaces
 *
 * Revision 1.17  2005/06/02 14:41:57  alex_simov
 * bug fix
 *
 * Revision 1.16  2005/06/02 14:19:19  alex_simov
 * v0.4.0
 *
 * Revision 1.2  2005/06/02 12:33:09  damian
 * fixes
 *
 * Revision 1.1  2005/05/26 09:36:03  damian
 * io package
 *
 * Revision 1.15  2005/04/27 07:49:49  damyan_rm
 * handling of the default datatypes (string, integer, decimal**) for literals
 *
 * Revision 1.14  2005/04/12 13:20:24  damyan_rm
 * bug [1180672] - parser print only the first instacne attribute value - removed attribute valency check within onAttributeKeyValue()
 *
 * Revision 1.13  2005/03/02 10:56:41  morcen
 * fixed bugs 1154273, 1154263, 1154190 with the serializers
 *
 * Revision 1.12  2005/02/24 12:57:29  morcen
 * accidentally committed a System.out statement. removed it
 *
 * Revision 1.11  2005/02/24 12:49:26  morcen
 * fixes to indentation missed the range of a function.
 *
 * Revision 1.10  2005/02/20 18:01:25  morcen
 * fixed bug 1144912 added indentation and consistent new lines to serializer
 *
 * Revision 1.9  2005/01/12 15:20:18  alex_simov
 * checkstyle formatting
 *
 */
