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


import java.io.*;
import java.util.*;

import org.deri.wsmo4j.io.parser.*;
import org.omwg.logicalexpression.*;
import org.omwg.logicalexpression.terms.Visitor;
import org.omwg.ontology.Axiom;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.factory.*;
import org.wsmo.wsml.ParserException;
import org.wsmo.wsml.compiler.analysis.*;
import org.wsmo.wsml.compiler.lexer.*;
import org.wsmo.wsml.compiler.node.*;
import org.wsmo.wsml.compiler.parser.Parser;

import com.ontotext.wsmo4j.common.*;
import com.ontotext.wsmo4j.parser.wsml.*;


/**
 * Accespoint for parsin LogicalExpressions (used by general wsml parser
 * and to create objectmodels from arbitrary LE strings)
 */
public class LogExprParserImpl {

    private ASTAnalysisContainer container;

    /**
     *
     * @param map optional set to factories to be used during parsing
     * (PARSER_WSMO_FACTORY PARSER_LE_FACTORY PARSER_DATA_FACTORY)
     * @param nsHolder gives available namespaces
     */
    public LogExprParserImpl(Map map, TopEntity nsHolder) {
        if (map==null) map = new HashMap();
        Object o = map.get(Factory.WSMO_FACTORY);
        if (o == null || ! (o instanceof WsmoFactory)) {
            o = Factory.createWsmoFactory(new HashMap <String, Object> ());
        }
        WsmoFactory factory = (WsmoFactory)o;
        assert (factory != null);

        o = map.get(Factory.LE_FACTORY);
        if (o == null || ! (o instanceof LogicalExpressionFactory)) {
            o = Factory.createLogicalExpressionFactory(new HashMap <String, Object> ());
        }
        LogicalExpressionFactory leFactory = (LogicalExpressionFactory)o;
        assert (leFactory != null);

        o = map.get(Factory.DATA_FACTORY);
        if (o == null || ! (o instanceof DataFactory)) {
            o = Factory.createDataFactory(new HashMap <String, Object> ());
        }
        DataFactory dataFactory = (DataFactory)o;
        assert (dataFactory != null);

        container = new ASTAnalysisContainer();

        HashMap <String, String> ns = new HashMap <String, String>();
        if (nsHolder!=null){
            if (nsHolder.getDefaultNamespace()!=null &&
                    nsHolder.getDefaultNamespace().getIRI()!=null){
                ns.put("_", nsHolder.getDefaultNamespace().getIRI().toString());
            }
            for (Iterator i = nsHolder.listNamespaces().iterator(); i.hasNext();){
                Namespace namespace = (Namespace) i.next();
                ns.put(namespace.getPrefix(),
                        namespace.getIRI().toString());
            }
        }
        new IdentifierAnalysis(container, factory).setNameSpaces(ns);
        new ValueAnalysis(container, dataFactory, leFactory, factory);
        new CompoundExpressionAnalysis(container, leFactory);
        new VariableAnalysis(container, leFactory);
        new AtomicExpressionAnalysis(container, factory, leFactory);
    }

    /**
     * This method parses a Node.
     * @param node
     *            SableCC Node that will be parsed, it expects a Node of the
     *            type "PLogExpr"
     * @return logical expression object model
     * @throws RuntimeException in case node is not of Type PLogExpr
     * @see org.omwg.logicalexpression.io.Parser#parse(java.lang.Object)
     */
    public LogicalExpression parse(Object node)
            throws RuntimeException {
        if (node instanceof PLogExpr) {
            ((PLogExpr)node).apply(container);
            return (LogicalExpression)container.getStack(LogicalExpression.class).remove(0);
        }
        throw new RuntimeException("WSML Parser Error: LogExprParser.parse requiers object of type " +
                                   "org.wsmo.wsml.compiler.node.PLogExpr, found" + node.getClass());
    }

