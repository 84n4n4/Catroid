/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2015 The Catrobat Team
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

package org.catrobat.catroid.devices.arduino.common.firmata.writer;

import org.catrobat.catroid.devices.arduino.common.firmata.message.ServoConfigMessage;
import org.catrobat.catroid.devices.arduino.common.firmata.serial.ISerial;
import org.catrobat.catroid.devices.arduino.common.firmata.serial.SerialException;

import static org.catrobat.catroid.devices.arduino.common.firmata.BytesHelper.lsb;
import static org.catrobat.catroid.devices.arduino.common.firmata.BytesHelper.msb;

/**
 * MessageWriter for ServoConfigMessage
 */
public class ServoConfigMessageWriter extends SysexMessageWriter<ServoConfigMessage> {

    @Override
    protected void writeData(ServoConfigMessage message, ISerial serial) throws SerialException {
        // can not use super.writeData() because it works with String
        writeServoData(message, serial);
    }

    private void writeServoData(ServoConfigMessage message, ISerial serial) throws SerialException {
        byte[] buffer = new byte[7];
        buffer[0] = (byte)message.getPin();

        buffer[1] = (byte) lsb(message.getMinPulse());
        buffer[2] = (byte) msb(message.getMinPulse());

        buffer[3] = (byte) lsb(message.getMaxPulse());
        buffer[4] = (byte) msb(message.getMaxPulse());

        buffer[5] = (byte) lsb(message.getAngle());
        buffer[6] = (byte) msb(message.getAngle());

        serial.write(buffer);
    }
}