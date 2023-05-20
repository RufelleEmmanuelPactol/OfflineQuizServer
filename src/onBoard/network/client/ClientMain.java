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
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }




                try {
                   Quiz quiz = new Quiz("Junit");
                   quiz.addIdentificationQuestion("Kinsay gapatay ni Rizal?", "Lapu-Lapu").setCaseInsensitive(true).setWhitespaceInsensitive(true);
                   quiz.addMultipleChoiceQuestion("Kinsa siya?", 2, Choice.A).addChoice("True").addChoice("False");
                   quiz.addMultipleAnswerQuestion("Ngano?", 2, new Choice[]{Choice.B}).addChoice("True").addChoice("False");
                   quiz.setTimeClose(NetworkGlobals.getTimeNow());
                   quiz.setTimeOpen(NetworkGlobals.getTimeNow());
                   NetworkGlobals.currentClass = new ClassData();
                   NetworkGlobals.currentClass.classId = 0;
                   NetworkGlobals.session().postQuiz(quiz);
                   NetworkGlobals.session();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
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
