package test.source.boor.Konachan;


import engine.connector.HttpsConnection;
import engine.connector.Method;
import engine.parser.JsonParser;
import org.junit.Test;
import source.Comment;
import source.Post;
import source.boor.Konachan;
import test.source.TestHelper;

import static org.junit.Assert.assertEquals;

public class KonachanCommentConstructorTest {

    private static Comment comment;

    public KonachanCommentConstructorTest() throws Exception{
        if (comment != null)  return;
        Post post = new Post(TestHelper.getDataFromBoorAdvanced(Konachan.get(), 246937), Konachan.get());
        String responseData = new HttpsConnection()
                .setRequestMethod(Method.GET)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .openConnection(post.getComments_url())
                .getResponse();
        JsonParser parser = new JsonParser();
        parser.startParse(responseData);
        comment = new Comment(parser.getResult().get(0));
    }

    @Test
    public void getId_Test() throws Exception{
        assertEquals(169516, comment.getId());
    }

    @Test
    public void getCreatorId_Test() throws Exception{
        assertEquals(14977, comment.getCreator_id());
    }

    @Test
    public void getPostId_Test() throws Exception{
        assertEquals(246937, comment.getPost_id());
    }

    @Test
    public void getBody_Test() throws Exception{
        assertEquals("[quote]HaruhiToy said:\\r\\nSo just what do you want your enslaved lolis to work on?\\r\\n\\r\\n\\r\\n[/quote]\\r\\n\\r\\n(Insert lolicon doujin/hentai here)", comment.getBody());
    }

    @Test
    public void getCreatedAt_Test() throws Exception{
        assertEquals("2017-07-26T20:47:20.427Z", comment.getCreated_at());
    }

    @Test
    public void getCreatorName_Test() throws Exception{
        assertEquals("Agos", comment.getCreator_name());
    }
}
