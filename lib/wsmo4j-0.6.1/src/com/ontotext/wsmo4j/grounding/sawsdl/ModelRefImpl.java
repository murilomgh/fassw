/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2004-2007, Ontotext Lab. / SIRMA

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

package com.ontotext.wsmo4j.grounding.sawsdl;

import javax.xml.namespace.QName;

import org.wsmo.common.IRI;
import org.wsmo.grounding.sawsdl.ModelRef;

public abstract class ModelRefImpl implements ModelRef {
    
    private QName source;
    private IRI target;
    
    ModelRefImpl(QName source, IRI target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException();
        }
        
        this.source = source;
        this.target = target;
    }

    public QName getSource() {
        return this.source;
    }

    public IRI getTarget() {
        return this.target;
    }
    
    public boolean equals(Object o) {
        if (o == null 
                || false == o instanceof ModelRef) {
            return false;
        }
        ModelRef ref = (ModelRef)o;
        return source.equals(ref.getSource()) 
                && target.equals(ref.getTarget());
    }
    
    public int hashCode() {
        return source.hashCode() + target.hashCode();
    }

}

/*
 * $Log: ModelRefImpl.java,v $
 * Revision 1.1  2007/04/24 16:47:20  alex_simov
 * new SA-WSDL impl
 *
 */
