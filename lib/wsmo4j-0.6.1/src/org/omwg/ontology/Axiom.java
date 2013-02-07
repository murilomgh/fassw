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

import org.omwg.logicalexpression.*;


/**
 * This interface represents a WSMO axiom.
 *
 * @author not attributable
 * @version $Revision: 1.11 $ $Date: 2007/04/02 12:13:14 $
 */
public interface Axiom
    extends OntologyElement {

    Set <LogicalExpression> listDefinitions();

    void addDefinition(LogicalExpression definition);

    void removeDefinition(LogicalExpression le);

    /**
     * @link aggregationByValue
     * @supplierCardinality 1..*
     * @supplierRole defined-by
     * @directed
     */
    /*# LogicalExpression lnkLogicalExpression; */
}

/*
 * $Log: Axiom.java,v $
 * Revision 1.11  2007/04/02 12:13:14  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.10  2005/09/02 13:32:43  ohamano
 * move logicalexpression packages from ext to core
 * move tests from logicalexpression.test to test module
 *
 * Revision 1.9  2005/06/01 10:10:40  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.4  2005/05/12 14:44:26  marin
 * javadoc, header, footer, etc
 *
 */
