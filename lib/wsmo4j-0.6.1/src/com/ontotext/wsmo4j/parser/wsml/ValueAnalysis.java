/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2004-2005, OntoText Lab. / SIRMA

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

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 */

package com.ontotext.wsmo4j.parser.wsml;

import java.util.*;

import org.deri.wsmo4j.io.parser.WrappedParsingException;
import org.deri.wsmo4j.logicalexpression.*;
import org.omwg.logicalexpression.terms.*;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.factory.*;
import org.wsmo.wsml.ParserException;
import org.wsmo.wsml.compiler.node.*;

public class ValueAnalysis extends ASTAnalysis {

    private DataFactory dFactory;
    private LogicalExpressionFactory lFactory;
    private ASTAnalysisContainer container;
    private WsmoFactory factory;

    private Stack termStack;
    private Stack lastTermBeforeFunctionSymbol = new Stack();
    private ConstantTransformer cf = ConstantTransformer.getInstance();

    public ValueAnalysis(ASTAnalysisContainer container,
            DataFactory dFactory,
            LogicalExpressionFactory lFactory,
            WsmoFactory factory) {
        if (container == null || dFactory == null || lFactory == null) {
            throw new IllegalArgumentException();
        }
        this.dFactory = dFactory;
        this.lFactory = lFactory;
        this.factory = factory;
        this.container = container;
        termStack = container.getStack(Term.class);

        // register the handled nodes
        container.registerNodeHandler(AVarTerm.class, this);
        container.registerNodeHandler(AParametrizedFunctionsymbol.class, this);
        container.registerNodeHandler(ADecimalNumber.class, this);
        container.registerNodeHandler(AIntegerNumber.class, this);
        container.registerNodeHandler(AStringValue.class, this);
        container.registerNodeHandler(ATermlist.class, this);
        container.registerNodeHandler(ATermTermlist.class, this);
        container.registerNodeHandler(ATermValue.class, this);
        container.registerNodeHandler(ATermValuelist.class, this);
        container.registerNodeHandler(AValuelistValuelist.class, this);
        container.registerNodeHandler(ANbAnonymousTerm.class, this);
        
    }

    public void inAParametrizedFunctionsymbol(AParametrizedFunctionsymbol node) {
        if (!termStack.isEmpty()){
            lastTermBeforeFunctionSymbol.add(termStack.peek());
        }
    }

    // PValueList types

    Object lastItem;

    public void inATermValuelist(ATermValuelist node) {
        if (!termStack.isEmpty())
            lastItem = termStack.peek();
    }

    public void outATermValuelist(ATermValuelist node) {
        if (termStack.isEmpty()) {
            throw new RuntimeException("No data value nodes processed!");
        }

        Vector terms = new Vector();
        while (!termStack.isEmpty() &&
                termStack.peek() != lastItem) {
            terms.add(0, termStack.pop());
        }

        container.getStack(Term[].class).push(terms.toArray(new Term[]{}));
    }

    public void inAValuelistValuelist(AValuelistValuelist node) {
        inATermValuelist(null); // same implementation
    }

    public void outAValuelistValuelist(AValuelistValuelist node) {
        outATermValuelist(null); // same implementation
    }

    // PValue types

    public void outAParametrizedFunctionsymbol(AParametrizedFunctionsymbol node) {
        IRI type = (IRI) container.popFromStack(Identifier.class, IRI.class);
        Vector args = new Vector();
        Object last = null;
        if (!lastTermBeforeFunctionSymbol.isEmpty()){
            last=lastTermBeforeFunctionSymbol.pop();
        }
        while (!termStack.isEmpty() &&
                termStack.peek()!=last){
            args.add(0,termStack.pop());
        }

        if (cf.isSimpleDataType(type.toString())) {
            termStack.push(args.get(0));
        }
        else if (cf.isDataType(type.toString())) {
            ComplexDataType complexType = (ComplexDataType) dFactory.createWsmlDataType(type);
            if (complexType.getIRI().toString().equals(WsmlDataType.WSML_IRI)) {
                termStack.push(args.firstElement());
            }
            else if (complexType.getIRI().toString().equals(WsmlDataType.WSML_SQNAME) &&
                    args.size() == 2) {
                AAnySqname sqname = new AAnySqname();
                sqname.setName(new TName(args.get(1).toString()));
                sqname.setPrefix(new APrefix(new TName(args.get(0).toString()), new THash()));
                sqname.apply(container.getNodeHandler(PId.class));
                termStack.push(container.getStack(Identifier.class).pop());
            }
            else {
                if (args.firstElement() instanceof SimpleDataValue == false)  {
                    ParserException pe = new ParserException("Data type has no valid value", null);
                    pe.setExpectedToken("Value");
                    pe.setErrorLine(node.getLpar().getLine());
                    pe.setErrorPos(node.getLpar().getPos()+1);
                    throw new WrappedParsingException(pe);
                    }
                termStack.push(dFactory.createDataValue(complexType, 
                        (SimpleDataValue[]) args.toArray(new SimpleDataValue[]{})));
                }
        }
        else{ //no DATATYPE
            termStack.add(lFactory.createConstructedTerm(type,args));
        }
    }

