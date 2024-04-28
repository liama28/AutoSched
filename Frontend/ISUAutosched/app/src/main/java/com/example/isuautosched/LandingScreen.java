package com.example.isuautosched;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.net.URI;
import java.net.URISyntaxException;

import tech.gusavila92.websocketclient.WebSocketClient;

public class LandingScreen extends AppCompatActivity {

	TextView chatOutput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_landing_screen);

		createWebSocketClient();

		Button createAccountButton = findViewById(R.id.createAccount);
		Button loginButton = findViewById(R.id.loginSelection);
		chatOutput = findViewById(R.id.chatOutput);
		Button chatButton = findViewById(R.id.chatButton);
		TextInputEditText chatInput = findViewById(R.id.chatInput);
		chatButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View view) {
				String input = chatInput.getText().toString();
				webSocketClient.send(input);
			}
		});
		createAccountButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// :O
				Intent i = new Intent(LandingScreen.this, CreateAccountScreen.class);
				startActivity(i);
				// >:O
			}
		});
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// :(
				Intent i = new Intent(LandingScreen.this, LoginScreen.class);
				startActivity(i);
				// :'(
			}
		});
	}

	private WebSocketClient webSocketClient;
	private static final String TAG = "WebSocketClient";

	private void createWebSocketClient() {
		URI uri;
		// websocket can be used for realtime communication between server and android app such as chat.

		try {
			uri = new URI("ws://coms-309-033.cs.iastate.edu:8080/directmessage/TestUser");
			//wss://socketsbay.com/wss/v2/2/demo/
			//wss://demo.piesocket.com/v3/channel_2?api_key=oCdCMcMPQpbvNjUIzqtvF1d2X2okWpDQj4AwARJuAgtjhzKxVEjQU6IdCjwm&notify_self
//ws://coms-309-033.cs.iastate.edu:8080
			webSocketClient = new WebSocketClient(uri) {
				@Override
				public void onOpen() {
					Log.i(TAG, "Session is starting");
					webSocketClient.send("Hello World!");
				}

				@Override
				public void onTextReceived(String message) {
					Log.i(TAG, "Text: " + message);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							chatOutput.setText(message);

						}
					});

				}

				@Override
				public void onBinaryReceived(byte[] data) {
					Log.i(TAG, "onBinaryReceived");

				}

				@Override
				public void onPingReceived(byte[] data) {
					Log.i(TAG, "onPingReceived");

				}

				@Override
				public void onPongReceived(byte[] data) {
					Log.i(TAG, "onPongReceived");

				}

				@Override
				public void onException(Exception e) {
					e.printStackTrace();
				}

				@Override
				public void onCloseReceived() {
					Log.i(TAG, "Session is Close");
					System.out.println("onCloseReceived");
				}
			};

			webSocketClient.setConnectTimeout(10000);
			webSocketClient.setReadTimeout(60000);
			webSocketClient.enableAutomaticReconnection(5000);
			webSocketClient.connect();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}
}