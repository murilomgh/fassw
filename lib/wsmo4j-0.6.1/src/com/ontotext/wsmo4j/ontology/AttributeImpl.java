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

import org.omwg.ontology.Attribute;
import org.omwg.ontology.Concept;
import org.omwg.ontology.Type;
import org.wsmo.common.Identifier;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.common.exception.SynchronisationException;

import com.ontotext.wsmo4j.common.EntityImpl;


public class AttributeImpl extends EntityImpl
        implements Attribute {

    private Concept domain;
    
    private LinkedHashSet <Type> ranges;
    
    private int minCardinality = 0;
    
    private int maxCardinality = Integer.MAX_VALUE;

    private boolean constraining = false,
                    reflexive = false,
                    symmetric = false,
                    transitive = false;

    private Identifier inverseAttribute = null;
    
    public AttributeImpl(Identifier thisID, Concept domain) {
        super(thisID);
        this.domain = domain;
        
        ranges = new LinkedHashSet <Type> ();
    }

    public Concept getConcept() throws SynchronisationException {
        return this.domain;
    }
    
    public void removeFromConcept() throws InvalidModelException {
        if (domain == null)
            return;
        Concept old = domain;
        domain = null;
        old.removeAttribute(this);
    }
    
    public boolean isConstraining() {
        return constraining;
    }
    
    public void setConstraining(boolean constraining) {
        this.constraining = constraining;
    }
    
    public int getMinCardinality() {
        return this.minCardinality;
    }
    
    public void setMinCardinality(int min) {
        this.minCardinality = min;
    }
    
    public int getMaxCardinality() {
        return this.maxCardinality;
    }
    
    public void setMaxCardinality(int max) {
        this.maxCardinality = max;
    }
    
    public Set <Type> listTypes() {
        return Collections.unmodifiableSet(ranges);
    }

    public void addType(Type type) throws InvalidModelException {
        if (type == null) {
            throw new IllegalArgumentException(); 
        }
        ranges.add(type);
    }
    
    public void removeType(Type type) throws InvalidModelException {
        if (type == null) {
            throw new IllegalArgumentException(); 
        }
        ranges.remove(type);
    }
    
    public boolean isReflexive() {
        return this.reflexive;
    }

    public void setReflexive(boolean reflexive) {
        this.reflexive = reflexive;
    }

    public boolean isSymmetric() {
        return symmetric;
    }

    public void setSymmetric(boolean symmetric) {
        this.symmetric = symmetric;
    }

    public boolean isTransitive() {
        return this.transitive;
    }

    public void setTransitive(boolean trans) {
        this.transitive = trans;
    }

    public Identifier getInverseOf() {
        return this.inverseAttribute;
    }

    public void setInverseOf(Identifier id) {       
        this.inverseAttribute = id;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true; // instance match
        }
        if (object == null
                || false == object instanceof Attribute) {
            return false;
        }
        if (!getConcept().equals(((Attribute) object).getConcept())) {
            return false;
        }
        
        return super.equals(object);
    }
    
}

/*
 * $Log: AttributeImpl.java,v $
 * Revision 1.32  2007/04/02 12:13:21  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.31  2006/11/17 10:52:41  vassil_momtchev
 * conceptImpl now uses attributeImpl, when attribute is removed to reset the domain of the attribute
 *
 * Revision 1.30  2006/08/07 10:53:00  vassil_momtchev
 * getConcept() method checks if this attribute instance was already removed from the Concept
 *
 * Revision 1.29  2006/03/23 16:12:13  nathaliest
 * moving the anon Id check to the validator
 *
 * Revision 1.28  2006/02/16 09:56:50  vassil_momtchev
 * setInverseOf(Attribute) changed to setInverseOf(Identifier)
 *
 * Revision 1.27  2006/02/13 11:49:58  nathaliest
 * changed anonId-check error String
 *
 * Revision 1.26  2006/02/13 09:21:21  nathaliest
 * added AnonIds Check
 *
 * Revision 1.25  2005/09/26 13:04:39  vassil_momtchev
 * setInverseOf is symmetric now
 *
 * Revision 1.24  2005/09/15 09:51:51  vassil_momtchev
 * changes from today are discarded
 *
 * Revision 1.22  2005/09/13 11:15:54  vassil_momtchev
 * removed method self call (stack overflow)
 *
 * Revision 1.21  2005/09/13 11:12:21  vassil_momtchev
 * switch back setInverseOf to not be symmetric (proxy problem)
 *
 * Revision 1.20  2005/09/13 09:53:44  alex_simov
 * bugfix in setInverseOf() - null argument is now supported
 *
 * Revision 1.19  2005/09/12 12:23:11  vassil_momtchev
 * setInverseOf changed to be symetric for both attributes
 *
 * Revision 1.18  2005/07/05 12:46:17  alex_simov
 * attributes refactored
 *
 * Revision 1.17  2005/06/22 09:16:05  alex_simov
 * Simplified equals() method. Identity strongly relies on identifier string
 *
 * Revision 1.16  2005/06/01 12:09:33  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.3  2005/05/17 12:04:57  alex
 * Collections.unmodifiableSet() used instead of new set construction
 *
 * Revision 1.2  2005/05/12 12:06:36  alex
 * addType()/removeType() now throw InvalidModelException
 *
 * Revision 1.1  2005/05/12 09:45:49  alex
 * initial commit
 *
 */
