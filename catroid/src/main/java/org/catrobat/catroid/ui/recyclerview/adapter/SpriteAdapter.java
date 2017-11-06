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

package org.catrobat.catroid.ui.recyclerview.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Sprite;

import java.util.List;

public class SpriteAdapter extends RecyclerViewAdapter<Sprite> {

	public SpriteAdapter(List<Sprite> items) {
		super(items);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		super.onBindViewHolder(holder, position);

		Context context = holder.itemView.getContext();

		Sprite item = items.get(position);
		Bitmap lookData = null;

		if (!item.getLookList().isEmpty()) {
			lookData = item.getLookList().get(0).getThumbnailBitmap();
		}

		holder.name.setText(item.getName());
		holder.image.setImageBitmap(lookData);

		if (showDetails) {
			holder.details.setVisibility(View.VISIBLE);

			holder.leftBottomDetails.setText(context
					.getString(R.string.number_of_scripts)
					.concat(" ")
					.concat(Integer.toString(item.getNumberOfScripts())));
			holder.rightBottomDetails.setText(context
					.getString(R.string.number_of_bricks)
					.concat(" ")
					.concat(Integer.toString(item.getNumberOfBricks())));

			holder.leftTopDetails.setText(context
					.getString(R.string.number_of_looks)
					.concat(" ")
					.concat(Integer.toString(item.getLookList().size())));
			holder.rightTopDetails.setText(context
					.getString(R.string.number_of_sounds)
					.concat(" ")
					.concat(Integer.toString(item.getSoundList().size())));
		} else {
			holder.details.setVisibility(View.GONE);
		}
	}
}
