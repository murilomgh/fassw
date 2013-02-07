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
package org.deri.wsmo4j.io.serializer.xml;


import org.w3c.dom.*;

import org.omwg.logicalexpression.*;


/**
 * This class is used to serialize logical expressions to XML.
 * @author retkru
 * @see org.omwg.logicalexpression.io.Serializer
 */
public class LogExprSerializerXML {

    private VisitorSerializeXML xml;

    /**
     * @param doc Document that will be filled with the xml structure
     * @see org.w3c.dom.Document
     */
    public LogExprSerializerXML(Document doc) {
        xml = new VisitorSerializeXML(doc);
    }

    /**
     * @param logExpr Logical expression object model to be serialized
     * @return XML Node of the Document tree
     * @see org.omwg.logicalexpression.io.Serializer#serialize(org.omwg.logicalexpression.LogicalExpression)
     * @see org.w3c.dom.Node
     */
    public Node serialize(LogicalExpression logExpr) {
        logExpr.accept(xml);
        return (Node)xml.getSerializedObject();
    }

}
