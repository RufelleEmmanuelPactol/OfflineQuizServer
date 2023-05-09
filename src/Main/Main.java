package Main;

import Network.AuthToken;
import Network.PortHandler;
import QuizQuestions.MultipleChoice.Choice;
import QuizQuestions.Quiz;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        var quiz = new Quiz("Hello!");
        quiz.addMultipleChoiceQuestion("Who is the you?", 3, Choice.B)
                .addChoice("Hello").addChoice("No").addChoice("R");
        SQLConnector connector = new SQLConnector();
        connector.postQuiz(quiz);

    }
}