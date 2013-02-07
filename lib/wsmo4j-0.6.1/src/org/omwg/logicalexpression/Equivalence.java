/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2005 University of Innsbruck, Austria
               2005 National University of Ireland, Galway

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

package org.omwg.logicalexpression;


/**
 * This interface represents specific kind of <code>Binary</code>.
 * A equation whose operator is equals.
 *
 * @author DERI Innsbruck, reto.krummenacher@deri.org
 * @author DERI Innsbruck, holger.lausen@deri.org
 * @author DERI Innsbruck, thomas.haselwanter@deri.org
 * @version $Revision$ $Date$
 */
public interface Equivalence
        extends Binary {

}
/*
 * $Log$
 * Revision 1.3  2005/09/09 11:53:21  holgerlausen
 * fixed exidential GPL header to correct LGPL header
 *
 * Revision 1.2  2005/09/09 11:12:12  marin_dimitrov
 * formatting
 *
 * Revision 1.1  2005/09/07 13:13:28  holgerlausen
 * Equation -> Equivalence (the former sounds more like an atom (a=b))
 *
 */