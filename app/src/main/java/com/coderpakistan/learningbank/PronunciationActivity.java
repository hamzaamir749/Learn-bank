package com.coderpakistan.learningbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.coderpakistan.learningbank.HelperClasses.URLHelper;
import com.coderpakistan.learningbank.Models.ProModel;
import com.coderpakistan.learningbank.Utils.LoadingDialog;
import com.coderpakistan.learningbank.databinding.ActivityPronounctaionBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class PronunciationActivity extends AppCompatActivity /*implements ProAdapter.TextSpeech*/ {
    private TextToSpeech mTTS;
    ActivityPronounctaionBinding binding;
    Context context = PronunciationActivity.this;
    LoadingDialog dialog;
    List<ProModel> list;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPronounctaionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        /* ProAdapter.speech = this;*/
        InitViews();
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getData();

        binding.proNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (i<list.size()-1){
                    Log.d("ival",String.valueOf(i));
                    ++i;
                    SetData();

                }
                else {
                    Toast.makeText(context, "FINISH", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        binding.proPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i>0){
                    --i;
                    SetData();
                }else {
                    Toast.makeText(context, "U cant Back It", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.proSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTTS.speak(list.get(i).getEnglish(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }

    private void getData() {
        list = new ArrayList<>();
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLHelper.GET_PRONOUNCATION, new Response.Listener<String>() {
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
                            list.add(new ProModel(object.getString("word"), object.getString("english_meaning"), object.getString("urdu_meaning"), object.getString("sentence"), URLHelper.BASE_IMAGE + "word/" + object.getString("image")));
                        }
                        if (list.isEmpty()) {
                            binding.proItem.setVisibility(View.GONE);
                        } else {
                            binding.proItem.setVisibility(View.VISIBLE);
                            SetData();
                        }
                        /*binding.proRecycler.setAdapter(new ProAdapter(list, context));*/
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

    private void SetData() {
        binding.proName.setText(list.get(i).getWord());
        binding.proEnglish.setText(list.get(i).getEnglish());
        binding.proUrdu.setText(list.get(i).getUrdu());
        binding.proSentence.setText(list.get(i).getSentence());
        Picasso.get().load(list.get(i).getImage()).into(binding.proImage);
    }

    private void InitViews() {
        dialog = new LoadingDialog(context);
        /*        binding.proRecycler.setLayoutManager(new LinearLayoutManager(context));*/
        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        Toast.makeText(PronunciationActivity.this, "Enable", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }

  /*  @Override
    public void onItemClick(String text) {
        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }*/
}
