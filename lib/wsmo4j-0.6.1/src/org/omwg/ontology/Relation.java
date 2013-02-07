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


import java.util.*;

import org.wsmo.common.exception.*;


/**
 * Defines the requirements for an object that
 * represents a WSMO relation.
 *
 * @see RelationInstance
 * @author not attributable
 * @version $Revision: 1.18 $ $Date: 2007/04/02 12:13:14 $
 */
public interface Relation
    extends OntologyElement {

    /**
     * Adds a new relation to the set of super-relation of this relation
     * <i>The super-relation will also add this relation to the list of its sub-relations</i>
     * @param superRel The new super-relation to be added.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeSuperRelation(Relation superRel)
     */
    void addSuperRelation(Relation superRel)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes a relation from the set of super-relations of this relation
     * <i>The super-relation will also remove this relation from the list of its sub-relations</i>
     * @param superRel The super-relation to be removed
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    void removeSuperRelation(Relation superRel)
        throws SynchronisationException, InvalidModelException;

    /**
     * Lists the super-relations of this relation
     * @return The set of super-relation for this relation
     * @throws org.wsmo.common.exception.SynchronisationException
     */
    Set <Relation> listSuperRelations()
        throws SynchronisationException;

    /**
     * Adds a new relation to the set of sub-relations of this relation
     * <i>The sub-relation will also add this relation to the list of its super-relations</i>
     * @param subRel The new sub-relation to be added.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeSubRelation(Relation subRel)
     */
    void addSubRelation(Relation subRel)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes a relation from the set of sub-relations of this relation
     * <i>The sub-relation will also remove this relation from the list of its super-relations</i>
     * @param subRel The sub-concept to be removed
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     */
    void removeSubRelation(Relation subRel)
        throws SynchronisationException, InvalidModelException;

    /**
     * Lists the sub-relations of this relation
     * @return The set of sub-relations defined by this ontology.
     * @throws org.wsmo.common.exception.SynchronisationException
     */
    Set <Relation> listSubRelations()
        throws SynchronisationException;

    /**
     * The positioning of the parameters is zero-based, i.e. the first parameter
     * is at position 0. The initial order of parameters creation must be
     * succesive, starting from the 0th position. Any other order raises an
     * InvalidModelException. If this method is called more than once for a
     * certain position, only the parameter created on the last call is
     * preserved - all the rest are discarded.
     * @param pos The position of the new Parameter for this Relation
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeParameter(Parameter param)
     * @see #removeParameter(byte pos)
     * @see #getParameter(byte pos)
     */
    Parameter createParameter(byte pos)
        throws SynchronisationException, InvalidModelException;

    /**
     * retrieve the parameter at a given position
     * Note: the parameter must already exist (e.g. a call to createParameter() must precede this call)
     * @param pos The position of the parameter in interest
     * @return a reference to the Parameter
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see #createParameter(byte pos)
     */
    Parameter getParameter(byte pos)
        throws SynchronisationException;

    /**
     * The positioning of the parameters is zero-based, i.e. the first parameter
     * is at position 0. The removal of parameters can only be performed from the
     * end of the list. Trying to remove a parameter followed by another parameter
     * will raise an exception. 
     * @param param The parameter to be removed
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeParameter(byte pos)
     */
    void removeParameter(Parameter param)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes a parameter from this relation's list of attributes.
     * @param pos The position of the parameter to be removed
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeParameter(Parameter param)
     */
    void removeParameter(byte pos)
        throws SynchronisationException, InvalidModelException;;

    /**
     * Returns a list of this relation's parameters
     * @return The list of this relation's parameters
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see Parameter
     */
    List <Parameter> listParameters()
        throws SynchronisationException;

    /**
     * Adds a new RelationInstance to the set of instances of this relation
     * <i>The relation instance will also set this relation as the relation it is an instance of</i>
     * @param relInst The new instance to be added.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #removeRelationInstance(RelationInstance relInst)
     * @see RelationInstance#setRelation(Relation memberOf)
     */
    void addRelationInstance(RelationInstance relInst)
        throws SynchronisationException, InvalidModelException;

    /**
     * Removes a relation instance from the set of instances of this relation
     * <i>The relation instance will also set <i>null</i> for as the relation it is an instance of</i>
     * @param relInst The instance to be removed
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see RelationInstance#setRelation(Relation memberOf)
     */
    void removeRelationInstance(RelationInstance relInst)
        throws SynchronisationException, InvalidModelException;

    /**
     * Returns a list of this relation's instances
     * @return The list of this relation's instances
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see RelationInstance
     */
    Set <RelationInstance> listRelationInstances()
        throws SynchronisationException;

//DO NOT EDIT below this line

    /**
     * @supplierCardinality 0..*
     * @directed
     * @supplierRole super-relation
     */
    /*# Relation lnkRelation; */

    /**
     * @supplierCardinality 0..*
     * @clientCardinality 1
     * @supplierRole has-parameter
     * @clientRole defined-in
     * @link aggregationByValue
     */
    /*# Parameter lnkParameter; */

    /**
     * @supplierCardinality 0..*
     * @supplierRole has-instance
     * @clientCardinality 0..1
     * @clientRole has-relation
     */
    /*# RelationInstance lnkRelationInstance; */
}

/*
 * $Log: Relation.java,v $
 * Revision 1.18  2007/04/02 12:13:14  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.17  2006/01/16 13:31:52  vassil_momtchev
 * java doc fixed
 *
 * Revision 1.16  2005/06/01 10:22:06  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.8  2005/05/13 15:38:26  marin
 * fixed javadoc errors
 *
 * Revision 1.7  2005/05/13 13:58:25  marin
 * javadoc, header, footer, etc
 *
 */
