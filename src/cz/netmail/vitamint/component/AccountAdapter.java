package cz.netmail.vitamint.component;

import android.accounts.Account;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AccountAdapter extends ArrayAdapter<Account> {
	private Account[] items;
	private Context context;
	private int resourceId;
	public AccountAdapter(Context context, int textViewResourceId,
			Account[] objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.items = objects;
		this.resourceId = textViewResourceId;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(this.resourceId, null);
        }
        Account item = items[position];
        if (item!= null) {
            // My layout has only one TextView
            TextView itemView = (TextView) view.findViewById(android.R.id.text1);
            if (itemView != null) {
                // do whatever you want with your string and long
                itemView.setText(item.name);
            }
         }

        return view;
    }

}
