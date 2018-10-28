package com.github.udplus.plusexamples;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;


/*
 * 1. 녹음시작
 * 2. 퍼미션 설정
 * 3. 명령과 연결
 *
 * */
public class SpeechToTextActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    public static final String TAG = SpeechToTextActivity.class.getSimpleName();
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;
    private TextView mText;
    private TextView mResultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);
        Log.d(TAG, "onCreate: ");

        checkPermission();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initLayout();

        initRecognizer();

    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }

    private void initLayout() {
        Log.d(TAG, "initLayout: ");
        mText = findViewById(R.id.textView);
        mResultText = findViewById(R.id.textView2);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        findViewById(R.id.button5).setOnTouchListener(this);
    }

    private void initRecognizer() {
        Log.d(TAG, "initRecognizer: ");

        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());

        // mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, Locale.getDefault());
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, "ko-KR");

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizer.setRecognitionListener(new MyRecognitionListener());

        Log.d(TAG, "initRecognizer: created SpeechRecognizer");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
        initRecognizer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        mSpeechRecognizer.stopListening();
        mSpeechRecognizer.destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        mSpeechRecognizer.stopListening();
        mSpeechRecognizer.destroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            Log.d(TAG, "onClick: id.fab");
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.button5) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    Log.d(TAG, "onTouch: UP");
                    mSpeechRecognizer.stopListening();
                    mResultText.setHint("You will see the input here");
                    break;
                case MotionEvent.ACTION_DOWN:
                    Log.d(TAG, "onTouch: DOWN");
                    mResultText.setText("");
                    mResultText.setHint("Listening...");
                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                    break;
            }

        }
        return false;
    }


    class MyRecognitionListener implements RecognitionListener {
        public void onReadyForSpeech(Bundle params) {
            Log.d(TAG, "onReadyForSpeech");
        }

        public void onBeginningOfSpeech() {
            Log.d(TAG, "onBeginningOfSpeech");
        }

        public void onRmsChanged(float rmsdB) {
            // 입력받는 소리의 크기를 나타내주는데,
            // 소리 크기값인 rmsDb는 디폴트 값이 -2 또는 -2.12 인거 같다.
            // 한번 음성입력을 받고 일정 시간동안 입력이 없으면(rmsdB 값이 -2 또는 -2.12일 때)
            // 자동으로 종료되는듯 하다.
            Log.d(TAG, "onRmsChanged");
        }

        public void onBufferReceived(byte[] buffer) {
            Log.d(TAG, "onBufferReceived");
        }

        public void onEndOfSpeech() {
            Log.d(TAG, "onEndOfSpeech");
        }

        /**
         * https://developer.android.com/reference/android/speech/SpeechRecognizer
         *
         * @param error error
         */
        public void onError(int error) {
            String errorMsg = "error code: " + error;

            if (error == SpeechRecognizer.ERROR_NETWORK_TIMEOUT) {
                errorMsg += ", Network operation timed out.";
            } else if (error == SpeechRecognizer.ERROR_NETWORK) {
                errorMsg += ", Other network related errors.";
            } else if (error == SpeechRecognizer.ERROR_RECOGNIZER_BUSY) {
                errorMsg += ", RecognitionService busy.";
            }
            // hotkey
            Log.d(TAG, errorMsg);
            mText.setText(errorMsg);

        }

        public void onResults(Bundle results) {
            Log.d(TAG, "onResults: ");
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (matches != null) {
                mResultText.setText(matches.get(0));
            }
            Log.d(TAG, "onResults: " + results);

            // String str = new String();
            // Log.d(TAG, "onResults " + results);
            //
            // ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            // for (int i = 0; i < data.size(); i++) {
            //     Log.d(TAG, "result " + data.get(i));
            //     str += data.get(i);
            // }
            // mText.setText("results: " + String.valueOf(data.size()));
            // mResultText.setText("result str: " + str);
        }

        public void onPartialResults(Bundle partialResults) {
            Log.d(TAG, "onPartialResults");
        }

        public void onEvent(int eventType, Bundle params) {
            Log.d(TAG, "onEvent " + eventType);
        }
    }
}
