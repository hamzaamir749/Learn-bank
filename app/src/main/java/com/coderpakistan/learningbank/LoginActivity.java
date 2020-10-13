package com.coderpakistan.learningbank;

import androidx.annotation.NonNull;
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
import com.coderpakistan.learningbank.HelperClasses.Session;
import com.coderpakistan.learningbank.HelperClasses.URLHelper;
import com.coderpakistan.learningbank.HelperClasses.UserSessionManager;
import com.coderpakistan.learningbank.Utils.LoadingDialog;
import com.coderpakistan.learningbank.databinding.ActivityLoginBinding;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    Context context = LoginActivity.this;
    LoadingDialog dialog;
    UserSessionManager userSessionManager;
    Session session;
    LoginButton fb;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    String name = "", email = "", image = "";
    private static final int RC_SIGN_IN = 99;
    private CallbackManager mCallbackManager;
    private String firebaseID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();

        binding.loginSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
        binding.loginBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });
        binding.loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                if (acct != null) {
                    mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(context, "Signed out successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        binding.loginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fb.performClick();
            }
        });
        fb.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("FBScuccess", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("FBCancel", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FBError", "facebook:onError", error);
                // ...
            }
        });
    }

    private void initViews() {
        LoginManager.getInstance().logOut();
        fb = findViewById(R.id.login_fb);
        dialog = new LoadingDialog(LoginActivity.this);
        userSessionManager = new UserSessionManager(context);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_id_gso))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        fb.setPermissions("email", "public_profile","user_friends");
    }

    private void CheckData() {
        if (binding.loginEmail.getText().toString().isEmpty() && binding.loginPassword.getText().toString().isEmpty()) {
            binding.loginEmail.setError("Please Fill");
            binding.loginPassword.setError("Please Fill");
        } else if (binding.loginEmail.getText().toString().isEmpty()) {
            binding.loginEmail.setError("Please Fill");
        } else if (binding.loginPassword.getText().toString().isEmpty()) {
            binding.loginPassword.setError("Please Fill");
        } else {
            LoginUser();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                // ...
            }
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {

                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("tokenFB", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("tokenFBSuccess", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("tokenFBfail", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //  Toast.makeText(MainActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
                }
            });
        }


        name = user.getDisplayName();
        email = user.getEmail();
        firebaseID = user.getUid();
        mAuth.signOut();
        CheckSocialLogin();
    }

    private void CheckSocialLogin() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.LOGIN_SOCIAL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("loginres", response);
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

                        RegisterSocialUser();
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
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("firebase", firebaseID);
                map.put("device_type", "android");
                map.put("token", FirebaseInstanceId.getInstance().getToken());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(stringRequest);

    }

    private void RegisterSocialUser() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.REGISTER_SOCIAL, new Response.Listener<String>() {
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
                        session = new Session(object.getString("id"), object.getString("email"), object.getString("name"), "", object.getString("image"));
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
                map.put("firebase", firebaseID);
                map.put("name", name);
                map.put("email", email);
                map.put("device_type", "android");
                map.put("token",  FirebaseInstanceId.getInstance().getToken());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(stringRequest);
    }


    private void LoginUser() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("loginres", response);
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
                    Log.d("loginexception", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("loginerror", error.toString());
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("email", binding.loginEmail.getText().toString());
                map.put("password", binding.loginPassword.getText().toString());
                map.put("device_type", "android");
                map.put("token",  FirebaseInstanceId.getInstance().getToken());
                Log.d("maplog",map.toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(stringRequest);

    }

    private void GoToHomeActivity() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finishAffinity();
    }
}
