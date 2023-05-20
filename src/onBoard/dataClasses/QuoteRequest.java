package onBoard.dataClasses;

import java.io.Serializable;

public class QuoteRequest implements Serializable {
    public String firstName;
    public String lastName;
    public String organizationName;
    public String email;

    public QuoteRequest(String firstName, String lastName, String organizationName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.organizationName = organizationName;
        this.email = email;
    }

    public QuoteRequest(){}
}
