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

package org.deri.wsmo4j.io.parser.wsml;

import java.util.*;

import org.deri.wsmo4j.choreography.*;
import org.deri.wsmo4j.orchestration.*;
import org.wsmo.factory.*;

import com.ontotext.wsmo4j.parser.wsml.*;

public class ParserImpl extends com.ontotext.wsmo4j.parser.wsml.ParserImpl {

    //TODO: Find a better place to put the constant
    public final static String PARSER_CHOREOGRAPHY_FACTORY = "parser_choreography_factory";
    public final static String PARSER_ORCHESTRATION_FACTORY = "parser_orchestration_factory";

    protected ChoreographyFactory cFactory;
    
    protected OrchestrationFactory oFactory;

    public ParserImpl(Map map) {
        super(map);
        Object o = map.get(PARSER_CHOREOGRAPHY_FACTORY);
        if (o == null || !(o instanceof ChoreographyFactory)) {
            //TODO: Use factory to create the ChoreographyFactory
            o = new ChoreographyFactoryRI();
        }
        cFactory = (ChoreographyFactory) o;
        
        o = map.get(PARSER_ORCHESTRATION_FACTORY);
        if (o == null || !(o instanceof OrchestrationFactory)) {
            //TODO: Use factory to create the OrchestrationFactory
            o = new OrchestrationFactoryRI();
        }
        oFactory = (OrchestrationFactory) o;
    }

    protected ASTAnalysisContainer createContainer() {
        ASTAnalysisContainer container = super.createContainer();
        new ChoreographyAnalysis(container, cFactory, oFactory, factory);
        new RuleAnalyzer(container, factory, oFactory, cFactory, LEFactory);
        return container;
    }
}

/*
 * $Log: ParserImpl.java,v $
 * Revision 1.10  2006/10/24 14:11:47  vassil_momtchev
 * choreography/orchestration rules refactored. different types where appropriate now supported
 *
 * Revision 1.9  2006/06/07 13:03:06  vassil_momtchev
 * orchestration factory added
 *
 * Revision 1.8  2006/04/17 07:56:00  vassil_momtchev
 * unused imports removed
 *
 * Revision 1.7  2006/03/24 15:39:18  vassil_momtchev
 * helper method parseCompoundFact(TopEntity nsHolder, String factText) removed; logic to be implemented by the applications that use wsmo4j
 *
 * Revision 1.6  2006/02/10 15:35:34  vassil_momtchev
 * new grammar implemented
 *
 * Revision 1.5  2006/01/09 15:31:55  alex_simov
 * Namespace serialization bugfix in compiling CompoundFact
 *
 * Revision 1.4  2005/12/21 13:55:10  vassil_momtchev
 * parseCompoundFact(TopEntity, String) return type changed from Set to List
 *
 * Revision 1.3  2005/12/21 13:30:15  vassil_momtchev
 * helper method parse compoundfact added
 *
 * Revision 1.2  2005/12/21 11:40:19  vassil_momtchev
 * rule analyzer added
 *
 * Revision 1.1  2005/11/28 16:00:41  vassil_momtchev
 * choreography parser added extending com.ontotext.wsmo4j.parser.wsml.ParserImpl (ParserImpl.java)
 * AST analysis of choreography (ChoreographyAnalysis.java)
 *
*/
