package ch.dietrich.activity;


import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.List;

import ch.dietrich.adapter.OfferTypeListAdapter;
import ch.dietrich.database.AsyncTaskCompleteListener;
import ch.dietrich.database.OfferDAO;
import ch.dietrich.dietrichapplication.R;
import ch.dietrich.entity.Offer;
import ch.dietrich.entity.OfferType;


public class AddOfferActivity extends FragmentActivity implements OnClickListener {
	private static final String LOG = "AddOfferActivity";

    private static final int PLACE_PICKER_REQUEST = 1;
	private EditText descriptionTextEdit;
	private Button mSubmit, mPlace;
	private GoogleMap mMap; // Might be null if Google Play services APK is not available.

	private ProgressDialog loadingTypesDialog;
	private ProgressDialog addingOfferDialog;

	private OfferDAO offertDAO = new OfferDAO();

	private List<OfferType> types;

	private Spinner typeSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.add_offer);
        setUpMapIfNeeded();



		descriptionTextEdit = (EditText) findViewById(R.id.description);



		typeSpinner = (Spinner) findViewById(R.id.type_spinner);



		mSubmit = (Button) findViewById(R.id.submit);
		mSubmit.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		loadingTypesDialog = new ProgressDialog(AddOfferActivity.this);
		loadingTypesDialog.setMessage("Loading Types...");
		loadingTypesDialog.setIndeterminate(false);
		loadingTypesDialog.setCancelable(true);
		loadingTypesDialog.show();
        setUpMapIfNeeded();
		offertDAO
				.getOfferTypes(new AsyncTaskCompleteListener<List<OfferType>>() {

                    @Override
                    public void onTaskComplete(List<OfferType> result) {
                        types = result;
                        updateOfferTypeList();
                        loadingTypesDialog.dismiss();
                    }

                    @Override
                    public void onTaskError(String errorString) {
                        loadingTypesDialog.dismiss();
                        Toast.makeText(getApplicationContext(), errorString,
                                Toast.LENGTH_LONG).show();
                    }
                });
	}

	@Override
	public void onClick(View v) {
		addingOfferDialog = new ProgressDialog(AddOfferActivity.this);
		addingOfferDialog.setMessage("Adding Offer...");
		addingOfferDialog.setIndeterminate(false);
		addingOfferDialog.setCancelable(true);
		addingOfferDialog.show();
		
		String description = descriptionTextEdit.getText().toString();

		Offer offer = new Offer();

		offer.setDescription(description);
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(AddOfferActivity.this);
		String apiKey = sp.getString("apiKey", "");

		offertDAO.addOffer(offer, apiKey, new AsyncTaskCompleteListener<Integer>() {

            @Override
            public void onTaskComplete(Integer result) {
                addingOfferDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Offer successfully added!", Toast.LENGTH_LONG)
                        .show();
                Intent i = new Intent(AddOfferActivity.this, MyOffersActivity.class);
                finish();
                startActivity(i);
            }

            @Override
            public void onTaskError(String errorString) {
                addingOfferDialog.dismiss();
                Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_LONG)
                        .show();
            }

        });
	}



	private void updateOfferTypeList() {
		OfferTypeListAdapter adapter = new OfferTypeListAdapter(this,
				R.layout.offertype_spinner_item, types);
		typeSpinner.setAdapter(adapter);
	}




	/**
	 * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
	 * installed) and the map has not already been instantiated.. This will ensure that we only ever
	 * call {@link #setUpMap()} once when {@link #mMap} is not null.
	 * <p/>
	 * If it isn't installed {@link SupportMapFragment} (and
	 * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
	 * install/update the Google Play services APK on their device.
	 * <p/>
	 * A user can return to this FragmentActivity after following the prompt and correctly
	 * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
	 * have been completely destroyed during this process (it is likely that it would only be
	 * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
	 * method in {@link #onResume()} to guarantee that it will be called.
	 */
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.

		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
					.getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	/**
	 * This is where we can add markers or lines, add listeners or move the camera. In this case, we
	 * just add a marker near Africa.
	 * <p/>
	 * This should only be called once and when we are sure that {@link #mMap} is not null.
	 */
	private void setUpMap() {
        mMap.setBuildingsEnabled(true);
        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setMapToolbarEnabled(true);
		mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latLng) {

				PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
				Log.d(LOG,"creating picker" );
				try {
					startActivityForResult(builder.build(AddOfferActivity.this), PLACE_PICKER_REQUEST);
				} catch (GooglePlayServicesRepairableException e) {
					e.printStackTrace();
				} catch (GooglePlayServicesNotAvailableException e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PLACE_PICKER_REQUEST) {
			if (resultCode == RESULT_OK) {
				Place place = PlacePicker.getPlace(data, this);
				MarkerOptions options = new MarkerOptions();
				options.anchor((float)place.getLatLng().latitude,(float)place.getLatLng().longitude);
				mMap.addMarker(options);
			}
		}
	}
}
