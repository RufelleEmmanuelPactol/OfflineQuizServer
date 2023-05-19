package onBoard.quizUtilities;

import java.io.Serializable;

public abstract class Question  implements Serializable {
    protected boolean isCorrect = false;

    public boolean isCorrect() {
        return isCorrect;
    }

    public String prompt;

    public Question (String question)
    {
        this.prompt = question;
    }

    public abstract boolean checkAnswer (Object ans);

    public abstract void log();
    private int marks;
    public Question setMarks(int marks){
        this.marks = marks;
        return this;
    }

    public int getMarks() {
        return marks;
    }

}
