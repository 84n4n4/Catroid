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
package org.catrobat.catroid.web;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.common.FlavoredConstants;
import org.catrobat.catroid.transfers.project.ProjectUploadData;
import org.catrobat.catroid.web.requests.HttpRequestsKt;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

import static org.catrobat.catroid.web.CatrobatWebClientKt.createFormEncodedRequest;
import static org.catrobat.catroid.web.CatrobatWebClientKt.performCallWith;
import static org.catrobat.catroid.web.ServerAuthenticationConstants.EXCHANGE_GOOGLE_CODE_URL;
import static org.catrobat.catroid.web.ServerAuthenticationConstants.GOOGLE_LOGIN_URL_APPENDING;
import static org.catrobat.catroid.web.ServerAuthenticationConstants.JSON_STATUS_CODE;
import static org.catrobat.catroid.web.ServerAuthenticationConstants.SERVER_RESPONSE_REGISTER_OK;
import static org.catrobat.catroid.web.ServerAuthenticationConstants.SERVER_RESPONSE_TOKEN_OK;
import static org.catrobat.catroid.web.ServerAuthenticationConstants.SIGNIN_EMAIL_KEY;
import static org.catrobat.catroid.web.ServerAuthenticationConstants.SIGNIN_GOOGLE_CODE_KEY;
import static org.catrobat.catroid.web.ServerAuthenticationConstants.SIGNIN_ID_TOKEN;
import static org.catrobat.catroid.web.ServerAuthenticationConstants.SIGNIN_LOCALE_KEY;
import static org.catrobat.catroid.web.ServerAuthenticationConstants.SIGNIN_OAUTH_ID_KEY;
import static org.catrobat.catroid.web.ServerAuthenticationConstants.SIGNIN_USERNAME_KEY;
import static org.catrobat.catroid.web.ServerAuthenticationConstants.TOKEN_CODE_INVALID;
import static org.catrobat.catroid.web.ServerAuthenticationConstants.TOKEN_LENGTH;

public final class ServerCalls {
	public static final String BASE_URL_TEST_HTTPS = "https://catroid-test.catrob.at/pocketcode/";
	public static final String TAG = ServerCalls.class.getSimpleName();
	public static boolean useTestUrl = false;
	private final OkHttpClient okHttpClient;
	private String resultString;
	private String projectId;

	public ServerCalls(OkHttpClient httpClient) {
		okHttpClient = httpClient;
	}

	public void uploadProject(ProjectUploadData uploadData, UploadSuccessCallback successCallback,
			UploadErrorCallback errorCallback) {

		executeUploadCall(
				HttpRequestsKt.createUploadRequest(uploadData),
				(uploadResponse) -> {
					String newToken = uploadResponse.token;
					projectId = uploadResponse.projectId;

					if (uploadResponse.statusCode != SERVER_RESPONSE_TOKEN_OK) {
						errorCallback.onError(uploadResponse.statusCode, "Upload failed! JSON Response was " + uploadResponse.statusCode);
					} else if (newToken.equals(TOKEN_CODE_INVALID) || newToken.length() != TOKEN_LENGTH) {
						errorCallback.onError(uploadResponse.statusCode, uploadResponse.answer);
					} else {
						successCallback.onSuccess(projectId, uploadData.getUsername(), newToken);
					}
				},
				errorCallback
		);
	}

	private void executeUploadCall(Request request, UploadCallSuccessCallback successCallback, UploadErrorCallback errorCallback) {
		Response response;
		UploadResponse uploadResponse;
		try {
			response = okHttpClient.newCall(request).execute();
			if (response.isSuccessful()) {
				uploadResponse = new Gson().fromJson(response.body().string(), UploadResponse.class);
				successCallback.onSuccess(uploadResponse);
			} else {
				Log.v(TAG, "Upload not successful");
				errorCallback.onError(response.code(), "Upload failed! HTTP Status code was " + response.code());
			}
		} catch (IOException ioException) {
			Log.e(TAG, Log.getStackTraceString(ioException));
			errorCallback.onError(WebconnectionException.ERROR_NETWORK, "I/O Exception");
		} catch (JsonSyntaxException jsonSyntaxException) {
			Log.e(TAG, Log.getStackTraceString(jsonSyntaxException));
			errorCallback.onError(WebconnectionException.ERROR_JSON, "JsonSyntaxException");
		}
	}

