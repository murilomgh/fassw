/*
 wsmo4j - a WSMO API and Reference Implementation
 
 Copyright (c) 2004-2005, OntoText Lab. / SIRMA
                          University of Innsbruck, Austria
 
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
package com.ontotext.wsmo4j.common;

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */

import java.io.Serializable;

import org.omwg.logicalexpression.terms.Visitor;
import org.wsmo.common.IRI;

public class IRIImpl implements IRI, Serializable {

	private CharUtil util = new CharUtil();
	
    private static final long serialVersionUID = 1L;
    
    /**
     * actual string uri.
     */
    private String iri;
    
    /**
     * indicating position in IRI string of namespace / localname split
     */
    private int seperator = -1;

    /**
     * Creates IRI object, does heuristics for namespace/localname split
     * @param iri String representation of IRI
     * @throws IllegalArgumentException in case no legal IRI is supplied
     */
    public IRIImpl(String iri) throws IllegalArgumentException {
        if (iri == null || iri.length() == 0) {
            throw new IllegalArgumentException("IRI can not be emtpy");
        }
        checkAbsoluteIRI(iri);
        seperator = getStartLocalName(iri);
        this.iri = iri;
    }
    
    /**
     * Creates IRI with the knowledge of sepration between namespace and localname.
     * 
     * @param ns
     *            String containing the part of the IRI that is the Namespace
     * @param ln
     *            String containing the part of the IRI that is the localname
     * @throws IllegalArgumentExpression
     *             <p>
     *             in case the IRI is not legal according to the IRI spec.
     *             </p>
     */
    public IRIImpl(String ns, String ln) throws IllegalArgumentException {
        if (ns == null || ln == null || ns.length() == 0 || ln.length() == 0) {
            throw new IllegalArgumentException(
                    "both local name and namespace may not be empty");
        }
        if (!isLegalLocalName(ln)) {
            throw new IllegalArgumentException("local name (" + ln
                    + ") is not valid");
        }
        checkAbsoluteIRI(ns+ln);
        seperator = ns.length();
    }

    /**
     * retrieve the &lt;localname&gt; part of the iri.
     * 
     * @return a localname as string
     */
    public String getLocalName() {
        
        int pos = iri.lastIndexOf('#');
        
        if (pos < 0) {
            pos = iri.lastIndexOf('/');
        }
        
        if (pos < 0) {
            return iri;
        }
        return iri.substring(pos + 1);
    }
    

    /**
     * retrieve the namespace part of iri
     * @return the namespace or null in case the namespace is not known.
     */
    public String getNamespace() {
        if (seperator == -1)
            return null;
        return iri.substring(0, seperator);
    }
    
    public String toString() {
        return iri;
    }
    
    /**
     * @see org.omwg.logicalexpression.terms.Term#accept(org.omwg.logicalexpression.terms.Visitor)
     */
	public void accept(Visitor v) {
        v.visitIRI(this);
    }
    
    /**
     * equality check.
     * 
     * @param object
     *            to compare with
     * @return true if object is lexical an identical IRI
     * @see Object#equals(java.lang.Object)
     */
    public boolean equals(Object object) {
        if (object == this) {
            return true; // instance match
        }
        if (object == null
                || false == object instanceof IRI) {
            return false;
        }
        return 0 == iri.compareTo(object.toString());
    }
    
    public int hashCode() {
        return iri.hashCode();
    }
    
    private void checkAbsoluteIRI(String iri) throws IllegalArgumentException{
        //scheme = ALPHA *( ALPHA / DIGIT / "+" / "-" / "." )
        int schemeEnd = iri.indexOf(':');
        if (schemeEnd<1){
            throw new IllegalArgumentException("Only absolute IRIs allowed: "+iri);
        }
        if (!util.isAlpha(iri.charAt(0))){
            throw new IllegalArgumentException("IRI scheme must start with alpha nummeric character: "+iri);
        }
        for (int i=1; i<schemeEnd;i++){
            if (!isScheme(iri.charAt(i))){
                throw new IllegalArgumentException("Illegal Character in IRI scheme: "+ iri.charAt(i) +"("+iri+")");
            }
        }
        //do not check scheme specific stuff (can't know all custom schemes).

    }
    
