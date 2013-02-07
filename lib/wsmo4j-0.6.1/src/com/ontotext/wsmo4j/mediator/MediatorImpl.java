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

package com.ontotext.wsmo4j.mediator;

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */ 
import java.util.*;

import org.wsmo.common.*;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.common.exception.SynchronisationException;
import org.wsmo.mediator.Mediator;

import com.ontotext.wsmo4j.common.TopEntityImpl;


public class MediatorImpl extends TopEntityImpl
        implements Mediator {

    protected LinkedHashSet <IRI> sources;
    private IRI target;
    private IRI mediationService;


    public MediatorImpl(IRI mediatorIRI) {
        super(mediatorIRI);
        sources = new LinkedHashSet <IRI> ();
    }

    public Set <IRI> listSources() throws SynchronisationException {
        return Collections.unmodifiableSet(sources);
    }

    public void addSource(IRI iri)
            throws SynchronisationException, InvalidModelException {
        if (iri == null) {
            throw new IllegalArgumentException();
        }
        if (sources.contains(iri)){
            return;
        }
        if (!sources.isEmpty()){
            throw new InvalidModelException("Only OO Mediators have multiple source");
        }
        sources.add(iri);
    }

    public void removeSource(IRI iri)
            throws SynchronisationException, InvalidModelException {
        if (iri == null) {
            throw new IllegalArgumentException();
        }
        sources.remove(iri);
    }

    public IRI getTarget() {
        return target;
    }

    public void setTarget(IRI target) {
        this.target = target;
    }

    public IRI getMediationService() {
        return mediationService;
    }

    public void setMediationService(IRI newServiceID) {
        mediationService = newServiceID;
    }

    public boolean equals(Object object) {
        if (object == null
                || object instanceof Mediator == false) {
            return false;
        }
        return super.equals(object);
    }
}

/*
 * $Log: MediatorImpl.java,v $
 * Revision 1.28  2007/04/02 12:13:24  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.27  2006/03/29 08:51:04  vassil_momtchev
 * mediator reference source/target by IRI (changed from TopEntity); mediator reference mediationService by IRI (changed from Identifier)
 *
 * Revision 1.26  2006/02/13 10:41:04  vassil_momtchev
 * the constructors of the topentities to disallow Identifier; see WsmoFactoryImpl
 *
 * Revision 1.25  2005/12/02 14:33:44  holgerlausen
 * fixed parsing bug due to type when overwriting method, fixed but related to multiple source of mediators during reparsing
 *
 * Revision 1.24  2005/11/29 15:53:29  holgerlausen
 * fixing unit test for checking multiple sources
 *
 * Revision 1.23  2005/11/08 13:54:17  alex_simov
 * mistyping: setMediationSerivice() -> setMediationService()
 *
 * Revision 1.22  2005/06/22 09:16:05  alex_simov
 * Simplified equals() method. Identity strongly relies on identifier string
 *
 * Revision 1.21  2005/06/01 12:07:31  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.1  2005/05/13 14:05:49  alex
 * initial commit
 *
 */
