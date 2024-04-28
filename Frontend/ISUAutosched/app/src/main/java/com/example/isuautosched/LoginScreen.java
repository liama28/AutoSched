package com.example.isuautosched;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

public class LoginScreen extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);
		EditText username = findViewById(R.id.usernameInput);
		EditText password = findViewById(R.id.passwordInput);
		Button submit = findViewById(R.id.submit);
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String name = username.getText().toString();
				String pass = password.getText().toString();

				RequestQueue queue = Volley.newRequestQueue(LoginScreen.this);
				JSONObject data = new JSONObject();
				try {
					data.put("name",name);
					data.put("password",pass);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.BASE_URL + "user/login", data, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						System.out.println(response);
						try {
							CurrentUser.setId(response.getInt("userid"));
							CurrentUser.setSessionId(response.getInt("sessionid"));
						} catch (JSONException e) {
							e.printStackTrace();
						}
						Intent i = new Intent(LoginScreen.this,MainMenuScreen.class);
						startActivity(i);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginScreen.this);
						alertDialogBuilder.setTitle("Error");
						alertDialogBuilder.setMessage(error.getMessage());
						alertDialogBuilder.setPositiveButton("Ok", null);
						alertDialogBuilder.setNegativeButton("", null);
						alertDialogBuilder.create().show();
					}
				});
				queue.add(request);
			}
		});
	}
}