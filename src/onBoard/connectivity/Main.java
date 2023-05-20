package onBoard.connectivity;

import onBoard.dataClasses.ClassData;
import onBoard.network.networkUtils.NetworkGlobals;
import onBoard.network.networkUtils.PortHandler;
import onBoard.quizUtilities.MultipleChoice.MultipleAnswerQuestion;
import onBoard.quizUtilities.Quiz;
import onBoard.quizUtilities.MultipleChoice.Choice;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException {
        Quiz q = new Quiz("Ego");
        var question = (MultipleAnswerQuestion)q.addMultipleAnswerQuestion("What is your name?", 3, new Choice[]{Choice.A, Choice.B}).addChoice("Rufelle").addChoice("Marion").addChoice("Tulin").setMarks(10);
        question.checkAnswer(Choice.B);
        System.out.println("Awarded Marks: " + question.getAwardedMarks());
        q.setTimeOpen().setYear(2000).setMonth(1).setDay(2);
        q.setTimeClose().setYear(2000).setMonth(1).setDay(2);

    }
}