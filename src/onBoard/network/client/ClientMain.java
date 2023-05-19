package onBoard.network.client;

import onBoard.dataClasses.ClassData;
import onBoard.network.networkUtils.NetworkGlobals;
import onBoard.network.networkUtils.NetworkUtils;
import onBoard.quizUtilities.Quiz;

public class ClientMain {
    public static void main(String[] args) throws Throwable {
       NetworkGlobals.createSession("jtulin@gmail.com", "jtulin");

       /*
       This block is for debugging purposes only.
       The proper implementation will be conducted
       only after the implementation of the quiz area
       is finished.
        */
       NetworkGlobals.currentClass = new ClassData();
       NetworkGlobals.currentClass.classId = 0;



       /*
       Quiz setup: ensure that the setTimeClose and setTimeOpen
       is properly invoked to avoid server-side exceptions.
        */
       Quiz q = new Quiz("Alter");
       q.setTimeOpen().setYear(2000).setMonth(1).setDay(2);
       q.setTimeClose().setYear(2000).setMonth(1).setDay(2);

       // The proper postquiz method
       NetworkGlobals.session().postQuiz(q);
        NetworkGlobals.endSession();


    }

}
