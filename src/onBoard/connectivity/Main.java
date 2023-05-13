package onBoard.connectivity;

import onBoard.network.networkUtils.PortHandler;
import onBoard.quizUtilities.Quiz;
import onBoard.quizUtilities.MultipleChoice.Choice;
import onBoard.quizUtilities.Quiz;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(PortHandler.serverAddress, 3306);
        Quiz myquiz = new Quiz("Pabs");
        myquiz.addMultipleChoiceQuestion("Who is a?", 4, Choice.A)
                .addChoice("R").addChoice("B").addChoice("C").addChoice("D");
        System.out.println(myquiz.getQuestionNumber(1).checkAnswer(Choice.A));
        myquiz.log();
        System.out.println("\n");
        SQLConnector sqlc;
        try {
            sqlc = new SQLConnector();
            // posts a quiz to the database
            sqlc.postQuiz(myquiz);
            Quiz newquiz = sqlc.getQuiz(2);
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