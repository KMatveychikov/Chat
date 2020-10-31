package chat.auth;

import chat.User;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseAuthService implements AuthService  {
    private static Connection connection;
    private static Statement statement;

    synchronized static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:NetworkServer/users.s3db");
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    synchronized static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized String getUsernameByLoginAndPassword(String login, String password) {
        //System.out.println(login + password);
        connect();
        String query = String.format("select nickname from users where login='%s' and password='%s'", login, password);
        try (ResultSet set = statement.executeQuery(query)) {
            if (set.next())
                return set.getString("nickname");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            disconnect();
        }
        return null;


    }
//    private static final Map<String, String> USERS = new HashMap<>() {{
//        put("login1", "pass1");
//        put("login2", "pass2");
//        put("login3", "pass3");
//    }};

//    private static final List<User> USERS = List.of(
//            new User("login1", "pass1", "Oleg"),
//            new User("login2", "pass2", "Alexey"),
//            new User("login3", "pass3", "Peter")
//    );

    @Override
    public void start() {
        System.out.println("Auth service has been started");
        connect();
    }


    @Override
    public void stop() {
        System.out.println("Auth service has been finished");
    }

//    @Override
//    public String getUsernameByLoginAndPassword(String login, String password) {
//        for (User user : USERS) {
//            if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
//                return user.getUsername();
//            }
//        }
//
//        return null;
//    }
}
