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
package org.deri.wsmo4j.io.parser.wsml;

import org.omwg.logicalexpression.terms.*;
import org.omwg.ontology.*;

public class TempVariable implements Variable{
    int i;
    TempVariable (int i){
        this.i=i;
    }
    public String getName() {
        return "temp"+i;
    }

    public void accept(Visitor v) {
        v.visitVariable(this);
    }
    
    public String toString(){
        return "?temp"+i;
    }
    
}

/*
 *$Log$
 *Revision 1.3  2005/11/28 15:12:05  vassil_momtchev
 *merged with branch parser_refactor
 *
 *Revision 1.2.2.2  2005/11/22 08:58:19  holgerlausen
 *update such that the whole of le fits in the new structure
 *
 *Revision 1.2  2005/11/09 11:28:54  holgerlausen
 *fixed functionsymbol2Atom rewriting (during parsing for comparissons and toString())
 *
 *Revision 1.1  2005/11/08 08:37:12  holgerlausen
 *fixed RFE 665349 (built in as predicates)
 *
 */