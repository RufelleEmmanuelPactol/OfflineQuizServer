package onBoard.quizUtilities.MultipleChoice;

import java.util.ArrayList;
import java.util.List;

public class MultipleAnswerQuestion extends MultipleChoiceQuestion{

    protected ArrayList<Choice> correctChoices;
    protected ArrayList<Choice> attempts;
    protected boolean isPartialMarks;


    public ArrayList<Choice> getCorrectChoices() {
        return correctChoices;
    }

    public MultipleAnswerQuestion setCorrectChoices(ArrayList<Choice> correctChoices) {
        this.correctChoices = correctChoices;
        return this;
    }

    public ArrayList<Choice> getAttempts() {
        return attempts;
    }

    public MultipleAnswerQuestion setAttempts(ArrayList<Choice> attempts) {
        this.attempts = attempts;
        return this;
    }

    public boolean isPartialMarks() {
        return isPartialMarks;
    }

    public MultipleAnswerQuestion setPartialMarks(boolean partialMarks) {
        isPartialMarks = partialMarks;
        return this;
    }

    public MultipleAnswerQuestion(String question, int maxChoice, Choice[] answers) {
        super(question, maxChoice, null);
        correctChoices = new ArrayList<>(List.of(answers));
        attempts = new ArrayList<>();
        isPartialMarks = true;
    }

    public MultipleAnswerQuestion(String question, int maxChoice, Choice[] answers, boolean isPartialMarks) {
        super(question, maxChoice, null);
        correctChoices = new ArrayList<>(List.of(answers));
        attempts = new ArrayList<>();
        this.isPartialMarks = isPartialMarks;
    }

    // One correct answer is partial credit
    public void setPartialMarks (){
        isPartialMarks = true;
    }
    
    // One correct answer is full credit
    public void setFullMarks(){
        isPartialMarks = false;
    }


    @Override
    public Object getAnswerAttempt() {
        return attempts;
    }

    @Override
    public boolean checkAnswer(Object ans) {
        attempts.add((Choice)ans);
        if (correctChoices.contains(ans)) isCorrect = true;
        return correctChoices.contains(ans);
    }



    @Override
    public double getAwardedMarks() {
        if (isPartialMarks)
        {
            int maxCounter = correctChoices.size();
            double score = 0;
            for (var i : attempts) {
                if (i == null) continue;
                if (correctChoices.contains(i)) {
                    score += (marks / maxCounter);
                    isCorrect = true;
                } else score -= (marks / maxCounter);
            }
            return score > 0 ? score : 0;
        }

        boolean correctYet = false;

        for (var i : attempts){
            correctYet = correctChoices.contains(i) ? true : false;
        } if (correctYet) {
            isCorrect = true;
            return marks;
        }
        return 0;
    }

    public void clearAttempts(){
        attempts.clear();
    }

    @Override
    public void log(){
        System.out.println(prompt);
        System.out.println("Choices: ");
        for (var i : choices) {
            System.out.println(i.choiceLetter.name() + ": " + i.choiceString);
        }
        System.out.println("The correct choices are:\n");
        for (var x : correctChoices) {
            System.out.println("CHOICE: " + x.name() + choices.get(x.ordinal()));
        }

    }
}
