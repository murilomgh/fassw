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


package com.ontotext.wsmo4j.ontology;

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.omwg.ontology.Axiom;
import org.omwg.logicalexpression.LogicalExpression;
import org.wsmo.common.Identifier;


public class AxiomImpl extends OntologyElementImpl
        implements Axiom {
    
    private LinkedHashSet <LogicalExpression> definitions;

    public AxiomImpl(Identifier thisID) {
        super(thisID);
        definitions = new LinkedHashSet <LogicalExpression> ();
    }

    public Set <LogicalExpression> listDefinitions() {
        return Collections.unmodifiableSet(definitions);
    }

    public void addDefinition(LogicalExpression definition) {
        if (definition == null) {
            throw new IllegalArgumentException();
        }
        definitions.add(definition);
    }

    public void removeDefinition(LogicalExpression le) {
        if (le == null) {
            throw new IllegalArgumentException();
        }
        definitions.remove(le);
    }
    
    public boolean equals(Object target) {
        if (target == this) {
            return true; // instance match
        }

        if (null == target 
                || false == target instanceof Axiom) {
            return false;
        }
        return super.equals(target);
    }
}

/*
 * $Log: AxiomImpl.java,v $
 * Revision 1.16  2007/04/02 12:13:20  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.15  2005/09/02 13:32:45  ohamano
 * move logicalexpression packages from ext to core
 * move tests from logicalexpression.test to test module
 *
 * Revision 1.14  2005/06/22 09:16:05  alex_simov
 * Simplified equals() method. Identity strongly relies on identifier string
 *
 * Revision 1.13  2005/06/01 12:09:33  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.2  2005/05/17 12:04:56  alex
 * Collections.unmodifiableSet() used instead of new set construction
 *
 * Revision 1.1  2005/05/11 13:40:00  alex
 * initial commit
 *
 */