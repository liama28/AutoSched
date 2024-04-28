package com.example.isuautosched;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateAccountScreen extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);

		RequestQueue queue = Volley.newRequestQueue(this);
		Button createAccountButton = findViewById(R.id.createAccountFinish);
		createAccountButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// :O
				JSONObject data = new JSONObject();
				try {
					data.put("name", ((EditText) findViewById(R.id.createAccountUserName)).getText().toString());
					data.put("password", ((EditText) findViewById(R.id.createAccountPassword)).getText().toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.BASE_URL+"user/save", data, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						System.out.println(response);
						try {
							CurrentUser.setId(response.getInt("userid"));
							CurrentUser.setSessionId(response.getInt("sessionid"));
						} catch (JSONException e) {
							e.printStackTrace();
						}
						Intent i = new Intent(CreateAccountScreen.this, MainMenuScreen.class);
						startActivity(i);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateAccountScreen.this);
						alertDialogBuilder.setTitle("Error");
						alertDialogBuilder.setMessage(error.getMessage());
						alertDialogBuilder.setPositiveButton("Ok", null);
						alertDialogBuilder.setNegativeButton("", null);
						alertDialogBuilder.create().show();
					}
				});

				// Add the request to the queue
				queue.add(jsonObjectRequest);

				// >:O
			}
		});
	}
}
