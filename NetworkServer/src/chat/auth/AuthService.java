package chat.auth;

public interface AuthService {

    void start();

    static String getUsernameByLoginAndPassword(String login, String password){
        return null;
    }

    void stop();

}
