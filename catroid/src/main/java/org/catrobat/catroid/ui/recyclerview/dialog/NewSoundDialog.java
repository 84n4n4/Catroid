/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2017 The Catrobat Team
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

package org.catrobat.catroid.ui.recyclerview.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.catrobat.catroid.BuildConfig;
import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.common.SoundInfo;
import org.catrobat.catroid.io.StorageHandler;
import org.catrobat.catroid.soundrecorder.SoundRecorderActivity;
import org.catrobat.catroid.ui.WebViewActivity;
import org.catrobat.catroid.ui.recyclerview.dialog.dialoginterface.NewItemInterface;
import org.catrobat.catroid.ui.recyclerview.fragment.util.UniqueNameProvider;
import org.catrobat.catroid.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.catrobat.catroid.common.Constants.SOUND_DIRECTORY;
import static org.catrobat.catroid.ui.recyclerview.fragment.SoundFragment.FILE;
import static org.catrobat.catroid.ui.recyclerview.fragment.SoundFragment.LIBRARY;
import static org.catrobat.catroid.ui.recyclerview.fragment.SoundFragment.RECORD;
import static org.catrobat.catroid.utils.Utils.buildPath;
import static org.catrobat.catroid.utils.Utils.buildScenePath;

public class NewSoundDialog extends DialogFragment {

	public static final String TAG = NewSoundDialog.class.getSimpleName();

	private static final String CURRENT_SOUND_DIRECTORY = buildPath(buildScenePath(
			ProjectManager.getInstance().getCurrentProject().getName(),
			ProjectManager.getInstance().getCurrentScene().getName()),
			SOUND_DIRECTORY);

	private NewItemInterface<SoundInfo> newItemInterface;
	private UniqueNameProvider uniqueNameProvider = new UniqueNameProvider();

	public NewSoundDialog(NewItemInterface<SoundInfo> newItemInterface) {
		this.newItemInterface = newItemInterface;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity())
				.inflate(R.layout.dialog_new_sound, (ViewGroup) getView(), false);
		setupRecordButton(view);
		setupGalleryButton(view);
		setupMediaLibraryButton(view);
		setupPocketMusicButton(view);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(view)
				.setTitle(R.string.new_sound_dialog_title);

		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}

	private void setupRecordButton(View view) {
		View button = view.findViewById(R.id.dialog_new_sound_recorder);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				requestNewItem(RECORD);
			}
		});
	}

	private void setupGalleryButton(View view) {
		View button = view.findViewById(R.id.dialog_new_sound_gallery);

		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				requestNewItem(FILE);
			}
		});
	}

	private void setupMediaLibraryButton(View view) {
		View button = view.findViewById(R.id.dialog_new_sound_media_library);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (Utils.isNetworkAvailable(view.getContext(), true)) {
					requestNewItem(LIBRARY);
				}
			}
		});
	}

	private void setupPocketMusicButton(View view) {
		View button = view.findViewById(R.id.dialog_new_sound_pocketmusic);

		if (BuildConfig.FEATURE_POCKETMUSIC_ENABLED) {
			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					requestNewItem(RECORD);
				}
			});
		} else {
			button.setVisibility(View.GONE);
		}
	}

	private void requestNewItem(int requestType) {
		switch (requestType) {
			case RECORD:
				Intent recorderIntent = new Intent(getActivity(), SoundRecorderActivity.class);
				startActivityForResult(recorderIntent, RECORD);
				break;
			case LIBRARY:
				Intent libraryIntent = new Intent(getActivity(), WebViewActivity.class);
				String url = Constants.LIBRARY_SOUNDS_URL;
				libraryIntent.putExtra(WebViewActivity.INTENT_PARAMETER_URL, url);
				libraryIntent.putExtra(WebViewActivity.CALLING_ACTIVITY, TAG);
				startActivityForResult(libraryIntent, requestType);
				break;
			case FILE:
				Intent fileChooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
				fileChooserIntent.setType("audio/*");
				startActivityForResult(Intent.createChooser(fileChooserIntent,
						getString(R.string.sound_select_source)),
						requestType);
				break;
			default:
				break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_CANCELED) {
			return;
		}

		String srcPath;

		switch (requestCode) {
			case RECORD:
			case FILE:
				srcPath = StorageHandler.getPathFromUri(getActivity().getContentResolver(), data.getData());
				createItem(srcPath);
				break;
			case LIBRARY:
				srcPath = data.getStringExtra(WebViewActivity.MEDIA_FILE_PATH);
				createItem(srcPath);
				break;
			default:
				break;
		}

		dismiss();
	}

	private void createItem(String srcPath) {
		if (srcPath.isEmpty()) {
			return;
		}
		try {
			String name = StorageHandler.getSanitizedFileName(new File(srcPath));
			String fileName = StorageHandler.copyFile(srcPath, CURRENT_SOUND_DIRECTORY).getName();
			newItemInterface.addItem(new SoundInfo(uniqueNameProvider.getUniqueName(name, getScope()), fileName));
		} catch (IOException e) {
			Log.e(TAG, Log.getStackTraceString(e));
		}
	}

	private Set<String> getScope() {
		Set<String> scope = new HashSet<>();
		for (SoundInfo item : ProjectManager.getInstance().getCurrentSprite().getSoundList()) {
			scope.add(item.getTitle());
		}
		return scope;
	}
}
