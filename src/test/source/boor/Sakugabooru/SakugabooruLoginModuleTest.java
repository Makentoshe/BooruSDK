package test.source.boor.Sakugabooru;

import engine.BooruEngineException;
import module.interfacе.LoginModuleInterface;
import org.junit.Test;
import source.boor.Sakugabooru;
import source.boor.Yandere;
import test.source.TestHelper;

import javax.naming.AuthenticationException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class SakugabooruLoginModuleTest extends TestHelper{

    private final LoginModuleInterface boor;

    public SakugabooruLoginModuleTest(){
        boor = Sakugabooru.get();
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
        assertFalse(((HashMap<String, String>)boor.getLoginData()).isEmpty());
    }

    @Test
    public void getAuthenticateRequest_Test() throws Exception {
        assertEquals("https://sakugabooru.com/user/authenticate", boor.getAuthenticateRequest());
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
