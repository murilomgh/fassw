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

import org.wsmo.service.ad.*;

public class ActivityDiagramImpl implements ActivityDiagram {

    private ActivityGroup group;
    private ActivityNode startNode;


    public ActivityGroup getActivityGroup() {
        return this.group;
    }

    public void setActivityGroup(ActivityGroup group) {
        if (group == null) {
            throw new IllegalArgumentException();
        }
        this.group = group;
    }

    public ActivityNode getStartNode() {
        return this.startNode;
    }

    public void setStartNode(ActivityNode node) {
        if (node == null) {
            throw new IllegalArgumentException();
        }
        this.startNode = node;
    }

    public boolean equals(Object obj) {
        if (obj == null
                || false == obj instanceof ActivityDiagram) {
            return false;
        }
        ActivityDiagram diagram = (ActivityDiagram)obj;
        if (this.group == null) {
            if (diagram.getActivityGroup() != null) {
                return false;
            }
        }
        else {
            if (false == this.group.equals(diagram.getActivityGroup())) {
                return false;
            }
        }
        if (this.startNode == null) {
            return diagram.getStartNode() == null;
        }
        else {
            return this.startNode.equals(diagram.getStartNode());
        }
    }

}

/*
 * $Log: ActivityDiagramImpl.java,v $
 * Revision 1.1  2006/12/08 17:11:10  vassil_momtchev
 * initial version of the parser
 *
*/
