package ch.dietrich.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import ch.dietrich.adapter.OfferItemListAdapter;
import ch.dietrich.database.AsyncTaskCompleteListener;
import ch.dietrich.database.OfferDAO;
import ch.dietrich.dietrichapplication.R;
import ch.dietrich.entity.OfferListItem;

import java.util.List;

public class AllOffersActivity extends ActionBarActivity implements AsyncTaskCompleteListener<List<OfferListItem>> {

	// Progress Dialog
	private ProgressDialog pDialog;

	// manages all of our offers in a list.
	private List<OfferListItem> offerList;
	
	private OfferDAO offertDAO = new OfferDAO();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_offers);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.alloffers_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_search:
			// openSearch();
			return true;
		case R.id.action_settings:
			// openSettings();
			return true;
		case R.id.action_myoffers:
			Intent i = new Intent(AllOffersActivity.this, MyOffersActivity.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// loading the offers via AsyncTask
		pDialog = new ProgressDialog(AllOffersActivity.this);
		pDialog.setMessage("Loading Offers...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
		offertDAO.getAllOffers(this);
	}

	public void addOffer(View v) {
		Intent i = new Intent(AllOffersActivity.this, AddOfferActivity.class);
		startActivity(i);
	}

	@Override
	public void onTaskComplete(List<OfferListItem> result) {
		offerList = result;
		updateList();
		pDialog.dismiss();
	}

	@Override
	public void onTaskError(String errorString) {
		pDialog.dismiss();
		Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * Inserts the parsed data into the listview.
	 */
	private void updateList() {

		OfferItemListAdapter adapter = new OfferItemListAdapter(this, offerList);
		final ListView listview = (ListView) findViewById(R.id.list);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(AllOffersActivity.this, OfferDetailActivity.class);
				OfferListItem item = offerList.get(position);
				intent.putExtra("offer_id", item.getId());
				startActivity(intent);
			}
		});
	}
}
