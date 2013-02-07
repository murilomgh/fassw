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

public interface WSDLInteraction extends Interaction {

	public PartnerLink getPartnerLink();
	public void setPartnerLink(PartnerLink value);
	public String getPortType();
	public void setPortType(String value);
	public String getOperation();
	public void setOperation(String value);
	public boolean addCorrelation(Correlation value);
	public boolean removeCorrelation(Correlation value);
	public java.util.Set<Correlation> listCorrelations();
}
/*
 * $Log: WSDLInteraction.java,v $
 * Revision 1.1  2007/04/18 16:27:11  lcekov
 * adding sbpel
 *
**/
