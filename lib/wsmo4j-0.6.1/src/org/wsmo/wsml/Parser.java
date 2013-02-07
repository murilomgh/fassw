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

package org.wsmo.wsml;


import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.wsmo.common.TopEntity;
import org.wsmo.common.exception.InvalidModelException;


/**
 *
 * @author not attributable
 * @version $Revision: 1.15 $ $Date: 2007/04/02 12:13:15 $
 */
public interface Parser {
    
    public static final String CLEAR_MODEL = "clear_model_on_parse";
    public static final String CACHE_LOGICALEXPRESSION_STRING = "cache_le_string";

    /**
     * Parses the input data consisted of Ontology, Goal, Mediator or
     * WebServices definitions.
     * @param src The reader to read from
     * @return a top level WSMO entity found in the input
     * @throws java.io.IOException
     * @throws org.wsmo.wsml.ParserException
     * @throws org.wsmo.common.exception.InvalidModelException
     */

    TopEntity[] parse(Reader src)
            throws IOException, ParserException, InvalidModelException;

    /**
     * Parses the input data consisted of Ontology, Goal, Mediator or
     * WebServices definitions.
     * @param src The reader to read from
     * @param options an optional Map with user supplied options specific to this parse operation
     * @return a top level WSMO entity found in the input
     * @throws java.io.IOException
     * @throws org.wsmo.wsml.ParserException
     * @throws org.wsmo.common.exception.InvalidModelException
     */

    TopEntity[] parse(Reader src, Map options)
            throws IOException, ParserException, InvalidModelException;

    /**
     * Parses the input data consisted of Ontology, Goal, Mediator or
     * WebServices definitions.
     * @param src The string buffer to read from
     * @return top level WSMO entity found in the input
     * @throws org.wsmo.wsml.ParserException
     * @throws org.wsmo.common.exception.InvalidModelException
     */

    TopEntity[] parse(StringBuffer src)
            throws ParserException, InvalidModelException;

    /**
     * Parses the input data consisted of Ontology, Goal, Mediator or
     * WebServices definitions.
     * @param src The string buffer to read from
     * @param options an optional Map with user supplied options specific to this parse operation
     * @return top level WSMO entity found in the input
     * @throws org.wsmo.wsml.ParserException
     * @throws org.wsmo.common.exception.InvalidModelException
     */

    TopEntity[] parse(StringBuffer src, Map options)
            throws ParserException, InvalidModelException;

    /**
     * List known keywords.
     * @return set of known keywords
     */
    Set <String> listKeywords();
    
    /**
     * This method returns a List containing warnings that 
     * occured during the parsing.
     * 
     * @return List of collected warnings
     */
    public List <Object> getWarnings();
    
    /**
     * This method returns a List containing errors that occured during 
     * the parsing.
     * 
     * @return List of collected errors
     */
    public List <Object> getErrors();
}
/*
 * $Log: Parser.java,v $
 * Revision 1.15  2007/04/02 12:13:15  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.14  2006/11/17 15:20:49  holgerlausen
 * added constant to the parser enabling to "cache" the formating of logical expressions.
 *
 * Revision 1.13  2006/11/10 11:08:54  nathaliest
 * added getWarnings() and getErrors() methods to Parser interface, implemented them in the rdf parser implementation and added UnsupportedOperationException to the other parser implementations
 *
 * Revision 1.12  2006/04/11 16:06:59  holgerlausen
 * addressed RFE 1468651 ( http://sourceforge.net/tracker/index.php?func=detail&aid=1468651&group_id=113501&atid=665349)
 * currently the default behaviour of the parser is still as before
 *
 * Revision 1.11  2006/01/11 13:02:06  marin_dimitrov
 * common constants moved to Factory
 *
 * Revision 1.10  2006/01/09 16:16:42  nathaliest
 * deleted duplicate entry from wsmo4j.properties and renamed the parser_le_factory and the parser_wsmo_factory to le_factory and wsmo_factory.
 *
 * Revision 1.9  2005/12/09 11:26:54  vassil_momtchev
 * listKeywords method added
 *
 * Revision 1.8  2005/09/16 12:31:13  marin_dimitrov
 * DataFactory can now be created from the meta factory
 *
 * Revision 1.7  2005/07/19 13:52:17  vassil_momtchev
 * more than one topentity allowed in the wsml files (again)
 *
 * Revision 1.6  2005/07/04 11:44:40  marin_dimitrov
 * formatting?
 *
 * Revision 1.4  2005/06/30 13:34:45  marin_dimitrov
 * javadoc
 *
 * Revision 1.3  2005/06/30 13:10:35  marin_dimitrov
 * parse() now returns a SINGLE TopEntity (was: array of TEs)
 *
 * Revision 1.2  2005/06/30 09:52:53  marin_dimitrov
 * Parser and Serialiser split
 *
 * Revision 1.1  2005/06/30 09:31:47  alex_simov
 * refactoring: org.wsmo.parser -> org.wsmo.wsml.*
 *
 * Revision 1.1  2005/06/27 08:51:50  alex_simov
 * refactoring: *.io.locator -> *.locator
 * refactoring: *.io.parser -> *.parser
 * refactoring: *.io.datastore -> *.datastore
 *
 * Revision 1.6  2005/06/22 15:00:46  marin_dimitrov
 * note about Parser *not* closing the stream after write
 *
 * Revision 1.5  2005/06/22 14:40:49  alex_simov
 * Copyright header added/updated
 *
 * Revision 1.4  2005/06/01 10:56:44  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.5  2005/05/19 08:10:20  marin
 * oops
 *
 * Revision 1.4  2005/05/13 15:38:26  marin
 * fixed javadoc errors
 *
 * Revision 1.3  2005/05/11 14:02:03  marin
 * formatting
 *
 * Revision 1.2  2005/05/11 09:35:03  marin
 * fixed imports + changed Identifiable to TopEntity
 *
 * Revision 1.1.1.1  2005/05/10 15:12:12  marin
 * no message
 *
 * Revision 1.3  2005/02/04 11:37:40  alex_simov
 * mistyping: 'tagret' -> 'target' in serialize()
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
 * Revision 1.7  2004/11/26 11:26:10  marin_dimitrov
 * Parser::parse now throws ParserException and InvalidModelException
 *
 * Revision 1.6  2004/11/09 10:59:49  damyan_rm
 * fixed using Identifiable. see [ 1058788 ] Parser::parse() should return Entity
 *
 * Revision 1.5  2004/10/31 19:43:57  marin_dimitrov
 * 1. fixed @author and @version tags
 * 2. history log added at the end of file
 *
 */
