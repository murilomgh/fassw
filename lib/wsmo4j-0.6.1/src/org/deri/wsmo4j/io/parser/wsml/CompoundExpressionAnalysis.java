/*
 wsmo4j - a WSMO API and Reference Implementation
 Copyright (c) 2004, University of Innsbruck, Institute of Computer Science
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

import org.deri.wsmo4j.logicalexpression.LogicalExpressionImpl;
import org.omwg.logicalexpression.*;
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
public class CompoundExpressionAnalysis extends ASTAnalysis {
    private LogicalExpressionFactory lFactory;
    private ASTAnalysisContainer container;
    
    private Stack logexps; // handle to logexp stack
    
    /**
     * Adapter for all Compound Expressions
     * @param container
     * @param lFactory
     */
    public CompoundExpressionAnalysis(ASTAnalysisContainer container, 
            LogicalExpressionFactory lFactory) {
        
        if (container == null || lFactory == null) {
            throw new IllegalArgumentException();
        }
        this.container = container;
        this.lFactory = lFactory;
        
        // register the handled nodes
        container.registerNodeHandler(AConjunction.class, this);
        container.registerNodeHandler(ADisjunction.class, this);
        container.registerNodeHandler(AConstraintLogExpr.class, this);
        container.registerNodeHandler(ALpRuleLogExpr.class, this);
        container.registerNodeHandler(AQuantified.class, this);
        container.registerNodeHandler(ANegatedSubexpr.class, this);
        container.registerNodeHandler(AImplicationExpr.class, this);

        container.registerNodeHandler(AOtherExpressionLogExpr.class, this);
        
        //shortcuts
        logexps = container.getStack(LogicalExpression.class);

    }

    private void outALogDefinition(){
        Stack orgLes = container.getStack(String.class);
        Object le = logexps.peek();
        if (orgLes.size()==0 || !(le instanceof LogicalExpressionImpl)){
            return;
        }
        String org = (String)orgLes.remove(0);
        ((LogicalExpressionImpl)le).setStringRepresentation(org);
    }

    public void outAOtherExpressionLogExpr(AOtherExpressionLogExpr node){
        outALogDefinition();
    }
    
    public void outAConjunction(AConjunction node) {
        LogicalExpression le2 = (LogicalExpression)logexps.pop();
        LogicalExpression le1 = (LogicalExpression)logexps.pop();
        logexps.add(lFactory.createConjunction(le1,le2));
    }

    public void outADisjunction(ADisjunction node) {
        LogicalExpression le2 = (LogicalExpression)logexps.pop();
        LogicalExpression le1 = (LogicalExpression)logexps.pop();
        logexps.add(lFactory.createDisjunction(le1,le2));
    }

    public void outAConstraintLogExpr(AConstraintLogExpr node) {
        logexps.add(lFactory.createConstraint(
                (LogicalExpression)logexps.pop()));
        outALogDefinition();
    }    
    
    public void outALpRuleLogExpr(ALpRuleLogExpr node) {
        LogicalExpression le2 = (LogicalExpression)logexps.pop();
        LogicalExpression le1 = (LogicalExpression)logexps.pop();
        logexps.add(lFactory.createLogicProgrammingRule(
                le1,le2));
        outALogDefinition();
    }
    
    public void outAQuantified(AQuantified node) {
        PQuantifierKey quantifierKey = node.getQuantifierKey();
        Variable[] vars = (Variable[])container.popFromStack(Variable[].class,Variable[].class);
        Set <Variable> variables = new HashSet <Variable> ();
        for (int i=0; i<vars.length; i++){
            variables.add(vars[i]);
        }
        
        if (quantifierKey instanceof AExistsQuantifierKey) {
            logexps.add(lFactory.createExistentialQuantification(
                    variables, 
                    (LogicalExpression)logexps.pop()));
        }
        else {
            logexps.add(lFactory.createUniversalQuantification(
                    variables, 
                    (LogicalExpression)logexps.pop()));
        }
    }
    
    public void outANegatedSubexpr(ANegatedSubexpr node) {
        if (node.getTNot().getText().equals("neg")) {
            logexps.add(lFactory.createNegation(
                    (LogicalExpression)logexps.pop()));
        }
        else {
            logexps.add(lFactory.createNegationAsFailure(
                    (LogicalExpression)logexps.pop()));
        }
    }
    
    public void outAImplicationExpr(AImplicationExpr node) {
        PImplyOp pOp = node.getImplyOp();
        LogicalExpression le2 = (LogicalExpression)logexps.pop();
        LogicalExpression le1 = (LogicalExpression)logexps.pop();
        if (pOp instanceof AImpliesImplyOp) {
            logexps.add(lFactory.createImplication(
                    (LogicalExpression)le1, 
                    (LogicalExpression)le2));
        }
        else if (pOp instanceof AEquivalentImplyOp) {
            logexps.add(lFactory.createEquivalence(
                    (LogicalExpression)le1, 
                    (LogicalExpression)le2));
        }
        else {
            logexps.add(lFactory.createInverseImplication(
                    (LogicalExpression)le1, 
                    (LogicalExpression)le2));
        }
    }
}
