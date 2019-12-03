/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2018 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.test.content.bricks;

import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.FormulaBrick;
import org.catrobat.catroid.content.bricks.GlideToBrick;
import org.catrobat.catroid.content.bricks.InsertItemIntoUserListBrick;
import org.catrobat.catroid.content.bricks.PlaceAtBrick;
import org.catrobat.catroid.content.bricks.ReplaceItemInUserListBrick;
import org.catrobat.catroid.content.bricks.SayForBubbleBrick;
import org.catrobat.catroid.content.bricks.SetPenColorBrick;
import org.catrobat.catroid.content.bricks.ShowTextBrick;
import org.catrobat.catroid.content.bricks.ShowTextColorSizeAlignmentBrick;
import org.catrobat.catroid.content.bricks.ThinkForBubbleBrick;
import org.catrobat.catroid.physics.content.bricks.SetGravityBrick;
import org.catrobat.catroid.physics.content.bricks.SetVelocityBrick;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

import static org.catrobat.catroid.content.bricks.Brick.BrickField.DURATION_IN_SECONDS;
import static org.catrobat.catroid.content.bricks.Brick.BrickField.INSERT_ITEM_INTO_USERLIST_VALUE;
import static org.catrobat.catroid.content.bricks.Brick.BrickField.PEN_COLOR_RED;
import static org.catrobat.catroid.content.bricks.Brick.BrickField.PHYSICS_GRAVITY_X;
import static org.catrobat.catroid.content.bricks.Brick.BrickField.PHYSICS_VELOCITY_X;
import static org.catrobat.catroid.content.bricks.Brick.BrickField.REPLACE_ITEM_IN_USERLIST_INDEX;
import static org.catrobat.catroid.content.bricks.Brick.BrickField.STRING;
import static org.catrobat.catroid.content.bricks.Brick.BrickField.X_POSITION;

@RunWith(Parameterized.class)
public class BricksDefaultFormulaFieldTest {

	@Parameterized.Parameters(name = "{0}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{ShowTextBrick.class.getSimpleName(), new ShowTextBrick(), X_POSITION},
				{ShowTextColorSizeAlignmentBrick.class.getSimpleName(), new ShowTextColorSizeAlignmentBrick(), X_POSITION},
				{InsertItemIntoUserListBrick.class.getSimpleName(), new InsertItemIntoUserListBrick(), INSERT_ITEM_INTO_USERLIST_VALUE},
				{ReplaceItemInUserListBrick.class.getSimpleName(), new ReplaceItemInUserListBrick(), REPLACE_ITEM_IN_USERLIST_INDEX},
				{SayForBubbleBrick.class.getSimpleName(), new SayForBubbleBrick(), STRING},
				{ThinkForBubbleBrick.class.getSimpleName(), new ThinkForBubbleBrick(), STRING},
				{SetPenColorBrick.class.getSimpleName(), new SetPenColorBrick(), PEN_COLOR_RED},
				{PlaceAtBrick.class.getSimpleName(), new PlaceAtBrick(), X_POSITION},
				{GlideToBrick.class.getSimpleName(), new GlideToBrick(), DURATION_IN_SECONDS},
				{SetVelocityBrick.class.getSimpleName(), new SetVelocityBrick(), PHYSICS_VELOCITY_X},
				{SetGravityBrick.class.getSimpleName(), new SetGravityBrick(), PHYSICS_GRAVITY_X},
		});
	}

	@Parameterized.Parameter
	public String name;

	@Parameterized.Parameter(1)
	public FormulaBrick brick;

	@Parameterized.Parameter(2)
	public Brick.BrickField expectedDefaultBrickField;

	@Test
	public void testEditFormula() {
		assertEquals(expectedDefaultBrickField, brick.getDefaultBrickField());
	}
}
