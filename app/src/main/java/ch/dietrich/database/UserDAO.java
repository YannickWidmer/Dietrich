package ch.dietrich.database;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import ch.dietrich.dietrichapplication.AppController;
import ch.dietrich.entity.User;
import ch.dietrich.entity.UserListItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserDAO extends DAOBase {

	private static final String LOGIN_URL = MAIN_URL + "login";
	private static final String GET_USERBYID = MAIN_URL + "user/";

	private static final String TAG_USERNAME = "username";
	private static final String TAG_USER_ID = "id";
	private static final String TAG_USER = "user";
	private static final String TAG_MALE = "male";

	private AsyncTaskCompleteListener<User> getUserListener;
	private AsyncTaskCompleteListener<UserListItem> getUserByIDListener;

	public void getUser(String username, String password,
			AsyncTaskCompleteListener<User> listener) {

		getUserListener = listener;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("email", username);
		params.put("password", password);

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
				LOGIN_URL, new JSONObject(params),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						try {
							String email = response.getString("email");
							String apiKey = response.getString("apiKey");

							User user = new User();
							user.setEmail(email);
							user.setApiKey(apiKey);
							getUserListener.onTaskComplete(user);

						} catch (JSONException e) {
							e.printStackTrace();
							getUserListener.onTaskError(e.getMessage());
						}
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						getUserListener.onTaskError("Error: "
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

	public void getUserByID(final int inUserID,
			AsyncTaskCompleteListener<UserListItem> listener) {
		getUserByIDListener = listener;
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
				GET_USERBYID + inUserID, new JSONObject(),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							if (response.getBoolean("error")) {
								getUserByIDListener
										.onTaskError("Error occurred while loading the offers.");
							} else {
								UserListItem user = parseUserListItem(response
										.getJSONObject(TAG_USER));
								getUserByIDListener.onTaskComplete(user);
							}
						} catch (JSONException e) {
							e.printStackTrace();
							getUserByIDListener.onTaskError(e.getMessage());
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						getUserByIDListener.onTaskError("Error: "
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

	private UserListItem parseUserListItem(JSONObject c) throws JSONException {
		UserListItem user = new UserListItem();

		int userid = c.getInt(TAG_USER_ID);
		String username = c.getString(TAG_USERNAME);
		boolean male = c.getInt(TAG_MALE) == 1;

		user.setId(userid);
		user.setUsername(username);
		user.setMale(male);

		return user;
	}
}
