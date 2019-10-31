package com.example.helfcode;

import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button button;
    private EditText inputText;
    private JSONObject HelfJson = null;
    private GestureDetectorCompat gestureDetectorCompat = null;
    private Vibrator v;
    private String currWord;
    private String currCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currCode = "";
        currWord = "";
        textView = (TextView) findViewById(R.id.DetectSwipeDirection);
        inputText = (EditText) findViewById(R.id.inputText);
        button = (Button) findViewById(R.id.button);
        DetectSwipeGestureListener gestureListener = new DetectSwipeGestureListener();
        gestureListener.setActivity(this);
        gestureDetectorCompat = new GestureDetectorCompat(this, gestureListener);
        HelfJson = HelfHelper.getJSONObject(getApplicationContext());
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrateHelfMessage();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetectorCompat.onTouchEvent(event);
        return true;
    }

    public void displayMessage(int message) {
        if (textView != null) {
            //textView.setText(message);
            switch(message) {
                case 1:
                    currCode += "1x";
                    break;
                case 2:
                    currCode += "2x";
                    break;
                case 3:
                    fetchWord(currCode);
                    break;
                case 4:
                    try {
                        currWord = currWord.substring(0, currWord.length()-1);
                        textView.setText(currWord);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    nextLetter(currCode);
                    break;
                case 6:
                    currWord = "Text Deleted!";
                    textView.setText(currWord);
                    break;
            }
        }
    }

    private void nextLetter(String code) {
        Iterator iterator = HelfJson.keys();
        if (currCode.length() > 1) {
            currCode = currCode.substring(0, currCode.length()-1);
            while(iterator.hasNext()) {
                String key = (String) iterator.next();
                Log.d("Code: ", currCode);
                try {
                    if (HelfJson.getString(key).equals(currCode)) {
                        currWord += key;
                        textView.setText(currWord);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        currCode = "";
    }

    private void fetchWord(String code) {
        currWord += " ";
    }

    private void vibrateHelfMessage() {
        String text = String.valueOf(inputText.getText());
        text = text.toUpperCase();
        for (int i = 0; i < text.length(); i++) {
            String temp = text.substring(i, i+1);
            try {
                String code = HelfJson.getString(temp);
                for (int j = 0; j < code.length(); j++) {
                    if (code.charAt(j) == '1') {
                        vibrateForTime(200);
                    } else if (code.charAt(j) == '2') {
                        vibrateForTime(600);
                    } else {
                        //Thread.s
                        // leep(1000);
                        holdTime(800);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            holdTime(1200);
        }
    }

    private void holdTime(int millis) {
        long expectedTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - expectedTime <= millis) {
            textView.setText("On Hold!");
        }
        textView.setText("Not on Hold!");
    }

    private void vibrateForTime(int millis) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(millis);
        }
    }

}
