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

package org.catrobat.catroid.devices.mindstorms.ev3.sensors;

import android.util.Log;
import android.util.SparseArray;

import org.catrobat.catroid.devices.mindstorms.LegoSensor;
import org.catrobat.catroid.devices.mindstorms.MindstormsConnection;
import org.catrobat.catroid.devices.mindstorms.MindstormsException;
import org.catrobat.catroid.devices.mindstorms.ev3.EV3Command;
import org.catrobat.catroid.devices.mindstorms.ev3.EV3CommandByte.EV3CommandByteCode;
import org.catrobat.catroid.devices.mindstorms.ev3.EV3CommandByte.EV3CommandOpCode;
import org.catrobat.catroid.devices.mindstorms.ev3.EV3CommandByte.EV3CommandParamFormat;
import org.catrobat.catroid.devices.mindstorms.ev3.EV3CommandByte.EV3CommandVariableScope;
import org.catrobat.catroid.devices.mindstorms.ev3.EV3CommandType;
import org.catrobat.catroid.devices.mindstorms.ev3.EV3Reply;

import java.util.Locale;

public abstract class EV3Sensor implements LegoSensor {

	public enum Sensor {
		NO_SENSOR,
		TOUCH,
		COLOR,
		COLOR_AMBIENT,
		COLOR_REFLECT,
		INFRARED,
		HT_NXT_COLOR,
		NXT_TEMPERATURE_C,
		NXT_TEMPERATURE_F,
		NXT_LIGHT,
		NXT_LIGHT_ACTIVE,
		NXT_SOUND,
		NXT_ULTRASONIC;

		public static String[] getSensorCodes() {
			String[] valueStrings = new String[values().length];

			for (int i = 0; i < values().length; i++) {
				valueStrings[i] = values()[i].name();
			}

			return valueStrings;
		}

		public String getSensorCode() {
			return this.name();
		}

		public static EV3Sensor.Sensor getSensorFromSensorCode(String sensorCode) {
			if (sensorCode == null) {
				return Sensor.NO_SENSOR;
			}

			try {
				return valueOf(sensorCode);
			} catch (IllegalArgumentException e) {
				return Sensor.NO_SENSOR;
			}
		}
	}

	public enum CpuCommandFlag {
		INCLUDE_TYPE_AND_MODE,
		NO_TYPE_AND_MODE,
		INCLUDE_RETURN_VALUE,
		NO_RETURN_VALUE
	}

	protected final int port;
	protected final EV3SensorType sensorType;
	protected final EV3SensorMode sensorMode;
	protected final int updateInterval = 250;

	protected final MindstormsConnection connection;

	protected boolean hasInit;
	protected float lastValidValue = 0;

	public static final String TAG = EV3Sensor.class.getSimpleName();

	public EV3Sensor(int port, EV3SensorType sensorType, EV3SensorMode sensorMode, MindstormsConnection connection) {
		this.port = port;
		this.sensorType = sensorType;
		this.sensorMode = sensorMode;

		this.connection = connection;
	}

	protected void initialize() {

		if (connection != null && connection.isConnected()) {

			int commandCounter = connection.getCommandCounter();
			hasInit = true;

			EV3Command command = buildCommand(1, EV3CommandByteCode.INPUT_DEVICE_READY_RAW,
					EV3CommandOpCode.OP_INPUT_DEVICE, CpuCommandFlag.INCLUDE_TYPE_AND_MODE, CpuCommandFlag.INCLUDE_RETURN_VALUE);

			try {
				sendCommandAndGetReply(command, commandCounter);
			} catch (MindstormsException e) {
				Log.e(TAG, e.getMessage());
			}
		} else {
			hasInit = false;
		}
	}

	public int getPercentValue() {
		int percentValue = 0;

		if (!hasInit) {
			initialize();
		} else {
			int commandCount = connection.getCommandCounter();

			EV3Command command = buildCommand(1, null, EV3CommandOpCode.OP_INPUT_READ,
					CpuCommandFlag.INCLUDE_TYPE_AND_MODE, CpuCommandFlag.NO_RETURN_VALUE);

			try {
				EV3Reply reply = sendCommandAndGetReply(command, commandCount);
				percentValue = reply.getByte(3); // first 2 bytes(reply length) not saved
			} catch (MindstormsException e) {
				Log.e(TAG, e.getMessage());
			}
		}
		return percentValue;
	}

