package Main;

import Network.PortHandler;
import QuizQuestions.MultipleChoice.Choice;
import QuizQuestions.Quiz;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Quiz myquiz = new Quiz("Pabs");
        myquiz.addMultipleChoiceQuestion("Who is a?", 4, Choice.A)
                .addChoice("R").addChoice("B").addChoice("C").addChoice("D");
        System.out.println(myquiz.getQuestionNumber(1).checkAnswer(Choice.A));
        myquiz.log();
        System.out.println("\n");
        SQLConnector sqlc;
        try {
            sqlc = new SQLConnector();
            sqlc.postQuiz(myquiz);
            Quiz newquiz = sqlc.getQuiz(1);
            System.out.println(newquiz.getQuestionNumber(1).checkAnswer(Choice.A));
            newquiz.log();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}