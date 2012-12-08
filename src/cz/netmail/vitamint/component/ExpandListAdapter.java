package cz.netmail.vitamint.component;

import java.util.List;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.content.Context;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cz.netmail.vitamint.R;
import cz.netmail.vitamint.model.Article;
import cz.netmail.vitamint.model.ExpandableDataProvider;
import cz.netmail.vitamint.service.DataService;

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

		((TextView) view.findViewById(R.id.list_title)).setText(child.title.toString());
		((TextView) view.findViewById(R.id.list_teaser)).setText(child.teaser.toString());
		
		((TextView) view.findViewById(R.id.views_text)).setText(child.stats.views.toString());
		((TextView) view.findViewById(R.id.likes_text)).setText(child.stats.likes.toString());
		((TextView) view.findViewById(R.id.comments_text)).setText(child.stats.comments.toString());
		
		((TextView) view.findViewById(R.id.list_spacer)).setVisibility(View.VISIBLE);
		
		if (child.cover_url!=null && !child.cover_url.isEmpty()) {
			String url = DataService.SERVER_URL + child.cover_url;
			ImageView iv = (ImageView)view.findViewById(R.id.list_image);
			UrlImageViewHelper.setUrlDrawable(iv, url, R.drawable.loading, null);
		}
		
		if (child.country!=null) {
			ImageView countryFlag = (ImageView)view.findViewById(R.id.country_image);
			countryFlag.setImageResource(DataService.getResourceForCountry(child.country));	
		}
		
		
		
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
		
		((TextView) view.findViewById(R.id.list_title)).setText(group.name);
		
		TextView teaser = (TextView) view.findViewById(R.id.list_teaser);
		if (group.introduction!=null && !group.introduction.isEmpty()) {
			teaser.setText(group.introduction);
			teaser.setVisibility(View.VISIBLE);
		} else {
			teaser.setVisibility(View.GONE);
		}
		
		((ImageView) view.findViewById(R.id.list_image)).setImageResource(DataService.getResourceForCountry(group.id));
		
		return view;
	}
	
	
	
	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

}
