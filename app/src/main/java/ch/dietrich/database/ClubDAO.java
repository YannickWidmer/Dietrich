package ch.dietrich.database;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import ch.dietrich.dietrichapplication.AppController;
import ch.dietrich.entity.Club;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClubDAO extends DAOBase {

	private static final String GET_CLUBS_URL = MAIN_URL + "clubs/";
	private static final String GET_CITIES_URL = MAIN_URL + "clubcities/";
	private static final String GET_COUNTRIES_URL = MAIN_URL + "clubcountries";
	private static final String GET_CLUBBYID_URL = MAIN_URL + "club/";

	private static final String TAG_CLUBS = "clubs";
	private static final String TAG_CLUB = "club";
	private static final String TAG_CITIES = "cities";
	private static final String TAG_CONTRIES = "countries";

	private static final String TAG_CLUB_NAME = "name";
	private static final String TAG_CLUB_ID = "id";
	private static final String TAG_CLUB_ADDRESS = "address";
	private static final String TAG_CLUB_CITY = "city";
	private static final String TAG_CLUB_ZIP = "zip";
	private static final String TAG_CLUB_COUNTRY = "country";
	private static final String TAG_CLUB_TEMPORARY = "temporary";

	private AsyncTaskCompleteListener<List<String>> getCountriesListener;
	private AsyncTaskCompleteListener<List<String>> getCitiesListener;
	private AsyncTaskCompleteListener<List<Club>> getAllClubsListener;
	private AsyncTaskCompleteListener<Club> getClubByIDListener;

	public void getClubByID(final int inClubID,
			AsyncTaskCompleteListener<Club> listener) {
		getClubByIDListener = listener;
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
				GET_CLUBBYID_URL + inClubID, new JSONObject(),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							if (response.getBoolean("error")) {
								getClubByIDListener
										.onTaskError("Error occurred while loading the offers.");
							} else {
								Club club = parseClub(response
										.getJSONObject(TAG_CLUB));
								getClubByIDListener.onTaskComplete(club);
							}
						} catch (JSONException e) {
							e.printStackTrace();
							getClubByIDListener.onTaskError(e.getMessage());
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						getClubByIDListener.onTaskError("Error: "
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

	public void getClubs(String inCountry, String inCity,
			AsyncTaskCompleteListener<List<Club>> listener) {
		getAllClubsListener = listener;
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
				GET_CLUBS_URL + inCountry + "/" + inCity, new JSONObject(),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						ArrayList<Club> items = new ArrayList<Club>();
						try {
							Log.v("test", response.toString());
							if (response.getBoolean("error")) {
								getAllClubsListener
										.onTaskError("Error occurred while loading the offers.");
							} else {
								JSONArray cities = response
										.getJSONArray(TAG_CLUBS);
								for (int i = 0; i < cities.length(); i++) {
									items.add(parseClub(cities.getJSONObject(i)));
								}
								getAllClubsListener.onTaskComplete(items);
							}
						} catch (JSONException e) {
							e.printStackTrace();
							getAllClubsListener.onTaskError(e.getMessage());
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						getAllClubsListener.onTaskError("Error: "
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

	private Club parseClub(JSONObject c) throws JSONException {
		// gets the content of each tag
		Integer id = c.getInt(TAG_CLUB_ID);
		String clubname = c.getString(TAG_CLUB_NAME);
		String address = c.getString(TAG_CLUB_ADDRESS);
		String city = c.getString(TAG_CLUB_CITY);
		String zip = c.getString(TAG_CLUB_ZIP);
		String country = c.getString(TAG_CLUB_COUNTRY);
		boolean temporary = c.getInt(TAG_CLUB_TEMPORARY) == 1;

		Club club = new Club();
		club.setId(id);
		club.setName(clubname);
		club.setAddress(address);
		club.setCity(city);
		club.setZip(zip);
		club.setCountry(country);
		club.setTemproary(temporary);
		return club;
	}

	public void getCities(String inCountry,
			AsyncTaskCompleteListener<List<String>> listener) {
		getCitiesListener = listener;
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
				GET_CITIES_URL + inCountry, new JSONObject(),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						ArrayList<String> items = new ArrayList<String>();
						try {
							if (response.getBoolean("error")) {
								getCitiesListener
										.onTaskError("Error occurred while loading the offers.");
							} else {
								JSONArray cities = response
										.getJSONArray(TAG_CITIES);
								for (int i = 0; i < cities.length(); i++) {
									items.add(cities.getString(i));
								}
								getCitiesListener.onTaskComplete(items);
							}
						} catch (JSONException e) {
							e.printStackTrace();
							getCitiesListener.onTaskError(e.getMessage());
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						getCitiesListener.onTaskError("Error: "
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

	public void getCountries(AsyncTaskCompleteListener<List<String>> listener) {
		getCountriesListener = listener;
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
				GET_COUNTRIES_URL, new JSONObject(),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						ArrayList<String> items = new ArrayList<String>();
						try {
							if (response.getBoolean("error")) {
								getCountriesListener
										.onTaskError("Error occurred while loading the offers.");
							} else {
								JSONArray countries = response
										.getJSONArray(TAG_CONTRIES);
								for (int i = 0; i < countries.length(); i++) {
									items.add(countries.getString(i));
								}
								getCountriesListener.onTaskComplete(items);
							}
						} catch (JSONException e) {
							e.printStackTrace();
							getCountriesListener.onTaskError(e.getMessage());
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						getCountriesListener.onTaskError("Error: "
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
}
