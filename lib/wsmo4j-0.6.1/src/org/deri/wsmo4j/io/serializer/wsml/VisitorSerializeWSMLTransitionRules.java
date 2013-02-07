/*
 wsmo4j extension - a Choreography API and Reference Implementation

 Copyright (c) 2005, University of Innsbruck, Austria
 2006, Ontotext Lab. / SIRMA Group

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

import java.util.*;

import org.omwg.logicalexpression.*;
import org.omwg.logicalexpression.terms.*;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.service.choreography.rule.*;
import org.wsmo.service.orchestration.rule.*;
import org.wsmo.service.rule.*;
import org.wsmo.service.rule.Visitor;

import com.ontotext.wsmo4j.parser.*;

/**
 * Visitor Implementation Class for the Choreography Rules
 *
 * TODO: - FactMolecule - Update Rules
 *
 * <pre>
 *    Created on Sep 29, 2005
 *    Committed by $Author: morcen $
 *    $Source: /cvsroot/wsmo4j/ext/choreography/src/ri/org/deri/wsmo4j/io/serializer/wsml/VisitorSerializeWSMLTransitionRules.java,v $
 * </pre>
 *
 * @author James Scicluna
 * @author Thomas Haselwanter
 * @author Vassil Momtchev
 *
 * @version $Revision: 1.17 $ $Date: 2007/04/02 13:05:18 $
 */
public class VisitorSerializeWSMLTransitionRules implements Visitor {
    private static final boolean USE_PREFFERED = true;

    private VisitorSerializeWSML logExpSer;

    private VisitorSerializeWSMLTerms termVisitor;

    private Stack<String> stack;

    private int indentationLevel = 2;

