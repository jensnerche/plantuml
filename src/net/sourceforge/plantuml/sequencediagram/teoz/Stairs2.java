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
 * Revision $Revision: 6054 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.graphic.HtmlColor;

public class Stairs2 {

	private final List<StairsPosition> ys = new ArrayList<StairsPosition>();
	private final List<IntegerColored> values = new ArrayList<IntegerColored>();
	private final Map<Double, IntegerColored> cache = new HashMap<Double, IntegerColored>();

	public void addStep(StairsPosition position, int value, HtmlColor color) {
		// System.err.println("Stairs2::addStep " + position + " " + value + " color=" + color);
		assert ys.size() == values.size();
		if (ys.size() > 0) {
			final double lastY = ys.get(ys.size() - 1).getValue();
			if (position.getValue() < lastY) {
				throw new IllegalArgumentException();
			}
			if (lastY == position.getValue()) {
				values.set(ys.size() - 1, new IntegerColored(value, color));
				cache.clear();
				return;
			}
		}
		ys.add(position);
		values.add(new IntegerColored(value, color));
		cache.clear();
	}

	public int getMaxValue() {
		int max = Integer.MIN_VALUE;
		for (IntegerColored vc : values) {
			final int v = vc.getValue();
			if (v > max) {
				max = v;
			}
		}
		return max;
	}

	public List<StairsPosition> getYs() {
		return Collections.unmodifiableList(ys);
	}

	public IntegerColored getValue(double y) {
		IntegerColored resultc = cache.get(y);
		if (resultc == null) {
			resultc = getValueSlow(new StairsPosition(y, false));
			cache.put(y, resultc);
		}
		return resultc;
	}

	private IntegerColored getValueSlow(StairsPosition y) {
		final int idx = Collections.binarySearch(ys, y);
		if (idx >= 0) {
			return values.get(idx);
		}
		final int insertPoint = -idx - 1;
		if (insertPoint == 0) {
			return new IntegerColored(0, null);
		}
		return values.get(insertPoint - 1);
	}

	public int getLastValue() {
		final int size = values.size();
		if (size == 0) {
			return 0;
		}
		return values.get(size - 1).getValue();
	}

}
