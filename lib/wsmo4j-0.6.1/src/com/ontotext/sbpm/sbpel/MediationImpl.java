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

public class MediationImpl implements Mediation,java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int hibId;

	public int getHibId() {
		return this.hibId;
	}

	public void setHibId(int id) {
		this.hibId=id;
	}

	private SemanticVariable inputVariable;
	public SemanticVariable getInputVariable() {
		return this.inputVariable;
	}
	public void setInputVariable(SemanticVariable value) {
		this.inputVariable=value;
	}

	private SemanticVariable outPutVariable;
	public SemanticVariable getOutPutVariable() {
		return this.outPutVariable;
	}
	public void setOutPutVariable(SemanticVariable value) {
		this.outPutVariable=value;
	}

	private java.util.Set<org.wsmo.common.IRI> usesWSMOMediatorSet = new java.util.HashSet<org.wsmo.common.IRI>();
	public boolean addUsesWSMOMediator(org.wsmo.common.IRI value) {
		return this.usesWSMOMediatorSet.add(value);
	}
	public boolean removeUsesWSMOMediator(org.wsmo.common.IRI value) {
		return this.usesWSMOMediatorSet.remove(value);
	}
	public java.util.Set<org.wsmo.common.IRI> listUsesWSMOMediators() {
		return new java.util.LinkedHashSet<org.wsmo.common.IRI>(this.usesWSMOMediatorSet);
	}
	public java.util.Set<org.wsmo.common.IRI> getUsesWSMOMediatorSet() {
		return this.usesWSMOMediatorSet;
	}
	public void setUsesWSMOMediatorSet(java.util.Set<org.wsmo.common.IRI> value) {
		this.usesWSMOMediatorSet=value;
	}

}
/*
 * $Log: MediationImpl.java,v $
 * Revision 1.1  2007/04/18 16:27:13  lcekov
 * adding sbpel
 *
**/
