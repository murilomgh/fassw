/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2004-2006, Ontotext Lab. / SIRMA

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
 * <p>Copyright:  Copyright (c) 2004-2006</p>
 * <p>Company: Ontotext Lab. / SIRMA </p>
 */

package com.ontotext.wsmo4j.parser.wsml;

import java.util.*;

import org.deri.wsmo4j.io.parser.*;
import org.deri.wsmo4j.logicalexpression.*;
import org.omwg.logicalexpression.*;
import org.wsmo.common.*;
import org.wsmo.factory.*;
import org.wsmo.wsml.*;
import org.wsmo.wsml.compiler.node.*;

public class IdentifierAnalysis extends ASTAnalysis {

    private WsmoFactory factory;
    private ASTAnalysisContainer container;
    private HashMap nsPrefixCache = new HashMap();
    private Stack namespaces; // handle to namespace stack
    private Stack identifiers; // handle to identifier stack
    private Stack identifierLists; // handle to identifierList stack

    public IdentifierAnalysis(ASTAnalysisContainer container, WsmoFactory factory) {
        if (factory == null || container == null) {
            throw new IllegalArgumentException();
        }
        this.factory = factory;
        this.container = container;

        // register the handled nodes for namespaces
        container.registerNodeHandler(ADefaultnsPrefixdefinitionlist.class, this);
        container.registerNodeHandler(ADefaultPrefixdefinition.class, this);
        container.registerNodeHandler(ANamespacedefPrefixdefinition.class, this);

        // register the handled nodes for ids
        container.registerNodeHandler(AAnonymousId.class, this);
        container.registerNodeHandler(AIriId.class, this);
        container.registerNodeHandler(AIriIri.class, this);
        container.registerNodeHandler(AAnySqname.class, this);
        container.registerNodeHandler(AIdIdlist.class, this);
        container.registerNodeHandler(AIdlistIdlist.class, this);
        container.registerNodeHandler(AUniversalTruthId.class, this);
        container.registerNodeHandler(AUniversalFalsehoodId.class, this);
        container.registerNodeHandler(PId.class, this);

        namespaces = container.getStack(Namespace.class);
        identifiers = container.getStack(Identifier.class);
        identifierLists = container.getStack(Identifier[].class);
    }

    public void setNameSpaces(HashMap ns){
        this.nsPrefixCache = ns;
    }

    // Hanlde namespaces

    public void inADefaultnsPrefixdefinitionlist(ADefaultnsPrefixdefinitionlist node1) {
        String namespaceIRI = _strip(node1.getFullIri().getText().trim());
        String namespacePrefix = "_"; // this is always the default namespace prefix
        Namespace namespace = factory.createNamespace(namespacePrefix,
                factory.createIRI(namespaceIRI));
        namespaces.push(namespace);
        nsPrefixCache.put(namespacePrefix, namespaceIRI);
    }

    public void inADefaultPrefixdefinition(ADefaultPrefixdefinition node1) {
        String namespaceIRI = _strip(node1.getFullIri().getText().trim());
        String namespacePrefix = "_"; // this is always the default namespace prefix
        Namespace namespace = factory.createNamespace(namespacePrefix,
                factory.createIRI(namespaceIRI));
        Stack namespaces = container.getStack(Namespace.class);
        namespaces.push(namespace);
        nsPrefixCache.put(namespacePrefix, namespaceIRI);
    }

    public void inANamespacedefPrefixdefinition(ANamespacedefPrefixdefinition node1) {
        String namespaceIRI = _strip(node1.getFullIri().getText().trim());
        String namespacePrefix = node1.getName().getText().trim();
        Namespace namespace = factory.createNamespace(unEscape(namespacePrefix),
                factory.createIRI(namespaceIRI));
        Stack namespaces = container.getStack(Namespace.class);
        namespaces.push(namespace);
        nsPrefixCache.put(namespacePrefix, namespaceIRI);
    }

    // Handle identifier

    public void caseAIriId(AIriId node) {
        if (node.getIri() != null)
            node.getIri().apply(this);
    }

    public void caseASqnameIri(ASqnameIri node) {
        if (node.getSqname() != null)
            node.getSqname().apply(this);
    }

    public void caseAAnonymousId(AAnonymousId node) {
        identifiers.push(factory.createAnonymousID());
        node.replaceBy(null);
    }

    public void caseAIriIri(AIriIri node) {
        try{
            identifiers.push(factory.createIRI(_strip(node.getFullIri().getText())));
        }catch (IllegalArgumentException e){
            ParserException pe = new ParserException("relative IRIs are not allowed", null);
            Token t = node.getFullIri();
            pe.setErrorLine(t.getLine());
            pe.setErrorPos(t.getPos());
            pe.setFoundToken(t.getText());
            throw new WrappedParsingException(pe);
        }
        node.replaceBy(null);
    }

