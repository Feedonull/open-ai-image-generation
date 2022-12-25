package com.ahmed.openaigeneratedimage.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.ahmed.openaigeneratedimage.R;
import com.ahmed.openaigeneratedimage.fragment.CreateImageFragment;
import com.ahmed.openaigeneratedimage.util.Constants;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class GenerateImageRequest {

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    private Context ctx;
    private String imgUrl = "";
    private ProgressDialog progressDialog;

    public GenerateImageRequest(Context ctx){
        this.ctx = ctx;
    }

    public String send(String prompt) {

        progressDialog = new ProgressDialog(ctx, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please wait...\nThis might take somtime");
        progressDialog.setTitle("Creating your image");
        progressDialog.show();
        progressDialog.setCancelable(false);

        JSONObject js = new JSONObject();
        try {
            js.put("prompt", prompt);
            js.put("n", 1);
            js.put("size", "1024x1024");
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.GENERATE_IMAGE_URL, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (!response.equals(null)) {

                    int created = 0;
                    JSONArray dataArray = new JSONArray();

                    try {

                        created = response.getInt("created");
                        dataArray = response.getJSONArray("data");
                        JSONObject imgObj = dataArray.getJSONObject(0);
                        imgUrl = imgObj.getString("url");

                        Glide.with(ctx).
                                load(imgUrl)
                                .thumbnail(Glide.with(ctx).load(R.drawable.waiting))
                                .fitCenter()
                                .into(CreateImageFragment.generatedImg);

                        progressDialog.cancel();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Log.e("Your Array Response", response.toString());
                    Log.e("created", ""+created);
                    try {
                        URI uri = new URI(imgUrl);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    Log.e("url", imgUrl);
                } else {
                    Log.e("Your Array Response", "Data Null");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
                progressDialog.cancel();
            }
        }) {

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer "+ Constants.API_KEY);
                return params;
            }

            //Pass Your Parameters here
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(ctx);
        queue.add(request);

        return imgUrl;
    }
}
