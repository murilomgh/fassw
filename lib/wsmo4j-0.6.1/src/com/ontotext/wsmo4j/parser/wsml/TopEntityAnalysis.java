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

import org.deri.wsmo4j.io.parser.*;
import org.wsmo.common.*;
import org.wsmo.factory.*;
import org.wsmo.wsml.*;
import org.wsmo.wsml.compiler.node.*;

public class TopEntityAnalysis extends ASTAnalysis {

    private ASTAnalysisContainer container;

    private WsmoFactory factory;

    private Stack topEntityStack;

    public TopEntityAnalysis(ASTAnalysisContainer container, WsmoFactory factory) {
        if (container == null) {
            throw new IllegalArgumentException();
        }
        this.container = container;
        this.factory = factory;
        topEntityStack = container.getStack(TopEntity.class);

        // register the handled nodes
        container.registerNodeHandler(AImportsontology.class, this);
        container.registerNodeHandler(AUsesmediator.class, this);
        container.registerNodeHandler(AWsmlvariant.class, this);
    }

    public void outAImportsontology(AImportsontology node) {
        if (topEntityStack.isEmpty()) {
            return;
        }
        Identifier[] iris = (Identifier[]) container.popFromStack(Identifier[].class,
                Identifier[].class);
        TopEntity topEntity = (TopEntity) topEntityStack.peek();
        for (int i = 0; i < iris.length; i++) {
            topEntity.addOntology(factory.getOntology((IRI) iris[i]));
        }
    }

    public void outAUsesmediator(AUsesmediator node) {
        if (topEntityStack.isEmpty()) {
            return;
        }
        Identifier[] iris = (Identifier[]) container.popFromStack(Identifier[].class,
                Identifier[].class);
        TopEntity topEntity = (TopEntity) topEntityStack.peek();
        for (int i = 0; i < iris.length; i++) {
            if (iris[i] instanceof IRI) {
                topEntity.addMediator((IRI) iris[i]);
                    }
            else {
                ParserException pe = new ParserException("usesMediator could "
                        + "refer Mediators only by IRI!", null);
                pe.setErrorLine(node.getTUsemediator().getLine());
                pe.setErrorPos(node.getTUsemediator().getPos());
                throw new WrappedParsingException(pe);
                }
            }
    }

    public void inAWsmlvariant(AWsmlvariant node) {
        String wsmlVariant = node.getFullIri().toString().trim();
        wsmlVariant = wsmlVariant.substring(2, wsmlVariant.length() - 1);
        container.getStack(AWsmlvariant.class).push(wsmlVariant);
    }

    public static void addNamespaceAndVariant(TopEntity entity, Stack namespaces, Stack variant) {
        for (int i = 0; i < namespaces.size(); i++) {
            Namespace namespace = (Namespace) namespaces.get(i);
            if (namespace.getPrefix().equals("_")) {
                entity.setDefaultNamespace(namespace);
            }
            else {
                entity.addNamespace(namespace);
            }
        }

        if (!variant.isEmpty()) {
            entity.setWsmlVariant((String) variant.peek());
        }
    }

    /**
     * 
     * @param id id of top entitiy
     * @param token: token of parent node of id 
     * (used for error msg in case id is null)
     */
    public static void isValidTopEntityIdentifier(PId id, Token token) {
        if (id instanceof AIriId == false) {
            ParserException pe = 
                new ParserException("Expected IRI but found Identifier,", null);
            pe.setExpectedToken("IRI");
            if (id!=null){
                pe.setFoundToken(id.toString());
            }
            if (id instanceof AAnonymousId) {
                Token t = ((AAnonymousId) id).getAnonymous();
                pe.setErrorLine(t.getLine());
                pe.setErrorPos(t.getPos());
            }
            //in case id is null take end position of parent element
            if (id == null && token != null){
                pe.setErrorLine(token.getLine());
                pe.setErrorPos(token.getPos()+token.getText().length());
            }
            throw new WrappedParsingException(pe);
        }
    }
}

/*
 * $Log: TopEntityAnalysis.java,v $
 * Revision 1.7  2006/11/17 12:02:11  vassil_momtchev
 * validation of Identifier = IRI is not longer specific to TopEntity types only
 *
 * Revision 1.6  2006/04/24 08:04:58  holgerlausen
 * improved error handling in case of topentities without identifier
 * moved thomas unit test to "open" package, since it does not break expected behavior, but just document some derivations from the spec
 *
 * Revision 1.5  2006/04/05 13:24:45  vassil_momtchev
 * usesMediator now refer mediators by  IRI instead of handle to object
 *
 * Revision 1.4  2006/03/07 10:43:20  vassil_momtchev
 * parser tries to get one of the 4 kind of mediators in the usesMediator constructions
 *
 * Revision 1.3  2006/02/13 09:48:52  vassil_momtchev
 * the code to handle the topentities identifier validity refactored
 *
 * Revision 1.2  2006/02/10 14:37:25  vassil_momtchev
 * parser addapted to the grammar changes; unused class variables removed;
 *
 * Revision 1.1  2005/11/28 13:55:26  vassil_momtchev
 * AST analyses
 *
 */