package com.nvcreation.androidapps.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton; //Using ImageButton instead of Button - Chapter 2 - Exercise 3
    private ImageButton mPrevButton; //Using ImageButton instead of Button - Chapter 2 - Exercise 3
    private Button mCheatButton;
    private TextView mQuestionTextView;
    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),

    };

    private static final String TAG = "QuizActivity";

    private static final String KEY_INDEX = "index";

    private static final String KEY_CHEATER = "cheater";

    private static final int REQUEST_CODE_CHEAT = 0;

    private boolean mIsCheater;

    private int mCurrentIndex = 0;
    private int mPrevIndex = mCurrentIndex - 1;
    private int mNoOfCorrectAns = 0;
    private int mNoOfIncorrectAns = 0;
    private ArrayList<Integer> mQuestionsAnswered = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"JV Testing - OnCreate");

        if(savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);
            mIsCheater = savedInstanceState.getBoolean(KEY_CHEATER, false); //On rotation - mIsCheater will be retained - Chapter 5 - Exercise 2
        }
        setContentView(R.layout.activity_quiz);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        // Listener Added to TextView - Chapter 2 - Exercise 1
        mQuestionTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });


        mTrueButton = (Button) findViewById(R.id.t_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Toast.makeText(QuizActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT).show();
                checkAnswer(true);

            }
        });

        mFalseButton = (Button) findViewById(R.id.f_button);

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(QuizActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
                checkAnswer(false);
            }
        });

        mCheatButton =(Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

                //Explicit Intent - Starting another activity in the same package

                Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
                intent.putExtra("EXTRA_ANSWER_IS_TRUE", answerIsTrue); //extra received by CheatActivity
                startActivityForResult(intent, REQUEST_CODE_CHEAT);

            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        // Previous Button Added - Chapter 2 - Exercise 2
        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentIndex == 0) {
                    mCurrentIndex = mQuestionBank.length - 1;

                } else {
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;

                }
                updateQuestion();
            }
        });
        updateQuestion();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_CODE_CHEAT){
            if(data == null){
                return;
            }
        }

        mIsCheater = data.getBooleanExtra("EXTRA_ANSWER_SHOWN", false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "JV Testing - OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "JV Testing - OnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "JV Testing - OnPause");
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG,"JV Testing - OnSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(KEY_CHEATER, mIsCheater); //On rotation - mIsCheater will be retained - Chapter 5 - Exercise 2
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "JV Testing - OnStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "JV Testing - OnDestroy");
    }

    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);

        if(!mQuestionsAnswered.contains(mCurrentIndex)) {
            mTrueButton.setClickable(true);
            mFalseButton.setClickable(true);
        } else{
            Toast.makeText(this, "Question Answered.", Toast.LENGTH_LONG).show();
            mTrueButton.setClickable(false);
            mFalseButton.setClickable(false);

        }
    }

    private void checkAnswer(boolean userPressedTrue){
        Log.d(TAG, "mCurrentIndex" + mCurrentIndex);
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if(!mQuestionsAnswered.contains(mCurrentIndex)) {

            mQuestionsAnswered.add(mCurrentIndex);
            if (mIsCheater) {
                Toast.makeText(this, R.string.judgement_toast, Toast.LENGTH_SHORT).show();

            }

                if (userPressedTrue == answerIsTrue) {
                    messageResId = R.string.correct_toast;

                    // Counting No. of Correct Answers and disabling the buttons of the quiz answered to prevent repeat answers - Chapter 3 - Exercise 1
                    mNoOfCorrectAns++;
                    mTrueButton.setClickable(false); //Disabling Buttons once answered, so user can't come back and grading is not manipulated for Chapter 3 - Exercise 2 and can's come back to cheated answer - Chapter 5 - Exercise 3
                    mFalseButton.setClickable(false);

                } else {
                    messageResId = R.string.incorrect_toast;

                    // Counting No. of InCorrect Answers and disabling the buttons of the quiz answered to prevent repeat answers - Chapter 3 - Exercise 1
                    mNoOfIncorrectAns++;
                    mTrueButton.setClickable(false);
                    mFalseButton.setClickable(false);

                }

        }
        Toast myToast = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT);
        myToast.setGravity(Gravity.TOP, 0, 0);
        myToast.show();

        //  Grading at the end of all questions answered - Chapter 3 - Exercise 2

        if((mNoOfCorrectAns + mNoOfIncorrectAns) == (mQuestionBank.length)){
            int mlength = mQuestionBank.length;
            int mCalc =  (mNoOfCorrectAns*100)/mlength;
            Toast.makeText(this, "Correct Answer: "+mNoOfCorrectAns+" | Incorrect Answer: "+mNoOfIncorrectAns+" | Total Percentage :"+mCalc, Toast.LENGTH_LONG).show();

        }

    }
}