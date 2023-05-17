package onBoard.network.client;

import onBoard.dataClasses.ClassData;
import onBoard.network.networkUtils.NetworkGlobals;
import onBoard.network.networkUtils.NetworkUtils;
import onBoard.quizUtilities.Quiz;

public class ClientMain {
    public static void main(String[] args) throws Throwable {
       NetworkGlobals.createSession("jtulin@gmail.com", "jtulin");
       var response = NetworkGlobals.session().getUserInfo();
       NetworkGlobals.currentClass = new ClassData();
       NetworkGlobals.currentClass.classId = 0;
        NetworkGlobals.session().getUserInfo();
        Quiz q = new Quiz("Alter");
        q.setTimeOpen().setYear(2000).setMonth(1).setDay(2);
        q.setTimeClose().setYear(2000).setMonth(1).setDay(2);
        NetworkGlobals.session().postQuiz(q);

    }

}
