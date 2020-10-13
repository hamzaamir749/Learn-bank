package com.coderpakistan.learningbank;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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
import com.coderpakistan.learningbank.databinding.ActivityUpdateProfileBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class UpdateProfileActivity extends AppCompatActivity {
    ActivityUpdateProfileBinding binding;
    Context context = UpdateProfileActivity.this;
    LoadingDialog dialog;
    UserSessionManager userSessionManager;
    Session session;
    String phone = "", image = "";
    public static int SELECT_PHOTO = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();

        binding.backwardTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.upImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_PHOTO);
            }
        });
        binding.upBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.upEmail.getText().toString().isEmpty() && binding.upUserName.getText().toString().isEmpty()) {
                    binding.upUserName.setError("Please Fill");
                    binding.upEmail.setError("Please Fill");
                } else if (binding.upEmail.getText().toString().isEmpty()) {
                    binding.upEmail.setError("Please Fill");
                } else if (binding.upUserName.getText().toString().isEmpty()) {
                    binding.upUserName.setError("Please Fill");
                } else {
                    if (!binding.upMobile.getText().toString().isEmpty()) {
                        phone = binding.upMobile.getText().toString();
                    }
                    UpdateProfileUser();
                }
            }
        });
    }

    private void UpdateProfileUser() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.UPDATE_PROFILE, new Response.Listener<String>() {
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
                map.put("user_id", session.getId());
                map.put("image", image);
                map.put("phone", phone);
                map.put("email", binding.upEmail.getText().toString());
                map.put("name", binding.upUserName.getText().toString());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                binding.upImage.setImageBitmap(bitmap);
                image = ImageToString(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String ImageToString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return "data:image/jpeg;base64," + imgString;
    }

    private void initViews() {
        dialog = new LoadingDialog(UpdateProfileActivity.this);
        userSessionManager = new UserSessionManager(context);
        session = userSessionManager.getSessionDetails();
        Picasso.get().load(URLHelper.BASE_IMAGE + "profile/" + session.getImage()).into(binding.upImage);
        binding.upUserName.setText(session.getName());
        binding.upEmail.setText(session.getEmail());
        phone = session.getPhone();
        binding.upMobile.setText(phone);
    }
}
