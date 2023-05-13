package onBoard.dataClasses;

import onBoard.connectivity.Serialize;

import java.io.Serializable;

public class User implements Serializable {
    public int userId;
    public String firstname;
    public String lastname;
    public String email;
    public String organization;
    public int userType;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", organization='" + organization + '\'' +
                ", userType=" + userType +
                '}';
    }

    public User(int userId, String firstname, String lastname, String email, String organization, int userType) {
        this.userId = userId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.organization = organization;
        this.userType = userType;
    }
}
