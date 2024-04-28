package com.experiment.experimenttwo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		((Button) findViewById(R.id.changeText)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				((TextView) findViewById(R.id.textBoxChange)).setText("Username: " + ((EditText) findViewById(R.id.editTextTextPersonName)).getText());
			}
		});
	}
}