    public void caseALocalkeywordSqname(ALocalkeywordSqname node) {
        //TODO: Check why there special Sqname {localkeyword} case in wsml grammar??
        TName tn = new TName(node.getAnykeyword().toString());
        String prefix = null;
        if ((APrefix)node.getPrefix()!=null){
            tn = ((APrefix)node.getPrefix()).getName();
            prefix = tn.getText().trim();
        }
        sQNameHandler(new TName(node.getAnykeyword().toString()).getText().trim(),
                prefix, tn);
        node.replaceBy(null);
    }

    // copy pasted from IdentifierProcessing.java QName inner class

    public void caseAAnySqname(AAnySqname node) {
        TName tn = node.getName();
        String prefix = null;
        if ((APrefix)node.getPrefix()!=null){
            tn = ((APrefix)node.getPrefix()).getName();
            prefix = tn.getText().trim();
        }
        sQNameHandler(node.getName().getText().trim(),prefix,tn);
        node.replaceBy(null);
    }

    private void sQNameHandler(String name, String prefix, TName tn) {
        //built-ins
        String iri = ConstantTransformer.getInstance().findIri(name);
        if (iri != null) {
            identifiers.push(factory.createIRI(iri));
            return;
        }

        //check on non existing or non sufficient ns container
        String error = null;
        if (prefix == null && nsPrefixCache.get("_") == null) {
            error= ParserException.NO_NAMESPACE;
        }
        else if (prefix != null && nsPrefixCache.get(prefix) == null) {
            error = ParserException.NAMESPACE_PREFIX_NOT_FOUND;
        }
        if (error != null) {
            ParserException e = new ParserException(error, null);
            e.setErrorLine(tn.getLine());
            e.setErrorPos(tn.getPos());
            if (prefix != null){
                e.setFoundToken(prefix+"#"+name);
            }else{
                e.setFoundToken(name);
            }
            throw new WrappedParsingException(e);
        }

        //create IRI
        String namespace = null;
        if (prefix !=null){
            namespace = (String) nsPrefixCache.get(prefix);
        }
        else {
            namespace = (String) nsPrefixCache.get("_");
        }

        identifiers.push(factory.createIRI(namespace + unEscape(name)));
    }

    public static String unEscape(String localname) {
        //remove \ from input.
        if (localname.indexOf('\\') == -1) {
            return localname;
        }
        StringBuffer result = new StringBuffer(localname.length());
        for (int i = 0; i < localname.length(); i++) {
            if (localname.charAt(i) != '\\') {
                result.append(localname.charAt(i));
            }
        }
        return result.toString();
    }

    // copy pasted from QNameProcessing.java

    private String _strip(String str) {
        if (null == str || str.length() < 3) return str;
        return str.substring(2, str.length() - 1);
    }

    // handle Identifier list

    Object lastItem;

    public void inAIdIdlist(AIdIdlist node) {
        if (identifiers.isEmpty()) {
            lastItem = null;
        }
        else {
            lastItem = identifiers.peek();
        }
    }

    public void outAIdIdlist(AIdIdlist node) {
        Vector ids = new Vector();
        while (!identifiers.isEmpty() &&
                identifiers.peek() != lastItem) {
            ids.add(0, identifiers.pop());
        }
        if (ids.isEmpty()) {
            throw new RuntimeException("Id list contained no ids!");
        }
        identifierLists.push(ids.toArray(new Identifier[]{}));
    }

    public void inAIdlistIdlist(AIdlistIdlist node) {
        inAIdIdlist(null); // same implementaion
    }

    public void outAIdlistIdlist(AIdlistIdlist node) {
        outAIdIdlist(null); // same implementation
    }

    public void caseAUniversalFalsehoodId(AUniversalFalsehoodId node) {
        identifiers.push(factory.createIRI(Constants.UNIV_FALSE));
        node.replaceBy(null);
    }

    public void caseAUniversalTruthId(AUniversalTruthId node) {
        identifiers.push(factory.createIRI(Constants.UNIV_TRUE));
        node.replaceBy(null);
    }
}

/*
 * $Log: IdentifierAnalysis.java,v $
 * Revision 1.3  2006/05/04 14:17:09  alex_simov
 * bugfix: invalid sQName parsing/serializing - illegal symbols not escaped
 *
 * Revision 1.2  2005/11/30 17:17:41  holgerlausen
 * throwing parser exception with more information in case of relative IRIs
 *
 * Revision 1.1  2005/11/28 13:55:26  vassil_momtchev
 * AST analyses
 *
*/
