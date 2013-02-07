/*
 wsmo4j extension - a Choreography API and Reference Implementation

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

package org.wsmo.service.rule;

import org.omwg.logicalexpression.*;

/**
 * TODO: Improve the javadoc description
 * Extends CompoundFact and Relation Instance since only updates on
 * relation instances are allowed.
 *
 * @author James Scicluna
 * @author Thomas Haselwanter
 * @author Vassil Momtchev
 *
 * Created on 02-Feb-2006
 * Committed by $Author: vassil_momtchev $
 *
 * $Source: /cvsroot/wsmo4j/ext/choreography/src/api/org/wsmo/service/rule/RelationFact.java,v $,
 * @version $Revision: 1.1 $ $Date: 2006/10/24 14:11:48 $
 */

public interface RelationFact extends CompoundFact {

    /**
     */
    public Atom getAtom();

    /**
     */
    public void setAtom(Atom atom);

}

/*
 * $Log: RelationFact.java,v $
 * Revision 1.1  2006/10/24 14:11:48  vassil_momtchev
 * choreography/orchestration rules refactored. different types where appropriate now supported
 *
 * Revision 1.3  2006/04/17 07:44:28  vassil_momtchev
 * association level between RelationFact and Atom decreased from inheritance to containement
 *
 * Revision 1.2  2006/02/10 15:30:35  vassil_momtchev
 * isEmpty method shifted from MoleculeFact to CompoundFact; log footer added
 *
 */
