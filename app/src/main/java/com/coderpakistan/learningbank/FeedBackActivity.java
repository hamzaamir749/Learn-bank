package com.coderpakistan.learningbank;

import androidx.appcompat.app.AppCompatActivity;

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
import com.coderpakistan.learningbank.HelperClasses.Session;
import com.coderpakistan.learningbank.HelperClasses.URLHelper;
import com.coderpakistan.learningbank.HelperClasses.UserSessionManager;
import com.coderpakistan.learningbank.Utils.LoadingDialog;
import com.coderpakistan.learningbank.databinding.ActivityFeedBackBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class FeedBackActivity extends AppCompatActivity {
    ActivityFeedBackBinding binding;
    Context context = FeedBackActivity.this;
    LoadingDialog dialog;
    UserSessionManager userSessionManager;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedBackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();

        binding.toolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.feedbackSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.feedbackTitle.getText().toString().isEmpty() && binding.feedbackMessage.getText().toString().isEmpty()) {
                    binding.feedbackTitle.setError("Please Fill");
                    binding.feedbackMessage.setError("Please Fill");
                } else if (binding.feedbackTitle.getText().toString().isEmpty()) {
                    binding.feedbackTitle.setError("Please Fill");
                } else if (binding.feedbackMessage.getText().toString().isEmpty()) {
                    binding.feedbackMessage.setError("Please Fill");
                } else {
                    submitFeedback();
                }
            }
        });
    }

    private void submitFeedback() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.FEEDBACK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("registerres", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        final PrettyDialog pDialog = new PrettyDialog(context);
                        pDialog
                                .setTitle("Status").setIcon(R.drawable.ic_check_circle_black_24dp)
                                .setMessage(jsonObject.getString("message"))
                                .addButton(
                                        "Ok",
                                        R.color.pdlg_color_white,
                                        R.color.colorPrimaryDark,
                                        new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                pDialog.dismiss();
                                                onBackPressed();
                                            }
                                        }
                                )
                                .show();
                    } else {
                        Toast.makeText(context, "Try Again", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("registerexception", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("registererror", error.toString());
                dialog.dismiss();
                submitFeedback();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", session.getId());
                map.put("title", binding.feedbackTitle.getText().toString());
                map.put("message", binding.feedbackMessage.getText().toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(stringRequest);
    }

    private void initViews() {
        dialog = new LoadingDialog(FeedBackActivity.this);
        userSessionManager = new UserSessionManager(context);
        session = userSessionManager.getSessionDetails();
    }
}
