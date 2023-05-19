package onBoard.quizUtilities;

public class Identification extends Question {
    protected boolean caseInsensitive = false;
    protected boolean whitespaceInsensitive = false;
    protected String correctAnswer;

    public Identification(String question, String correctAnswer) {
        super(question);
        this.correctAnswer = correctAnswer;
    }

    public Identification setCaseInsensitive(boolean choice){
        caseInsensitive = choice;
        return this;
    }

    public Identification setWhitespaceInsensitive(boolean choice) {
        whitespaceInsensitive = choice;
        return this;
    }

    @Override
    public boolean checkAnswer(Object ans) {
        super.checkAnswer(ans);
        var answer = (String)ans;
        answer = transform(answer);
        var tempAnswer = transform(correctAnswer);
        isCorrect = tempAnswer.equals(answer) ? true : false;
        return isCorrect;
    }

    @Override
    public void log() {
        System.out.println("Question: " + prompt);
        System.out.println("Answer: " + correctAnswer);
    }

    protected String transform(String correctAnswer) {
        correctAnswer = caseInsensitive ? correctAnswer.toLowerCase() : correctAnswer;
        if (whitespaceInsensitive) {
            StringBuilder noSpace = new StringBuilder();
            for (int i = 0; i < correctAnswer.length(); i++){
                if (correctAnswer.charAt(i) == ' ') continue;
                noSpace.append(correctAnswer.charAt(i));
            }
            correctAnswer = noSpace.toString();
        }
        return correctAnswer;
    }


}
