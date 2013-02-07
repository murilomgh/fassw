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

import org.deri.wsmo4j.common.*;
import org.deri.wsmo4j.io.parser.*;
import org.deri.wsmo4j.logicalexpression.*;
import org.omwg.logicalexpression.terms.*;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.factory.*;
import org.wsmo.wsml.*;
import org.wsmo.wsml.compiler.node.*;

import com.ontotext.wsmo4j.parser.*;

public class OntologyAnalysis extends ASTAnalysis {

    private byte paramCount=0;
    
    private WsmoFactory factory;

    private DataFactory dFactory;

    private ASTAnalysisContainer container;
    
    private boolean cleanOnParse=false;

    public OntologyAnalysis(ASTAnalysisContainer container, WsmoFactory factory,
            DataFactory dFactory) {
        if (factory == null || container == null) {
            throw new IllegalArgumentException();
        }
        this.factory = factory;
        this.dFactory = dFactory;
        this.container = container;

        // register the handled nodes
        container.registerNodeHandler(AOntology.class, this);
        container.registerNodeHandler(AConcept.class, this);
        container.registerNodeHandler(ASuperconcept.class, this);
        container.registerNodeHandler(AAttribute.class, this);
        container.registerNodeHandler(ARelation.class, this);
        container.registerNodeHandler(ASuperrelation.class, this);
        container.registerNodeHandler(AParamtype.class, this);
        container.registerNodeHandler(AInstance.class, this);
        container.registerNodeHandler(AMemberof.class, this);
        container.registerNodeHandler(AAttributevalue.class, this);
        container.registerNodeHandler(ARelationinstance.class, this);
        container.registerNodeHandler(AAxiom.class, this);
    }
    
    public void setCleanOnParse(boolean cleanOnParse){
        this.cleanOnParse = cleanOnParse;
    }

    // Ontology section

    public void inAOntology(AOntology node) {
        TopEntityAnalysis.isValidTopEntityIdentifier(node.getId(),node.getTOntology());
        node.getId().apply(container.getNodeHandler(PId.class));
        IRI iri = (IRI) container.popFromStack(Identifier.class, IRI.class);
        Ontology ontology = factory.createOntology(iri);
        
        //clean previous contents of this ontology:
        if (cleanOnParse)
        try {
            ClearTopEntity.clearTopEntity(ontology);
        }
        catch (SynchronisationException e) {
            // should never happen
            throw new RuntimeException("Error During Cleaning TopEntity from previous defintions",e);
        }
        catch (InvalidModelException e) {
            // should never happen
            throw new RuntimeException("Error During Cleaning TopEntity from previous defintions",e);
        }
        
        container.getStack(TopEntity.class).push(ontology);
        container.getStack(Entity.class).push(ontology);
        TopEntityAnalysis.addNamespaceAndVariant(ontology, container.getStack(Namespace.class),
                container.getStack(AWsmlvariant.class));
    }

    public void outAOntology(AOntology node) {
        container.popFromStack(Entity.class, Ontology.class);
    }

    // end Ontology section

    // Concept section

    public void inAConcept(AConcept node) {
        node.getId().apply(container.getNodeHandler(PId.class));
        Identifier id = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        Concept concept = factory.createConcept(id);
        container.getStack(Entity.class).push(concept);
        addToOntology(concept);
    }

    public void outASuperconcept(ASuperconcept node) {
        Identifier[] superConceptId = (Identifier[]) container.popFromStack(Identifier[].class,
                Identifier[].class);
        Concept concept = (Concept) container.peekFromStack(Entity.class, Concept.class);
        for (int i = 0; i < superConceptId.length; i++) {
            try {
                concept.addSuperConcept(factory.getConcept(superConceptId[i]));
            }
            catch (InvalidModelException e) {
                throw new WrappedInvalidModelException(e);
            }
        }
    }
    
    private IRI inverseAttrIRI;

