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

import org.omwg.ontology.*;

/**
 * Helper type to serialize/deserialize xml element ontology
 * 
 * @author not attributable
 */
class NodeOntology {

    static void serialize(Ontology ontology, WsmlRdfSerializer serializer) {
        
        if (!ontology.listNFPValues().isEmpty()) {
            NodeNFP.serialize(ontology, serializer);
        }

        if (!ontology.listConcepts().isEmpty()) {
            Object[] concepts = ontology.listConcepts().toArray();
            for (int i = 0; i < concepts.length; i++) {
                NodeConcept.serialize((Concept) concepts[i], serializer);
            }
        }

        if (!ontology.listInstances().isEmpty()) {
            Object[] instances = ontology.listInstances().toArray();
            for (int i = 0; i < instances.length; i++) {
                NodeInstance.serialize((Instance) instances[i],serializer);
            }
        }

        if (!ontology.listRelations().isEmpty()) {
            Object[] relations = ontology.listRelations().toArray();
            for (int i = 0; i < relations.length; i++)
                NodeRelation.serialize((Relation) relations[i],serializer);
        }

        if (!ontology.listRelationInstances().isEmpty()) {
            Object[] relationInstances = ontology.listRelationInstances().toArray();
            for (int i = 0; i < relationInstances.length; i++) {
                NodeRelationInstance.serialize(
                        (RelationInstance) relationInstances[i], serializer);
            }
        }
        
        if (!ontology.listAxioms().isEmpty()) {
            Object[] axioms = ontology.listAxioms().toArray();
            for (int i = 0; i < axioms.length; i++) {
                NodeLogExp.serialize((Axiom) axioms[i], serializer);
            }
        }
        
    }
}

/*
 * $Log: NodeOntology.java,v $
 * Revision 1.2  2006/11/16 14:35:09  ohamano
 * *** empty log message ***
 *
 * Revision 1.1  2006/11/10 15:02:59  ohamano
 * *** empty log message ***
 *
 * Revision 1.1  2006/11/10 10:08:42  ohamano
 * *** empty log message ***
 *
 *
*/