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

import org.deri.wsmo4j.io.parser.*;
import org.omwg.logicalexpression.*;
import org.omwg.logicalexpression.terms.*;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.factory.*;
import org.wsmo.wsml.*;
import org.wsmo.wsml.compiler.node.*;

import com.ontotext.wsmo4j.common.*;
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
public class AtomicExpressionAnalysis extends ASTAnalysis {
    private LogicalExpressionFactory lFactory;
    private WsmoFactory factory;
    private ASTAnalysisContainer container;
    
    private Stack logexps; // handle to logexp stack
    private Stack terms; // handle to terms stack
    private Stack termLists; // handle to term lists stack
    private Stack molecules;
    
    private int tempVarCounter=0;
    private Stack mathAtomsToRewrite=new Stack();
    
    
    public AtomicExpressionAnalysis(ASTAnalysisContainer container, 
            WsmoFactory factory,
            LogicalExpressionFactory lFactory) {
        
        if (container == null || lFactory == null || factory == null) {
            throw new IllegalArgumentException();
        }
        this.container = container;
        this.lFactory = lFactory;
        this.factory = factory;
        
        // register the handled nodes
        container.registerNodeHandler(AAtomSimple.class, this);
        container.registerNodeHandler(AComparison.class, this);
        container.registerNodeHandler(AAttrDefAttrRelation.class, this);
        container.registerNodeHandler(AAttrValAttrRelation.class, this);
        container.registerNodeHandler(AAttributeMoleculeMolecule.class, this);
        container.registerNodeHandler(AConceptMoleculeNonpreferredMolecule.class, this);
        container.registerNodeHandler(AConceptMoleculePreferredMolecule.class, this);
        container.registerNodeHandler(AMoleculeSimple.class, this);

        container.registerNodeHandler(AMultVal.class, this);
        container.registerNodeHandler(AMultiplicationMultVal.class, this);
        container.registerNodeHandler(ASimpleAdditionArithVal.class, this);
        container.registerNodeHandler(ASemisimple2AdditionArithVal.class, this);
        container.registerNodeHandler(ASemisimple1AdditionArithVal.class, this);
        container.registerNodeHandler(AAdditionArithVal.class, this);

        //shortcuts
        logexps = container.getStack(LogicalExpression.class);
        terms = container.getStack(Term.class);
        termLists = container.getStack(Term[].class);
        molecules = container.getStack(Molecule.class);

    }
    

    public void outAAtomSimple(AAtomSimple node) {
        Term term =null;
        if (!terms.isEmpty()){
            term = (Term)terms.pop();
        }
        if (term instanceof ConstructedTerm){
            ConstructedTerm t = (ConstructedTerm)term;
            Vector v = new Vector();
            for(int i=0; i<t.getArity(); i++){
                v.add(t.getParameter(i));
            }
            logexps.add(lFactory.createAtom(
                    (IRI) t.getFunctionSymbol(),v));
            rewriteMathAtoms();
            return;
        }
        if (term instanceof IRIImpl){
            logexps.add(lFactory.createAtom(
                    (IRI) term,
                    new Vector()));
            rewriteMathAtoms();
            return;
        }
        ParserException e = new ParserException("Found Invalid Atom: "+node, null);
        Token t = null;
        if (node.getTerm() instanceof AVarTerm){
            t=((AVarTerm)node.getTerm()).getVariable();
        }
        if (node.getTerm() instanceof ANbAnonymousTerm){
            t=((ANbAnonymousTerm)node.getTerm()).getNbAnonymous();
        }
        if (node.getTerm() instanceof ADataTerm){
            PValue pv =((ADataTerm)node.getTerm()).getValue();
            if (pv instanceof AStringValue){
                t=((AStringValue)pv).getString();
            }
            if (pv instanceof ANumericValue){
                PNumber pn = ((ANumericValue)pv).getNumber();
                if (pn instanceof ADecimalNumber){
                    t=((ADecimal)((ADecimalNumber)pn).getDecimal()).getPosDecimal();
                }
                if (pn instanceof AIntegerNumber){
                    t=((AInteger)((AIntegerNumber)pn).getInteger()).getPosInteger();
                }
            }
            if (pv instanceof ADatatypeValue){
                PFunctionsymbol pf = ((ADatatypeValue)pv).getFunctionsymbol();
                if (pf instanceof AMathFunctionsymbol){
                    t = ((AMathFunctionsymbol)pf).getLpar();
                }
                if (pf instanceof AParametrizedFunctionsymbol){
                    t = ((AParametrizedFunctionsymbol)pf).getLpar();
                }
            }
        }
        if (t!=null){
            e.setErrorLine(t.getLine());
            e.setErrorPos(t.getPos());
        }
        e.setFoundToken(node.toString());
        e.setExpectedToken("IRI or sQName");
        throw new WrappedParsingException(e);
    }
    
