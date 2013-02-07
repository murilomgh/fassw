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
package org.deri.wsmo4j.io.parser.xml;


import java.math.*;
import java.util.*;

import org.deri.wsmo4j.logicalexpression.*;
import org.omwg.logicalexpression.*;
import org.omwg.logicalexpression.terms.*;
import org.omwg.ontology.Variable;
import org.w3c.dom.*;
import org.wsmo.common.*;
import org.wsmo.factory.*;
import org.wsmo.wsml.*;


/**
 * @author retkru
 * @see org.deri.wsmo4j.io.parser.xml.LogExprParserImpl
 */
public class XMLExprParser {

    DataFactory df;
    LogicalExpressionFactory leFactory;
    WsmoFactory factory;

    public XMLExprParser(WsmoFactory factory, 
            LogicalExpressionFactory leFactory,  DataFactory df){
        this.df=df;
        this.leFactory=leFactory;
        this.factory=factory;
    }
    
    /**
     * Recursively builds logical expressions
     * @param exprNode SableCC Node that will be parsed, it expects a Node of the
     *                 type "PLogExpr"
     * @return logical expression object model
     * @throws ParserException in case exprNode is an invalid node or in case
     * a wrong element name for a term is submitted to the evaluateXMLTerm method
     * @see org.deri.wsmo4j.io.parser.xml.LogExprParserImpl#parse(Object)
     */
    public LogicalExpression evaluateXML(Element exprNode)
            throws ParserException {
        String exprName = exprNode.getNodeName();
        LogicalExpression logExpr = null;
        //resolve all non-text child nodes, in that way we can ensure not
        //to fall into the trap of whitespace text
        List exprs = null;
        if (exprName.equals("and")) {
            exprs = childElements(exprNode.getChildNodes());
            logExpr = evaluateXMLAnd(exprs);
        }
        else if (exprName.equals("or")) {
            exprs = childElements(exprNode.getChildNodes());
            logExpr = evaluateXMLOr(exprs);
        }
        else if (exprName.equals("forall")) {
            exprs = childElements(exprNode.getChildNodes());
            logExpr = evaluateXMLForAll(exprs);
        }
        else if (exprName.equals("exists")) {
            exprs = childElements(exprNode.getChildNodes());
            logExpr = evaluateXMLExists(exprs);
        }
        else if (exprName.equals("equivalent")) {
            exprs = childElements(exprNode.getChildNodes());
            logExpr = evaluateXMLEquals(exprs);
        }
        else if (exprName.equals("impliedBy")) {
            exprs = childElements(exprNode.getChildNodes());
            logExpr = evaluateXMLImpliedBy(exprs);
        }
        else if (exprName.equals("implies")) {
            exprs = childElements(exprNode.getChildNodes());
            logExpr = evaluateXMLImplies(exprs);
        }
        else if (exprName.equals("impliedByLP")) {
            exprs = childElements(exprNode.getChildNodes());
            logExpr = evaluateXMLImpliesLP(exprs);
        }
        else if (exprName.equals("naf")) {
            exprs = childElements(exprNode.getChildNodes());
            logExpr = evaluateXMLNaf(exprs);
        }
        else if (exprName.equals("neg")) {
            exprs = childElements(exprNode.getChildNodes());
            logExpr = evaluateXMLNeg(exprs);
        }
        else if (exprName.equals("constraint")) {
            exprs = childElements(exprNode.getChildNodes());
            logExpr = evaluateXMLConstraing(exprs);
        }
        else if (exprName.equals("molecule")) {
            NodeList cptE = exprNode.getElementsByTagName("isa");
            NodeList attrValue = exprNode.getElementsByTagName("attributeValue");
            NodeList attrDef = exprNode.getElementsByTagName("attributeDefinition");

            Term t = evaluateXMLTerm((Element)exprNode.getElementsByTagName("term").item(0));
            List <Molecule> molecules = new Vector <Molecule>();

            if (attrValue.getLength() > 0 || attrDef.getLength() > 0) {
                if (attrValue.getLength() > 0) {
                    molecules.addAll(evaluateXMLAttrValue(attrValue, t));
                }
                if (attrDef.getLength() > 0) {
                    molecules.addAll(evaluateXMLAttrDef(attrDef, t));
                }
            }
            if (cptE.getLength() > 0) {
                Node isa = cptE.item(0);
                String type = isa.getAttributes().getNamedItem("type").getNodeValue();
                if (type.equals("memberOf")) {
                    Iterator types = evaluateXMLIsa(cptE).iterator();
                    while (types.hasNext()){
                        molecules.add(leFactory.createMemberShipMolecule(
                                t,(Term)types.next()));
                    }
                }
                else if (type.equals("subConceptOf")) {
                    Iterator types = evaluateXMLIsa(cptE).iterator();
                    while (types.hasNext()){
                        molecules.add(leFactory.createSubConceptMolecule(
                                t,(Term)types.next()));
                    }
                }
            }
            if (molecules.size()==0){
                throw new ParserException("XML Parse Error: Molecule element without content detected! ("+t+")", null);
            }
            else if (molecules.size()==1){
                logExpr = molecules.remove(0);
            }
            else {
                logExpr = leFactory.createCompoundMolecule(molecules);
            }
        }
        else if (exprName.equals("atom")) {
            logExpr = evaluateXMLConstructed(exprNode, factory.createIRI(exprNode.getAttribute("name")));

            /*} else if (exprName.equals("term")) {
                args.add(_().createLEIRI(exprNode.getAttribute("name")));
                logExpr = _().createLogExpression(Operator.ATOM,pOp,args);*/
        }
        else {
            throw new ParserException("XML Parse Error... : " + exprName, null);
        }
        return logExpr;
    }

