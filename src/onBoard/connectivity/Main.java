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
        Quiz q = new Quiz("Ego");
        NetworkGlobals.createSession("jtulin@gmail.com", "jtulin");
        var req = NetworkGlobals.session().getAllRequests(NetworkGlobals.getCurrentUser().userId);
        var app = req.get(0);
        System.out.println(app);
        NetworkGlobals.session().approveRequest(app.studentID, app.classID);


//        NetworkGlobals.connectionOfficer();
//        var question = (MultipleAnswerQuestion)q.addMultipleAnswerQuestion("What is your name?", 3, new Choice[]{Choice.A, Choice.B}).addChoice("Rufelle").addChoice("Marion").addChoice("Tulin").setMarks(10);
//        q.addIdentificationQuestion("What is your name", "secret").setMarks(20);
//        NetworkGlobals.currentClass = new ClassData();
//        NetworkGlobals.currentClass.classId = 1;
//        q.setTimeOpen().setYear(2000).setMonth(1).setDay(2);
//        q.setTimeClose().setYear(2000).setMonth(1).setDay(2);
//        NetworkGlobals.session().postQuiz(q);
//        NetworkGlobals.session().postQuiz(q);
//        NetworkGlobals.currentClass.classId = 3;
//        NetworkGlobals.session().postQuiz(q);

    }
}