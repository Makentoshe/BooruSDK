package test.source.boor.Rule34;

import engine.BooruEngineException;
import module.interfacе.LoginModuleInterface;
import org.junit.Test;
import source.boor.Rule34;
import source.boor.Safebooru;
import test.source.TestHelper;

import javax.naming.AuthenticationException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class Rule34LoginModuleTest {

    private final LoginModuleInterface boor;

    public Rule34LoginModuleTest(){
        boor = Rule34.get();
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
        assertEquals("https://rule34.xxx/index.php?page=account&s=login&code=00", boor.getAuthenticateRequest());
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
