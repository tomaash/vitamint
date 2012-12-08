package cz.netmail.vitamint;

import java.util.List;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListView;
import cz.netmail.vitamint.ItemListFragment.Callbacks;
import cz.netmail.vitamint.component.ExpandListAdapter;
import cz.netmail.vitamint.model.Article;
import cz.netmail.vitamint.model.ExpandableDataProvider;
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
public class ItemExpandableListFragment extends Fragment {

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
	//	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	private ListFragment parentFragment;
	private ExpandListAdapter adapter;


	public ItemExpandableListFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.expandable_main, container, false);
		
		int section = getArguments().getInt(MainActivity.ARG_SECTION_ID);
		
		List<ExpandableDataProvider> data;
		if (section == 1) {
			data = DataService.countries;
		} else {
			data = DataService.chapters;
		}
		
		adapter = new ExpandListAdapter(getActivity(), data);
		ExpandableListView lv = (ExpandableListView) v.findViewById(R.id.ExpList);
		lv.setAdapter(adapter);

		lv.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Article child = (Article)adapter.getChild(groupPosition, childPosition);
				((MainActivity)getActivity()).onItemSelected(""+child.id);
				//					((MainActivity)getActivity()).onItemSelected(""+childPosition);
				return true;
			}
		});
		return v;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

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
				//				mCallbacks.onItemSelected(""+position);
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
		//		if (!(activity instanceof Callbacks)) {
		//			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		//		}
		//
		//		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		//		mCallbacks = sDummyCallbacks;
	}

	//	@Override
	//	public void onListItemClick(ListView listView, View view, int position, long id) {
	//		super.onListItemClick(listView, view, position, id);
	//
	//		// Notify the active callbacks interface (the activity, if the
	//		// fragment is attached to one) that an item has been selected.
	//		mCallbacks.onItemSelected(""+position);
	//	}

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
		//		getListView().setChoiceMode(activateOnItemClick
		//				? ListView.CHOICE_MODE_SINGLE
		//						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		//		if (position == ListView.INVALID_POSITION) {
		//			getListView().setItemChecked(mActivatedPosition, false);
		//		} else {
		//			getListView().setItemChecked(position, true);
		//		}

		mActivatedPosition = position;
	}
}
