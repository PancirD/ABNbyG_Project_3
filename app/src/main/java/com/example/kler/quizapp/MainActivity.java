package com.example.kler.quizapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final static String APP_PREFERENCES_FILE_NAME = "my_preferences";
    private final static String APP_PREFERENCES_VISIBILITY = "visibility_additional_buttons";
    private final static String APP_PREFERENCES_LAST_SCORE = "last_score";
    private final static int VISIBILITY_DEFAULT = View.GONE;
    private final static int VISIBILITY_VISIBLE = View.VISIBLE;
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreferences = getSharedPreferences(APP_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        setVisibilityAdditionalButtons(mPreferences.getInt(APP_PREFERENCES_VISIBILITY, VISIBILITY_DEFAULT));
    }

    /**
     * Save Additional buttons visibility
     */
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(APP_PREFERENCES_VISIBILITY, getVisibilityAdditionalButtons());
        editor.apply();
    }

    /**
     * Math and display score by doneButton click
     */
    public void getResult(View view) {
        int score = 0;
        // check right answers
        score += getPointFromSingleCheckAnswer(R.id.question1Answer1);
        score += getPointFromSingleCheckAnswer(R.id.question2Answer3);
        score += getPointFromMultiCheckAnswer(
                R.id.question3Answer1, true, R.id.question3Answer2, true,
                R.id.question3Answer3, false, R.id.question3Answer4, true);
        score += getPointFromTextAnswer(R.id.question4Answer1, "RatingBar");
        score += getPointFromMultiCheckAnswer(
                R.id.question5Answer1, false, R.id.question5Answer2, true,
                R.id.question5Answer3, true, R.id.question5Answer4, false);
        displayScore(score, 5);
        saveScoreForMail(score);
        setVisibilityAdditionalButtons(VISIBILITY_VISIBLE);
    }

    /**
     * get RadioButton id.
     * return 1 if isChecked and 0 if not checked
     */
    private int getPointFromSingleCheckAnswer(int id) {
        RadioButton radioButton = (RadioButton) findViewById(id);
        if (radioButton.isChecked()) {
            return 1;
        }
        return 0;
    }

    /**
     * get CheckBoxes id and boolean check station.
     * return 1 if all CheckBoxes checked as intended. Else return 0
     */
    private int getPointFromMultiCheckAnswer(int id1, boolean checked1, int id2, boolean checked2,
                                             int id3, boolean checked3, int id4, boolean checked4) {
        CheckBox checkBox1 = (CheckBox) findViewById(id1);
        CheckBox checkBox2 = (CheckBox) findViewById(id2);
        CheckBox checkBox3 = (CheckBox) findViewById(id3);
        CheckBox checkBox4 = (CheckBox) findViewById(id4);
        if (checkBox1.isChecked() == checked1 && checkBox2.isChecked() == checked2 &&
                checkBox3.isChecked() == checked3 && checkBox4.isChecked() == checked4) {
            return 1;
        }
        return 0;
    }
    /**
     * Get EditText id and String correct answer.
     * return 1 if EditText.text.equals answer.  Else return 0
     */
    private int getPointFromTextAnswer(int id, String answer) {
        EditText editText = (EditText) findViewById(id);
        String text = editText.getText().toString();
        if (text.equals(answer)) {
            return 1;
        }
        return 0;
    }

    /*
     * Display score with Toast
     */
    private void displayScore(int score, int maxScore) {
        String congratulationText;
        String name = getName();
        if (score == maxScore) {
            congratulationText = "Congratulations, " + name + ", you are the Best! <3";
        } else if (score == 0) {
            congratulationText = "No one right answer, " + name + " :'( \nWould you like learn Android? ;)";
        } else {
            congratulationText = "Well done, " + name + ", you have " + score + " of " + maxScore + " correct answers!";
        }
        Toast.makeText(this, congratulationText, Toast.LENGTH_LONG).show();
    }
    /*
     * return user name
     */
    private String getName() {
        EditText editText = (EditText) findViewById(R.id.question0Answer1);
        String name = editText.getText().toString().trim();
        if (name.equals("")) {
            name = "Anonymous";
        }
        return name;
    }
    /*
     * return current visibility Additional Buttons
     */
    private int getVisibilityAdditionalButtons() {
        Button additionalButton = (Button) findViewById(R.id.resetButton);
        return additionalButton.getVisibility();
    }

    /*
     * set visibility Additional Buttons
     */
    private void setVisibilityAdditionalButtons(int visibility) {
        Button additionalButton;
        additionalButton = (Button) findViewById(R.id.resetButton);
        additionalButton.setVisibility(visibility);
        additionalButton = (Button) findViewById(R.id.mailButton);
        additionalButton.setVisibility(visibility);
    }

    /*
     * resetButton onClick
     * reset all answers: unCheck checkable objects and clear EditText
     * and hide Additional buttons
     */
    public void resetAnswers(View view) {
        // set unChecked all RadioButtons and CheckBoxes
        int[] checkableObject = {
                R.id.question1Answer1, R.id.question1Answer2, R.id.question1Answer3,
                R.id.question2Answer1, R.id.question2Answer2, R.id.question2Answer3,
                R.id.question3Answer1, R.id.question3Answer2, R.id.question3Answer3, R.id.question3Answer4,
                R.id.question5Answer1, R.id.question5Answer2, R.id.question5Answer3, R.id.question5Answer4};
        for (int id : checkableObject) {
            unCheckObjectById(id);
        }
        //clear text answer
        EditText editText = (EditText) findViewById(R.id.question4Answer1);
        editText.setText("");
        setVisibilityAdditionalButtons(View.GONE);
    }

    /*
     * unCheck checkable objects by id
     */
    private void unCheckObjectById(int id) {
        CompoundButton checkBox = (CompoundButton) findViewById(id);
        checkBox.setChecked(false);
    }

    /*
     * mailButton onClick
     * create messages and use Intent for prepare mail with quiz score
     */
    public void composeEMail(View view) {
        String messageText = getScoreMessage();
        String subjectText = "Quiz score from " + getName();
        Intent eMailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "", null));
        eMailIntent.putExtra(Intent.EXTRA_SUBJECT, subjectText);
        eMailIntent.putExtra(Intent.EXTRA_TEXT, messageText);
        if (eMailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(eMailIntent, "Send email..."));
        } else {
            Toast.makeText(getApplicationContext(), "Can't send mail :(", Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * return score message for mail
     */
    private String getScoreMessage() {
        int score = mPreferences.getInt(APP_PREFERENCES_LAST_SCORE, 0);
        return "My last score in Quiz App = " + score + "!";
    }

    /*
     * save score in Prefences for mail
     */
    private void saveScoreForMail(int score) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(APP_PREFERENCES_LAST_SCORE, score);
        editor.apply();
    }
}