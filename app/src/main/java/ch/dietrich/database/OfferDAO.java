package ch.dietrich.database;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.appdatasearch.GetRecentContextCall;

import ch.dietrich.dietrichapplication.AppController;
import ch.dietrich.entity.Offer;
import ch.dietrich.entity.OfferListItem;
import ch.dietrich.entity.OfferType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.dietrich.entity.Offer;
import ch.dietrich.entity.OfferListItem;
import ch.dietrich.entity.OfferType;

public class OfferDAO extends DAOBase {

	private static final String ADD_OFFER_URL = MAIN_URL + "offers";
	private static final String READ_ALLOFFERS_URL = MAIN_URL + "offers";
	private static final String READ_USER_OFFERS_URL = MAIN_URL + "myoffers";
	private static final String GET_OFFERTYPES = MAIN_URL + "offertypes";
	private static final String GET_OFFERBYID = MAIN_URL + "offer/";

	private static final String TAG_OFFERS = "offers";
	private static final String TAG_OFFER = "offer";
	private static final String TAG_OFFERTYPES = "types";

	private static final String TAG_CLUB_NAME = "club_name";
	private static final String TAG_CLUB_ID = "club_id";
	private static final String TAG_CITY = "club_city";
	private static final String TAG_OFFER_ID = "id";
	private static final String TAG_USER_ID = "user_id";
	private static final String TAG_USERNAME = "username";
	private static final String TAG_OFFERTYPE = "type";
	private static final String TAG_OFFER_TYPEID = "type_id";
	private static final String TAG_DESCRIPTION = "description";

	private static final String TAG_OFFERTYPE_ID = "id";
	private static final String TAG_OFFERTYPE_NAME = "name";
	private static final String TAG_OFFERTYPE_HASPRICE = "hasprice";

	private AsyncTaskCompleteListener<List<OfferListItem>> getAllOffersListener;
	private AsyncTaskCompleteListener<Offer> getOfferByIDListener;
	private AsyncTaskCompleteListener<List<OfferType>> getOfferTypesListener;
	private AsyncTaskCompleteListener<Integer> addOfferListener;

