package test.source.boor.E621;

import engine.connector.HttpsConnection;
import engine.connector.Method;
import engine.parser.JsonParser;
import org.junit.Test;
import source.Comment;
import source.Post;
import source.boor.E621;
import test.source.PostTest;

import static org.junit.Assert.assertEquals;

public class E621CommentConstructorTest {

    private static Comment comment;

    public E621CommentConstructorTest() throws Exception {
        if (comment != null) return;
        Post post = new Post(PostTest.getDataFromBoorAdvanced(E621.get(), 8595), E621.get());

        String responseData = new HttpsConnection()
                .setRequestMethod(Method.GET)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .openConnection(post.getComments_url()).getResponse();

        JsonParser parser = new JsonParser();
        parser.startParse(responseData);
        comment = new Comment(parser.getResult().get(0));
    }

    @Test
    public void getId_Test() throws Exception {
        assertEquals(428221, comment.getId());
    }

    @Test
    public void getCreatorId_Test() throws Exception {
        assertEquals(10927, comment.getCreator_id());
    }

    @Test
    public void getPostId_Test() throws Exception {
        assertEquals(8595, comment.getPost_id());
    }

    @Test
    public void getBody_Test() throws Exception {
        assertEquals("Score 7!\\r\\nD:", comment.getBody());
    }

    @Test
    public void getCreatedAt_Test() throws Exception {
        assertEquals("2011-03-23 21:18", comment.getCreated_at());
    }

    @Test
    public void getCreatorName_Test() throws Exception {
        assertEquals("BranislavDJ", comment.getCreator_name());
    }
}
