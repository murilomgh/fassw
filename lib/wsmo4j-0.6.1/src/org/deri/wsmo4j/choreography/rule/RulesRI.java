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

package org.deri.wsmo4j.choreography.rule;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.wsmo.common.Identifier;
import org.wsmo.service.choreography.rule.ChoreographyRules;
import org.wsmo.service.rule.*;

import com.ontotext.wsmo4j.common.EntityImpl;

/**
 * Interface or class description
 * 
 * @author James Scicluna
 * @author Thomas Haselwanter
 * 
 * Created on 06-Dec-2005 Committed by $Author: vassil_momtchev $
 * 
 * $Source:
 * /cvsroot/wsmo4j/ext/choreography/src/ri/org/deri/wsmo4j/choreography/rule/RulesRI.java,v $,
 * @version $Revision: 1.6 $ $Date: 2006/10/24 14:11:47 $
 */

public class RulesRI extends EntityImpl implements ChoreographyRules, Iterable {
    /**
     * @uml.property   name="rules"
     * @uml.associationEnd   multiplicity="(0 -1)" elementType="org.wsmo.service.rule.ChoreographyRule"
     */
    Set<ChoreographyRule> rules = new HashSet<ChoreographyRule>();

    /**
     * Initializes the Rules container of Choreography Descriptions with the
     * given Identifier
     * 
     * @param id
     *            Identifier of the transition rules
     */
    public RulesRI(Identifier id) {
        super(id);
    }

    /**
     * Initializes the Rules container of Choreography Descriptions with the
     * given Identifier and rules
     * 
     * @param id
     *            Identifier of the Rules Container
     * @param rules
     *            A set of rules to be put in the container
     */
    public RulesRI(Identifier id, Set<ChoreographyRule> rules) {
        this(id);
        this.rules = rules;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.rule.Rules#listRules()
     */
    public Set<ChoreographyRule> listRules() {
        return Collections.unmodifiableSet(this.rules);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.rule.Rules#removeRule(org.wsmo.service.choreography.rule.Rule)
     */
    public void removeRule(ChoreographyRule rule) {
        this.rules.remove(rule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.wsmo.service.choreography.rule.Rules#addRule(org.wsmo.service.choreography.rule.Rule)
     */
    public void addRule(ChoreographyRule rule) {
        this.rules.add(rule);
    }

    public Iterator<ChoreographyRule> iterator() {
        return this.rules.iterator();
    }
}
