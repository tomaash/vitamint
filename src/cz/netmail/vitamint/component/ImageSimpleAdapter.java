package cz.netmail.vitamint.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import cz.netmail.vitamint.R;

public class ImageSimpleAdapter extends SimpleAdapter {

	public ImageSimpleAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
					int[] to) {
		super(context, data, resource, from, to);
	}

	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		@SuppressWarnings("unchecked")
		HashMap<String, String> data = (HashMap<String, String>) getItem(position);

		String imageUrl = (String) data.get("image");
		if (imageUrl!=null && imageUrl!="") {
			ImageView iv = (ImageView)view.findViewById(R.id.list_image);
			UrlImageViewHelper.setUrlDrawable(iv, imageUrl, R.drawable.loading, null);
		}

		return view;
	}

}
