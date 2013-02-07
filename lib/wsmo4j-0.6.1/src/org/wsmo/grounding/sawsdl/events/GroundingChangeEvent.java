/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2004-2007, Ontotext Lab. / SIRMA

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

package org.wsmo.grounding.sawsdl.events;

import javax.xml.namespace.QName;

/**
 * An event indicating an addition or removal of semantic model annotations. 
 *
 */
public class GroundingChangeEvent {

    public static final int ADD_ACTION = 1;
    public static final int REMOVE_ACTION = 2;
    
    public static final int MODEL_REF = 101;
    public static final int LOWERING_REF = 102;
    public static final int LIFTING_REF = 102;

    /**
     * @uml.property  name="action"
     */
    private int action = -1;
    /**
     * @uml.property  name="type"
     */
    private int type = -1;
    /**
     * @uml.property  name="wsdlName"
     */
    private QName wsdlName = null;
    /**
     * @uml.property  name="modelRef"
     */
    private Object modelRef = null;

    private GroundingChangeEvent() {
    }

    public GroundingChangeEvent(int action, int type, QName wsdlName, Object ref) {
        this.action = action;
        this.type = type;
        this.wsdlName = wsdlName;
        this.modelRef = ref;
    }

    /**
     * The type of the performed action (ADD_ACTION or REMOVE_ACTION).
     * @return  Returns the action type.
     */
    public int getAction() {
        return action;
    }


    /**
     * The type of the grounding element which has been added or removed 
     * (MODEL_REF, LOWERING_REF or LIFTING_REF).
     * @return  Returns the type of the grounding element
     */
    public int getType() {
        return type;
    }

    /**
     * The WSDL or XML Schema element to which the grounding element belongs.
     * @return  Returns the WSDL or XML Schema element name
     * @uml.property  name="wsdlName"
     */
    public QName getWsdlName() {
        return wsdlName;
    }

    /**
     * The target of grounding element which has been changed.
     * @return  Returns reference id (IRI) or schema reference (URI)
     * @uml.property  name="modelRef"
     */
    public Object getModelRef() {
        return modelRef;
    }

}

/*
 * $Log: GroundingChangeEvent.java,v $
 * Revision 1.2  2007/04/27 17:46:31  alex_simov
 * javadoc added
 *
 * Revision 1.1  2007/04/24 14:09:44  alex_simov
 * new SA-WSDL api
 *
 */
