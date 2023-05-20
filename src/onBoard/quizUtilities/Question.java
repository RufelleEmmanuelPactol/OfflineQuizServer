package onBoard.quizUtilities;

import java.io.Serializable;

public abstract class Question  implements Serializable {
    protected boolean isCorrect = false;
    protected Object attempt;
    public Object getAnswerAttempt(){
        return attempt;
    }


    public boolean isCorrect() {
        return isCorrect;
    }

    protected String prompt;

    public String getPrompt() {
        return prompt;
    }

    public Question (String question)
    {
        this.prompt = question;
    }

    public boolean checkAnswer (Object ans){
        this.attempt = ans;
        return false;
    }

    public abstract void log();
    protected double marks;
    public Question setMarks(int marks){
        this.marks = marks;
        return this;
    }

    // clears attempt
    public void clearAttempt(){
        attempt = null;
    }

    public double getMarks() {
        return marks;
    }


    // returns the taker's score
    public abstract double getAwardedMarks();

}
