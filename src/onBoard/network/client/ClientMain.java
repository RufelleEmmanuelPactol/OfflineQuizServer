package onBoard.network.client;

import onBoard.connectivity.SQLConnector;
import onBoard.dataClasses.ClassData;
import onBoard.network.networkUtils.NetworkGlobals;
import onBoard.network.networkUtils.NetworkUtils;
import onBoard.quizUtilities.MultipleChoice.Choice;
import onBoard.quizUtilities.MultipleChoice.MultipleAnswerQuestion;
import onBoard.quizUtilities.MultipleChoice.MultipleChoiceQuestion;
import onBoard.quizUtilities.Quiz;

import java.io.IOException;
import java.sql.SQLException;

public class ClientMain {
    public static void main(String[] args) throws Throwable {
        var a = new Runnable() {
            @Override
            public void run() {
                try {
                    NetworkGlobals.createSession("jtulin@gmail.com", "jtulin");
                    System.out.println(NetworkGlobals.getCurrentUser().userId);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

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


                try {
                   // NetworkGlobals.session().postQuiz(q);
                    SQLConnector n = new SQLConnector();
                    var quiz = n.getQuiz(237);
                    System.out.println(quiz.quizID);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }  catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    NetworkGlobals.endSession();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        for (int i=0; i<1; i++){
            Thread thread = new Thread(a);
            Thread.sleep(2000);
            thread.start();
        }



    }


}
