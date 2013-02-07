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

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.omwg.ontology.Relation;
import org.omwg.ontology.RelationInstance;
import org.omwg.ontology.Value;
import org.wsmo.common.Identifier;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.common.exception.SynchronisationException;

import com.ontotext.wsmo4j.factory.IDReference;

public class RelationInstanceImpl extends OntologyElementImpl
        implements RelationInstance {

    private Relation memberOf;

    private Value[] parameterValues;
    
    public RelationInstanceImpl(Identifier thisID, Relation rel)
            throws SynchronisationException, InvalidModelException {
        super(thisID);
        _setRelation(rel);
    }
    
  
    /**
     * Sets the relation this relation instance is
     * an instance of.
     * @param concept The Relation this relation instance
     * is an instance of.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #getRelation()
     */
    public void setRelation(Relation relation)
            throws SynchronisationException, InvalidModelException {
        _setRelation(relation);
    }

    /**
     * Returns the relation this relation instance
     * is an instance of.
     * @return The relation this relation instance
     * is an instance of.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see #setRelation(org.omwg.ontology.Relation)
     */    
    public Relation getRelation() throws SynchronisationException {
        return memberOf;
    }

    /**
     * Returns a set of ParameterValues.
     * @return A set of ParameterValues
     * for all parameters set via <code>setParameterValue</code>.
     */
    public List <Value> listParameterValues() {
        if (parameterValues == null) {
            return Collections.unmodifiableList(new ArrayList <Value> ()); // immutable
        }
        ArrayList <Value> result = new ArrayList <Value>(parameterValues.length);
        for(int i = 0; i < parameterValues.length; i++) {
            result.add(parameterValues[i]);
        }
        return result;
    }

    /**
     * Returns the value of the specified paramter.
     * @param parameter The parameter of interest.
     * @return The value of the specified parameter.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see #setParameterValue(org.omwg.ontology.Parameter, org.omwg.ontology.Value)
     */
    public Value getParameterValue(byte pos)
            throws SynchronisationException, InvalidModelException {
        if (memberOf == null
                || parameterValues == null) {
            throw new InvalidModelException();
        }
        if (pos < 0 
                || pos >= parameterValues.length) {
            throw new IllegalArgumentException();
        }
        return parameterValues[pos];
    }

    /**
     * Sets the value of the specified Parameter to be the specified
     * ParameterValue object. If this method is called more than once for a
     * certain parameter position, only the parameter value added on the last
     * call is preserved - all the rest are discarded.
     * Null values are allowed for removal purposes. 
     * 
     * @param pos
     *            The index of the parameter of interest.
     * @param value
     *            The ParameterValue object to set the specified Parameter's
     *            value to.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    public void setParameterValue(byte pos, Value value)
            throws SynchronisationException, InvalidModelException {
        if (memberOf == null 
                || parameterValues == null) {
            throw new InvalidModelException();
        }
        
        //if relation is not resolved, don't assume anything about arity but extend it like
        //requested.
        if (memberOf instanceof Proxy){
            if(!((IDReference)Proxy.getInvocationHandler(memberOf)).isResolved() &&
                    parameterValues.length<=pos){
                correctValueHolderLength(pos+1);
            }
        }
        int size = 0;
        if (memberOf.listParameters()!=null){
            size = memberOf.listParameters().size();
        }
        if (pos < 0
               || (pos >= parameterValues.length)) {
            throw new IllegalArgumentException("Parameter position must be within arity ("+
                    size+") of relation ("+
                    memberOf.getIdentifier()+ ")");
        }
        
        parameterValues[pos] = value;
    }

    public boolean equals(Object object) {
        if (object == null
                || false == object instanceof RelationInstance) {
            return false;
        }
        return super.equals(object);
    }
    
    /**
     * Ensures that the values holder size is the same as the Relation
     * parameters count.
     * 
     * @param newParamCount
     */
    private void correctValueHolderLength(int newParamCount) {
        Value[] newHolder = new Value[newParamCount];
        System.arraycopy(parameterValues, 
                         0, 
                         newHolder, 
                         0, 
                         Math.min(parameterValues.length, 
                                  newParamCount));
        parameterValues = newHolder;
    }
    
    /**
     * Sets the relation this relation instance is
     * an instance of.
     * @param concept The Relation this relation instance
     * is an instance of.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #getRelation()
     */
    private void _setRelation(Relation relation)
            throws SynchronisationException, InvalidModelException {
        if (relation == null) {
            throw new InvalidModelException();
        }
        int newParamCount = relation.listParameters().size();
        if (false == relation.equals(memberOf)) {
            parameterValues = new Value[newParamCount]; // new relation - new param values
        }
        else {
            if (parameterValues.length != newParamCount) {
                correctValueHolderLength(newParamCount);
            }
        }
        memberOf = relation;
        if (false == memberOf.listRelationInstances().contains(this)) {
            memberOf.addRelationInstance(this);
        }
    }

}

/*
 * $Log: RelationInstanceImpl.java,v $
 * Revision 1.29  2007/04/02 12:19:08  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.28  2007/04/02 12:13:21  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.27  2007/02/22 15:34:06  alex_simov
 * unnecessary check removed from equals()
 *
 * Revision 1.26  2006/03/23 16:12:13  nathaliest
 * moving the anon Id check to the validator
 *
 * Revision 1.25  2006/02/13 11:49:58  nathaliest
 * changed anonId-check error String
 *
 * Revision 1.24  2006/02/13 09:21:21  nathaliest
 * added AnonIds Check
 *
 * Revision 1.23  2006/02/08 15:20:45  holgerlausen
 * added more specific error message
 *
 * Revision 1.22  2005/11/29 16:42:38  holgerlausen
 * if the relation of an instance is not know no error is neccessary on setParameter(i)... (relation arity is then just assumed)
 *
 * Revision 1.21  2005/09/13 08:59:39  vassil_momtchev
 * constructor accept (Identifier, Relation) now
 *
 * Revision 1.20  2005/06/27 09:04:09  alex_simov
 * Object param substituted by Value
 *
 * Revision 1.19  2005/06/22 09:16:06  alex_simov
 * Simplified equals() method. Identity strongly relies on identifier string
 *
 * Revision 1.18  2005/06/03 13:02:12  alex_simov
 * fix
 *
 * Revision 1.17  2005/06/01 12:09:33  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.2  2005/05/17 12:43:32  alex
 * immutable Collections.EMPTY_LIST instead of a new empty list
 *
 * Revision 1.1  2005/05/12 14:56:41  alex
 * initial commit
 *
 */