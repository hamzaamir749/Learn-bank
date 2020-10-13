package com.coderpakistan.learningbank;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.coderpakistan.learningbank.databinding.ActivitySampleTextBinding;

public class SampleTextActivity extends AppCompatActivity {
    String textShow = "";
    ActivitySampleTextBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySampleTextBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        textShow = getIntent().getStringExtra("text");
        binding.toolText.setText(getIntent().getStringExtra("tooltext"));
        binding.backwardTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.stText.setText(textShow);
    }
}
