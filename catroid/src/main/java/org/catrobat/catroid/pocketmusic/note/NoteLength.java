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
package org.catrobat.catroid.pocketmusic.note;

public enum NoteLength {
	WHOLE_DOT(4f + 2f), WHOLE(4f), HALF_DOT(2f + 1f), HALF(2f), QUARTER_DOT(1f + 1 / 2f),
	QUARTER(1f), EIGHT_DOT(1 / 2f + 1 / 4f), EIGHT(1 / 2f), SIXTEENTH(1 / 4f);

	private static final long DEFAULT_TICK_DURATION_MODIFIER = 8;
	private static final NoteLength SMALLEST_NOTE_LENGTH = SIXTEENTH;

	private static final int MINUTE_IN_SECONDS = 60;
	private static final int SECOND_IN_MILLISECONDS = 1000;

	private float length;

	NoteLength(float length) {
		this.length = length;
	}

	public static NoteLength getNoteLengthFromTickDuration(long duration, int beatsPerMinute) {
		NoteLength noteLength = SMALLEST_NOTE_LENGTH;
		NoteLength[] allNoteLengths = NoteLength.values();

		for (int i = (allNoteLengths.length - 1); i >= 0; i--) {
			long difference = duration - allNoteLengths[i].toTicks(beatsPerMinute);

			if (difference < 0) {
				break;
			}

			noteLength = allNoteLengths[i];
		}

		return noteLength;
	}

	public long toTicks(int beatsPerMinute) {
		return Math.round(beatsPerMinute * DEFAULT_TICK_DURATION_MODIFIER * length);
	}

	public long toMilliseconds(int beatsPerMinute) {
		return Math.round(beatsPerMinute * length * SECOND_IN_MILLISECONDS / MINUTE_IN_SECONDS);
	}
}
