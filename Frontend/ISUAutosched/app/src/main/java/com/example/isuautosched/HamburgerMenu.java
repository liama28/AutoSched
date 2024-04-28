package com.example.isuautosched;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

public class HamburgerMenu extends AppCompatActivity {

	public static void setOnClickListener(View.OnClickListener onClickListener) {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hamburger_menu);

		Button friendsButton = (Button) findViewById(R.id.hamburgerFriends);
		friendsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(HamburgerMenu.this, MainMenuScreen.class);
				startActivity(i);
			}
		});

		Button eventButton = (Button) findViewById(R.id.hamburgerCreateEventButton);
		eventButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(HamburgerMenu.this, CreateConflictScreen.class);
				startActivity(i);
			}
		});

		Button calendarButton = (Button) findViewById(R.id.hamburgerCalendarButton);
		calendarButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(HamburgerMenu.this, DayCalendarScreen.class);
				startActivity(i);
			}
		});
	}
}