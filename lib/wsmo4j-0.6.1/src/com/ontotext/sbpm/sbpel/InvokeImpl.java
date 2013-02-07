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

public class InvokeImpl implements Invoke,java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int hibId;

	public int getHibId() {
		return this.hibId;
	}

	public void setHibId(int id) {
		this.hibId=id;
	}

	private PartnerLink partnerLink;
	public PartnerLink getPartnerLink() {
		return this.partnerLink;
	}
	public void setPartnerLink(PartnerLink value) {
		this.partnerLink=value;
	}

	private String portType;
	public String getPortType() {
		return this.portType;
	}
	public void setPortType(String value) {
		this.portType=value;
	}

	private String operation;
	public String getOperation() {
		return this.operation;
	}
	public void setOperation(String value) {
		this.operation=value;
	}

	private java.util.Set<Correlation> correlationSet = new java.util.HashSet<Correlation>();
	public boolean addCorrelation(Correlation value) {
		return this.correlationSet.add(value);
	}
	public boolean removeCorrelation(Correlation value) {
		return this.correlationSet.remove(value);
	}
	public java.util.Set<Correlation> listCorrelations() {
		return new java.util.LinkedHashSet<Correlation>(this.correlationSet);
	}
	public java.util.Set<Correlation> getCorrelationSet() {
		return this.correlationSet;
	}
	public void setCorrelationSet(java.util.Set<Correlation> value) {
		this.correlationSet=value;
	}

	private String name;
	public String getName() {
		return this.name;
	}
	public void setName(String value) {
		this.name=value;
	}

	private boolean doesSuppressJoinFailure;
	public boolean getDoesSuppressJoinFailure() {
		return this.doesSuppressJoinFailure;
	}
	public void setDoesSuppressJoinFailure(boolean value) {
		this.doesSuppressJoinFailure=value;
	}

	private java.util.Set<Link> isTargetSet = new java.util.HashSet<Link>();
	public boolean addIsTarget(Link value) {
		return this.isTargetSet.add(value);
	}
	public boolean removeIsTarget(Link value) {
		return this.isTargetSet.remove(value);
	}
	public java.util.Set<Link> listIsTargets() {
		return new java.util.LinkedHashSet<Link>(this.isTargetSet);
	}
	public java.util.Set<Link> getIsTargetSet() {
		return this.isTargetSet;
	}
	public void setIsTargetSet(java.util.Set<Link> value) {
		this.isTargetSet=value;
	}

	private Condition joinCondition;
	public Condition getJoinCondition() {
		return this.joinCondition;
	}
	public void setJoinCondition(Condition value) {
		this.joinCondition=value;
	}

	private java.util.Set<Source> isSourceSet = new java.util.HashSet<Source>();
	public boolean addIsSource(Source value) {
		return this.isSourceSet.add(value);
	}
	public boolean removeIsSource(Source value) {
		return this.isSourceSet.remove(value);
	}
	public java.util.Set<Source> listIsSources() {
		return new java.util.LinkedHashSet<Source>(this.isSourceSet);
	}
	public java.util.Set<Source> getIsSourceSet() {
		return this.isSourceSet;
	}
	public void setIsSourceSet(java.util.Set<Source> value) {
		this.isSourceSet=value;
	}

	private Variable inputVariable;
	public Variable getInputVariable() {
		return this.inputVariable;
	}
	public void setInputVariable(Variable value) {
		this.inputVariable=value;
	}

	private Variable outputVariable;
	public Variable getOutputVariable() {
		return this.outputVariable;
	}
	public void setOutputVariable(Variable value) {
		this.outputVariable=value;
	}

	private java.util.Set<Catch> catchSet = new java.util.HashSet<Catch>();
	public boolean addCatch(Catch value) {
		return this.catchSet.add(value);
	}
	public boolean removeCatch(Catch value) {
		return this.catchSet.remove(value);
	}
	public java.util.Set<Catch> listCatchs() {
		return new java.util.LinkedHashSet<Catch>(this.catchSet);
	}
	public java.util.Set<Catch> getCatchSet() {
		return this.catchSet;
	}
	public void setCatchSet(java.util.Set<Catch> value) {
		this.catchSet=value;
	}

	private CatchAll catchAll;
	public CatchAll getCatchAll() {
		return this.catchAll;
	}
	public void setCatchAll(CatchAll value) {
		this.catchAll=value;
	}

	private CompensationHandler compensationHandler;
	public CompensationHandler getCompensationHandler() {
		return this.compensationHandler;
	}
	public void setCompensationHandler(CompensationHandler value) {
		this.compensationHandler=value;
	}

	private java.util.Set<ToParts> toPartsSet = new java.util.HashSet<ToParts>();
	public boolean addToParts(ToParts value) {
		return this.toPartsSet.add(value);
	}
	public boolean removeToParts(ToParts value) {
		return this.toPartsSet.remove(value);
	}
	public java.util.Set<ToParts> listToPartss() {
		return new java.util.LinkedHashSet<ToParts>(this.toPartsSet);
	}
	public java.util.Set<ToParts> getToPartsSet() {
		return this.toPartsSet;
	}
	public void setToPartsSet(java.util.Set<ToParts> value) {
		this.toPartsSet=value;
	}

	private java.util.Set<FromParts> fromPartsSet = new java.util.HashSet<FromParts>();
	public boolean addFromParts(FromParts value) {
		return this.fromPartsSet.add(value);
	}
	public boolean removeFromParts(FromParts value) {
		return this.fromPartsSet.remove(value);
	}
	public java.util.Set<FromParts> listFromPartss() {
		return new java.util.LinkedHashSet<FromParts>(this.fromPartsSet);
	}
	public java.util.Set<FromParts> getFromPartsSet() {
		return this.fromPartsSet;
	}
	public void setFromPartsSet(java.util.Set<FromParts> value) {
		this.fromPartsSet=value;
	}

}
/*
 * $Log: InvokeImpl.java,v $
 * Revision 1.1  2007/04/18 16:27:13  lcekov
 * adding sbpel
 *
**/