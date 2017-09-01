package test.source.boor.Rule34;


import engine.parser.XmlParser;
import org.junit.Test;
import source.Comment;
import source.Post;
import source.boor.Rule34;
import test.source.TestHelper;

import static org.junit.Assert.assertEquals;

public class Rule34CommentConstructorTest {

    private static Comment comment;

    public Rule34CommentConstructorTest() throws Exception{
        if (comment != null)  return;
        Post post = new Post(TestHelper.getDataFromBoorBasic(Rule34.get(), 2231007), Rule34.get());
        XmlParser parser = new XmlParser();
        parser.startParse(post.getComments_url());
        comment = new Comment(parser.getResult().get(0));
    }

    @Test
    public void getId_Test() throws Exception{
        assertEquals(877622, comment.getId());
    }

    @Test
    public void getCreatorId_Test() throws Exception{
        assertEquals(176668, comment.getCreator_id());
    }

    @Test
    public void getPostId_Test() throws Exception{
        assertEquals(2231007, comment.getPost_id());
    }

    @Test
    public void getBody_Test() throws Exception{
        assertEquals("her face are so adoraball <3", comment.getBody());
    }

    @Test
    public void getCreatedAt_Test() throws Exception{
        //this getCreated_at return time, when data was got.
        //assertEquals("2017-07-27 22:03", comment.getCreated_at());
    }

    @Test
    public void getCreatorName_Test() throws Exception{
        assertEquals("cutehotcatgirl", comment.getCreator_name());
    }
}