    /**
     * @param expr String
     * @return logical expression object model
     * @throws ParserException
     * @throws InvalidModelException
     * @see org.omwg.logicalexpression.io.Parser#parse(java.lang.String)
     */
    /* TODO This method looks like a good point of attack for optimization. It's eats up a lot more time for
             * it's own computations (1709) than for it's callouts to subroutines (737) in the org.wsmo.wsml.compiler.*  packages.
     *
     * 100,0% - 2.446 ms - inherent 1.709 ms - 1 invocation org.deri.wsmo4j.io.parser.wsml.LogExprParserImpl.parse
     * 15,1% - 369 ms - inherent 0 ms - 1 invocation org.wsmo.wsml.compiler.parser.Parser.<init>
     * 6,2% - 151 ms - inherent 0 ms - 1 invocation org.wsmo.wsml.compiler.lexer.Lexer.<init>
     * 5,9% - 143 ms - inherent 0 ms - 1 invocation org.wsmo.wsml.compiler.node.Start.apply
     * 2,9% - 72 ms - inherent 0 ms - 1 invocation org.wsmo.wsml.compiler.parser.Parser.parse
             * 0,0% - 0 ms - inherent 0 ms - 1 invocation org.deri.wsmo4j.io.parser.wsml.LogExprParserImpl$LogExpProcesser.<init>
     * 0,0% - 0 ms - inherent 0 ms - 1 invocation java.io.StringReader.<init>
     * 0,0% - 0 ms - inherent 0 ms - 1 invocation java.io.PushbackReader.<init>
     * 0,0% - 0 ms - inherent 0 ms - 1 invocation java.util.Vector.<init>
     * 0,0% - 0 ms - inherent 0 ms - 1 invocation java.lang.StringBuilder.<init>
     * 0,0% - 0 ms - inherent 0 ms - 2 invocation java.lang.StringBuilder.append
     * 0,0% - 0 ms - inherent 0 ms - 2 invocation java.lang.String.trim
     * 0,0% - 0 ms - inherent 0 ms - 1 invocation java.lang.StringBuilder.toString
     * 0,0% - 0 ms - inherent 0 ms - 1 invocation java.lang.String.lastIndexOf
     * 0,0% - 0 ms - inherent 0 ms - 1 invocation java.util.Vector.firstElement
     * 0,0% - 0 ms - inherent 0 ms - 1 invocation java.lang.String.length
     */
    public org.omwg.logicalexpression.LogicalExpression parse(String expr)
            throws ParserException {
        //System.out.println(expr.lastIndexOf(".")+"-"+(expr.length()-1));
        if (expr.trim().lastIndexOf(".") != expr.trim().length() - 1) {
            expr += ".";
        }
        
        container.getStack(String.class).add(expr);
        
        expr = "ontology o987654321 axiom a987654321 definedBy \n" + expr + "  \n";

        //to avoid pushbackoverflow increase size of buffer.
        Lexer lexer = new Lexer(new PushbackReader(new StringReader(expr),65536));
        Parser parser = new Parser(lexer);
        Vector axiom = new Vector();
        Start head;
        try {
            head = parser.parse();
        }
        catch (org.wsmo.wsml.compiler.parser.ParserException pe) {
            ParserException e = new ParserException(ParserException.NOT_VALID_PARSETREE, pe);
            Token t = pe.getToken();
            e.setErrorLine(t.getLine() - 1);
            e.setErrorPos(t.getPos());
            e.setFoundToken(t.getText());
            try {
                e.setExpectedToken(pe.getMessage().split("expecting: ")[1].trim());
            }
            catch (IndexOutOfBoundsException iobE) {
                //if error message does not follow usual pattern
                e.setExpectedToken(pe.getMessage());
            }
            throw e;
        }
        catch (org.wsmo.wsml.compiler.lexer.LexerException le) {
            ParserException e = new ParserException(ParserException.NOT_VALID_PARSETREE, le);
            try {
                e.setErrorLine(Integer.parseInt(le.getMessage().split(",")[0].split("\\[")[1]));
            }
            catch (NumberFormatException nfE) {
                //could not find line so leave default
            }
            try {
                e.setErrorPos(Integer.parseInt(le.getMessage().split(",")[1].split("\\]")[0]));
            }
            catch (NumberFormatException nfE) {
                //could not find pos so leave default
            }
            throw e;
        }
        catch (IOException ioe) {
            //should never happen logexp is never allone in a file
            throw new RuntimeException(ioe);
        }
        try {
            head.apply(new LogExpProcesser(
                    axiom));
        }
        catch (WrappedParsingException e) {
            if (e.getWrappedException()instanceof ParserException) {
                ParserException pe = (ParserException)e.getWrappedException();
                pe.setErrorLine(pe.getErrorLine() - 1);
                throw pe;
            }
            else if (e.getWrappedException()instanceof RuntimeException) {
                throw (RuntimeException)e.getWrappedException();
            }
            else {
                throw e;
            }
        }
        return (LogicalExpression)axiom.firstElement();
    }

