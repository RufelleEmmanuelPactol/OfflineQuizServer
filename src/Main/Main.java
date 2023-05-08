package Main;

import Network.PortHandler;
import QuizQuestions.MultipleChoice.Choice;
import QuizQuestions.Quiz;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Quiz myquiz = new Quiz("Pabs");
        myquiz.addMultipleChoiceQuestion("Who is a?", 4, Choice.A)
                .addChoice("R").addChoice("B").addChoice("C").addChoice("D");
        myquiz.getQuestionNumber(1).checkAnswer(Choice.A);
    }
}