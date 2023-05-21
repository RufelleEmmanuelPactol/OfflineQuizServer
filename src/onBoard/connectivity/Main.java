package onBoard.connectivity;

import onBoard.dataClasses.ClassData;
import onBoard.network.networkUtils.NetworkGlobals;
import onBoard.network.networkUtils.NetworkUtils;
import onBoard.network.networkUtils.PortHandler;
import onBoard.quizUtilities.MultipleChoice.MultipleAnswerQuestion;
import onBoard.quizUtilities.Quiz;
import onBoard.quizUtilities.MultipleChoice.Choice;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws Exception {
        NetworkGlobals.currentClass = new ClassData();
        NetworkGlobals.currentClass.classId = 10;
        Quiz q = new Quiz("Ego");
        q.setTimeOpen(NetworkGlobals.getTimeNow());
        q.setTimeClose(NetworkGlobals.getTimeNow());
        NetworkGlobals.createSession("jtulin@gmail.com", "jtulin");
      //  NetworkGlobals.session().postQuiz(q);
        Quiz r = NetworkGlobals.session().getQuiz(377);
        System.out.println("ID is " + r.quizID);
        r.setQuizName("HELLO FUTURE");
        try {
            NetworkGlobals.session().updateQuiz(r);
        } catch (Exception e){
            System.err.println(e);
        }



    }
}