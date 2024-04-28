package com.example.isuautosched;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class NetworkManager {

    private final RequestQueue queue;

    interface GetALLUserData{
        public void onUserResults(User[] users);
    }

    public NetworkManager(RequestQueue queue) {
        this.queue = queue;
    }


    public void getAllData(GetALLUserData callback){
        StringRequest request = new StringRequest(Request.Method.GET, "http://coms-309-033.cs.iastate.edu:8080/user/getAllData",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        Gson gson = new Gson();// [{name:"",id:1,password:"aa",session"},{name:"",id:1,password:"aa",session"}]
                        User[] users = gson.fromJson(response, User[].class);
                        callback.onUserResults(users);
                   //     String userListString = ""; // I'm sorry god
//                        for (User user : users) {
//                            userListString = userListString.concat("\n" + user.name);
//                        }
//                        System.out.println(userListString);
                    }
                },
                new Response.ErrorListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        // Add the request to the queue
        queue.add(request);
    }

}
