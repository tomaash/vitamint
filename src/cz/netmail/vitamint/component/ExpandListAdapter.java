package cz.netmail.vitamint.component;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import cz.netmail.vitamint.R;
import cz.netmail.vitamint.model.Article;
import cz.netmail.vitamint.model.ExpandableDataProvider;

public class ExpandListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<ExpandableDataProvider> groups;
	public ExpandListAdapter(Context context, List<ExpandableDataProvider> groups) {
		this.context = context;
		this.groups = groups;
	}
	
	public void addItem(Article item, ExpandableDataProvider group) {
		if (!groups.contains(group)) {
			groups.add(group);
		}
		int index = groups.indexOf(group);
		List<Article> ch = groups.get(index).articlesCollection;
		ch.add(item);
		groups.get(index).articlesCollection = ch;
	}
	public Object getChild(int groupPosition, int childPosition) {
		List<Article> chList = groups.get(groupPosition).articlesCollection;
		return chList.get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		Article child = (Article) getChild(groupPosition, childPosition);
		if (view == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.list_item_image, null);
		}
		TextView tv = (TextView) view.findViewById(R.id.list_title);
		tv.setText(child.title.toString());

		return view;
	}

	public int getChildrenCount(int groupPosition) {
		List<Article> chList = groups.get(groupPosition).articlesCollection;
		return chList.size();
	}

	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	public int getGroupCount() {
		return groups.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		ExpandableDataProvider group = (ExpandableDataProvider) getGroup(groupPosition);
		if (view == null) {
			LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = inf.inflate(R.layout.list_item_group, null);
		}
		TextView tv = (TextView) view.findViewById(R.id.list_title);
		tv.setText(group.name);
		return view;
	}
	
	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

}
