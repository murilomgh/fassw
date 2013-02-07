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

public class ReplyImpl implements Reply,java.io.Serializable {

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

	private Variable variable;
	public Variable getVariable() {
		return this.variable;
	}
	public void setVariable(Variable value) {
		this.variable=value;
	}

	private String faultName;
	public String getFaultName() {
		return this.faultName;
	}
	public void setFaultName(String value) {
		this.faultName=value;
	}

	private MessageExchange messageExchange;
	public MessageExchange getMessageExchange() {
		return this.messageExchange;
	}
	public void setMessageExchange(MessageExchange value) {
		this.messageExchange=value;
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

}
/*
 * $Log: ReplyImpl.java,v $
 * Revision 1.1  2007/04/18 16:27:14  lcekov
 * adding sbpel
 *
**/
