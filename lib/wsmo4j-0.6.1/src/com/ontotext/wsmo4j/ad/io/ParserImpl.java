/*
 AD extension - a WSMO API and Reference Implementation

 Copyright (c) 2004-2006, OntoText Lab. / SIRMA

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

package com.ontotext.wsmo4j.ad.io;

import java.util.*;

import org.deri.wsmo4j.io.parser.wsml.*;
import org.wsmo.factory.*;

import com.ontotext.wsmo4j.factory.*;
import com.ontotext.wsmo4j.parser.wsml.*;

public class ParserImpl extends org.deri.wsmo4j.io.parser.wsml.ParserImpl {

    //TODO: Find a better place to put the constant
    public final static String PARSER_AD_FACTORY = "parser_ad_factory";

    protected ADFactory adFactory;

    public ParserImpl(Map map) {
        super(map);
        Object o = map.get(PARSER_AD_FACTORY);
        if (o == null || !(o instanceof ChoreographyFactory)) {
            o = new ADFactoryImpl();
        }
        adFactory = (ADFactory) o;
    }

    protected ASTAnalysisContainer createContainer() {
        ASTAnalysisContainer container = super.createContainer();
        new ADAnalyzer(container, factory, cFactory, adFactory);
        new ChoreographyAnalysis(container, cFactory, oFactory, factory);
        new RuleAnalyzer(container, factory, oFactory, cFactory, LEFactory);
        return container;
    }
}

/*
 * $Log: ParserImpl.java,v $
 * Revision 1.1  2006/12/08 17:11:13  vassil_momtchev
 * initial version of the parser
 *
*/
