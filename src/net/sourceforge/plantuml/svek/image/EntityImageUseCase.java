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
 * Revision $Revision: 5183 $
 *
 */
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.BodyEnhanced;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.svek.image.EntityImageUseCase.MyUGraphicEllipse;
import net.sourceforge.plantuml.ugraphic.AbstractUGraphicHorizontalLine2;
import net.sourceforge.plantuml.ugraphic.TextBlockInEllipse;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UHorizontalLine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class EntityImageUseCase extends AbstractEntityImage {

	final private TextBlock desc;

	final private List<Url> url;

	static private final UStroke stroke = new UStroke(1.5);

	public EntityImageUseCase(ILeaf entity, ISkinParam skinParam) {
		super(entity, skinParam);
		final Stereotype stereotype = entity.getStereotype();

		final TextBlock tmp = new BodyEnhanced(entity.getDisplay(), FontParam.USECASE, skinParam,
				HorizontalAlignement.CENTER, stereotype, true, false);

		if (stereotype == null || stereotype.getLabel() == null) {
			this.desc = tmp;
		} else {
			final TextBlock stereo = TextBlockUtils.create(
					Display.getWithNewlines(stereotype.getLabel()),
					new FontConfiguration(SkinParamUtils.getFont(getSkinParam(), FontParam.USECASE_ACTOR_STEREOTYPE,
							stereotype), SkinParamUtils.getFontColor(getSkinParam(),
							FontParam.USECASE_ACTOR_STEREOTYPE, null)), HorizontalAlignement.CENTER, skinParam);
			this.desc = TextBlockUtils.mergeTB(stereo, tmp, HorizontalAlignement.CENTER);
		}
		this.url = entity.getUrls();

	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new TextBlockInEllipse(desc, stringBounder).calculateDimension(stringBounder);
	}

	final public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final TextBlockInEllipse ellipse = new TextBlockInEllipse(desc, stringBounder);
		if (getSkinParam().shadowing()) {
			ellipse.setDeltaShadow(3);
		}

		if (url.size() > 0) {
			ug.startUrl(url.get(0));
		}

		ug = ug.apply(stroke).apply(
				new UChangeColor(SkinParamUtils.getColor(getSkinParam(), ColorParam.usecaseBorder, getStereo())));
		HtmlColor backcolor = getEntity().getSpecificBackColor();
		if (backcolor == null) {
			backcolor = SkinParamUtils.getColor(getSkinParam(), ColorParam.usecaseBackground, getStereo());
		}
		ug = ug.apply(new UChangeBackColor(backcolor));
		final UGraphic ug2 = new MyUGraphicEllipse(ug, 0, 0, ellipse.getUEllipse());

		ellipse.drawU(ug2);

		if (url.size() > 0) {
			ug.closeAction();
		}
	}

	public ShapeType getShapeType() {
		return ShapeType.OVAL;
	}

	public int getShield() {
		return 0;
	}

	static class MyUGraphicEllipse extends AbstractUGraphicHorizontalLine2 {

		private final double startingX;
		private final double yTheoricalPosition;
		private final UEllipse ellipse;

		@Override
		protected AbstractUGraphicHorizontalLine2 copy(UGraphic ug) {
			return new MyUGraphicEllipse(ug, startingX, yTheoricalPosition, ellipse);
		}

		MyUGraphicEllipse(UGraphic ug, double startingX, double yTheoricalPosition, UEllipse ellipse) {
			super(ug);
			this.startingX = startingX;
			this.ellipse = ellipse;
			this.yTheoricalPosition = yTheoricalPosition;
		}

		private double getNormalized(double y) {
			if (y < yTheoricalPosition) {
				throw new IllegalArgumentException();
			}
			y = y - yTheoricalPosition;
			if (y > ellipse.getHeight()) {
				throw new IllegalArgumentException();
			}
			return y;
		}

		private double getStartingX(double y) {
			return startingX + ellipse.getStartingX(getNormalized(y));
		}

		private double getEndingX(double y) {
			return startingX + ellipse.getEndingX(getNormalized(y));
		}

		@Override
		protected void drawHline(UGraphic ug, UHorizontalLine line, UTranslate translate) {
			final double y = translate.getDy();
			line.drawLine(ug.apply(translate), getStartingX(y), getEndingX(y), 0, stroke);
		}

	}

}
