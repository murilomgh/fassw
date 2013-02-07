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

package org.wsmo.wsml;


import java.io.*;
import java.util.*;

import org.wsmo.common.*;

/**
 *
 * @author not attributable
 * @version $Revision: 1.4 $ $Date: 2005/07/22 12:29:33 $
 */
public interface Serializer {
    /**
     * serializes Ontology, Goal, Mediator or a Webservice
     * @param item A top level WSMO item to serialize
     * Note that since the parser expects an open stream it will <b>not</b>
     * attempt to close the stream after writing to it. E.g. it is the responsibility of the user
     * to open/close the stream.
     * @param target The writer to write to
     * @throws java.io.IOException
     */
    void serialize(TopEntity[] item, Writer target)
            throws IOException;

    /**
     * serializes Ontology, Goal, Mediator or a Webservice
     * @param item A top level WSMO item to serialize
     * Note that since the parser expects an open stream it will <b>not</b>
     * attempt to close the stream after writing to it. E.g. it is the responsibility of the user
     * to open/close the stream.
     * @param target The writer to write to
     * @param options an optional Map with user supplied options specific to this serialisation
     * @throws java.io.IOException
     */
    void serialize(TopEntity[] item, Writer target, Map options)
            throws IOException;

    /**
     * serializes Ontology, Goal, Mediator or a Webservice
     * @param item A top level WSMO item to serialize
     * in the same string buffer
     * @param target The buffer to write to
     */
    void serialize(TopEntity[] item, StringBuffer target);

    /**
     * serializes Ontology, Goal, Mediator or a Webservice
     * @param item A top level WSMO item to serialize
     * in the same string buffer
     * @param target The buffer to write to
     * @param options an optional Map with user supplied options specific to this serialization
     */
    void serialize(TopEntity[] item, StringBuffer target, Map options);

    String SERIALIZER_LE_SERIALIZER = "serializer_le_serializer";
}
/*
 * $Log: Serializer.java,v $
 * Revision 1.4  2005/07/22 12:29:33  vassil_momtchev
 * works with array of entities (again!)
 *
 * Revision 1.3  2005/07/01 13:16:08  marin_dimitrov
 * added an optional Map param
 *
 * Revision 1.2  2005/06/30 13:42:44  marin_dimitrov
 * serialize() now accepts a SINGLE TopEntity (was: array of TEs)
 *
 * Revision 1.1  2005/06/30 12:29:16  alex_simov
 * renamed
 *
 * Revision 1.2  2005/06/30 09:52:53  marin_dimitrov
 * Parser and Serialiser split
 *
 */
