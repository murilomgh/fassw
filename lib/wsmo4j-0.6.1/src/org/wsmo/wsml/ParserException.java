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

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 */

package org.wsmo.wsml;


/**
 * This is a <b>checked</b> exception thrown when the stream being parsed
 * contains syntax errors or is invalid w.r.t. the predefined grammar
 *
 * @author not attributable
 * @version $Revision: 1.7 $ $Date: 2005/10/17 14:57:00 $
 */
public class ParserException
        extends Exception {
    
    //remember to change when updating class!
    private static final long serialVersionUID = 4051040887066865975L;
    
    public static final String NO_NAMESPACE = "Parsing sQName, but could not find namespace defintion. ";
    public static final String NAMESPACE_PREFIX_NOT_FOUND = "Parsing sQName, but could not find namespace defintion for prefix. ";
    public static final String NOT_VALID_PARSETREE = "Could not parse WSML: ";
    public static final String ANONYMOUS_ID_AS_ATOM = "Anonymous Id not allowed as identifier for Atom. ";
    public static final String NB_ANONYMOUS_ID_AS_ATOM = "Numbered Anonymous Id not allowed as identifier for Atom. ";
    public static final String VALUE_AS_ATOM = "Value not allowed as identifier of an atom. ";
    public static final String FUNCTION_SYMBOL_AS_ATOM = "Function Symbol not allowed as identifier of an atom. ";
    public static final String VARIABLE_AS_ATOM = "Variable not allowed as identifier of an atom. ";
    public static final String WRONG_ARG_SIMPLE_DT = "Argument of a simple data value must be singular and of type wsml#string. ";

    private int line = -1;
    private int pos = -1;
    private String expectedToken = null;
    private String foundToken = null;

    /**
     * Creates a Parser Exception indicating a problem found when 
     * processing an InputStream. 
     *  
     * @param msg message indicating the error that occured. Note
     * that this class appends some exta information (if given) like the
     * line and position of the error when "getMessage()" is called.
     * 
     * @param cause the initial cause of the error. In case the 
     * problem is caused by the underlying parser (e.g. sableCC it
     * will contain this Exception
     */
    public ParserException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Indicates the line where the parsing error occured.
     *
     * @return line with parsing error or -1 if not known.
     */
    public int getErrorLine() {
        return line;
    }

    /**
     * Sets the line where the error occured.
     * 
     * @param line where the error occured.
     */
    public void setErrorLine(int line) {
        this.line=line;
    }

    /**
     * inicates the position where the parsing error occured.
     *
     * @return position where parsing error occured or -1 if not known.
     */
    public int getErrorPos() {
        return pos;
    }
    
    /**
     * sets the position (column) where the error occured.
     * 
     * @param pos of error.
     */
    public void setErrorPos(int pos) {
        this.pos=pos;
    }
    
    /**
     * returns the token that was expected.
     * 
     * @return the token that was expected or null if not known.
     */
    public String getFoundToken(){
        return this.foundToken;
    }

    /**
     * set the token that was found during parsing.
     * 
     * @param token the token that was found during parsing.
     */
    public void setFoundToken(String token){
        this.foundToken=token;
    }

    /**
     * Indicates which token was expecting when parsing error occured
     *
     * @return list of token (comma sperated) that are expecting at the position
     *         with parsing error, null if not known.
     */
    public String getExpectedToken() {
        return expectedToken;
    }

    /**
     * sets the token that was expected when parsing error occured.
     * 
     * @param token the toke that was expected when parsing error occured.
     */
    public void setExpectedToken(String token){
        this.expectedToken=token;
    }
    
    /**
     * Returns human readable description of error. 
     * @return message indicating the error that occured. Note
     * that this class appends some exta information (if given) like the
     * line and position of the error when "getMessage()" is called.
     * @see java.lang.Throwable#getMessage()
     */
    public String getMessage() {
        String details ="";
        if (line!=-1) details = "(Line: "+line+" Pos: "+pos;
        if (expectedToken!=null) details += " Expected:"+expectedToken;
        if (foundToken!=null) details += " Found:"+foundToken;
        if (line!=-1) details += ")";
        return super.getMessage()+details;
    }
}
/*
 * $Log: ParserException.java,v $
 * Revision 1.7  2005/10/17 14:57:00  ohamano
 * new exception text for simple data values without singular argument of type wsml#string
 *
 * Revision 1.6  2005/09/21 08:15:39  holgerlausen
 * fixing java doc, removing asString()
 *
 * Revision 1.5  2005/08/04 05:48:26  holgerlausen
 * added found token
 *
 * Revision 1.4  2005/08/01 13:01:27  holgerlausen
 * more fine grained Error Handling
 *
 * Revision 1.3  2005/07/04 11:44:40  marin_dimitrov
 * formatting?
 *
 * Revision 1.2  2005/06/30 09:43:28  alex_simov
 * refactoring: org.wsmo.parser -> org.wsmo.wsml.*
 *
 * Revision 1.2  2005/06/28 09:00:42  holgerlausen
 * extended ParserException and added enhanced version to WSMLAnalyzer to get better parser error
 *
 * Revision 1.1  2005/06/27 08:51:50  alex_simov
 * refactoring: *.io.locator -> *.locator
 * refactoring: *.io.parser -> *.parser
 * refactoring: *.io.datastore -> *.datastore
 *
 * Revision 1.4  2005/06/22 14:40:49  alex_simov
 * Copyright header added/updated
 *
 * Revision 1.3  2005/06/01 10:56:44  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.2  2005/05/11 14:02:03  marin
 * formatting
 *
 * Revision 1.1.1.1  2005/05/10 15:12:12  marin
 * no message
 *
 * Revision 1.2  2004/12/11 11:15:43  marin_dimitrov
 * formatting:
 * 1. indent is 4 spaces
 * 2. line break indent is 8 spaces
 * 3. no tabs
 *
 * Revision 1.1  2004/11/30 13:21:22  marin_dimitrov
 * parser stuff moved from org.wsmo.io --> org.wsmo.io.parser
 *
 * Revision 1.2  2004/11/29 16:19:34  damyan_rm
 * non-default public constructor added
 *
 * Revision 1.1  2004/11/26 11:26:10  marin_dimitrov
 * Parser::parse now throws ParserException and InvalidModelException
 */