    /**
     * Get the index that points to the position in the IRI where the localname
     * starts. If this index is -1, no valid localname or no namespace is found.
     * 
     * @param iri
     *            String representing the full IRI
     * @return index that points to the position in the IRI where the localname
     *         starts or -1, if namespace or localname are not found
     * @see #isLegalLocalName(String)
     */
    private int getStartLocalName(String iri) {
        int i = iri.length() - 1;
        while (i > 0 && isLegalLocalNameChar(iri.charAt(i))) {
            i--;
        }
        // no namespace found so -1
        if (i == 0) {
            i = -1;
        }
        // only '_' and Letter are allowed as first char of localname
        else {
            while (i < iri.length()
                    && !(iri.charAt(i) == '_' || util.isLetter(iri.charAt(i)))) {
                i++;
            }
        }
        // no local name found so -1
        if (i == iri.length()) {
            i = -1;
        }
        return i;
    }

    private boolean isScheme(char chr){
        return util.isAlpha(chr) || util.isDigit(chr) ||
                chr == '-' || chr == '+' || chr == '.';
    }

    /**
     * This method checks if a given character is a legal character for a
     * localname. It must be either a valid letter, a digit or '_'.
     * 
     * @param chr
     *            character
     * @return <code>true</code> if chr is a letter, a numeral or '_', or
     *         <code>false</code> otherwise
     * @see #isLetter(char)
     */
    private boolean isLegalLocalNameChar(char chr) {
        // letter || digit
        if (util.isLetter(chr) || util.isDigit(chr))
            return true;
        if (chr == '_')
            return true;
        if (util.isCombiningChar(chr) || util.isExtender(chr))
        	return true;
        return false;
    }
    
    /**
     * This method checks if a given string is a legal localname. This is the
     * case if every character of the string is either a valid letter, a digit
     * or '_'. Also, only '_' and Letter are allowed as first char of a
     * localname.
     * 
     * @param ln
     *            String representing the localname part of an IRI
     * @return <code>true</code> if ln is a legal localname;
     *         <code>false</code> otherwise
     * @see #isLegalLocalNameChar(char)
     */
    private boolean isLegalLocalName(String ln) {
        boolean validLocalName = true;
        for (int i = 0; i < ln.length(); i++) {
            if (!isLegalLocalNameChar(ln.charAt(i))) {
                validLocalName = false;
            }
        }
        return (validLocalName && (ln.charAt(0) == '_' || util.isLetter(ln.charAt(0))));
    }
    
}

/*
* $Log: IRIImpl.java,v $
* Revision 1.10  2006/11/17 16:40:14  nathaliest
* fixed wsml serializer to not serialize sqnames with unallowed characters and added util class to check characters
*
* Revision 1.9  2005/11/04 06:14:26  holgerlausen
* fixed unit tests relying on relative iris
*
* Revision 1.8  2005/11/03 18:15:34  holgerlausen
* included check in IRIImpl for absolute IRI
*
* Revision 1.7  2005/10/17 15:10:00  marin_dimitrov
* getNameSpace --> getNamespace
*
* Revision 1.6  2005/09/16 14:02:44  alex_simov
* Identifier.asString() removed, use Object.toString() instead
* (Implementations MUST override toString())
*
* Revision 1.5  2005/09/06 18:25:33  holgerlausen
* implemented namespace heurisitcs for IRI and additional type checks
*
* Revision 1.4  2005/09/02 13:32:44  ohamano
* move logicalexpression packages from ext to core
* move tests from logicalexpression.test to test module
*
* Revision 1.3  2005/09/02 09:43:32  ohamano
* integrate wsmo-api and le-api on api and reference implementation level; full parser, serializer and validator integration will be next step
*
* Revision 1.2  2005/06/22 14:20:15  alex_simov
* Copyright header added/updated
*
* Revision 1.1  2005/06/01 12:00:32  marin_dimitrov
* v0.4.0
*
* Revision 1.2  2005/05/30 15:06:37  alex
* toString() delegates to asString()
*
* Revision 1.1  2005/05/11 12:24:05  alex
* initial commit
*
*/
