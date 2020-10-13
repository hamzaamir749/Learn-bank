package com.coderpakistan.learningbank.Game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.coderpakistan.learningbank.Adapters.SessionVocabularyAdapter;
import com.coderpakistan.learningbank.HelperClasses.Session;
import com.coderpakistan.learningbank.HelperClasses.SharedHelper;
import com.coderpakistan.learningbank.HelperClasses.URLHelper;
import com.coderpakistan.learningbank.HelperClasses.UserSessionManager;
import com.coderpakistan.learningbank.LearnVocabulary.SessionVocabularyActivity;
import com.coderpakistan.learningbank.Models.SessionVocabularyModel;
import com.coderpakistan.learningbank.R;
import com.coderpakistan.learningbank.Utils.LoadingDialog;
import com.coderpakistan.learningbank.databinding.ActivityGameSessionBinding;
import com.coderpakistan.learningbank.databinding.ActivitySessionVocabularyBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameSessionActivity extends AppCompatActivity {
    ActivityGameSessionBinding binding;
    Context context = GameSessionActivity.this;
    LoadingDialog dialog;
    UserSessionManager userSessionManager;
    Session session;
    SharedHelper sharedHelper;
    List<SessionVocabularyModel> list;

    public static Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameSessionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        binding.toolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getData();
    }


    private void getData() {
        list = new ArrayList<>();
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.GAME_SESSION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("loginres", response);
                try {
                    JSONObject object;
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray("sessions");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            object = jsonArray.getJSONObject(i);
                            list.add(new SessionVocabularyModel(object.getString("id"), object.getString("name"),"game", object.getBoolean("unlocked")));
                        }
                        binding.sessionvocabularyRecycler.setAdapter(new SessionVocabularyAdapter(list, context));
                    } else {
                        Toast.makeText(context, "Not Available", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("loginexception", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("loginerror", error.toString());
                dialog.dismiss();
                getData();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", session.getId());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(stringRequest);

    }

    private void initViews() {
        activity=this;
        sharedHelper=new SharedHelper(context);
        dialog = new LoadingDialog(context);
        userSessionManager = new UserSessionManager(context);
        session = userSessionManager.getSessionDetails();
        binding.sessionvocabularyRecycler.setLayoutManager(new LinearLayoutManager(context));
    }
}
