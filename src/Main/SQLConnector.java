package Main;

import Network.AuthToken;
import Network.Exceptions.InvalidAuthException;
import Network.PortHandler;
import Network.RequestToken;
import QuizQuestions.*;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static java.lang.System.exit;

public class SQLConnector {
    Connection connection;
    Statement statement;

    public SQLConnector() {
        try {
   //         connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306", "root", "");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "");
            statement = connection.createStatement();
            statement.executeUpdate("USE dbonboard");
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }
    public void postQuiz (Quiz quiz) throws IOException, SQLException {
        var quizByteStream = Serialize.writeToBytes(quiz);
        var prepared = connection.prepareStatement("INSERT INTO quiz(quiz_id, quiz_blob) values (null, ?)");
        prepared.setBytes(1, quizByteStream);
        prepared.executeUpdate();
    }

    public Quiz getQuiz (int id) throws SQLException, IOException, ClassNotFoundException {
        var resultSet = statement.executeQuery("SELECT quiz_blob FROM quiz WHERE quiz_id = " + id);
        resultSet.next();
        return (Quiz) Serialize.constructFromBlob(resultSet.getBinaryStream("quiz_blob"));
    }

    /*
    Modifies the request token. The token's response will turn into a new room
    if the user exists.
     */
    public void verifyUser (RequestToken token) {
        try {
            var authentication = (AuthToken) token.authentication;
            var preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE email = ? AND password = ? ");
            preparedStatement.setString(1, authentication.username);
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

}
