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

package org.omwg.ontology;


import java.util.*;

import org.wsmo.common.*;
import org.wsmo.common.exception.*;


/**
 * Defines WSMO attribute. Attribute could be specified as Concept 
 * Attribute (attribute which has no {link WsmlDataType} as range) or 
 * Data Attribute.
 * 
 * @author not attributable
 * @see Concept
 * @author not attributable
 * @version $Revision: 1.19 $Date:  
 */
public interface Attribute
        extends Entity {

    /**
     * Returns the type of this attribute.
     * @return true (constraining) / false (inferring)
     */
    boolean isConstraining();

    /**
     * Sets the type of this attribute.
     * @param constraining true (constraining) / false (inferring)
     */
    void setConstraining(boolean constraining);

    /**
     * Returns the minimal cardinality constraint of this attribute.
     * The cardinality option is only available to Concept Attributes.
     * @return number indicating the minimal cardinality constraint
     */
    int getMinCardinality();

    /**
     * Sets the minimal cardinality constraint of this attribute.
     * The cardinality option is only available to Concept Attributes.
     * @param min  number indicating the minimal cardinality constraint
     */
    void setMinCardinality(int min);

    /**
     * Returns the maximal cardinality constraint of this attribute.
     * The cardinality option is only available to Concept Attributes.
     * @return number indicating the maximal cardinality constraint
     */
    int getMaxCardinality();

    /**
     * Sets the maximal cardinality constraint of this attribute.
     * The cardinality option is only available to Concept Attributes.
     * For specifiying unrestricted MAX cardinality use: Integer.MAX_VALUE.
     * @return max  number indicating the maximal cardinality constraint
     */
    void setMaxCardinality(int max);

    /**
     * Returns the {@link Concept} described by this attribute.
     * @return Concept
     */
    Concept getConcept();

    /**
     * Returns the list of allowed {@link Type} for the current attribute.
     * @return Set of Type objects
     */
    Set <Type> listTypes();

    /**
     * Adds a new {@link Type} as range for this attribute.
     * @param type  the Type to be added
     * @throws InvalidModelException
     */
    void addType(Type type)
            throws InvalidModelException;

    /**
     * Removes a {@link Type} for this attribute.
     * @param type  the Type to be removed
     * @throws InvalidModelException
     */
    void removeType(Type type)
            throws InvalidModelException;

    /**
     * Returns if this attribute is defined as reflexive.
     * This option is available only for the concept attribtues. 
     * @return
     */
    boolean isReflexive();

    /**
     * Sets this attribute whether to be reflexive.
     * This option is available only for the concept attribtues.
     * @param reflexive  is reflexive
     */
    void setReflexive(boolean reflexive);

    /**
     * Returns if this attribute is defined as symmetric.
     * When an attribute is specified as being symmetric, 
     * this means that if an individual a has a symmetric attribute 
     * att with value b, then b also has attribute att with value a.
     * This option is available only for the concept attribtues. 
     * @return
     */
    boolean isSymmetric();

    /**
     * Sets this attribute whether to be symmetric.
     * This option is available only for the concept attribtues.
     * @param symmetric  is reflexive
     */
    void setSymmetric(boolean symmetric);

    /**
     * Returns if this attribute is defined as transitive.
     * When an attribute is specified as being transitive, 
     * this means that if three individuals a, b and c are related 
     * via a transitive attribute att in such a way: a att b att c 
     * then c is also a value for the attribute att at a: a att c.
     * This option is available only for the concept attribtues. 
     * @return
     */
    boolean isTransitive();

    /**
     * Sets this attribute whether to be transitive.
     * This option is available only for the concept attribtues.
     * @param trans  is reflexive
     */
    void setTransitive(boolean trans);

    /**
     * Returns the inverse attribute of this attribute.
     * When an attribute is specified as being the inverse of another 
     * attribute, this means that if an individual a has an attribute 
     * att1 with value b and att1 is the inverse of a certain 
     * attribute att2, then it is inferred that b has an attribute 
     * att2 with value a.
     * This option is available only for the concept attribtues.
     * @return null or the inverse attribute's identifier
     */
    Identifier getInverseOf();

    /**
     * Sets this attribute to be inverse of another attribute. 
     * @param id  attribute to be set as inverse of or null to remove 
     * the current inverse attribute
     */
    void setInverseOf(Identifier id);

    /**
     * @supplierCardinality 0..*
     * @directed
     * @supplierRole has-type*/
    /*# Type lnkType; */
}

/*
 * $Log: Attribute.java,v $
 * Revision 1.19  2007/04/02 12:13:14  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.18  2006/02/16 09:47:30  vassil_momtchev
 * setInverseOf uses Identifier instead of Attribute
 *
 * Revision 1.17  2006/02/15 10:51:48  holgerlausen
 * added javadoc hint to max cardinality
 *
 * Revision 1.16  2005/09/26 08:36:20  vassil_momtchev
 * javadoc, header and footer added
 *
*/
