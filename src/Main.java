
import source.Post;
import source.boor.Konachan;
import test.source.PostTest;

public class Main {

    public static void main(String[] args) throws Exception{
        Post post = new Post(PostTest.getDataFromBoorAdvanced(Konachan.get(), 246946), Konachan.get());
        System.out.println(post.getSource());
    }

}
