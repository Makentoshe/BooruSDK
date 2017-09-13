package test.source.boor.E621;

import engine.BooruEngineException;
import module.LoginModule;
import org.junit.Test;
import source.boor.E621;
import source.boor.Gelbooru;
import test.source.TestHelper;

import javax.naming.AuthenticationException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class E621LoginModuleTest extends TestHelper{

    private final LoginModule boor;

    public E621LoginModuleTest(){
        boor = E621.get();
    }

    @Test
    public void logInAuthenticateFail_Test() throws Exception {
        try {
            boor.logIn("User", "Pass");
        }catch (BooruEngineException e){
            assertEquals(AuthenticationException.class, e.getCause().getClass());
        }
    }

    @Test
    public void logInSuccess_Test() throws Exception {
        boor.logIn(getLogin(), getPass());
        assertTrue(!((HashMap<String, String>)boor.getLoginData()).isEmpty());
        boor.logOff();
    }

    @Test
    public void getAuthenticateRequest_Test() throws Exception {
        assertEquals("https://e621.net/user/authenticate", boor.getAuthenticateRequest());
    }

    @Test
    public void logOff_Test() throws Exception{
        boor.logOff();
        assertEquals(0, ((HashMap<String, String>)boor.getLoginData()).size());
    }

    @Test
    public void getLoginDataCookie_Test() throws Exception {
        boor.logOff();
        ((HashMap<String, String>)boor.getLoginData()).put("Sas", "123");
        ((HashMap<String, String>)boor.getLoginData()).put("Asa", "321");
        assertEquals("Sas=123; Asa=321", boor.getCookieFromLoginData());
        boor.logOff();
    }
}
