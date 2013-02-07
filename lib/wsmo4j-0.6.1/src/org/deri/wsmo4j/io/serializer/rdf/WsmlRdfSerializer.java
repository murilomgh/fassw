/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2006, University of Innsbruck, Austria

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
package org.deri.wsmo4j.io.serializer.rdf;

import java.io.*;
import java.util.*;

import org.openrdf.model.*;
import org.openrdf.model.impl.*;
import org.openrdf.rio.*;
import org.openrdf.rio.n3.*;
import org.openrdf.rio.ntriples.*;
import org.openrdf.rio.rdfxml.*;
import org.openrdf.rio.turtle.*;
import org.openrdf.vocabulary.*;
import org.wsmo.common.*;
import org.wsmo.wsml.*;

/**
 * XML serializer of WSMO object (check wsml-xml-syntax.xsd)
 * 
 * @author not attributable
 */
public class WsmlRdfSerializer implements Serializer {

    public final static String RDF_SERIALIZER_TYPE = "rdf_serializer";
    public final static Integer RDF_SERIALIZER_N3 = new Integer(1);
    public final static Integer RDF_SERIALIZER_NTRIPLES = new Integer(2);
    public final static Integer RDF_SERIALIZER_XML = new Integer(3);
    public final static Integer RDF_SERIALIZER_TURTLE = new Integer(4);

    private ValueFactory vf = new ValueFactoryImpl();
    private RdfDocumentWriter rdw = null;
    private int writerType = 0;
    
    /**
     * Constructor should not be invoked directly.
     *  Map properties = new TreeMap();
     *  properties.put(Factory.PROVIDER_CLASS, "com.ontotext.wsmo4j.xmlparser.WsmlXmlSerializer");
     *  serializer = Factory.createSerializer(properties);
     * @param map All parameters are ignored
     */
    public WsmlRdfSerializer(Map map) {
        if(map.get(RDF_SERIALIZER_TYPE)!=null) {
            writerType = ((Integer)map.get(RDF_SERIALIZER_TYPE)).intValue();
        } else {
            //default serialization = RDF/XML
            writerType = RDF_SERIALIZER_XML.intValue();
        }
    }
    
    public ValueFactory getFactory() { return vf; }
    public RdfDocumentWriter getWriter() { return rdw;}

    private RdfDocumentWriter setSerializer(Writer target) {
        switch(writerType) {
            case 1:
                return new N3Writer(target);
            case 2:
                return new NTriplesWriter(target);
            case 3:
                return new RdfXmlWriter(target);
            case 4:
                return new TurtleWriter(target);
            default:
                return null;
        }
    }
    
    /**
     * Serialize array of top entities to XML
     * @param item Entities to serialize
     * @param target Output
     */
    public void serialize(TopEntity[] item, Writer target) throws IOException {
        rdw = setSerializer(target);
        try {
            //rdf
            this.getWriter().setNamespace(
                    "rdf",
                    RDF.NAMESPACE);
            //rdfs
            this.getWriter().setNamespace(
                    "rdfs",
                    RDFS.NAMESPACE);
            //xsd
            this.getWriter().setNamespace(
                    "xsd",
                    XmlSchema.NAMESPACE);
            //defined namespace bindings
            for (int i = 0; i < item.length; i++) { 
                //default namespace
                this.getWriter().setNamespace(
                        "",
                        item[i].getDefaultNamespace().getIRI().toString());
                if(!item[i].listNamespaces().isEmpty()) {
                    Iterator iter = item[i].listNamespaces().iterator();
                    while (iter.hasNext()) {
                        Namespace ns = (Namespace)iter.next();
                        this.getWriter().setNamespace(
                                ns.getPrefix(),ns.getIRI().toString());
                        
                    }
                }
            }
            rdw.startDocument();
            
            NodeWsml.serializeTopEntity(item, this);
            
            rdw.endDocument();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Serialize array of top entities to XML
     * @param item Entities to serialize
     * @param target Output
     * @param options Ignored
     */
    public void serialize(TopEntity[] item, Writer target, Map options) throws IOException {
        serialize(item, target);
    }

    /**
     * Serialize array of top entities to XML
     * @param item Entities to serialize
     * @param target Output
     */
    public void serialize(TopEntity[] item, final StringBuffer target) {
        Writer myWriter = new Writer() {

            public void write(String arg0) throws IOException {
                target.append(arg0);
            }

            public void write(String arg0, int arg1, int arg2) throws IOException {
                target.append(arg0.toCharArray(), arg1, arg2);
            }

            public void write(int arg0) throws IOException {
                if (arg0 <= 255) {
                    target.append((char) arg0);
                }
                else {
                    byte[] bytes = new byte[] {(byte) (arg0 & 0x00FF), (byte) (arg0 & 0xFF00)};
                    target.append(new String(bytes));
                }
            }

            public void write(char[] arg0) throws IOException {
                target.append(arg0);
            }

            public void write(char[] arg0, int arg1, int arg2) throws IOException {
                target.append(arg0, arg1, arg2);
            }

            public void flush() throws IOException {
                return;
            }

            public void close() throws IOException {
                return;
            }
        };

        try {
            serialize(item, myWriter);
        }
        catch (IOException e) {
            return;
        }
    }

    /**
     * Serialize array of top entities to XML
     * @param item Entities to serialize
     * @param target Output
     * @param options Ignored
     */
    public void serialize(TopEntity[] item, StringBuffer target, Map options) {
        serialize(item, target);
    }
}

/*
 * $Log: WsmlRdfSerializer.java,v $
 * Revision 1.2  2006/11/10 15:38:49  ohamano
 * *** empty log message ***
 *
 * Revision 1.1  2006/11/10 15:02:59  ohamano
 * *** empty log message ***
 *
 * Revision 1.1  2006/11/10 10:08:42  ohamano
 * *** empty log message ***
 *
 *
*/