    public void outAComparison(AComparison node) {
        Vector v = new Vector();
        Term te2 = (Term)terms.pop();
        Term te1 = (Term)terms.pop();
        v.add(te1);
        v.add(te2);
        Identifier op = determineCompOp(node.getCompOp(),te1,te2);
        logexps.add(lFactory.createAtom(op, v));
        rewriteMathAtoms();
    }
    
    public void outAAttrDefAttrRelation(AAttrDefAttrRelation node) {
        Term attid = (Term) terms.pop();
        Term mId = (Term) terms.peek();
        Term[] values = (Term[]) termLists.pop();

        if (node.getAttrDefOp() instanceof AImpliestypeAttrDefOp){
            for (int i=0; i<values.length; i++){
                molecules.add(lFactory.createAttributeInference(
                        mId, attid,values[i]));
            }
        }
        else{
            for (int i=0; i<values.length; i++){
                molecules.add(lFactory.createAttributeConstraint(
                        mId, attid,values[i]));
            }
        }
    }

    public void outAAttrValAttrRelation(AAttrValAttrRelation node) {
        Term attid = (Term) terms.pop();
        Term mId = (Term) terms.peek();
        Term[] values = (Term[]) termLists.pop();
        for (int i=0; i<values.length; i++){
            molecules.add(lFactory.createAttributeValue(
                    mId, attid,values[i]));
        }
    }

    public void outAAttributeMoleculeMolecule(AAttributeMoleculeMolecule node) {
        //clean termstack from id
        terms.pop();
    }


    public void outAConceptMoleculeNonpreferredMolecule(AConceptMoleculeNonpreferredMolecule node) {
        moleculeCptHelper(node.getCptOp());
    }
    
    public void outAConceptMoleculePreferredMolecule(AConceptMoleculePreferredMolecule node) {
        moleculeCptHelper(node.getCptOp());
    }

    private void moleculeCptHelper(PCptOp node){
        Term [] conceptOrMemberOfTerms = (Term[])termLists.pop();
        Term molId = (Term) terms.pop();
        if (node instanceof AMemberofCptOp){
            for (int i=0; i<conceptOrMemberOfTerms.length; i++){
                molecules.add(lFactory.createMemberShipMolecule(
                        molId, conceptOrMemberOfTerms[i]));
            }
        }
        else{
            for (int i=0; i<conceptOrMemberOfTerms.length; i++){
                molecules.add(lFactory.createSubConceptMolecule(
                        molId, conceptOrMemberOfTerms[i]));
            }
        }
    }

    public void outAMoleculeSimple(AMoleculeSimple node) {
        if (molecules.size()==1){
            logexps.add(molecules.pop());
        }
        else{
            logexps.add(lFactory.createCompoundMolecule(new Vector(molecules)));
            molecules.clear();
        }
        rewriteMathAtoms();
    }
    
    //[arith] +- [mul]
    public void outAAdditionArithVal(AAdditionArithVal node) {
        Term t2 = (Term)terms.pop(); 
        createPredicate(getFSIRI(node.getArithOp()),
                (Term)terms.pop(),t2);
    }

    //term +- [mul]
    public void outASemisimple1AdditionArithVal(ASemisimple1AdditionArithVal node) {
        Term t2 = (Term)terms.pop(); 
        createPredicate(getFSIRI(node.getArithOp()),
                (Term)terms.pop(),t2);
    }

