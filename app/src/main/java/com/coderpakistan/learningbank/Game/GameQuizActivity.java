package com.coderpakistan.learningbank.Game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import com.coderpakistan.learningbank.Models.GameQuizModel;
import com.coderpakistan.learningbank.Models.SessionVocabularyModel;
import com.coderpakistan.learningbank.R;
import com.coderpakistan.learningbank.Utils.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class GameQuizActivity extends AppCompatActivity {

    Context context = GameQuizActivity.this;
    LoadingDialog dialog;
    UserSessionManager userSessionManager;
    Session session;
    SharedHelper sharedHelper;
    int minute;
    long min;
    int i = 0;
    List<GameQuizModel> list;
    private TextView quizQuestion;

    private RadioGroup radioGroup;
    private RadioButton optionOne;
    private RadioButton optionTwo;
    private RadioButton optionThree;
    private RadioButton optionFour;

    private int currentQuizQuestion = 0, correctAnswers = 0, wronganswer = 0, booleancount = 0;
    private int quizCount;
    GameQuizModel model;
    List<Boolean> answers = null;
    TextView quizAppCount, tv_timer;
    CardView exam_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_quiz);
        initViews();
        list = new ArrayList<>();
        answers = new ArrayList<>();
        getData();

        Button previousButton = (Button) findViewById(R.id.previousquiz);
        Button nextButton = (Button) findViewById(R.id.nextquiz);
        Button cancel = findViewById(R.id.cancel);
        tv_timer = findViewById(R.id.duration);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuizQuestion <= list.size() - 1) {
                    wronganswer++;
                    answers.add(false);
                    currentQuizQuestion++;
                    Log.d("value", String.valueOf(currentQuizQuestion));
                    uncheckedRadioButton();
                    SetQuestion("normal");

                } else {

                    if (correctAnswers >= wronganswer) {
                        saveData();
                    } else {
                        final PrettyDialog pDialog = new PrettyDialog(context);
                        pDialog
                                .setTitle("Status").setIcon(R.drawable.ic_error_outline_black_24dp)
                                .setMessage("You Failed To Complete This level:\n Correct Answers: " + String.valueOf(correctAnswers) + "\nWrong Answers: " + String.valueOf(wronganswer))
                                .addButton(
                                        "Retry Again",
                                        R.color.pdlg_color_white,
                                        R.color.colorPrimaryDark,
                                        new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                pDialog.dismiss();
                                                startActivity(new Intent(context, GameQuizActivity.class));
                                                finish();
                                            }
                                        }
                                )
                                .show();
                    }
                }

            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioSelected = radioGroup.getCheckedRadioButtonId();
                String ans = getSelectedAnswer(radioSelected);
                if (ans.equals("")) {
                    Toast.makeText(GameQuizActivity.this, "Select One Please", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (currentQuizQuestion <= list.size() - 1) {
                    Log.d("value", String.valueOf(currentQuizQuestion));
                    if (ans.equals(list.get(currentQuizQuestion).getAnswer())) {
                        currentQuizQuestion++;
                        correctAnswers++;
                        answers.add(true);
//                        Toast.makeText(GameQuizActivity.this, "You got the answer correct", Toast.LENGTH_LONG).show();

                        uncheckedRadioButton();
                        SetQuestion("normal");

                    } else {
                        currentQuizQuestion++;
  //                      Toast.makeText(GameQuizActivity.this, "You got the Wrong correct", Toast.LENGTH_LONG).show();
                        wronganswer++;
                        answers.add(false);
                        uncheckedRadioButton();
                        SetQuestion("normal");
                    }

                } else {
                    if (correctAnswers >= wronganswer) {
                        saveData();
                    } else {
                        final PrettyDialog pDialog = new PrettyDialog(context);
                        pDialog
                                .setTitle("Status").setIcon(R.drawable.ic_error_outline_black_24dp)
                                .setMessage("You Failed To Complete This level:\n Correct Answers: " + String.valueOf(correctAnswers) + "\nWrong Answers: " + String.valueOf(wronganswer))
                                .addButton(
                                        "Retry Again",
                                        R.color.pdlg_color_white,
                                        R.color.colorPrimaryDark,
                                        new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                pDialog.dismiss();
                                                startActivity(new Intent(context, GameQuizActivity.class));
                                                finish();
                                            }
                                        }
                                )
                                .show();
                    }
                }


            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuizQuestion--;
                Log.d("value", String.valueOf(currentQuizQuestion));
                if (currentQuizQuestion<=-1) {
                    currentQuizQuestion = 0;
                    answers.clear();
                    correctAnswers = 0;
                    wronganswer = 0;
                    Toast.makeText(context, "Inside", Toast.LENGTH_SHORT).show();
                    Log.d("value", String.valueOf(currentQuizQuestion));
                    return;
                }

                booleancount = answers.size() - 1;

                if (booleancount >= 0) {
                    if (answers.get(booleancount)) {
                        correctAnswers--;
                    } else {
                        wronganswer--;
                    }

                    answers.remove(booleancount);

                    uncheckedRadioButton();
                    SetQuestion("minus");

                } else return;
            }
        });

    }

    private void initViews() {
        sharedHelper = new SharedHelper(context);
        dialog = new LoadingDialog(context);
        userSessionManager = new UserSessionManager(context);
        session = userSessionManager.getSessionDetails();
        quizQuestion = findViewById(R.id.quiz_question);
        radioGroup = findViewById(R.id.radioGroup);
        optionOne = findViewById(R.id.radio0);
        optionTwo = findViewById(R.id.radio1);
        optionThree = findViewById(R.id.radio2);
        optionFour = findViewById(R.id.radio3);
        quizAppCount = findViewById(R.id.quizcount);
        exam_card = findViewById(R.id.exam_card);
    }

    private void getData() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.GAME_QUIZ, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("loginres", response);
                try {
                    JSONObject object;
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray("questions");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            object = jsonArray.getJSONObject(i);
                            list.add(new GameQuizModel(object.getString("question"), object.getString("option1"), object.getString("option2"), object.getString("option3"), object.getString("option4"), object.getString("answer")));
                        }

                        if (list.isEmpty() || list != null) {
                            exam_card.setVisibility(View.GONE);
                            Toast.makeText(context, "Not Available", Toast.LENGTH_SHORT).show();
                        } else {
                            exam_card.setVisibility(View.VISIBLE);
                            quizCount = list.size();
                            minute = 3;
                            min = minute * 60 * 1000;
                            counter(min);
                            SetQuestion("Normal");
                        }
                    } else {
                        exam_card.setVisibility(View.GONE);
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
                map.put("sess_id", sharedHelper.getKey("gamesessionid"));
                map.put("level_id", sharedHelper.getKey("gamelevelid"));
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(stringRequest);

    }

    private void saveData() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.GAME_PROGRESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("loginres", response);
                try {
                    JSONObject object;
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        final boolean sessionComplete = jsonObject.getBoolean("session");

                        final PrettyDialog pDialog = new PrettyDialog(context);
                        pDialog
                                .setTitle("Status").setIcon(R.drawable.ic_error_outline_black_24dp)
                                .setMessage("You Successfully Complete This level:\n Correct Answers: " + String.valueOf(correctAnswers) + "\nWrong Answers: " + String.valueOf(wronganswer))
                                .addButton(
                                        "Next Level",
                                        R.color.pdlg_color_white,
                                        R.color.colorPrimaryDark,
                                        new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                pDialog.dismiss();
                                                if (sessionComplete) {
                                                    new GameLevelActivity().activity.finish();
                                                    new GameSessionActivity().activity.finish();
                                                    startActivity(new Intent(getApplicationContext(), GameSessionActivity.class));
                                                    finish();
                                                } else {
                                                    new GameLevelActivity().activity.finish();
                                                    startActivity(new Intent(getApplicationContext(), GameLevelActivity.class));
                                                    finish();
                                                }
                                            }
                                        }
                                )
                                .show();

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
                saveData();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", session.getId());
                map.put("sess_id", sharedHelper.getKey("gamesessionid"));
                map.put("level_id", sharedHelper.getKey("gamelevelid"));
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(stringRequest);

    }

    private void counter(long min) {
        CountDownTimer timer = new CountDownTimer(min, 1000) {
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                tv_timer.setText(String.format("%d H:%d M:%d S", hours, minutes, seconds));
            }

            public void onFinish() {
                Toast.makeText(context, "Time End", Toast.LENGTH_SHORT).show();
                int total = wronganswer + correctAnswers;
                if (total == quizCount) {

                } else {
                    wronganswer = wronganswer + (quizCount - total);
                    if (correctAnswers >= wronganswer) {
                        saveData();
                    } else {
                        final PrettyDialog pDialog = new PrettyDialog(context);
                        pDialog
                                .setTitle("Status").setIcon(R.drawable.ic_error_outline_black_24dp)
                                .setMessage("You Failed To Complete This level:\n Correct Answers: " + String.valueOf(correctAnswers) + "\nWrong Answers: " + String.valueOf(wronganswer))
                                .addButton(
                                        "Retry Again",
                                        R.color.pdlg_color_white,
                                        R.color.colorPrimaryDark,
                                        new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                pDialog.dismiss();
                                                startActivity(new Intent(context, GameQuizActivity.class));
                                                finish();
                                            }
                                        }
                                )
                                .show();
                    }
                }
            }
        };
        timer.start();
    }

    private void SetQuestion(String type) {
        Log.d("value", String.valueOf(currentQuizQuestion) + " " + list.size());

        if (currentQuizQuestion <= list.size() - 1) {
            quizAppCount.setText(String.valueOf(currentQuizQuestion + 1) + "/" + String.valueOf(quizCount));
            quizQuestion.setText(list.get(currentQuizQuestion).getQuestion());
            optionOne.setText(list.get(currentQuizQuestion).getOptionOne());
            optionTwo.setText(list.get(currentQuizQuestion).getOptionTwo());
            optionThree.setText(list.get(currentQuizQuestion).getOptionThree());
            optionFour.setText(list.get(currentQuizQuestion).getOptionFour());
        } else {
            if (correctAnswers >= wronganswer) {
                Toast.makeText(context, "Excute", Toast.LENGTH_SHORT).show();
                saveData();
            } else {
                final PrettyDialog pDialog = new PrettyDialog(context);
                pDialog
                        .setTitle("Status").setIcon(R.drawable.ic_error_outline_black_24dp)
                        .setMessage("You Failed To Complete This level:\n Correct Answers: " + String.valueOf(correctAnswers) + "\nWrong Answers: " + String.valueOf(wronganswer))
                        .addButton(
                                "Retry Again",
                                R.color.pdlg_color_white,
                                R.color.colorPrimaryDark,
                                new PrettyDialogCallback() {
                                    @Override
                                    public void onClick() {
                                        pDialog.dismiss();
                                        startActivity(new Intent(context, GameQuizActivity.class));
                                        finish();
                                    }
                                }
                        )
                        .show();
            }
        }
    }

    private String getSelectedAnswer(int radioSelected) {

        String answerSelected = "";
        if (radioSelected == R.id.radio0) {
            answerSelected = optionOne.getText().toString();
        }
        if (radioSelected == R.id.radio1) {
            answerSelected = optionTwo.getText().toString();
        }
        if (radioSelected == R.id.radio2) {
            answerSelected = optionThree.getText().toString();

        }
        if (radioSelected == R.id.radio3) {
            answerSelected = optionFour.getText().toString();

        }
        return answerSelected;
    }

    private void uncheckedRadioButton() {
        optionOne.setChecked(false);
        optionTwo.setChecked(false);
        optionThree.setChecked(false);
        optionFour.setChecked(false);
    }

}
