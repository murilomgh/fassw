/*
 wsmo4j extension - an Orchestration API and Reference Implementation

 Copyright (c) 2005, University of Innsbruck, Austria

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

package org.wsmo.service.orchestration;

import org.wsmo.service.orchestration.rule.*;
import org.wsmo.service.signature.*;

/**
 * WSMO4J Orchestration Interface
 * 
 * @author James Scicluna
 * 
 * Created on 30-May-2006 Committed by $Author: vassil_momtchev $
 * 
 * $Source: /cvsroot/wsmo4j/ext/orchestration/src/api/org/wsmo/service/orchestration/Orchestration.java,v $,
 * @version $Revision: 1.2 $ $Date: 2006/10/24 14:13:25 $
 */

public interface Orchestration extends org.wsmo.service.Orchestration
{

  /**
   * Returns the state signature of the orchestration description.
   * 
   * @return A <code>StateSignature</code> object or null.
   */
  public StateSignature getStateSignature();

  /**
   * Sets the state signature of the orchestration description.
   * 
   * @param signature
   *          <code>StateSignature</code> object to be set.
   */
  public void setStateSignature(StateSignature signature);

  /**
   * Returns the transition rules of the orchestration description.
   * 
   * @return A <code>Rules</code> object containing the transition rules.
   */
  public OrchestrationRules getRules();

  /**
   * Sets the transition rules of the orchestration description.
   * 
   * @param rules
   *          <code>Rules</code> object to be set.
   */
  public void setRules(OrchestrationRules rules);

}