    public void inAAttribute(AAttribute node) {
        node.getId().apply(container.getNodeHandler(PId.class));
        if (node.getId().getClass().equals(AAnonymousId.class)) {
            Token t = ((AAnonymousId) node.getId()).getAnonymous();
            ParserException pe = new ParserException("Anonymous Attributes not supported", null);
            pe.setErrorLine(t.getLine());
            pe.setErrorPos(t.getPos());
            pe.setExpectedToken("IRI");
            pe.setFoundToken(node.getId().toString());
            throw new WrappedParsingException(pe);
        }
        IRI attrIri = (IRI) container.popFromStack(Identifier.class, IRI.class);
        Concept concept = (Concept) container.peekFromStack(Entity.class, Concept.class);
        Attribute attribute;
        try {
            attribute = concept.createAttribute(attrIri);
        } catch (InvalidModelException e) {
            // can never happen since anonIDs are not reused during parsing
            throw new RuntimeException(e);
        }

        if (node.getAttType() instanceof AOpenWorldAttType) {
            attribute.setConstraining(true); // datatype attribute
        }
        else if (node.getAttType() instanceof AClosedWorldAttType) {
            attribute.setConstraining(false); // concept or datatype attribute
        }

        if (node.getCardinality() != null) {
            ACardinality card = (ACardinality) node.getCardinality();
            int minCard = Integer.parseInt(card.getPosInteger().getText().trim());
            attribute.setMinCardinality(minCard);
            if (null != card.getCardinalityNumber()) {
                String str = card.getCardinalityNumber().toString().trim();
                if (str.startsWith("*"))
                    attribute.setMaxCardinality(Integer.MAX_VALUE);
                else
                    attribute.setMaxCardinality(Integer.parseInt(str));
            }
            else { //if no max is given same then min
                attribute.setMaxCardinality(minCard);
            }
        }

        // Process the attribute features
        Object features[] = node.getAttributefeature().toArray();
        for (int j = 0; j < features.length; j++) {
            if (features[j] instanceof ATransitiveAttributefeature) {
                ((Attribute) attribute).setTransitive(true);
            }
            else if (features[j] instanceof ASymmetricAttributefeature) {
                ((Attribute) attribute).setSymmetric(true);
            }
            else if (features[j] instanceof AReflexiveAttributefeature) {
                ((Attribute) attribute).setReflexive(true);
            }
            else if (features[j] instanceof AInverseAttributefeature) {
                AInverseAttributefeature tmp = (AInverseAttributefeature) features[j];
                tmp.getId().apply(container.getNodeHandler(PId.class));
                inverseAttrIRI = (IRI) container.popFromStack(Identifier.class, IRI.class);
                attribute.setInverseOf(inverseAttrIRI);
            }
        }

        container.getStack(Attribute.class).push(attribute);
        container.getStack(Entity.class).push(attribute);
    }

    public void outAAttribute(AAttribute node){
        // Process the attribute range
        Attribute attribute = (Attribute) container.getStack(Attribute.class).pop();
        container.popFromStack(Entity.class, Attribute.class); // remove Entity stack
        Identifier[] typeIds = (Identifier[]) container.getStack(Identifier[].class).pop();
        ConstantTransformer cf = ConstantTransformer.getInstance();
        for (int i = 0; i < typeIds.length; i++) {
            try {
                if (cf.isDataType(typeIds[i].toString())) {
                    attribute.addType(dFactory.createWsmlDataType(typeIds[i].toString()));
                }
                else {
                    attribute.addType(factory.createConcept(typeIds[i]));
                }
            }
            catch (InvalidModelException e) {
                throw new WrappedInvalidModelException(e);
            }
        }
    }

    public void outAConcept(AConcept node) {
        container.popFromStack(Entity.class, Concept.class);
    }

    // end Concept section

    // Instance section

    public void inAInstance(AInstance node) {
        Identifier id = null;
        if (node.getId() != null) {
            node.getId().apply(container.getNodeHandler(PId.class));
            id = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        }
        else {
            id = factory.createAnonymousID();
        }

        Instance instance = factory.createInstance(id);
        container.getStack(Entity.class).push(instance);
        addToOntology(instance);
    }

    public void outAMemberof(AMemberof node) {
        Instance instance = (Instance) container.peekFromStack(Entity.class, Instance.class);
        Identifier[] conceptIds = (Identifier[]) container.popFromStack(Identifier[].class,
                Identifier[].class);
        for (int i = 0; i < conceptIds.length; i++) {
            try {
                instance.addConcept(factory.getConcept(conceptIds[i]));
            }
            catch (InvalidModelException e) {
                throw new WrappedInvalidModelException(e);
            }
        }
    }

