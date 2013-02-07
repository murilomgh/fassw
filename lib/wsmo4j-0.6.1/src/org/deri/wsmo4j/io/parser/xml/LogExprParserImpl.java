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
package org.deri.wsmo4j.io.parser.xml;


import java.util.*;

import org.w3c.dom.*;
import org.wsmo.factory.*;
import org.wsmo.wsml.*;


/**
 * <p>This singleton class is used to create a LogicalExpression XML Parser</p>
 * <p>The Singleton design pattern allows to ensure that only one instance of a class
 * is created, to provide a global point of access to the object and to allow multiple
 * instances in the future without affecting a singleton class's clients</p>
 * @author retkru
 * @see org.omwg.logicalexpression.io.Parser
 * @see <a href="http://en.wikipedia.org/wiki/Singleton_pattern">Singleton Pattern</a>
 */
public class LogExprParserImpl {

    WsmoFactory factory;
    LogicalExpressionFactory leFactory;
    DataFactory dataFactory;
    XMLExprParser xmlParser;

    public LogExprParserImpl(Map map){
        if (map==null){
            map = new HashMap();
        }
        Object o = map.get(Factory.WSMO_FACTORY);
        if (o == null || ! (o instanceof WsmoFactory)) {
            o = Factory.createWsmoFactory(new HashMap <String, Object> ());
        }
        factory = (WsmoFactory)o;
        assert (factory != null);

        o = map.get(Factory.LE_FACTORY);
        if (o == null || ! (o instanceof LogicalExpressionFactory)) {
            o = Factory.createLogicalExpressionFactory(new HashMap <String, Object> ());
        }
        leFactory = (LogicalExpressionFactory)o;
        assert (leFactory != null);

        o = map.get(Factory.DATA_FACTORY);
        if (o == null || ! (o instanceof DataFactory)) {
            o = Factory.createDataFactory(new HashMap <String, Object> ());
        }
        dataFactory = (DataFactory)o;
        assert (dataFactory != null);

        xmlParser = new XMLExprParser(factory, leFactory, dataFactory);
    }

    /**
     * This method parses a XML Node.
     * @param node
     *            XML Element Node that will be parsed
     * @return logical expression object model
     * @throws IllegalArgumentException in case node is not of Type Node
     */
    public org.omwg.logicalexpression.LogicalExpression parse(Object node)
            throws IllegalArgumentException, ParserException {
        if (node instanceof Node) {
            Element exprNode = (Element)node;
            return xmlParser.evaluateXML(exprNode);
        }
        throw new IllegalArgumentException(
                "XML Parse Error: LogExprParser.parse requiers object of type org.w3c.dom.Node");
    }
}
