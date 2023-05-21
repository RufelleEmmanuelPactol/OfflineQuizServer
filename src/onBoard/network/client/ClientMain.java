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
                    NetworkGlobals.currentClass = new ClassData();
                    NetworkGlobals.currentClass.classId = 1;
                    var x = NetworkGlobals.session().getProctorClasses(1);
                    x.forEach(i -> {
                        System.out.println(i);
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


//                try {
//                   Quiz quiz = new Quiz("Test ONlt");
//                   quiz.addIdentificationQuestion("Kinsay gapatay ni Rizal?", "Lapu-Lapu").setCaseInsensitive(true).setWhitespaceInsensitive(true).setMarks(10);
//                   quiz.addMultipleChoiceQuestion("Kinsa siya?", 2, Choice.A).addChoice("True").addChoice("False").setMarks(10);
//                   quiz.addMultipleAnswerQuestion("Ngano?", 2, new Choice[]{Choice.B}).addChoice("True").addChoice("False").setMarks(10);
//                   var modified =NetworkGlobals.getTimeNow();
//                   modified.setYear(2025);
//                   quiz.setTimeOpen(NetworkGlobals.getTimeNow());
//                   quiz.setTimeClose(modified);
//                   NetworkGlobals.currentClass = new ClassData();
//                   NetworkGlobals.currentClass.classId = 0;
//                   NetworkGlobals.session().postQuiz(quiz);
//                     quiz = NetworkGlobals.session().getQuiz(258);
//                   quiz.getQuestionNumber(1).checkAnswer("lapu - lapu");
//                   quiz.getQuestionNumber(2).checkAnswer(Choice.A);
//                   quiz.getQuestionNumber(3).checkAnswer(Choice.B);
//                   quiz.log();
//                   NetworkGlobals.session().postAttempt(quiz, NetworkGlobals.getTimeNow());
//                   var attempt = NetworkGlobals.session().getAttempt(quiz.quizID );
//                    System.out.println(attempt.quizBlob.getQuizName());
//                   attempt.quizBlob.log();
//                    System.out.println("Your total score for this attempt is: " + attempt.quizBlob.getMarks() + " out of " + attempt.quizBlob.getTotalMarks());
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                } catch (ClassNotFoundException e) {
//                    throw new RuntimeException(e);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
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
