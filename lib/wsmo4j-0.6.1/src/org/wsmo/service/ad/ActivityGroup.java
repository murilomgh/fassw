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

package org.wsmo.service.ad;

import java.util.*;
import org.wsmo.common.Identifier;

public interface ActivityGroup {

    /**
     */
    public abstract void addNode(ActivityNode node);

    /**
     */
    public abstract ActivityNode removeNode(ActivityNode node);

    /**
     */
    public abstract Iterator<ActivityNode> listNodes();

    /**
     */
    public abstract void addEdge(ActivityEdge edge);

    /**
     */
    public abstract ActivityEdge removeEdge(ActivityEdge edge);

    /**
     */
    public abstract Iterator<ActivityEdge> listEdges();

    /**
     */
    public abstract void addGroup(ActivityGroup group);

    /**
     */
    public abstract ActivityGroup removeGroup(ActivityGroup group);

    /**
     */
    public abstract Iterator<ActivityGroup> listGroups();


    /**
     */
    public abstract Identifier getIdentifier();


}

/*
 * $Log: ActivityGroup.java,v $
 * Revision 1.1  2006/12/08 17:11:08  vassil_momtchev
 * initial version of the parser
 *
*/
