package onBoard.dataClasses;

import java.io.Serializable;

public class Requests implements Serializable {
    public String studentFirstname;
    public String studentLastName;
    public int studentID;
    public String className;
    public int classID;

    public boolean isApproved = false;

    public Requests(String studentFirstname, String studentLastName, int studentID, String className, int classID) {
        this.studentFirstname = studentFirstname;
        this.studentLastName = studentLastName;
        this.studentID = studentID;
        this.className = className;
        this.classID = classID;
    }

    public int getStatus(){
        return isApproved ? 1 : 0;
    }

    public Requests() {
    }

    @Override
    public String toString() {
        return "Requests{" +
                "studentFirstname='" + studentFirstname + '\'' +
                ", studentLastName='" + studentLastName + '\'' +
                ", studentID=" + studentID +
                ", className='" + className + '\'' +
                ", classID=" + classID +
                ", isApproved=" + isApproved +
                '}';
    }
}
