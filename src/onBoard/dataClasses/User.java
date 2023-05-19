package onBoard.dataClasses;

import onBoard.connectivity.Serialize;

import java.io.Serializable;

public class User implements Serializable {
    public int userId;
    public String firstname;
    public String lastname;
    public String email;
    public int userType;
    public String organization;

    public User(int userId, String firstname, String lastname, String email, String organization, int userType) {
        this.userId = userId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.userType = userType;
        this.organization = organization;

    }
}
