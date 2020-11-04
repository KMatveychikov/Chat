package chat.auth;



import java.sql.*;


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


    @Override
    public void start() {
        System.out.println("Auth service has been started");
        connect();
    }


    @Override
    public void stop() {
        System.out.println("Auth service has been finished");
    }


}
