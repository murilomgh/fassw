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

public class PartnerImpl implements Partner,java.io.Serializable {

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

}
/*
 * $Log: PartnerImpl.java,v $
 * Revision 1.1  2007/04/18 16:27:14  lcekov
 * adding sbpel
 *
**/
