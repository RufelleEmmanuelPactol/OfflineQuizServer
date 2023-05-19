package onBoard.network.client;

import onBoard.dataClasses.ClassData;
import onBoard.network.networkUtils.NetworkGlobals;
import onBoard.network.networkUtils.NetworkUtils;
import onBoard.quizUtilities.Quiz;

import java.io.IOException;

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
                try {
                    NetworkGlobals.session().postQuiz(q);
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
        for (int i=0; i<20; i++){
            Thread thread = new Thread(a);
            Thread.sleep(2000);
            thread.start();
        }



    }


}
