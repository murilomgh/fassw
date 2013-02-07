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
package org.wsmo.validator;


/**
 * Gives Structure to a validation error.
 *
 * <pre>
 *  Created on November 15, 2005
 *  Committed by $Author: nathaliest $
 *  $Source: /cvsroot/wsmo4j/wsmo-api/org/wsmo/validator/ValidationError.java,v $,
 * </pre>
 *
 * @author Holger Lausen
 * @author nathalie.steinmetz@deri.org
 * @version $Revision: 1.7 $ $Date: 2006/08/22 16:24:09 $
 */
public interface ValidationError extends ValidationMessage{

    public static final String META_MODEL_ERR = "Meta Modelling Error:";
    
    public static final String CONC_ERR = "Concept Error";

    public static final String ANON_ID_ERR = "Error in use of anonymous identifiers";
    
    public static final String ATTR_ERR = "Attribute Error";
    
    public static final String ATTR_FEAT_ERR = "Attribute Feature Error";

    public static final String ATTR_CONS_ERR = "Attribute Constraint Error";

    public static final String ATTR_CARD_ERR = "Attribute Cardinality Error";

    public static final String REL_ARITY_ERR = "Relation Arity Error";

    public static final String REL_CONS_ERR = "Relation Constraint Error";

    public static final String REL_ERR = "Relation Error";
    
    public static final String REL_INST_ERR = "Relation Instance Error";

    public static final String AX_HEAD_ERR = "Axiom - Inadmissible Head formula";

    public static final String AX_BODY_ERR = "Axiom - Inadmissible Body formula";
    
    public static final String AX_FORMULA_ERR = "Axiom - Inadmissible formula";
    
    public static final String AX_LHS_ERR = "Axiom - Inadmissible left-hand side formula";

    public static final String AX_RHS_ERR = "Axiom - Inadmissible right-hand side formula";
    
    public static final String AX_ATOMIC_ERR = "Axiom - Inadmissible Atomic formula";
    
    public static final String AX_IMPL_BY_ERR = "Axiom - Inadmissible inverse implication formula";
    
    public static final String AX_IMP_ERR = "Axiom - Inadmissible implication formula";
    
    public static final String AX_EQUIV_ERR = "Axiom - Inadmissible equivalence formula";

    public static final String AX_SAFETY_COND = "Axiom - Safety condition doesn't hold for this logical expression";

    public static final String AX_GRAPH_ERR = "Axiom - Invalid Graph";
    
    public static final String ID_ERR = "Inadmissible Identifier";
    
    public static final String IMPORT_ERR = "Error at imported ontology";

    
    /**
     * Returns the String representation of URI representing the variant that the
     *         error violates
     * 
     * @return a String 
     */
    public String getViolatesVariant();

}

/*
 * $log: $
 */
