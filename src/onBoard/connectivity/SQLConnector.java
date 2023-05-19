package onBoard.connectivity;

import onBoard.dataClasses.ClassData;
import onBoard.dataClasses.ClassUser;
import onBoard.dataClasses.User;
import onBoard.network.networkUtils.*;
import onBoard.network.exceptions.InvalidAuthException;
import onBoard.quizUtilities.Quiz;

import java.awt.*;
import java.util.Date;
import javax.print.attribute.standard.DateTimeAtCreation;
import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;

public class SQLConnector {
    private static String lucky_creds = "user_for_school";
    private static String user = "root";
    Connection connection;
    Statement statement;
    public static boolean checkConnection(){

        if (NetworkGlobals.luckyMode) {
            user = lucky_creds;
            System.out.println(user);
        }

        try {
            //         connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306", "root", "");
            var connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", user, "");
            var statement = connection.createStatement();
            statement.executeUpdate("USE dbonboard");
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            try {
                NetworkUtils.showNotif("Your SQL server is off.", "To make sure that the OnBoard server is working properly, please turn on your SQL server.");
                return false;
            } catch (AWTException r){
                System.err.println(r);
                return false;
            }
        }
    }

    public SQLConnector() {
        try {
   //         connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306", "root", "");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", user, "");
            statement = connection.createStatement();
            statement.executeUpdate("USE dbonboard");
        } catch (SQLException e) {
            System.out.println(e);
            try {
                NetworkUtils.showNotif("Your SQL server is off.", "To make sure that the OnBoard server is working properly, please turn on your SQL server.");
            } catch (AWTException r){}
        }
    }


    public void postQuiz (Quiz quiz, ClassData instance) throws IOException, SQLException {
        var quizByteStream = Serialize.writeToBytes(quiz);
        var prepared = connection.prepareStatement("INSERT INTO quiz(quiz_id, quiz_blob, quiz_name, class_id, quiz_open, quiz_close) values (null, ?, ?, ?, ?, ?)");
        prepared.setBytes(1, quizByteStream);
        prepared.setString(2, quiz.getQuizName());
        prepared.setInt(3, instance.classId);
        prepared.setString(4, quiz.getTimeOpen().toString());
        prepared.setString(5, quiz.getTimeClose().toString());
        prepared.executeUpdate();
    }

    public Quiz getQuiz (int id) throws SQLException, IOException, ClassNotFoundException {
        var resultSet = statement.executeQuery("SELECT quiz_blob FROM quiz WHERE quiz_id = " + id);
        resultSet.next();
        return Serialize.constructFromBlob(resultSet.getBinaryStream("quiz_blob"));
    }

    /*
    Modifies the request token. The token's response will turn into a new room
    if the user exists.
     */
    public void verifyUser (RequestToken token) {
        try {
            var authentication = (AuthToken) token.authentication;
            var preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE email = ? AND password = ? ");
            preparedStatement.setString(1, authentication.email);
            preparedStatement.setString(2, authentication.password);
            var set = preparedStatement.executeQuery();
            token.response = PortHandler.getNewRoom();

            if (!set.next()) {
                token.exception = new InvalidAuthException();
                return;
            }
            authentication.userType = set.getInt("is_proctor") == 1 ? "PROCTOR" : set.getInt("is_proctor") == 2 ? "ADMIN" : "STUDENT";
        } catch (SQLException e){
            System.out.println("Error at verifyUser: " + e);
        }
    }

    public User getUserData (RequestToken token) throws SQLException {
        var authentication = (AuthToken) token.authentication;
        var preparedStatement = connection.prepareStatement("SELECT * FROM user, organization WHERE email = ? and password = ? and user.organization_id = organization.organization_id");
        preparedStatement.setString(1, authentication.email);
        preparedStatement.setString(2, authentication.password);
        var set = preparedStatement.executeQuery();
        if (!set.next()) {
            return null;
        }
        return new User(set.getInt("user_id"), set.getString("firstname")
        , set.getString("lastname"), set.getString("email"), set.getString("organization_name"), set.getInt("is_proctor"));
    }

}
