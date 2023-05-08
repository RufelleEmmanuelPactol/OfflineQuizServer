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
        connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306", "root", "");
        statement = connection.createStatement();
        statement.executeUpdate("USE quiztest");
    }
    public void postQuiz (Quiz quiz) throws IOException, SQLException {
        var quizByteStream = Serialize.writeToBytes(quiz);
        var prepared = connection.prepareStatement("INSERT INTO tblquiz(quizID, quizBlob) values (null, ?)");
        prepared.setBytes(1, quizByteStream);
        prepared.executeUpdate();
    }

    public Quiz getQuiz (int id) throws SQLException, IOException, ClassNotFoundException {
        var resultSet = statement.executeQuery("SELECT quizBlob FROM tblQuiz WHERE quizID = " + id);
        resultSet.next();
        return (Quiz) Serialize.constructFromBlob(resultSet.getBinaryStream("quizBlob"));
    }

}
