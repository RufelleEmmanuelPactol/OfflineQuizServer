package onBoard.connectivity;

import onBoard.dataClasses.ClassData;
import onBoard.network.networkUtils.NetworkGlobals;
import onBoard.network.networkUtils.PortHandler;
import onBoard.quizUtilities.Quiz;
import onBoard.quizUtilities.MultipleChoice.Choice;

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

        myquiz.addMultipleAnswerQuestion("Question", 3, new Choice[]{Choice.A, Choice.B});
        myquiz.setTimeOpen().setYear(2025).setDay(30).setMonth(12).setHour(12).setMinute(00);
        myquiz.setTimeClose().setYear(2026).setDay(30).setMonth(12).setHour(12).setMinute(00);
        System.out.println(myquiz.getTimeOpen());
        System.out.println("\n");
        var cls = new ClassData();
        cls.classId = 0;
        NetworkGlobals.currentClass = cls;
        SQLConnector sqlc;
        try {
            sqlc = new SQLConnector();
            sqlc.postQuiz(myquiz, cls);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}