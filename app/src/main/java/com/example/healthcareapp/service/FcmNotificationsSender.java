package com.example.healthcareapp.service;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;

import androidx.browser.trusted.sharing.ShareTarget;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthcareapp.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.ErrorListener;

public class FcmNotificationsSender {
    String userFcmToken;
    String title;
    String body;
    Context mContext;
    private RequestQueue requestQueue;
    private final String postUrl= "https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey="AAAAMaPn94U:APA91bEBHhkxgRr8ykrQtu0efCC_Lvu4pFp7PbGh2XSbUDf1u6-Pnr4bs-QrXu2XoNHyxM4liHhu3xKpgRUyNWfN35BCU5QdtcV6DkbX4zWd6u5wSnP4KP4jqpJjKYQ3Oo1Cl9Zrx0K9";
    public FcmNotificationsSender (String userFcmToken, String title, String body, Context mContext) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
    }

    public void sendNotification(){
        requestQueue = Volley.newRequestQueue(mContext);
        JSONObject mainObj = new JSONObject();
        try{
            mainObj.put("to",userFcmToken);
            JSONObject notiObj = new JSONObject();
            notiObj.put("title", title);
            notiObj.put("body", body);
            notiObj.put("icon", R.drawable.baseline_notifications_active_24);
            mainObj.put("notification", notiObj);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "key=" + fcmServerKey);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            requestQueue.add(request);
        } catch (Exception e){
            System.out.println(e);
        }
    }

}
