package com.coderpakistan.learningbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
import com.coderpakistan.learningbank.databinding.ActivityRegisterBinding;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    Context context = RegisterActivity.this;
    LoadingDialog dialog;
    UserSessionManager userSessionManager;
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        binding.registerLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToLoginActivity();
            }
        });

        binding.registerBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });
    }

    private void CheckData() {
        if (binding.registerUserName.getText().toString().isEmpty() && binding.registerMobile.getText().toString().isEmpty() && binding.registerEmail.getText().toString().isEmpty() && binding.registerPassword.getText().toString().isEmpty()) {
            binding.registerUserName.setError("Please Fill");
            binding.registerMobile.setError("Please Fill");
            binding.registerEmail.setError("Please Fill");
            binding.registerPassword.setError("Please Fill");
        } else if (binding.registerUserName.getText().toString().isEmpty()) {
            binding.registerUserName.setError("Please Fill");
        } else if (binding.registerMobile.getText().toString().isEmpty()) {
            binding.registerMobile.setError("Please Fill");
        } else if (binding.registerEmail.getText().toString().isEmpty()) {
            binding.registerEmail.setError("Please Fill");
        } else if (binding.registerPassword.getText().toString().isEmpty()) {
            binding.registerPassword.setError("Please Fill");
        }
        else {
            RegisterUser();
        }
    }

    private void RegisterUser() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("registerres", response);
                try {
                    JSONObject object;
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        object = jsonObject.getJSONObject("user");
                        session = new Session(object.getString("id"), object.getString("email"), object.getString("name"), object.getString("phone"), object.getString("image"));
                        userSessionManager.setLoggedIn(true);
                        userSessionManager.setSessionDetails(session);
                        GoToHomeActivity();
                    } else {
                        final PrettyDialog pDialog = new PrettyDialog(context);
                        pDialog
                                .setTitle("Status").setIcon(R.drawable.ic_error_outline_black_24dp)
                                .setMessage(jsonObject.getString("message"))
                                .addButton(
                                        "Ok",
                                        R.color.pdlg_color_white,
                                        R.color.colorPrimaryDark,
                                        new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                pDialog.dismiss();

                                            }
                                        }
                                )
                                .show();

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
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("phone", binding.registerMobile.getText().toString());
                map.put("password", binding.registerPassword.getText().toString());
                map.put("name", binding.registerUserName.getText().toString());
                map.put("email", binding.registerEmail.getText().toString());
                map.put("device_type", "android");
                map.put("token",  FirebaseInstanceId.getInstance().getToken());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(stringRequest);
    }
    private void GoToHomeActivity() {
        startActivity(new Intent(context, HomeActivity.class));
        finish();
    }

    private void GoToLoginActivity() {
        startActivity(new Intent(context, LoginActivity.class));
        finish();
    }

    private void initViews() {
        dialog = new LoadingDialog(RegisterActivity.this);
        userSessionManager = new UserSessionManager(context);
    }
}
