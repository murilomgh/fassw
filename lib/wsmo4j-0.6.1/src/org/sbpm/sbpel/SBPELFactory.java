/*
	wsmo4j - a WSMO API and Reference Implementation

	Copyright (c) 2007, Ontotext Lab. / SIRMA

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

package org.sbpm.sbpel;

public interface SBPELFactory {
	public Initiate createInitiate();
	public Pattern createPattern();
	public EndpointReference createEndpointReference();
	public Process createProcess();
	public Extension createExtension();
	public Import createImport();
	public PartnerLink createPartnerLink();
	public Variable createVariable();
	public DataType createDataType();
	public MessageType createMessageType();
	public Type createType();
	public Element createElement();
	public CorrelationSet createCorrelationSet();
	public CompensationHandler createCompensationHandler();
	public FaultHandler createFaultHandler();
	public Catch createCatch();
	public CatchAll createCatchAll();
	public TerminationHandler createTerminationHandler();
	public OnEvent createOnEvent();
	public Event createEvent();
	public MessageEvent createMessageEvent();
	public OnMessage createOnMessage();
	public OnAlarm createOnAlarm();
	public RepeatableOnAlarm createRepeatableOnAlarm();
	public StandardAttributes createStandardAttributes();
	public Activity createActivity();
	public BasicActivity createBasicActivity();
	public StructuredActivity createStructuredActivity();
	public Source createSource();
	public Condition createCondition();
	public Interaction createInteraction();
	public WSDLInteraction createWSDLInteraction();
	public ExtensionActivity createExtensionActivity();
	public NewActivityType createNewActivityType();
	public CorrelationWithPattern createCorrelationWithPattern();
	public Correlation createCorrelation();
	public Validate createValidate();
	public Receive createReceive();
	public MessageExchange createMessageExchange();
	public Reply createReply();
	public Invoke createInvoke();
	public ToParts createToParts();
	public FromParts createFromParts();
	public Assign createAssign();
	public AssignOperation createAssignOperation();
	public ExtensionAssignOperation createExtensionAssignOperation();
	public Copy createCopy();
	public CopySpecification createCopySpecification();
	public CopyVariablePart createCopyVariablePart();
	public CopyPartnerLinkEndpointReference createCopyPartnerLinkEndpointReference();
	public CopyVariableProperty createCopyVariableProperty();
	public CopyExpression createCopyExpression();
	public CopyLiteral createCopyLiteral();
	public CopyPartnerLink createCopyPartnerLink();
	public Throw createThrow();
	public Wait createWait();
	public WaitStatement createWaitStatement();
	public For createFor();
	public Until createUntil();
	public Empty createEmpty();
	public Exit createExit();
	public Rethrow createRethrow();
	public Compensate createCompensate();
	public CompensateScope createCompensateScope();
	public Sequence createSequence();
	public OrderedActivity createOrderedActivity();
	public OrderedConditionalBranch createOrderedConditionalBranch();
	public If createIf();
	public ConditionalBranch createConditionalBranch();
	public Else createElse();
	public While createWhile();
	public RepeatUntil createRepeatUntil();
	public Pick createPick();
	public Flow createFlow();
	public Link createLink();
	public ForEach createForEach();
	public CompletionCondition createCompletionCondition();
	public CounterValue createCounterValue();
	public Scope createScope();
	public SemanticProcess createSemanticProcess();
	public SemanticScope createSemanticScope();
	public Partner createPartner();
	public SemanticVariable createSemanticVariable();
	public SemanticInvoke createSemanticInvoke();
	public SemanticReceive createSemanticReceive();
	public SemanticReply createSemanticReply();
	public SemanticPick createSemanticPick();
	public SemanticOnMessage createSemanticOnMessage();
	public Conversation createConversation();
	public InterfaceDescription createInterfaceDescription();
	public IncomingInterface createIncomingInterface();
	public OutgoingInterface createOutgoingInterface();
	public Mediation createMediation();
	public PartnerLinkType createPartnerLinkType();
	public Role createRole();
}
/*
 * $Log: SBPELFactory.java,v $
 * Revision 1.1  2007/04/18 16:27:09  lcekov
 * adding sbpel
 *
**/
