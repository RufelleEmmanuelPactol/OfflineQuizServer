package onBoard.dataClasses;

import onBoard.network.networkUtils.NetworkGlobals;
import onBoard.network.utils.DateBuilder;
import onBoard.quizUtilities.Quiz;

import java.io.Serializable;
import java.util.Date;

public class Result implements Serializable {
    public int resultID;
    public int studentID;
    public int quizID;
    public Quiz quizBlob;
    public DateBuilder startTime;
    public DateBuilder endTime;


    public Result (){}

    public Result(int resultID, int studentID, Quiz quizBlob, DateBuilder startTime, DateBuilder endTime) {
        this.resultID = resultID;
        this.studentID = studentID;
        this.quizBlob = quizBlob;
        this.startTime = startTime;
        this.endTime = endTime;
        quizID = quizBlob.quizID;
    }


}
