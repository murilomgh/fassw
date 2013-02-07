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
package org.deri.wsmo4j.io.serializer.rdf;

import java.io.IOException;

import org.omwg.logicalexpression.AttributeValueMolecule;
import org.omwg.logicalexpression.Implication;
import org.omwg.logicalexpression.LogicalExpression;
import org.omwg.ontology.Axiom;
import org.openrdf.vocabulary.RDFS;

/**
 * Helper type to serialize/deserialize xml elements with logical expressions
 * 
 * @author not attributable
 */
class NodeLogExp {
    
    static void serialize(Axiom axiom, WsmlRdfSerializer serializer) {
        Object[] exprs = axiom.listDefinitions().toArray();
        for (int i = 0; i < exprs.length; i++) {
            LogicalExpression expr = (LogicalExpression)exprs[i];
            if(expr instanceof Implication) {
                Implication impl = (Implication)expr;
                if(impl.getLeftOperand() instanceof AttributeValueMolecule &&
                    impl.getRightOperand() instanceof AttributeValueMolecule) {
                    
                    try {
                        AttributeValueMolecule left = (AttributeValueMolecule)impl.getLeftOperand();
                        AttributeValueMolecule right = (AttributeValueMolecule)impl.getRightOperand();
                        
                        serializer.getWriter().writeStatement(
                                serializer.getFactory().createURI(left.getAttribute().toString()),
                                serializer.getFactory().createURI(RDFS.SUBPROPERTYOF),
                                serializer.getFactory().createURI(right.getAttribute().toString()));
                    } catch(IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }
}

/*
 *
*/