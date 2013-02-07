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
package org.deri.wsmo4j.io.serializer.wsml;


import org.deri.wsmo4j.io.parser.wsml.LogExprParserImpl;
import org.deri.wsmo4j.logicalexpression.LogicalExpressionImpl;
import org.omwg.logicalexpression.*;
import org.wsmo.common.*;


/**
 * This class is used to serialize logical expressions to WSML.
 * @author retkru hollau
 * @see org.omwg.logicalexpression.io.Serializer
 */
public class LogExprSerializerWSML{

    private VisitorSerializeWSML wsml;
    private TopEntity container = null;

    /**
     * @param container TopEntity
     */
    public LogExprSerializerWSML(TopEntity container) {
        this.container=container;
        wsml = new VisitorSerializeWSML(container);
    }

    /**
     * @param logExpr Logical expression object model to be serialized
     * @return serialized String object
     * @see org.omwg.logicalexpression.io.Serializer#serialize(org.omwg.logicalexpression.LogicalExpression)
     */
    public String serialize(LogicalExpression logExpr) {
        logExpr.accept(wsml);
        if (!(logExpr instanceof LogicalExpressionImpl)){
            return wsml.getSerializedObject() + ". ";
        }
        LogicalExpressionImpl logExprImpl = (LogicalExpressionImpl)logExpr;
        String originalLE = logExprImpl.getStringRepresentation();
        if (originalLE==null){
            return wsml.getSerializedObject() + ". ";
        }
        try{
            LogExprParserImpl p =  new LogExprParserImpl(null,container);
            LogicalExpression leFromOrgExpr =p.parse(originalLE);
            if (leFromOrgExpr.equals(logExpr)){
                //nice we can safe the old style of the LE
                return originalLE;
            }
        }catch (Throwable e){
            //OK we take the serializer version
            //e.printStackTrace();
        }
        return wsml.getSerializedObject() + ". ";
    }

}
