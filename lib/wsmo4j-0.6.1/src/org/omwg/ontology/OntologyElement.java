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

package org.omwg.ontology;


import org.wsmo.common.*;
import org.wsmo.common.exception.*;

/**
 * this is a helper super-interface for ontology elements:
 *    - axioms
 *    - concepts
 *    - instances
 *    - relations
 *    - relation instances
 *
 * @see Ontology
 * @author not attributable
 * @version $Revision: 1.2 $ $Date: 2006/01/16 13:37:08 $
 */

public interface OntologyElement
    extends Entity {

    /**
     * Retrieve the ontology containing this entity.
     * @throws org.wsmo.common.exception.SynchronisationException
     */
    Ontology getOntology()
        throws SynchronisationException;

    /**
     * Sets the ontology containing this entity
     * Note that a call to setOntology() i.e. adding an element to an ontology, should also
     * invoke the respective Ontology::addXXX() method and Ontology::removeXXX() method
     * (of the old ontology if existing).
     * @param ontology the ontology
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #getOntology()
     * @see Ontology#addAxiom(Axiom axiom)
     * @see Ontology#addConcept(Concept concept)
     * @see Ontology#addInstance(Instance instance)
     * @see Ontology#addRelation(Relation relation)
     * @see Ontology#addRelationInstance(RelationInstance instance)
     */
    void setOntology(Ontology ontology)
        throws SynchronisationException, InvalidModelException;
}

/*
 * $Log: OntologyElement.java,v $
 * Revision 1.2  2006/01/16 13:37:08  vassil_momtchev
 * javadoc fixed
 *
 * Revision 1.1  2005/06/01 10:13:58  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.5  2005/05/13 15:38:26  marin
 * fixed javadoc errors
 *
 * Revision 1.4  2005/05/13 13:29:57  marin
 * javadoc, header, footer, etc
 *
 */
