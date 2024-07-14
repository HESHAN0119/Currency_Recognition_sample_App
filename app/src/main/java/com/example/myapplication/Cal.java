package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Cal extends AppCompatActivity {
    TextView value;
    EditText value2;
    private TextView tvText;
    TextView  btnSpeak2,plus_min,opr;
    String op="+";

    private TextToSpeech textToSpeech;
    private TextToSpeech textToSpeech2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal);

        StringToNumberMapping.initializeMapping();

        // Retrieve the value passed from the previous activity
        String resultValue = getIntent().getStringExtra("RESULT_VALUE");

        // Use the value as needed
        value = findViewById(R.id.value_txt);
        value2 =findViewById(R.id.textView2);
        tvText = findViewById(R.id.tvText);
        btnSpeak2 = findViewById(R.id.btnSpeak2);
        plus_min= findViewById(R.id.btnSpeak);
        opr= findViewById(R.id.textView);

        value.setText(resultValue);



        textToSpeech = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                // Set the language to Sinhala
                Locale sinhalaLocale = new Locale("si", "LK");
                int result = textToSpeech.setLanguage(sinhalaLocale);

                if (result == TextToSpeech.LANG_MISSING_DATA ||
                        result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Sinhala language is not supported");
                    Toast.makeText(Cal.this, "Sinhala language is not supported", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("TTS", "Initialization failed");
                Toast.makeText(Cal.this, "Text-to-speech initialization failed", Toast.LENGTH_SHORT).show();
            }
        });



        btnSpeak2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        plus_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (op.equals("+")){
                   opr.setText("+");
                    textToSpeech.speak(String.valueOf("එකතු කිරීම"), TextToSpeech.QUEUE_FLUSH, null, null);
                   op="-";
                }else {
                    opr.setText("-");
                    textToSpeech.speak(String.valueOf("අඩු කිරීම"), TextToSpeech.QUEUE_FLUSH, null, null);
                    op="+";
                }


            }
        });

        plus_min.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Handle long click

                int value_1;
                int value_2;

// Parsing value_1
                String textValue1 = value.getText().toString();
                if (textValue1.isEmpty()) {
                    value_1 = 0;
                } else {
                    try {
                        value_1 = Integer.parseInt(textValue1);
                    } catch (NumberFormatException e) {
                        value_1 = 0;
                    }
                }

// Parsing value_2
                String textValue2 = value2.getText().toString();
                if (textValue2.isEmpty()) {
                    value_2 = 0;
                } else {
                    try {
                        value_2 = Integer.parseInt(textValue2);
                    } catch (NumberFormatException e) {
                        value_2 = 0;
                    }
                }

// Perform addition
                int ans=0;
                String operator=opr.getText().toString();
                if (operator.equals("+")){
                     ans = value_1 + value_2;
                }else{
                     ans = value_1 - value_2;
                }


                textToSpeech.speak(String.valueOf(ans), TextToSpeech.QUEUE_FLUSH, null, null);

//                if (op.equals("+")) {
//                    opr.setText("+");
//                    textToSpeech.speak("එකතු", TextToSpeech.QUEUE_FLUSH, null, null);
//                    op = "-";
//                } else {
//                    opr.setText("-");
//                    textToSpeech.speak("අඩු", TextToSpeech.QUEUE_FLUSH, null, null);
//                    op = "+";
//                }
                return true; // Returning true indicates the event is consumed
            }
        });
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak something...");

        try {
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Sorry, your device doesn't support speech input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 100: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String spokenText = result.get(0);
                    String firstWord = spokenText.split(" ")[0];
//
                    // Use StringToNumberMapping to find the corresponding number
                    Integer correspondingNumber = StringToNumberMapping.findNumber(firstWord);

                    if (correspondingNumber != null) {
                        String val = value2.getText().toString();
                        String combinedText = val + correspondingNumber ;
                        value2.setText(combinedText);
                        textToSpeech.speak(String.valueOf(combinedText), TextToSpeech.QUEUE_FLUSH, null, null);
                    } else {
                        Toast.makeText(Cal.this, "No corresponding number found for '" + firstWord + "'", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
        }
    }
}