    public void inADecimalNumber(ADecimalNumber node) {
        termStack.push(dFactory.createWsmlDecimal(node.toString().trim()));
    }

    public void inAIntegerNumber(AIntegerNumber node) {
        String intAsString = node.getInteger().toString().trim();
        // sablecc does change "-5" to "- 5" which will later not be parsed
        // correctly
        if (intAsString.length() >= 2 && intAsString.charAt(1) == ' ') {
            intAsString = "-" + intAsString.substring(2);

        }
        termStack.push(dFactory.createWsmlInteger(intAsString));
    }

    public void inAStringValue(AStringValue node) {
        String str = node.getString().toString();
        //get rid of quotes
        str = str.substring(1,str.length()-2);
        //unescape
        str = str.replaceAll("\\\\\\\\", "\\\\");
        str = str.replaceAll("\\\\\"","\"");

        termStack.push(dFactory.createWsmlString(str));
    }

    public void inATermValue(ATermValue node) {
        node.getId().apply(container.getNodeHandler(PId.class));
        termStack.push((Term) container.getStack(Identifier.class).pop());
    }

    public void inAVarTerm(AVarTerm node) {
        termStack.push(lFactory.createVariable(
                node.getVariable().getText()));
    }

    public void outATermTermlist(ATermTermlist node) {
        container.getStack(Term[].class).push(
                new Term[]{(Term)termStack.pop()});
    }

    public void inATermlist(ATermlist node) {
        if (!termStack.isEmpty())
            lastItem = termStack.peek();
    }

    public void outATermlist(ATermlist node) {
        Vector terms = new Vector();
        while (!termStack.isEmpty() &&
                termStack.peek() != lastItem) {
            terms.add(0, termStack.pop());
        }
        container.getStack(Term[].class).push(terms.toArray(new Term[]{}));
    }

    public void inANbAnonymousTerm(ANbAnonymousTerm arg0) {
        String no  = arg0.getNbAnonymous().getText();
        no = no.substring(2);
        byte number = (byte)Integer.parseInt(no);
        termStack.push(lFactory.createAnonymousID(number));
    }
}

/*
 * $Log: ValueAnalysis.java,v $
 * Revision 1.9  2006/11/10 14:46:21  holgerlausen
 * fixed bug with negative ints
 *
 * Revision 1.8  2006/11/08 12:02:48  vassil_momtchev
 * fixed bug when parsed data value "_boolean(IRI)"
 *
 * Revision 1.7  2006/11/03 07:17:32  holgerlausen
 * fixed a bug in parsing nested function symbols (only arguments of last functionsymbol where added correctly but args of rest has been ignored)
 *
 * Revision 1.6  2006/06/21 07:46:29  vassil_momtchev
 * createVariable(String) method moved from WsmoFactory to LogicalExpressionFactory interface
 *
 * Revision 1.5  2006/03/06 14:58:41  vassil_momtchev
 * unused imports removed
 *
 * Revision 1.4  2006/03/06 14:55:45  vassil_momtchev
 * _iri(_"http://iri...") and _sqname("prefix", "localpart") construction handled also
 *
 * Revision 1.3  2005/12/02 14:11:59  holgerlausen
 * fixed the handling of escpaed character during parsing and serialization
 *
 * Revision 1.2  2005/12/02 12:47:59  holgerlausen
 * numbered anon id processing added
 *
 * Revision 1.1  2005/11/28 13:55:26  vassil_momtchev
 * AST analyses
 *
*/
