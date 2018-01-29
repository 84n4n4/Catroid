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

package org.catrobat.catroid.ui.recyclerview.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.catrobat.catroid.R;

public class ExtendedVH extends ViewHolder {

	public ImageView image;
	public LinearLayout details;
	public TextView leftTopDetails;
	public TextView leftBottomDetails;
	public TextView rightTopDetails;
	public TextView rightBottomDetails;

	public ExtendedVH(View view) {
		super(view);
		image = view.findViewById(R.id.image_view);
		details = view.findViewById(R.id.list_item_details);
		leftTopDetails = view.findViewById(R.id.details_left_top);
		leftBottomDetails = view.findViewById(R.id.details_left_bottom);
		rightTopDetails = view.findViewById(R.id.details_right_top);
		rightBottomDetails = view.findViewById(R.id.details_right_bottom);
	}
}