    /**
     * Creates Variables or basic terms
     * @param exprNode Element
     * @return a Term
     * @throws ParserException in case the Element name is wrong
     */
    private Term evaluateXMLTerm(Element exprNode)
            throws ParserException {
        String exprName = exprNode.getNodeName();
        Term t = null;
        if (exprName.equals("variable")) {
            try {
                t = leFactory.createVariable(exprNode.getAttribute("name").substring(1));
            }
            catch (IllegalArgumentException e) {
                throw new ParserException("XML Parse Error: Zero-length variable not allowed.", e);
            }
            //FIXME: ADD COMPLEX DATAVALUES!
        }
        else if (exprName.equals("term") ||
                 exprName.equals("name") ||
                 exprName.equals("arg") ||
                 exprName.equals("value")) {
            String iriStr = exprNode.getAttribute("name");
            String notation = ConstantTransformer.getInstance().findNotation(iriStr);
            if (notation != null && notation.equals("_integer")) {
                t = df.createWsmlInteger(new BigInteger(exprNode.getFirstChild().getNodeValue()));
            }
            else if (notation != null && notation.equals("_decimal")) {
                t = df.createWsmlDecimal(new BigDecimal(exprNode.getFirstChild().getNodeValue()));
            }
            else if (notation != null && notation.equals("_string")) {
                t = df.createWsmlString(exprNode.getFirstChild().getNodeValue());
            }
            else if (iriStr.equals(Constants.ANONYMOUS_ID)) {
                t = factory.createAnonymousID();
            }
            else if (iriStr.startsWith(Constants.ANONYMOUS_ID)) {
                //numberedID
                t = leFactory.createAnonymousID(Byte.parseByte(iriStr.substring(iriStr.lastIndexOf("ID") + 2)));
            }
            else {
                t = helpEvaluateTerm(exprNode, iriStr);
            }
        }
        else if (exprName.equals("type")) {
            t = factory.createIRI(exprNode.getAttributes().getNamedItem("name").getNodeValue());
        }
        else {
            throw new ParserException("XML Parse Error: wrong element name: " + exprName + " for a term", null);
        }
        return t;
    }