    /**
     * Constructor for the Rules visitor.
     *
     * @param nsContainer
     *            A TopEntity object containing the namespace definitions
     */
    public VisitorSerializeWSMLTransitionRules(TopEntity nsContainer) {
        this.logExpSer = new VisitorSerializeWSML(nsContainer);
        this.termVisitor = new VisitorSerializeWSMLTerms(nsContainer);
        this.stack = new Stack<String>();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.wsmo.service.choreography.rule.Visitor#visitIfThen(org.wsmo.service.choreography.rule.IfThen)
     */
    public void visitChoreographyIfThen(ChoreographyIfThen rule) {
        String sCond = this.helpCondition(rule.getCondition());
        String sRules = this.helpNestedRules(rule.listNestedRules());
        this.stack.push(getIndent() + "if " + sCond + " then\n" + sRules + getIndent() + "endIf\n");
    }
    
    public void visitOrchestrationIfThen(OrchestrationIfThen rule) {
        String sCond = this.helpCondition(rule.getCondition());
        String sRules = this.helpNestedRules(rule.listNestedRules());
        this.stack.push(getIndent() + "if " + sCond + " then\n" + sRules + getIndent() + "endIf\n");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.wsmo.service.choreography.rule.Visitor#visitForAll(org.wsmo.service.choreography.rule.ForAll)
     */
    public void visitChoreographyForAll(ChoreographyForAll rule) {
        String sVars = this.helpVariableList(rule.listVariables());
        String sCond = this.helpCondition(rule.getCondition());
        String sRules = this.helpNestedRules(rule.listNestedRules());
        this.stack.push(getIndent() + "forall " + sVars + " with " + sCond + " do \n" + sRules
                + getIndent() + "endForall\n");
    }
    
    public void visitOrchestrationForAll(OrchestrationForAll rule) {
        String sVars = this.helpVariableList(rule.listVariables());
        String sCond = this.helpCondition(rule.getCondition());
        String sRules = this.helpNestedRules(rule.listNestedRules());
        this.stack.push(getIndent() + "forall " + sVars + " with " + sCond + " do \n" + sRules
                + getIndent() + "endForall\n");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.wsmo.service.choreography.rule.Visitor#visitChoose(org.wsmo.service.choreography.rule.Choose)
     */
    public void visitChoreographyChoose(ChoreographyChoose rule) {
        String sVars = this.helpVariableList(rule.listVariables());
        String sCond = this.helpCondition(rule.getCondition());
        String sRules = this.helpNestedRules(rule.listNestedRules());
        this.stack.push(getIndent() + "choose " + sVars + " with " + sCond + " do\n" + sRules
                + getIndent() + "endChoose\n");
    }

    public void visitOrchestrationChoose(OrchestrationChoose rule) {
        String sVars = this.helpVariableList(rule.listVariables());
        String sCond = this.helpCondition(rule.getCondition());
        String sRules = this.helpNestedRules(rule.listNestedRules());
        this.stack.push(getIndent() + "choose " + sVars + " with " + sCond + " do\n" + sRules
                + getIndent() + "endChoose\n");
    }
    
    /*
     * (non-Javadoc)
     *
     * @see org.wsmo.service.choreography.rule.Visitor#visitAdd(org.wsmo.service.choreography.rule.Add)
     */
    public void visitAdd(Add rule) {
        this.stack.push(getIndent() + "add(" + this.helpCompoundFact(rule.getFact(), null) + ")\n");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.wsmo.service.choreography.rule.Visitor#visitDelete(org.wsmo.service.choreography.rule.Delete)
     */
    public void visitDelete(Delete rule) {
        this.stack.push(getIndent() + "delete(" + this.helpCompoundFact(rule.getFact(), null)
                + ")\n");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.wsmo.service.choreography.rule.Visitor#visitUpdate(org.wsmo.service.choreography.rule.Update)
     */
    public void visitUpdate(Update rule) {
        this.stack.push(getIndent() + "update("
                + this.helpCompoundFact(rule.getNewFact(), rule.getOldFact()) + ")\n");
    }
    
    public void visitPerform(Perform rule) {
        
    }

    /*
     * (non-Javadoc)
     *
     * @see org.wsmo.service.choreography.rule.Visitor#getSerializedObject()
     */
    public String getSerializedObject() {
        return this.stack.pop();
    }

    private String helpCondition(Condition expr) {
        expr.getRestrictedLogicalExpression().accept(this.logExpSer);
        return "(" + this.logExpSer.getSerializedObject() + ")";
    }

    private String helpNestedRules(Set<? extends Rule> rules) {
        String s = "";
        indentationLevel++;
        Iterator<? extends Rule> i = rules.iterator();
        while (i.hasNext()) {
            Rule r = i.next();
            r.accept(this);
            s += this.getSerializedObject();
        }
        indentationLevel--;
        return s;
    }

    private String helpVariableList(Set<Variable> vars) {
        String s = "{";
        Iterator<Variable> i = vars.iterator();
        while (i.hasNext()) {
            Variable var = i.next();
            var.accept(this.termVisitor);
            s += this.termVisitor.getSerializedObject();
            if (i.hasNext())
                s += ",";
        }
        return s + "}";
    }

    public String helpCompoundFact(CompoundFact newFact, CompoundFact oldFact) {
        if (newFact == null) {
            throw new IllegalArgumentException();
        }
        StringBuffer buffer = new StringBuffer();
        if (newFact instanceof MoleculeFact) {//deal with normal molecule facts
            MoleculeFact newMolFact = (MoleculeFact) newFact;
            MoleculeFact oldMolFact = null;
            if (oldFact != null)
                if (oldFact instanceof MoleculeFact) {
                    oldMolFact = (MoleculeFact) oldFact;
                }
                else {
                    throw new WrappedInvalidModelException(new InvalidModelException(
                            "Both old and new Compound Facts must be of the same type "));
                }
            if (newMolFact.isEmpty()) {
                throw new WrappedInvalidModelException(new InvalidModelException(
                        "CompoundFact do not contains neither AttributeValueMolecule "
                                + "nor MembershipMolecule!"));
            }
            Term instanceId = null;
            if (!newMolFact.listAttributeValueMolecules().isEmpty()) {
                instanceId = newMolFact.listAttributeValueMolecules().iterator().next()
                        .getLeftParameter();
            }
            else {
                instanceId = newMolFact.listMembershipMolecules().iterator().next()
                        .getLeftParameter();
            }
            buffer.append(helpSerializeTerm(instanceId));

            if (USE_PREFFERED) {
                buffer.append(helpAttributeValues(newMolFact.listAttributeValueMolecules(),
                        oldMolFact == null ? new HashSet<AttributeValueMolecule>() : oldMolFact
                                .listAttributeValueMolecules()));
                buffer.append(helpMembershipMolecules(newMolFact.listMembershipMolecules(),
                        oldMolFact == null ? new HashSet<MembershipMolecule>() : oldMolFact
                                .listMembershipMolecules()));
            }
            else {
                buffer.append(helpMembershipMolecules(newMolFact.listMembershipMolecules(),
                        oldMolFact == null ? new HashSet<MembershipMolecule>() : oldMolFact
                                .listMembershipMolecules()));
                buffer.append(helpAttributeValues(newMolFact.listAttributeValueMolecules(),
                        oldMolFact == null ? new HashSet<AttributeValueMolecule>() : oldMolFact
                                .listAttributeValueMolecules()));
            }

        }
        else if (newFact instanceof RelationFact) {//deal with relation facts
            if (oldFact != null) {
                buffer
                        .append(this.helpRelationFact((RelationFact) oldFact,
                                (RelationFact) newFact));
            }
            else {
                buffer.append(this.helpRelationFact(null, (RelationFact) newFact));
            }
        }
        return new String(buffer);
    }

    private String helpAttributeValues(Set<AttributeValueMolecule> newVals,
            Set<AttributeValueMolecule> oldVals) {
        Set<Term> attributes = validateAttributeValues(newVals, oldVals);
        if (attributes.size() == 0)
            return "";

        StringBuffer buffer = new StringBuffer(" [");
        for (Iterator i = attributes.iterator(); i.hasNext();) {
            Term attr = (Term) i.next();
            buffer.append(helpSerializeTerm(attr));
            buffer.append(" hasValue ");

            Set<Term> replacedValues = new HashSet<Term>();
            for (AttributeValueMolecule val : oldVals) {
                if (val.getAttribute().equals(attr))
                    replacedValues.add(val.getRightParameter());
            }
            Set<Term> replacingValues = new HashSet<Term>();
            for (AttributeValueMolecule val : newVals) {
                if (val.getAttribute().equals(attr))
                    replacingValues.add(val.getRightParameter());
            }

            if (!replacedValues.isEmpty()) {
                buffer.append(helpTermList(replacedValues));
                buffer.append(" => ");
            }
            buffer.append(helpTermList(replacingValues));
            if (i.hasNext()) {
                buffer.append(", ");
            }
        }

        buffer.append("]");
        return new String(buffer);
    }

    private String helpMembershipMolecules(Set<MembershipMolecule> newVals,
            Set<MembershipMolecule> oldVals) {
        if (newVals.isEmpty()) {
            return "";
        }
        StringBuffer buffer = new StringBuffer(" memberOf ");
        Term instance = newVals.iterator().next().getLeftParameter();

        Set<Term> replacedValues = new HashSet<Term>();
        for (MembershipMolecule val : oldVals) {
            if (!val.getLeftParameter().equals(val.getLeftParameter())) {
                String msg = String.format("All AttributeValueMolecule of a CompoundFact "
                        + "hash to use the same left parameter. Expected %s but %s was found!",
                        new Object[] { instance, val.getLeftParameter() });
                throw new WrappedInvalidModelException(new InvalidModelException(msg));
            }
            replacedValues.add(val.getRightParameter());
        }
        Set<Term> replacingValues = new HashSet<Term>();
        for (MembershipMolecule val : newVals) {
            if (!val.getLeftParameter().equals(val.getLeftParameter())) {
                String msg = String.format("All AttributeValueMolecule of a CompoundFact "
                        + "hash to use the same left parameter. Expected %s but %s was found!",
                        new Object[] { instance, val.getLeftParameter() });
                throw new WrappedInvalidModelException(new InvalidModelException(msg));
            }
            replacingValues.add(val.getRightParameter());
        }

        if (!replacedValues.isEmpty()) {
            buffer.append(helpTermList(replacedValues));
            buffer.append(" => ");
        }
        buffer.append(helpTermList(replacingValues));
        return new String(buffer);
    }

    private Set<Term> validateAttributeValues(Set<AttributeValueMolecule> newVals,
            Set<AttributeValueMolecule> oldVals) {
        if (newVals.isEmpty()) {
            return new HashSet<Term>();
        }

        Term instance = newVals.iterator().next().getLeftParameter();
        HashSet<Term> attributes = new HashSet<Term>();

        for (AttributeValueMolecule val : newVals) {
            if (!val.getLeftParameter().equals(instance)) {
                String msg = String.format("All AttributeValueMolecule of a CompoundFact "
                        + "hash to use the same left parameter. Expected %s but %s was found!",
                        new Object[] { instance, val.getLeftParameter() });
                throw new WrappedInvalidModelException(new InvalidModelException(msg));
            }
            attributes.add(val.getAttribute());
        }
        for (AttributeValueMolecule val : oldVals) {
            if (!val.getLeftParameter().equals(instance)) {
                String msg = String.format("All AttributeValueMolecule of a CompoundFact "
                        + "hash to use the same left parameter. Expected %s but %s was found!",
                        new Object[] { instance, val.getLeftParameter() });
                throw new WrappedInvalidModelException(new InvalidModelException(msg));
            }
            if (!attributes.contains(val.getAttribute())) {
                String msg = String.format("The AttributeValueMolecule ? [%s hasValue ?] "
                        + "has no new valid to replace it! Specify AttributeValueMolecule "
                        + "in new CompoundFact ? [%s hasValue <newvalue>].", new Object[] {
                        val.getAttribute(), val.getAttribute() });
                throw new WrappedInvalidModelException(new InvalidModelException(msg));
            }
        }
        return attributes;
    }

    private String helpSerializeTerm(Term term) {
        term.accept(this.termVisitor);
        return this.termVisitor.getSerializedObject();
    }

    private String helpTermList(Set<Term> terms) {
        if (terms.size() == 1) {
            return new String(helpSerializeTerm(terms.iterator().next()));
        }
        else if (terms.size() > 1) {
            StringBuffer buffer = new StringBuffer("{");
            for (Iterator i = terms.iterator(); i.hasNext();) {
                buffer.append(helpSerializeTerm((Term) i.next()));
                if (i.hasNext()) {
                    buffer.append(", ");
                }
            }
            buffer.append("}");
            return new String(buffer);
        }
        else
            throw new RuntimeException("Internal exception during serialization");
    }

    private String helpRelationFact(RelationFact oldFact, RelationFact newFact) {
        StringBuffer buffer = new StringBuffer();
        if (oldFact != null) {//deal with value updates
            if (validIfSerializeableUpdateRelationFact(oldFact, newFact)) {
                assert newFact.getAtom().listParameters().size() > 0;
                assert oldFact.getAtom().listParameters().size() == 
                    newFact.getAtom().listParameters().size();
                
                buffer.append("@" + helpSerializeTerm(newFact.getAtom().getIdentifier())+ "(");
                Iterator i1 = oldFact.getAtom().listParameters().iterator();
                Iterator i2 = newFact.getAtom().listParameters().iterator();
                
                while (i1.hasNext()) {
                    buffer.append(helpRelationFactTermUpdate((Term) i1.next(), (Term) i2.next()));
                    if (i1.hasNext())
                        buffer.append(",");
                }
                buffer.append(")");
            }
        }
        else {//deal with a normal relation fact
            buffer.append("@");
            this.logExpSer.visitAtom(newFact.getAtom());
            buffer.append(this.logExpSer.getSerializedObject());
        }
        return buffer.toString();
    }

    private String helpRelationFactTermUpdate(Term oldTerm, Term newTerm) {
        String termUpdate = "";
        if (oldTerm.equals(newTerm)) {
            termUpdate = this.helpSerializeTerm(newTerm);
        }
        else {
            termUpdate += this.helpSerializeTerm(oldTerm) + " => "
                    + this.helpSerializeTerm(newTerm);
        }
        return termUpdate;
    }

    private boolean validIfSerializeableUpdateRelationFact(RelationFact oldFact, RelationFact newFact) {
        try {
            if (oldFact.getAtom().listParameters().size() !=
                newFact.getAtom().listParameters().size()) {
                throw new WrappedInvalidModelException(new InvalidModelException(
                        "The Atoms of the new and old RelationFact do not have the same" +
                        "size parameter lists!"));
            }
        }
        catch (NullPointerException ex) {
            throw new WrappedInvalidModelException(new InvalidModelException(
            "The Atom of the RelationFact is null!"));
        }
        if (oldFact != null && !oldFact.getAtom().getIdentifier().equals(
                newFact.getAtom().getIdentifier())) {
            throw new WrappedInvalidModelException(new InvalidModelException(
                    "The Identifier names of the relation updates are different"));
        }
        return true;
    }

    public void visitChoreographyPipedRules(ChoreographyPipedRules rules) {
        this.stack.push(this.helpPipedRules(rules));
    }
    
    public void visitOrchestrationPipedRules(OrchestrationPipedRules rules) {
        this.stack.push(this.helpPipedRules(rules));
    }

    private String helpPipedRules(ChoreographyPipedRules rules) {
        StringBuffer buffer = new StringBuffer();
        Iterator i = rules.listPipedRules().iterator();
        ChoreographyRule r;
        while (i.hasNext()) {
            r = (ChoreographyRule) i.next();
            r.accept(this);
            buffer.append(this.getSerializedObject());
            if (i.hasNext())
                buffer.append(" | ");
            else
                buffer.append("\n");
        }
        return buffer.toString();
    }
    
    private String helpPipedRules(OrchestrationPipedRules rules) {
        StringBuffer buffer = new StringBuffer();
        Iterator i = rules.listPipedRules().iterator();
        Rule r;
        while (i.hasNext()) {
            r = (Rule) i.next();
            r.accept(this);
            buffer.append(this.getSerializedObject());
            if (i.hasNext())
                buffer.append(" | ");
            else
                buffer.append("\n");
        }
        return buffer.toString();
    }

    private String getIndent() {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < indentationLevel; i++) {
            buff.append("     ");
        }
        return buff.toString();
    }

	public void visitOrchestrationAchieveGoal(OrchestrationAchieveGoal rule) {
		String s = getIndent() + "perform ";
		if (rule.getPerformIRI() != null)
			s = s + rule.getPerformIRI() + " ";
		s = s + " achieveGoal " + rule.getGoal().getIdentifier() + "\n";
        this.stack.push(s);
	}

	public void visitOrchestrationApplyMediation(OrchestrationApplyMediation rule) {
		String s = getIndent() + "perform ";
		if (rule.getPerformIRI() != null)
			s = s + rule.getPerformIRI() + " ";
		s = s + " mediate " + rule.getPpMediator().getIdentifier() + "\n";
        this.stack.push(s);
	}

	public void visitOrchestrationInvokeService(OrchestrationInvokeService rule) {
		String s = getIndent() + "perform ";
		if (rule.getPerformIRI() != null)
			s = s + rule.getPerformIRI() + " ";
		s = s + " invokeService " + rule.getService().getIdentifier() + "\n";
        this.stack.push(s);
	}

	public void visitReceive(Receive rule) {
		String s = getIndent() + "perform ";
		if (rule.getPerformIRI() != null)
			s = s + rule.getPerformIRI() + " ";
		s = s + " receive source " + rule.getSourceIRI() + "\n";
        this.stack.push(s);
	}

	public void visitSend(Send rule) {
		String s = getIndent() + "perform ";
		if (rule.getPerformIRI() != null)
			s = s + rule.getPerformIRI() + " ";
		s = s + " send target " + rule.getTargetIRI() + "\n";
        this.stack.push(s);
	}
}

/*
 * $Log: VisitorSerializeWSMLTransitionRules.java,v $
 * Revision 1.17  2007/04/02 13:05:18  morcen
 * Generics support added to wsmo-ext
 *
 * Revision 1.16  2006/11/08 15:26:53  vassil_momtchev
 * support of ppMediator and orchestration perform rules added
 *
 * Revision 1.15  2006/11/03 13:44:19  haselwanter
 * Serialzer extension.
 *
 * Revision 1.14  2006/10/24 14:11:47  vassil_momtchev
 * choreography/orchestration rules refactored. different types where appropriate now supported
 *
 * Revision 1.13  2006/04/17 09:51:46  vassil_momtchev
 * condition.getLogicalExpression() method name changed to condition.getRestrictedLogicalExpression()
 *
 * Revision 1.12  2006/04/17 08:17:07  vassil_momtchev
 * bug fixed in serialization of RelationFact
 *
 * Revision 1.11  2006/03/24 15:37:29  vassil_momtchev
 * exception msg formatting problem fixed; wrong equals method used in validateAttributeValues method
 *
 * Revision 1.10  2006/03/13 12:40:39  alex_simov
 * identation for 'endIf' fixed
 *
 * Revision 1.9  2006/02/21 09:05:03  vassil_momtchev
 * serialization formatting improved
 *
 * Revision 1.8  2006/02/15 15:51:08  alex_simov
 * method helpCompoundFact() extra visibility added to be reused in MoleculeFact.toString()
 *
 * Revision 1.7  2006/02/08 15:30:15  jamsci001
 * - added "@" symbol for the serialization of relation fact
 *
 * Revision 1.6  2006/02/03 13:32:49  jamsci001
 * - Serializer reflects changes in API and implementation:
 *  - PipedRules
 *  - Relation Facts
 *  - Updates
 *
 * Revision 1.5  2006/01/31 10:35:06  vassil_momtchev
 * serialization of the fact modified according the new api; few bugs removed; log footer added
 *
 */