    public void outAAttributevalue(AAttributevalue node) {
        if (!(node.parent() instanceof AInstance)) {
            return; // do not handle attributevalue in NFP section see NFPAnalysis
        }

        IRI id = (IRI) container.popFromStack(Identifier.class, Identifier.class);
        Term[] terms = (Term[]) container.popFromStack(Term[].class, Term[].class);
        Instance instance = (Instance) container.peekFromStack(Entity.class, Instance.class);

        for (int i = 0; i < terms.length; i++) {
            try {
                if (terms[i] instanceof DataValue) // DataValue
                    instance.addAttributeValue(id, (DataValue) terms[i]);
                else
                    // Instance iri
                    instance.addAttributeValue(id, factory
                            .getInstance((Identifier) terms[i]));
            }
            catch (InvalidModelException e) {
                throw new WrappedInvalidModelException(e);
            }
        }
    }

    public void outAInstance(AInstance node) {
        container.popFromStack(Entity.class, Instance.class);
    }

    // end Instance section

    // Relation section

    public void inARelation(ARelation node) {
        // Check to now allow both arity and parameters set!
        paramCount=0;
        ParserException ex = null;
        if (node.getArity() == null && node.getParamtyping() == null) {
            ex = new ParserException("For relation " + node.getId()
                    + " are neither cardinality nor parameter types defined", null);
        }
        if (node.getArity() != null && node.getParamtyping() != null) {
            ex = new ParserException("For relation " + node.getId()
                    + " are both cardinality and parameter types defined", null);
        }
        if (ex != null) {
            Token token = ((ARelation) node.getId().parent()).getTRelation();
            ex.setErrorLine(token.getLine());
            ex.setErrorPos(token.getPos());
            throw new WrappedParsingException(ex);
        }

        node.getId().apply(container.getNodeHandler(PId.class));
        Identifier id = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        Relation relation = factory.createRelation(id);
        container.getStack(Entity.class).push(relation);
        addToOntology(relation);

        if (node.getArity() != null) {
            int arity = Integer.parseInt(((AArity) node.getArity()).getPosInteger().toString()
                    .trim());
            for (byte i = 0; i < arity; i++) {
                try {
                    relation.createParameter(i);
                }
                catch (InvalidModelException e) {
                    throw new WrappedInvalidModelException(e);
                }
            }
        }
    }

    public void outASuperrelation(ASuperrelation node) {
        Identifier[] superRelationId = (Identifier[]) container.popFromStack(Identifier[].class,
                Identifier[].class);
        Relation relation = (Relation) container.peekFromStack(Entity.class, Relation.class);
        for (int i = 0; i < superRelationId.length; i++) {
            try {
                relation.addSuperRelation(factory.getRelation(superRelationId[i]));
            }
            catch (InvalidModelException e) {
                throw new WrappedInvalidModelException(e);
            }
        }
    }

