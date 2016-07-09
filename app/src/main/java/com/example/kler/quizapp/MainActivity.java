package com.example.kler.quizapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    //Full answer_id_mask = question<1>Answer<2> where <1> - question number, <2> - answer number
    private static final String ANSWER_ID_MASK_PART_1 = "question";
    private static final String ANSWER_ID_MASK_PART_2 = "Answer";

    // Full correct_answer_mask = question_<1>_correct_answer
    private static final String CORRECT_ANSWER_MASK_PART_1 = "question_";
    private static final String CORRECT_ANSWER_MASK_PART_2 = "_correct_answer";

    // Full answer_type_mask = question_<1>_answer_type where <1> - question number
    private static final String ANSWER_TYPE_MASK_PART_1 = "question_";
    private static final String ANSWER_TYPE_MASK_PART_2 = "_answer_type";


    private static final String ANSWER_TYPE_1 = "single";
    private static final String ANSWER_TYPE_2 = "multiple";
    private static final String ANSWER_TYPE_3 = "text";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();
    }

    public void quickTest(View view) {
        String answerTypeName = ANSWER_TYPE_MASK_PART_1 + 4 + ANSWER_TYPE_MASK_PART_2;
        //int temp = getResId("icon", Drawable.class);
        int temp = getResources().getIdentifier(answerTypeName, "string", getApplicationContext().getPackageName());
        String answerTypeNameValue = getString(temp);
        //String answerTypeText = getString(R.string.question_1_text);

        int answerTypeId = 0;
        try {
            //int answerTypeId = getResources().getIdentifier(getAnswerTypeName(questionNumber), "string", getApplicationContext().getPackageName());
            display("question0");
            answerTypeId = getResources().getIdentifier("question0", "string", getApplicationContext().getPackageName());
            display("id" + answerTypeId);
           //return getString(answerTypeId);

        } catch (Exception e) {
            e.printStackTrace();
            display("some error 1");
            //return "-1";
        }
        try {
            String ttemp = getString(answerTypeId);
            display("ttemp_" + ttemp);
            //return getString(answerTypeId);

        } catch (Exception e) {
            e.printStackTrace();
            display("2 some error 2");
            //return "-1";
        }

        //display(answerTypeName);
        //display(answerTypeNameValue);

    }

    private static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void estimate(View view) {
        int score = 0;
        for (int i = 1; i <= 5; i++) {
            score += getPointsForQuestion(i);
        }
        display("Score = " + score);
    }

    private int getPointsForQuestion(int questionNumber) {
        String answerType = getAnswerTypeValue(questionNumber);
        //int pointForQuestion = getPointsByQuestionType(questionNumber, answerType);
        int point = 0;
        switch (answerType) {
            case ANSWER_TYPE_1:
                point = getPointSingleQuestion(questionNumber);
                display(ANSWER_TYPE_1);
                break;
            case ANSWER_TYPE_2:
                point = getPointMultipleQuestion(questionNumber, answerType);
                display(ANSWER_TYPE_2);
                break;
            case ANSWER_TYPE_3:
                point = getPointTextQuestion(questionNumber, answerType);
                display(ANSWER_TYPE_3);
                break;
            default: {
                display("Error! Unknown answer type in question " + questionNumber + "\n answerType = _" + getAnswerTypeValue(questionNumber) + "_ \n ");
                break;
            }
        }
        return point;
    }

//    private int getPointsByQuestionType(int questionNumber, String answerType) {
//        int point = 0;
//        switch (answerType) {
//            case ANSWER_TYPE_1:
//                point = getPointSingleQuestion(questionNumber);
//                display(ANSWER_TYPE_1);
//                break;
//            case ANSWER_TYPE_2:
//                point = getPointMultipleQuestion(questionNumber, answerType);
//                display(ANSWER_TYPE_2);
//                break;
//            case ANSWER_TYPE_3:
//                point = getPointTextQuestion(questionNumber, answerType);
//                display(ANSWER_TYPE_3);
//                break;
//            default: {
//                display("Error! Unknown answer type in question " + questionNumber + "\n answerType = _" + getAnswerTypeValue(questionNumber) + "_ \n ");
//                break;
//            }
//        }
//        return point;
//    }


    private String getCorrectAnswerName(int questionNumber) {
        String answerTypeName = CORRECT_ANSWER_MASK_PART_1 + questionNumber + CORRECT_ANSWER_MASK_PART_2;
        return answerTypeName;
    }

    private String getCorrectAnswerValue(int questionNumber){
        // 1. getCorrectAnswerName by questionNumber                question_1_correct_answer
        String correctAnswerName = getCorrectAnswerName(questionNumber);

        // 2. getCorrectAnswerValue by questionNumber & correctAnswerName             "2"
        return getStringsValueByName(correctAnswerName);
    }

    private String getCorrectAnswerObjectId (int questionNumber, String correctAnswerValue){
        return ANSWER_ID_MASK_PART_1 + questionNumber + ANSWER_ID_MASK_PART_2 + correctAnswerValue;
        //  question1Answer2
    }
    private int getPointSingleQuestion(int questionNumber){
        int point = 0;
        // 1. get correctAnswerValue by questionNumber                              2
        String correctAnswerValue = getCorrectAnswerValue(questionNumber);

        // 2. get correctAnswerObjectId by questionNumber & correctAnswerValue      question1Answer2        CanUseInMultiple
        String correctAnswerObjectId = getCorrectAnswerObjectId(questionNumber, correctAnswerValue);

        // 3. findById by correctAnswerObjectId                                     <Object>                CanUseInMultiple


        // 4. if (Checked) return 1                                                                         CanUseInMultiple
        return point;
    }

    private int getPointMultipleQuestion(int questionNumber, String answerType){

        return 0;
    }
    private int getPointTextQuestion(int questionNumber, String answerType){
        return 0;
    }

    /**
     * Return string value from strings.xml by string name
     */
    private String getStringsValueByName(String stringName) {
        try {
            //int answerTypeId = getResources().getIdentifier(getAnswerTypeName(questionNumber), "string", getApplicationContext().getPackageName());
            int answerTypeId = getResources().getIdentifier(stringName, "string", getApplicationContext().getPackageName());
            return getString(answerTypeId);

        } catch (Exception e) {
            e.printStackTrace();
            return "-1";
        }
    }

    private String getAnswerTypeValue(int questionNumber) {
        return getStringsValueByName(getAnswerTypeName(questionNumber));
//        try {
//            //String answerTypeNameValue = getString(getResources().getIdentifier(getAnswerTypeName(questionNumber), "string", getApplicationContext().getPackageName()));
//            int answerTypeId = getResources().getIdentifier(getAnswerTypeName(questionNumber), "string", getApplicationContext().getPackageName());
//            return getString(answerTypeId);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "-1";
//        }
    }

    private String getAnswerTypeName(int questionNumber) {
        String answerTypeName = ANSWER_TYPE_MASK_PART_1 + questionNumber + ANSWER_TYPE_MASK_PART_2;
        return answerTypeName;
    }

    private void display(int number) {
        Toast.makeText(this, "_" + number + "_", Toast.LENGTH_SHORT).show();
    }

    private void display(String text) {
        Toast.makeText(this, "_" + text + "_", Toast.LENGTH_SHORT).show();
    }
}

// String to int!!!
//        try {
//            myNum = Integer.parseInt(et.getText().toString());
//        } catch(NumberFormatException nfe) {
//            System.out.println("Could not parse " + nfe);
//        }