package com.coderpakistan.learningbank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

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
import com.coderpakistan.learningbank.Adapters.LearnVocabularyAdapter;
import com.coderpakistan.learningbank.HelperClasses.URLHelper;
import com.coderpakistan.learningbank.Models.LearnVocabularyModel;
import com.coderpakistan.learningbank.Utils.LoadingDialog;
import com.coderpakistan.learningbank.databinding.ActivityLearnVocabularyBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class learnVocabularyActivity extends AppCompatActivity {
    ActivityLearnVocabularyBinding binding;
    LoadingDialog dialog;
    Context context = learnVocabularyActivity.this;
    List<LearnVocabularyModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLearnVocabularyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        getData();
        binding.toolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getData() {
        list = new ArrayList<>();
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLHelper.GET_LEARN_OPTIONS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("loginres", response);
                try {
                    JSONObject object;
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray("categories");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            object = jsonArray.getJSONObject(i);
                            list.add(new LearnVocabularyModel(object.getString("id"), object.getString("name"), URLHelper.BASE_IMAGE+"category/"+object.getString("image"), object.getBoolean("subcategories")));
                        }
                        binding.learnvocabularyRecycler.setAdapter(new LearnVocabularyAdapter(list, context));
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
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(stringRequest);

    }

    private void initViews() {
        dialog = new LoadingDialog(learnVocabularyActivity.this);
        binding.learnvocabularyRecycler.setLayoutManager(new GridLayoutManager(context, 2));
    }
}
