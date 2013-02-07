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

package com.ontotext.wsmo4j.serializer.wsml;

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 * @author not attributable
 * @version 1.0
 *
 */
import org.wsmo.common.Entity;
import java.lang.ref.WeakReference;
import java.lang.reflect.*;


public class WSMLItem extends WeakReference implements Item2Visit {
    public WSMLItem(Entity o) { super(o); }

    public void apply(Visitor visitor) {
        Class[] oC = super.get().getClass().getInterfaces();
        try {
            for (int i = 0; i < oC.length; i++) {
                Method m = null;
                try {
                    m = visitor.getClass().getMethod("visit", new Class[]{oC[i]});
                }
                catch (NoSuchMethodException nse) {
                    continue;
                }
                if (m != null) {
                    m.invoke(visitor, new Object[]{get()});
                }
                break;
            }
        }
        catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            if (t != null)
                throw new RuntimeException(t.getMessage(), t);
            throw new RuntimeException(e);
        }
        catch (Exception e) {
            throw new RuntimeException("in apply", e);
        }

    }
}

/*
 * $Log: WSMLItem.java,v $
 * Revision 1.3  2006/03/24 15:31:19  vassil_momtchev
 * message friendly handling of invocation exceptions added
 *
 * Revision 1.2  2005/11/28 14:51:37  vassil_momtchev
 * moved from com.ontotext.wsmo4j.parser
 *
 * Revision 1.1.2.1  2005/11/28 13:59:35  vassil_momtchev
 * package refactored from com.ontotext.wsmo4j.parser to com.ontotext.wsmo4j.serializer.wsml
 *
 * Revision 1.1  2005/06/27 08:32:00  alex_simov
 * refactoring: *.io.parser -> *.parser
 *
 * Revision 1.4  2005/06/22 14:49:27  alex_simov
 * Copyright header added/updated
 *
 * Revision 1.3  2005/06/02 14:19:19  alex_simov
 * v0.4.0
 *
 * Revision 1.2  2005/06/02 12:33:09  damian
 * fixes
 *
 * Revision 1.1  2005/05/26 09:36:03  damian
 * io package
 *
 * Revision 1.2  2005/01/12 15:20:18  alex_simov
 * checkstyle formatting
 *
 */
