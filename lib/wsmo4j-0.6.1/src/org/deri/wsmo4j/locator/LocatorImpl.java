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
package org.deri.wsmo4j.locator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.omwg.ontology.Ontology;
import org.wsmo.common.Entity;
import org.wsmo.common.IRI;
import org.wsmo.common.Identifier;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.common.exception.SynchronisationException;
import org.wsmo.factory.Factory;
import org.wsmo.locator.Locator;
import org.wsmo.wsml.Parser;
import org.wsmo.wsml.ParserException;

/**
 * This locator is meant to be used for importing wsml files. He looks for a 
 * file given its IRI, and parses it into an WSMO object model.
 * 
 * By default, the locator only imports ontologies from logical URIs. 
 * Given a mapping from logical URIs to physical locations, he can also 
 * locate ontologies from physical locations. This feature is enabled by 
 * submitting a HashMap containing such a mapping in the preferences map.
 *   HashMap preferences = new HashMap();
 *   HashMap mapping = new HashMap();
 *   mapping.put(...,...);
 *	 preferences.put(Factory.PROVIDER_CLASS, "org.deri.wsmo4j.locator.LocatorImpl");
 *   preferences.put(Locator.URI_MAPPING, mapping);
 *
 * <pre>
 *  Created on Aug 22, 2006
 *  Committed by $Author: morcen $
 *  $Source: /cvsroot/wsmo4j/wsmo4j/org/deri/wsmo4j/locator/LocatorImpl.java,v $,
 * </pre>
 *
 * @author nathalie.steinmetz@deri.org
 * @version $Revision: 1.4 $ $Date: 2007/04/02 12:13:28 $
 */
public class LocatorImpl implements Locator {
	
	private Parser wsmlParser = null;
	
	private HashMap mapping = new HashMap();
	
	public LocatorImpl(Map prefs) {
		if (prefs != null) {
			// this preference indicates a given wsmo parser to use
			Object o = prefs.get(Factory.WSMO_PARSER);
			if (o == null || !(o instanceof Parser)) {
				o = Factory.createParser(new HashMap <String, Object>());
			}
			wsmlParser = (Parser) o;
			
			// this preference submits a mapping from logical URIs to physical file 
			// locations, e.g. the file identified by "http://example.org/ex" is 
			// located on "file:/C:/exampleFiles/example.wsml"
			o = prefs.get(Locator.URI_MAPPING);
			if (o != null && o instanceof HashMap) {
				mapping = (HashMap) o;
			}
		}
	}
	
	public Set <Entity> lookup(Identifier id) throws SynchronisationException {
		Set <Entity> set = new HashSet <Entity>();
		Ontology ontology = (Ontology) lookup(id, null);
		if (ontology != null) {
			set.add(ontology);
		}
		return set;
	}

	public Entity lookup(Identifier id, Class clazz) {
		if (id instanceof IRI) {
			URL urlId = null;
			String idString = null;
			if (!mapping.isEmpty() && mapping.containsKey(id.toString())) {
				idString = (String) mapping.get(id.toString());
			}
			try {
				if (idString != null) {
					urlId = new URL(idString);
				}
				else {
					urlId = new URL(id.toString());
				}
				InputStream is = urlId.openStream();
				if (is != null) {
					// assuming first topentity in file is an ontology  
					return (Ontology) wsmlParser.parse(new InputStreamReader(is))[0]; 
				}
			} catch (MalformedURLException e) {
				System.out.println(e.toString() + " at: " + urlId.toString());
				return null;
			} catch (IOException e) {
				System.out.println(e.toString() + " at: " + urlId.toString());
				return null;
			} catch (ParserException e) {
				System.out.println(e.toString() + " at: " + urlId.toString());
				return null;
			} catch (InvalidModelException e) {
				System.out.println(e.toString() + " at: " + urlId.toString());
				return null;
			}
		}
		return null;
	}

}
/*
 * $Log: LocatorImpl.java,v $
 * Revision 1.4  2007/04/02 12:13:28  morcen
 * Generics support added to wsmo-api, wsmo4j and wsmo-test
 *
 * Revision 1.3  2006/11/27 11:36:08  nathaliest
 * changed the validator to not import ontologies on its own and added documentation how to use the default locator to import ontologies
 *
 * Revision 1.2  2006/08/29 15:19:48  nathaliest
 * changed the validator to call the memory based locator manager and fixed a problem causing loops at the location of ontologies in the validator
 *
 * Revision 1.1  2006/08/22 16:25:49  nathaliest
 * added locator implementation and set it as default locator at the locator manager
 *
 * 
 */