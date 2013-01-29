/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 9786 $
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import net.sourceforge.plantuml.StringUtils;

public enum LeafType {

	EMPTY_PACKAGE,

	ABSTRACT_CLASS, CLASS, INTERFACE, LOLLIPOP, ENUM, ACTOR, USECASE, COMPONENT, CIRCLE_INTERFACE, NOTE, OBJECT, ASSOCIATION,

	BOUNDARY, CONTROL, ENTITY_DOMAIN,

	COMPONENT2,

	ARC_CIRCLE,

	ACTIVITY, BRANCH, SYNCHRO_BAR, CIRCLE_START, CIRCLE_END, POINT_FOR_ASSOCIATION, ACTIVITY_CONCURRENT,

	STATE, STATE_CONCURRENT, PSEUDO_STATE,

	BLOCK,

	STILL_UNKNOWN;

	public static LeafType getLeafType(String arg0) {
		arg0 = arg0.toUpperCase();
		if (arg0.startsWith("ABSTRACT")) {
			return LeafType.ABSTRACT_CLASS;
		}
		return LeafType.valueOf(arg0);
	}

	public boolean isLikeClass() {
		return this == LeafType.ABSTRACT_CLASS || this == LeafType.CLASS || this == LeafType.INTERFACE
				|| this == LeafType.ENUM;
	}

	public String toHtml() {
		final String html = toString().replace('_', ' ').toLowerCase();
		return StringUtils.capitalize(html);
	}

	public boolean manageModifier() {
		if (this == ABSTRACT_CLASS || this == CLASS || this == INTERFACE || this == ENUM || this == OBJECT) {
			return true;
		}
		return false;
	}
}
