package onBoard.connectivity;

import onBoard.dataClasses.ClassData;
import onBoard.dataClasses.Requests;
import onBoard.dataClasses.Result;
import onBoard.dataClasses.User;
import onBoard.network.exceptions.CannotReattemptQuizAgain;
import onBoard.network.networkUtils.*;
import onBoard.network.exceptions.InvalidAuthException;
import onBoard.network.utils.DateBuilder;
import onBoard.quizUtilities.Quiz;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.IOException;

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


    public void postQuiz (Quiz quiz, ClassData instance)  {
        try {
            var quizByteStream = Serialize.writeToBytes(quiz);
            var prepared = connection.prepareStatement("INSERT INTO quiz(quiz_id, quiz_blob, quiz_name, class_id, quiz_open, quiz_close) values (null, ?, ?, ?, ?, ?)");
            prepared.setBytes(1, quizByteStream);
            prepared.setString(2, quiz.getQuizName());
            prepared.setInt(3, instance.classId);
            prepared.setString(4, quiz.getTimeOpen().toString());
            prepared.setString(5, quiz.getTimeClose().toString());
            prepared.executeUpdate();
            int id = getID("quiz", "quiz_id");
            prepared = connection.prepareStatement("SELECT quiz_blob from quiz where quiz_id = " + id);
            var result = prepared.executeQuery();
            result.next();
            Quiz quizWithID = Serialize.constructFromBlob(result.getBinaryStream("quiz_blob"));
            quizWithID.quizID = id;
            prepared = connection.prepareStatement("UPDATE quiz set quiz_blob = ? where quiz_id = " + id);
            prepared.setBytes(1, Serialize.writeToBytes(quizWithID));
            prepared.executeUpdate();
        }catch (SQLException | ClassNotFoundException | IOException e){
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public int getID(String table, String ID) throws SQLException {
        var prepared = connection.prepareStatement("SELECT MAX(" + ID + ") as count from " + table);
        var rs = prepared.executeQuery();
        rs.next();
        return rs.getInt("count");
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

    public void postAttempt (Result result) throws SQLException, IOException {
        var state = connection.prepareStatement("SELECT COUNT(*) as COUNT from result where quiz_id = " + result.quizID + " and student_id = " + result.studentID);
        var resulting = state.executeQuery();
        resulting.next();
        if (resulting.getInt(1) != 0) {
            throw new CannotReattemptQuizAgain();
        }
        PreparedStatement statement = connection.prepareStatement("INSERT INTO result values (null, ?, ?, ?, ?, ?)");
        statement.setInt(1, result.studentID);
        statement.setInt(2, result.quizID);
        statement.setString(3, result.startTime.toString());
        statement.setString(4, result.endTime.toString());
        statement.setBytes(5, Serialize.writeToBytes(result.quizBlob));
        statement.executeUpdate();
    }

    public Result getAttempt (int quizID, int userID) throws SQLException, IOException, ClassNotFoundException {
        PreparedStatement statement = connection.prepareStatement("SELECT * from result where student_id = ? and quiz_id = ?");
        statement.setInt(1, userID);
        statement.setInt(2, quizID);
        var result = statement.executeQuery();
        if (result.next()) {
            Result res = new Result();
            res.studentID = userID;
            res.quizID = quizID;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(result.getDate("submitted_time"));
            res.endTime = new DateBuilder().setYear(calendar.get(Calendar.YEAR))
                    .setMonth(calendar.get(Calendar.MONTH)).setDay(calendar.get(Calendar.DATE))
                    .setHour(calendar.get(Calendar.HOUR)).setMinute(calendar.get(Calendar.MINUTE));
            calendar.setTime(result.getDate("start_time"));
            res.startTime = new DateBuilder().setYear(calendar.get(Calendar.YEAR))
                    .setMonth(calendar.get(Calendar.MONTH)).setDay(calendar.get(Calendar.DATE))
                    .setHour(calendar.get(Calendar.HOUR)).setMinute(calendar.get(Calendar.MINUTE));
            res.quizBlob = Serialize.constructFromBlob(result.getBinaryStream("quiz_blob"));
            return res;
        }
        return new Result();


    }

    public ArrayList<ClassData> getUserClasses (int userID) throws Exception{
        var statement = connection.prepareStatement("SELECT class.class_name, class.class_id, class.proctor_id, user.firstname, user.lastname from class_user, class, user where class_user.user_id = ? and class_user.class_id = class.class_id and user.user_id = class.proctor_id;");
        statement.setInt(1, userID);
        var result = statement.executeQuery();
        ArrayList<ClassData> data =  new ArrayList<>();
        while (result.next()){
            var entity = new ClassData();
            entity.classId = result.getInt("class_id");
            entity.className = result.getString("class_name");
            entity.proctorID = result.getInt("proctor_id");
            entity.proctorName = result.getString("firstname") + " " + result.getString("lastname");
            data.add(entity);
        } return data;

    }

    public ArrayList<ClassData> getProctorClasses (int userID) throws Exception{
        var statement = connection.prepareStatement("SELECT * from class where proctor_id = ?");
        statement.setInt(1, userID);
        var result = statement.executeQuery();
        ArrayList<ClassData> data =  new ArrayList<>();
        while (result.next()){
            var entity = new ClassData();
            entity.classId = result.getInt("class_id");
            entity.className = result.getString("class_name");
            entity.proctorID = result.getInt("proctor_id");
            entity.joinCode = result.getString("join_code");
            data.add(entity);
        } return data;


    }

    public ArrayList<ClassData> getOngoingQuizzes(int userID){
        try {
            var statement = connection.prepareStatement("SELECT class_id from class_user where user_id = ?");
            statement.setInt(1, userID);
            var result = statement.executeQuery();
            ArrayList<Integer> class_list = new ArrayList<>();
            while (result.next()){
                class_list.add(result.getInt("class_id"));
            } if (class_list.size() == 0) return null;
            var array = new String[class_list.size()];
            for (int i=0; i<class_list.size(); i++){
                array[i] = Integer.toString(class_list.get(i));
            }
            String arrayAsString = String.join(", ", array);
            statement = connection.prepareStatement("SELECT quiz_blob, class.class_id, join_code, user_id, firstname, lastname, class_name  from class, quiz, user where class.class_id in (" + arrayAsString+ ") and user.user_id = class.proctor_id and class.class_id = quiz.class_id and quiz.quiz_close > NOW()");
            ArrayList<ClassData> data = new ArrayList<>();
            result = statement.executeQuery();

            while (result.next()){
                ClassData entry = new ClassData();
                entry.classId = result.getInt("class_id");
                entry.className = result.getString("class_name");
                entry.joinCode = result.getString("join_code");
                entry.proctorID = result.getInt("user_id");
                entry.proctorName = result.getString("firstname");
                entry.proctorName.concat(" " + result.getString("lastname"));
                entry.quiz = Serialize.constructFromBlob(result.getBytes("quiz_blob"));
                data.add(entry);
            } return data;

        } catch (SQLException e){
            System.err.println(e);
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } return null;
    }

    public boolean isValidClassCode (String classCode) throws SQLException {
        var prep = connection.prepareStatement("SELECT count(*) from class where join_code = ?");
        prep.setString(1, classCode);
        var res = prep.executeQuery();
        res.next();
        return res.getInt(1) == 0 ? false : true;
    }

    public void sendRequest (int studentID, String code) throws SQLException {
        var prep = connection.prepareStatement("SELECT class_id from class where join_code = ?");
        prep.setString(1, code);
        var res = prep.executeQuery();
        res.next();
        int classID = res.getInt(1);
         prep = connection.prepareStatement("SELECT COUNT(*) from requests where user_id = ? and class_id = ?");
        prep.setInt(1, studentID);
        prep.setInt(2, classID);
         res = prep.executeQuery();
        res.next();
        if (res.getInt(1) != 0) return;
        prep = connection.prepareStatement("INSERT INTO requests values(?, ?, 0)");
        prep.setInt(2, studentID);
        prep.setInt(1, classID);
        prep.executeUpdate();
    }


    public ArrayList<Requests> getAllRequests(int proctorID) throws SQLException {
        var statement = connection.prepareStatement("SELECT class_id from class where proctor_id = ?");
        statement.setInt(1, proctorID);
        var result = statement.executeQuery();
        ArrayList<Integer> class_list = new ArrayList<>();
        while (result.next()){
            class_list.add(result.getInt("class_id"));
        }
        var array = new String[class_list.size()];
        for (int i=0; i<class_list.size(); i++){
            array[i] = Integer.toString(class_list.get(i));
        }
        String arrayAsString = String.join(", ", array);
        var prep = connection.prepareStatement("SELECT firstname, lastname, requests.user_id, requests.class_id, class_name from class, user, requests where requests.class_id = class.class_id and requests.user_id = user.user_id and requests.class_id in (" + arrayAsString + ") and isApproved = 0");
        var res = prep.executeQuery();
        ArrayList<Requests> ret = new ArrayList<>();
        while (res.next()){
            Requests r = new Requests(res.getString("firstname"), res.getString("lastname"), res.getInt("user_id"),  res.getString("class_name"), res.getInt("class_id"));
            ret.add(r);
        } return ret;
    }

    public void ApproveRequests (int studentID, int classID) throws SQLException {
        var prep = connection.prepareStatement("UPDATE requests set isApproved = 1 where user_id = " + studentID + " and class_id = " + classID);
        prep.executeUpdate();
        prep = connection.prepareStatement("INSERT INTO class_user values (null, ?, ?)");
        prep.setInt(1, classID);
        prep.setInt(2, studentID);
        prep.executeUpdate();
    }

    public ArrayList<Quiz> getQuizzesPerClass (int classID) throws Exception {
        var prep = connection.prepareStatement("SELECT quiz_blob, quiz_id FROM quiz WHERE class_id = ?");
        prep.setInt(1, classID);
        var rsSet = prep.executeQuery();
        ArrayList<Quiz> q = new ArrayList<>();
        while (rsSet.next()){
            var qz = (Quiz) Serialize.constructFromBlob(rsSet.getBytes("quiz_blob"));
            qz.quizID = rsSet.getInt("quiz_id");
            q.add(qz);
        } return q;
    }

    public void updateQuiz (Quiz q) throws Exception {
        var prep = connection.prepareStatement("UPDATE quiz set quiz_blob = ?, quiz_name = ?, quiz_open = ?, quiz_close = ?, class_id = ? where quiz_id = ?");
        prep.setBytes(1, Serialize.writeToBytes(q));
        prep.setString(2, q.getQuizName());
        prep.setDate(3, q.getTimeOpen().toSqlDate());
        prep.setDate(4, q.getTimeClose().toSqlDate());
        prep.setInt(5, q.getClassID());
        prep.setInt(6, q.quizID);
        System.out.println(prep);
        prep.executeUpdate();
    }







}