    /**
     * Creates a variable, an IRI or a ConstructedTerm
     * @param exprNode Node
     * @param iriStr
     * @return a Term
     * @throws ParserException in case a wrong element name for a term is submitted
     * to the evaluateXMLTerm method
     */
    private Term helpEvaluateTerm(Element exprNode, String iriStr)
            throws ParserException {
        Term t;
        if (iriStr.startsWith("?")) {
            try {
                t = leFactory.createVariable(iriStr.substring(1));
            }
            catch (IllegalArgumentException e) {
                throw new ParserException("XML Parse Error: Zero-length variable not allowed.", e);
            }
        }
        else {
            IRI iri = factory.createIRI(iriStr);
            if (exprNode.getChildNodes().getLength() > 1) {
                Element e;
                Vector <Term> ll = new Vector <Term>();
                NodeList nl = exprNode.getChildNodes();
                for (int i = 0; i < nl.getLength(); i++) {
                    Node n = nl.item(i);
                    if (n instanceof Element) {
                        e = (Element)n;
                        ll.add(evaluateXMLTerm(e));
                    }
                }
                t = leFactory.createConstructedTerm(iri, ll);
            }
            else {
                t = iri;
            }
        }
        return t;
    }

    /**
     * Builds a Quantified LogicalExpression
     * @param exprs a list of child elements of a specified Node
     * @param op type of operator
     * @return Quantified Logical Expression
     * @throws ParserException in case an invalid node is submitted to the
     * evaluateXML method
     */
    private LogicalExpression evaluateXMLExists(List exprs)
            throws ParserException {
        HashSet <Variable> hs = new HashSet <Variable>();
        for (int i = 0; i < exprs.size() - 1; i++) {
            hs.add((Variable) evaluateXMLTerm((Element)exprs.get(i)));
        }
        return leFactory.createExistentialQuantification(hs, evaluateXML((Element)exprs.get(exprs.size() - 1)));
    }

    private LogicalExpression evaluateXMLForAll(List exprs)
            throws ParserException {
        HashSet  <Variable> hs = new HashSet <Variable> ();
        for (int i = 0; i < exprs.size() - 1; i++) {
            hs.add((Variable) evaluateXMLTerm((Element)exprs.get(i)));
        }
        return leFactory.createUniversalQuantification(hs, evaluateXML((Element)exprs.get(exprs.size() - 1)));
    }

    /**
     * Builds a Binary LogicalExpression
     * @param exprs a list of child elements of a specified Node
     * @param op type of operator
     * @return Binary Logical Expression
     * @throws ParserException in case an invalid node is submitted to the
     * evaluateXML method
     */
    private LogicalExpression evaluateXMLAnd(List exprs)
            throws ParserException {
        return leFactory.createConjunction(evaluateXML((Element)exprs.get(0)), evaluateXML((Element)exprs.get(1)));
    }

    private LogicalExpression evaluateXMLOr(List exprs)
            throws ParserException {
        return leFactory.createDisjunction(evaluateXML((Element)exprs.get(0)), evaluateXML((Element)exprs.get(1)));
    }

    private LogicalExpression evaluateXMLImplies(List exprs)
            throws ParserException {
        return leFactory.createImplication(evaluateXML((Element)exprs.get(0)), evaluateXML((Element)exprs.get(1)));
    }

    private LogicalExpression evaluateXMLImpliesLP(List exprs)
            throws ParserException {
        return leFactory.createLogicProgrammingRule(evaluateXML((Element)exprs.get(0)),
                evaluateXML((Element)exprs.get(1)));
    }

    private LogicalExpression evaluateXMLImpliedBy(List exprs)
            throws ParserException {
        return leFactory.createInverseImplication(evaluateXML((Element)exprs.get(0)), evaluateXML((Element)exprs.get(1)));
    }

    private LogicalExpression evaluateXMLEquals(List exprs)
            throws ParserException {
        return leFactory.createEquivalence(evaluateXML((Element)exprs.get(0)), evaluateXML((Element)exprs.get(1)));
    }

    /**
     * Builds an Unary LogicalExpression
     * @param exprs a list of child elements of a specified Node
     * @param op type of operator
     * @return Unary Logical Expression
     * @throws ParserException in case an invalid node is submitted to the
     * evaluateXML method
     */
    private LogicalExpression evaluateXMLNeg(List exprs)
            throws ParserException {
        return leFactory.createNegation(evaluateXML((Element)exprs.get(0)));
    }

