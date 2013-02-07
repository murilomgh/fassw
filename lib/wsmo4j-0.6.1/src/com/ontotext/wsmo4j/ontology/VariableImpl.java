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

package com.ontotext.wsmo4j.ontology;

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */

import org.omwg.logicalexpression.terms.Visitor;
import org.omwg.ontology.Variable;

public class VariableImpl implements Variable {

    private String name;
    
    public VariableImpl(String name) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Variable name can not be null");
        }
        if (name.startsWith("?")) {
            this.name = name.substring(1);
        }
        else {
            this.name = name;
        }
        
		if (!isValidName(this.name)){
            throw new IllegalArgumentException("Variable name ("+name+") may only contain alphanumeric characters");
		}
        
    }
    
    public String getName() {
        return name;
    }
    
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null
                || false == object instanceof Variable) {
            return false;
        }
        return name.equals(((Variable)object).getName());
    }
    
    public int hashCode() {
        return this.name.hashCode();
    }
    
    public String toString() {
        return "?" + this.name;
    }
    
    public void accept(Visitor v) {
    	v.visitVariable(this);
    }
    
    /**
     * Checks if the given name is a valid name. This check is made
     * character by character.
     * @param name
     * @return <code>true</code> if the name is valid or <code>false</code> if not
     * @see #isValidNameChar(char)
     */
    private boolean isValidName(String name) {
        for (int i = 0; i < name.length(); i++) {
            if (!isValidNameChar(name.charAt(i))) {
                return false;
            }
        }
        return true;

    }
    
    /**
     * Checks the validity of the given character. 
     * @param chr character of a String name
     * @return <code>true</code> if the given character is valid, <code>false</code> otherwise
     * @see #isValidName(String)
     */
    private boolean isValidNameChar(char chr) {
        return // basechar
                (chr >= '\u0041' && chr <= '\u005A') || 
                (chr >= '\u0061' && chr <= '\u007A') ||
               // ideographic
                (chr >= '\u4E00' && chr <= '\u9FA5') || 
                (chr == '\u3007') || 
                (chr >= '\u3021' && chr <= '\u3029') ||
                //digit	
                (chr >= '\u0030' && chr <= '\u0039');
    }
}

/*
 * $Log: VariableImpl.java,v $
 * Revision 1.7  2006/11/21 15:45:20  vassil_momtchev
 * isValid check was not applied over the correct variable
 *
 * Revision 1.6  2005/09/02 15:19:16  alex_simov
 * bugfix: inverted if condition in constructor
 *
 * Revision 1.5  2005/09/02 14:20:56  holgerlausen
 * throws illegal argument exception now
 *
 * Revision 1.4  2005/09/02 13:32:45  ohamano
 * move logicalexpression packages from ext to core
 * move tests from logicalexpression.test to test module
 *
 * Revision 1.3  2005/09/02 09:43:32  ohamano
 * integrate wsmo-api and le-api on api and reference implementation level; full parser, serializer and validator integration will be next step
 *
 * Revision 1.2  2005/08/31 08:36:14  alex_simov
 * bugfix: Variable.getName() now returns the name without leading '?'
 *
 * Revision 1.1  2005/06/01 12:09:05  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.1  2005/05/12 15:07:25  alex
 * initial commit
 *
 *
 */
