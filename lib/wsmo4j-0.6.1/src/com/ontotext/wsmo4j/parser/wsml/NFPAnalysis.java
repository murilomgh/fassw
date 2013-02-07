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

import org.omwg.logicalexpression.terms.*;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.wsml.compiler.node.*;

import com.ontotext.wsmo4j.parser.*;

public class NFPAnalysis extends ASTAnalysis {

    private Stack termsStack;
    private Stack identifierStack;
    private Stack entityStack;

    public NFPAnalysis(ASTAnalysisContainer container) {
        if (container == null) {
            throw new IllegalArgumentException();
        }
        termsStack = container.getStack(Term[].class);
        identifierStack = container.getStack(Identifier.class);
        entityStack = container.getStack(Entity.class);

        // register the handled nodes
        container.registerNodeHandler(ANfp.class, this);
        container.registerNodeHandler(PNfp.class, this);
    }

    private Object rootIdentifier;
    private Object rootTerm;

    public void inANfp(ANfp node) {
        // keep a handle to the stack's last element
        if (!termsStack.isEmpty())
            rootTerm = termsStack.peek();
        else
            rootTerm = null;
        if (!identifierStack.isEmpty())
            rootIdentifier = identifierStack.peek();
        else
            rootIdentifier = null;
    }

    public void outANfp(ANfp node) {
        if (entityStack.isEmpty()) {
            return; // NFP node outside of known Entity set
        }
        Entity entity = (Entity) entityStack.peek();

        try {
            Term[] terms = null;
            while (!identifierStack.isEmpty() &&
                    identifierStack.peek() != rootIdentifier) {

                if (termsStack.isEmpty()) {
                    throw new RuntimeException("The NFP term queue is shorter then iri queue!");
                }

                IRI iri = (IRI) identifierStack.pop();
                terms = (Term[]) termsStack.pop();

                for (int i = 0; i < terms.length; i++) {
                    if (terms[i] instanceof DataValue)
                        entity.addNFPValue(iri, (DataValue) terms[i]);
                    else // Identifier
                        entity.addNFPValue(iri, (Identifier) terms[i]);
                }
            }

            // check that the last stack element did not change
            if (!termsStack.isEmpty() &&
                    termsStack.peek() != rootTerm) {
                throw new RuntimeException("The NFP term queue is longer then iri queue!");
            }
        }
        catch (InvalidModelException ime) {
            throw new WrappedInvalidModelException(ime);
        }
    }
}

/*
 * $Log: NFPAnalysis.java,v $
 * Revision 1.2  2006/02/10 14:37:25  vassil_momtchev
 * parser addapted to the grammar changes; unused class variables removed;
 *
 * Revision 1.1  2005/11/28 13:55:26  vassil_momtchev
 * AST analyses
 *
*/
