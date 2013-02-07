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

package com.ontotext.wsmo4j.ontology;

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.omwg.ontology.Parameter;
import org.omwg.ontology.Relation;
import org.omwg.ontology.Type;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.common.exception.SynchronisationException;


public class ParameterImpl implements Parameter {

    private Relation domain;
    
    private LinkedHashSet <Type> ranges;
    
    private boolean constraining = false; /* RFE 1249611 */

    public ParameterImpl(Relation domain) {

        assert null != domain;
        this.domain = domain;
        ranges = new LinkedHashSet <Type>();
    }

    public Relation getRelation() throws SynchronisationException {
        return domain;
    }

    public void addType(Type type) throws InvalidModelException {
        if (type == null) {
            throw new IllegalArgumentException();
        }
        ranges.add(type);
    }

    public void removeType(Type type) {
        if (type == null) {
            throw new IllegalArgumentException();
        }
        ranges.remove(type);
    }

    public Set <Type> listTypes() {
        return Collections.unmodifiableSet(ranges);
    }

    public boolean isConstraining() {
        return constraining;
    }

    public void setConstraining(boolean constraining) {
        this.constraining = constraining;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true; // instance match
        }

        return false;
    }
    
}

/*
 * $Log: ParameterImpl.java,v $
 * Revision 1.22  2007/04/02 12:13:21  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.21  2006/03/23 16:12:13  nathaliest
 * moving the anon Id check to the validator
 *
 * Revision 1.20  2006/02/13 22:49:22  nathaliest
 * - changed concept.createAttribute() and Parameter.addType to throw InvalidModelException.
 * - small change at check AnonIds in ConceptImpl
 *
 * Revision 1.19  2006/02/13 11:49:58  nathaliest
 * changed anonId-check error String
 *
 * Revision 1.18  2006/02/13 09:21:21  nathaliest
 * added AnonIds Check
 *
 * Revision 1.17  2005/12/06 13:03:03  vassil_momtchev
 * to be equal two parameters they need to have same relations, same ranges and same position as well. only instance match could be evaluated without to introduce a position method in Parameter interface
 *
 * Revision 1.16  2005/08/31 09:17:30  vassil_momtchev
 * use Type and Value instead of Object where appropriate bug SF[1276677]
 *
 * Revision 1.15  2005/08/08 09:26:56  marin_dimitrov
 * constraining=false by default (RFE 1249611)
 *
 * Revision 1.14  2005/06/01 12:09:33  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.2  2005/05/17 12:04:57  alex
 * Collections.unmodifiableSet() used instead of new set construction
 *
 * Revision 1.1  2005/05/12 08:21:03  alex
 * initial commit
 *
 */