	public byte[] getRawValue(int numBytes) {
		byte[] valueBytes = new byte[numBytes];

		if (!hasInit) {
			initialize();
		} else {
			int commandCount = connection.getCommandCounter();

			EV3Command command = buildCommand(numBytes, EV3CommandByteCode.INPUT_DEVICE_GET_RAW,
					EV3CommandOpCode.OP_INPUT_DEVICE, CpuCommandFlag.NO_TYPE_AND_MODE, CpuCommandFlag.NO_RETURN_VALUE);

			try {
				EV3Reply reply = sendCommandAndGetReply(command, commandCount);
				int offset = 3;
				int replyLength = reply.getLength();
				valueBytes = reply.getData(offset, replyLength - offset);
			} catch (MindstormsException e) {
				Log.e(TAG, e.getMessage());
			}
		}
		return valueBytes;
	}

	public byte[] getSiValue(int numBytes) {
		byte[] siValue = new byte[numBytes];

		if (!hasInit) {
			initialize();
		} else {
			int commandCount = connection.getCommandCounter();

			EV3Command command = buildCommand(numBytes, null, EV3CommandOpCode.OP_INPUT_READ_SI,
					CpuCommandFlag.INCLUDE_TYPE_AND_MODE, CpuCommandFlag.NO_RETURN_VALUE);

			try {
				EV3Reply reply = sendCommandAndGetReply(command, commandCount);
				int offset = 3;
				int replyLength = reply.getLength();
				siValue = reply.getData(offset, replyLength - offset);
			} catch (MindstormsException e) {
				Log.e(TAG, e.getMessage());
			}
		}
		return siValue;
	}

	private EV3Command buildCommand(int globalVars, EV3CommandByteCode commandCode, EV3CommandOpCode opCode, CpuCommandFlag
			addTypeAndMode, CpuCommandFlag addReturnValue) {

		EV3Command command = new EV3Command(connection.getCommandCounter(), EV3CommandType.DIRECT_COMMAND_REPLY,
				globalVars, 0, opCode);
		connection.incCommandCounter();

		int chainLayer = 0;
		int type = this.sensorType.getByte();
		int mode = this.sensorMode.getByte();
		int returnValue = 1; // request 1 return value
		int returnValueIndex = 0;

		if (commandCode != null) {
			command.append(commandCode);
		}

		command.append(EV3CommandParamFormat.PARAM_FORMAT_SHORT, chainLayer);
		command.append(EV3CommandParamFormat.PARAM_FORMAT_SHORT, this.port);

		if (addTypeAndMode == CpuCommandFlag.INCLUDE_TYPE_AND_MODE) {
			command.append(EV3CommandParamFormat.PARAM_FORMAT_LONG, type);
			command.append(EV3CommandParamFormat.PARAM_FORMAT_SHORT, mode);
		}

		if (addReturnValue == CpuCommandFlag.INCLUDE_RETURN_VALUE) {
			command.append(EV3CommandParamFormat.PARAM_FORMAT_SHORT, returnValue);
		}

		command.append(EV3CommandVariableScope.PARAM_VARIABLE_SCOPE_GLOBAL, returnValueIndex);

		return command;
	}

	private EV3Reply sendCommandAndGetReply(EV3Command command, int commandCounter) throws MindstormsException {
		EV3Reply reply = new EV3Reply(connection.sendAndReceive(command));

		if (!reply.isValid(commandCounter)) {
			throw new MindstormsException("Reply not valid!");
		}
		return reply;
	}

	@Override
	public int getUpdateInterval() {
		return updateInterval;
	}

	@Override
	public void updateLastSensorValue() {
		try {
			lastValidValue = getValue();
		} catch (MindstormsException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	@Override
	public float getLastSensorValue() {
		return lastValidValue;
	}

	@Override
	public String getName() {
		return String.format(Locale.getDefault(), "%s_%s_%d", TAG, sensorType.name(), port);
	}

	@Override
	public int getConnectedPort() {
		return port;
	}
}
