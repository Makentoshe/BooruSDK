package module;

/**
 * Interface with the basic methods for storing and creating a user data, with which a *boor will work.
 */
public interface LoginModule {

    /**
     * Setter for two values - identify and pass.
     *
     * @param identify a user identification - id, login, etc
     * @param pass     a user pass. As usually it's a pass hash, but can be something else.
     */
    void setUserData(final String identify, final String pass);

    /**
     * Constructor in special format. Each *boor need own format.
     *
     * @return string with the appended data.
     */
    String getUserData();

    /**
     * Getter.
     *
     * @return user identify.
     */
    String getIdentify();

    /**
     * Getter
     *
     * @return user pass
     */
    String getPass();
}
