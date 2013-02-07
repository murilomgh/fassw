/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2004-2005, University of Innsbruck Austria

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


/**
 * Interface or class description
 *
 * <pre>
 * Created on Sep 5, 2005
 * Committed by $Author$
 * </pre>
 *
 * @author Holger Lausen (holger.lausen@deri.org)
 *
 * @version $Revision$ $Date$
 */
public interface SimpleDataValue
        extends DataValue {

}
/*
 * $Log$
 * Revision 1.2  2005/09/09 10:52:20  marin_dimitrov
 * formatting
 *
 * Revision 1.1  2005/09/06 18:23:53  holgerlausen
 * removed createSimpleTypes from LogicalExpressionFactory
 * splited DataValue into simple and complex value classes
 * removed explicit classes for simple datavalues (now all org.omwg.ontology.SimpledataValue)
 * adopted Term visitor to new data structure
 *
 */
