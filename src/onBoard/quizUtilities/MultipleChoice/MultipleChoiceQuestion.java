package onBoard.quizUtilities.MultipleChoice;

import onBoard.network.exceptions.MaxChoiceCapacity;
import onBoard.quizUtilities.Question;

import java.util.ArrayList;

public class MultipleChoiceQuestion extends Question {
    private Choice correctAnswer;
    protected int maxChoice;
    protected ArrayList<QuestionChoice> choices;


    public MultipleChoiceQuestion(String question, int maxChoice, Choice correctAnswer) {
        super(question);
        this.maxChoice = maxChoice;
        choices = new ArrayList<>();
        this.correctAnswer = correctAnswer;
    }

    public ArrayList<QuestionChoice> getChoices() {
        return choices;
    }

    @Override
    public boolean checkAnswer (Object ans) {
        if (ans==null) return false;
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

    public void addChoiceList(String[] choice){
        for (int i=0; i<maxChoice; i++){
            addChoice(choice[i]);
        }
    }



}

