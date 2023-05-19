package onBoard.quizUtilities.MultipleChoice;

import onBoard.quizUtilities.MultipleChoice.Exceptions.MaxChoiceCapacity;
import onBoard.quizUtilities.Question;

import java.util.ArrayList;

public class MultipleChoiceQuestion extends Question {
    private Choice correctAnswer;
    protected int maxChoice;
    private ArrayList<QuestionChoice> choices;


    public MultipleChoiceQuestion(String question, int maxChoice, Choice correctAnswer) {
        super(question);
        this.maxChoice = maxChoice;
        choices = new ArrayList<>();
        this.correctAnswer = correctAnswer;
    }

    @Override
    public boolean checkAnswer (Object ans) {
        var attempt = (Choice)ans;
        isCorrect = attempt.equals(correctAnswer) ? true : false;
        return isCorrect;
    }

    public MultipleChoiceQuestion addChoice (String choice){
        // Assigns a letter to the choice
        if (choices.size() >= maxChoice) throw new MaxChoiceCapacity(maxChoice);

        choices.add(new QuestionChoice(Choice.values()[
                choices.size()], choice));
        return this;
    }

    public void log(){
        System.out.println(prompt);
        System.out.println("Choices: ");
        for (var i : choices) {
            System.out.println(i.choiceLetter.name() + ": " + i.choiceString);
        }
        System.out.println("The correct choice is: " + correctAnswer.name());
    }



}

