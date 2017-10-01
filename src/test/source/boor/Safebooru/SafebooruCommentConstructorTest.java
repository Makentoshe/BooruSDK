package test.source.boor.Safebooru;


import engine.parser.XmlParser;
import org.junit.Test;
import source.Comment;
import source.Post;
import source.boor.Safebooru;
import test.source.TestHelper;

import static org.junit.Assert.assertEquals;

public class SafebooruCommentConstructorTest {

    private static Comment comment;

    public SafebooruCommentConstructorTest() throws Exception{
        if (comment != null)  return;
        Post post = new Post(TestHelper.getPostFromBoor(Safebooru.get(), 2244511), Safebooru.get());
        XmlParser parser = new XmlParser();
        parser.startParse(post.getComments_url());
        comment = new Comment(parser.getResult().get(0));
    }

    @Test
    public void getId_Test() throws Exception{
        assertEquals(16084, comment.getId());
    }

    @Test
    public void getCreatorId_Test() throws Exception{
        assertEquals(642, comment.getCreator_id());
    }

    @Test
    public void getPostId_Test() throws Exception{
        assertEquals(2244511, comment.getPost_id());
    }

    @Test
    public void getBody_Test() throws Exception{
        assertEquals("Translation please? Is he finally marrying his own sister after 18 years?", comment.getBody());
    }

    @Test
    public void getCreatedAt_Test() throws Exception{
        //this getCreated_at return time, when data was got.
        //assertEquals("2017-07-27 22:03", comment.getCreated_at());
    }

    @Test
    public void getCreatorName_Test() throws Exception{
        assertEquals("Anonymous", comment.getCreator_name());
    }
}
