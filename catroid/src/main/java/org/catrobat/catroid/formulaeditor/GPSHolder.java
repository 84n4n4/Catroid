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
package org.catrobat.catroid.formulaeditor;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;

import com.parrot.freeflight.service.DroneControlService;

import org.catrobat.catroid.CatroidApplication;
import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.bluetooth.base.BluetoothDevice;
import org.catrobat.catroid.bluetooth.base.BluetoothDeviceService;
import org.catrobat.catroid.camera.CameraManager;
import org.catrobat.catroid.cast.CastManager;
import org.catrobat.catroid.common.CatroidService;
import org.catrobat.catroid.common.ServiceProvider;
import org.catrobat.catroid.devices.arduino.phiro.Phiro;
import org.catrobat.catroid.devices.mindstorms.ev3.LegoEV3;
import org.catrobat.catroid.devices.mindstorms.nxt.LegoNXT;
import org.catrobat.catroid.drone.ardrone.DroneServiceWrapper;
import org.catrobat.catroid.facedetection.FaceDetectionHandler;
import org.catrobat.catroid.nfc.NfcHandler;
import org.catrobat.catroid.utils.TouchUtil;

import java.util.Calendar;

import static org.catrobat.catroid.formulaeditor.Sensors.ALTITUDE;
import static org.catrobat.catroid.formulaeditor.Sensors.LATITUDE;
import static org.catrobat.catroid.formulaeditor.Sensors.LOCATION_ACCURACY;
import static org.catrobat.catroid.formulaeditor.Sensors.LONGITUDE;

public class GPSHolder implements LocationListener,
		GpsStatus.Listener {
	public static final float RADIAN_TO_DEGREE_CONST = 180f / (float) Math.PI;
	private static final String TAG = GPSHolder.class.getSimpleName();
	private LocationManager locationManager = null;
	private double latitude = 0d;
	private double longitude = 0d;
	private float locationAccuracy = 0f;
	private double altitude = 0d;

	private boolean isGpsConnected = false;
	private Location lastLocationGps;
	private long lastLocationGpsMillis;

	public GPSHolder(Context context) {
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeUpdates(this);
		locationManager.removeGpsStatusListener(this);
		locationManager.addGpsStatusListener(this);
		if (gpsSensorAvailable()) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		}
		if (networkGpsAvailable()) {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		}
	}

	public boolean gpsAvailable() {
		return gpsSensorAvailable() | networkGpsAvailable();
	}

	private boolean gpsSensorAvailable() {
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	private boolean networkGpsAvailable() {
		return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

	public void stopSensorListeners() {
		locationManager.removeUpdates(this);
		locationManager.removeGpsStatusListener(this);
	}

	public Object getSensorValue(Sensors sensor) {
		switch (sensor) {
			case LATITUDE:
				return latitude;

			case LONGITUDE:
				return longitude;

			case LOCATION_ACCURACY:
				return (double) locationAccuracy;

			case ALTITUDE:
				return altitude;
		}
		return 0d;
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location == null) {
			return;
		}
		if (location.getProvider().equals(LocationManager.GPS_PROVIDER) || !isGpsConnected) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			locationAccuracy = location.getAccuracy();
			altitude = location.hasAltitude() ? location.getAltitude() : 0;
		}

		if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
			lastLocationGpsMillis = SystemClock.elapsedRealtime();
			lastLocationGps = location;
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onGpsStatusChanged(int event) {
		switch (event) {
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				if (lastLocationGps != null) {
					isGpsConnected = (SystemClock.elapsedRealtime() - lastLocationGpsMillis) < 3000;
				}
				break;
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				isGpsConnected = true;
				break;
		}
	}
}
