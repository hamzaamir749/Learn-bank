package com.coderpakistan.learningbank.LearnVocabulary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import com.coderpakistan.learningbank.Adapters.DirectLevelVocabularyAdapter;
import com.coderpakistan.learningbank.HelperClasses.Session;
import com.coderpakistan.learningbank.HelperClasses.SharedHelper;
import com.coderpakistan.learningbank.HelperClasses.URLHelper;
import com.coderpakistan.learningbank.HelperClasses.UserSessionManager;
import com.coderpakistan.learningbank.HomeActivity;
import com.coderpakistan.learningbank.Models.SessionVocabularyModel;
import com.coderpakistan.learningbank.Models.WordsModel;
import com.coderpakistan.learningbank.R;
import com.coderpakistan.learningbank.Utils.LoadingDialog;
import com.coderpakistan.learningbank.databinding.ActivityDirectWordsBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirectWordsActivity extends AppCompatActivity {
    Context context = DirectWordsActivity.this;
    LoadingDialog dialog;
    UserSessionManager userSessionManager;
    Session session;
    List<WordsModel> list;
    SharedHelper sharedHelper;
    ActivityDirectWordsBinding binding;
    int i = 0;
    Boolean sessionComplete=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDirectWordsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        getData();
        binding.toolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.directwordNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++i;
                if (i <= list.size() - 1) {
                    setWords();
                } else {
                    saveData();
                }
            }
        });

        binding.directwordNextlevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (sessionComplete){
                  new DirectLevelVocabularyActivity().activity.finish();
                  new SessionVocabularyActivity().activity.finish();
                  startActivity(new Intent(getApplicationContext(), SessionVocabularyActivity.class));
                  finish();
              }
              else {
                  new DirectLevelVocabularyActivity().activity.finish();
                  startActivity(new Intent(getApplicationContext(), DirectLevelVocabularyActivity.class));
                  finish();
              }
            }
        });

        binding.directwordHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finishAffinity();
            }
        });
    }

    private void setWords() {
        Picasso.get().load(list.get(i).getImage()).into(binding.directwordImage);
        binding.directwordName.setText(list.get(i).getName());
    }

    private void saveData() {
        list = new ArrayList<>();
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.DIRECT_FINISH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("loginres", response);
                try {
                    JSONObject object;
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        sessionComplete=jsonObject.getBoolean("session");
                        binding.directCard.setVisibility(View.GONE);
                        binding.directwordFinishcard.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(context, "Try Again", Toast.LENGTH_SHORT).show();
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
                map.put("level_id", sharedHelper.getKey("directlevelid"));
                map.put("category_id", sharedHelper.getKey("categoryid"));
                map.put("sess_id", sharedHelper.getKey("directsessionid"));
                Log.d("loginres",map.toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(stringRequest);

    }

    private void getData() {
        list = new ArrayList<>();
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.DIRECT_WORDS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("loginres", response);
                try {
                    JSONObject object;
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray("words");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            object = jsonArray.getJSONObject(i);
                            list.add(new WordsModel(object.getString("name"), URLHelper.BASE_IMAGE + "word/" + object.getString("image")));
                        }
                        setWords();
                        binding.directwordNext.setClickable(true);
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
                map.put("level_id", sharedHelper.getKey("directlevelid"));
                map.put("category_id", sharedHelper.getKey("categoryid"));
                map.put("sess_id", sharedHelper.getKey("directsessionid"));
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(stringRequest);

    }

    private void initViews() {
        sharedHelper = new SharedHelper(context);
        dialog = new LoadingDialog(DirectWordsActivity.this);
        userSessionManager = new UserSessionManager(context);
        session = userSessionManager.getSessionDetails();
    }
}
