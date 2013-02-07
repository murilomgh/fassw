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

package com.ontotext.wsmo4j.serializer.wsml;

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.wsmo.common.TopEntity;
import org.wsmo.wsml.Serializer;

public class WSMLSerializerImpl implements Serializer {

    public WSMLSerializerImpl(Map <String, Object> props) {

    }

    public void serialize(TopEntity[] item, StringBuffer writer) {
        StringWriter wrt = new StringWriter();
        try {
            serialize(item, wrt);
            writer.append(wrt.getBuffer());
        }
        catch (IOException ioe) {
            throw new RuntimeException("cannot write due to an exception:", ioe);
        }
    }

    public void serialize(TopEntity[] item, StringBuffer writer, Map options) {
        throw new UnsupportedOperationException("Unimplemented method serialize(TopEntity, StringBuffer, Map)");
    }
   
    
    public void serialize(TopEntity[] topItem, Writer writer) throws IOException {
        //quick hack, since WSMLTextExportHelper expects an array
        new WSMLTextExportHelper(writer).process(topItem);
    }

    public void serialize(TopEntity[] item, Writer writer, Map options) {
        throw new UnsupportedOperationException("Unimplemented method serialize(TopEntity, Writer, Map)");
    }


}
