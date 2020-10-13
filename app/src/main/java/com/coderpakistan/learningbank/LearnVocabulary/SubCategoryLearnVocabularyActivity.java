package com.coderpakistan.learningbank.LearnVocabulary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

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
import com.coderpakistan.learningbank.Adapters.SubCategoryLearnVocabularyAdapter;
import com.coderpakistan.learningbank.HelperClasses.Session;
import com.coderpakistan.learningbank.HelperClasses.SharedHelper;
import com.coderpakistan.learningbank.HelperClasses.URLHelper;
import com.coderpakistan.learningbank.HelperClasses.UserSessionManager;
import com.coderpakistan.learningbank.Models.SessionVocabularyModel;
import com.coderpakistan.learningbank.Models.SubCategoryLearnVocabularyModel;
import com.coderpakistan.learningbank.R;
import com.coderpakistan.learningbank.Utils.LoadingDialog;
import com.coderpakistan.learningbank.databinding.ActivitySubCategoryLearnVocabularyBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubCategoryLearnVocabularyActivity extends AppCompatActivity {
    ActivitySubCategoryLearnVocabularyBinding binding;
    Context context = SubCategoryLearnVocabularyActivity.this;
    LoadingDialog dialog;
    List<SubCategoryLearnVocabularyModel> list;
    String name, id;
    SharedHelper sharedHelper;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubCategoryLearnVocabularyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        id = sharedHelper.getKey("categoryid");
        name = sharedHelper.getKey("categoryname");
        binding.sclvTitle.setText(name);
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.SUB_CATEGORIES_VOCABULARY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("loginres", response);
                try {
                    JSONObject object;
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray("sub_categories");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            object = jsonArray.getJSONObject(i);
                            list.add(new SubCategoryLearnVocabularyModel(object.getString("id"), object.getString("name")));
                        }
                        binding.sclvRecycler.setAdapter(new SubCategoryLearnVocabularyAdapter(list, context));
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
                map.put("category_id", id);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(stringRequest);

    }

    private void initViews() {
        dialog = new LoadingDialog(context);
        binding.sclvRecycler.setLayoutManager(new GridLayoutManager(context, 2));
        sharedHelper = new SharedHelper(context);
    }
}
