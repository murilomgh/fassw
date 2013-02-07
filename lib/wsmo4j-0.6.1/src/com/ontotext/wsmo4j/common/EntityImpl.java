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

package com.ontotext.wsmo4j.common;


import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.omwg.ontology.Value;
import org.wsmo.common.Entity;
import org.wsmo.common.IRI;
import org.wsmo.common.Identifier;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.common.exception.SynchronisationException;

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */
public abstract class EntityImpl implements Entity {

    private Identifier id;
    private HashMap <IRI, Set <Object>> nfpProps;

    public EntityImpl(Identifier thisID) {
        if (thisID == null) {
            throw new IllegalArgumentException(Errors.ERR_INVALID_ID);
        }
        this.id = thisID;
        nfpProps = new HashMap <IRI, Set <Object>> ();
    }

    /**
     * Returns the list of values associated
     * with the specified key.
     * @param key The key of interest.
     * @return A list of values associated with
     * the specified key.
     */
    public Set <Object> listNFPValues(IRI key) throws SynchronisationException {
        //preconditions
        if (key == null) {
            throw new IllegalArgumentException(Errors.ERR_INVALID_NFP_KEY);
        }

        LinkedHashSet <Object> vals = (LinkedHashSet <Object>) nfpProps.get(key);

        if (vals == null) {
            return Collections.unmodifiableSet(new HashSet <Object> ()); // immutable
        }
        return Collections.unmodifiableSet(vals);
    }

    public Map <IRI, Set <Object>> listNFPValues() throws SynchronisationException {
        LinkedHashMap <IRI, Set <Object>>  result = new LinkedHashMap <IRI, Set <Object>> ();
        for(Iterator <IRI> it = nfpProps.keySet().iterator(); it.hasNext();) {
            IRI key = it.next();
            LinkedHashSet <Object> curVal = (LinkedHashSet <Object>) nfpProps.get(key);
            result.put(key, Collections.unmodifiableSet(curVal));
        }
        return result;
    }

    private void _addNFPValue(IRI key, Object value) throws SynchronisationException,
            InvalidModelException {
        //preconditions
        if (key == null) {
            throw new IllegalArgumentException(Errors.ERR_INVALID_NFP_KEY);
        }
        if (value == null) {
            throw new IllegalArgumentException(Errors.ERR_INVALID_NFP_VALUE);
        }

        synchronized (this.nfpProps) {
            LinkedHashSet <Object> vals = (LinkedHashSet <Object>) nfpProps.get(key);

            if (vals == null) {
                vals = new LinkedHashSet <Object>();
                nfpProps.put(key, vals);
            }
            vals.add(value);
        }
    }

    public void addNFPValue(IRI key, Identifier value) throws SynchronisationException,
            InvalidModelException {

        this._addNFPValue(key, value);
    }


    public void addNFPValue(IRI key, Value value) throws SynchronisationException,
            InvalidModelException {

        this._addNFPValue(key, value);
    }


    private void _removeNFPValue(IRI key, Object value) throws SynchronisationException,
            InvalidModelException {

        //preconditions
        if (key == null) {
            throw new IllegalArgumentException(Errors.ERR_INVALID_NFP_KEY);
        }
        if (value == null) {
            throw new IllegalArgumentException(Errors.ERR_INVALID_NFP_VALUE);
        }

        synchronized (this.nfpProps) {
            LinkedHashSet <Object> vals = (LinkedHashSet <Object>) nfpProps.get(key);
            //no such entry?
            if (vals == null) {
                return;
            }
            vals.remove(value);
            if (vals.size() == 0) {
                //remove key entry, no values left
                nfpProps.remove(key);
            }
        }
    }

    public void removeNFPValue(IRI key, Identifier value) throws SynchronisationException,
            InvalidModelException {

        this._removeNFPValue(key, value);
    }

    public void removeNFPValue(IRI key, Value value) throws SynchronisationException,
            InvalidModelException {

        this._removeNFPValue(key, value);
    }

    public void removeNFP(IRI key)
            throws SynchronisationException, InvalidModelException {

        //preconditions
        if (key == null) {
            throw new IllegalArgumentException(Errors.ERR_INVALID_NFP_KEY);
        }

        this.nfpProps.remove(key);
    }

    public Identifier getIdentifier() {
        return id;
    }

    public boolean equals(Object target) {
        if (target == this) {
            return true; // instance match
        }
        if (null == target || false == target instanceof Entity) {
            return false;
        }
        return getIdentifier().equals(((Entity) target).getIdentifier());
    }

    public int hashCode() {
        return id.hashCode();
    }
}

/*
 * $Log: EntityImpl.java,v $
 * Revision 1.8  2007/04/02 12:19:08  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.7  2007/04/02 12:13:20  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.6  2005/09/16 12:41:05  marin_dimitrov
 * 1. Entity::addNfpValue(IRI, Object) split into Entity::addNfpValue(IRI, Identifier) and Entity::addNfpValue(IRI, Value)
 *
 * 2. same for removeNFP
 *
 * Revision 1.5  2005/08/31 12:28:44  vassil_momtchev
 * addNFPValue and removeNFPValue changed back to accept Object. The Value type limits all kind of identifiers (IRI, AnonymousID)
 *
 * Revision 1.4  2005/08/31 09:17:29  vassil_momtchev
 * use Type and Value instead of Object where appropriate bug SF[1276677]
 *
 * Revision 1.3  2005/08/19 08:58:31  marin_dimitrov
 * added removeNFP(key)
 *
 * Revision 1.2  2005/06/22 09:16:05  alex_simov
 * Simplified equals() method. Identity strongly relies on identifier string
 *
 * Revision 1.1  2005/06/01 12:00:32  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.5  2005/05/17 12:42:56  alex
 * immutable Collections.EMPTY_SET instead of a new empty set
 *
 * Revision 1.4  2005/05/17 12:00:00  alex
 * Collections.unmodifiableSet() used instead of new set construction
 *
 * Revision 1.3  2005/05/12 13:46:32  marin
 * changed to an _abstract_ class (not supposed to be used directly but only through subclassing)
 *
 * Revision 1.2  2005/05/11 13:33:58  alex
 * copyright notice updated
 *
 * Revision 1.1  2005/05/11 11:50:56  alex
 * initial commit
 *
 */
