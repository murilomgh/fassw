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

package org.wsmo.grounding.sawsdl;

import javax.xml.namespace.QName;

import org.wsmo.common.IRI;

/**
 * An abstract representation of a semantic model annotation of a WSDL or XML Schema
 * entity.
 *
 * For details see: <br>
 *   <a href="http://www.w3.org/TR/sawsdl/" target="_blank">http://www.w3.org/TR/sawsdl/</a> <br>
 *   <a href="http://www.w3.org/2001/XMLSchema/" target="_blank">http://www.w3.org/2001/XMLSchema/</a>
 *   <a href="http://www.w3.org/TR/wsdl20/" target="_blank">http://www.w3.org/TR/wsdl20/</a>
 */
public interface ModelRef {

	/**
     * Returns unique id of the object annotated by this ModelReference
     * @return   unique id of the annotated object
     * @uml.property  name="source"
     */
	public QName getSource();

	/**
     * The target reference of this annotation, pointing to a concept of a semantic model.
     *  
     * @uml.property  name="target"
	 */
	public IRI getTarget();

}

/*
 * $Log: ModelRef.java,v $
 * Revision 1.3  2007/04/27 17:52:18  alex_simov
 * no message
 *
 * Revision 1.2  2007/04/27 17:46:31  alex_simov
 * javadoc added
 *
 * Revision 1.1  2007/04/24 14:09:44  alex_simov
 * new SA-WSDL api
 *
 */
