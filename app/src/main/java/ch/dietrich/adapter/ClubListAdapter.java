package ch.dietrich.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ch.dietrich.dietrichapplication.R;
import ch.dietrich.entity.Club;

import java.util.List;

public class ClubListAdapter extends ArrayAdapter<Club> {

	private final Context context;
	private final List<Club> objects;
	private final int resource;

	public ClubListAdapter(Context context, int resource, List<Club> objects) {
		super(context, resource, objects);
		this.context = context;
		this.objects = objects;
		this.resource = resource;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
	    TextView v = (TextView) super.getView(position, convertView, parent);

	    if (v == null) {
	        v = new TextView(context);
	    }
	    v.setText(objects.get(position).getName());
	    return v;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(resource, parent, false);
		}
		TextView clubTextView = (TextView) convertView
				.findViewById(R.id.text1);
		Club _item = objects.get(position);
		clubTextView.setText(_item.getName());
		return convertView;
	}
}
