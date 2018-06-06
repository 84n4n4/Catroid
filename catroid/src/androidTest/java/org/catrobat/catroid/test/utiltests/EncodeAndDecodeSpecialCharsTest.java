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
package org.catrobat.catroid.test.utiltests;

import android.test.InstrumentationTestCase;

import org.catrobat.catroid.utils.FileMetaDataExtractor;

public class EncodeAndDecodeSpecialCharsTest extends InstrumentationTestCase {

	public void testEncodeAndDecodeSpecialCharsForFileSystem() {
		String projectName1 = ".*\"/:<>?\\|%";
		String projectName1Encoded = FileMetaDataExtractor.encodeSpecialCharsForFileSystem(projectName1);
		assertEquals(projectName1Encoded);
		assertEquals(projectName1, FileMetaDataExtractor.decodeSpecialCharsForFileSystem(projectName1Encoded));

		String projectName2 = "../*\"/:<>?\\|";
		String projectName2Encoded = FileMetaDataExtractor.encodeSpecialCharsForFileSystem(projectName2);
		assertEquals(projectName2Encoded);
		assertEquals(projectName2, FileMetaDataExtractor.decodeSpecialCharsForFileSystem(projectName2Encoded));

		String projectName3 = "./*T?E\"S/T:T<E>S?T\\T\\E|S%";
		String projectName3Encoded = FileMetaDataExtractor.encodeSpecialCharsForFileSystem(projectName3);
		assertEquals(projectName3Encoded);
		assertEquals(projectName3, FileMetaDataExtractor.decodeSpecialCharsForFileSystem(projectName3Encoded));

		String projectName4 = ".";
		String projectName4Encoded = FileMetaDataExtractor.encodeSpecialCharsForFileSystem(projectName4);
		assertEquals(projectName4Encoded);
		assertEquals(projectName4, FileMetaDataExtractor.decodeSpecialCharsForFileSystem(projectName4Encoded));

		String projectName5 = "..";
		String projectName5Encoded = FileMetaDataExtractor.encodeSpecialCharsForFileSystem(projectName5);
		assertEquals(projectName5Encoded);
		assertEquals(projectName5, FileMetaDataExtractor.decodeSpecialCharsForFileSystem(projectName5Encoded));

		String projectName6 = "../*T?E\"S/T:%22T<E>S?T\\T\\E|S%äö|üß";
		String projectName6Encoded = FileMetaDataExtractor.encodeSpecialCharsForFileSystem(projectName6);
		assertEquals(projectName6Encoded);
		assertEquals(projectName6, FileMetaDataExtractor.decodeSpecialCharsForFileSystem(projectName6Encoded));
	}
}
