/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2004-2007, Ontotext Lab. / SIRMA

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

package com.ontotext.wsmo4j.grounding.sawsdl.io;

import java.io.Reader;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.wsmo.grounding.sawsdl.Grounding;
import org.wsmo.grounding.sawsdl.io.GroundingParser;
import org.xml.sax.InputSource;

import com.ontotext.wsmo4j.grounding.sawsdl.GroundingImpl;

public class GroundingParserImpl implements GroundingParser {

    public GroundingParserImpl(Map props) {
        // no additional params currently are needed
    }

    public Grounding parse(Reader input) throws Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setIgnoringElementContentWhitespace(true);

        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource inputSource = new InputSource(input);
        Document doc = builder.parse(inputSource);

        if (doc == null) {
            throw new Exception("No WSDL data found!");
        }
        return new GroundingImpl(doc);
    }

}

/*
 * $Log: GroundingParserImpl.java,v $
 * Revision 1.1  2007/04/25 16:40:51  alex_simov
 * no message
 *
 */
