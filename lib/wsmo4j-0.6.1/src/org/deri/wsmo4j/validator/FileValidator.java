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

package org.deri.wsmo4j.validator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.wsmo.common.TopEntity;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.factory.Factory;
import org.wsmo.factory.LogicalExpressionFactory;
import org.wsmo.factory.WsmoFactory;
import org.wsmo.validator.ValidationError;
import org.wsmo.validator.ValidationMessage;
import org.wsmo.validator.ValidationWarning;
import org.wsmo.validator.WsmlValidator;
import org.wsmo.wsml.Parser;
import org.wsmo.wsml.ParserException;

/**
 * A file validator that validates a given WSML document
 * given in the form of a file handle and prints complaints
 * from the parser and the validator out to the console.
 *
 * It is targeted at external programs rather than human
 * users, in particular at <i>glue</i> languages like perl
 * and phyton. All of its output adheres to the same regular
 * expression and is going to <code>stdout</code> rather than
 * <code>stderr</code>, defaulting to be accessible via 
 * shell pipes.
 * 
 * TODO make this class self-checking, to verify the 
 * outgoing lines against the regular expression
 *
 * <pre>
 * Created on Apr 16, 2006
 * Committed by $Author: morcen $
 * $Source: /cvsroot/wsmo4j/wsmo4j/org/deri/wsmo4j/validator/FileValidator.java,v $
 * </pre>
 * 
 * @author Thomas Haselwanter
 *
 * @version $Revision: 1.4 $ $Date: 2007/04/02 12:13:19 $code
 */ 
public class FileValidator {

	private static final String usage = "wsmlvalidator [file]";
    private Parser parser;
    private WsmoFactory factory;
    private LogicalExpressionFactory leFactory;
    private WsmlValidator validator;
	private List <ValidationError> errors;
	private List <ValidationWarning> warnings;
    
	public FileValidator() {
		super();
		initialise();
	}
    
	private void initialise() {        
        HashMap <String, Object> createParams = new HashMap <String, Object> ();
        createParams.put(Factory.PROVIDER_CLASS, "org.deri.wsmo4j.factory.LogicalExpressionFactoryImpl");
        leFactory = Factory.createLogicalExpressionFactory(createParams);
        factory = Factory.createWsmoFactory(null);
        createParams = new HashMap <String, Object> ();
        createParams.put(Factory.WSMO_FACTORY, factory);
        createParams.put(Factory.LE_FACTORY, leFactory);
        parser = Factory.createParser(createParams);
        validator = Factory.createWsmlValidator(null);
    }
        
    private void printMessages(List messages) {
    	String p = "";
    	for (int i = 0; i < messages.size(); i++) {
    		ValidationMessage vm  = (ValidationMessage) messages.get(i);
    		p = "Validator::[1,1]::" + vm.getClass().getName() + "::" + vm.getReason();    		
        	System.out.println(p);
    	}
    }

    private void validate(File document) 
    		throws FileNotFoundException, IOException, ParserException, InvalidModelException {
		if (!document.exists())
			throw new FileNotFoundException("File doesn't exist.");
		if (!document.canRead())
			throw new FileNotFoundException("File not readable, check permissions.");
		TopEntity[] topEntities = parser.parse(new FileReader(document));
		errors = new ArrayList <ValidationError> ();
		warnings = new ArrayList <ValidationWarning> ();
    	for (int i = 0; i < topEntities.length; i++) {
			validator.isValid(topEntities[i], errors, warnings);		
    	}
    }
    
	public static void main(String[] args) {
		FileValidator fv = new FileValidator();
		try {
			sanitycheck(args);
			fv.validate(new File(args[0]));
			fv.printMessages(fv.getErrors());
			fv.printMessages(fv.getWarnings());
		} catch (ParserException pe) {
			String token = pe.getFoundToken();
			if (token == null || token == "")
				token = " "; //fallback if somebody tries to precisely tag the error location
			System.out.println("Parser::[" + pe.getErrorLine() + "," + pe.getErrorPos() + "]::" + 
					pe.getFoundToken() + "::" + pe.getMessage());
		} catch (Throwable t) {
			System.out.println("Failure::[1,1]::" + t.getClass().getName() + "::" +
					"Failure: " + t.getClass().getName() + ": " + t.getStackTrace()[0].getClassName() + ":" + t.getStackTrace()[0].getLineNumber());
			t.printStackTrace(System.err);
			System.exit(1);
		}
	}

	private static void sanitycheck(String[] args) throws InvalidParameterException {
		//FIXME stronger sanity checks and sanitation
		if (args.length < 1) {
			throw new InvalidParameterException("No file name supplied. Usage: " + usage);
		}
	}

	public List getErrors() {
		return errors;
	}

	public List getWarnings() {
		return warnings;
	}
	
}
