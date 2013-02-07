/*
 wsmo4j - a WSMO API and Reference Implementation

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
package org.deri.wsmo4j.io.parser.wsml;

import java.util.*;

import org.omwg.ontology.*;
import org.wsmo.factory.*;
import org.wsmo.wsml.compiler.node.*;

import com.ontotext.wsmo4j.parser.wsml.*;

/**
 * 
 * <pre>
 * Created on 21.11.2005
 * Committed by $Author$
 * $Source$,
 * </pre>
 *
 * @author Holger Lausen
 * @version $Revision$ $Date$
 */
public class VariableAnalysis extends ASTAnalysis {
    private LogicalExpressionFactory factory;
    private ASTAnalysisContainer container;
    
    private Stack terms; // handle to terms stack
    private Stack varLists; // handle to var lists stack
    private Stack myVars = new Stack();
    public VariableAnalysis(ASTAnalysisContainer container, 
            LogicalExpressionFactory factory) {
        
        if (container == null || factory == null) {
            throw new IllegalArgumentException();
        }
        this.factory = factory;
        this.container = container;
        
        // register the handled nodes
        container.registerNodeHandler(AVariableVariables.class, this);
        container.registerNodeHandler(AVariableVariablelist.class, this);
        container.registerNodeHandler(AVariableListVariablelist.class, this);
        container.registerNodeHandler(AVariables.class, this);
        
        varLists = container.getStack(Variable[].class);
    }


    public void outAVariableVariablelist(AVariableVariablelist node) {
        outVarList();
    }

    public void outAVariableListVariablelist(AVariableListVariablelist node) {
        outVarList();
    }

    public void outAVariables(AVariables node) {
        outVarList();
    }

    public void outAVariableVariables(AVariableVariables node) {
        outVarList();
    }

    public void caseAVariables(AVariables node) {
        myVars.add(factory.createVariable(
                node.getVariable().getText()));
    }


    public void caseAVariableVariablelist(AVariableVariablelist node) {
        myVars.add(factory.createVariable(
                node.getVariable().getText()));
    }


    public void caseAVariableVariables(AVariableVariables node) {
        myVars.add(factory.createVariable(
                node.getVariable().getText()));
    }
    
    private void outVarList(){
        if (!myVars.isEmpty()){
            varLists.add((Variable[])
                    myVars.toArray(new Variable[myVars.size()]));
            myVars.clear();
        }
    }

}
