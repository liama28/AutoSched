package com.example.isuautosched;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;

public class MainMenuScreen extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);

		/* BUTTON JUNK */
		ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsButton);
		settingsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(MainMenuScreen.this, SettingScreen.class);
				startActivity(i);
			}
		});

		Button addFriendScreen = (Button) findViewById(R.id.addFriendScreenButton);
		addFriendScreen.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(MainMenuScreen.this, AddFriendScreen.class);
				startActivity(i);
			}
		});

		ImageButton HamburgerMenu = findViewById(R.id.hamburger);
		HamburgerMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(MainMenuScreen.this, HamburgerMenu.class);
				startActivity(i);
			}
		});

		Button createGroup = (Button) findViewById(R.id.createGroupButton);
		createGroup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(MainMenuScreen.this, CreateGroup.class);
				startActivity(i);
			}
		});

		System.out.println("sessionid: " + CurrentUser.getSessionId());
		/* LOAD FRIENDS AND GROUPS */
		// Request Data
		RequestQueue queue = Volley.newRequestQueue(MainMenuScreen.this);

		// Get Groups
		JsonArrayRequest groupListRequest = new JsonArrayRequest(Request.Method.GET, Constants.BASE_URL + "user/" + CurrentUser.getId() + "/groups", null, new Response.Listener<org.json.JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				System.out.println();

				// Display groups
				if (response.length() > 0) {
					findViewById(R.id.noGroupsText).setVisibility(View.GONE);
					HorizontalScrollView groupsDisplay = (HorizontalScrollView) findViewById(R.id.groupsDisplay);
					LinearLayout groupsLinear = new LinearLayout(MainMenuScreen.this);

					// Setup Layout for the LinearLayout
					float scale = MainMenuScreen.this.getResources().getDisplayMetrics().density;
					groupsLinear.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
					groupsLinear.setOrientation(LinearLayout.HORIZONTAL);

					// Generate friends circles
					for (int i = 0; i < response.length(); i++) {
						// Attempt to generate the button
						try {
							groupsLinear.addView(generateCircleButton(response.getJSONObject(i).getString("name"), scale));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					// Make display visible
					groupsDisplay.addView(groupsLinear);
					groupsDisplay.setVisibility(View.VISIBLE);
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainMenuScreen.this);
				alertDialogBuilder.setTitle("Error");
				alertDialogBuilder.setMessage(error.getMessage());
				alertDialogBuilder.setPositiveButton("Ok", null);
				alertDialogBuilder.setNegativeButton("", null);
				alertDialogBuilder.create().show();
			}
		});
		queue.add(groupListRequest);

		// Get Friends
		JsonArrayRequest friendListRequest = new JsonArrayRequest(Request.Method.GET, Constants.BASE_URL + "user/" + CurrentUser.getId() + "/friends", null, new Response.Listener<org.json.JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				System.out.println(response);

				if (response.length() > 0) {
					// Display friends
					findViewById(R.id.noFriendsText).setVisibility(View.GONE);
					HorizontalScrollView friendsDisplay = (HorizontalScrollView) findViewById(R.id.friendsDisplay);
					LinearLayout friendsLinear = new LinearLayout(MainMenuScreen.this);

					// Setup Layout for the LinearLayout
					float scale = MainMenuScreen.this.getResources().getDisplayMetrics().density;
					friendsLinear.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
					friendsLinear.setOrientation(LinearLayout.HORIZONTAL);

					// Generate friends circles
					for (int i = 0; i < response.length(); i++) {
						// Attempt to generate the button
						try {
							friendsLinear.addView(generateCircleButton(response.getJSONObject(i).getString("name"), scale));
							View spacer = new View(MainMenuScreen.this);
							spacer.setLayoutParams(new LinearLayout.LayoutParams((int)(10*scale), (int)(10*scale)));
							friendsLinear.addView(spacer);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					// Make display visible
					friendsDisplay.addView(friendsLinear);
					friendsDisplay.setVisibility(View.VISIBLE);
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainMenuScreen.this);
				alertDialogBuilder.setTitle("Error");
				alertDialogBuilder.setMessage(error.getMessage());
				alertDialogBuilder.setPositiveButton("Ok", null);
				alertDialogBuilder.setNegativeButton("", null);
				alertDialogBuilder.create().show();
			}
		});
		queue.add(friendListRequest);
	}

	int starts = 0;
	@Override
	protected void onStart() {
		// Reload the screen
		super.onStart();
		starts++;
		if (starts > 1) {
			finish();
			startActivity(getIntent());
		}
	}

	/**
	 * Generates a circle button for names and groups
	 * @param name The name to be displayed in the circle
	 * @param scale The scale of the screen
	 * @return The circle button
	 */
	MaterialButton generateCircleButton(String name, float scale) {
		// Create circle for friend
		MaterialButton friendButton = new MaterialButton(MainMenuScreen.this);

		// Create layout params
		friendButton.setLayoutParams(new LinearLayout.LayoutParams((int)(100*scale), (int)(100*scale)));
		friendButton.setPadding(0,0,10,0);
		GradientDrawable shape = new GradientDrawable();
		shape.setCornerRadius((int)(50*scale));
		shape.setColor(Color.rgb(255,138,138));
		friendButton.setBackground(shape);
		friendButton.setText(name);

		return friendButton;
	}
}