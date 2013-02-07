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


package org.wsmo.common;


import java.util.Set;

import org.omwg.ontology.Ontology;


/**
 * Base class for top level WSMO entities (ontologies, mediators, goals, services + capabilities and interfaces)
 *
 * @author not attributable
 * @version $Revision: 1.5 $ $Date: 2007/04/02 12:13:14 $
 * @since 0.4.0
 */
public interface TopEntity
    extends Entity {

    /**
     * returns the WSML variant used for the definition of this entity
     * @return the name of the WSML variant (as string)
     * @see WSML
     * @see #setWsmlVariant(String variant)
     */
    String getWsmlVariant();

    /**
     * specifies the WSML variant used for the definition of this entity
     * @param variant the name of the WSML variant (as string)
     * @see org.wsmo.common.WSML
     */
    void setWsmlVariant(String variant);

    /**
     * retrieves all avalible namespaces (prefix/iri pairs). 
     * Note: This does not include the default namespace
     * @return a set of namespaces
     * @see Namespace
     * @see #getDefaultNamespace()
     */
    Set <Namespace> listNamespaces();

    /**
     * add a namespace in the contaioner
     * @param ns to add
     * @see #removeNamespace(String)
     * @see #removeNamespace(Namespace)
     */
    void addNamespace(Namespace ns);

    /**
     * remove namespace from the top entity
     * @param prexif prexif of the namespcae to be removed
     */
    void removeNamespace(String prefix);

    /**
     * remove namespace from the top entity
     * @param ns namespace to be removed
     */
    void removeNamespace(Namespace ns);

    /**
     * finds a namespace by its prefix
     * @param prefix of a namespace to look for
     * @return a namesspace with such prefix or null if not found
     */
    Namespace findNamespace(String prefix);

    /**
     * retrieve the default namespace
     * @return default namespace or null if not set
     * @see #setDefaultNamespace(Namespace ns)
     * @see #setDefaultNamespace(IRI iri)
     */
    Namespace getDefaultNamespace();

    /**
     * sets a default namespace for the container
     * @param ns namespace to set as default
     * @see #setDefaultNamespace(IRI iri)
     */
    void setDefaultNamespace(Namespace ns);

    /**
     * sets a default namespace for the container
     * @param iri IRI of namespace to set as default
     * @see #setDefaultNamespace(Namespace ns)
     */
    void setDefaultNamespace(IRI iri);

    /**
     * Adds a reference to a new mediator to this element's used mediators list.
     * @param iri the ID of the used mediator reference
     * @see #removeMediator(IRI mediator)
     */
    void addMediator(IRI mediator);

    /**
     * Removes a reference to a mediator from this element's used mediator list.
     * @param iri The ID of the mediator to be removed from this element's list of used mediators.
     */
    void removeMediator(IRI iri);

    /**
     * Returns the list of used mediators.
     * @return : The list of used mediators.
     * @see org.wsmo.mediator.Mediator
     */
    Set <IRI> listMediators();

    /**
     * Adds an ontology to this element's imported ontologies list
     * @param ontology The ontology to be imported.
     * @see #removeOntology(Ontology ontology)
     * @see #removeOntology(IRI iri)
     */
    void addOntology(Ontology ontology);

    /**
     * Removes an ontology from the list of ontolgies which this element imports.
     * @param iri The ID of the ontology to be removed from this element's imported ontologies list.
     */
    void removeOntology(IRI iri);

    /**
     * Removes an ontology from the list of ontolgies which this element imports.
     * @param ontology The ontology to be removed from this element's imported ontologies list.
     */
    void removeOntology(Ontology ontology);

    /**
     * Returns a list of ontologies which this element imports.
     * @return : The list of ontologies that this element imports.
     * @see org.omwg.ontology.Ontology
     */
    Set <Ontology> listOntologies();

//DO NOT EDIT below this line

    /**
     * @supplierCardinality 1
     * @supplierRole default namespace
     * @directed
     */
    /*# Namespace lnkNamespace1; */

    /**
     * @supplierCardinality 0..*
     * @supplierRole namespaces
     * @directed
     */
    /*# Namespace lnkNamespace; */
}

/*
 * $Log: TopEntity.java,v $
 * Revision 1.5  2007/04/02 12:13:14  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.4  2006/04/05 13:21:52  vassil_momtchev
 * usesMediator now refer mediators by  IRI instead of handle to object
 *
 * Revision 1.3  2005/11/22 09:23:48  holgerlausen
 * added docu for namespace issue by jan (rfe 1348215)
 *
 * Revision 1.2  2005/09/21 08:15:39  holgerlausen
 * fixing java doc, removing asString()
 *
 * Revision 1.1  2005/06/01 10:30:24  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.5  2005/05/13 12:38:36  marin
 * added setDefaultNamespace(IRI) since the def namespace does not have a  prefix, e.g. it is not a complete Namespace object
 *
 * Revision 1.4  2005/05/12 14:42:57  marin
 * added @since tags
 *
 * Revision 1.3  2005/05/12 13:29:16  marin
 * javadoc, header, footer, etc
 *
 */
