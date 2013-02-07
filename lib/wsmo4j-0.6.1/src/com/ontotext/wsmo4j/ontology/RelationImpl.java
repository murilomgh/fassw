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

import java.util.*;

import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;


public class RelationImpl extends OntologyElementImpl
        implements Relation {

    private LinkedHashSet <Relation> superRelations;
    
    private LinkedHashSet <Relation> subRelations;
    
    private LinkedHashSet <RelationInstance> relInstances;
    
    private final static String ERROR_CYCLE = "Cycle in relation hierarchy detected!";
    
    private List <Parameter> parameters;

    public RelationImpl(Identifier thisID) {
        super(thisID);
        superRelations = new LinkedHashSet <Relation> ();
        subRelations = new LinkedHashSet <Relation> ();
        relInstances = new LinkedHashSet <RelationInstance> ();
        parameters = new LinkedList <Parameter> ();
    }

    public Set <Relation> listSuperRelations() throws SynchronisationException {
        return Collections.unmodifiableSet(superRelations);
    }

    public void addSuperRelation(Relation relation)
            throws SynchronisationException, InvalidModelException {
        if (relation == null) {
            throw new InvalidModelException();
        }
        if (checkInheritanceCycles(relation)) {
            throw new InvalidModelException(ERROR_CYCLE);
        }
        superRelations.add(relation);
        // establishing the reverse connection
        if (false == relation.listSubRelations().contains(this)) { 
            relation.addSubRelation(this);
        }
    }

    public void removeSuperRelation(Relation relation)
            throws SynchronisationException, InvalidModelException {
        if (relation == null) {
            throw new InvalidModelException();
        }
        superRelations.remove(relation);
        // removing the reverse connection
        if (relation.listSubRelations().contains(this)) { 
            relation.removeSubRelation(this);
        }
    }

    public Set <RelationInstance> listRelationInstances() throws SynchronisationException {
        return Collections.unmodifiableSet(relInstances);
    }

    public void addRelationInstance(RelationInstance instance)
            throws SynchronisationException, InvalidModelException {
        if (instance == null) {
            throw new InvalidModelException();
        }
        relInstances.add(instance);
        if (instance.getRelation() != this) {
            instance.setRelation(this);
        }
    }

    public void removeRelationInstance(RelationInstance instance)
            throws SynchronisationException, InvalidModelException {
        if (instance == null) {
            throw new InvalidModelException();
        }
        relInstances.remove(instance);
    }
    
    public Set <Relation> listSubRelations() throws SynchronisationException {
        return Collections.unmodifiableSet(subRelations);
    }

    public void addSubRelation(Relation relation)
            throws SynchronisationException, InvalidModelException {
        if (relation == null) {
            throw new InvalidModelException();
        }
        subRelations.add(relation);
        // establishing the reverse connection
        if (false == relation.listSuperRelations().contains(this)) { 
            relation.addSuperRelation(this);
        }
    }

    public void removeSubRelation(Relation relation)
            throws SynchronisationException, InvalidModelException {
        if (relation == null) {
            throw new InvalidModelException();
        }
        subRelations.remove(relation);
        // removing the reverse connection
        if (relation.listSuperRelations().contains(this)) { 
            relation.removeSuperRelation(this);
        }
    }
    
    public List <Parameter> listParameters() throws SynchronisationException {
        return Collections.unmodifiableList(parameters);
    }

    /**
     * The positioning of the parameters is zero-based, i.e. the first parameter
     * is at position 0. The initial order of parameters creation must be
     * succesive, starting from the 0th position. Any other order raises an
     * InvalidModelException. If this method is called more than once for a
     * certain position, only the parameter created on the last call is
     * preserved - all the rest are discarded.
     * 
     * @param pos
     *            The position of the new Parameter for this Relation
     * @throws org.wsmo.common.exception.InvalidModelException
     * @return The newly created Parameter
     */
    public Parameter createParameter(byte pos)
            throws SynchronisationException, InvalidModelException {
        if (pos < 0 || pos > parameters.size()) {
            throw new InvalidModelException();
        }
        Parameter param = new ParameterImpl(this);
        if (pos == parameters.size()) {
            parameters.add(param);
        }
        else {
            parameters.set(pos, param);
        }
        return param;
    }
    
    /**
     * retrieve the parameter at a given position
     * Note: the parameter must already exist (e.g. a call to createParameter() must precede this call)
     * @param pos The position of the parameter in interest
     * @return a reference to the Parameter
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see #createParameter(byte pos)
     */
    public Parameter getParameter(byte pos)
            throws SynchronisationException {
        if (pos < 0 
                || pos > parameters.size()-1) {
            throw new IllegalArgumentException();
        }
        return parameters.get(pos);
    }

    /**
     * The positioning of the parameters is zero-based, i.e. the first parameter
     * is at position 0. The removal of parameters can only be performed from the
     * end of the list. Trying to remove a parameter followed by another parameter
     * will raise an exception. 
     * @param param The parameter to be removed 
     * @throws java.lang.IllegalArgumentException
     */
    
    public void removeParameter(Parameter param)
            throws SynchronisationException, InvalidModelException {
        if (parameters.size() == 0 
                || false == parameters.get(parameters.size()-1).equals(param)) {
            throw new InvalidModelException();
        }
        parameters.remove(param);
    }
    
    /**
     * The positioning of the parameters is zero-based, i.e. the first parameter
     * is at position 0. The removal of parameters can only be performed from the
     * end of the list. Trying to remove a parameter followed by another parameter
     * will raise an exception. 
     * @param pos The position of the parameter to be removed 
     * @throws java.lang.IllegalArgumentException
     */
    
    public void removeParameter(byte pos) 
            throws SynchronisationException, InvalidModelException {
        if (pos < 0 || parameters.size() != pos+1) {
            throw new InvalidModelException();
        }
        parameters.remove(pos);
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true; // instance match
        }
        if (object == null 
                || false == object instanceof Relation) {
            return false;
        }
        return super.equals(object);
    }
    
    private boolean checkInheritanceCycles(Relation relation) {
        if (relation.equals(this))
            return true;
        
        for (Iterator i = relation.listSuperRelations().iterator(); i.hasNext();) {
            Relation superRelation = (Relation) i.next();
            if (checkInheritanceCycles(superRelation))
                return true;
        }
        
        return false;
    }
    
}

/*
 * $Log: RelationImpl.java,v $
 * Revision 1.21  2007/04/02 12:13:21  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.20  2006/03/23 16:12:13  nathaliest
 * moving the anon Id check to the validator
 *
 * Revision 1.19  2006/02/27 10:22:58  nathaliest
 * added anonId check at relation.addXXXRelation(Relation)
 *
 * Revision 1.18  2005/09/15 14:59:41  vassil_momtchev
 * cycles in the inheritance hierarchy are not allowed - checkInheritanceCycles method implemented; imports organized;
 *
 * Revision 1.17  2005/06/25 13:16:50  damyan_rm
 * fixed getParameter() to avoid throwing Exception in case that index is correct
 *
 * Revision 1.16  2005/06/22 09:16:06  alex_simov
 * Simplified equals() method. Identity strongly relies on identifier string
 *
 * Revision 1.15  2005/06/03 13:02:12  alex_simov
 * fix
 *
 * Revision 1.14  2005/06/01 12:09:33  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.3  2005/05/17 12:04:57  alex
 * Collections.unmodifiableSet() used instead of new set construction
 *
 * Revision 1.2  2005/05/16 08:49:31  alex
 * implementation method getParameter(byte) added
 *
 * Revision 1.1  2005/05/12 09:20:13  alex
 * initial commit
 *
 */
