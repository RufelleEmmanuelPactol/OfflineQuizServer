package onBoard.dataClasses;

import java.io.Serializable;

public class ClassUser implements Serializable {
    // This is a bundled instance of a class and a user

    public ClassData classInstance;
    public User  userInstance;

    public ClassUser(ClassData classInstance, User userInstance) {
        this.classInstance = classInstance;
        this.userInstance = userInstance;
    }
}
