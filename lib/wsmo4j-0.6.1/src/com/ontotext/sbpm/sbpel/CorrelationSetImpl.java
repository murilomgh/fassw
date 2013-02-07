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

public class CorrelationSetImpl implements CorrelationSet,java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int hibId;

	public int getHibId() {
		return this.hibId;
	}

	public void setHibId(int id) {
		this.hibId=id;
	}

	private String name;
	public String getName() {
		return this.name;
	}
	public void setName(String value) {
		this.name=value;
	}

	private java.util.Set<String> propertiesSet = new java.util.HashSet<String>();
	public boolean addProperties(String value) {
		return this.propertiesSet.add(value);
	}
	public boolean removeProperties(String value) {
		return this.propertiesSet.remove(value);
	}
	public java.util.Set<String> listPropertiess() {
		return new java.util.LinkedHashSet<String>(this.propertiesSet);
	}
	public java.util.Set<String> getPropertiesSet() {
		return this.propertiesSet;
	}
	public void setPropertiesSet(java.util.Set<String> value) {
		this.propertiesSet=value;
	}

}
/*
 * $Log: CorrelationSetImpl.java,v $
 * Revision 1.1  2007/04/18 16:27:12  lcekov
 * adding sbpel
 *
**/
