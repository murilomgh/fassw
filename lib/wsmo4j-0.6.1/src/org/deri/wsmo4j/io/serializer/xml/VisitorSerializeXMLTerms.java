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
package org.deri.wsmo4j.io.serializer.xml;


import java.util.*;

import org.w3c.dom.*;

import org.omwg.logicalexpression.*;
import org.omwg.logicalexpression.terms.*;
import org.omwg.logicalexpression.terms.Visitor;
import org.omwg.ontology.*;
import org.wsmo.common.*;


/**
 * Concrete Visitor class. For each visited term, a document element is built.
 * @see org.deri.wsmo4j.io.serializer.xml.VisitorSerializeXML
 * @see org.omwg.logicalexpression.terms.Visitor
 */
public class VisitorSerializeXMLTerms
        implements Visitor {

    private Vector stack;

    private Document doc;

    private String name;

    /**
     * @param doc Document that will be filled with the xml structure
     * @see org.deri.wsmo4j.io.serializer.xml.VisitorSerializeXML#VisitorSerializeXML(TopEntity, Document)
     */
    public VisitorSerializeXMLTerms(Document doc) {
        this.doc = doc;
        stack = new Vector();
        name = "term";
    }

    /**
     * Set the type of element that is to be added to the document
     * (term, atom or molecule).
     * @param name type of element
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Builds an element in the document with the ConstructedTerm's name
     * as attribute. The arguments are added as children to the node.
     * The element is then added to a vector.
     * @param t ConstructedTerm to be serialized
             * @see org.omwg.logicalexpression.terms.Visitor#visitConstructedTerm(org.omwg.logicalexpression.terms.ConstructedTerm)
     */
    public void visitConstructedTerm(ConstructedTerm t) {
        Element term = doc.createElement(name);
        term.setAttribute("name", t.getFunctionSymbol().toString());

        int nbParams = t.getArity();
        for (int i = 0; i < nbParams; i++) {
            name = "arg";
            t.getParameter(i).accept(this);
            term.appendChild((Node)stack.remove(stack.size() - 1));
        }
        stack.add(term);
    }

    /**
     * Builds an element in the document with the Variable's String
     * representation as attribute. The element is added to a vector.
     * @param t Variable to be serialized
     * @see org.omwg.logicalexpression.terms.Visitor#visitVariable(org.omwg.logicalexpression.terms.Variable)
     */
    public void visitVariable(Variable t) {
        Element term = doc.createElement(name);
        term.setAttribute("name", Constants.VARIABLE_DEL + t.getName());
        stack.add(term);
    }

    public void visitComplexDataValue(ComplexDataValue t) {
        Element term = doc.createElement(name);
        term.setAttribute("name", t.getType().getIRI().toString());

        int nbParams = t.getArity();
        for (byte i = 0; i < nbParams; i++) {
            name = "arg";
            ((Term)t.getArgumentValue(i)).accept(this);
            term.appendChild((Node)stack.remove(stack.size() - 1));
        }
        stack.add(term);
    }

    public void visitSimpleDataValue(SimpleDataValue t) {
        Element value = doc.createElement(name);
        value.setAttribute("name", t.getType().getIRI().toString());
        Text numVal = doc.createTextNode("" + t.getValue());
        value.appendChild(numVal);
        stack.add(value);
    }

    /**
     * Builds an element in the document with the Unnumbered
     * Anonymous ID's String representation as attribute.
     * The element is added to a vector.
     * @param t UnNBAnonymousID to be serialized
     * @see org.omwg.logicalexpression.terms.Visitor#visitAnonymousID(org.omwg.logicalexpression.terms.UnNbAnonymousID)
     */
    public void visitUnnumberedID(UnnumberedAnonymousID t) {
        Element term = doc.createElement(name);
        term.setAttribute("name", Constants.ANONYMOUS_ID);
        stack.add(term);
    }

    /**
     * Builds an element in the document with the Numbered
     * Anonymous ID's String representation as attribute.
     * The element is added to a vector.
     * @param t NbAnonymousID to be serialized
     * @see org.omwg.logicalexpression.terms.Visitor#visitNbAnonymousID(org.omwg.logicalexpression.terms.NbAnonymousID)
     */
    public void visitNumberedID(NumberedAnonymousID t) {
        Element term = doc.createElement(name);
        term.setAttribute("name", Constants.ANONYMOUS_ID + t.getNumber());
        stack.add(term);
    }

    /**
     * Builds an element in the document with the IRI's String
     * representation as attribute. The element is added to a vector.
     * @param t IRI to be serialized
     * @see org.omwg.logicalexpression.terms.Visitor#visitIRI(org.omwg.logicalexpression.terms.IRI)
     */
    public void visitIRI(IRI t) {
        Element term = doc.createElement(name);
        term.setAttribute("name", t.toString());
        stack.add(term);
    }

    /**
     * All serialized elements are added to a vector. This method removes the
     * first serialized element from this vector and shifts any subsequent
     * elements to the left (subtracts one from their indices).
     * @return the serialized document node that is the first element in this vector
     */
    public Object getSerializedObject() {
        return stack.remove(0);
    }
}
