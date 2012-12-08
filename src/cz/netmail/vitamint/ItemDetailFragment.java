package cz.netmail.vitamint;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import cz.netmail.vitamint.model.Article;
import cz.netmail.vitamint.service.DataService;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Article mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ItemDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			int position = Integer.parseInt(getArguments().getString(ARG_ITEM_ID));
			//        	Object[] array = (Object[])DataService.articles.toArray();
			//        	mItem = (Article)array[position];
			for (Article article : DataService.articles) {
				if (article.id == position) mItem = article;
			}

			if (mItem==null) {
				Toast.makeText(getActivity().getApplicationContext(), "Article with id " + position + " not found", Toast.LENGTH_LONG).show();
			}
			//            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.description);
			((TextView) rootView.findViewById(R.id.item_title)).setText(mItem.title);

			ImageView iv = (ImageView)rootView.findViewById(R.id.item_image);

			if (mItem.cover_url != null && !mItem.cover_url.isEmpty()) {
				String url = DataService.SERVER_URL + mItem.cover_url;
				UrlImageViewHelper.setUrlDrawable(iv, url, R.drawable.loading, null);
			}
		}

		return rootView;
	}
}
