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

import java.io.*;
import java.util.Map;

import org.w3c.dom.Document;
import org.wsmo.grounding.sawsdl.Grounding;
import org.wsmo.grounding.sawsdl.io.Factory;
import org.wsmo.grounding.sawsdl.io.GroundingSerializer;

import com.ontotext.wsmo4j.grounding.sawsdl.GroundingImpl;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;


public class GroundingSerializerImpl implements GroundingSerializer {

    private String xmlEncoding = "UTF-8";
    
    public GroundingSerializerImpl(Map props) {
        if (props.containsKey(Factory.ENCODING)) {
            xmlEncoding = (String)props.get(Factory.ENCODING);
        }
    }
    
    public void serialize(Grounding grounding, Writer output) throws Exception {

        if (false == grounding instanceof GroundingImpl) {
            throw new RuntimeException("Incorrect serializer usage: "
                    + " expected object of type com.ontotext.wsmo4j.grounding.sawsdl.GroundingImpl");
        }
        Document doc = ((GroundingImpl)grounding).getWSMLDocument();
        OutputFormat of = new OutputFormat("XML", this.xmlEncoding, true);
        XMLSerializer serializer = new XMLSerializer(output, of);
        serializer.asDOMSerializer();
        serializer.serialize(doc);
    }

}

/*
 * $Log: GroundingSerializerImpl.java,v $
 * Revision 1.1  2007/04/25 16:40:51  alex_simov
 * no message
 *
 */
