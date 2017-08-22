package module;

/**
 * Interface with the basic methods for storing and creating a user data, with which a *boor will work.
 */
public interface LoginModule {

    void logIn(final String login, final String password) throws Exception;

    void logOff();

    Object getLoginData();
}
