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


package com.ontotext.wsmo4j.common;

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */
import java.io.Serializable;

import org.wsmo.common.IRI;
import org.wsmo.common.Namespace;


public class NamespaceImpl
        implements Namespace, Serializable {

    private String prefix;

    private IRI fullIRI;

    public NamespaceImpl(String prefix, IRI fullIRI) {
        if (null == prefix 
            || null == fullIRI 
//            || prefix.length() == 0 
            || fullIRI.toString().length() == 0) {
            throw new IllegalArgumentException("cannot use 'null' as "
                    + "prefix of fullIRI!");
        }
        this.prefix = prefix;
        if (fullIRI.toString().endsWith("#") 
                || fullIRI.toString().endsWith("/")) {
            this.fullIRI = fullIRI;
        }
        else {
            this.fullIRI = new IRIImpl(fullIRI.toString() + '#');
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public IRI getIRI() {
        return fullIRI;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null
                || false == object instanceof Namespace) {
            return false;
        }
        
        // internal preconditions
        assert prefix != null;
        assert fullIRI != null;
        
        Namespace anotherNS = (Namespace)object;
        return (this.prefix.equals(anotherNS.getPrefix())
                && this.fullIRI.equals(anotherNS.getIRI()));
    }
    
    public int hashCode() {
        return this.prefix.hashCode() + this.fullIRI.hashCode();
    }
}

/*
 * $Log: NamespaceImpl.java,v $
 * Revision 1.14  2005/09/16 14:02:44  alex_simov
 * Identifier.asString() removed, use Object.toString() instead
 * (Implementations MUST override toString())
 *
 * Revision 1.13  2005/06/02 14:20:51  alex_simov
 * Namespace allows empty string prefix for the default namespace
 *
 * Revision 1.12  2005/06/01 12:00:53  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.2  2005/05/11 13:33:58  alex
 * copyright notice updated
 *
 * Revision 1.1  2005/05/11 12:24:05  alex
 * initial commit
 *
 */
