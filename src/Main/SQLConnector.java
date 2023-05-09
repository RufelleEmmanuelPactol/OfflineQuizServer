package Main;

import QuizQuestions.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLConnector {
    Connection connection;
    Statement statement;

    public SQLConnector() throws SQLException, ClassNotFoundException {
//        connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306", "root", "");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "");
        statement = connection.createStatement();
        statement.executeUpdate("USE dbonboard");
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

}
