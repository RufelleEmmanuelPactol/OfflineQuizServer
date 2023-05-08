package QuizQuestions;

import java.io.Serializable;

public abstract class Question  implements Serializable {
    protected String prompt;

    public Question (String question)
    {
        this.prompt = question;
    }

    public abstract boolean checkAnswer (Object ans);

    public abstract void log();



}
