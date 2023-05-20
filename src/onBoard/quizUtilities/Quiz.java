package onBoard.quizUtilities;

import onBoard.network.utils.DateBuilder;
import onBoard.quizUtilities.MultipleChoice.MultipleAnswerQuestion;
import onBoard.quizUtilities.MultipleChoice.MultipleChoiceQuestion;
import onBoard.quizUtilities.MultipleChoice.Choice;

import java.io.Serializable;
import java.util.ArrayList;

public class Quiz implements Serializable {
    public int quizID;
    protected ArrayList<Question> questions;
    protected String quizName;
    public int teacherID;
    public int classID;
    private DateBuilder opens;
    private DateBuilder closes;
    public boolean isAttempted = false;



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
            if (questions.get(i) instanceof MultipleAnswerQuestion att) for (var attempts : (ArrayList)att.getAnswerAttempt()) System.out.println("Attempts were: " + attempts);
            else System.out.println("Attempt was: " + questions.get(i).attempt);
            System.out.println("Status is: " + questions.get(i).isCorrect);
        }
    }

    /*
    Returns the highest possible score.
     */
    public double getTotalMarks(){
        int marks = 0;
        for (var i : questions){
            marks+=i.getMarks();
        } return marks;
    }

    public int getQuestionCount (){
        return questions.size();
    }

    /*
    Returns the user's score.
     */
    public double getMarks(){
        double marks = 0;
        for (var i : questions){
            marks += i.getAwardedMarks();
        } return marks;
    }

    public DateBuilder setTimeClose(){
        this.closes = new DateBuilder();
        return this.closes;
    }

    public void setTimeClose(DateBuilder builder){
        closes = builder;
    }


    public String getQuizName() {
        return quizName;
    }

    public DateBuilder setTimeOpen(){
        this.opens = new DateBuilder();
        return this.opens;
    }

    public void setTimeOpen(DateBuilder builder){
        opens = builder;
    }

    public DateBuilder getTimeOpen() {
        return opens;
    }

    public DateBuilder getTimeClose() {
        return closes;
    }

    public ArrayList getQuestions () {return questions;}
}
