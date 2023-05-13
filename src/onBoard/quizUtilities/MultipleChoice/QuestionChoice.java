package onBoard.quizUtilities.MultipleChoice;

import java.io.Serializable;

public class QuestionChoice implements Serializable {
    public Choice choiceLetter;
    public String choiceString;

    public QuestionChoice(Choice choiceLetter, String choiceString) {
        this.choiceLetter = choiceLetter;
        this.choiceString = choiceString;
    }
}
