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

public interface Assign extends BasicActivity, StandardAttributes {

	public boolean getValidate();
	public void setValidate(boolean value);
	public boolean addAssignOperation(AssignOperation value);
	public boolean removeAssignOperation(AssignOperation value);
	public java.util.Set<AssignOperation> listAssignOperations();
}
/*
 * $Log: Assign.java,v $
 * Revision 1.1  2007/04/18 16:27:05  lcekov
 * adding sbpel
 *
**/
