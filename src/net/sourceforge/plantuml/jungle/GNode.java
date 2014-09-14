/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * Revision $Revision: 8475 $
 *
 */
package net.sourceforge.plantuml.jungle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.cucadiagram.Display;

public class GNode {

	private final String id;
	private final Display display;
	private final List<GNode> children = new ArrayList<GNode>();

	public GNode(String id, Display display) {
		this.id = id;
		this.display = display;
	}

	public String getId() {
		return id;
	}

	public Display getDisplay() {
		return display;
	}

	public List<GNode> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public GNode addChild(String id, Display display) {
		final GNode child = new GNode(id, display);
		children.add(child);
		return child;
	}

	public GNode getDirectChild(String id) {
		for (final GNode node : children) {
			if (node.id.equals(id)) {
				return node;
			}
		}
		return null;
	}
}