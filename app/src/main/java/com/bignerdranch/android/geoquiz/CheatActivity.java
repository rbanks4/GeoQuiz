package com.bignerdranch.android.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CheatActivity extends AppCompatActivity {

    private boolean m_booleanAnswer;

    private TextView m_answerTextView;
    private Button m_showAnswer;
    private TextView m_apiLevel;

    private static final String EXTRA_ANSWER_SHOWN = "AnswerShown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        //get our boolean answer from the intent...else return false
        m_booleanAnswer = getIntent().getBooleanExtra(Question.EXTRA_ANSWER, false);

        m_answerTextView = (TextView) findViewById(R.id.answer_text_view);
        m_showAnswer = (Button) findViewById(R.id.show_answer_button);
        m_showAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m_booleanAnswer == true)
                    m_answerTextView.setText(R.string.true_button);
                else
                    m_answerTextView.setText(R.string.false_button);
                setAnswerShownResult(true);

                int cx = m_showAnswer.getWidth() / 2;
                int cy = m_showAnswer.getHeight() / 2;
                float radius = m_showAnswer.getWidth();

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Animator anim = ViewAnimationUtils
                            .createCircularReveal(m_showAnswer, cx, cy, radius, 0);

                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            m_showAnswer.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    m_showAnswer.setVisibility(View.INVISIBLE);
                }
            }
        });
        m_apiLevel = (TextView) findViewById(R.id.api_level_text_view);
        m_apiLevel.setText("API level "+ Build.VERSION.SDK_INT);
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    public static Intent newIntent(Context packageContext, boolean answer) {
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(Question.EXTRA_ANSWER, answer);
        return i;
    }

}
