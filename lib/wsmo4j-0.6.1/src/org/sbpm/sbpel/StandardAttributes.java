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

public interface StandardAttributes {

	public String getName();
	public void setName(String value);
	public boolean getDoesSuppressJoinFailure();
	public void setDoesSuppressJoinFailure(boolean value);
	public boolean addIsTarget(Link value);
	public boolean removeIsTarget(Link value);
	public java.util.Set<Link> listIsTargets();
	public Condition getJoinCondition();
	public void setJoinCondition(Condition value);
	public boolean addIsSource(Source value);
	public boolean removeIsSource(Source value);
	public java.util.Set<Source> listIsSources();
}
/*
 * $Log: StandardAttributes.java,v $
 * Revision 1.1  2007/04/18 16:27:10  lcekov
 * adding sbpel
 *
**/
