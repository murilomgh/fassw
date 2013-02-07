package org.deri.wsmo4j.io.serializer.wsml;

import java.io.*;
import java.util.*;

import org.wsmo.common.*;
import org.wsmo.service.choreography.*;
import org.wsmo.service.choreography.io.*;

import com.ontotext.wsmo4j.common.*;
import com.ontotext.wsmo4j.serializer.wsml.*;

public class SerializerImpl extends WSMLSerializerImpl implements Serializer {

    public SerializerImpl(Map <String, Object> map) {
        super(map);
    }

    public void serialize(final TopEntity[] topItem, final Writer writer) throws IOException {
        if (topItem.length > 0) {
            new ChoreographySerializerWSML(writer, topItem[0]).process(topItem);
        }
    }
    
    public String serialize(Choreography chor) {
        StringWriter writer = new StringWriter();    
        //TODO: Remove dependence between serializer and wsmo4j impl
        ChoreographySerializerWSML chorSer = 
            new ChoreographySerializerWSML(writer, new TopEntityImpl(new UnnumberedAnonymousIDImpl()));
        chorSer.helpChoreography(chor);
        return writer.toString();
    }
}

/*
 * $Log: SerializerImpl.java,v $
 * Revision 1.4  2007/04/02 13:05:18  morcen
 * Generics support added to wsmo-ext
 *
 * Revision 1.3  2006/02/01 15:28:40  vassil_momtchev
 * serialize(Choreography chor) bug fixed
 *
 * Revision 1.2  2006/02/01 14:44:15  vassil_momtchev
 * implements org.wsmo.service.choreography.io.Serializer interface
 *
 * Revision 1.1  2005/11/29 13:59:21  vassil_momtchev
 * serialization implementation to support choreography api
 *
*/