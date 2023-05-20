package onBoard.dataClasses;

import onBoard.quizUtilities.Quiz;

import java.io.Serializable;

public class ClassData implements Serializable {
    public int classId;
    public String className;
    public String joinCode;
    public int proctorID;
    public String proctorName;
    public Quiz quiz;


    public ClassData(int classId, String className, String joinCode, int proctorID, String courseName) {
        this.classId = classId;
        this.className = className;
        this.joinCode = joinCode;
        this.proctorID = proctorID;

    }


    public ClassData(){}

    @Override
    public String toString() {
        return "Class{" +
                "classId=" + classId +
                ", className='" + className + '\'' +
                ", joinCode='" + joinCode + '\'' +
                ", proctorID=" + proctorID +
                '}';
    }
}