    private LogicalExpression evaluateXMLNaf(List exprs)
            throws ParserException {
        return leFactory.createNegationAsFailure(evaluateXML((Element)exprs.get(0)));
    }

    private LogicalExpression evaluateXMLConstraing(List exprs)
            throws ParserException {
        return leFactory.createConstraint(evaluateXML((Element)exprs.get(0)));
    }

    private Set <Molecule> evaluateXMLAttrValue(NodeList attValue, Term id)
            throws ParserException {
        Set <Molecule> molecules = new HashSet <Molecule> ();
        for (int i = 0; i < attValue.getLength(); i++) {
            Node singleAttVal = attValue.item(i);
            Term attributeName = evaluateXMLTerm((Element)childElements(((Element)singleAttVal).getElementsByTagName("name")).get(0));
            List  attValContent = childElements(((Element)singleAttVal).getElementsByTagName("value"));
            for (int j = 0; j < attValContent.size(); j++) {
                Term value = evaluateXMLTerm((Element)attValContent.get(j));
                molecules.add(leFactory.createAttributeValue(id, attributeName, value));
            }
        }
        return molecules;
    }

    /**
     * Put the Attribute Definitions of a Molecule (contained in the NodeList attDef)
     * into a Set. A NodeList contains all children of a specified Node.
     * @param attDef a list of child elements of a specified Node
     * @param id id of the molecule
     * @return a Set of molecules
     * @throws ParserException in case a wrong element name for a term is submitted
     * to the evaluateXMLTerm method
     */
    private Set <Molecule> evaluateXMLAttrDef(NodeList attDef, Term id)
            throws ParserException {
        Set <Molecule> molecules = new HashSet <Molecule> ();
        for (int i = 0; i < attDef.getLength(); i++) {
            Node singleAttDef = attDef.item(i);
            Term attributeName = evaluateXMLTerm((Element)childElements(((Element)singleAttDef).getElementsByTagName("name")).get(0));
            List attDefContent = childElements(((Element)singleAttDef).getElementsByTagName("type"));
            for (int j = 0; j < attDefContent.size(); j++) {
                Term type = evaluateXMLTerm((Element)attDefContent.get(j));
                if (singleAttDef.getAttributes().getNamedItem("type").getNodeValue().equals("constraining")) {
                    molecules.add(leFactory.createAttributeConstraint(
                            id, attributeName,  type));
                }else{
                    molecules.add(leFactory.createAttributeInference(
                            id, attributeName,  type));
                }
            }
        }
        return molecules;
    }

    private List <Term> evaluateXMLIsa(NodeList cptE)
            throws ParserException {
        NodeList isaTerms;
        List <Term> set = new Vector <Term> ();
        for (int i = 0; i < cptE.getLength(); i++) {
            isaTerms = ((Element)cptE.item(i)).getElementsByTagName("term");
            for (int j = 0; j < isaTerms.getLength(); j++) {
                set.add(evaluateXMLTerm((Element)isaTerms.item(j)));
            }
        }
        return set;
    }

    /**
     * Creates an Atom
     * @param exprNode exprNode SableCC Node that will be parsed, it expects a Node of the
     *                 type "PLogExpr"
     * @param identifier identifier of the Atom
     * @return Atom
     * @throws ParserException in case a wrong element name for a term is submitted
     * to the evaluateXMLTerm method
     */
    private LogicalExpression evaluateXMLConstructed(Node exprNode, IRI identifier)
            throws ParserException {
        Element e;
        Vector <Term> ll = new Vector <Term> ();
        NodeList nl = exprNode.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);

            if (n instanceof Element) {
                e = (Element)n;
                ll.add(evaluateXMLTerm(e));
            }
        }
        return leFactory.createAtom(identifier, ll);
    }

    /**
     * A NodeList contains all children of a specified Node. If there are no
     * children, this is a NodeList containing no nodes. The children
     * from the NodeList are put into a LinkedList
     *
     * @param nodes a list of child elements of a specified Node.
     * @return a list of child elements of a specified Node
     */
    private List <Node> childElements(NodeList nodes) {
        List <Node> list = new LinkedList <Node>();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i)instanceof Element) {
                list.add(nodes.item(i));
            }
        }
        return list;
    }
}