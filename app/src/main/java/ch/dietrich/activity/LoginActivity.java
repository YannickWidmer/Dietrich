package ch.dietrich.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ch.dietrich.database.AsyncTaskCompleteListener;
import ch.dietrich.database.UserDAO;
import ch.dietrich.dietrichapplication.R;
import ch.dietrich.entity.User;

public class LoginActivity extends Activity implements OnClickListener, AsyncTaskCompleteListener<User>{

	private EditText user, pass;
	private Button mSubmit, mRegister;

	// Progress Dialog
	private ProgressDialog pDialog;

	private UserDAO userDAO = new UserDAO();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// setup input fields
		user = (EditText) findViewById(R.id.username);
		pass = (EditText) findViewById(R.id.password);
		
		user.setText("david1@gmx.ch");
		pass.setText("test");

		// setup buttons
		mSubmit = (Button) findViewById(R.id.login);
		mRegister = (Button) findViewById(R.id.register);

		// register listeners
		mSubmit.setOnClickListener(this);
		mRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		// NO Auth
		Intent i = new Intent(LoginActivity.this, AllOffersActivity.class);
		finish();
		startActivity(i);

		/*With auth
		switch (v.getId()) {
		case R.id.login:
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Attempting login...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			String username = user.getText().toString();
			String password = pass.getText().toString();
			userDAO.getUser(username, password, this);
			break;
		case R.id.register:
			Intent i = new Intent(this, RegisterActivity.class);
			startActivity(i);
			break;
		default:
			break;
		}
		*/
	}

	@Override
	public void onTaskComplete(User result) {
		pDialog.dismiss();

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(LoginActivity.this);
		Editor edit = sp.edit();
		edit.putString("apiKey", result.getApiKey());
		edit.commit();
		Intent i = new Intent(LoginActivity.this, AllOffersActivity.class);
		finish();
		startActivity(i);		
	}

	@Override
	public void onTaskError(String errorString) {
		pDialog.dismiss();
		Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_LONG).show();
	}
}
