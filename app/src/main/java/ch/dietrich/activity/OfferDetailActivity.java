package ch.dietrich.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import ch.dietrich.database.AsyncTaskCompleteListener;
import ch.dietrich.database.ClubDAO;
import ch.dietrich.database.OfferDAO;
import ch.dietrich.database.UserDAO;
import ch.dietrich.dietrichapplication.R;
import ch.dietrich.entity.Club;
import ch.dietrich.entity.Offer;
import ch.dietrich.entity.UserListItem;

public class OfferDetailActivity extends ActionBarActivity implements
		OnClickListener, AsyncTaskCompleteListener<Offer> {

	// Progress Dialog
	private ProgressDialog pDialog;

	private OfferDAO offertDAO = new OfferDAO();
	private ClubDAO clubDAO = new ClubDAO();
	private UserDAO userDAO = new UserDAO();

	private Offer offer = new Offer();
	private Club club = new Club();
	private UserListItem user = new UserListItem();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offer_detail);
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
		case R.id.action_myoffers: {
			Intent i = new Intent(OfferDetailActivity.this,
					MyOffersActivity.class);
			startActivity(i);
			return true;
		}
		case R.id.action_alloffers: {
			Intent i = new Intent(OfferDetailActivity.this,
					AllOffersActivity.class);
			startActivity(i);
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		pDialog = new ProgressDialog(OfferDetailActivity.this);
		pDialog.setMessage("Loading Data...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
		Bundle bundle = getIntent().getExtras();
		int offerid = bundle.getInt("offer_id");
		offertDAO.getOfferByID(offerid, this);
	}

	/**
	 * Retrieves recent offer data from the server.
	 */
	public void updateOfferData() {
		clubDAO.getClubByID(offer.getClubid(),
				new AsyncTaskCompleteListener<Club>() {

					@Override
					public void onTaskComplete(Club result) {
						club = result;
						if (user != null && pDialog != null) {
							pDialog.dismiss();
							updateView();
						}
					}

					@Override
					public void onTaskError(String errorString) {
						OfferDetailActivity.this.onTaskError(errorString);
					}
				});

		userDAO.getUserByID(offer.getUserid(),
				new AsyncTaskCompleteListener<UserListItem>() {

					@Override
					public void onTaskComplete(UserListItem result) {
						user = result;
						if (club != null && pDialog != null) {
							pDialog.dismiss();
							updateView();
						}
					}

					@Override
					public void onTaskError(String errorString) {
						OfferDetailActivity.this.onTaskError(errorString);
					}
				});
	}

	/**
	 * Inserts the parsed data into the listview.
	 */
	private void updateView() {
		TextView titleView = (TextView) findViewById(R.id.offer_detailview_titel_textEdit);
		titleView.setText(club.getName() + ", " + club.getCity());
		TextView usernameView = (TextView) findViewById(R.id.offer_detailview_username_textEdit);
		usernameView.setText(user.getUsername());
		TextView typeView = (TextView) findViewById(R.id.offer_detailview_type_textEdit);
		typeView.setText(offer.getTypename());
		TextView descriptionView = (TextView) findViewById(R.id.offer_detailview_description_textEdit);
		descriptionView.setText(offer.getDescription());
	}

	@Override
	public void onTaskComplete(Offer result) {
		offer = result;
		updateOfferData();
	}

	@Override
	public void onTaskError(String errorString) {
		pDialog.dismiss();
		Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_LONG)
				.show();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
