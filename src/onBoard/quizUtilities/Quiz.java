package onBoard.quizUtilities;

import onBoard.quizUtilities.MultipleChoice.MultipleAnswerQuestion;
import onBoard.quizUtilities.MultipleChoice.MultipleChoiceQuestion;
import onBoard.quizUtilities.MultipleChoice.Choice;
import onBoard.quizUtilities.MultipleChoice.MultipleChoiceQuestion;

import java.io.Serializable;
import java.util.ArrayList;

public class Quiz implements Serializable {
    protected ArrayList<Question> questions;
    protected String quizName;
    public int teacherID;
    public int classID;



    public Identification addIdentificationQuestion (String question, String correctStringAnswer) {
        Identification q = new Identification(question, correctStringAnswer);
        questions.add(q);
        return q;
    }

    public MultipleChoiceQuestion addMultipleChoiceQuestion (String question, int maxChoice, Choice correctAnswer){
        MultipleChoiceQuestion q = new MultipleChoiceQuestion(question, maxChoice, correctAnswer);
        questions.add(q);
        return q;
    }

    public Question getQuestionNumber (int n) {
        return questions.get(n-1);
    }
    public Identification addIdentificationQuestion (String question, String correctStringAnswer, int replaceQuestionNo) {
        Identification q = new Identification(question, correctStringAnswer);
        questions.set(replaceQuestionNo+1, q);
        return q;
    }

    public MultipleChoiceQuestion addMultipleChoiceQuestion (String question, int maxChoice, Choice correctAnswer, int replaceQuestionNo){
        MultipleChoiceQuestion q = new MultipleChoiceQuestion(question, maxChoice, correctAnswer);
        questions.set(replaceQuestionNo + 1, q);
        return q;
    }

    public MultipleAnswerQuestion addMultipleAnswerQuestion (String question, int maxChoice, Choice[] correctAnswer){
        MultipleAnswerQuestion q = new MultipleAnswerQuestion(question, maxChoice, correctAnswer);
        questions.add(q);
        return q;
    }

    public MultipleAnswerQuestion addMultipleAnswerQuestion (String question, int maxChoice, Choice[] correctAnswer, int replaceQuestionNo){
        MultipleAnswerQuestion q = new MultipleAnswerQuestion(question, maxChoice, correctAnswer);
        questions.set(replaceQuestionNo + 1, q);
        return q;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public Quiz setTeacherID(int teacherID) {
        this.teacherID = teacherID;
        return this;
    }

    public int getClassID() {
        return classID;
    }

    public Quiz setClassID(int classID) {
        this.classID = classID;
        return this;
    }

    public Quiz(String quizName) {
        this.quizName = quizName;
        questions = new ArrayList<>();
    }

    public void log(){
        System.out.println(questions.size());
        for (int i=0; i<questions.size(); i++){
            System.out.println("Question " + (i+1) + ": ");
            questions.get(i).log();
        }
    }

    /*
    Returns the highest possible score.
     */
    public int getTotalMarks(){
        int marks = 0;
        for (var i : questions){
            marks+=i.getMarks();
        } return marks;
    }

    /*
    Returns the user's score.
     */
    public int getMarks(){
        int marks = 0;
        for (var i : questions){
            if (i.isCorrect())
            marks+=i.getMarks();
        } return marks;
    }


}