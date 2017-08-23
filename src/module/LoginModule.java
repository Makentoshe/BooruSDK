package module;

import engine.BooruEngineException;

/**
 * Interface with the basic methods for storing and creating a user data, with which a *boor will work.
 */
public interface LoginModule {

    /**
     * Login a user.
     *
     * @param login user login
     * @param password user pass
     * @throws BooruEngineException - when something go wrong.
     */
    void logIn(final String login, final String password) throws BooruEngineException;

    /**
     * Logoff a user
     */
    void logOff();

    /**
     * @return any object, which storage login data.
     */
    Object getLoginData();

    /**
     * Create request for authentication.
     *
     * @return url.
     */
    String getAuthenticateRequest();
}
