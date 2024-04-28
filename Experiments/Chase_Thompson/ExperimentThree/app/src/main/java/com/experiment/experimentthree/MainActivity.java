package com.experiment.experimentthree;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initialize activity components and requestQueue
		RequestQueue queue = Volley.newRequestQueue(this);
		TextView responseDisplay = findViewById(R.id.volleyRequest);
		Button requestButton = findViewById(R.id.volleyRequestButton);

		// URL to send the request to
		String url ="https://www.google.com";

		// When the button is pressed send the request
		requestButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Create the request
				StringRequest request = new StringRequest(Request.Method.GET, url,
						new Response.Listener<String>() {
							@Override
							public void onResponse(String response) {
								responseDisplay.setText(response);
							}
						}, new Response.ErrorListener() {
					@SuppressLint("SetTextI18n")
					@Override
					public void onErrorResponse(VolleyError error) {
						responseDisplay.setText("Ran into an Error: \n" + error);
					}
				});

				// Add the request to the queue
				queue.add(request);
			}
		});
	}
}