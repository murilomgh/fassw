/*
 wsmo4j extension - a Choreography API and Reference Implementation

 Copyright (c) 2005, University of Innsbruck, Austria

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

package org.wsmo.service.signature;

import java.util.Set;

import org.omwg.ontology.Ontology;
import org.wsmo.common.Entity;
import org.wsmo.common.IRI;
import org.wsmo.mediator.Mediator;
import org.wsmo.mediator.OOMediator;

/**
 * StateSignature defines the containers for the modes and also implements
 * methods to allow the modification of imported ontologies which define the
 * state of the machine.
 * 
 * <pre>
 *      Created on Jul 26, 2005
 *      Committed by $Author: vassil_momtchev $
 *      $Source: /cvsroot/wsmo4j/ext/choreography/src/api/org/wsmo/service/signature/StateSignature.java,v $
 * </pre>
 * 
 * @author James Scicluna
 * @author Thomas Haselwanter
 * @author Holger Lausen
 * 
 * @version $Revision: 1.1 $ $Date: 2006/10/24 14:56:41 $
 */
public interface StateSignature extends Entity, Iterable<Mode>  {
    
    /**
     * Lists the In Modes
     * 
     * @return Set of concept references and groundings for the In Mode
     */
    public Set<In> listInModes();

    /**
     * Lists the Out Modes
     * 
     * @return Set of concept references and groundings for the Out Mode
     */
    public Set<Out> listOutModes();

    /**
     * Lists the Shared Modes
     * 
     * @return Set of concept references and groundings (if any) for the Shared
     *         Mode
     */
    public Set<Shared> listSharedModes();

    /**
     * Lists the Static Mode
     * 
     * @return Set of concept references for the Static Mode
     */
    public Set<Static> listStaticModes();

    /**
     * Lists the Controlled Mode
     * 
     * @return Set of concept references for the Controlled Mode
     */
    public Set<Controlled> listControlledModes();

    /**
     * Adds an ontology to this element's imported ontologies list
     * 
     * @param ontology
     *            The ontology to be imported.
     * @see #removeOntology(Ontology ontology)
     * @see #removeOntology(IRI iri)
     */
    public void addOntology(Ontology ontology);

    /**
     * Removes an ontology from the list of ontolgies which this element
     * imports.
     * 
     * @param iri
     *            The ID of the ontology to be removed from this element's
     *            imported ontologies list.
     */
    public void removeOntology(IRI iri);

    /**
     * Removes an ontology from the list of ontolgies which this element
     * imports.
     * 
     * @param ontology
     *            The ontology to be removed from this element's imported
     *            ontologies list.
     */
    public void removeOntology(Ontology ontology);

    /**
     * Returns a list of ontologies which this element imports.
     * 
     * @return : The list of ontologies that this element imports.
     * @see org.omwg.ontology.Ontology
     */
    public Set<Ontology> listOntologies();

    /**
     * Adds a reference to a new oo-mediator to this element's used mediators list.
     * @param mediator the used mediator reference
     * @see #removeMediator(Mediator mediator)
     */
    void addMediator(OOMediator mediator);

    /**
     * Removes a reference to a mediator from this element's used mediator list.
     * @param mediator The mediator to be removed from this element's list of used mediators.
     */
    void removeMediator(OOMediator mediator);

    /**
     * Removes a reference to a oo-mediator from this element's used mediator list.
     * @param iri The ID of the mediator to be removed from this element's list of used mediators.
     */
    void removeMediator(IRI iri);

    /**
     * Returns the list of used mediators.
     * @return : The list of used mediators.
     * @see org.wsmo.mediator.Mediator
     */
    Set listMediators();

    /**
     * Adds a particular mode to the signature
     * 
     * @param mode
     */
    public void addMode(Mode mode);

    /**
     * Removes a particular mode from the state signature
     * 
     * @param mode
     *            The Mode object to be removed
     */
    public void removeMode(Mode mode);
}
