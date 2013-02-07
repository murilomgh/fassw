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

public class SemanticProcessImpl implements SemanticProcess,java.io.Serializable {

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

	private org.wsmo.common.IRI targetNamespace;
	public org.wsmo.common.IRI getTargetNamespace() {
		return this.targetNamespace;
	}
	public void setTargetNamespace(org.wsmo.common.IRI value) {
		this.targetNamespace=value;
	}

	private org.wsmo.common.IRI queryLanguage;
	public org.wsmo.common.IRI getQueryLanguage() {
		return this.queryLanguage;
	}
	public void setQueryLanguage(org.wsmo.common.IRI value) {
		this.queryLanguage=value;
	}

	private org.wsmo.common.IRI expressionLanguage;
	public org.wsmo.common.IRI getExpressionLanguage() {
		return this.expressionLanguage;
	}
	public void setExpressionLanguage(org.wsmo.common.IRI value) {
		this.expressionLanguage=value;
	}

	private boolean doSuppressJoinFailure;
	public boolean getDoSuppressJoinFailure() {
		return this.doSuppressJoinFailure;
	}
	public void setDoSuppressJoinFailure(boolean value) {
		this.doSuppressJoinFailure=value;
	}

	private boolean doExitOnStandardFault;
	public boolean getDoExitOnStandardFault() {
		return this.doExitOnStandardFault;
	}
	public void setDoExitOnStandardFault(boolean value) {
		this.doExitOnStandardFault=value;
	}

	private java.util.Set<Extension> extensionSet = new java.util.HashSet<Extension>();
	public boolean addExtension(Extension value) {
		return this.extensionSet.add(value);
	}
	public boolean removeExtension(Extension value) {
		return this.extensionSet.remove(value);
	}
	public java.util.Set<Extension> listExtensions() {
		return new java.util.LinkedHashSet<Extension>(this.extensionSet);
	}
	public java.util.Set<Extension> getExtensionSet() {
		return this.extensionSet;
	}
	public void setExtensionSet(java.util.Set<Extension> value) {
		this.extensionSet=value;
	}

	private java.util.Set<Import> importSet = new java.util.HashSet<Import>();
	public boolean addImport(Import value) {
		return this.importSet.add(value);
	}
	public boolean removeImport(Import value) {
		return this.importSet.remove(value);
	}
	public java.util.Set<Import> listImports() {
		return new java.util.LinkedHashSet<Import>(this.importSet);
	}
	public java.util.Set<Import> getImportSet() {
		return this.importSet;
	}
	public void setImportSet(java.util.Set<Import> value) {
		this.importSet=value;
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

	private Activity activity;
	public Activity getActivity() {
		return this.activity;
	}
	public void setActivity(Activity value) {
		this.activity=value;
	}

	private java.util.Set<Conversation> conversationSet = new java.util.HashSet<Conversation>();
	public boolean addConversation(Conversation value) {
		return this.conversationSet.add(value);
	}
	public boolean removeConversation(Conversation value) {
		return this.conversationSet.remove(value);
	}
	public java.util.Set<Conversation> listConversations() {
		return new java.util.LinkedHashSet<Conversation>(this.conversationSet);
	}
	public java.util.Set<Conversation> getConversationSet() {
		return this.conversationSet;
	}
	public void setConversationSet(java.util.Set<Conversation> value) {
		this.conversationSet=value;
	}

	private java.util.Set<Partner> partnersSet = new java.util.HashSet<Partner>();
	public boolean addPartners(Partner value) {
		return this.partnersSet.add(value);
	}
	public boolean removePartners(Partner value) {
		return this.partnersSet.remove(value);
	}
	public java.util.Set<Partner> listPartnerss() {
		return new java.util.LinkedHashSet<Partner>(this.partnersSet);
	}
	public java.util.Set<Partner> getPartnersSet() {
		return this.partnersSet;
	}
	public void setPartnersSet(java.util.Set<Partner> value) {
		this.partnersSet=value;
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
 * $Log: SemanticProcessImpl.java,v $
 * Revision 1.1  2007/04/18 16:27:15  lcekov
 * adding sbpel
 *
**/
