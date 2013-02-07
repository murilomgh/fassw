/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2004-2005, OntoText Lab. / SIRMA
                          University of Innsbruck, Austria

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

package com.ontotext.wsmo4j.parser.wsml;

import java.util.*;

import org.deri.wsmo4j.common.*;
import org.deri.wsmo4j.io.parser.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.factory.*;
import org.wsmo.mediator.*;
import org.wsmo.wsml.*;
import org.wsmo.wsml.compiler.node.*;

import com.ontotext.wsmo4j.parser.*;

public class MediatorAnalysis extends ASTAnalysis {

    private WsmoFactory factory;
    
    private ASTAnalysisContainer container;
    
    private boolean cleanOnParse=false;

    public MediatorAnalysis(ASTAnalysisContainer container, WsmoFactory factory) {
        if (container == null || factory == null) {
            throw new IllegalArgumentException();
        }
        this.factory = factory;
        this.container = container;

        // register the handled nodes
        container.registerNodeHandler(AOomediator.class, this);
        container.registerNodeHandler(AGgmediator.class, this);
        container.registerNodeHandler(AWgmediator.class, this);
        container.registerNodeHandler(AWwmediator.class, this);
        container.registerNodeHandler(ASource.class, this);
        container.registerNodeHandler(AMsources.class, this);
        container.registerNodeHandler(ATarget.class, this);
        container.registerNodeHandler(AUseService.class, this);
    }
    
    public void setCleanOnParse(boolean cleanOnParse){
        this.cleanOnParse=cleanOnParse;
    }
    
    private void cleanMediator(Mediator m){
        //clean previous contents of this mediator:
        try {
            ClearTopEntity.clearTopEntity(m);
        }
        catch (SynchronisationException e) {
            // should never happen
            throw new RuntimeException("Error During Cleaning TopEntity from previous defintions",e);
        }
        catch (InvalidModelException e) {
            // should never happen
            throw new RuntimeException("Error During Cleaning TopEntity from previous defintions",e);
        }

    }
    
    public void inAOomediator(AOomediator node) {
        TopEntityAnalysis.isValidTopEntityIdentifier(node.getId(),node.getTOomediator());
        node.getId().apply(container.getNodeHandler(PId.class));
        IRI iri = (IRI) container.popFromStack(Identifier.class, IRI.class);
        Mediator mediator = factory.createOOMediator(iri);
        if (cleanOnParse){
            cleanMediator(mediator);
        }
        container.getStack(Entity.class).push(mediator);
        container.getStack(TopEntity.class).push(mediator);

        TopEntityAnalysis.addNamespaceAndVariant(mediator, container.getStack(Namespace.class),
                container.getStack(AWsmlvariant.class));
        
    }

    public void inAGgmediator(AGgmediator node) {
        TopEntityAnalysis.isValidTopEntityIdentifier(node.getId(),node.getTGgmediator());
        node.getId().apply(container.getNodeHandler(PId.class));
        IRI iri = (IRI) container.popFromStack(Identifier.class, IRI.class);
        Mediator mediator = factory.createGGMediator(iri);
        if (cleanOnParse){
            cleanMediator(mediator);
        }
        container.getStack(Entity.class).push(mediator);
        container.getStack(TopEntity.class).push(mediator);

        TopEntityAnalysis.addNamespaceAndVariant(mediator, container.getStack(Namespace.class),
                container.getStack(AWsmlvariant.class));
    }

    public void inAWgmediator(AWgmediator node) {
        TopEntityAnalysis.isValidTopEntityIdentifier(node.getId(),node.getTWgmediator());
        node.getId().apply(container.getNodeHandler(PId.class));
        IRI iri = (IRI) container.popFromStack(Identifier.class, IRI.class);
        Mediator mediator = factory.createWGMediator(iri);
        if (cleanOnParse){
            cleanMediator(mediator);
        }
        container.getStack(Entity.class).push(mediator);
        container.getStack(TopEntity.class).push(mediator);

        TopEntityAnalysis.addNamespaceAndVariant(mediator, container.getStack(Namespace.class),
                container.getStack(AWsmlvariant.class));
    }

    public void inAWwmediator(AWwmediator node) {
        TopEntityAnalysis.isValidTopEntityIdentifier(node.getId(),node.getTWwmediator());
        node.getId().apply(container.getNodeHandler(PId.class));
        IRI iri = (IRI) container.popFromStack(Identifier.class, IRI.class);
        Mediator mediator = factory.createWWMediator(iri);
        if (cleanOnParse){
            cleanMediator(mediator);
        }
        container.getStack(Entity.class).push(mediator);
        container.getStack(TopEntity.class).push(mediator);

        TopEntityAnalysis.addNamespaceAndVariant(mediator, container.getStack(Namespace.class),
                container.getStack(AWsmlvariant.class));
    }

    public void outAOomediator(AOomediator node) {
        container.popFromStack(Entity.class, OOMediator.class);
    }

    public void outAGgmediator(AGgmediator node) {
        container.popFromStack(Entity.class, GGMediator.class);
    }

    public void outAWgmediator(AWgmediator node) {
        container.popFromStack(Entity.class, WGMediator.class);
    }

    public void outAWwmediator(AWwmediator node) {
        container.popFromStack(Entity.class, WWMediator.class);
    }
    
