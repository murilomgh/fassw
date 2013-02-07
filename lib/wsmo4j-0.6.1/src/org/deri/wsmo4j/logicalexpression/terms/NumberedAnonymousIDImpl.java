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

package org.deri.wsmo4j.logicalexpression.terms;


/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */

import java.io.*;

import org.omwg.logicalexpression.terms.*;


/**
 * An interface representing an anonymous numbered identifier
 *
 * @author not attributable
 * @version $Revision: 1.4 $ $Date: 2005/09/21 08:15:39 $
 * @since 0.4.0
 */
public class NumberedAnonymousIDImpl
        implements Serializable, NumberedAnonymousID {

    private static final long serialVersionUID = 3118168739751464544L;

    private byte id;

    public NumberedAnonymousIDImpl(byte id) {
        this.id = id;
    }

    public String toString() {
        return new StringBuffer("_#").append(this.id).toString();
    }

    public byte getNumber() {
        return this.id;
    }

    public void accept(Visitor v) {
        v.visitNumberedID(this);
    }

    /**
     * <p>
     * The <code>equals</code> method implements an equivalence relation
     * on non-null object references. Numbered Anonymous IDs are equal when they
     * have the same number within
     * the same scope, this method only checks the former.
     * </p>
     * <p>
     * It is generally necessary to override the <code>hashCode</code> method whenever this method
     * is overridden.
     * </p>
     * @param o Object of Type Numbered Anonymous ID
     * @return <code>true</code> if this Numbered Anonymous ID's number is the same as the obj
     *          argument's number; <code>false</code> otherwise.
     * @see java.lang.Object#equals(java.lang.Object)
     * @see java.lang.Object#hashCode()
     */
    public boolean equals(Object o) {
        if (o instanceof NumberedAnonymousID &&
            ((NumberedAnonymousID)o).getNumber() == getNumber()) {
            return true;
        }
        return false;
    }

    /**
     * <p>
     * If two objects are equal according to the <code>equals(Object)</code> method, then calling
     * the <code>hashCode</code> method on each of the two objects must produce the same integer
     * result. However, it is not required that if two objects are unequal according to
     * the <code>equals(Object)</code> method, then calling the <code>hashCode</code> method on each of the two
     * objects must produce distinct integer results.
     * </p>
     * <p>
     * This method should be overriden, when the <code>equals(Object)</code> method is overriden.
     * </p>
     * @return number of the Numbered Anonymous ID
     * @see java.lang.Object#hashCode()
     * @see java.lang.Object#equals(Object)
     * @see com.ontotext.wsmo4j.common.NumberedIDImpl#getNumber()
     */
    public int hashCode() {
        return getNumber();
    }

}
/*
 * $Log: NumberedAnonymousIDImpl.java,v $
 * Revision 1.4  2005/09/21 08:15:39  holgerlausen
 * fixing java doc, removing asString()
 *
 * Revision 1.3  2005/09/09 15:51:42  marin_dimitrov
 * formatting
 *
 * Revision 1.2  2005/09/09 06:31:07  holgerlausen
 * added serializeID
 *
 * Revision 1.1  2005/09/02 13:32:45  ohamano
 * move logicalexpression packages from ext to core
 * move tests from logicalexpression.test to test module
 *
 * Revision 1.1  2005/09/02 09:43:27  ohamano
 * integrate wsmo-api and le-api on api and reference implementation level; full parser, serializer and validator integration will be next step
 *
 * Revision 1.2  2005/06/22 14:20:15  alex_simov
 * Copyright header added/updated
 *
 * Revision 1.1  2005/06/01 12:00:32  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.4  2005/05/30 15:06:38  alex
 * toString() delegates to asString()
 *
 * Revision 1.3  2005/05/19 14:21:57  marin
 * fix
 *
 * Revision 1.2  2005/05/19 13:48:03  marin
 * fixed (_# prefix)
 *
 * Revision 1.1  2005/05/19 12:50:11  marin
 * created
 *
 *
 */
