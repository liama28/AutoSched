package com.example.isuautosched;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreateGroup extends AppCompatActivity {

    RecyclerView rvItems;
    ParticipantAdapter adapter;
    List<ModelParticipant> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateGroup.this, MainMenuScreen.class);
                startActivity(i);
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);

        rvItems = findViewById(R.id.rvItems);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ParticipantAdapter(this, items);
        rvItems.setAdapter(adapter);

        findViewById(R.id.llAddParticipant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didTapAddParticipant();
            }
        });

        Button createGroupButton = findViewById(R.id.save);
        SwitchCompat weekly = findViewById(R.id.weekly);
        SwitchCompat biweekly = findViewById(R.id.biweekly);
        SwitchCompat monthly = findViewById(R.id.monthly);
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // :O
                String switchValue = "";
                if(weekly.isEnabled()) {
                    switchValue = "weekly";
                }
                else if(biweekly.isEnabled()) {
                    switchValue = "biweekly";
                }
                else if(monthly.isEnabled()) {
                    switchValue = "monthly";
                }
                String finalSwitchValue = switchValue;
                String allNames = "";
                for(ModelParticipant participant: items) {
                    if(TextUtils.isEmpty(participant.getName())) continue;
                    allNames = allNames + participant.getName() + ", ";
                }
                if(!TextUtils.isEmpty(allNames)) {
                    allNames = allNames.substring(0, allNames.length() - 2);
                }
                String finalAllNames = allNames;
                JSONObject newGroup = new JSONObject();
                try {
                    newGroup.put("groupName", ((EditText) findViewById(R.id.groupName)).getText().toString());
                    newGroup.put("startDay", ((EditText) findViewById(R.id.etStartDay)).getText().toString());
                    newGroup.put("endDay", ((EditText) findViewById(R.id.etEndDay)).getText().toString());
                    newGroup.put("meetingFrequency", finalSwitchValue);
                    newGroup.put("groupParticipants", finalAllNames);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.BASE_URL+"group", newGroup, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        Intent i = new Intent(CreateGroup.this, MainMenuScreen.class);
                        startActivity(i);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateGroup.this);
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

    private void didTapAddParticipant(){
        items.add(new ModelParticipant(""));
        adapter.notifyItemInserted(items.size() - 1);
    }
}