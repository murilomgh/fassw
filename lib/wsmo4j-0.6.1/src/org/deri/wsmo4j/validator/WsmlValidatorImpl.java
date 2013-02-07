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

import java.util.*;

import org.omwg.logicalexpression.LogicalExpression;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.SynchronisationException;
import org.wsmo.factory.Factory;
import org.wsmo.factory.LogicalExpressionFactory;
import org.wsmo.validator.*;


/**
 * Interface or class description
 *
 * <pre>
 *  Created on Aug 18, 2005
 *  Committed by $Author: morcen $
 *  $Source: /cvsroot/wsmo4j/wsmo4j/org/deri/wsmo4j/validator/WsmlValidatorImpl.java,v $,
 * </pre>
 *
 * @author Holger Lausen (holger.lausen@deri.org)
 * @author nathalie.steinmetz@deri.org
 * @version $Revision: 1.27 $ $Date: 2007/04/02 12:13:20 $
 * @see org.wsmo.validator.WsmlValidator
 */
public class WsmlValidatorImpl
        implements WsmlValidator {

    public LogicalExpressionFactory leFactory = null;

    public boolean validateImports = true;
    
    // set containing the identifiers of all ontologies that have been tried to be validated
    public HashSet <Identifier> visitedOntologies = new HashSet <Identifier> ();
    
    public WsmlValidatorImpl(){
        this(null);
    }

    /**
     * The WsmlValidator is initialised based on the supplied preferences.
     * The properties map can contain the factories to be used as Strings or as
     * instances. If a factory to use is only indicated as String, the constructor
     * needs to create an instance of this given factory.
     *
     * @param properties the preferences for the validator.
     */
    public WsmlValidatorImpl(Map properties) {
        if (properties != null && properties.containsKey(Factory.LE_FACTORY)) {
        Object o = properties.get(Factory.LE_FACTORY);
            if (o == null || !(o instanceof LogicalExpressionFactory)) {
                o = Factory.createLogicalExpressionFactory(new HashMap <String, Object>());
            }
            leFactory = (LogicalExpressionFactory) o;
        }

        if (leFactory == null) {
            leFactory = Factory.createLogicalExpressionFactory(new HashMap <String, Object>());
        }
        assert (leFactory != null);  
        
        if (properties != null && properties.containsKey(WsmlValidator.VALIDATE_IMPORTS)) {
        	Object o = properties.get(WsmlValidator.VALIDATE_IMPORTS);
        	// by default imported ontologies are validated
        	if (o == null || !(o instanceof Boolean)) {
        		o = new Boolean(true);
        	}
        	validateImports = ((Boolean) o).booleanValue();
        }
    }
    
    /**
     * When no variant is specified in the TopEntity, the TopEntity is checked for valid wsml-full,
     * otherwise it is validated against the variant stated in the TopEntity.
     *
     * @param te TopEntity
     * @param errorMessages list that will be filled with error messages dound during validation
     * @param warningMessages list that will be filled with warning messages found during validation
     * @return true/false, if the TopEntity is valid to the TopEntity's variant or not
     * @see org.wsmo.validator.Validator#isValid(org.wsmo.common.TopEntity, java.util.List, 
     *      java.util.List)
     */
    public boolean isValid(TopEntity te, List <ValidationError> errorMessages, List <ValidationWarning> warningMessages) {
    	String variant = te.getWsmlVariant();
        if (variant == null) {
            variant = WSML.WSML_FULL;
        }
        return isValid(te, variant, errorMessages, warningMessages);
    }

    /**
     * The TopEntity is validated against the given variant.
     *
     * @param te TopEntity to be validated.
     * @param variant to be validated against (String representation of the corresponging IRI 
     * 			e.g. "http://www.wsmo.org/wsml/wsml-syntax/wsml-core")
     * @param errorMessages list that will be filled with error messages found during validation
     * @param warningMessages list that will be filled with warning messages found during validation
     * @return true/false, if the TopEntity is valid with respect to the variant or not
     * @see org.wsmo.validator.WsmlValidator#isValid(org.wsmo.common.TopEntity, java.lang.String, java.util.List, java.util.List)
     * @see ValidationError
     * @see ValidationWarning
     */
    public boolean isValid(TopEntity te, String variant, List <ValidationError> errorMessages, List <ValidationWarning> warningMessages) {
        List <ValidationError> errors = errorMessages;
        List <ValidationWarning> warnings = warningMessages;
        if (errors == null){
            errors = new LinkedList <ValidationError> ();
        }
        if (warnings== null){
            warnings = new LinkedList <ValidationWarning> ();
        }

        if (te.getWsmlVariant() == null){
            warnings.add(new ValidationWarningImpl(
                    te,
                    "No WSML variant specified",
                    "Specify the variant using the wsmlVariant keyword with the corresponding IRI"));
        }
        if (te.getWsmlVariant() != null &&
                ! (te.getWsmlVariant().equals(WSML.WSML_CORE)||
                te.getWsmlVariant().equals(WSML.WSML_DL)||
                te.getWsmlVariant().equals(WSML.WSML_RULE)||
                te.getWsmlVariant().equals(WSML.WSML_FLIGHT)||
                te.getWsmlVariant().equals(WSML.WSML_FULL))){
            errors.add(new ValidationErrorImpl(te,
                    "The WSML variant specified is not valid ("+
                    te.getWsmlVariant()+")", WSML.WSML_FULL));
        }

        if (validateImports) {
        	locateOntologies(te, errors, warnings);
        }
        else if (te.listOntologies().size() > 0){
        	warnings.add(new ValidationWarningImpl(te, "The feature to validate " +
        			"imported ontologies is disabled. Thus we cannot check the " +
        			"validity of eventually imported ontologies.", "Enable " +
					"the validation of imported ontologies by setting the validator " +
					"preference VALIDATE_IMPORTS on true."));
        }
        
        if (variant.equals(WSML.WSML_CORE)) {
            WsmlCoreValidator v = new WsmlCoreValidator(leFactory);
            return (v.isValid(te, errors, warnings));
        }
        else if (variant.equals(WSML.WSML_DL)) {
            WsmlDLValidator v = new WsmlDLValidator(leFactory);
            return (v.isValid(te,errors, warnings));
        }
        else if (variant.equals(WSML.WSML_FLIGHT)) {
            WsmlFlightValidator v = new WsmlFlightValidator(leFactory);
            return (v.isValid(te, errors, warnings));
        }
        else if (variant.equals(WSML.WSML_RULE)) {
            WsmlRuleValidator v = new WsmlRuleValidator(leFactory);
            return (v.isValid(te, errors, warnings));
        }
        else if (variant.equals(WSML.WSML_FULL)) {
            WsmlFullValidator v = new WsmlFullValidator(leFactory);
            return (v.isValid(te, errors, warnings));
        }
        else {
            throw new IllegalArgumentException(
                    "Invalid WSML Variant specified: \"" + variant +
                    "\". Expected String representation of respective IRI, e.g: \""+WSML.WSML_CORE+"\"");
        }
    }

    /**
     * The logical expression is validated against the given variant.
     *
     * @param logExpr LogicalExpression to be checked
     * @param variant to be validated against (String representation of the corresponging IRI 
     * 		  e.g. "http://www.wsmo.org/wsml/wsml-syntax/wsml-core")
     * @param errorMessages list that will be filled with error messages dound during validation
     * @param warningMessages list that will be filled with warning messages found during validation
     * @return true/false, if the LogicalExpression is valid to the given variant or not
     * @see org.wsmo.validator.Validator#isValid(LogicalExpression, String, java.util.List, 
     *      java.util.List)
     */
	public boolean isValid(LogicalExpression logExpr, String variant, List <ValidationError> errorMessages, List <ValidationWarning> warningMessages) {
		List <ValidationError> errors = errorMessages;
        List <ValidationWarning>warnings = warningMessages;
        if (errors == null){
            errors = new LinkedList <ValidationError> ();
        }
        if (warnings== null){
            warnings = new LinkedList <ValidationWarning> ();
        }
        
        if (variant.equals(WSML.WSML_CORE)) {
            WsmlCoreValidator v = new WsmlCoreValidator(leFactory);
            return (v.isValid(logExpr, errors, warnings));
        }
        else if (variant.equals(WSML.WSML_DL)) {
            WsmlDLValidator v = new WsmlDLValidator(leFactory);
            return (v.isValid(logExpr,errors, warnings));
        }
        else if (variant.equals(WSML.WSML_FLIGHT)) {
            WsmlFlightValidator v = new WsmlFlightValidator(leFactory);
            return (v.isValid(logExpr, errors, warnings));
        }
        else if (variant.equals(WSML.WSML_RULE)) {
            WsmlRuleValidator v = new WsmlRuleValidator(leFactory);
            return (v.isValid(logExpr, errors, warnings));
        }
        else if (variant.equals(WSML.WSML_FULL)) {
            WsmlFullValidator v = new WsmlFullValidator(leFactory);
            return (v.isValid(logExpr, errors, warnings));
        }
        else {
            throw new IllegalArgumentException(
                    "Invalid WSML Variant specified: \"" + variant +
                    "\". Expected String representation of respective IRI, e.g: \""+WSML.WSML_CORE+"\"");
        }
	}
    
    /**
     * This method checks for which wsml variant the given TopEntity is valid.
     *
     * @param te TopEntity to be checked
     * @return String variant, the IRI of the variant that te is in, null if not valid wsml full
     * @see org.wsmo.validator.WsmlValidator#determineVariant(org.wsmo.common.TopEntity, java.util.List, java.util.List)
     */
    public String determineVariant(TopEntity te, List <ValidationError> errorMessages, List <ValidationWarning> warningMessages) {
        List <ValidationError> errors = errorMessages;
        String variant = null;
        WsmlFullValidator vf = new WsmlFullValidator(leFactory);
        if (vf.isValid(te, errors, warningMessages)) {
            variant = WSML.WSML_FULL;
            WsmlRuleValidator vr = new WsmlRuleValidator(leFactory);
            if (vr.isValid(te, errors, warningMessages)) {
                variant = WSML.WSML_RULE;
                WsmlFlightValidator vfl = new WsmlFlightValidator(leFactory);
                if (vfl.isValid(te, errors, warningMessages)) {
                    variant = WSML.WSML_FLIGHT;
                    WsmlCoreValidator vc = new WsmlCoreValidator(leFactory);
                    if (vc.isValid(te, errors, warningMessages)) {
                        variant = WSML.WSML_CORE;
                    }
                }
            }
            else {
            	errors.clear();
            	warningMessages.clear();
	            WsmlDLValidator vdl = new WsmlDLValidator(leFactory);
	            if (vdl.isValid(te, errors, warningMessages)) {
	            	variant = WSML.WSML_DL;
	            }
            }
        }
        return variant;
    }
    
    /**
     * This method checks for which wsml variant the given logical expression is valid.
     *
     * @param logExpr LogicalExpression to be checked
     * @return String variant, the IRI of the variant that logExpr is in, null if not valid wsml full
     * @see org.wsmo.validator.WsmlValidator#determineVariant(LogicalExpression, java.util.List, java.util.List)
     */
    public String determineVariant(LogicalExpression logExpr, List <ValidationError> errorMessages, List <ValidationWarning> warningMessages) {
    	List <ValidationError> errors = errorMessages;
        String variant = null;
        WsmlFullValidator vf = new WsmlFullValidator(leFactory);
        if (vf.isValid(logExpr, errors, warningMessages)) {
            variant = WSML.WSML_FULL;
            WsmlRuleValidator vr = new WsmlRuleValidator(leFactory);
            if (vr.isValid(logExpr, errors, warningMessages)) {
                variant = WSML.WSML_RULE;
                WsmlFlightValidator vfl = new WsmlFlightValidator(leFactory);
                if (vfl.isValid(logExpr, errors, warningMessages)) {
                    variant = WSML.WSML_FLIGHT;
                    WsmlCoreValidator vc = new WsmlCoreValidator(leFactory);
                    if (vc.isValid(logExpr, errors, warningMessages)) {
                        variant = WSML.WSML_CORE;
                    }
                }
            }
            else {
            	errors.clear();
            	warningMessages.clear();
	            WsmlDLValidator vdl = new WsmlDLValidator(leFactory);
	            if (vdl.isValid(logExpr, errors, warningMessages)) {
	            	variant = WSML.WSML_DL;
	            }
            }
        }
        return variant;
	}

    private void locateOntologies(TopEntity te, List <ValidationError> errors, List <ValidationWarning> warnings) {
    	for (Iterator it = te.listOntologies().iterator(); it.hasNext();){
    		Ontology ontology = (Ontology) it.next();
    		if (!visitedOntologies.contains(ontology.getIdentifier())){
    			visitedOntologies.add(ontology.getIdentifier());
    			List <ValidationError> errorMessages = new Vector <ValidationError> ();
    			String variant = null;
    			try {
    				variant = ontology.getWsmlVariant();
    	
    				if (ontology.getWsmlVariant() == null) {
    					variant = WSML.WSML_FULL;
    				}
    				boolean valid = isValid(ontology, errorMessages, warnings);
    				if (!valid) {	
    					errors.add(new ValidationErrorImpl(te, ValidationError.IMPORT_ERR + 
    							":\nImported ontology " + ontology.getIdentifier() + " is not valid " +
    							"against the variant specified in the file.", variant));
    				}
    				boolean badVariant = false;
    				String teVariant = te.getWsmlVariant();
    				if (teVariant!=null){
    					if ((te.getWsmlVariant().equals(WSML.WSML_RULE) && 
    							variant.equals(WSML.WSML_FULL)) || 
    							(te.getWsmlVariant().equals(WSML.WSML_FLIGHT) && 
    							(variant.equals(WSML.WSML_RULE) || variant.equals(WSML.WSML_FULL))) || 
		    					(te.getWsmlVariant().equals(WSML.WSML_DL) && 
		    					(variant.equals(WSML.WSML_FLIGHT) || variant.equals(WSML.WSML_RULE) || 
		    					variant.equals(WSML.WSML_FULL))) || 
		    					(te.getWsmlVariant().equals(WSML.WSML_CORE) && 
		    					!(variant.equals(WSML.WSML_CORE)))) {
		    					badVariant = true;
    					}
    				}
    				if (badVariant) {
    					errors.add(new ValidationErrorImpl(te, ValidationError.IMPORT_ERR + 
    							":\nThe variant of the imported ontology " + ontology.getIdentifier() + 
    							" is higher than the variant of the main ontology.", variant));
    				}	
    			} catch (SynchronisationException e) {
    				warnings.add(new ValidationWarningImpl(te, "Could not validate " +
    						"Ontology: " + ontology.getIdentifier() + ", as it is " +
    						"only a proxy, not a real imported ontology", "First import ontology " +
    								ontology.getIdentifier() + ", and then validate again"));
    			}
    		}
    	}
    }
    
}