    /*
     * <p>Helper class for parsing logical expressions</p>
     * @author reto.krummenacher@deri.org, holger.lausen@deri.org
     *
     */
    private class LogExpProcesser
            extends DepthFirstAdapter {

        private Vector <LogicalExpression> axioms;

        LogExpProcesser(Vector <LogicalExpression> axioms) {
            this.axioms = axioms;
        }

        public void inALogDefinition(ALogDefinition node) {
            axioms.add(parse(node.getLogExpr().get(0)));
        }
    }

    static TopEntity copyNsHolder(TopEntity nsHolder){
        //copy the nsHolder otherwise problems with still holding
        //reference to some entities that do not garbage collected!
        if (nsHolder==null){
            return null;
        }
        Identifier dummyID = new UnnumberedAnonymousID(){public void accept(Visitor v){};};
        TopEntity nsHolderCopy = new TopEntityImpl(dummyID);
        nsHolderCopy.setDefaultNamespace(nsHolder.getDefaultNamespace());
        Iterator i = nsHolder.listNamespaces().iterator();
        while(i.hasNext()){
            nsHolderCopy.addNamespace((Namespace)i.next());
        }
        return nsHolderCopy;
    }
    
    
    /*
     * puts all Logical Expression Strings of a Reader on a Stack and 
     * returns a new Reader (reading from beginning)
     */
    public static Reader findLogicalExpressions(Reader src, Stack allLogExp){
        StringBuffer buffer = new StringBuffer();
        try{
            boolean inALineComment = false;
            boolean inAString = false;
            boolean inAIRI = false;
            boolean inAComment = false;
            boolean inALogexp = false;
            char current = 0;
            char previous = 0;
            char preprevious = 0;
            char[] r = new char[9];
            char[] def = "definedBy".toCharArray();
            StringBuffer logexp = new StringBuffer();
            while(src.ready()){
                current = (char)src.read();
                if (current == 65535) break;
                buffer.append(current);
                
                if (!inAString && ! inAIRI && current=='/' && previous=='/'){
                    inALineComment=true;
                }
                if (inALineComment && current=='\n'){
                    inALineComment=false;
                }
                
                if (!inAString && ! inAIRI && current=='*' && previous=='/'){
                    inAComment=true;
                }
                if (inAComment && current=='/' && previous=='*'){
                    inAComment=false;
                }
                
                if (current=='"' && previous=='_'){
                    inAIRI=true;
                }
                if (inAIRI && current=='"' && previous!='\\'){
                    inAIRI=false;
                }
                
                if (current=='"' && previous!='_'  && previous!='\\'){
                    inAString=true;
                }
                if (inAString && current=='"' && previous!='\\'){
                    inAString=false;
                }
                
                r[0]=r[1];
                r[1]=r[2];
                r[2]=r[3];
                r[3]=r[4];
                r[4]=r[5];
                r[5]=r[6];
                r[6]=r[7];
                r[7]=r[8];
                r[8]=current;
                String str = r.toString();
                if (inALogexp){
                    logexp.append(current);
                }
                
                if (!inAComment && !inAIRI && !inALineComment && !inAString && Arrays.equals(r, def)){
                    inALogexp=true;
                }
                if (inALogexp && !inAComment && !inALineComment && current =='.' && previous != '\\'){
                    allLogExp.add(logexp.toString().trim());
                    logexp.delete(0, logexp.length());
                }
                if (inALogexp && !inAComment && !inALineComment && isAxiomOrRelationOrConceptOrInstance(r)){
                    logexp.delete(0, logexp.length());
                    inALogexp=false;
                }
                preprevious=previous;
                previous=current;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return new StringReader(buffer.toString());
    }
    
    private static String[] words = new String[]{
        "axiom","relation", "relationInstance",
        "concept","instance",
        "webService", "mediator", "ontology", "goal"};
    
    private static boolean isAxiomOrRelationOrConceptOrInstance(char[] c){
        for (int i=0; i<words.length; i++){
            boolean check=true;
            int m=0;
            for(int n=words[i].length()-1; n>=0 && 8-m>=0; n--){
                //System.out.println(words[i]+"-"+" i"+i+" n"+n);
                if(words[i].charAt(n)!=c[8-m++]){
                    check=false;
                    break;
                }
            }
            if (check) return true;
        }
        return false;
    }

}