    public void inASource(ASource node) {
        TopEntityAnalysis.isValidTopEntityIdentifier(node.getId(),node.getTSource());
    }

    public void outASource(ASource node) {
        if (container.getStack(TopEntity.class).peek() instanceof Mediator == false) {
            // if it's unresolved ppMediator ignore it
            container.popFromStack(Identifier.class, IRI.class);
            return;
        }
        Mediator mediator = (Mediator) container.peekFromStack(TopEntity.class, Mediator.class);
        IRI iri = (IRI) container.popFromStack(Identifier.class, IRI.class);
        try {
            mediator.addSource(iri);
        }
        catch (InvalidModelException e) {
            throw new WrappedInvalidModelException(e);
        } 
    }
    
    private int idStackSize;
    
    public void inAMsources(AMsources node) {
        Stack identifierStack = container.getStack(Identifier.class);
        idStackSize = identifierStack.size();
    }
    
    public void outAMsources(AMsources node) {
        Mediator mediator = (Mediator) container.peekFromStack(TopEntity.class, Mediator.class);
        Stack identifierStack = container.getStack(Identifier.class);
        while (identifierStack.size() > idStackSize) {
            Identifier id = (Identifier) identifierStack.remove(idStackSize);
            if (id instanceof IRI == false) {
                ParserException pe = new ParserException("Anonymous sources "
                        + "are not supported!", null);
                pe.setExpectedToken("IRI");
                pe.setFoundToken(id.toString());
                pe.setErrorLine(node.getLbrace().getLine());
                pe.setErrorPos(node.getLbrace().getPos());
                throw new WrappedParsingException(pe);
            }
            
            try {
                mediator.addSource((IRI) id);
            }
            catch (InvalidModelException e) {
                throw new WrappedInvalidModelException(e);
            } 
        }
    }
    
    public void inATarget(ATarget node) {
        TopEntityAnalysis.isValidTopEntityIdentifier(node.getId(),node.getTTarget());
    }

    public void outATarget(ATarget node) {
        if (container.getStack(TopEntity.class).peek() instanceof Mediator == false) {
            // if it's unresolved ppMediator ignore it
            container.popFromStack(Identifier.class, IRI.class);
            return;
        }
        Mediator mediator = (Mediator) container.peekFromStack(TopEntity.class, Mediator.class);
        IRI iri = (IRI) container.popFromStack(Identifier.class, IRI.class);

        try {
            mediator.setTarget(iri);
        }
        catch (InvalidModelException e) {
            throw new WrappedInvalidModelException(e);
        }
    }
    
    public void inAUseService(AUseService node) {
        TopEntityAnalysis.isValidTopEntityIdentifier(node.getId(),node.getTUseservice());
    }

    public void outAUseService(AUseService node) {
        if (container.getStack(TopEntity.class).peek() instanceof Mediator == false) {
            // if it's unresolved ppMediator ignore it
            container.popFromStack(Identifier.class, IRI.class);
            return;
        }
        Mediator mediator = (Mediator) container.peekFromStack(TopEntity.class, Mediator.class);
        IRI iri = (IRI) container.popFromStack(Identifier.class, IRI.class);

        try {
            mediator.setMediationService(iri);
        }
        catch (InvalidModelException e) {
            throw new WrappedInvalidModelException(e);
        }
    }
}

/*
 * $Log: MediatorAnalysis.java,v $
 * Revision 1.12  2006/10/25 07:06:06  vassil_momtchev
 * ignore source, target and useservice when ppmediator are not resolved
 *
 * Revision 1.11  2006/04/24 08:04:58  holgerlausen
 * improved error handling in case of topentities without identifier
 * moved thomas unit test to "open" package, since it does not break expected behavior, but just document some derivations from the spec
 *
 * Revision 1.10  2006/04/11 16:06:58  holgerlausen
 * addressed RFE 1468651 ( http://sourceforge.net/tracker/index.php?func=detail&aid=1468651&group_id=113501&atid=665349)
 * currently the default behaviour of the parser is still as before
 *
 * Revision 1.9  2006/03/29 08:51:40  vassil_momtchev
 * mediator reference source/target by IRI (changed from TopEntity); mediator reference mediationService by IRI (changed from Identifier); new checks to not allow anonymous id added
 *
 * Revision 1.8  2006/03/07 13:49:41  alex_simov
 * source/target possible types corrected according to D.29.02
 *
 * Revision 1.7  2006/03/07 13:25:46  vassil_momtchev
 * parser probes all allowed top entity types for a mediator source/target
 *
 * Revision 1.6  2006/02/13 09:48:52  vassil_momtchev
 * the code to handle the topentities identifier validity refactored
 *
 * Revision 1.5  2005/12/07 14:46:10  vassil_momtchev
 * change method signature outAUserService -> outAUseService (spell error)
 *
 * Revision 1.4  2005/12/07 14:14:25  vassil_momtchev
 * bugfix; multiple mediator sources are not ignored
 *
 * Revision 1.3  2005/12/06 15:51:39  alex_simov
 * System.out.println's removed
 *
 * Revision 1.2  2005/12/02 14:33:44  holgerlausen
 * fixed parsing bug due to type when overwriting method, fixed but related to multiple source of mediators during reparsing
 *
 * Revision 1.1  2005/11/28 13:55:26  vassil_momtchev
 * AST analyses
 *
*/
