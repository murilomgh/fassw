/*
 wsmo4j extension - a Choreography API and Reference Implementation

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

import java.io.*;
import java.util.*;

import org.omwg.logicalexpression.terms.*;
import org.wsmo.common.*;
import org.wsmo.service.*;
import org.wsmo.service.choreography.Choreography;
import org.wsmo.service.choreography.rule.*;
import org.wsmo.service.rule.*;
import org.wsmo.service.signature.*;

import com.ontotext.wsmo4j.serializer.wsml.*;

/**
 * Choreography Serializer Class
 *
 * <pre>
 *    Created on Sep 18, 2005
 *    Committed by $Author: morcen $
 *    $Source: /cvsroot/wsmo4j/ext/choreography/src/ri/org/deri/wsmo4j/io/serializer/wsml/ChoreographySerializerWSML.java,v $
 * </pre>
 *
 * @author James Scicluna
 * @author Thomas Haselwanter
 *
 * @version $Revision: 1.22 $ $Date: 2007/04/02 13:05:18 $
 */
public class ChoreographySerializerWSML extends WSMLTextExportHelper {

    private VisitorSerializeWSMLTransitionRules ruleVisitor;

    public ChoreographySerializerWSML(Writer writer, TopEntity topEntity) {
        super(writer);
        if (writer == null || topEntity == null) {
            throw new IllegalArgumentException();
        }
        this.writer = writer;
        super.visitorSerializeTerm = new VisitorSerializeWSMLTerms(topEntity);
        this.ruleVisitor = new VisitorSerializeWSMLTransitionRules(topEntity);
    }

    public void visit(Interface item) {
        inInterface(item);
        visit((TopEntity)item);
        if (item.getChoreography() != null) {
            if (item.getChoreography() instanceof org.wsmo.service.choreography.Choreography) {
                helpChoreography((org.wsmo.service.choreography.Choreography)
                        item.getChoreography());
            }
        }
        if (item.getOrchestration() != null) {
            new WSMLItem(item.getOrchestration()).apply(this);
        }
        outInterface(item);
    }

    /*
     * Serializes the Choreography Object
     */
    public void helpChoreography(Choreography c) {
        writeln("").write(indentation).write("choreography ");
        write(helpSerializeTerm(c.getIdentifier()));
        //Non-Functional Properties
        visit((Entity) c);
        //State Signature
        if (c.getStateSignature() != null)
            helpStateSignature(c.getStateSignature());
        //Transition Rules
        if (c.getRules() != null)
            helpTransitionRules(c);
    }

    /*
     * Serliazises the StateSignature object
     */
    private void helpStateSignature(StateSignature sig){
        //Identifier
        writeln("").write(indentation).write("stateSignature ");
        write(helpSerializeTerm(sig.getIdentifier()));

        //Non-Functional Properties
        visit(sig);

        //Imported Ontologies (State)
        if (!sig.listOntologies().isEmpty()) {
            writeln("").write(indentation).write(indentation).write("importsOntology");
            onImportedOntologies(sig.listOntologies());
            writeln("");
        }

        //In
        if (sig.listInModes().size() > 0) {
            helpGroundedMode(sig.listInModes(),"in");
        }

        //Out
        if (sig.listOutModes().size() > 0) {
            helpGroundedMode(sig.listOutModes(),"out");
        }

        //Shared
        if (sig.listSharedModes().size() > 0) {
            helpGroundedMode(sig.listSharedModes(),"shared");
        }

        //Controlled
        if (sig.listControlledModes().size() > 0)
            helpUnGroundedMode(sig.listControlledModes(),"controlled");

        //Static
        if (sig.listStaticModes().size() > 0) {
            helpUnGroundedMode(sig.listStaticModes(), "static");
        }
    }

    /*
     * Serializes the Grounded Modes
     *
     * TODO: Exception throwing checking Not-Grounded Modes
     */
    private void helpGroundedMode(Set modes, String mName) {
        writeln("").write(indentation).write(indentation);
        write(mName).write(" ");
        if (modes != null) {
            if (modes.size() > 0) {
                Iterator i = modes.iterator();
                GroundedMode m;
                while (i.hasNext()) {
                    m = (GroundedMode) i.next();
                    if(m.isConcept()) {
                        writeln("").write(indentation).write(indentation);
                        write(indentation + "concept ");
                        write(helpSerializeTerm(m.getConcept().getIdentifier()));
                    }
                    else {
                        writeln("").write(indentation).write(indentation);
                        write(indentation + "relation ");
                        write(helpSerializeTerm(m.getRelation().getIdentifier()));
                    }
                    try {
                        helpGroundingList(m.getGrounding());
                    }
                    catch (NotGroundedException ex) {
                    }
                    
                    if (i.hasNext())
                        write(", ");
                }
            }
        }
        writeln("");
    }