    public void outAParamtype(AParamtype node) {
        Identifier[] paramTypes = (Identifier[]) container.popFromStack(Identifier[].class,
                Identifier[].class);
        Relation relation = (Relation) container.peekFromStack(Entity.class, Relation.class);
        Parameter param = null;
        
        try {
            param = relation.createParameter(paramCount++);
        }
        catch (InvalidModelException e) {
            throw new WrappedInvalidModelException(e);
        }

        if (node.getAttType() instanceof AOpenWorldAttType) {
            param.setConstraining(true);
        }
        else {
            param.setConstraining(false);
        }
        ConstantTransformer cf = ConstantTransformer.getInstance();
        for (int i = 0; i < paramTypes.length; i++) {
            // can never happen since anonIDs are not reused during parsing
            if (cf.isDataType(paramTypes[i].toString())) {
                try {
                    param.addType(dFactory.createWsmlDataType(paramTypes[i].toString()));
                } catch (InvalidModelException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                try {
                    param.addType(factory.getConcept(paramTypes[i]));
                } catch (InvalidModelException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void outARelation(ARelation node) {
        container.popFromStack(Entity.class, Relation.class);
    }

    // end Relation section

    // RelationInstance section

    private Term rootTerm;

    public void inARelationinstance(ARelationinstance node) {
        Identifier id = null;
        if (node.getName() != null) {
            node.getName().apply(container.getNodeHandler(PId.class));
            id = (Identifier) container.popFromStack(Identifier.class, Identifier.class);
        }
        else {
            id = factory.createAnonymousID();
        }

        node.getRelation().apply(container.getNodeHandler(PId.class));
        Relation relation = factory.getRelation((Identifier) container.popFromStack(
                Identifier.class, Identifier.class));
        try {
            RelationInstance relInstance = factory.createRelationInstance(id, relation);
            addToOntology(relInstance);
            container.getStack(Entity.class).push(relInstance);
        }
        catch (InvalidModelException e) {
            throw new WrappedInvalidModelException(e);
        }

        if (!container.getStack(Term.class).isEmpty()){
            rootTerm = (Term) container.peekFromStack(Term.class, Term.class);
        }
        else {
            rootTerm = null;
        }
    }

    public void outARelationinstance(ARelationinstance node) {
        Stack termStack = container.getStack(Term.class);
        RelationInstance relInstance = (RelationInstance) container.peekFromStack(Entity.class,
                RelationInstance.class);
        //byte argument = (byte) relInstance.getRelation().listParameters().size();
        //might be a proxy and above thus does not work
        Vector v = new Vector();
        while (!termStack.isEmpty() && rootTerm != container.peekFromStack(Term.class, Term.class)) {
            v.add(0,container.popFromStack(Term.class, Term.class));
        }
        Iterator i = v.iterator();
        for (byte n=0; i.hasNext(); n++) {
            Term term = (Term) i.next();
            try {
                if (term instanceof DataValue) {// DataValue
                    relInstance.setParameterValue(n, (DataValue) term);
                }
                else { // Instance iri
                    relInstance.setParameterValue(n, factory
                            .getInstance((Identifier) term));
                }
            }
            catch (InvalidModelException e) {
                throw new WrappedInvalidModelException(e);
            }
        }
        container.popFromStack(Entity.class, RelationInstance.class);
    }

    public void outAAxiom(AAxiom node) {
        Axiom axiom = (Axiom) container.popFromStack(Axiom.class, Axiom.class);
        addToOntology(axiom);
    }

    private void addToOntology(OntologyElement element) {
        Stack topEntities = container.getStack(TopEntity.class);
        if (topEntities.isEmpty() || !(topEntities.peek() instanceof Ontology)) {
            throw new RuntimeException("OntologyElement does not " + "have Ontology context!");
        }
        try {
            element.setOntology((Ontology) topEntities.peek());
        }
        catch (InvalidModelException e) {
            throw new WrappedInvalidModelException(e);
        }
    }
}

/*
 * $Log: OntologyAnalysis.java,v $
 * Revision 1.8  2006/04/24 08:04:58  holgerlausen
 * improved error handling in case of topentities without identifier
 * moved thomas unit test to "open" package, since it does not break expected behavior, but just document some derivations from the spec
 *
 * Revision 1.7  2006/04/11 16:06:58  holgerlausen
 * addressed RFE 1468651 ( http://sourceforge.net/tracker/index.php?func=detail&aid=1468651&group_id=113501&atid=665349)
 * currently the default behaviour of the parser is still as before
 *
 * Revision 1.6  2006/02/16 10:02:50  vassil_momtchev
 * setInverseOf(Attribute) changed to setInverseOf(Identifier)
 *
 * Revision 1.5  2006/02/13 22:49:23  nathaliest
 * - changed concept.createAttribute() and Parameter.addType to throw InvalidModelException.
 * - small change at check AnonIds in ConceptImpl
 *
 * Revision 1.4  2006/02/13 09:48:52  vassil_momtchev
 * the code to handle the topentities identifier validity refactored
 *
 * Revision 1.3  2006/02/10 14:35:27  vassil_momtchev
 * parser addapted to the new api changes
 *
 * Revision 1.2  2005/11/29 14:23:38  nathaliest
 * fixed bug concerning number of relation parameters
 *
 * Revision 1.1  2005/11/28 13:55:26  vassil_momtchev
 * AST analyses
 *
*/
