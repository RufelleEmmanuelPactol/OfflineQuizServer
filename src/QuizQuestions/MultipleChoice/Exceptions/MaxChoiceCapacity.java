package QuizQuestions.MultipleChoice.Exceptions;

public class MaxChoiceCapacity extends RuntimeException {
    private int max;

    public MaxChoiceCapacity(int max){
        this.max = max;
    }
    @Override
    public String toString() {
        return "Choice max overflow, max choice at " + max + '.';
    }
}