    /*
     * Serializes the Grounding List
     */
    private void helpGroundingList(Set<Grounding> grounding) {
        if (grounding != null) {
            if (grounding.size() > 0) {
                write(" withGrounding ");
                if (grounding.size() > 1)
                    write("{");
                Iterator<Grounding> i = grounding.iterator();
                while (i.hasNext()) {
                    helpGroundingEntry(i.next());
                    if (i.hasNext())
                        write(",");
                }
                if (grounding.size() > 1)
                    write("}");
            }
        }
    }

    /*
     * Checks the different types of grounding and serializes accordingly
     */
    private void helpGroundingEntry(Grounding g) {
        if (g instanceof WSDLGrounding) {
            WSDLGrounding wsdlG = (WSDLGrounding) g;
            write(helpSerializeTerm(wsdlG.getIRI()));
        }
    }

    /*
     * Returns a string representation of the concept identifiers of the
     * Ungrounded Modes Static and Controlled
     */
    private void helpUnGroundedMode(Set modes, String mName) {
        writeln("").write(indentation).write(indentation);
        write(mName).write(" ");
        if (modes != null) {
            Iterator i = modes.iterator();
            while (i.hasNext()) {
                Mode m = (Mode) i.next();
                if(m.isConcept()) {
                    writeln("").write(indentation).write(indentation);
                    write(indentation + "concept ");
                    write(helpSerializeTerm(m.getConcept().getIdentifier()));
                }else {
                    writeln("").write(indentation).write(indentation);
                    write(indentation + "relation ");
                    write(helpSerializeTerm(m.getRelation().getIdentifier()));
                }
                if (i.hasNext())
                    write(",");
            }
        }
        write(" ");
    }

    /*
     * Serializes the transition rules
     */
    private void helpTransitionRules(Choreography c){
        ChoreographyRules rules = c.getRules();
        if (rules == null) {
            return;
        }
        writeln("").write(indentation).write("transitionRules ").writeln(helpSerializeTerm(rules.getIdentifier()));
        if (rules.listRules().size() == 0) {
            return;
        }
        
        Iterator <ChoreographyRule> i = rules.listRules().iterator();
        ChoreographyRule r = null;
        while(i.hasNext()){
            r = i.next();
            if(r instanceof ChoreographyIfThen) this.ruleVisitor.visitChoreographyIfThen((ChoreographyIfThen)r);
            else if(r instanceof ChoreographyForAll) this.ruleVisitor.visitChoreographyForAll((ChoreographyForAll)r);
            else if(r instanceof ChoreographyChoose) this.ruleVisitor.visitChoreographyChoose((ChoreographyChoose)r);
            else if(r instanceof Add) this.ruleVisitor.visitAdd((Add)r);
            else if(r instanceof Delete) this.ruleVisitor.visitDelete((Delete)r);
            else if(r instanceof Update) this.ruleVisitor.visitUpdate((Update)r);
            writeln(this.ruleVisitor.getSerializedObject());
        }
    }
    
    protected String helpSerializeTerm(Term term){
        term.accept(visitorSerializeTerm);
        return visitorSerializeTerm.getSerializedObject();
    }
    
    protected ChoreographySerializerWSML write(String str) {
        try {
            writer.write(str);
        }
        catch (IOException ioe) {
            throw new RuntimeException("while writing", ioe);
        }
        return this;
    }
    
    protected ChoreographySerializerWSML writeln(String str) {
        return write(str).write("\n");
    }
    
    protected final String indentation = "     ";
}

/*
 * $Log: ChoreographySerializerWSML.java,v $
 * Revision 1.22  2007/04/02 13:05:18  morcen
 * Generics support added to wsmo-ext
 *
 * Revision 1.21  2006/10/24 14:11:47  vassil_momtchev
 * choreography/orchestration rules refactored. different types where appropriate now supported
 *
 * Revision 1.20  2006/04/17 10:52:11  vassil_momtchev
 * serialization of Choreography getRules() == null or getStateSignature == null is now allowed (skipped in wsml)
 *
 * Revision 1.19  2006/03/01 15:22:15  vassil_momtchev
 * all modes can be serialized with no grounding information
 *
 * Revision 1.18  2006/02/21 09:05:02  vassil_momtchev
 * serialization formatting improved
 *
 * Revision 1.17  2006/02/14 15:25:15  alex_simov
 * output text indentation improved for StateSignature modes
 *
 * Revision 1.16  2006/02/14 10:59:04  vassil_momtchev
 * choreography that do not define rules container or rules is allowed to be serialized
 *
 * Revision 1.15  2006/02/03 13:32:49  jamsci001
 * - Serializer reflects changes in API and implementation:
 *  - PipedRules
 *  - Relation Facts
 *  - Updates
 *
 * Revision 1.14  2006/02/01 15:26:25  vassil_momtchev
 * dublicated fields with the super class removed
 *
 * Revision 1.13  2006/02/01 14:43:44  vassil_momtchev
 * extend wsmo4j parser; do not implement org.wsmo.service.choreography.io.Serializer anymore see SerializerImpl; repeated code removed; all nfps/importOntologies are serialized; all string concatinations replaced with writer
 *
 * Revision 1.12  2006/01/31 10:51:42  vassil_momtchev
 * unused fields removed; log footer added
 *
*/
