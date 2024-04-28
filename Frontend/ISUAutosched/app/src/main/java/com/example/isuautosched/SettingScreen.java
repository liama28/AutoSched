package com.example.isuautosched;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class SettingScreen extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		ImageButton SettingScreen = (ImageButton) findViewById(R.id.hamburger);
		HamburgerMenu.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				Intent i = new Intent(SettingScreen.this, HamburgerMenu.class);
				startActivity(i);
			}
		});

	}
}