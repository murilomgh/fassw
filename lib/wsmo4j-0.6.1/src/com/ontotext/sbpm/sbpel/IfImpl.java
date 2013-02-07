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

public class IfImpl implements If,java.io.Serializable {

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

	private ConditionalBranch ifBranch;
	public ConditionalBranch getIfBranch() {
		return this.ifBranch;
	}
	public void setIfBranch(ConditionalBranch value) {
		this.ifBranch=value;
	}

	private java.util.Set<OrderedConditionalBranch> elseIfBranchSet = new java.util.HashSet<OrderedConditionalBranch>();
	public boolean addElseIfBranch(OrderedConditionalBranch value) {
		return this.elseIfBranchSet.add(value);
	}
	public boolean removeElseIfBranch(OrderedConditionalBranch value) {
		return this.elseIfBranchSet.remove(value);
	}
	public java.util.Set<OrderedConditionalBranch> listElseIfBranchs() {
		return new java.util.LinkedHashSet<OrderedConditionalBranch>(this.elseIfBranchSet);
	}
	public java.util.Set<OrderedConditionalBranch> getElseIfBranchSet() {
		return this.elseIfBranchSet;
	}
	public void setElseIfBranchSet(java.util.Set<OrderedConditionalBranch> value) {
		this.elseIfBranchSet=value;
	}

	private Else elseBranch;
	public Else getElseBranch() {
		return this.elseBranch;
	}
	public void setElseBranch(Else value) {
		this.elseBranch=value;
	}

}
/*
 * $Log: IfImpl.java,v $
 * Revision 1.1  2007/04/18 16:27:13  lcekov
 * adding sbpel
 *
**/