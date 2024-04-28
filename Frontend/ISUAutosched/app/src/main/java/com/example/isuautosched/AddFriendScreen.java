package com.example.isuautosched;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;

public class AddFriendScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_screen);

        // Request Data
        RequestQueue queue = Volley.newRequestQueue(AddFriendScreen.this);
        JSONObject data = new JSONObject();
        try {
            data.put("userid", CurrentUser.getId());
            data.put("sessionid", CurrentUser.getSessionId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Search Users
        ScrollView friendRequestLayout = findViewById(R.id.friendRequestArea);
        LinearLayout userSearchResults = findViewById(R.id.userSearchResults);
        EditText searchBar = findViewById(R.id.searchUsers);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Search for users if text isn't empty
                if (i2 > 0) {
                    JsonArrayRequest groupListRequest = new JsonArrayRequest(Request.Method.GET, Constants.BASE_URL + "user/nameStartsWith/" + charSequence, null, new Response.Listener<org.json.JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            System.out.println(response);
                            // if text isnt empty and there are users hide the other display and show the user display
                            if (response.length() > 0) {

                                // Clear userSearchResults
                                userSearchResults.removeAllViews();

                                // Add results to userSearchResults
                                for (int i = 0; i < response.length(); i++) {
                                    LinearLayout userResult = new LinearLayout(AddFriendScreen.this);
                                    userResult.setOrientation(LinearLayout.HORIZONTAL);
                                    userResult.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                                    // Create name display
                                    TextView userResultName = new TextView(AddFriendScreen.this);
                                    try {
                                        userResultName.setText(response.getJSONObject(i).getString("name"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    userResultName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                                    userResult.addView(userResultName);

                                    // Create add button
                                    Button userAddButton = new Button(AddFriendScreen.this);
                                    userAddButton.setText("Add");
                                    int currentResponse = i;
                                    userAddButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            try {
                                                JsonObjectRequest acceptFriend = new JsonObjectRequest(Request.Method.PUT, Constants.BASE_URL + "user/addFriend/" + response.getJSONObject(currentResponse).getInt("id"), data, new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        System.out.println(response);
                                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddFriendScreen.this);
                                                        alertDialogBuilder.setTitle("Sent Friend Request");
                                                        alertDialogBuilder.setPositiveButton("Ok", null);
                                                        alertDialogBuilder.create().show();
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        error.printStackTrace();
                                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddFriendScreen.this);
                                                        alertDialogBuilder.setTitle("Error");
                                                        alertDialogBuilder.setMessage(error.getMessage());
                                                        alertDialogBuilder.setPositiveButton("Ok", null);
                                                        alertDialogBuilder.setNegativeButton("", null);
                                                        alertDialogBuilder.create().show(); }
                                                });
                                                queue.add(acceptFriend);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    userResult.addView(userAddButton);

                                    userSearchResults.addView(userResult);
                                }

                                friendRequestLayout.setVisibility(View.GONE);
                                userSearchResults.setVisibility(View.VISIBLE);
                            } else {
                                friendRequestLayout.setVisibility(View.VISIBLE);
                                userSearchResults.setVisibility(View.GONE);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddFriendScreen.this);
                            alertDialogBuilder.setTitle("Error");
                            alertDialogBuilder.setMessage(error.getMessage());
                            alertDialogBuilder.setPositiveButton("Ok", null);
                            alertDialogBuilder.setNegativeButton("", null);
                            alertDialogBuilder.create().show();
                        }
                    });
                    queue.add(groupListRequest);
                } else {
                    friendRequestLayout.setVisibility(View.VISIBLE);
                    userSearchResults.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /* LOAD INCOMING AND OUTGOING FRIEND REQUESTS */
        // Get Incoming
        LinearLayout incomingFriendRequests = findViewById(R.id.incomingFriendRequests);
        JsonRequest<JSONArray> incomingFriendRequest = new JsonRequest<JSONArray>(Request.Method.POST, Constants.BASE_URL + "user/friendRequest", data.toString(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println(response);
                // Add results to userSearchResults
                for (int i = 0; i < response.length(); i++) {
                    LinearLayout userResult = new LinearLayout(AddFriendScreen.this);
                    userResult.setOrientation(LinearLayout.HORIZONTAL);
                    userResult.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    // Create name display
                    TextView userResultName = new TextView(AddFriendScreen.this);
                    try {
                        userResultName.setText(response.getJSONObject(i).getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    userResultName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                    userResult.addView(userResultName);

                    // Create accept button
                    Button accept = new Button(AddFriendScreen.this);
                    accept.setText("Accept");
                    int currentResponse = i;
                    accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                JsonObjectRequest acceptFriend = new JsonObjectRequest(Request.Method.PUT, Constants.BASE_URL + "user/friendRequest/" + response.getJSONObject(currentResponse).getInt("id") + "/accept", data, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        System.out.println(response);
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddFriendScreen.this);
                                        alertDialogBuilder.setTitle("Accepted Friend Request");
                                        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Reload the screen
                                                finish();
                                                startActivity(getIntent());
                                            }
                                        });
                                        alertDialogBuilder.create().show();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddFriendScreen.this);
                                        alertDialogBuilder.setTitle("Error");
                                        alertDialogBuilder.setMessage(error.getMessage());
                                        alertDialogBuilder.setPositiveButton("Ok", null);
                                        alertDialogBuilder.setNegativeButton("", null);
                                        alertDialogBuilder.create().show(); }
                                });
                                queue.add(acceptFriend);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    userResult.addView(accept);

                    // Create deny button
                    Button deny = new Button(AddFriendScreen.this);
                    deny.setText("Deny");
                    deny.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                JsonObjectRequest acceptFriend = new JsonObjectRequest(Request.Method.PUT, Constants.BASE_URL + "user/friendRequest/" + response.getJSONObject(currentResponse).getInt("id") + "/deny", data, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        System.out.println(response);
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddFriendScreen.this);
                                        alertDialogBuilder.setTitle("Deleted Friend Request");
                                        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Reload the screen
                                                finish();
                                                startActivity(getIntent());
                                            }
                                        });
                                        alertDialogBuilder.create().show();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddFriendScreen.this);
                                        alertDialogBuilder.setTitle("Error");
                                        alertDialogBuilder.setMessage(error.getMessage());
                                        alertDialogBuilder.setPositiveButton("Ok", null);
                                        alertDialogBuilder.setNegativeButton("", null);
                                        alertDialogBuilder.create().show();
                                    }
                                });
                                queue.add(acceptFriend);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    userResult.addView(deny);

                    incomingFriendRequests.addView(userResult);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddFriendScreen.this);
                alertDialogBuilder.setTitle("Error");
                alertDialogBuilder.setMessage(error.getMessage());
                alertDialogBuilder.setPositiveButton("Ok", null);
                alertDialogBuilder.setNegativeButton("", null);
                alertDialogBuilder.create().show();
            }
        }) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                System.out.println(response.data);
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                    return Response.success(new JSONArray(jsonString),
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        queue.add(incomingFriendRequest);

        // Get Pending
        LinearLayout pendingFriendRequests = findViewById(R.id.pendingFriendRequests);
        JsonRequest<JSONArray> pendingFriendRequest = new JsonRequest<JSONArray>(Request.Method.POST, Constants.BASE_URL + "user/sentRequest", data.toString(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println(response);
                // Add results to userSearchResults
                for (int i = 0; i < response.length(); i++) {
                    LinearLayout userResult = new LinearLayout(AddFriendScreen.this);
                    userResult.setOrientation(LinearLayout.HORIZONTAL);
                    userResult.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    // Create name display
                    TextView userResultName = new TextView(AddFriendScreen.this);
                    try {
                        userResultName.setText(response.getJSONObject(i).getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    userResultName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                    userResult.addView(userResultName);

                    pendingFriendRequests.addView(userResult);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddFriendScreen.this);
                alertDialogBuilder.setTitle("Error");
                alertDialogBuilder.setMessage(error.getMessage());
                alertDialogBuilder.setPositiveButton("Ok", null);
                alertDialogBuilder.setNegativeButton("", null);
                alertDialogBuilder.create().show();
            }
        }) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                System.out.println(response.data);
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                    return Response.success(new JSONArray(jsonString),
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        queue.add(pendingFriendRequest);

    }
}
