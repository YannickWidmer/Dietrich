package ch.dietrich.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ch.dietrich.dietrichapplication.R;
import ch.dietrich.entity.OfferListItem;

import java.util.List;

public class OfferItemListAdapter extends ArrayAdapter<OfferListItem> {

	private final Context context;
	private final List<OfferListItem> objects;
	
	public OfferItemListAdapter(Context context, List<OfferListItem> objects) {
		super(context, R.layout.offer_item, objects);
		this.context = context;
		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.offer_item, parent, false);
		}
		TextView clubTextView = (TextView) convertView.findViewById(R.id.club);
		OfferListItem _item = objects.get(position);
		clubTextView.setText(_item.getClubname());
		TextView offertTypeTextView = (TextView) convertView.findViewById(R.id.offertype);
		offertTypeTextView.setText(_item.getType());
		TextView usernameTextView = (TextView) convertView.findViewById(R.id.username);
		usernameTextView.setText(_item.getUsername());
		return convertView;
	}
}
