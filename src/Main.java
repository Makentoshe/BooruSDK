import engine.HttpConnection;

/**
 * Created by Makentoshe on 10.07.2017.
 */
public class Main {

    public static void main(String[] args) throws Exception{
        System.out.println(new HttpConnection(false).getRequest("https://sas.com/anus.psa"));
    }
}