	public void downloadMedia(final String url, final String filePath, final ResultReceiver receiver)
			throws IOException, WebconnectionException {

		File file = new File(filePath);
		if (!(file.getParentFile().mkdirs() || file.getParentFile().isDirectory())) {
			throw new IOException("Directory not created");
		}

		Request request = new Request.Builder()
				.url(url)
				.build();

		OkHttpClient.Builder httpClientBuilder = okHttpClient.newBuilder();
		httpClientBuilder.networkInterceptors().add(chain -> {
			Response originalResponse = chain.proceed(chain.request());
			ProgressResponseBody body = new ProgressResponseBody(originalResponse.body(),
					progress -> {
				Bundle bundle = new Bundle();
				bundle.putLong(ProgressResponseBody.TAG_PROGRESS, progress);
				receiver.send(Constants.UPDATE_DOWNLOAD_PROGRESS, bundle);
			});
			return originalResponse.newBuilder()
					.body(body)
					.build();
		});
		OkHttpClient httpClient = httpClientBuilder.build();
		Response response = httpClient.newCall(request).execute();

		try (BufferedSink bufferedSink = Okio.buffer(Okio.sink(file))) {
			if (response.body() != null) {
				bufferedSink.writeAll(response.body().source());
			} else {
				throw new WebconnectionException(WebconnectionException.ERROR_NETWORK, "FAIL");
			}
		} catch (IOException e) {
			throw new WebconnectionException(WebconnectionException.ERROR_NETWORK, Log.getStackTraceString(e));
		}
	}

	static class UploadResponse {
		String projectId;
		int statusCode;
		String answer;
		String token;
	}

	public boolean googleLogin(String mail, String username, String id, String locale, Context context) throws
			WebconnectionException {

		if (context == null) {
			throw new WebconnectionException(WebconnectionException.ERROR_JSON, "Context is null.");
		}

		try {
			HashMap<String, String> postValues = new HashMap<>();
			postValues.put(SIGNIN_EMAIL_KEY, mail);
			postValues.put(SIGNIN_USERNAME_KEY, username);
			postValues.put(SIGNIN_OAUTH_ID_KEY, id);
			postValues.put(SIGNIN_LOCALE_KEY, locale);

			String serverUrl = FlavoredConstants.BASE_URL_HTTPS + GOOGLE_LOGIN_URL_APPENDING;
			Request request = createFormEncodedRequest(postValues, serverUrl);
			resultString = performCallWith(okHttpClient, request);

			JSONObject jsonObject = new JSONObject(resultString);
			checkStatusCode200(jsonObject.getInt(JSON_STATUS_CODE));
			refreshUploadTokenAndUsername(jsonObject.getString(Constants.TOKEN), username, context);

			return true;
		} catch (JSONException e) {
			throw new WebconnectionException(WebconnectionException.ERROR_JSON, Log.getStackTraceString(e));
		}
	}

	public boolean googleExchangeCode(String code, String id, String username,
			String mail, String locale, String idToken) throws WebconnectionException {

		try {
			HashMap<String, String> postValues = new HashMap<>();
			postValues.put(SIGNIN_GOOGLE_CODE_KEY, code);
			postValues.put(SIGNIN_OAUTH_ID_KEY, id);
			postValues.put(SIGNIN_USERNAME_KEY, username);
			postValues.put(SIGNIN_EMAIL_KEY, mail);
			postValues.put(SIGNIN_LOCALE_KEY, locale);
			postValues.put(SIGNIN_ID_TOKEN, idToken);
			postValues.put(Constants.REQUEST_MOBILE, "Android");

			Request request = createFormEncodedRequest(postValues, EXCHANGE_GOOGLE_CODE_URL);
			resultString = performCallWith(okHttpClient, request);

			JSONObject jsonObject = new JSONObject(resultString);
			int statusCode = jsonObject.getInt(JSON_STATUS_CODE);
			if (!(statusCode == SERVER_RESPONSE_TOKEN_OK || statusCode == SERVER_RESPONSE_REGISTER_OK)) {
				throw new WebconnectionException(statusCode, resultString);
			}

			return true;
		} catch (JSONException e) {
			throw new WebconnectionException(WebconnectionException.ERROR_JSON, Log.getStackTraceString(e));
		}
	}

	private void checkStatusCode200(int statusCode) throws WebconnectionException {
		if (statusCode != SERVER_RESPONSE_TOKEN_OK) {
			throw new WebconnectionException(statusCode, resultString);
		}
	}

	private void refreshUploadTokenAndUsername(String newToken, String username, Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putString(Constants.TOKEN, newToken).commit();
		sharedPreferences.edit().putString(Constants.USERNAME, username).commit();
	}

	public interface UploadSuccessCallback {
		void onSuccess(String projectId, String username, String token);
	}

	public interface UploadErrorCallback {
		void onError(int statusCode, String errorMessage);
	}

	private interface UploadCallSuccessCallback {
		void onSuccess(UploadResponse response);
	}
}
