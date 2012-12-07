package cz.netmail.vitamint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import cz.netmail.vitamint.component.ImageSimpleAdapter;
import cz.netmail.vitamint.model.Article;
import cz.netmail.vitamint.model.Chapter;
import cz.netmail.vitamint.model.Country;
import cz.netmail.vitamint.service.DataService;

/**
 * A list fragment representing a list of Items. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link ItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ItemListFragment extends ListFragment {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
	private static final String STATE_TWO_PANE = "two_pane";

	public static final String ARG_SECTION_NAME = "section_name";

	public boolean mTwoPane = false;
	//    public static final String ARG_ARTICLES = "articles";


	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	private ListFragment parentFragment;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(String id);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(String id) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ItemListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parentFragment = this;

		new LoadArticlesTask().execute();
	}


	private class LoadArticlesTask extends AsyncTask<Void,Void,Collection<Article>> {
		@Override
		protected Collection<Article> doInBackground(Void... nil) {
			try {
				String url = "https://oauth-demo-netmail.appspot.com/api/articles";
				HttpGet http_get = new HttpGet(url);
				HttpResponse result = DataService.client.execute(http_get);
				String data = EntityUtils.toString(result.getEntity());
				Collection<Article> articles = DataService.gson.fromJson(data, DataService.ArticleCollectionType);
				return articles;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Collection<Article> result) {
			//				Log.e("data", result.toString());
			//				Toast.makeText(getActivity().getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
			if (result == null || result.isEmpty()) {
				Toast.makeText(getActivity().getApplicationContext(), "No articles for this account", Toast.LENGTH_LONG).show();
				return;
			}

			DataService.articles = result;

			ArrayList<HashMap<String, String>> listData = new ArrayList<HashMap<String, String>>();
			HashMap<String, Chapter> chapterData = new HashMap<String, Chapter>();
			HashMap<String, Country> countryData = new HashMap<String, Country>();
			DataService.countries = new ArrayList<Country>();
			DataService.chapters = new ArrayList<Chapter>();

			for (Article article : result) {
//				Log.e("chapter", article.chapter);
//				Log.e("title", article.title);
				if (chapterData.containsKey(article.chapter)) {
					chapterData.get(article.chapter).articlesCollection.add(article);
				} else {
					Chapter chapter = new Chapter();
					chapter.id = article.chapter;
					chapter.articlesCollection = new ArrayList<Article>();
					chapter.articlesCollection.add(article);
					chapterData.put(article.chapter, chapter);
				}
				
				for (String countryCode : article.countries) {
					if (countryData.containsKey(countryCode)) {
						countryData.get(countryCode).articlesCollection.add(article);
					} else {
						Country country = new Country();
						country.id = countryCode;
						country.articlesCollection = new ArrayList<Article>();
						country.articlesCollection.add(article);
						countryData.put(countryCode, country);
					}
				}
				
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("title", article.title);
				map.put("teaser", article.teaser);
				map.put("image", DataService.SERVER_URL + article.cover_url);
				listData.add(map);
			}
			
			for (Country country : countryData.values()) {
//				Log.e("country", country.id);
				DataService.countries.add(country);
			}
			
			for (Chapter chapter : chapterData.values()) {
				Log.e("chapter", chapter.id);
				DataService.chapters.add(chapter);
			}

			ListAdapter adapter = new ImageSimpleAdapter(
					getActivity(),
					listData,
					R.layout.list_item_image,
					new String[] {"title","teaser","image"},
					new int[]{R.id.list_title,R.id.list_teaser,R.id.list_image}); 

			setListAdapter(adapter);
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (mTwoPane) setActivateOnItemClick(true);
		
		if (savedInstanceState != null) {
			
			
			if (savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
				int position = savedInstanceState.getInt(STATE_ACTIVATED_POSITION);
				setActivatedPosition(position);
				mCallbacks.onItemSelected(""+position);
			}

			if (savedInstanceState.containsKey(STATE_TWO_PANE)) {
				mTwoPane = savedInstanceState.getBoolean(STATE_TWO_PANE);
				setActivateOnItemClick(mTwoPane);	
			}

		}

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);

		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		mCallbacks.onItemSelected(""+position);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putBoolean(STATE_TWO_PANE, mTwoPane);
		
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
			
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(activateOnItemClick
				? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
}
