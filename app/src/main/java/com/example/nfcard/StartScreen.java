package com.example.nfcard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartScreen extends AppCompatActivity {
    ImageButton info;
    Button read, save, base;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        read = findViewById(R.id.readbtn);
        read.setOnClickListener(view -> {
            startActivity(new Intent(StartScreen.this, ReadCkass.class));
        });
        save = findViewById(R.id.savebtn);
        save.setOnClickListener(view -> {
            startActivity(new Intent(StartScreen.this, WriteClass.class));
        });
        base = findViewById(R.id.basebtn);
        base.setOnClickListener(view -> {
            startActivity(new Intent(StartScreen.this, BaseClass.class));
        });
        info = findViewById(R.id.infobtn);
        info.setOnClickListener(v -> {
            startActivity(new Intent(StartScreen.this, InfoClass.class));
        });
    }
}
