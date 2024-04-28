package com.example.isuautosched;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreateConflictScreen extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_conflict);

		RequestQueue queue = Volley.newRequestQueue(CreateConflictScreen.this);

		ImageButton back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(CreateConflictScreen.this, MainMenuScreen.class);
				startActivity(i);
			}
		});

		Button saveButton = findViewById(R.id.saveConflict);
		saveButton.setOnClickListener(new View.OnClickListener() {
			@RequiresApi(api = Build.VERSION_CODES.M)
			@Override
			public void onClick(View view) {
				JSONObject sessionId = new JSONObject();
				JSONObject eventInfo = new JSONObject();
				try {
					sessionId.put("userid",CurrentUser.getId());
					sessionId.put("sessionid",CurrentUser.getSessionId());
				} catch (JSONException e) {
					e.printStackTrace();
				}

				JSONObject saveCalendarData = new JSONObject();
				TimePicker startTimePicker = ((TimePicker) findViewById(R.id.startTimePicker));
				TimePicker endTimePicker = ((TimePicker) findViewById(R.id.endTimePicker));
				DatePicker startDatePicker = ((DatePicker) findViewById(R.id.startDatePicker));
				DatePicker endDatePicker = ((DatePicker) findViewById(R.id.endDatePicker));
				try {
					eventInfo.put("name", ((EditText)findViewById(R.id.conflictTitleSet)).getText().toString());
					ZoneId zoneId = ZoneId.systemDefault();
					LocalDateTime dateTime = LocalDateTime.of(startDatePicker.getYear(), startDatePicker.getMonth(), startDatePicker.getDayOfMonth(),
							startTimePicker.getHour(), startTimePicker.getMinute());
					eventInfo.put("start", dateTime.atZone(zoneId).toEpochSecond());
					dateTime = LocalDateTime.of(startDatePicker.getYear(), startDatePicker.getMonth(), startDatePicker.getDayOfMonth(),
							endTimePicker.getHour(), endTimePicker.getMinute());
					eventInfo.put("end", dateTime.atZone(zoneId).toEpochSecond());
					eventInfo.put("repeats", false);
					eventInfo.put("repeatType", "DAILY");
					dateTime = LocalDateTime.of(startDatePicker.getYear(), startDatePicker.getMonth(), startDatePicker.getDayOfMonth(),
							startTimePicker.getHour(), startTimePicker.getMinute());
					eventInfo.put("repeatStart", dateTime.atZone(zoneId).toEpochSecond());
					dateTime = LocalDateTime.of(endDatePicker.getYear(), endDatePicker.getMonth(), endDatePicker.getDayOfMonth(),
							endTimePicker.getHour(), endTimePicker.getMinute());
					eventInfo.put("repeatEnd", dateTime.atZone(zoneId).toEpochSecond());
					saveCalendarData.put("sessionId", sessionId);
					saveCalendarData.put("event", eventInfo);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.BASE_URL + "calendar/save", saveCalendarData, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						System.out.println(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateConflictScreen.this);
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