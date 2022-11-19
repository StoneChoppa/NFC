package com.example.nfcard;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ReadCkass extends AppCompatActivity {
    FloatingActionButton goback, read;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_layout);
        goback = findViewById(R.id.goback);
        read = findViewById(R.id.readtag);
        goback.setOnClickListener(view -> {
            startActivity(new Intent(ReadCkass.this, StartScreen.class));
        });
    }
}
