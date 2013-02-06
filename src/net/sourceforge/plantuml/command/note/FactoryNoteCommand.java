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
 * Revision $Revision: 7558 $
 *
 */
package net.sourceforge.plantuml.command.note;

import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;

public final class FactoryNoteCommand implements SingleMultiFactoryCommand<AbstractEntityDiagram> {

	private RegexConcat getRegexConcatMultiLine() {
		return new RegexConcat(new RegexLeaf("^(note)\\s+"), //
				new RegexLeaf("CODE", "as\\s+([\\p{L}0-9_.]+)"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("COLOR", "(#\\w+[-\\\\|/]?\\w+)?"), //
				new RegexLeaf("$") //
		);
	}

	private RegexConcat getRegexConcatSingleLine() {
		return new RegexConcat(new RegexLeaf("^note\\s+"), //
				new RegexLeaf("DISPLAY", "\"([^\"]+)\"\\s+as\\s+"), //
				new RegexLeaf("CODE", "([\\p{L}0-9_.]+)\\s*"), //
				new RegexLeaf("COLOR", "(#\\w+[-\\\\|/]?\\w+)?"), //
				new RegexLeaf("$") //
		);

	}

	public Command createSingleLine(final AbstractEntityDiagram system) {
		return new SingleLineCommand2<AbstractEntityDiagram>(system, getRegexConcatSingleLine()) {

			@Override
			protected CommandExecutionResult executeArg(RegexResult arg) {
				final String display = arg.get("DISPLAY", 0);
				return executeInternal(getSystem(), arg, StringUtils.getWithNewlines2(display));
			}

		};
	}

	public Command createMultiLine(final AbstractEntityDiagram system) {
		return new CommandMultilines2<AbstractEntityDiagram>(system, getRegexConcatMultiLine(), MultilinesStrategy.KEEP_STARTING_QUOTE) {

			@Override
			public String getPatternEnd() {
				return "(?i)^end ?note$";
			}

			public CommandExecutionResult executeNow(List<String> lines) {
				//StringUtils.trim(lines, false);
				final RegexResult line0 = getStartingPattern().matcher(lines.get(0).trim());

				final List<String> strings = StringUtils.removeEmptyColumns(lines.subList(1, lines.size() - 1));

				return executeInternal(getSystem(), line0, strings);
			}
		};
	}

	private CommandExecutionResult executeInternal(AbstractEntityDiagram system, RegexResult arg,
			final List<? extends CharSequence> display) {
		final Code code = Code.of(arg.get("CODE", 0));
		final IEntity entity = system.createLeaf(code, new Display(display), LeafType.NOTE);
		assert entity != null;
		entity.setSpecificBackcolor(HtmlColorUtils.getColorIfValid(arg.get("COLOR", 0)));
		return CommandExecutionResult.ok();
	}

}
