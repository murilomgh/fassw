/*
 wsmo4j - a WSMO API and Reference Implementation
 Copyright (c) 2005, University of Innsbruck, Austria
                     Ontotext Lab. / SIRMA Group
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
package org.deri.wsmo4j.io.serializer.wsml;


import java.util.*;

import org.deri.wsmo4j.io.parser.wsml.*;
import org.deri.wsmo4j.logicalexpression.*;
import org.omwg.logicalexpression.*;
import org.omwg.logicalexpression.terms.*;
import org.omwg.logicalexpression.terms.Visitor;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.factory.*;
import org.wsmo.wsml.*;

import com.ontotext.wsmo4j.common.CharUtil;


/**
 * Concrete Visitor class. For each visited term, a String is built.
 * @see org.omwg.logicalexpression.terms.Visitor
 */
public class VisitorSerializeWSMLTerms
        implements Visitor {
    
    private Map atoms2ConstructedTerms;
    
    private Vector stack;

    private Namespace[] nsHolder;
    
    private CharUtil util;

    /**
     * @param nsC TopEntity
     * @see org.deri.wsmo4j.io.serializer.wsml.VisitorSerializeWSML#VisitorSerializeWSML(TopEntity)
     */
    public VisitorSerializeWSMLTerms(TopEntity nsC) {
        
        if (nsC != null) {
            Set <Namespace> nsBuffer = new HashSet <Namespace> (nsC.listNamespaces());
            if (nsC.getDefaultNamespace() != null) {
                nsBuffer.add(nsC.getDefaultNamespace());
            }
            if (nsBuffer.size() > 0) {
                nsHolder = nsBuffer.toArray(new Namespace[nsBuffer.size()]);
                Arrays.sort(nsHolder, new Comparator <Object>() {
                    public int compare(Object o1, Object o2) {
                        String ns1IRI = ((Namespace)o1).getIRI().toString();
                        String ns2IRI = ((Namespace)o2).getIRI().toString();
                        return ns1IRI.compareTo(ns2IRI);
                    }
                });
            }
        }
        stack = new Vector();
        util = new CharUtil();
    }
    
    public void setAtoms2ConstructedTerms(Map atoms2ConstructedTerms){
        this.atoms2ConstructedTerms = atoms2ConstructedTerms;
    }

    /**
     * Builds a String representing the ConstructedTerm and adds it to a vector.
     * @param t ConstructedTerm to be serialized
             * @see org.omwg.logicalexpression.terms.Visitor#visitConstructedTerm(org.omwg.logicalexpression.terms.ConstructedTerm)
     */
    public void visitConstructedTerm(ConstructedTerm t) {
        String iri = t.getFunctionSymbol().toString();
        String s = "";
        t.getFunctionSymbol().accept(this);
        if (ConstantTransformer.getInstance().isInfix(iri)) {
            String operator = (String)stack.remove(stack.size() - 1);
            t.getParameter(0).accept(this);
            t.getParameter(1).accept(this);
            s = "("+ (String)stack.remove(stack.size() - 2)
                + operator + (String)stack.remove(stack.size() - 1) + ")";
        }
        else {
            s = s + (String)stack.remove(stack.size() - 1);
            int nbParams = t.getArity();
            if (nbParams > 0) {
                s = s + "(";
                for (int i = 0; i < nbParams; i++) {
                    t.getParameter(i).accept(this);
                    s = s + (String)stack.remove(stack.size() - 1);
                    if (i + 1 < nbParams) {
                        s = s + ",";
                    }
                }
                s = s + ")";
            }
        }
        stack.add(s);
    }

    /**
     * Builds a String representing the Variable and adds it to a vector.
     * @param t Variable to be serialized
     * @see org.omwg.logicalexpression.terms.Visitor#visitVariable(org.omwg.logicalexpression.terms.Variable)
     */
    public void visitVariable(Variable t) {
        if (t instanceof TempVariable){
            Term term = (Term)atoms2ConstructedTerms.get(t);
            if (term!=null) term.accept(this);
            else stack.add(t.toString()+"<NonResolvableDependencyToBuiltInAtom>");
            return;
        }
        stack.add(Constants.VARIABLE_DEL + t.getName());
    }

    public void visitComplexDataValue(ComplexDataValue value) {
        String s = "";
        value.getType().getIRI().accept(this);
        s = (String)stack.remove(stack.size() - 1);
        int nbParams = value.getArity();
        s = s + "(";
        for (byte i = 0; i < nbParams; i++) {
            ((Term)value.getArgumentValue(i)).accept(this);
            s = s + (String)stack.remove(stack.size() - 1);
            if (i + 1 < nbParams) {
                s = s + ",";
            }
        }
        stack.add(s + ")");
    }

    public void visitSimpleDataValue(SimpleDataValue value) {
        if (value.getType().getIRI().toString().equals(WsmlDataType.WSML_STRING)) {
            //escape \ and "
            String content = (String)value.getValue();
            content = content.replaceAll("\\\\", "\\\\\\\\");
            content = content.replaceAll("\"", "\\\\\"");
            stack.add(Constants.STRING_DEL_START + content + Constants.STRING_DEL_END);
        }
        else { // WSML_DECIMAL || WSML_INTEGER
            stack.add("" + value.getValue());
        }
        String iri = value.getType().getIRI().toString();
    }

    /**
     * Builds a String representing the Unnumbered Anonymous ID
     * and adds it to a vector.
     * @param t UnNBAnonymousID to be serialized
     * @see org.omwg.logicalexpression.terms.Visitor#visitAnonymousID(org.omwg.logicalexpression.terms.UnNbAnonymousID)
     */
    public void visitUnnumberedID(UnnumberedAnonymousID t) {
        stack.add(Constants.ANONYMOUS_ID_NOTATION);
    }

    /**
     * Builds a String representing the Numbered Anonymous ID
     * and adds it to a vector.
     * @param t NbAnonymousID to be serialized
     * @see org.omwg.logicalexpression.terms.Visitor#visitNbAnonymousID(org.omwg.logicalexpression.terms.NbAnonymousID)
     */
    public void visitNumberedID(NumberedAnonymousID t) {
        stack.add(Constants.ANONYMOUS_ID_NOTATION + t.getNumber());
    }

    /**
     * Builds a String representing the IRI and adds it to a vector.
     * @param t IRI to be serialized
     * @see org.omwg.logicalexpression.terms.Visitor#visitIRI(org.omwg.logicalexpression.terms.IRI)
     */
    public void visitIRI(IRI t) {
        String iri = t.toString();
        String notion = ConstantTransformer.getInstance().findNotation(iri);
        if (notion != null) {
            stack.add(notion);
            return;
        }
        stack.add(resolveIRI(iri));
    }
    
    String resolveIRI(String iri) {

        Parser p = Factory.createParser(null);
        if (nsHolder == null) { // no or empty namespace holder spacified
            return Constants.IRI_DEL_START + iri + Constants.IRI_DEL_END;
        }
        
        for(int i = nsHolder.length-1; i >= 0; i--) {
            Namespace ns = nsHolder[i];
            if (iri.startsWith(ns.getIRI().toString())) {
                
                // the namespace consumes the whole iri ?
                if (iri.length() == ns.getIRI().toString().length()) {
                    continue;
                }
                // the new sQName would contain invalid characters
                if (checkChars(iri.substring(ns.getIRI().toString().length()))) {
                    continue;
                }
                boolean isDefaultNSMatch = ns.getPrefix() == null 
                                           || ns.getPrefix().equals("")
                                           || ns.getPrefix().equals("_");
                // the raw sqname which might contain characters to be escaped
                String sqname = (isDefaultNSMatch) ? 
                        iri.substring(ns.getIRI().toString().length()) // --> default namespace
                        : (ns.getPrefix() + Constants.SQNAME_DEL // --> other namespace
                            + iri.substring(ns.getIRI().toString().length()));
                
                sqname = escapeNonSQNameSymbols(sqname);
                
                if (isDefaultNSMatch // avoid using reserved terms as identifiers
                        && p.listKeywords().contains(sqname)) {
                    continue;
                }
                return sqname;
            }
        }
        return Constants.IRI_DEL_START + iri + Constants.IRI_DEL_END;
    }
    
    public static String escapeNonSQNameSymbols(String sqname) {
        StringBuffer buf = new StringBuffer(sqname.length());
        // escape all '.' and '-' symbols by '\'
        for (int i = 0; i < sqname.length(); i++) {
            if (sqname.charAt(i) == '-' || sqname.charAt(i) == '.') {
                buf.append('\\');
            }
            buf.append(sqname.charAt(i));
        }
        return buf.toString();
    }

    /**
     * All serialized elements are added to a vector. This method removes the
     * first serialized object from this vector and shifts any subsequent
     * objects to the left (subtracts one from their indices).
     * @return the serialized String object that is the first element in this vector
     */
    public String getSerializedObject() {
        return stack.remove(0).toString();
    }
    
    /**
     * Checks if the given string can NOT be encoded as a sQName
     * @param iri
     * @return true if the string contains symbols which can not be encoded in sQName
     */
    private boolean checkChars(String iri) { 
    	for (int i=0; i<iri.length();i++) {
    		char chr = iri.charAt(i);
    		if (!util.isAlpha(chr) && !util.isDigit(chr) && !(chr == '_')) {
    			if (!util.isCombiningChar(chr) && !util.isExtender(chr))
    				return true;
    		}
    	}
    	return false;
    }
}

/*
 * $Log: VisitorSerializeWSMLTerms.java,v $
 * Revision 1.22  2007/04/02 12:13:23  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.21  2007/01/08 16:04:58  alex_simov
 * no message
 *
 * Revision 1.20  2007/01/03 09:25:59  alex_simov
 * IRI serializing bugfix: longer namespace IRIs are preferred when trying to
 * transform full IRIs into sQNames
 *
 * Revision 1.19  2006/12/05 13:45:44  alex_simov
 * bugfix: IRI serialization produced invalid SQNames
 *
 * Revision 1.18  2006/11/17 16:40:14  nathaliest
 * fixed wsml serializer to not serialize sqnames with unallowed characters and added util class to check characters
 *
 * Revision 1.17  2006/10/31 15:50:26  vassil_momtchev
 * when IRI is equal to namespace iri, do not use sqnames, but full IRI
 *
 * Revision 1.16  2006/05/04 14:17:09  alex_simov
 * bugfix: invalid sQName parsing/serializing - illegal symbols not escaped
 *
 */
