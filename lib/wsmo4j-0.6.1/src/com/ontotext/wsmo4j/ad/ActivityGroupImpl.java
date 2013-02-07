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

import java.util.*;

import org.wsmo.common.*;
import org.wsmo.service.ad.*;

public class ActivityGroupImpl implements ActivityGroup {

    private Identifier id;
    private LinkedHashSet<ActivityNode> nodes;
    private LinkedHashSet<ActivityEdge> edges;
    private LinkedHashSet<ActivityGroup> groups;

    public ActivityGroupImpl(Identifier id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        this.id = id;
        this.nodes = new LinkedHashSet<ActivityNode>();
        this.edges = new LinkedHashSet<ActivityEdge>();
        this.groups = new LinkedHashSet<ActivityGroup>();
    }

    public void addNode(ActivityNode node) {
        if (node == null) {
            throw new IllegalArgumentException();
        }
        this.nodes.add(node);
    }

    public ActivityNode removeNode(ActivityNode node) {
        if (node == null) {
            throw new IllegalArgumentException();
        }
        this.nodes.remove(node);
        return node;
    }

    public Iterator<ActivityNode> listNodes() {
        return Collections.unmodifiableSet(this.nodes).iterator();
    }

    public void addEdge(ActivityEdge edge) {
        if (edge == null) {
            throw new IllegalArgumentException();
        }
        this.edges.add(edge);
    }

    public ActivityEdge removeEdge(ActivityEdge edge) {
        if (edge == null) {
            throw new IllegalArgumentException();
        }
        this.edges.remove(edge);
        return edge;
    }

    public Iterator<ActivityEdge> listEdges() {
        return Collections.unmodifiableSet(this.edges).iterator();
    }

    public void addGroup(ActivityGroup group) {
        if (group == null) {
            throw new IllegalArgumentException();
        }
        this.groups.add(group);
    }

    public ActivityGroup removeGroup(ActivityGroup group) {
        if (group == null) {
            throw new IllegalArgumentException();
        }
        this.groups.remove(group);
        return group;
    }

    public Iterator<ActivityGroup> listGroups() {
        return Collections.unmodifiableSet(this.groups).iterator();
    }


    public Identifier getIdentifier() {
        return this.id;
    }

    public boolean equals(Object obj) {
        if (obj == null
                || false == obj instanceof ActivityGroup) {
            return false;
        }
        return id.equals(((ActivityGroup)obj).getIdentifier());
    }

}

/*
 * $Log: ActivityGroupImpl.java,v $
 * Revision 1.1  2006/12/08 17:11:11  vassil_momtchev
 * initial version of the parser
 *
*/
