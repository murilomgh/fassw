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

package org.sbpm.sbpel;

public interface Scope extends StructuredActivity, StandardAttributes {

	public boolean getIsIsolated();
	public void setIsIsolated(boolean value);
	public boolean getExitOnStandardFault();
	public void setExitOnStandardFault(boolean value);
	public boolean addVariable(Variable value);
	public boolean removeVariable(Variable value);
	public java.util.Set<Variable> listVariables();
	public boolean addPartnerLink(PartnerLink value);
	public boolean removePartnerLink(PartnerLink value);
	public java.util.Set<PartnerLink> listPartnerLinks();
	public boolean addMessageExchange(MessageExchange value);
	public boolean removeMessageExchange(MessageExchange value);
	public java.util.Set<MessageExchange> listMessageExchanges();
	public boolean addCorrelationSet(CorrelationSet value);
	public boolean removeCorrelationSet(CorrelationSet value);
	public java.util.Set<CorrelationSet> listCorrelationSets();
	public boolean addCatch(Catch value);
	public boolean removeCatch(Catch value);
	public java.util.Set<Catch> listCatchs();
	public CatchAll getCatchAll();
	public void setCatchAll(CatchAll value);
	public boolean addOnEvent(OnEvent value);
	public boolean removeOnEvent(OnEvent value);
	public java.util.Set<OnEvent> listOnEvents();
	public boolean addOnAlarm(RepeatableOnAlarm value);
	public boolean removeOnAlarm(RepeatableOnAlarm value);
	public java.util.Set<RepeatableOnAlarm> listOnAlarms();
	public CompensationHandler getCompensationHandler();
	public void setCompensationHandler(CompensationHandler value);
	public TerminationHandler getTerminationHandler();
	public void setTerminationHandler(TerminationHandler value);
	public Activity getActivity();
	public void setActivity(Activity value);
}
/*
 * $Log: Scope.java,v $
 * Revision 1.1  2007/04/18 16:27:09  lcekov
 * adding sbpel
 *
**/
