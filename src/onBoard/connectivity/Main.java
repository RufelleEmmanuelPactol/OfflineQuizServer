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
    public static void main(String[] args) throws Throwable {
       NetworkGlobals.createSession("test", "test");
       NetworkGlobals.currentClass = new ClassData();
       NetworkGlobals.currentClass.classId = 3;
       var x = NetworkGlobals.session().getAttempt(384);
        System.out.println(x.quizBlob.getMarks());


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




       // addQuizzes();





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


    public static void addQuizzes() throws IOException, ClassNotFoundException, InterruptedException {
     SQLConnector conn = new SQLConnector();
//        Quiz quiz = conn.getQuiz(1);
//        quiz.log();

        var later = NetworkGlobals.getTimeNow();
        later.setYear(2025);
     Quiz quiz1 = new Quiz("OOP2 Review");
     quiz1.setTimeOpen(NetworkGlobals.getTimeNow());
     quiz1.setTimeClose(later);
     quiz1.setClassID(1);
     quiz1.setTeacherID(1);
     quiz1.addMultipleAnswerQuestion("What does JDBC stand for in the context of database-driven java applications?",
                     4, new Choice[]{Choice.B})
             .addChoice("Java Database Connection")
             .addChoice("Java Database Connectivity")
             .addChoice("Java Database Consortium")
             .addChoice("Java Database Convention")
             .setMarks(10);
     quiz1.addMultipleAnswerQuestion("What is the data type equivalent of String in a database?",
                     4, new Choice[]{Choice.C})
             .addChoice("string")
             .addChoice("int")
             .addChoice("varchar")
             .addChoice("blob")
             .setMarks(10);
     quiz1.addMultipleAnswerQuestion("What does SQL stand for?",
                     4, new Choice[]{Choice.B})
             .addChoice("Standard Query Language")
             .addChoice("Structured Query Language")
             .addChoice("Standardized Query Language")
             .addChoice("Sequential Query Language")
             .setMarks(10);
     quiz1.addMultipleAnswerQuestion("Getter and setter methods are also known respectively as...",
                     4, new Choice[]{Choice.A})
             .addChoice("accessors and mutators")
             .addChoice("accessors and modifiers ")
             .addChoice("modifiers and mutators")
             .addChoice("mutators and modifiers")
             .setMarks(15);
     quiz1.addMultipleAnswerQuestion("What method of Statement is used if you want to execute DELETE SQL statement?",
                     4, new Choice[]{Choice.C})
             .addChoice("executeDelete()")
             .addChoice("executeQuery()")
             .addChoice("executeUpdate()")
             .addChoice("executeSelect()")
             .setMarks(10);
     quiz1.addMultipleAnswerQuestion("What method of Statement is used if you want to execute SELECT SQL statement?",
                     4, new Choice[]{Choice.B})
             .addChoice("executeDelete()")
             .addChoice("executeQuery()")
             .addChoice("executeUpdate()")
             .addChoice("executeSelect()")
             .setMarks(10);
     quiz1.addMultipleAnswerQuestion("Which of the following is an incorrect way of converting Double to String?",
                     4, new Choice[]{Choice.C})
             .addChoice("Double.toString(num);")
             .addChoice("String.valueOf(num);")
             .addChoice("Double.valueOf(num);")
             .addChoice("String.format(\"%f\", num);")
             .setMarks(15);
     quiz1.addIdentificationQuestion("Static polymorphism is achieved using method overloading and dynamic polymorphism using method _____.", "overriding")
             .setMarks(20);

     System.out.println(SQLConnector.checkConnection());
     conn.postQuiz(quiz1, new ClassData());

     Quiz quiz2 = new Quiz("Graphic Design");
     quiz2.setTimeOpen(NetworkGlobals.getTimeNow());

     quiz2.setTimeClose(later);
     quiz2.setClassID(1);
     quiz2.setTeacherID(1);
     quiz2.addMultipleAnswerQuestion("How long should a heading or title be in terms of the number of words?",
                     4, new Choice[]{Choice.C})
             .addChoice("1-3 words")
             .addChoice("4-5 words")
             .addChoice("5-6 words")
             .addChoice("6-7 words")
             .setMarks(10);
     quiz2.addMultipleAnswerQuestion("Which design principle creates movement and directs the reader’s eyes?",
                     4, new Choice[]{Choice.A})
             .addChoice("Rhythm")
             .addChoice("Contrast")
             .addChoice("Harmony")
             .addChoice("Simplicity")
             .setMarks(10);
     quiz2.addMultipleAnswerQuestion("An American designer who coined the term \"graphic design\" in the early 20th century. He is known for his work in typography, book design, and advertising.",
                     4, new Choice[]{Choice.B})
             .addChoice("Saul Bass")
             .addChoice("William Addison Dwiggins")
             .addChoice("Paul Rand")
             .addChoice("Jan Tschichold")
             .setMarks(10);
     quiz2.addMultipleAnswerQuestion("Which design principle refers to the distribution of visual weight of elements?",
                     4, new Choice[]{Choice.A})
             .addChoice("Balance")
             .addChoice("Rhythm ")
             .addChoice("Harmony")
             .addChoice("Emphasis")
             .setMarks(10);
     quiz2.addMultipleAnswerQuestion("What is the ideal number of typefaces to be used in a design layout?",
                     4, new Choice[]{Choice.B})
             .addChoice("1-2 typefaces")
             .addChoice("2-3 typefaces")
             .addChoice("4-5 typefaces")
             .addChoice("5-6 typefaces")
             .setMarks(10);
     quiz2.addIdentificationQuestion("This term is used to refer to a group of fonts that share similar design characteristics?",
                     "typeface")
             .setMarks(10);
     quiz2.addMultipleAnswerQuestion("What are the two placements that should be avoided in typography?",
                     4, new Choice[]{Choice.B})
             .addChoice("Widows and Holes")
             .addChoice("Orphans and Widows")
             .addChoice("Orphans and Gaps")
             .addChoice("Orphans and Spacing")
             .setMarks(10);
     quiz2.addIdentificationQuestion("What do you call a space between specific letters in a word?",
                     "kerning")
             .setMarks(10);
     quiz2.addMultipleAnswerQuestion("What do you call the art of arranging letters and text in a way that makes the copy legible, clear, and visually appealing to the reader?",
                     4, new Choice[]{Choice.C})
             .addChoice("Scriptography")
             .addChoice("Typesetting")
             .addChoice("Typography")
             .addChoice("Calligraphy")
             .setMarks(10);
     quiz2.addMultipleAnswerQuestion("Which design principle is used to give all graphic elements in a design more space and room to breathe?",
                     4, new Choice[]{Choice.C})
             .addChoice("Unity")
             .addChoice("Contrast")
             .addChoice("Space")
             .addChoice("Dominance")
             .setMarks(10);

     NetworkGlobals.session().postQuiz(quiz1);
     NetworkGlobals.session().postQuiz(quiz2);

    }


}