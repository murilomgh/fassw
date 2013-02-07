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

package com.ontotext.wsmo4j.examples;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import org.omwg.ontology.Attribute;
import org.omwg.ontology.Concept;
import org.omwg.ontology.Instance;
import org.omwg.ontology.Ontology;
import org.omwg.ontology.Parameter;
import org.omwg.ontology.Relation;
import org.omwg.ontology.RelationInstance;
import org.omwg.ontology.WsmlDataType;
import org.wsmo.common.Namespace;
import org.wsmo.common.TopEntity;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.factory.DataFactory;
import org.wsmo.factory.Factory;
import org.wsmo.factory.WsmoFactory;
import org.wsmo.wsml.Serializer;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004-2005
 * </p>
 * <p>
 * Company: Ontotext Lab., Sirma AI
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class CreateOntologyExample {

  private WsmoFactory factory;

  private DataFactory dataFactory;

  public static Ontology showExample() throws InvalidModelException {
    CreateOntologyExample example = new CreateOntologyExample();
    example.createFactory();
    return example.createSimpleOntology();
  }

  private void createFactory() {
    // use default implementation for factory
    factory = Factory.createWsmoFactory(null);
    dataFactory = Factory.createDataFactory(null);
  }

  private Ontology createSimpleOntology() throws InvalidModelException {
    // TODO: TO BE UPDATED!
    Ontology ontology = factory.createOntology(factory.createIRI("http://www.example.org/ontologies/example"));
    Namespace nsDefault = factory.createNamespace("default", factory
                        .createIRI("http://www.example.org/ontologies/example"));
    Namespace nsDC = factory.createNamespace("dc", factory.createIRI("http://purl.org/dc/elements/1.1"));

    // add NFP values
    ontology.addNFPValue(factory.createIRI(nsDefault, "title"), dataFactory.createWsmlString("WSML example ontology"));
    ontology.addNFPValue(factory.createIRI(nsDefault, "date"), dataFactory.createWsmlDate(2005, 9, 15, 12, 30));
    ontology.addNFPValue(factory.createIRI(nsDC, "rights"), factory.createIRI("http://www.deri.org/privacy.html"));

    // create a concept human
    Concept human = factory.createConcept(factory.createIRI(nsDefault, "Human"));

    // create attribute of concept hasName of type wsmlString
    Attribute attributeName = human.createAttribute(factory.createIRI(nsDefault, "hasName"));
    attributeName.addType(dataFactory.createWsmlDataType(WsmlDataType.WSML_STRING));

    // create attribute of concept hasAge of type wsmlInteger
    Attribute attributeAge = human.createAttribute(factory.createIRI(nsDefault, "hasAge"));
    attributeAge.addType(dataFactory.createWsmlDataType(WsmlDataType.WSML_INTEGER));

    // create attribute of concept hasChild of type Human inverseOf(hasParent)
    Attribute attributeChild = human.createAttribute(factory.createIRI(nsDefault, "hasChild"));
    attributeChild.addType(human);
    attributeChild.setInverseOf(factory.createIRI(nsDefault, "hasParent"));

    // create attribute of concept hasParent of type Human inverseOf(hasChild)
    Attribute attributeParent = human.createAttribute(factory.createIRI(nsDefault, "hasParent"));
    attributeParent.addType(human);
    attributeParent.setInverseOf(factory.createIRI(nsDefault, "hasChild"));

    // create atrribute of concept isMarriedTo of type Human symmetric
    Attribute attributeMarriedTo = human.createAttribute(factory.createIRI(nsDefault, "isMarriedTo"));
    attributeMarriedTo.addType(human);
    attributeMarriedTo.setSymmetric(true);
    attributeMarriedTo.setMinCardinality(0);
    attributeMarriedTo.setMaxCardinality(1);

    // create a concept man and woman
    Concept man = factory.createConcept(factory.createIRI(nsDefault, "Man"));
    man.addSuperConcept(human);
    Concept woman = factory.createConcept(factory.createIRI(nsDefault, "Woman"));
    woman.addSuperConcept(human);

    // create a concept Country
    Concept country = factory.createConcept(factory.createIRI(nsDefault, "Country"));
    Attribute attributeCountrName = country.createAttribute(factory.createIRI(nsDefault, "hasCountryName"));
    attributeCountrName.addType(dataFactory.createWsmlDataType(WsmlDataType.WSML_STRING));

    // create relation CitizenOf with parameters Human and Country
    Relation citizenOf = factory.createRelation(factory.createIRI(nsDefault, "CitizenOf"));
    Parameter parameterHuman = citizenOf.createParameter((byte) 0);
    parameterHuman.addType(human);
    Parameter parameterCountry = citizenOf.createParameter((byte) 1);
    parameterCountry.addType(country);

    // create Man with name Joe age 28, wife Mary and son Paul
    Instance joe = factory.createInstance(factory.createIRI(nsDefault, "Joe"), man);
    joe.addAttributeValue(attributeName.getIdentifier(), dataFactory.createWsmlString("Joe"));
    joe.addAttributeValue(attributeAge.getIdentifier(), dataFactory.createWsmlInteger("28"));
    joe.addAttributeValue(attributeChild.getIdentifier(), factory.getInstance(factory.createIRI(nsDefault, "Paul")));
    joe.addAttributeValue(attributeMarriedTo.getIdentifier(), factory.getInstance(factory.createIRI(nsDefault, "Mary")));

    // create Woman with name Joe age 27, husband Joe and son Paul
    Instance mary = factory.createInstance(factory.createIRI(nsDefault, "Mary"), woman);
    mary.addAttributeValue(attributeName.getIdentifier(), dataFactory.createWsmlString("Mary"));
    mary.addAttributeValue(attributeAge.getIdentifier(), dataFactory.createWsmlInteger("27"));
    mary.addAttributeValue(attributeChild.getIdentifier(), factory.getInstance(factory.createIRI(nsDefault, "Paul")));
    mary.addAttributeValue(attributeMarriedTo.getIdentifier(), factory.getInstance(factory.createIRI(nsDefault, "Joe")));

    // create Man with name Paul age 4 and parent Mary and Joe
    Instance paul = factory.createInstance(factory.createIRI(nsDefault, "Paul"), man);
    paul.addAttributeValue(attributeName.getIdentifier(), dataFactory.createWsmlString("John"));
    paul.addAttributeValue(attributeAge.getIdentifier(), dataFactory.createWsmlInteger("4"));
    paul.addAttributeValue(attributeParent.getIdentifier(), factory.getInstance(factory.createIRI(nsDefault, "Joe")));
    paul.addAttributeValue(attributeParent.getIdentifier(), factory.getInstance(factory.createIRI(nsDefault, "Mary")));

    // create countries UK, France
    Instance uk = factory.createInstance(factory.createIRI(nsDefault, "UK"), country);
    uk.addAttributeValue(attributeCountrName.getIdentifier(), dataFactory.createWsmlString("UK"));
    Instance france = factory.createInstance(factory.createIRI(nsDefault, "France"), country);
    france.addAttributeValue(attributeCountrName.getIdentifier(), dataFactory.createWsmlString("France"));

    // Joe would be from UK
    RelationInstance relInstanceJoe = factory.createRelationInstance(citizenOf);
    relInstanceJoe.setParameterValue((byte) 0, paul);
    relInstanceJoe.setParameterValue((byte) 1, uk);

    // Mary from France
    RelationInstance relInstanceMary = factory.createRelationInstance(citizenOf);
    relInstanceMary.setParameterValue((byte) 0, mary);
    relInstanceMary.setParameterValue((byte) 1, france);

    // Paul from UK
    RelationInstance relInstancePaul = factory.createRelationInstance(citizenOf);
    relInstancePaul.setParameterValue((byte) 0, joe);
    relInstancePaul.setParameterValue((byte) 1, uk);

    // add all ontology elements to the ontology
    ontology.addConcept(human);
    ontology.addConcept(man);
    ontology.addConcept(woman);
    ontology.addConcept(country);
    ontology.addRelation(citizenOf);
    ontology.addInstance(joe);
    ontology.addInstance(mary);
    ontology.addInstance(paul);
    ontology.addInstance(uk);
    ontology.addInstance(france);
    ontology.addRelationInstance(relInstanceJoe);
    ontology.addRelationInstance(relInstanceMary);
    ontology.addRelationInstance(relInstancePaul);

    return ontology;    
  }
  
  public static void main(String[] args) throws InvalidModelException, IOException {
     Ontology ontology = CreateOntologyExample.showExample();
     
     Serializer serializer = Factory.createSerializer(new HashMap<String, Object>(0));
     serializer.serialize(new TopEntity[] {ontology}, new PrintWriter(System.out));
  }
}



/*
 * $Log: CreateOntologyExample.java,v $
 * Revision 1.6  2007/06/06 07:27:54  lcekov
 * Add main method to the example
 *
 * Revision 1.5  2007/06/06 07:15:52  lcekov
 * example had compilation errors
 *
 * Revision 1.4  2007/06/06 06:59:40  lcekov
 * This is commit with errors in one of the examples. It should trigger automatic build of wsmo4j project
 * Revision 1.3 2006/02/10 14:41:38
 * vassil_momtchev examples updated
 * 
 * Revision 1.2 2005/09/23 12:20:42 holgerlausen *** empty log message ***
 * 
 * Revision 1.1 2005/09/19 10:20:22 vassil_momtchev inherite OntologyExample
 * 
 */
