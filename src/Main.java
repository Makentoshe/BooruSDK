import engine.HttpConnection;
import source.boor.Gelbooru;

/**
 * Created by Makentoshe on 10.07.2017.
 */
public class Main {

    public static void main(String[] args) throws Exception{
        System.out.println( HttpConnection.getRequest(Gelbooru.get().getCompleteRequest(2, "",0)));
    }
}
