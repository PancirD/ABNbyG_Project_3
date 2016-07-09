package com.example.kler.quizapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final static int TEMP = R.id.question0Answer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVisibilityAdditionalButtons(View.GONE);
        /* TODO View.GONE in variables. Nead safe state in rotation */
    }

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
        /* TODO add: cant edit any answer object
           TODO Safe Score to mail message
          */
    }

    private int getPointFromSingleCheckAnswer(int id) {
        RadioButton radioButton = (RadioButton) findViewById(id);
        if (radioButton.isChecked()) {
            return 1;
        }
        return 0;
    }

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

    private int getPointFromTextAnswer(int id, String answer) {
        EditText editText = (EditText) findViewById(id);
        String text = editText.getText().toString();
        if (text.equals(answer)) {
            return 1;
        }
        return 0;
    }

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
        setVisibilityAdditionalButtons(View.VISIBLE);
    }

    private String getName() {
        EditText editText = (EditText) findViewById(R.id.question0Answer1);
        String name = editText.getText().toString().trim();
        if (name.equals("")) {
            name = "Anonymous";
        }
        return name;
    }

    private void setVisibilityAdditionalButtons(int visibility) {
        Button buttonReset = (Button) findViewById(R.id.resetButton);
        buttonReset.setVisibility(visibility);
        Button buttonMail = (Button) findViewById(R.id.mailButton);
        buttonMail.setVisibility(visibility);
    }

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
        /* TODO add: can edit any answer object  */
    }

    public void composeEMail(View view){
        Intent eMailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "", null));
        eMailIntent.putExtra(Intent.EXTRA_SUBJECT, getName());
        eMailIntent.putExtra(Intent.EXTRA_TEXT, "score message");
        if (eMailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(eMailIntent, "Send email..."));
        }
        else {
            Toast.makeText(getApplicationContext(), "Can't send mail :(", Toast.LENGTH_SHORT).show();
        }

    }

    /*
* ***************************
* **************************************************
* **************************************************************************
* * ************************************************************************************************
* **************************************************************************
* **************************************************
* ***************************
* */

    private void clearRadioGroupById(int id) {
        RadioGroup radioGroup = (RadioGroup) findViewById(id);
        radioGroup.clearCheck();
    }

    public void quickTestNew(View view) {
//        clearRadioGroupById(R.id.question1AnswersGroup);
//        clearRadioGroupById(R.id.question2AnswersGroup);

//        RadioButton radioButton = (RadioButton) findViewById(R.id.question1Answer1);
//        radioButton.setChecked(false);
//
//        CheckBox checkBox = (CheckBox) findViewById(R.id.question5Answer1);
//        checkBox.setChecked(false);
        /* TODO
        */
    }

    private void unCheckObjectById(int id) {
        CompoundButton checkBox = (CompoundButton) findViewById(id);
        checkBox.setChecked(false);
    }
}


// String to int!!!
//        try {
//            myNum = Integer.parseInt(et.getText().toString());
//        } catch(NumberFormatException nfe) {
//            System.out.println("Could not parse " + nfe);
//        }