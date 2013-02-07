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

public interface Flow extends StructuredActivity, StandardAttributes {

	public boolean addLink(Link value);
	public boolean removeLink(Link value);
	public java.util.Set<Link> listLinks();
	public boolean addActivity(Activity value);
	public boolean removeActivity(Activity value);
	public java.util.Set<Activity> listActivitys();
}
/*
 * $Log: Flow.java,v $
 * Revision 1.1  2007/04/18 16:27:08  lcekov
 * adding sbpel
 *
**/
