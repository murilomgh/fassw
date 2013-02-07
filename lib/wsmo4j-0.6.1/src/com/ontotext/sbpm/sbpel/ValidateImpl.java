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

public class ValidateImpl implements Validate,java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int hibId;

	public int getHibId() {
		return this.hibId;
	}

	public void setHibId(int id) {
		this.hibId=id;
	}

	private java.util.Set<Variable> variableSet = new java.util.HashSet<Variable>();
	public boolean addVariable(Variable value) {
		return this.variableSet.add(value);
	}
	public boolean removeVariable(Variable value) {
		return this.variableSet.remove(value);
	}
	public java.util.Set<Variable> listVariables() {
		return new java.util.LinkedHashSet<Variable>(this.variableSet);
	}
	public java.util.Set<Variable> getVariableSet() {
		return this.variableSet;
	}
	public void setVariableSet(java.util.Set<Variable> value) {
		this.variableSet=value;
	}

}
/*
 * $Log: ValidateImpl.java,v $
 * Revision 1.1  2007/04/18 16:27:15  lcekov
 * adding sbpel
 *
**/
