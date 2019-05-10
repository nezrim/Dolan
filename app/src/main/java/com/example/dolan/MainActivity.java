package com.example.dolan;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Locale;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity {
    private TextToSpeech textToSpeech;
    private EditText inputText;
    private SeekBar pitchBar;
    private SeekBar speedBar;
    private Button speakButton;
    private String lang = "English";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speakButton = findViewById(R.id.speak_Button);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    Log.i("textToSpeech", "Connected to textToSpeech");
                } else {
                    Log.e("textToSpeech", "Can not load textToSpeech");
                    return;
                }
            }
        });

        inputText = findViewById(R.id.input_Text);
        pitchBar = findViewById(R.id.pitch_Bar);
        speedBar = findViewById(R.id.speed_Bar);

        speakButton.setEnabled(true);

        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch();
            }
        });
    }

    private void launch() {
        Log.i("Lang",lang);

        int r;

        if(lang.equals("English")){
            r = textToSpeech.setLanguage(Locale.ENGLISH);
        } else if (lang.equals("German")){
            r = textToSpeech.setLanguage(Locale.GERMAN);
        } else {
            Log.e("Language", "Something went wrong");
            return;
        }

        if (r == TextToSpeech.LANG_MISSING_DATA || r == TextToSpeech.LANG_NOT_SUPPORTED) {
            speakButton.setEnabled(false);
            Log.e("textToSpeech", "Can not load that Language");
        } else {
            speakButton.setEnabled(true);
        }

        if(inputText.getText().toString().equals("")){
            makeToast("Írj valamit a mezőbe");
            return;
        }

        String text = "goofi pls " + inputText.getText().toString();

        float pitchValue = (float) pitchBar.getProgress() / 50;
        if (pitchValue < 0.1) pitchValue = 0.1f;
        float speedValue = (float) speedBar.getProgress() / 50;
        if (speedValue < 0.1) speedValue = 0.1f;

        Log.i("Original Pitch Value", String.valueOf(pitchBar.getProgress()));
        Log.i("Original Speed Value", String.valueOf(speedBar.getProgress()));
        Log.i("Pitch Value", String.valueOf(pitchValue));
        Log.i("Speed Value", String.valueOf(speedValue));

        textToSpeech.setPitch(pitchValue);
        textToSpeech.setSpeechRate(speedValue);

        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void onLangSelect(View view) {
        switch(view.getId()) {
            case R.id.radioEnglish:
                lang = "English";
                break;
            case R.id.radioGerman:
                lang = "German";
                break;
            default:
                break;
        }
        makeToast("Kiválasztott nyelv: " + lang);
    }

    public void makeToast(String str) {
        Context context = getApplicationContext();
        CharSequence text = str;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void makeJokeToast(View view) {
        Context context = getApplicationContext();
        CharSequence text = "Goofi PLS";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            Log.i("stopped", "textToSpeech");
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        super.onDestroy();
    }
}