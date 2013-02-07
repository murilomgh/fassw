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

public interface SemanticProcess extends Process {

	public boolean addConversation(Conversation value);
	public boolean removeConversation(Conversation value);
	public java.util.Set<Conversation> listConversations();
	public boolean addPartners(Partner value);
	public boolean removePartners(Partner value);
	public java.util.Set<Partner> listPartnerss();
	public boolean addSemanticOnMessage(SemanticOnMessage value);
	public boolean removeSemanticOnMessage(SemanticOnMessage value);
	public java.util.Set<SemanticOnMessage> listSemanticOnMessages();
}
/*
 * $Log: SemanticProcess.java,v $
 * Revision 1.1  2007/04/18 16:27:10  lcekov
 * adding sbpel
 *
**/
