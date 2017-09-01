package test.source.boor.Safebooru;

import engine.BooruEngineException;
import module.interfacе.LoginModuleInterface;
import org.junit.Test;
import source.boor.Safebooru;
import test.source.TestHelper;

import javax.naming.AuthenticationException;
import java.util.HashMap;

import static org.junit.Assert.*;

public class SafebooruLoginModuleTest {

    private final LoginModuleInterface boor;

    public SafebooruLoginModuleTest(){
        boor = Safebooru.get();
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
        boor.logIn(TestHelper.getLogin(), TestHelper.getPass());
        assertEquals(2, ((HashMap<String, String>)boor.getLoginData()).size());
    }

    @Test
    public void getAuthenticateRequest_Test() throws Exception {
        assertEquals("https://safebooru.org/index.php?page=account&s=login&code=00", boor.getAuthenticateRequest());
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
    }

}
