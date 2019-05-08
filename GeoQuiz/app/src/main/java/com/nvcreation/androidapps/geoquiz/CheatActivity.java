package com.nvcreation.androidapps.geoquiz;

import android.animation.Animator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private boolean mAnswerIsTrue;

    private boolean mIsAnswerShown;
    private TextView mAnswerTextView;
    private Button mShowAnswerButton;

    private static final String KEY_ANSWER = "answer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if(savedInstanceState != null){
            mIsAnswerShown = savedInstanceState.getBoolean(KEY_ANSWER, false); //On rotation - mIsAnswerShown will be retained - Chapter 5 - Exercise 1
            if(mIsAnswerShown){ //on rotation if the answer was true then the same shall be sent as the result of the Intent - Chapter 5 - Exercise 1
                setAnswerShowResult(true);
            }
        }
        mAnswerIsTrue = getIntent().getBooleanExtra("EXTRA_ANSWER_IS_TRUE", false); //getting the extra from QuizActivity

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);

        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAnswerIsTrue){
                    mAnswerTextView.setText(R.string.true_button);
                }
                else{
                    mAnswerTextView.setText(R.string.false_button);
                }
                mIsAnswerShown = true;
                setAnswerShowResult(true);

                int cx = mShowAnswerButton.getWidth();
                int cy = mShowAnswerButton.getHeight();
                float radius = mShowAnswerButton.getWidth();

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                    anim.addListener(new Animator.AnimatorListener() {

                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                        mShowAnswerButton.setVisibility(View.INVISIBLE);

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }


                    });
                    anim.start();
                }
                else{
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(KEY_ANSWER, mIsAnswerShown); //On rotation - mIsAnswerShown will be retained - Chapter 5 - Exercise 1

    }

    private void setAnswerShowResult(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra("EXTRA_ANSWER_SHOWN", isAnswerShown);
        setResult(RESULT_OK, data);
    }
}
