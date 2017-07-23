import source.Post;

/**
 * TODO: create Comment.class and put comments to default Item constructor
 * TODO: for each boor create comment API access
 */
public class Main {

    public static void main(String[] args) {
        Post post = new Post();
        System.out.println(post.getMd5());
        post.setMd5("SAS");
        System.out.println(post.getMd5());
        post.setMd5("ASA");
        System.out.println(post.getMd5());
    }

}
