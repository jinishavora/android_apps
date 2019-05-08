package com.nvcreation.sayit;

/*
Created by Jinisha Vora (jinishatanna@gmail.com) on 27h February, 2019
Name of App: Say It
Purpose: To help with the US pronunciation of word/words/sentences.
*/
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.w3c.dom.Text;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextToSpeech sayIt;
    EditText mTextEt;
    Button mSayItBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextEt = (EditText) findViewById(R.id.etWord);
        mSayItBtn = (Button) findViewById(R.id.btnSayIt);

        sayIt = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if(status != TextToSpeech.ERROR){
                    sayIt.setLanguage(Locale.US);
                }
            }
        });

        mSayItBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sayItText = mTextEt.getText().toString();
                sayIt.speak(sayItText,TextToSpeech.QUEUE_FLUSH, null);

            }
        });


    }

    @Override
    protected void onPause() {
        if(sayIt != null){
            sayIt.stop();
            sayIt.shutdown();
        }
        super.onPause();
    }
}
