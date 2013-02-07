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

public class SemanticScopeImpl implements SemanticScope,java.io.Serializable {

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

	private boolean isIsolated;
	public boolean getIsIsolated() {
		return this.isIsolated;
	}
	public void setIsIsolated(boolean value) {
		this.isIsolated=value;
	}

	private boolean exitOnStandardFault;
	public boolean getExitOnStandardFault() {
		return this.exitOnStandardFault;
	}
	public void setExitOnStandardFault(boolean value) {
		this.exitOnStandardFault=value;
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

	private java.util.Set<PartnerLink> partnerLinkSet = new java.util.HashSet<PartnerLink>();
	public boolean addPartnerLink(PartnerLink value) {
		return this.partnerLinkSet.add(value);
	}
	public boolean removePartnerLink(PartnerLink value) {
		return this.partnerLinkSet.remove(value);
	}
	public java.util.Set<PartnerLink> listPartnerLinks() {
		return new java.util.LinkedHashSet<PartnerLink>(this.partnerLinkSet);
	}
	public java.util.Set<PartnerLink> getPartnerLinkSet() {
		return this.partnerLinkSet;
	}
	public void setPartnerLinkSet(java.util.Set<PartnerLink> value) {
		this.partnerLinkSet=value;
	}

	private java.util.Set<MessageExchange> messageExchangeSet = new java.util.HashSet<MessageExchange>();
	public boolean addMessageExchange(MessageExchange value) {
		return this.messageExchangeSet.add(value);
	}
	public boolean removeMessageExchange(MessageExchange value) {
		return this.messageExchangeSet.remove(value);
	}
	public java.util.Set<MessageExchange> listMessageExchanges() {
		return new java.util.LinkedHashSet<MessageExchange>(this.messageExchangeSet);
	}
	public java.util.Set<MessageExchange> getMessageExchangeSet() {
		return this.messageExchangeSet;
	}
	public void setMessageExchangeSet(java.util.Set<MessageExchange> value) {
		this.messageExchangeSet=value;
	}

	private java.util.Set<CorrelationSet> correlationSetSet = new java.util.HashSet<CorrelationSet>();
	public boolean addCorrelationSet(CorrelationSet value) {
		return this.correlationSetSet.add(value);
	}
	public boolean removeCorrelationSet(CorrelationSet value) {
		return this.correlationSetSet.remove(value);
	}
	public java.util.Set<CorrelationSet> listCorrelationSets() {
		return new java.util.LinkedHashSet<CorrelationSet>(this.correlationSetSet);
	}
	public java.util.Set<CorrelationSet> getCorrelationSetSet() {
		return this.correlationSetSet;
	}
	public void setCorrelationSetSet(java.util.Set<CorrelationSet> value) {
		this.correlationSetSet=value;
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

	private java.util.Set<OnEvent> onEventSet = new java.util.HashSet<OnEvent>();
	public boolean addOnEvent(OnEvent value) {
		return this.onEventSet.add(value);
	}
	public boolean removeOnEvent(OnEvent value) {
		return this.onEventSet.remove(value);
	}
	public java.util.Set<OnEvent> listOnEvents() {
		return new java.util.LinkedHashSet<OnEvent>(this.onEventSet);
	}
	public java.util.Set<OnEvent> getOnEventSet() {
		return this.onEventSet;
	}
	public void setOnEventSet(java.util.Set<OnEvent> value) {
		this.onEventSet=value;
	}

	private java.util.Set<RepeatableOnAlarm> onAlarmSet = new java.util.HashSet<RepeatableOnAlarm>();
	public boolean addOnAlarm(RepeatableOnAlarm value) {
		return this.onAlarmSet.add(value);
	}
	public boolean removeOnAlarm(RepeatableOnAlarm value) {
		return this.onAlarmSet.remove(value);
	}
	public java.util.Set<RepeatableOnAlarm> listOnAlarms() {
		return new java.util.LinkedHashSet<RepeatableOnAlarm>(this.onAlarmSet);
	}
	public java.util.Set<RepeatableOnAlarm> getOnAlarmSet() {
		return this.onAlarmSet;
	}
	public void setOnAlarmSet(java.util.Set<RepeatableOnAlarm> value) {
		this.onAlarmSet=value;
	}

	private CompensationHandler compensationHandler;
	public CompensationHandler getCompensationHandler() {
		return this.compensationHandler;
	}
	public void setCompensationHandler(CompensationHandler value) {
		this.compensationHandler=value;
	}

	private TerminationHandler terminationHandler;
	public TerminationHandler getTerminationHandler() {
		return this.terminationHandler;
	}
	public void setTerminationHandler(TerminationHandler value) {
		this.terminationHandler=value;
	}

	private Activity activity;
	public Activity getActivity() {
		return this.activity;
	}
	public void setActivity(Activity value) {
		this.activity=value;
	}

	private java.util.Set<SemanticOnMessage> semanticOnMessageSet = new java.util.HashSet<SemanticOnMessage>();
	public boolean addSemanticOnMessage(SemanticOnMessage value) {
		return this.semanticOnMessageSet.add(value);
	}
	public boolean removeSemanticOnMessage(SemanticOnMessage value) {
		return this.semanticOnMessageSet.remove(value);
	}
	public java.util.Set<SemanticOnMessage> listSemanticOnMessages() {
		return new java.util.LinkedHashSet<SemanticOnMessage>(this.semanticOnMessageSet);
	}
	public java.util.Set<SemanticOnMessage> getSemanticOnMessageSet() {
		return this.semanticOnMessageSet;
	}
	public void setSemanticOnMessageSet(java.util.Set<SemanticOnMessage> value) {
		this.semanticOnMessageSet=value;
	}

}
/*
 * $Log: SemanticScopeImpl.java,v $
 * Revision 1.1  2007/04/18 16:27:15  lcekov
 * adding sbpel
 *
**/
