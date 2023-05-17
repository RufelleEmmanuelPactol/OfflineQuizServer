package onBoard.quizUtilities.MultipleChoice;

import java.util.ArrayList;
import java.util.List;

public class MultipleAnswerQuestion extends MultipleChoiceQuestion{

    ArrayList<Choice> correctChoices;


    public MultipleAnswerQuestion(String question, int maxChoice, Choice[] answers) {
        super(question, maxChoice, null);
        correctChoices = new ArrayList<>(List.of(answers));
    }

    @Override
    public boolean checkAnswer(Object ans) {
        return correctChoices.contains(ans);
    }
}
