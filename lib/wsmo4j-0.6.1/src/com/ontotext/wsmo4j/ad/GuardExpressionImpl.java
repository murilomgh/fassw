/*
 AD extension - a WSMO API and Reference Implementation

 Copyright (c) 2004-2006, OntoText Lab. / SIRMA

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

package com.ontotext.wsmo4j.ad;

import org.omwg.logicalexpression.*;
import org.wsmo.service.ad.*;

public class GuardExpressionImpl implements GuardExpression {

    private LogicalExpression le;

    public GuardExpressionImpl(LogicalExpression le) {
        this.le = le;
    }

    public LogicalExpression getLogicalExpression() {
        return this.le;
    }

    public void setLogicalExpression(LogicalExpression le) {
        this.le = le;
    }

    public int hashCode() {
        if (this.le == null) {
            return -1;
        }
        return le.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null
                || false == obj instanceof GuardExpression) {
            return false;
        }
        GuardExpression ge = (GuardExpression)obj;
        if (this.le == null) {
            return ge.getLogicalExpression() == null;
        }
        return this.le.equals(ge.getLogicalExpression());
    }

}

/*
 * $Log: GuardExpressionImpl.java,v $
 * Revision 1.1  2006/12/08 17:11:12  vassil_momtchev
 * initial version of the parser
 *
*/
