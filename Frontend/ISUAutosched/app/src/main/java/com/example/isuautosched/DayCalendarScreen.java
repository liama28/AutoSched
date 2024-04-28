package com.example.isuautosched;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.type.DateTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

public class DayCalendarScreen extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_day_calendar);

		ZoneId zoneId = ZoneId.systemDefault();
		LocalDateTime dateTime = LocalDateTime.now();

		/* MOCK DATA */
//		JSONArray mockData = new JSONArray();
//		try {
//			JSONObject data = new JSONObject();
//			data.put("name", "testname1");
//			data.put("start", "2021-12-10 16:00");
//			data.put("end", "2021-12-10 16:40");
//			mockData.put(data);
//			data = new JSONObject();
//			data.put("name", "testname2");
//			data.put("start", "2021-12-10 17:00");
//			data.put("end", "2021-12-10 17:40");
//			mockData.put(data);
//		} catch(JSONException e) {
//			e.printStackTrace();
//		}
//
//		LinearLayout eventList = (LinearLayout) findViewById(R.id.eventList);
//		// Generate conflict circles
//		for (int i = 0; i < mockData.length(); i++) {
//			// Attempt to generate the button
//			try {
//				// Format times
//				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-dd HH:mm", Locale.ENGLISH);
//				LocalDateTime start = LocalDateTime.parse(mockData.getJSONObject(i).getString("start"), formatter);
//				LocalDateTime end = LocalDateTime.parse(mockData.getJSONObject(i).getString("end"), formatter);
//				eventList.addView(generateConflictBubble(mockData.getJSONObject(i).getString("name"), start, end));
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		/* MOCK DATA */

		// Get Conflicts
		JSONObject sessionId = new JSONObject();
		try {
			sessionId.put("userid",CurrentUser.getId());
			sessionId.put("sessionid",CurrentUser.getSessionId());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		RequestQueue queue = Volley.newRequestQueue(DayCalendarScreen.this);
		CustomJsonArrayRequest groupListRequest = new CustomJsonArrayRequest(Request.Method.POST, Constants.BASE_URL + "calendar/day/" + dateTime.atZone(zoneId).toEpochSecond(), sessionId, new Response.Listener<org.json.JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				System.out.println(response);
				// Display conflicts
				if (response.length() > 0) {
					LinearLayout eventList = (LinearLayout) findViewById(R.id.eventList);
					// Generate conflict circles
					for (int i = 0; i < response.length(); i++) {
						// Attempt to generate the button
						try {
							// Format times
							String startTime = response.getJSONObject(i).getString("start");
							String[] stuff = startTime.split("T", 2);
							String endTime = response.getJSONObject(i).getString("end");
							String[] otherStuff = endTime.split("T", 2);
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-dd HH:mm", Locale.ENGLISH);
							LocalDateTime start = LocalDateTime.parse(stuff[0] + " " + stuff[1], formatter);
							LocalDateTime end = LocalDateTime.parse(otherStuff[0] + " " + otherStuff[1], formatter);
							eventList.addView(generateConflictBubble(response.getJSONObject(i).getString("name"), start, end));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DayCalendarScreen.this);
				alertDialogBuilder.setTitle("Error");
				alertDialogBuilder.setMessage(error.getMessage());
				alertDialogBuilder.setPositiveButton("Ok", null);
				alertDialogBuilder.setNegativeButton("", null);
				alertDialogBuilder.create().show();
			}
		});
		queue.add(groupListRequest);
	}

	LinearLayout generateConflictBubble(String name, LocalDateTime start, LocalDateTime end) {
		LinearLayout conflictBubble = new LinearLayout(DayCalendarScreen.this);
		TextView nameDisplay = new TextView(DayCalendarScreen.this);
		TextView timeDisplay = new TextView(DayCalendarScreen.this);
		TextView border = new TextView(DayCalendarScreen.this);
		nameDisplay.setText(name);
		timeDisplay.setText(start.getHour() + ":" + start.getMinute() + "-" + end.getHour() + ":" + end.getMinute());
		border.setText("-------------------------------");

		conflictBubble.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		conflictBubble.setOrientation(LinearLayout.VERTICAL);
		float scale = getResources().getDisplayMetrics().density;
		conflictBubble.setPadding((int) Math.ceil(10 * scale),(int) Math.ceil(10 * scale),(int) Math.ceil(10 * scale),(int) Math.ceil(10 * scale));

		conflictBubble.addView(nameDisplay);
		conflictBubble.addView(timeDisplay);
		conflictBubble.addView(border);
		return conflictBubble;
	}
}