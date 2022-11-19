package com.example.nfcard;

import android.content.ComponentName;
import android.nfc.cardemulation.NfcFCardEmulation;
import android.os.Bundle;
import android.os.Parcel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseClass extends AppCompatActivity {
    NfcFCardEmulation emulation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);
    }
}
