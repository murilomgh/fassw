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
 * Defines WSMO parameter of a relation.
 * 
 * @author not attributable
 * @see Relation
 * @author not attributable
 * @version $Revision: 1.17 $Date:  
 */
public interface Parameter {

    /**
     * Returns the type of this parameter.
     * @return true (constraining) / false (inferring)
     */
    boolean isConstraining();

    /**
     * Sets the type of this parameter.
     * @param constraining true (constraining) / false (inferring)
     */
    void setConstraining(boolean constraining);

    /**
     * Returns the {@link Relation} to which this parameter is assigned.
     * @return
     */
    Relation getRelation();

    /**
     * Adds a new {@link Type} as range for this parameter.
     * @param type  the Type to be added
     * @throws InvalidModelException
     */
    void addType(Type type) throws InvalidModelException;

    /**
     * Removes a {@link Type} for this parameter.
     * @param type  the Type to be removed
     * @throws InvalidModelException
     */
    void removeType(Type type);

    /**
     * Returns the list of allowed {@link Type} for the current parameter.
     * @semantics result is either a Concept or a WsmlDatatype
     * @return Set of Type objects
     */
    Set <Type> listTypes();

    /**
     * @supplierCardinality 1
     * @directed
     * @supplierRole has-type
     */
    /*# Concept lnkConcept; */

    /**
     * @supplierCardinality 1
     * @supplierRole has-datatype
     * @directed
     */
    /*# WsmlDataType lnkWsmlDataType; */
}

/*
 * $Log: Parameter.java,v $
 * Revision 1.17  2007/04/02 12:13:14  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.16  2006/02/13 22:49:23  nathaliest
 * - changed concept.createAttribute() and Parameter.addType to throw InvalidModelException.
 * - small change at check AnonIds in ConceptImpl
 *
 * Revision 1.15  2005/09/26 08:36:20  vassil_momtchev
 * javadoc, header and footer added
 *
*/
