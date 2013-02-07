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

package com.ontotext.wsmo4j.parser.wsml;

import java.util.*;

import org.omwg.logicalexpression.*;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.factory.*;
import org.wsmo.wsml.compiler.node.*;


public class AxiomAnalysis extends ASTAnalysis {

    private WsmoFactory factory;
    private ASTAnalysisContainer container;

    public AxiomAnalysis(ASTAnalysisContainer container, WsmoFactory factory) {
        if (factory == null || container == null) {
            throw new IllegalArgumentException();
        }
        this.factory = factory;
        this.container = container;

        // register the handled nodes
        container.registerNodeHandler(ADefinedAxiomAxiomdefinition.class, this);
        container.registerNodeHandler(ANfpAxiomAxiomdefinition.class, this);
        container.registerNodeHandler(AUseAxiomAxiomdefinition.class, this);
    }

    public void inADefinedAxiomAxiomdefinition(ADefinedAxiomAxiomdefinition node) {
        _inAxiomdefinition(node.getId());
    }

    public void inANfpAxiomAxiomdefinition(ANfpAxiomAxiomdefinition node) {
        _inAxiomdefinition(node.getId());
    }

    public void inAUseAxiomAxiomdefinition(AUseAxiomAxiomdefinition node) {
        _inAxiomdefinition(node.getId());
    }

    public void outADefinedAxiomAxiomdefinition(ADefinedAxiomAxiomdefinition node) {
        _outAAxiomdefinition();
    }

    public void outANfpAxiomAxiomdefinition(ANfpAxiomAxiomdefinition node) {
        _outAAxiomdefinition();
    }

    public void outAUseAxiomAxiomdefinition(AUseAxiomAxiomdefinition node) {
        _outAAxiomdefinition();
    }


    private void _outAAxiomdefinition() {
        Axiom a = (Axiom) container.popFromStack(Entity.class, Axiom.class);
        Stack s = container.getStack(LogicalExpression.class);
        while (!s.isEmpty()) {
            a.addDefinition((LogicalExpression) s.remove(0));
        }
    }

    private void _inAxiomdefinition(PId id) {
        Axiom axiom = null;
        if (id == null) {
            axiom = factory.createAxiom(factory.createAnonymousID());
        }
        else {
            id.apply(container.getNodeHandler(PId.class));
            axiom = factory.createAxiom((Identifier) container.popFromStack(Identifier.class,
                    Identifier.class));
        }

        container.getStack(Entity.class).push(axiom);
        container.getStack(Axiom.class).push(axiom);
    }
}

/*
 * $Log: AxiomAnalysis.java,v $
 * Revision 1.2  2005/11/29 14:44:24  holgerlausen
 * fixed axiom analysis, logexp are now removed from stack
 *
 * Revision 1.1  2005/11/28 13:55:26  vassil_momtchev
 * AST analyses
 *
*/
