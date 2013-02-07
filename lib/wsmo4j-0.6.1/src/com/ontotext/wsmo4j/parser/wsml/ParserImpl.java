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

import java.io.*;
import java.util.*;

import org.deri.wsmo4j.io.parser.*;
import org.deri.wsmo4j.io.parser.wsml.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.factory.*;
import org.wsmo.validator.ValidationError;
import org.wsmo.validator.ValidationWarning;
import org.wsmo.wsml.*;
import org.wsmo.wsml.compiler.lexer.*;
import org.wsmo.wsml.compiler.node.*;

public class ParserImpl implements Parser {

    protected WsmoFactory factory;
    protected LogicalExpressionFactory LEFactory;
    protected DataFactory dataFactory;
    protected boolean cleanOnParse=false;
    protected boolean memorizeLELook=false;

    public ParserImpl(Map map) {
        Object o = map.get(Factory.WSMO_FACTORY);
        if (o == null || !(o instanceof WsmoFactory)) {
            o = Factory.createWsmoFactory(new HashMap <String, Object> ());
        }
        factory = (WsmoFactory) o;
        assert (factory != null);

        o = map.get(Factory.LE_FACTORY);
        if (o == null || !(o instanceof LogicalExpressionFactory)) {
            o = Factory.createLogicalExpressionFactory(new HashMap <String, Object> ());
        }
        LEFactory = (LogicalExpressionFactory) o;
        assert (LEFactory != null);

        o = map.get(Factory.DATA_FACTORY);
        if (o == null || !(o instanceof DataFactory)) {
            o = Factory.createDataFactory(new HashMap <String, Object> ());
        }
        dataFactory = (DataFactory) o;
        assert (dataFactory != null);
        
        o = map.get(Parser.CLEAR_MODEL);
        if (o != null && !o.toString().equals("false")){
            cleanOnParse=true;
        }
        
        o = map.get(Parser.CACHE_LOGICALEXPRESSION_STRING);
        if (o != null && !o.toString().equals("false")){
            memorizeLELook=true;
        }
    }

    public TopEntity[] parse(Reader src) throws IOException, ParserException, InvalidModelException {
        return _parse(src);
    }

    public TopEntity[] parse(Reader src, Map options) throws IOException, ParserException,
            InvalidModelException {
        throw new UnsupportedOperationException("Use method parse(Reader) instead!");
    }

    public TopEntity[] parse(StringBuffer src) throws ParserException, InvalidModelException {
        try {
            return _parse(new StringReader(src.toString()));
        }
        catch (IOException e) {
            // should never happens
            throw new RuntimeException("I/O error occured!", e);
        }
    }

    public TopEntity[] parse(StringBuffer src, Map options) throws ParserException,
            InvalidModelException {
        throw new UnsupportedOperationException("Use method parse(StringBuffer) instead!");
    }

    protected ASTAnalysisContainer createContainer() {
        ASTAnalysisContainer container = new ASTAnalysisContainer();

        new IdentifierAnalysis(container, factory);
        new OntologyAnalysis(container, factory, dataFactory).setCleanOnParse(cleanOnParse);
        new MediatorAnalysis(container, factory).setCleanOnParse(cleanOnParse);
        new NFPAnalysis(container);
        new ValueAnalysis(container, dataFactory, LEFactory, factory);
        new TopEntityAnalysis(container, factory);
        new ServiceAnalysis(container, factory, LEFactory).setCleanOnParse(cleanOnParse);
        new AxiomAnalysis(container, factory);
        new VariableAnalysis(container, LEFactory);
        new AtomicExpressionAnalysis(container, factory,LEFactory);
        new CompoundExpressionAnalysis(container, LEFactory);
        return container;
    }