	public void getAllOffers(
			AsyncTaskCompleteListener<List<OfferListItem>> listener) {

		getAllOffersListener = listener;

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
				READ_ALLOFFERS_URL, new JSONObject(),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						ArrayList<OfferListItem> items = new ArrayList<OfferListItem>();
						try {
							if (response.getBoolean("error")) {
								getAllOffersListener
										.onTaskError("Error occurred while loading the offers.");
							} else {
								JSONArray offers = response
										.getJSONArray(TAG_OFFERS);

								// looping through all offers according to the
								// json object
								// returned
								for (int i = 0; i < offers.length(); i++) {
									JSONObject c = offers.getJSONObject(i);
									OfferListItem item = parseOfferListItem(c);
									items.add(item);
								}
								getAllOffersListener.onTaskComplete(items);
							}

						} catch (JSONException e) {
							e.printStackTrace();
							getAllOffersListener.onTaskError(e.getMessage());
						}
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						getAllOffersListener.onTaskError("Error: "
								+ error.getMessage());
					}
				}) {

			/**
			 * Passing some request headers
			 * */
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Content-Type", "application/json; charset=utf-8");
				return headers;
			}
		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq);
	}

	public void getOffers(final String apiKey,
			AsyncTaskCompleteListener<List<OfferListItem>> listener) {

		getAllOffersListener = listener;

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
				READ_USER_OFFERS_URL, new JSONObject(),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						ArrayList<OfferListItem> items = new ArrayList<OfferListItem>();
						try {
							Log.v("test", response.toString());
							if (response.getBoolean("error")) {
								getAllOffersListener
										.onTaskError("Error occurred while loading the offers.");
							} else {
								JSONArray offers = response
										.getJSONArray(TAG_OFFERS);

								// looping through all offers according to the
								// json object
								// returned
								for (int i = 0; i < offers.length(); i++) {
									JSONObject c = offers.getJSONObject(i);
									OfferListItem item = parseOfferListItem(c);
									items.add(item);
								}
								getAllOffersListener.onTaskComplete(items);
							}

						} catch (JSONException e) {
							e.printStackTrace();
							getAllOffersListener.onTaskError(e.getMessage());
						}
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						getAllOffersListener.onTaskError("Error: "
								+ error.getMessage());
					}
				}) {

			/**
			 * Passing some request headers
			 * */
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Content-Type", "application/json; charset=utf-8");
				headers.put("Authorization", apiKey);
				return headers;
			}
		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq);
	}

	public void addOffer(Offer offer, final String apiKey,
			AsyncTaskCompleteListener<Integer> listener) {

		addOfferListener = listener;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(TAG_CLUB_ID, Integer.toString(offer.getClubid()));
		params.put(TAG_DESCRIPTION, offer.getDescription());
		params.put(TAG_OFFER_TYPEID, Integer.toString(offer.getTypeid()));

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
				ADD_OFFER_URL, new JSONObject(params),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							if (response.getBoolean("error")) {
								addOfferListener
										.onTaskError("Error occurred while loading the offers.");
							} else {
								int club = response.getInt("offer_id");
								addOfferListener.onTaskComplete(club);
							}
						} catch (JSONException e) {
							e.printStackTrace();
							addOfferListener.onTaskError(e.getMessage());
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						addOfferListener.onTaskError("Error: "
								+ error.getMessage());
					}
				}) {

			/**
			 * Passing some request headers
			 * */
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Content-Type", "application/json; charset=utf-8");
				headers.put("Authorization", apiKey);
				return headers;
			}
		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq);
	}

	public void getOfferTypes(
			AsyncTaskCompleteListener<List<OfferType>> listener) {
		getOfferTypesListener = listener;
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
				GET_OFFERTYPES, new JSONObject(),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							if (response.getBoolean("error")) {
								getOfferTypesListener
										.onTaskError("Error occurred while loading the offers.");
							} else {
								ArrayList<OfferType> types = new ArrayList<OfferType>();
								JSONArray typesArray = response
										.getJSONArray(TAG_OFFERTYPES);
								for (int i = 0; i < typesArray.length(); i++) {
									JSONObject c = typesArray.getJSONObject(i);
									String id = c.getString(TAG_OFFERTYPE_ID);
									String name = c
											.getString(TAG_OFFERTYPE_NAME);
									int hasprice = c
											.getInt(TAG_OFFERTYPE_HASPRICE);

									OfferType type = new OfferType();
									type.setId(Integer.parseInt(id));
									type.setName(name);
									type.setHasPrice(hasprice == 1);
									types.add(type);
								}
								getOfferTypesListener.onTaskComplete(types);
							}
						} catch (JSONException e) {
							e.printStackTrace();
							getOfferTypesListener.onTaskError(e.getMessage());
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						getOfferTypesListener.onTaskError("Error: "
								+ error.getMessage());
					}
				}) {

			/**
			 * Passing some request headers
			 * */
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Content-Type", "application/json; charset=utf-8");
				return headers;
			}
		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq);
	}

	public void getOfferByID(final int inClubID,
			AsyncTaskCompleteListener<Offer> listener) {
		getOfferByIDListener = listener;
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
				GET_OFFERBYID + inClubID, new JSONObject(),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							if (response.getBoolean("error")) {
								getOfferByIDListener
										.onTaskError("Error occurred while loading the offers.");
							} else {
								JSONObject offerobject = response
										.getJSONObject(TAG_OFFER);
								Offer offer = parseOffer(offerobject);
								getOfferByIDListener.onTaskComplete(offer);
							}
						} catch (JSONException e) {
							e.printStackTrace();
							getOfferByIDListener.onTaskError(e.getMessage());
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						getOfferByIDListener.onTaskError("Error: "
								+ error.getMessage());
					}
				}) {

			/**
			 * Passing some request headers
			 * */
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Content-Type", "application/json; charset=utf-8");
				return headers;
			}
		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq);
	}

	private Offer parseOffer(JSONObject c) throws JSONException {

		Integer id = c.getInt(TAG_OFFER_ID);
		int userid = c.getInt(TAG_USER_ID);
		int clubid = c.getInt(TAG_CLUB_ID);
		String description = c.getString(TAG_DESCRIPTION);
		String offerTypename = c.getString(TAG_OFFERTYPE);
		int typeid = c.getInt(TAG_OFFER_TYPEID);

		Offer offer = new Offer();
		offer.setId(id);
		offer.setUserid(userid);
		offer.setClubid(clubid);
		offer.setDescription(description);
		offer.setTypename(offerTypename);
		offer.setTypeid(typeid);

		return offer;
	}

	private OfferListItem parseOfferListItem(JSONObject c) throws JSONException {
		// gets the content of each tag
		String clubname = c.getString(TAG_CLUB_NAME);
		int id = c.getInt(TAG_OFFER_ID);
		String city = c.getString(TAG_CITY);
		String type = c.getString(TAG_OFFERTYPE);
		String username = c.getString(TAG_USERNAME);

		OfferListItem item = new OfferListItem();
		item.setId(id);
		item.setClubname(clubname + ", " + city);
		item.setType(type);
		item.setUsername(username);
		return item;
	}
}
