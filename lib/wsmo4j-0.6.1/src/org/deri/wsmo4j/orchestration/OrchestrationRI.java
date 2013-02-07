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

package org.deri.wsmo4j.orchestration;

import org.wsmo.common.Identifier;
import org.wsmo.service.orchestration.Orchestration;
import org.wsmo.service.orchestration.rule.*;
import org.wsmo.service.signature.*;

import com.ontotext.wsmo4j.common.TopEntityImpl;

/**
 * Interface or class description
 * 
 * @author James Scicluna
 * 
 * Created on 30-May-2006 Committed by $Author: vassil_momtchev $
 * 
 * $Source: /cvsroot/wsmo4j/ext/orchestration/src/ri/org/deri/wsmo4j/orchestration/OrchestrationRI.java,v $,
 * @version $Revision: 1.2 $ $Date: 2006/10/24 14:13:25 $
 */

public class OrchestrationRI extends TopEntityImpl implements Orchestration
{

  StateSignature signature;

  OrchestrationRules rules;

  /**
   * Initializes the <code>Orchestration</code> object with the given
   * parameters.
   * 
   * @param id
   *          An <code>Identifier<code> for the <code>Orchestration</code>
   * @param signature The <code>StateSignature</code> of the <code>Orchestration</code>
   * @param rules The transition <code>Rules</code> of the <code>Orchestration</code>
   */
  public OrchestrationRI(Identifier id, StateSignature signature, OrchestrationRules rules)
  {
    super(id);
    this.signature = signature;
    this.rules = rules;
  }

  /**
   * Default constructor of the <code>Orchestration</code>
   * 
   * @param id
   *          An <code>Identifier</code> of the <code>Orchestration</code>
   */
  public OrchestrationRI(Identifier id)
  {
    super(id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.wsmo.service.orchestration.Orchestration#getStateSignature()
   */
  public StateSignature getStateSignature()
  {
    return this.signature;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.wsmo.service.orchestration.Orchestration#setStateSignature(org.wsmo.service.choreography.signature.StateSignature)
   */
  public void setStateSignature(StateSignature signature)
  {
    this.signature = signature;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.wsmo.service.orchestration.Orchestration#getRules()
   */
  public OrchestrationRules getRules()
  {
    return this.rules;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.wsmo.service.orchestration.Orchestration#setRules(org.wsmo.service.choreography.rule.Rules)
   */
  public void setRules(OrchestrationRules rules)
  {
    this.rules = rules;
  }

}