    private TopEntity[] _parse(Reader src) throws ParserException,
    InvalidModelException, IOException {
        ASTAnalysisContainer container = createContainer();

        if (memorizeLELook){
            src = LogExprParserImpl.findLogicalExpressions(
                src,container.getStack(String.class));
        }

        Lexer lexer = new Lexer(new PushbackReader(src, 16384));
        org.wsmo.wsml.compiler.parser.Parser parser =
            new org.wsmo.wsml.compiler.parser.Parser(lexer);

        try {
            Start head = parser.parse();
            head.apply(container);
            Stack stack = container.getStack(TopEntity.class);
            TopEntity[] topEntities = new TopEntity[stack.size()];
            for (int i = 0; i < topEntities.length; i++) {
                topEntities[i] = (TopEntity) stack.remove(0);
            }
            return topEntities;
        }
        catch (org.wsmo.wsml.compiler.parser.ParserException pe) {
            ParserException e = new ParserException(ParserException.NOT_VALID_PARSETREE,pe);
            Token t = pe.getToken();
            e.setErrorLine(t.getLine());
            e.setErrorPos(t.getPos());
            e.setFoundToken(t.getText());
            try{
                e.setExpectedToken(pe.getMessage().split("expecting: ")[1].trim());
            }
            catch (IndexOutOfBoundsException iobE){
                //if error message does not follow usual pattern
                e.setExpectedToken(pe.getMessage());
            }
            throw e;
        }
        catch (org.wsmo.wsml.compiler.lexer.LexerException le) {
            ParserException e = new ParserException(ParserException.NOT_VALID_PARSETREE, le);
            try{
                e.setErrorLine(Integer.parseInt(le.getMessage().split(",")[0].split("\\[")[1]));
            }catch (NumberFormatException nfE){
                //could not find line so leave default
            }
            try{
                e.setErrorPos(Integer.parseInt(le.getMessage().split(",")[1].split("\\]")[0]));
            }catch (NumberFormatException nfE){
                //could not find pos so leave default
            }
            throw e;
        }
        catch (WrappedParsingException wpe) {
            if (wpe.getWrappedException() instanceof ParserException){
                throw (ParserException) wpe.getWrappedException();
            }
            throw new RuntimeException(wpe);
        }
    }

    public Set <String> listKeywords() {
        String[] wsmlTokens = ASTAnalysisContainer.WSML_TOKENS;
        return new HashSet <String>(Arrays.asList(wsmlTokens));
    }
    
    /*
     *  (non-Javadoc)
     * @see org.wsmo.wsml.Parser#getWarnings()
     */
    public List <Object> getWarnings() {
		throw new UnsupportedOperationException("This method is not implemented for WSML parsing");
	}
    
    /*
     *  (non-Javadoc)
     * @see org.wsmo.wsml.Parser#getErrors()
     */
	public List <Object> getErrors() {
		throw new UnsupportedOperationException("This method is not implemented for WSML parsing");
	}
}

/*
 * $Log: ParserImpl.java,v $
 * Revision 1.9  2007/04/02 12:13:28  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.8  2006/11/17 15:07:59  holgerlausen
 * added an option to the parser to "remember" the original format of logical expressions, added the representation of the original String to LEImpl, and added to the serializer to check for the orginal formating to not not make complex expressions unreadable when serialized.
 *
 * Revision 1.7  2006/11/10 11:08:53  nathaliest
 * added getWarnings() and getErrors() methods to Parser interface, implemented them in the rdf parser implementation and added UnsupportedOperationException to the other parser implementations
 *
 * Revision 1.6  2006/06/21 07:46:29  vassil_momtchev
 * createVariable(String) method moved from WsmoFactory to LogicalExpressionFactory interface
 *
 * Revision 1.5  2006/04/11 16:06:58  holgerlausen
 * addressed RFE 1468651 ( http://sourceforge.net/tracker/index.php?func=detail&aid=1468651&group_id=113501&atid=665349)
 * currently the default behaviour of the parser is still as before
 *
 * Revision 1.4  2006/01/11 13:03:30  marin_dimitrov
 * common constants moved to Factory
 *
 * Revision 1.3  2005/12/14 08:58:56  vassil_momtchev
 * removed hardcoded wsmo and datafactory impl
 *
 * Revision 1.2  2005/12/09 11:28:42  vassil_momtchev
 * listkeywords method added; returns all known tokens from the grammar
 *
 * Revision 1.1  2005/11/28 13:55:26  vassil_momtchev
 * AST analyses
 *
*/
