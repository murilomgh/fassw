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
 * Defines a WSMO relation instance
 *
 * @see Relation
 * @author not attributable
 * @version $Revision: 1.18 $ $Date: 2007/04/02 12:13:14 $
 */
public interface RelationInstance
    extends OntologyElement {

    /**
     * Sets the relation this relation instance is an instance of.
     * @param memberOf The Relation this relation instance is an instance of.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #getRelation()
     */
    void setRelation(Relation memberOf)
        throws SynchronisationException, InvalidModelException;

    /**
     * Returns the relation this relation instance is an instance of.
     * @return The relation this relation instance is an instance of.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see #setRelation(org.omwg.ontology.Relation)
     */
    Relation getRelation()
        throws SynchronisationException;

    /**
     * Returns a list of ParameterValues.
     * @return A list of ParameterValues
     * for all parameters set via <code>setParameterValue</code>.
     */
    public List <Value> listParameterValues();

    /**
     * Returns the value of the specified paramter.
     * @param pos The index of the parameter of interest.
     * @return The value of the specified parameter.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @see #setParameterValue(byte, Value)
     */
    Value getParameterValue(byte pos)
        throws SynchronisationException, InvalidModelException;

    /**
     * Sets the value of the specieifed Parameter to be the specified ParameterValue object.
     * @param pos The index of the Parameter of interest.
     * @param value The ParameterValue object to set the specified Parameter's value to.
     * @throws org.wsmo.common.exception.SynchronisationException
     * @throws org.wsmo.common.exception.InvalidModelException
     * @see #getParameterValue(byte pos)
     */
    void setParameterValue(byte pos, Value value )
        throws SynchronisationException, InvalidModelException;

    /**
     * @supplierCardinality 0..*
     * @supplierRole specifies-value-for
     * @directed
     */
    /*# Parameter lnkParameter; */
}

/*
 * $Log: RelationInstance.java,v $
 * Revision 1.18  2007/04/02 12:13:14  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.17  2005/09/27 13:09:59  marin_dimitrov
 * javadoc
 *
 * Revision 1.16  2005/09/21 08:15:39  holgerlausen
 * fixing java doc, removing asString()
 *
 * Revision 1.15  2005/06/24 12:51:07  marin_dimitrov
 * now use Value for param/attr values (was: Object)
 *
 * Revision 1.14  2005/06/01 10:22:22  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.6  2005/05/13 15:38:26  marin
 * fixed javadoc errors
 *
 * Revision 1.5  2005/05/13 15:02:41  marin
 * javadoc, header, footer, etc
 *
 * Revision 1.7  2005/05/13 13:58:25  marin
 * javadoc, header, footer, etc
 *
 */
