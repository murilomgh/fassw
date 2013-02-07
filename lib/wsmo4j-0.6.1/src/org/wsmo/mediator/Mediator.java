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

package org.wsmo.mediator;


import java.util.*;

import org.wsmo.common.*;
import org.wsmo.common.exception.*;


public interface Mediator
    extends TopEntity {

    /**
     * Returns set of the source components for this mediator.
     * Depending on the type of the mediator, the source component 
     * may be IRI of a TopEntity such as WebService, Goal, ... .
     * @return : The set of the source components for this mediator.
     * @see org.wsmo.common.IRI
     */
    Set <IRI> listSources()
        throws SynchronisationException;

    /**
     * add a source component to this mediator.
     * @param iri a source component of this mediator.
     * @see #removeSource(IRI iri)
     */
    void addSource(IRI iri)
        throws SynchronisationException, InvalidModelException;

    /**
     * remove a source component from this mediator.
     * @param iri : a source component to remove.
     */
    void removeSource(IRI iri)
        throws SynchronisationException, InvalidModelException;

    /**
     * Returns the target component of this mediator.
     * Depending on the type of the mediator, the target component 
     * may be IRI of a TopEntity such as WebService, Goal, ... .
     * @return : The target component (TopEntity) of this mediator.
     * @see #setTarget(IRI target)
     */
    IRI getTarget()
        throws SynchronisationException;

    /**
     * Sets the target component of this mediator.
     * Depending on the type of the mediator, the target component may be a TopEntity such as WebService, Goal, ...
     * @param target : The new target component.
     * @see #getTarget()
     */
    void setTarget(IRI target)
        throws SynchronisationException, InvalidModelException;

    /**
     * Returns the mediation service of this mediator.
     * @return : The identificator of the mediation service of this mediator.
     * @see #setMediationSerivice(Identifier medServiceID)
     */
    IRI getMediationService()
        throws SynchronisationException;

    /**
     * Sets the ID of the service which performs the actual mediation.
     * @param newServiceID The ID of the service which performs the actual mediation.
     */
    void setMediationService(IRI medServiceID)
        throws SynchronisationException, InvalidModelException;
}

/*
 * $Log: Mediator.java,v $
 * Revision 1.13  2007/04/02 12:13:15  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.12  2006/03/29 08:49:52  vassil_momtchev
 * mediator reference source/target by IRI (changed from TopEntity); mediator reference mediationService by IRI (changed from Identifier)
 *
 * Revision 1.11  2005/11/08 13:54:44  alex_simov
 * mistyping: setMediationSerivice() -> setMediationService()
 *
 * Revision 1.10  2005/06/01 10:57:43  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.4  2005/05/13 15:38:26  marin
 * fixed javadoc errors
 *
 * Revision 1.3  2005/05/13 13:00:16  marin
 * 1.javadoc, header, footer, etc
 * 2. changed source/target component from java.lang.Object to org.wsmo.common.TopEntity
 *
 */
