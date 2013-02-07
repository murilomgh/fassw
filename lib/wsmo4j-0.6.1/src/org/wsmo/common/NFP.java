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

package org.wsmo.common;


/**
 * This class is a placeholder for the core Dublin Core non-functional property keys
 *
 * @author not attributable
 * @version $Revision: 1.10 $ $Date: 2006/11/16 09:36:28 $
 */

public final class NFP {

    public static final String DC_BASE = "http://purl.org/dc/elements/1.1#";

    public static final String WSML_BASE = WSML.WSML_NAMESPACE;

    public static final String DC_TITLE = DC_BASE + "title";

    public static final String DC_CREATOR = DC_BASE + "creator";

    public static final String DC_SUBJECT = DC_BASE + "subject";

    public static final String DC_DESCRIPTION = DC_BASE + "description";

    public static final String DC_PUBLISHER = DC_BASE + "publisher";

    public static final String DC_CONTRIBUTOR = DC_BASE + "contributor";

    public static final String DC_DATE = DC_BASE + "date";

    public static final String DC_TYPE = DC_BASE + "type";

    public static final String DC_FORMAT = DC_BASE + "format";

    public static final String DC_IDENTIFIER = DC_BASE + "identifier";

    public static final String DC_SOURCE = DC_BASE + "source";

    public static final String DC_LANGUAGE = DC_BASE + "language";

    public static final String DC_RELATION = DC_BASE + "relation";

    public static final String DC_COVERAGE = DC_BASE + "coverage";

    public static final String DC_RIGHTS = DC_BASE + "rights";

    public static final String VERSION = WSML_BASE + "version";
}

/*
 * $Log: NFP.java,v $
 * Revision 1.10  2006/11/16 09:36:28  holgerlausen
 * removed duplicated namespace definition occurences
 *
 * Revision 1.9  2006/01/13 10:22:29  vassil_momtchev
 * changed from interface to final class
 *
 * Revision 1.8  2005/06/22 14:40:48  alex_simov
 * Copyright header added/updated
 *
 * Revision 1.7  2005/06/01 10:30:48  marin_dimitrov
 * v0.4.0
 *
 * Revision 1.4  2005/05/17 12:49:30  marin
 * 1. added WSML_BASE
 * 2. fixed "version"
 *
 * Revision 1.3  2005/05/12 13:29:16  marin
 * javadoc, header, footer, etc
 *
 */
