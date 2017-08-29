package module.interfacе;

import engine.BooruEngineException;

/**
 * Interface with the basic methods for storing and creating a user data, with which a *boor will work.
 * <p>
 * Give access for login and other actions, which requires user data.
 */
public interface LoginModuleInterface {
//TODO: use URLEncoder.encode(data, String.valueOf(Charset.defaultCharset()))
    /**
     * Login a user.
     *
     * @param login user login
     * @param password user pass
     * @throws BooruEngineException - when something go wrong.
     * Use <tt>getCause</tt> to see details.
     */
    void logIn(final String login, final String password) throws BooruEngineException;

    /**
     * Logoff a user. Remove all user data.
     */
    void logOff();

    /**
     * Get all user data.
     *
     * @return any object, which storage login data.
     */
    Object getLoginData();


    String getCookieFromLoginData();
}
