/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2019 The Catrobat Team
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

import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.actions.EventThread;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.WaitBrick;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import static junit.framework.Assert.assertEquals;

@RunWith(JUnit4.class)
public class CommentOutTest {
	private Sprite sprite;
	private StartScript script;
	private EventThread sequence;

	@Before
	public void setUp() throws Exception {
		sprite = new Sprite("testSprite");
		script = new StartScript();
		sequence = (EventThread) sprite.getActionFactory().createEventThread(Mockito.mock(Script.class));
	}

	@Test
	public void testCommentOutSimple() {

		script.addBrick(new WaitBrick(1));
		script.addBrick(new WaitBrick(1));
		script.addBrick(new WaitBrick(1));

		Brick disabledBrick = new WaitBrick(1);
		disabledBrick.setCommentedOut(true);
		script.addBrick(disabledBrick);

		script.run(sprite, sequence);

		assertEquals(sequence.getActions().size, 3);
	}

	@Test
	public void testCommentOutScript() {
		script.addBrick(new WaitBrick(1));
		script.addBrick(new WaitBrick(1));

		script.setCommentedOut(true);

		script.run(sprite, sequence);

		assertEquals(sequence.getActions().size, 0);
	}
}

