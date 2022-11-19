package com.example.nfcard;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class WriteClass extends AppCompatActivity {
    public static final String Error_Detected = "No NFC tag detected";
    public static final String Write_Success = "No NFC tag detected";
    public static final String Write_Error = "Error during writing, try again!";
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writingTagFilters[];
    boolean writeMode;
    Tag myTag;
    Context context;
    TextView edit_message;
    TextView nfc_contents;
    FloatingActionButton goback, activatebutton;
    @SuppressLint("UnspecifiedImmutableFlag")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_layout);
        nfc_contents = findViewById(R.id.texttag);
        edit_message = findViewById(R.id.edit_message);
        goback = findViewById(R.id.goback1);
        activatebutton = findViewById(R.id.writingbtn);
        context = this;
        goback.setOnClickListener(view -> {
            startActivity(new Intent(WriteClass.this, StartScreen.class));
        });
        activatebutton.setOnClickListener(view -> {
            try{
                if(myTag==null){Toast.makeText(context, Error_Detected, Toast.LENGTH_SHORT).show();}
                else{write("Text from Tag:"+edit_message.getText().toString(), myTag);
                    Toast.makeText(context, Write_Success, Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) { Toast.makeText(context, Write_Error, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (FormatException e){ Toast.makeText(context, Write_Error, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter == null){
            Toast.makeText(context, "Device does not support NFC", Toast.LENGTH_SHORT).show();
            finish();
        }
        readFromIntent(getIntent());
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writingTagFilters = new IntentFilter[] { tagDetected };
    }
    private void readFromIntent(Intent intent) { String action = intent.getAction();
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if(rawMsgs!=null){
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i]=(NdefMessage) rawMsgs[i];
                }
            }
            buildTagViews(msgs);
        }
    }

    private void buildTagViews(NdefMessage[] msgs) {
        if(msgs == null || msgs.length == 0) return;
        String text = "";
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0]&128)==0) ? "UTF-8" : "UTF-16";
        int languageCodeLenth = payload[0]&0063;
        try{
            text = new String (payload, languageCodeLenth + 1, payload.length - languageCodeLenth-1, textEncoding);
        } catch (UnsupportedEncodingException e){
            Log.e("UnsupportedEncodingException", e.toString());

        }
        nfc_contents.setText("NFC Content: " + text);
    }
    private void write(String text, Tag tag) throws IOException, FormatException {
        NdefRecord[] records = {createRecord(text)};
        NdefMessage message = new NdefMessage(records);
        Ndef ndef = Ndef.get(tag);
        ndef.connect();
        ndef.writeNdefMessage(message);
        ndef.close();
    }
    private NdefRecord createRecord(String text) throws UnsupportedEncodingException{
        String lang="en";
        byte[] textBytes = text.getBytes();
        byte[] langBytes = lang.getBytes("US-ASCII");
        int langLenght = langBytes.length;
        int textLenght = textBytes.length;
        byte[] payload = new byte[1 + langLenght + textLenght];
        payload[0]=(byte) langLenght;
        System.arraycopy(langBytes, 0, payload, 1, langLenght);
        System.arraycopy(textBytes, 0, payload, 1 + langLenght, textLenght);
        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);
        return recordNFC;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        readFromIntent(intent);
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        WriteModeOff();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        WriteModeOn();
    }

    private void WriteModeOn() {
        writeMode = true;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writingTagFilters, null);
    }
    private void WriteModeOff() {
        writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }

}
