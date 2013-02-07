/*
	wsmo4j - a WSMO API and Reference Implementation

	Copyright (c) 2007, Ontotext Lab. / SIRMA

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

package com.ontotext.sbpm.sbpel;

import org.sbpm.sbpel.*;

public class ExtensionImpl implements Extension,java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int hibId;

	public int getHibId() {
		return this.hibId;
	}

	public void setHibId(int id) {
		this.hibId=id;
	}

	private org.wsmo.common.IRI namespace;
	public org.wsmo.common.IRI getNamespace() {
		return this.namespace;
	}
	public void setNamespace(org.wsmo.common.IRI value) {
		this.namespace=value;
	}

	private boolean isMustUnderstand;
	public boolean getIsMustUnderstand() {
		return this.isMustUnderstand;
	}
	public void setIsMustUnderstand(boolean value) {
		this.isMustUnderstand=value;
	}

}
/*
 * $Log: ExtensionImpl.java,v $
 * Revision 1.1  2007/04/18 16:27:13  lcekov
 * adding sbpel
 *
**/
