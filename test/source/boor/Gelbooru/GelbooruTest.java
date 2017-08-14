package test.source.boor.Gelbooru;

import engine.HttpsConnection;
import org.junit.Test;
import source.boor.Gelbooru;
import source.еnum.Api;
import source.еnum.Format;
import test.TestData;

import static org.junit.Assert.*;

//default test for correct basic methods.
public class GelbooruTest {

    @Test
    public void getApi_Test() throws Exception {
        assertEquals(Api.BASICS, Gelbooru.get().getApi());
    }

    @Test
    public void getDataType_Test() throws Exception{
        assertEquals(Format.XML, Gelbooru.get().getFormat());
    }

    @Test
    public void getCustomRequest_Test() throws Exception{
        String request = Gelbooru.get().getCustomRequest("request");
        String expected = "https://gelbooru.com/request";
        assertEquals(expected, request);
    }

    @Test
    public void cookies_Test() throws Exception {
        Gelbooru.get().setCookies("pass_hash", 0);
        assertEquals("pass_hash=pass_hash; user_id=0", Gelbooru.get().getCookies());
    }

    @Test
    public void login_Test() throws Exception {
        Gelbooru.get().setCookies(TestData.GELBOORU_PASS_HASH, TestData.GELBOORU_USER_ID);
        String response = new HttpsConnection()
                .setRequestMethod("GET")
                .setUserAgent("BooruEngineLib(mkliverout@gmail.com)/1.0")
                .setCookies(Gelbooru.get().getCookies())
                .openConnection("https://gelbooru.com/index.php?page=account&s=home")
                .getResponse();
        //because this string can be seen only if user is successfully log in.
        assertTrue(response.contains("<a href=\"index.php?page=account&amp;s=change_password\" style=\"font-size: 12px; font-weight: bold;\">Change your password</a><br />"));
    }

}