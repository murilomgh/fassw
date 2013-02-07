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

package org.wsmo.datastore;

import java.util.*;

import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.service.*;
import org.wsmo.mediator.*;

/**
 * An WSMO Repository facade
 *
 * @author not attributable
 * @version $Revision: 1.5 $ $Date: 2007/04/02 14:19:19 $
 */
public interface WsmoRepository extends DataStore {

    public String getDescription();

    public void setDescription(String desc);

    String getVersion() throws SynchronisationException;

    public void addOntology(Ontology ont) throws SynchronisationException;

    public void saveOntology(Ontology ont) throws SynchronisationException;

    public Ontology getOntology(IRI id) throws SynchronisationException;

    public void deleteOntology(IRI id) throws SynchronisationException;

    public List<IRI> listOntologies() throws SynchronisationException;

    public void addGoal(Goal goal) throws SynchronisationException;

    public void saveGoal(Goal ont) throws SynchronisationException;

    public Goal getGoal(IRI id) throws SynchronisationException;

    public void deleteGoal(IRI id) throws SynchronisationException;

    public List<IRI> listGoals() throws SynchronisationException;

    public void addMediator(Mediator med) throws SynchronisationException;

    public void saveMediator(Mediator med) throws SynchronisationException;

    public Mediator getMediator(IRI id) throws SynchronisationException;

    public void deleteMediator(IRI id) throws SynchronisationException;

    public List<IRI> listMediators() throws SynchronisationException;

    public void addWebService(WebService ws) throws SynchronisationException;

    public void saveWebService(WebService ws) throws SynchronisationException;

    public WebService getWebService(IRI id) throws SynchronisationException;

    public void deleteWebService(IRI id) throws SynchronisationException;

    public List<IRI> listWebServices() throws SynchronisationException;
}

/*
 * $Log: WsmoRepository.java,v $
 * Revision 1.5  2007/04/02 14:19:19  alex_simov
 * list-  methods return collections of IRIs
 *
 * Revision 1.4  2007/04/02 12:13:16  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.3  2005/06/27 14:30:26  alex_simov
 * bugfix: invalid return type for getGoal() - it was Ontology
 *
 * Revision 1.2  2005/06/27 14:27:20  alex_simov
 * mistyping error: listMediator() -> listMediators()
 *
 * Revision 1.1  2005/06/27 13:45:44  alex_simov
 * 1) refactoring: *.io.datastore -> *.datastore
 * 2) set/getDescription() added
 *
 * Revision 1.1  2005/06/24 13:29:45  marin_dimitrov
 * WsmoRepository facade
 *
 *
 */
