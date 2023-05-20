package onBoard.quizUtilities.MultipleChoice;

import java.util.ArrayList;
import java.util.List;

public class MultipleAnswerQuestion extends MultipleChoiceQuestion{

    protected ArrayList<Choice> correctChoices;
    protected ArrayList<Choice> attempts;
    protected boolean isPartialMarks;



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
        return correctChoices.contains(ans);
    }



    @Override
    public double getAwardedMarks() {
        int maxCounter = correctChoices.size();
        double score = 0;
        for (var i : attempts){
            if (i==null) continue;
            if (correctChoices.contains(i)){
                score += (marks/maxCounter);
            } else score -= (marks/maxCounter);
        } return score > 0 ? score : 0;
    }

    public void clearAttempts(){
        attempts.clear();
    }
}
