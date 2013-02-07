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
package org.deri.wsmo4j.factory;


import java.util.*;

import org.omwg.logicalexpression.*;
import org.omwg.logicalexpression.terms.*;
import org.wsmo.factory.*;


/**
 * <pre>
 *
 *  Created on Jun 20, 2005
 *  Committed by $Author$
 *
 * </pre>
 *
 * @author holger.lausen@deri.org
 * @version $Revision$ $Date$
 * @see org.omwg.logexpression.LogicalExpressionFactory
 */
public abstract class AbstractLogicalExpressionFactoryImpl
        implements LogicalExpressionFactory {

    /**
     * @see LogicalExpressionFactory#createAttribusteValues(Term, Term, List)
     */
    public CompoundMolecule createAttribusteValues(Term instanceID, Term attributeID, List attributeValues) throws IllegalArgumentException {
        if (attributeValues == null){
            throw new IllegalArgumentException("list of attribute values must contain at least 2 terms");
        }
        List <Molecule> molecules = new Vector <Molecule>();
        Iterator i = attributeValues.iterator();
        while (i.hasNext()){
            Term term;
            try{
                term=(Term)i.next();
            }catch (ClassCastException e){
                throw new IllegalArgumentException("list of attribute values can only contain terms");
            }
            molecules.add(createAttributeValue(instanceID,attributeID, term));
        }
        return createCompoundMolecule(molecules);
    }

    /**
     * @see LogicalExpressionFactory#createAttributeConstraints(Term, Term, List)
     */
    public CompoundMolecule createAttributeConstraints(Term instanceID, Term attributeID, List attributeTypes) throws IllegalArgumentException {
        if (attributeTypes == null){
            throw new IllegalArgumentException("list of attribute types must contain at least 2 term");
        }
        List <Molecule> molecules = new Vector <Molecule> ();
        Iterator i = attributeTypes.iterator();
        while (i.hasNext()){
            Term term;
            try{
                term=(Term)i.next();
            }catch (ClassCastException e){
                throw new IllegalArgumentException("list of attribute types can only contain terms");
            }
            molecules.add(createAttributeConstraint(instanceID,attributeID, term));
        }
        return createCompoundMolecule(molecules);
    }

    /**
     * @see LogicalExpressionFactory#createAttributeInferences(Term, Term, List)
     */
    public CompoundMolecule createAttributeInferences(Term instanceID, Term attributeID, List attributeTypes) throws IllegalArgumentException {
        if (attributeTypes == null){
            throw new IllegalArgumentException("list of attribute types must contain at least 2 term");
        }
        List <Molecule> molecules = new Vector <Molecule> ();
        Iterator i = attributeTypes.iterator();
        while (i.hasNext()){
            Term term;
            try{
                term=(Term)i.next();
            }catch (ClassCastException e){
                throw new IllegalArgumentException("list of attribute types can only contain terms");
            }
            molecules.add(createAttributeInference(instanceID,attributeID, term));
        }
        return createCompoundMolecule(molecules);
    }

    /**
     * @see LogicalExpressionFactory#createMemberShipMolecules(Term, List)
     */
    public CompoundMolecule createMemberShipMolecules(Term identifier, List concepts){
        if (concepts == null){
            throw new IllegalArgumentException("list of concepts must contain at least 2 terms");
        }
        List <Molecule> molecules = new Vector <Molecule> ();
        Iterator i = concepts.iterator();
        while (i.hasNext()){
            Term concept;
            try{
                concept=(Term)i.next();
            }catch (ClassCastException e){
                throw new IllegalArgumentException("list of concept ids can only contain terms");
            }
            molecules.add(createMemberShipMolecule(identifier, concept));
        }
        return createCompoundMolecule(molecules);
    }
    
    /**
     * @see LogicalExpressionFactory#createSubConceptMolecules(Term, List)
     */
    public CompoundMolecule createSubConceptMolecules(Term identifier, List concepts){
        if (concepts == null){
            throw new IllegalArgumentException("list of concepts must contain at least 2 terms");
        }
        List <Molecule> molecules = new Vector <Molecule> ();
        Iterator i = concepts.iterator();
        while (i.hasNext()){
            Term concept;
            try{
                concept=(Term)i.next();
            }catch (ClassCastException e){
                throw new IllegalArgumentException("list of concept ids can only contain terms");
            }
            molecules.add(createSubConceptMolecule(identifier, concept));
        }
        return createCompoundMolecule(molecules);
    }


}

/*
 * $Log$
 * Revision 1.2  2007/04/02 12:13:24  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.1  2006/02/16 19:40:27  holgerlausen
 * added convinience methods to logical expression factory:
 * RFE 113501
 *
 */