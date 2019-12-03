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

package org.catrobat.catroid.test.content.backwardcompatibility;

import org.catrobat.catroid.content.backwardcompatibility.BrickTreeBuilder;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.CompositeBrick;
import org.catrobat.catroid.content.bricks.IfLogicBeginBrick;
import org.catrobat.catroid.content.bricks.IfLogicElseBrick;
import org.catrobat.catroid.content.bricks.IfLogicEndBrick;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

@RunWith(Parameterized.class)
public class ThreePartCompositeBrickTreeBuilderTest {

	@Parameterized.Parameters(name = "{0}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{IfLogicBeginBrick.class.getSimpleName(), IfLogicBeginBrick.class, IfLogicElseBrick.class, IfLogicEndBrick.class}
		});
	}

	@Parameterized.Parameter
	public String name;

	@Parameterized.Parameter(1)
	public Class<CompositeBrick> compositeBrickClass;

	@Parameterized.Parameter(2)
	public Class<Brick> compositeSecondaryBrickClass;

	@Parameterized.Parameter(3)
	public Class<Brick> compositeEndBrickClass;

	@Test
	public void testBuildTreeFromValidList() throws IllegalAccessException, InstantiationException {
		List<Brick> flatList = new ArrayList<>();

		CompositeBrick compositeBrick = compositeBrickClass.newInstance();
		Brick compositeSecondaryBrick = compositeSecondaryBrickClass.newInstance();
		Brick compositeEndBrick = compositeEndBrickClass.newInstance();

		Brick mockBrickInCompositeList = mock(Brick.class);
		Brick mockBrickInSecondaryCompositeList = mock(Brick.class);
		Brick mockBrickAfterCompositeBrick = mock(Brick.class);

		flatList.add(compositeBrick);
		flatList.add(mockBrickInCompositeList);
		flatList.add(compositeSecondaryBrick);
		flatList.add(mockBrickInSecondaryCompositeList);
		flatList.add(compositeEndBrick);
		flatList.add(mockBrickAfterCompositeBrick);

		BrickTreeBuilder brickTreeBuilder = new BrickTreeBuilder();
		brickTreeBuilder.convertBricks(flatList);

		List<Brick> convertedBricks = brickTreeBuilder.toList();

		assertEquals(2, convertedBricks.size());

		assertSame(compositeBrick, convertedBricks.get(0));

		assertFalse(convertedBricks.contains(compositeEndBrick));

		assertEquals(1, compositeBrick.getNestedBricks().size());

		assertEquals(1, compositeBrick.getSecondaryNestedBricks().size());

		assertSame(mockBrickInCompositeList, compositeBrick.getNestedBricks().get(0));

		assertSame(mockBrickInSecondaryCompositeList, compositeBrick.getSecondaryNestedBricks().get(0));

		assertSame(mockBrickAfterCompositeBrick, convertedBricks.get(1));
	}

	@Test
	public void testBuildTreeFromValidNestedList() throws IllegalAccessException, InstantiationException {
		List<Brick> flatList = new ArrayList<>();

		CompositeBrick outerCompositeBrick = compositeBrickClass.newInstance();
		Brick outerCompositeSecondaryBrick = compositeSecondaryBrickClass.newInstance();
		Brick outerCompositeEndBrick = compositeEndBrickClass.newInstance();

		Brick mockBrickInOuterCompositeList = mock(Brick.class);
		Brick mockBrickInOuterSecondaryCompositeList = mock(Brick.class);
		Brick mockBrickAfterOuterCompositeBrick = mock(Brick.class);

		CompositeBrick innerCompositeBrick = compositeBrickClass.newInstance();
		Brick innerCompositeSecondaryBrick = compositeSecondaryBrickClass.newInstance();
		Brick innerCompositeEndBrick = compositeEndBrickClass.newInstance();

		Brick mockBrickInInnerCompositeList = mock(Brick.class);
		Brick mockBrickInInnerSecondaryCompositeList = mock(Brick.class);
		Brick mockBrickAfterInnerCompositeBrick = mock(Brick.class);

		flatList.add(outerCompositeBrick);
		flatList.add(mockBrickInOuterCompositeList);
		flatList.add(innerCompositeBrick);
		flatList.add(mockBrickInInnerCompositeList);
		flatList.add(innerCompositeSecondaryBrick);
		flatList.add(mockBrickInInnerSecondaryCompositeList);
		flatList.add(innerCompositeEndBrick);
		flatList.add(mockBrickAfterInnerCompositeBrick);
		flatList.add(outerCompositeSecondaryBrick);
		flatList.add(mockBrickInOuterSecondaryCompositeList);
		flatList.add(outerCompositeEndBrick);
		flatList.add(mockBrickAfterOuterCompositeBrick);

		BrickTreeBuilder brickTreeBuilder = new BrickTreeBuilder();
		brickTreeBuilder.convertBricks(flatList);

		List<Brick> convertedBricks = brickTreeBuilder.toList();

		assertEquals(2, convertedBricks.size());

		assertSame(outerCompositeBrick, convertedBricks.get(0));

		assertFalse(convertedBricks.contains(outerCompositeEndBrick));

		assertEquals(3, outerCompositeBrick.getNestedBricks().size());

		assertSame(mockBrickInOuterCompositeList, outerCompositeBrick.getNestedBricks().get(0));

		assertSame(innerCompositeBrick, outerCompositeBrick.getNestedBricks().get(1));

		assertEquals(1, innerCompositeBrick.getNestedBricks().size());

		assertFalse(convertedBricks.contains(innerCompositeEndBrick));

		assertFalse(outerCompositeBrick.getNestedBricks().contains(innerCompositeEndBrick));

		assertSame(mockBrickInInnerCompositeList, innerCompositeBrick.getNestedBricks().get(0));

		assertSame(mockBrickAfterInnerCompositeBrick, outerCompositeBrick.getNestedBricks().get(2));

		assertSame(mockBrickAfterOuterCompositeBrick, convertedBricks.get(1));
	}

	@Test
	public void testBuildTreeFromInvalidList() throws IllegalAccessException, InstantiationException {
		List<Brick> flatList = new ArrayList<>();

		CompositeBrick compositeBrick = compositeBrickClass.newInstance();
		Brick compositeSecondaryBrick = compositeSecondaryBrickClass.newInstance();
		Brick compositeEndBrick = compositeEndBrickClass.newInstance();

		Brick mockBrickInCompositeList = mock(Brick.class);
		doAnswer(new AddToFlatListAnswer()).when(mockBrickInCompositeList).addToFlatList(anyList());

		Brick mockBrickAfterCompositeBrick = mock(Brick.class);
		doAnswer(new AddToFlatListAnswer()).when(mockBrickAfterCompositeBrick).addToFlatList(anyList());

		flatList.add(compositeEndBrick);
		flatList.add(compositeSecondaryBrick);
		flatList.add(compositeBrick);
		flatList.add(mockBrickInCompositeList);
		flatList.add(compositeEndBrick);
		flatList.add(mockBrickAfterCompositeBrick);

		BrickTreeBuilder brickTreeBuilder = new BrickTreeBuilder();
		brickTreeBuilder.convertBricks(flatList);

		List<Brick> convertedBricks = brickTreeBuilder.toList();

		assertEquals(2, convertedBricks.size());

		List<Brick> convertedFlatList = new ArrayList<>();
		convertedBricks.get(0).addToFlatList(convertedFlatList);
		convertedBricks.get(1).addToFlatList(convertedFlatList);

		assertTrue(convertedFlatList.contains(compositeBrick));
		assertTrue(convertedFlatList.contains(mockBrickInCompositeList));
		assertTrue(convertedFlatList.contains(mockBrickAfterCompositeBrick));
	}

	@Test
	public void testBuildTreeFromInvalidNestedListDoesNotThrowException() throws IllegalAccessException, InstantiationException {
		List<Brick> flatList = new ArrayList<>();

		CompositeBrick outerCompositeBrick = compositeBrickClass.newInstance();
		Brick outerCompositeSecondaryBrick = compositeSecondaryBrickClass.newInstance();
		Brick outerCompositeEndBrick = compositeEndBrickClass.newInstance();

		Brick mockBrickInOuterCompositeList = mock(Brick.class);
		doAnswer(new AddToFlatListAnswer()).when(mockBrickInOuterCompositeList).addToFlatList(anyList());

		Brick mockBrickInOuterSecondaryCompositeList = mock(Brick.class);
		doAnswer(new AddToFlatListAnswer()).when(mockBrickInOuterSecondaryCompositeList).addToFlatList(anyList());

		Brick mockBrickAfterOuterCompositeBrick = mock(Brick.class);
		doAnswer(new AddToFlatListAnswer()).when(mockBrickAfterOuterCompositeBrick).addToFlatList(anyList());

		CompositeBrick innerCompositeBrick = compositeBrickClass.newInstance();
		Brick innerCompositeSecondaryBrick = compositeSecondaryBrickClass.newInstance();
		Brick innerCompositeEndBrick = compositeEndBrickClass.newInstance();

		Brick mockBrickInInnerCompositeList = mock(Brick.class);
		doAnswer(new AddToFlatListAnswer()).when(mockBrickInInnerCompositeList).addToFlatList(anyList());

		Brick mockBrickInInnerSecondaryCompositeList = mock(Brick.class);
		doAnswer(new AddToFlatListAnswer()).when(mockBrickInInnerSecondaryCompositeList).addToFlatList(anyList());

		Brick mockBrickAfterInnerCompositeBrick = mock(Brick.class);
		doAnswer(new AddToFlatListAnswer()).when(mockBrickAfterInnerCompositeBrick).addToFlatList(anyList());

		flatList.add(outerCompositeBrick);
		flatList.add(mockBrickInOuterCompositeList);
		flatList.add(innerCompositeEndBrick);
		flatList.add(innerCompositeBrick);
		flatList.add(mockBrickInInnerCompositeList);
		flatList.add(innerCompositeSecondaryBrick);
		flatList.add(mockBrickInInnerSecondaryCompositeList);
		flatList.add(mockBrickAfterInnerCompositeBrick);
		flatList.add(outerCompositeSecondaryBrick);
		flatList.add(mockBrickInOuterSecondaryCompositeList);
		flatList.add(outerCompositeEndBrick);
		flatList.add(mockBrickAfterOuterCompositeBrick);

		BrickTreeBuilder brickTreeBuilder = new BrickTreeBuilder();
		brickTreeBuilder.convertBricks(flatList);

		List<Brick> convertedBricks = brickTreeBuilder.toList();

		assertEquals(3, convertedBricks.size());

		List<Brick> convertedFlatList = new ArrayList<>();
		convertedBricks.get(0).addToFlatList(convertedFlatList);
		convertedBricks.get(1).addToFlatList(convertedFlatList);
		convertedBricks.get(2).addToFlatList(convertedFlatList);

		assertTrue(convertedFlatList.contains(outerCompositeBrick));
		assertTrue(convertedFlatList.contains(innerCompositeBrick));

		assertTrue(convertedFlatList.contains(mockBrickInOuterCompositeList));
		assertTrue(convertedFlatList.contains(mockBrickInOuterSecondaryCompositeList));
		assertTrue(convertedFlatList.contains(mockBrickAfterOuterCompositeBrick));

		assertTrue(convertedFlatList.contains(mockBrickInInnerCompositeList));
		assertTrue(convertedFlatList.contains(mockBrickInInnerSecondaryCompositeList));
		assertTrue(convertedFlatList.contains(mockBrickAfterInnerCompositeBrick));
	}

	private static class AddToFlatListAnswer implements Answer {
		@Override
		public Object answer(InvocationOnMock invocation) {
			((List<Brick>) invocation.getArgument(0)).add((Brick) invocation.getMock());
			return null;
		}
	}
}
