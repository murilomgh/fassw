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

import java.lang.reflect.*;

import org.omwg.ontology.*;
import org.wsmo.common.exception.*;
import org.wsmo.common.*;

import com.ontotext.wsmo4j.common.EntityImpl;

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */

public abstract class OntologyElementImpl extends EntityImpl implements OntologyElement {

    private Ontology ownerOntology;

    private static final Method[] ontologyMethods = Ontology.class.getMethods();
    
    public OntologyElementImpl(Identifier id) {
        super(id);
    }

    /**
     * Retrieve the owner of the Instance.
     * @throws org.wsmo.common.exception.SynchronisationException
     */
    public Ontology getOntology() throws SynchronisationException {
        return ownerOntology;
    }

    /**
     * Sets a new owner of the Instance.
     * @throws InvalidModelException 
     * @throws org.wsmo.common.exception.SynchronisationException
     */
    public void setOntology(Ontology ontology) throws InvalidModelException {
        if ((ontology == null && ownerOntology == null) || (ontology == ownerOntology)) {
            return;
        }
        if (ownerOntology != null) {
            Ontology oldOntology = ownerOntology;
            ownerOntology = null;
            invokeOntologyMethod("remove", oldOntology);
        }
        if (ontology != null) {
            ownerOntology = ontology;
            invokeOntologyMethod("add", ontology);
        }
    }

    private void invokeOntologyMethod(String operation, Ontology ontologyToInvoke) {
        Class[] supportedInterfaces = getClass().getInterfaces();

        // find the method from Ontology that starts with operationXXXEntity(handleToXXX)
        for (int i = 0; i < supportedInterfaces.length; i++) {
            for (int j = 0; j < ontologyMethods.length; j++) {
                if (ontologyMethods[j].getName().equals(
                        operation + getSimpleName(supportedInterfaces[i]))
                        && ontologyMethods[j].getParameterTypes().length == 1
                        && getSimpleName(ontologyMethods[j].getParameterTypes()[0]).equals(
                            getSimpleName(supportedInterfaces[i])))
                    try {
                        ontologyMethods[j].invoke(ontologyToInvoke, new Object[] { this });
                        return;
                    }
                    catch (Exception e) {
                        throw new RuntimeException(
                                "Could not " + operation + " from the ontology!", e);
                    }
            }
        }

        throw new RuntimeException("Could not " + operation + " from the ontology!");
    }
    
    private String getSimpleName(Class myClass) {
        int pos = myClass.getName().lastIndexOf(".");
        if (pos > -1 && pos + 1 < myClass.getName().length()) {
            return myClass.getName().substring(pos+1);
        }
        return myClass.getName();
    }
    
}

/*
 * $Log: OntologyElementImpl.java,v $
 * Revision 1.7  2006/03/23 16:12:13  nathaliest
 * moving the anon Id check to the validator
 *
 * Revision 1.6  2006/03/01 15:01:53  vassil_momtchev
 * Type is ambigious if compiled with java5 compiler
 *
 * Revision 1.5  2006/02/27 15:28:10  nathaliest
 * fixed anonId check
 *
 * Revision 1.4  2006/02/27 10:22:20  nathaliest
 * added anonId check at element.setOntology(ontology).
 *
 * Revision 1.3  2005/09/15 11:41:45  vassil_momtchev
 * getShortName() removed (java 1.4 compatible)
 *
 * Revision 1.2  2005/09/15 08:40:00  vassil_momtchev
 * OntologyElement.setOntology(Ontology) removes the element from the previous ontology if exists, and adds it to the new if not null
 *
 * Revision 1.1  2005/06/01 12:09:05  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.3  2005/05/12 13:46:44  marin
 * changed to an _abstract_ class (not supposed to be used directly but only through subclassing)
 *
 * Revision 1.2  2005/05/11 13:35:22  alex
 * copyright notice updated
 *
 * Revision 1.1  2005/05/11 12:40:58  alex
 * initial commit
 *
 */