    //[arith] +- term
    public void outASemisimple2AdditionArithVal(ASemisimple2AdditionArithVal node) {
        Term t2 = (Term)terms.pop(); 
        createPredicate(getFSIRI(node.getArithOp()),
                (Term)terms.pop(),t2);
    }
    //term +- term
    public void outASimpleAdditionArithVal(ASimpleAdditionArithVal node) {
        Term t2 = (Term)terms.pop(); 
        createPredicate(getFSIRI(node.getArithOp()),
                (Term)terms.pop(),t2);
    }

    //[mul] */ term
   public void outAMultiplicationMultVal(AMultiplicationMultVal node) {
       Term t2 = (Term)terms.pop(); 
       createPredicate(getFSIRI(node.getMulOp()),
               (Term)terms.pop(),t2);
   }

    //term */ term
   public void outAMultVal(AMultVal node) {
       Term t2 = (Term)terms.pop(); 
       createPredicate(getFSIRI(node.getMulOp()),
               (Term)terms.pop(),t2);
   }
   
   private void createPredicate(IRI iri, Term left, Term right){
       Variable v = new TempVariable(tempVarCounter++);
       List params = new Vector();
       params.add(v);
       params.add(left);
       params.add(right);
       mathAtomsToRewrite.push(lFactory.createAtom(iri,params));
       terms.add(v);
   }
   
   private void rewriteMathAtoms() {
       while (!mathAtomsToRewrite.isEmpty()){
           LogicalExpression left = (LogicalExpression) logexps.pop();
           Atom right = (Atom) mathAtomsToRewrite.pop();
           logexps.push(lFactory.createConjunction(left, right));
       }
   }

    private IRI getFSIRI(PArithOp op) {
        if (op instanceof AAddArithOp) {
            //add
            return factory.createIRI(Constants.NUMERIC_ADD);
        }
        //sub
        return factory.createIRI(Constants.NUMERIC_SUB);
    }

    private IRI getFSIRI(PMulOp op) {
        if (op instanceof AMulMulOp) {
            //mult
            return factory.createIRI(Constants.NUMERIC_MUL);
        }
        //div
        return factory.createIRI(Constants.NUMERIC_DIV);
    }
    
    
    private Identifier determineCompOp(PCompOp cOp, Term te1, Term te2) {
        Identifier op;
        if (cOp instanceof AEqualCompOp) {
            //equal, stringEqual, numericEqual
            if(isNumeric(te1) && isNumeric(te2)) {
                op = factory.createIRI(Constants.NUMERIC_EQUAL);
            } else if (isString(te1) && isString(te2)){
                op = factory.createIRI(Constants.STRING_EQUAL);
            } else {
                op = factory.createIRI(Constants.EQUAL);
            }
        }
        else if (cOp instanceof AGtCompOp) {
            op = factory.createIRI(Constants.GREATER_THAN);
        }
        else if (cOp instanceof AGteCompOp) {
            op = factory.createIRI(Constants.GREATER_EQUAL);
        }
        else if (cOp instanceof ALtCompOp) {
            op = factory.createIRI(Constants.LESS_THAN);
        }
        else if (cOp instanceof ALteCompOp) {
            op = factory.createIRI(Constants.LESS_EQUAL);
        }
        else if (cOp instanceof AStrongEqualCompOp) {
            op = factory.createIRI(Constants.STRONG_EQUAL);
        }
        else {
            //inequal, stringInequal, numericInequal
            if(isNumeric(te1) && isNumeric(te2)) {
                op = factory.createIRI(Constants.NUMERIC_INEQUAL);
            } else if (isString(te1) && isString(te2)){
                op = factory.createIRI(Constants.STRING_INEQUAL);
            } else {
                op = factory.createIRI(Constants.INEQUAL);
            }
        }
        return op;
    }
    
    //TODO more clever analysis (e.g. for variable)
    private boolean isNumeric(Term t) {
        if(t instanceof DataValue ) { 
            String s = ((DataValue)t).getType().getIRI().toString();
            if( s.equals(WsmlDataType.WSML_DOUBLE) 
                    || s.equals(WsmlDataType.WSML_FLOAT)
                    || s.equals(WsmlDataType.WSML_DECIMAL)
                    || s.equals(WsmlDataType.WSML_INTEGER) ) {
                    return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    private boolean isString(Term t) {
        if(t instanceof DataValue ) { 
            String s = ((DataValue)t).getType().getIRI().toString();
            if( s.equals(WsmlDataType.WSML_STRING)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
