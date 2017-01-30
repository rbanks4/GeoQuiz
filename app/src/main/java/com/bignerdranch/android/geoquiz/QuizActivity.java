package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private Button          m_trueButton;
    private Button          m_falseButton;
    private ImageButton     m_nextButton;
    private ImageButton     m_prevButton;
    private Button          m_cheatButton;
    private TextView        m_questionTextView;

    //create our questions class
    private Question[] m_questionsBank = new Question[]{
            //add our questions
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.questoin_americas, true),
            new Question(R.string.question_asia, true)
    };

    /**used to track what question we are on in the array*/
    private int m_currIndex = 0;
    /**used to log our actions and flow*/
    private static final String QUIZ_LOG = "QuizLog";
    /**the key that holds the index of our current question on save state instances (like rotating the screen)*/
    private static final String Q_INDEX = "QuestionIndex";
    /**this key will save the fact that our user is a cheater or not*/
    private static final String Q_CHEATER = "QuestionCheater";
    /**the index number for the child called when we create our Cheat activity, this reference number can be used to obtain the correct data from the correct child*/
    private static final int REQUEST_CHEAT = 0;

    //used to see if the user has cheated or not
    //private boolean m_IsCheater = false;
    private boolean[] m_questionsCheatedList;// = new boolean[m_questionsBank.length];

    //NOTE: we want to save an array of booleans to see if the user has cheated on an answer; we will then generate them into our Question model upon recovery
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(QUIZ_LOG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        m_questionsCheatedList = new boolean[m_questionsBank.length];
        int cheatListIndex = 0;
        for (boolean b : m_questionsCheatedList) {
            m_questionsCheatedList[cheatListIndex] = false;
            cheatListIndex++;
        }

        if (savedInstanceState != null) {
            //we get the saved value of our index, else we get 0
            m_currIndex = savedInstanceState.getInt(Q_INDEX,0);
            //we see if the user was a cheater, else they are not
            m_questionsCheatedList = savedInstanceState.getBooleanArray(Q_CHEATER);
        }

        m_questionTextView = (TextView) findViewById(R.id.question_text);
        m_questionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(QUIZ_LOG, "question pressed");
                nextQuestion();
            }
        });

        //create our buttons
        buildButtonsAndListeners();
        refreshQuestion();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(QUIZ_LOG, "onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(QUIZ_LOG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(QUIZ_LOG, "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(QUIZ_LOG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(QUIZ_LOG, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(QUIZ_LOG, "onSaveInstanceState, QuestionIndex = " + m_currIndex);
        outState.putInt(Q_INDEX, m_currIndex);
        outState.putBooleanArray(Q_CHEATER, m_questionsCheatedList);
    }

    private void nextQuestion() {
        m_currIndex = (m_currIndex + 1) % m_questionsBank.length;
        Log.i(QUIZ_LOG, "m_currIndex: " + m_currIndex);//, new Exception());
        refreshQuestion();
    }

    private void prevQuestion() {
        if(m_currIndex == 0)
            m_currIndex = (m_questionsBank.length - 1);
        else
            m_currIndex = (m_currIndex - 1) % m_questionsBank.length;
        Log.i(QUIZ_LOG, "m_currIndex: " + m_currIndex);//, new Exception());
        refreshQuestion();
    }

    /**
     * this function will build all of our buttons...add more if you like
     */
    private void buildButtonsAndListeners() {
        Log.i(QUIZ_LOG, "building buttons and listeners");
        //create and add a listener to our true button
        m_trueButton = (Button)findViewById(R.id.true_button);
        m_trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(QUIZ_LOG, "true pressed");
                userPressedTrue(true);
            }
        });

        //create and add a listener to our false button...blah
        m_falseButton = (Button)findViewById(R.id.false_button);
        m_falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(QUIZ_LOG, "false pressed");
                userPressedTrue(false);
            }
        });

        //create a next button to show the next question
        m_nextButton = (ImageButton)findViewById(R.id.next_button);
        m_nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(QUIZ_LOG, "next pressed");
                nextQuestion();
            }
        });

        //create a prev button to show the next question
        m_prevButton = (ImageButton)findViewById(R.id.prev_button);
        m_prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(QUIZ_LOG, "prev pressed");
                prevQuestion();
            }
        });

        //create a cheat button to show CheatActivity
        m_cheatButton = (Button) findViewById(R.id.cheat_button);
        m_cheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(QUIZ_LOG, "cheat pressed");
                Intent i = CheatActivity.newIntent(
                        QuizActivity.this,
                        m_questionsBank[m_currIndex].isAnswerTrue()
                );
                startActivityForResult(i, REQUEST_CHEAT);
            }
        });
    }

    /**
     * shows the next question in the list
     */
    private void refreshQuestion(){
        Log.i(QUIZ_LOG, "questions refreshed");
        int question = m_questionsBank[m_currIndex].getTextResID();
        m_questionTextView.setText(question);
    }

    /**
     * checks to see if our answer is true or false for the currently selected question
     * @param answer - the answer the user pressed
     */
    private void userPressedTrue(boolean answer){
        Log.i(QUIZ_LOG, "checking answer");
        boolean answerIsTrue = m_questionsBank[m_currIndex].isAnswerTrue();
        int msg = 0;

        if (m_questionsCheatedList[m_currIndex]) {
            msg = R.string.judgment_toast;
        }
        else {
            if (answerIsTrue == answer) {
                msg = R.string.correct_toast;
                Log.i(QUIZ_LOG, "answer is right");
            } else {
                msg = R.string.incorrect_toast;
                Log.i(QUIZ_LOG, "answer is wrong");
            }
        }

        Toast.makeText(QuizActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CHEAT) {
            if (data == null) {
                return;
            }
            m_questionsCheatedList[m_currIndex] = CheatActivity.wasAnswerShown(data);
            if (m_questionsCheatedList[m_currIndex]) {
                m_questionsCheatedList[m_currIndex] = true;
            }
        }
    }

}
