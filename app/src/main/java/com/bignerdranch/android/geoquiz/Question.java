package com.bignerdranch.android.geoquiz;

/**
 * Created by RBanks on 8/17/2016.
 */
public class Question {
    private int m_textResID;
    private boolean m_AnswerTrue;

    //the key for the answer we pass through in an intent
    public static final String EXTRA_ANSWER = "Answer";

    public int getTextResID() {
        return m_textResID;
    }

    public void setTextResID(int textResID) {
        m_textResID = textResID;
    }

    public boolean isAnswerTrue() {
        return m_AnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        m_AnswerTrue = answerTrue;
    }

    public Question(int textResID, boolean answerTrue){
        m_textResID = textResID;
        m_AnswerTrue = answerTrue;
    }
}
