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
 * Utility class to check if a character is a letter, a digit, an extender, ...
 * 
 * <pre>
 * Created on Nov 17, 2006
 * Committed by $Author: nathaliest $
 * $Source: /cvsroot/wsmo4j/wsmo4j/com/ontotext/wsmo4j/common/CharUtil.java,v $,
 * </pre>
 * 
 * @author Nathalie Steinmetz, DERI Innsbruck
 * @version $Revision: 1.1 $ $Date: 2006/11/17 16:40:14 $
 */
public class CharUtil {

	/**
     * This method checks if a given character is a valid basechar or a valid
     * ideograph
     * 
     * @param chr
     *            character
     * @return <code>true</code> if chr is a valid letter or
     *         <code>false</code> otherwise
     */
    public boolean isLetter(char chr) {
        if (isAlpha(chr)) 
            return true;
        // ideographic 
        if ((chr >= '\u4E00' && chr <= '\u9FA5') || chr == '\u3007'
                || (chr >= '\u3021' && chr <= '\u3029'))
            return true;
        return false;
    }
    
    /**
     * This method checks if a given character is a valid basechar
     * 
     * @param chr
     *            character
     * @return <code>true</code> if chr is a valid basechar or
     *         <code>false</code> otherwise
     */
    public boolean isAlpha(char chr){
        // basechar A-Z || a-z
        return ((chr >= '\u0041' && chr <= '\u005A')
                || (chr >= '\u0061' && chr <= '\u007A'));
    }
    
    /**
     * This method checks if a given character is a valid digit
     * 
     * @param chr
     *            character
     * @return <code>true</code> if chr is a valid digit or
     *         <code>false</code> otherwise
     */
    public boolean isDigit(char chr){
        // digit 0-9
        return (chr >= '\u0030' && chr <= '\u0039');
    }
    
    /**
     * This method checks if a given character is a combining char
     * 
     * @param chr
     *            character
     * @return <code>true</code> if chr is a combining char or
     *         <code>false</code> otherwise
     */
	public boolean isCombiningChar(char chr) {
		if ((chr >= '\u0300' && chr <= '\u0345') || (chr >= '\u0360' && 
				chr <= '\u0361') || (chr >= '\u0483' && chr <= '\u0486')) { 
			return true;
		}
		return false;
	}
	
	/**
     * This method checks if a given character is an extender
     * 
     * @param chr
     *            character
     * @return <code>true</code> if chr is an extender or
     *         <code>false</code> otherwise
     */
	public boolean isExtender(char chr) {
		if (chr == '\u00B7' || chr == '\u02D0' || chr == '\u02D1' 
			|| chr == '\u0387' || chr == '\u0640' || chr == '\u0E46' 
			|| chr == '\u0EC6' || chr == '\u3005' || (chr >= '\u3031' && 
			chr <= '\u3035') || (chr >= '\u309D' && chr <= '\u309E') 
			|| (chr >= '\u30FC' && chr <= '\u30FE')) { 
			return true;
		}
		return false;
	}
}
/*
 * $Log: CharUtil.java,v $
 * Revision 1.1  2006/11/17 16:40:14  nathaliest
 * fixed wsml serializer to not serialize sqnames with unallowed characters and added util class to check characters
 *
 * 
 */
