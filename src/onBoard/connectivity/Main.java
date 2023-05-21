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
//        NetworkGlobals.currentClass = new ClassData();
//        NetworkGlobals.currentClass.classId = 10;
//        Quiz q = new Quiz("Ego");
//        q.setTimeOpen(NetworkGlobals.getTimeNow());
//        q.setTimeClose(NetworkGlobals.getTimeNow());
//        NetworkGlobals.createSession("jtulin@gmail.com", "jtulin");
//      //  NetworkGlobals.session().postQuiz(q);
//        Quiz r = NetworkGlobals.session().getQuiz(377);
//        System.out.println("ID is " + r.quizID);
//        r.setQuizName("HELLO FUTURE");
//        try {
//            NetworkGlobals.session().updateQuiz(r);
//        } catch (Exception e){
//            System.err.println(e);
//        }
//
//        package onBoard.connectivity;

//        Quiz q = new Quiz("Ego");
//        NetworkGlobals.createSession("jtulin@gmail.com", "jtulin");
//        var req = NetworkGlobals.session().getAllRequests(NetworkGlobals.getCurrentUser().userId);
//        var app = req.get(0);
//        System.out.println(app);
//        NetworkGlobals.session().approveRequest(app.studentID, app.classID);

        SQLConnector conn = new SQLConnector();
        Quiz quiz = conn.getQuiz(1);
        quiz.log();

//        Quiz quiz1 = new Quiz("OOP2 Review");
//        quiz1.setTimeOpen(NetworkGlobals.getTimeNow());
//        quiz1.setTimeClose(NetworkGlobals.getTimeNow());
//        quiz1.setClassID(1);
//        quiz1.setTeacherID(1);
//        quiz1.addMultipleAnswerQuestion("What does JDBC stand for in the context of database-driven java applications?", 4,
//                        new Choice[]{Choice.B})
//                .addChoice("Java Database Connection")
//                .addChoice("Java Database Connectivity")
//                .addChoice("Java Database Consortium")
//                .addChoice("Java Database Convention")
//                .setMarks(10);
//        quiz1.addMultipleAnswerQuestion("What is the data type equivalent of String in a databaseWhat does SQL stand for...??", 4,
//                        new Choice[]{Choice.C})
//                .addChoice("string")
//                .addChoice("int")
//                .addChoice("varchar")
//                .addChoice("blob")
//                .setMarks(10);
//        quiz1.addMultipleAnswerQuestion("What does SQL stand for?", 4,
//                        new Choice[]{Choice.B})
//                .addChoice("Standard Query Language")
//                .addChoice("Structured Query Language")
//                .addChoice("Standardized Query Language")
//                .addChoice("Sequential Query Language")
//                .setMarks(10);
//        quiz1.addMultipleAnswerQuestion("Getter and setter methods are also known respectively as...", 4,
//                        new Choice[]{Choice.A})
//                .addChoice("accessors and mutators")
//                .addChoice("accessors and modifiers ")
//                .addChoice("modifiers and mutators")
//                .addChoice("mutators and modifiers")
//                .setMarks(15);
//        quiz1.addMultipleAnswerQuestion("What method of Statement is used if you want to execute DELETE SQL statement?", 4,
//                        new Choice[]{Choice.C})
//                .addChoice("executeDelete()")
//                .addChoice("executeQuery()")
//                .addChoice("executeUpdate()")
//                .addChoice("executeSelect()")
//                .setMarks(10);
//        quiz1.addMultipleAnswerQuestion("What method of Statement is used if you want to execute SELECT SQL statement?", 4,
//                        new Choice[]{Choice.B})
//                .addChoice("executeDelete()")
//                .addChoice("executeQuery()")
//                .addChoice("executeUpdate()")
//                .addChoice("executeSelect()")
//                .setMarks(10);
//        quiz1.addMultipleAnswerQuestion("Which of the following is an incorrect way of converting Double to String?", 4,
//                        new Choice[]{Choice.C})
//                .addChoice("Double.toString(num);")
//                .addChoice("String.valueOf(num);")
//                .addChoice("Double.valueOf(num);")
//                .addChoice("String.format(\"%f\", num);")
//                .setMarks(15);
//        quiz1.addIdentificationQuestion("Static polymorphism is achieved using method overloading and dynamic polymorphism using method _____.", "overriding")
//                .setMarks(20);
//
//        System.out.println(SQLConnector.checkConnection());
//        conn.postQuiz(quiz1, new ClassData());


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