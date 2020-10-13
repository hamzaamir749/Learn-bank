package com.coderpakistan.learningbank.Quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.coderpakistan.learningbank.databinding.ActivityQuizHomeBinding;

public class QuizHomeActivity extends AppCompatActivity {
    ActivityQuizHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MonthlyQuizActivity.class));
            }
        });
        binding.weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), WeeklyQuizActivity.class));
            }
        });
    }
}
