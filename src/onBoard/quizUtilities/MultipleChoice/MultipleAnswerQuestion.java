package onBoard.quizUtilities.MultipleChoice;

import java.util.ArrayList;
import java.util.List;

public class MultipleAnswerQuestion extends MultipleChoiceQuestion{

    protected ArrayList<Choice> correctChoices;

    public ArrayList<Choice> getCorrectAnswers() {
        return correctChoices;
    }

    @Override
    public Choice getCorrectAnswer() {
        System.err.println("Please do not use this method when invoking MultipleAnswerQuestion. Use getCorrectAnswers() instead.");
        throw new RuntimeException();
    }

    public MultipleAnswerQuestion(String question, int maxChoice, Choice[] answers) {
        super(question, maxChoice, null);
        correctChoices = new ArrayList<>(List.of(answers));
    }

    @Override
    public boolean checkAnswer(Object ans) {
        return correctChoices.contains(ans);
    }
}
