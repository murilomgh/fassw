/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2005, University of Innsbruck, Austria

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


import java.util.Set;

import org.omwg.ontology.Variable;


/**
 * This interface represents quantified logical expressions, i.e. universally and existentially
 * quantified expressions.
 * @author DERI Innsbruck, reto.krummenacher@deri.org
 * @version $Revision: 1.5 $ $Date: 2007/04/02 12:13:15 $
 * @see org.omwg.logicalexpression.Unary
 */
public interface Quantified
        extends Unary {

    /**
     * @return the set of variables that are quantified for the expression
     */
    public Set <Variable> listVariables();
    
    public void setVariables(Set <Variable> variables);
}
/*
 * $Log: Quantified.java,v $
 * Revision 1.5  2007/04/02 12:13:15  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.4  2005/09/21 06:31:55  holgerlausen
 * allowing to set arguments rfe  1290049
 *
 * Revision 1.3  2005/09/09 11:58:19  holgerlausen
 * fixed header logexp no longer extension
 *
 * Revision 1.2  2005/09/09 11:12:12  marin_dimitrov
 * formatting
 *
 * Revision 1.1  2005/09/02 13:32:43  ohamano
 * move logicalexpression packages from ext to core
 * move tests from logicalexpression.test to test module
 *
 * Revision 1.6  2005/08/30 14:14:20  haselwanter
 * Merging LE API to HEAD.
 *
 * Revision 1.5.2.1  2005/08/30 11:50:28  haselwanter
 * Adapting javadocs.
 *
 * Revision 1.5  2005/07/27 15:52:28  ohamano
 * get<Set> to list<Set>
 * get(int i) to find(int i)
 *
 * Revision 1.4  2005/06/22 13:32:01  ohamano
 * change header
 *
 * Revision 1.3  2005/06/20 08:30:03  holgerlausen
 * formating
 *
 * Revision 1.2  2005/06/18 14:06:10  holgerlausen
 * added local LEFactory, updated javadoc, refactored LEVariable > Variable etc. parse(String) for LEFactory is running now
 *
 * Revision 1.1  2005/06/16 13:55:23  ohamano
 * first import
 *
 */
